package com.art.myknot_gaming.Registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.art.myknot_gaming.AfterPayment.AfterPayment;
import com.art.myknot_gaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.OneSignal;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Squad extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PaymentResultListener {

    private static final String TAG = "SquadRegistration";
    private EditText et_squad_name;
    private EditText et_squad_1;
    private EditText et_squad_2;
    private EditText et_squad_3;
    private EditText et_squad_4;
    private Button btn_submit;
    private Spinner spinner_squad;
    private ProgressBar progressBar,statusBar;
    private TextView name,map,mode,date,time,type;
    private TextView prizeMoney,moneyBreakUp,entryFee;
    String teamName,player1,player2,player3,player4,slot;
    String id="",paymentStatus="-",uID="";
    String sID = "", lID = "",playerID="";
    private SharedPreferences sp,sp1,sp2;
    private Vibrator vibrator;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Squad Registration");
//  private DocumentReference documentReference = db.collection("Solo Registration").document("Team[]");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squad);

        sp = getSharedPreferences("matchID",MODE_PRIVATE);
        uID = sp.getString("value","");
        sp1 = getSharedPreferences("signupKey", MODE_PRIVATE);
        sID = sp1.getString("value", "");
        sp2 = getSharedPreferences("loginKey",MODE_PRIVATE);
        lID = sp2.getString("value","");
        playerID = sID+"-"+lID;

        name = findViewById(R.id.tv_squad_match_title);
        map =findViewById(R.id.tv_squad_map);
        mode= findViewById(R.id.tv_squad_mode);
        date= findViewById(R.id.tv_squad_date);
        time= findViewById(R.id.tv_squad_time);
        prizeMoney= findViewById(R.id.tv_squad_prizeMoney);
        moneyBreakUp = findViewById(R.id.tv_squad_moneyBreakUp);
        entryFee=findViewById(R.id.tv_squad_entryFee);
        statusBar=findViewById(R.id.match_status);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        et_squad_name = findViewById(R.id.et_squad_name);
        et_squad_1 = findViewById(R.id.et_squad_1);
        et_squad_2 = findViewById(R.id.et_squad_2);
        et_squad_3 = findViewById(R.id.et_squad_3);
        et_squad_4 = findViewById(R.id.et_squad_4);
        btn_submit = findViewById(R.id.btn_squad_submit);
        spinner_squad = findViewById(R.id.spinner_squad);
        progressBar =findViewById(R.id.squad_progressBar);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.slot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_squad.setAdapter(adapter);

        spinner_squad.setOnItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            name.setText(bundle.getString("name"));
            map.setText(bundle.getString("map"));
            mode.setText(bundle.getString("mode"));
            date.setText(bundle.getString("date"));
            time.setText(bundle.getString("time"));
            entryFee.setText(bundle.getString("ef"));
            prizeMoney.setText(bundle.getString("pm"));
            moneyBreakUp.setText(bundle.getString("mbu"));
            Log.d("mapID","map:"+bundle.getString("map"));
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_squad_name.getText().toString())
                        && !TextUtils.isEmpty(et_squad_1.getText().toString())
                        && !TextUtils.isEmpty(et_squad_2.getText().toString())
                        && !TextUtils.isEmpty(et_squad_3.getText().toString())
                        && !TextUtils.isEmpty(et_squad_4.getText().toString())) {
                vibrations(0);
                progressBar.setVisibility(View.VISIBLE);

                teamName = et_squad_name.getText().toString().trim();
                player1 = et_squad_1.getText().toString().trim();
                player2 = et_squad_2.getText().toString().trim();
                player3 = et_squad_3.getText().toString().trim();
                player4 = et_squad_4.getText().toString().trim();
                slot = spinner_squad.getSelectedItem().toString();

                Map<String, String> player = new HashMap<>();
                player.put("ID",playerID);
                player.put("teamName",teamName);
                player.put("player1",player1);
                player.put("player2",player2);
                player.put("player3",player3);
                player.put("player4",player4);
                player.put("slotBooked",slot);
                player.put("payment",paymentStatus);

                collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Squad.this, "Registration Failed",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Squad.this, "\t\tComplete Payment to \n confirm your participation",
                                Toast.LENGTH_SHORT).show();
                        id = documentReference.getId();
                        Log.d("playerID","sid:"+sID+"\nlid:"+lID+"\ndoc:"+id+"\nplayer1:"+player1+"\nuID:"+uID);
                        startPayment();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }else {
                Toast.makeText(Squad.this, "Empty fields not allowed",
                        Toast.LENGTH_LONG).show();
                vibrations(1);
            }
        }
        });
        //getting player count
        getPlayerCount();
            }

    private void getPlayerCount() {
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int count=0;
                    if (count<100){
                        for (DocumentSnapshot snapshot : Objects.requireNonNull(task.getResult())) {
                            count=count+1;
                        }
//                        Toast.makeText(Squad.this, "Participants joined  "+count,Toast.LENGTH_SHORT).show();
                        statusBar.setProgress(count);
                    }else{
                        Toast.makeText(Squad.this,"Slot Filled",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "Error getting participant list ", task.getException());
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        String text = parent.getSelectedItem().toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void startPayment() {

        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "MyKnot Gaming");
            options.put("description", "Registration Fee");
            //You can omit the image option to fetch the image from dashboard
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

            String payment = entryFee.getText().toString();
            double total = Double.parseDouble(payment);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
//            preFill.put("email", "axe.nexas@gmail.com");
//            preFill.put("contact", "9853837232");
//
//            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
//        Toast.makeText(this, "Payment successfully done! " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Payment successfully done! ", Toast.LENGTH_SHORT).show();
        //clear docID passed
         sp.edit().clear().apply();
        try {
            //creating array of players in the match detail list
            db.collection("Match List").document(uID).update("players", FieldValue.arrayUnion(playerID));

            Intent intent = new Intent(Squad.this, AfterPayment.class);
            intent.putExtra("id",uID);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Toast.makeText(Squad.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        //change status in DB
        Map<String, Object> team = new HashMap<>();
        team.put("ID",playerID);
        team.put("teamName",teamName);
        team.put("player1",player1);
        team.put("player2",player2);
        team.put("player3",player3);
        team.put("player4",player4);
        team.put("slotBooked",uID);
        team.put("payment", "paid");

        collectionReference.document(id).set(team)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("SquadID","AfterPaymentID: "+id);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Squad.this, "Updating Payment Status failed\nPlease check after sometime", Toast.LENGTH_LONG).show();
            }
        });
        //subscribe to message channel
        JSONObject tags = new JSONObject();
        try {
            tags.put(uID,true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OneSignal.sendTags(tags);

    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment error please try again", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    } public void vibrations(int level){
        switch (level){
            case 0:
                assert vibrator != null;
                vibrator.vibrate(25);
                break;
            case 1:
                assert vibrator != null;
                vibrator.vibrate(50);
                break;
        }
    }
}
