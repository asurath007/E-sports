package com.art.myknot_gaming.DashBoard;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.CircularArray;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.art.myknot_gaming.AfterPayment.AfterPayment;
import com.art.myknot_gaming.AfterPayment.AfterPaymentAdapter;
import com.art.myknot_gaming.MapList.MapAdapter;
import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.MapList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class CompletedFragment extends Fragment {

    private static final String TAG = "Completed Match";
    private RecyclerView rv;
    private RecyclerView.Adapter rv_adapter;
    private FirebaseFirestore firestoreDB= FirebaseFirestore.getInstance();
    private ListenerRegistration firestoreListener;
    private CollectionReference matchRef = firestoreDB.collection("Match List");
    private SharedPreferences sp2,sp1;
    String id="",sID="",lID="",playerID="";
//    Date currentTime = Calendar.getInstance().getTime();
//    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(new Date());
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("d-M-yyyy");
    String currentDate = df.format(Calendar.getInstance().getTime());
    public CompletedFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        rv = view.findViewById(R.id.f2_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        sp1 = Objects.requireNonNull(getContext()).getSharedPreferences("signupKey", MODE_PRIVATE);
        sID = sp1.getString("value", "");
        sp2 = getContext().getSharedPreferences("loginKey",MODE_PRIVATE);
        lID = sp2.getString("value","");
        playerID = sID+"-"+lID;
        Log.d("AF","id:"+playerID);
        Log.d(TAG,"date:"+currentDate);
        List<MapList> mapLists = new ArrayList<>();

        loadMatchList();
        firestoreListener = matchRef.whereArrayContains("players", playerID).whereLessThan("date",currentDate)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        //delete match tag for one-signal
                        rv_adapter = new MapAdapter(getMatchList, getContext(), firestoreDB);
                        rv.setAdapter(rv_adapter);
                        rv_adapter.notifyDataSetChanged();
                    }
                });
        return view;

    }

    private void loadMatchList() {
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
                } else {
                    Toast.makeText(getContext(), "Error getting Match List", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}