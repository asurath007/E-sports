package com.art.myknot_gaming.Admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.art.myknot_gaming.Util.MapList;
import com.art.myknot_gaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminAddMatch extends AppCompatActivity {

    private static final String TAG = "Admin Add Match";
    private static final String[] MAPS = new String[]{"ERANGEL", "MIRAMAR", "SANHOK", "VIKENDI", "TDM"};
    private static final String[] MODES = new String[]{"TPP","FPP"};
    private static final String[] TYPE = new String[]{"SOLO","DUO","SQUAD"};
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mapAdapter;
    private List<MapList> mapLists;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText et_title,et_date,et_time,et_prizeMoney,et_entryFee,et_moneyBreakUp;
    private AutoCompleteTextView et_map,et_mode,et_type;
    private Button btn_date, btn_time;
//    private ProgressBar match_status;
    private String id = "";

    private int mDate,mMonth,mYear,mHour,mMinute;


    private ListenerRegistration firestoreListener;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference matchRef = firestoreDB.collection("Match List");
//    private DocumentReference soloList = firestoreDB.document("Match List/Solo Match/Match List");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_match);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp();
            }
        });

        recyclerView = findViewById(R.id.rv_add_match);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get items from db
        loadMatchFromDB();

        firestoreListener = matchRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }

                        List<MapList> getMatchList = new ArrayList<>();

                        assert queryDocumentSnapshots != null;
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            MapList match = doc.toObject(MapList.class);
                            assert match != null;
                            match.setId(doc.getId());
                            getMatchList.add(match);
                        }
                        mapAdapter = new AdminMapAdapter(getMatchList, getApplicationContext(), firestoreDB);
                        recyclerView.setAdapter(mapAdapter);
                        mapAdapter.notifyDataSetChanged();
                    }
                });

    }


    @SuppressLint("SetTextI18n")
    private void popUp() {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.match_edit_popup, null);
        et_title =view.findViewById(R.id.et_title);
        et_map = view.findViewById(R.id.et_map);
        et_mode =view.findViewById(R.id.et_mode);
        et_date = view.findViewById(R.id.et_date);
        et_time = view.findViewById(R.id.et_time);
        et_prizeMoney = view.findViewById(R.id.et_prizeMoney);
        et_entryFee = view.findViewById(R.id.et_entryFee);
        et_type= view.findViewById(R.id.et_type);
        et_moneyBreakUp = view.findViewById(R.id.et_moneyBreakUp);
        btn_date = view.findViewById(R.id.btn_date);
        btn_time =view.findViewById(R.id.btn_time);

//        match_status = view.findViewById(R.id.match_status);

        //creating drop-down for map,mode,type
        ArrayAdapter<String> mapDropDown = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MAPS);
        et_map.setAdapter(mapDropDown);
        et_map.setThreshold(1);

        ArrayAdapter<String> modeDropDown = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MODES);
        et_mode.setAdapter(modeDropDown);
        et_mode.setThreshold(0);

        ArrayAdapter<String> typeDropDown = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, TYPE);
        et_type.setAdapter(typeDropDown);
        et_type.setThreshold(0);

        //adding timePicker & datePicker
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog mDatePicker = new DatePickerDialog(AdminAddMatch.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        newDate.set(Calendar.MONTH,month);
                        newDate.set(Calendar.YEAR,year);
                        et_date.setText(new StringBuilder().append(dayOfMonth).append("-").append(month+1).append("-").append(year));
                        mMonth=month+1;
                    }
                },mYear, mMonth, mDate);
                mDatePicker.setTitle("Set Match Date");
                /** Hide Future Date Here**/
                // mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                /** Hide Past Date Here**/
                mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                mDatePicker.show();
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog mTimePicker = new TimePickerDialog(AdminAddMatch.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar newTime = Calendar.getInstance();
                        newTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        newTime.set(Calendar.MINUTE, minute);

                        mHour = hourOfDay;
                        String am_pm;
                        if (mHour>12){
                            mHour = mHour-12;
                            am_pm = "PM";
                        }else{
                            am_pm = "AM";
                        }
                        et_time.setText(new StringBuilder().append(mHour).append(":").append(minute).append(" ").append(am_pm));
                    }
                },mHour,mMinute,false);
                mTimePicker.setTitle("Set Match Time");
                mTimePicker.show();
            }
        });

        Button btn_save = view.findViewById(R.id.btn_save_match_edit);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                //Save to DataBase & go to main screen
                if (et_title.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_entryFee.getText().toString().isEmpty() || et_date.getText().toString().isEmpty() ||
                        et_map.getText().toString().isEmpty() || et_mode.getText().toString().isEmpty() ||
                        et_prizeMoney.getText().toString().isEmpty() || et_time.getText().toString().isEmpty() ){
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT).show();
                }else{
                    saveMatchToDB(v);
                }
            }
        });
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void saveMatchToDB(View v) {
        MapList newMatch = new MapList();

        String newName = et_title.getText().toString();
        String newMap = et_map.getText().toString();
        String newMode = et_mode.getText().toString();
        String newDate = et_date.getText().toString();
        String newTime = et_time.getText().toString();
        String newPrizeMoney = et_prizeMoney.getText().toString();
        String newEntryFee = et_entryFee.getText().toString();
        String newType = et_type.getText().toString();
        String newMoneyBreakUp = et_moneyBreakUp.getText().toString();
//        int newProgress = match_status.getProgress();
        String newId = id;

        newMatch.setName(newName);
        newMatch.setMap(newMap);
        newMatch.setMode(newMode);
        newMatch.setDate(newDate);
        newMatch.setTime(newTime);
        newMatch.setPrizeMoney(newPrizeMoney);
        newMatch.setEntryFee(newEntryFee);
        newMatch.setType(newType);
        newMatch.setMoneyBreakUp(newMoneyBreakUp);
//        newMatch.setProgress(newProgress);
        newMatch.setId(newId);  //passing doc id as id to retrieve in user side

        //save to db
        Map<String, Object> Match = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee,newMoneyBreakUp,newType,newId).newMatch();

        firestoreDB.collection("Match List").add(Match)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AdminAddMatch.this, "Match has been Added", Toast.LENGTH_SHORT).show();
                        id= documentReference.getId();
                        Log.d("AdminID","ID:"+id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminAddMatch.this, "Match adding Failed", Toast.LENGTH_SHORT).show();
            }
        });

        //update id
//        firestoreDB.collection("Match List").document(id).update(Match);


        Snackbar.make(v,"Match Details Saved", Snackbar.LENGTH_SHORT).show();

        //adding data to sub-collections
        if (et_type.getText().toString().equals("SOLO")){
            Map<String, Object> SoloMatch = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee,newMoneyBreakUp,newType).newMatch();
            firestoreDB.collection("Match List").document("Solo Match")
                    .collection("Match List").add(SoloMatch)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminAddMatch.this, "Match has been Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddMatch.this, "Match adding Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (et_type.getText().toString().equals("DUO")){
            Map<String, Object> DuoMatch = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee,newMoneyBreakUp,newType).newMatch();
            firestoreDB.collection("Match List").document("Duo Match")
                    .collection("Match List").add(DuoMatch)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminAddMatch.this, "Match has been Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddMatch.this, "Match adding Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (et_type.getText().toString().equals("SQUAD")){
            Map<String, Object> SquadMatch = new MapList(newName,newMap,newMode,newDate,newTime,newPrizeMoney,newEntryFee,newMoneyBreakUp,newType).newMatch();
            firestoreDB.collection("Match List").document("Solo Match")
                    .collection("Match List").add(SquadMatch)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(AdminAddMatch.this, "Match has been Added", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminAddMatch.this, "Match adding Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                //start new Activity
                startActivity(new Intent(AdminAddMatch.this, AdminAddMatch.class));
                finish();
            }
        },1000);
    }

    private void loadMatchFromDB() {
        matchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    List<MapList> getMatchList = new ArrayList<>();

                    for (DocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {
                        MapList match = doc.toObject(MapList.class);
                        assert match != null;
                        match.setId(doc.getId());
                        getMatchList.add(match);
                    }
                    mapAdapter = new AdminMapAdapter(getMatchList, getApplicationContext(), firestoreDB);
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setAdapter(mapAdapter);
                    mapAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminAddMatch.this, "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    protected void onDestroy() {
        super.onDestroy();

        firestoreListener.remove();
    }

}
