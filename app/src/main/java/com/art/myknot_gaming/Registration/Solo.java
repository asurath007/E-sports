package com.art.myknot_gaming.Registration;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.art.myknot_gaming.Util.MapList;
import com.art.myknot_gaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Solo extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PaymentResultListener {

    private static final String TAG = "SoloRegistration";
    private EditText et_solo;
    private Button btn_submit;
    private Spinner spinner_solo;
    private ProgressBar progressBar,statusBar;
    private List<MapList> mapList;
    private SharedPreferences sp,sp1,sp2;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView name,map,mode,date,time,type;
    private TextView prizeMoney,moneyBreakUp,entryFee;
    String id = "",uID="",paymentStatus="-",player1="",slot;
    String sID = "", lID = "",playerID="";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Solo Registration");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solo);

        firebaseAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("matchID",MODE_PRIVATE);
        uID = sp.getString("value","");
        sp1 = getSharedPreferences("signupKey", MODE_PRIVATE);
        sID = sp1.getString("value", "");
        sp2 = getSharedPreferences("loginKey",MODE_PRIVATE);
        lID = sp2.getString("value","");
        playerID = sID+"-"+lID;

        name = findViewById(R.id.tv_solo_match_title);
        map =findViewById(R.id.tv_solo_map);
        mode= findViewById(R.id.tv_solo_mode);
        date= findViewById(R.id.tv_solo_date);
        time= findViewById(R.id.tv_solo_time);
        prizeMoney= findViewById(R.id.tv_solo_prizeMoney);
        moneyBreakUp = findViewById(R.id.tv_solo_moneyBreakUp);
        entryFee=findViewById(R.id.tv_solo_entryFee);
        statusBar = findViewById(R.id.solo_match_status);

        et_solo = findViewById(R.id.et_solo);
        btn_submit = findViewById(R.id.btn_solo_submit);
        spinner_solo = findViewById(R.id.spinner_solo);
        progressBar = findViewById(R.id.solo_progressBar);


        //Slot Booking dropdown
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.slot_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_solo.setAdapter(adapter);

        spinner_solo.setOnItemSelectedListener(this);

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
//            uID =bundle.getString("ID");
            //Log.d("mapID","map:"+bundle.getString("map"));
        }
        slot = name.getText().toString();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_solo.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);

                    player1 = et_solo.getText().toString().trim();
                    String slotBooked = spinner_solo.getSelectedItem().toString();

                    Map<String, String> player = new HashMap<>();
                    //put sID||lID & retrieve while sorting on user side list of matches
                    player.put("ID", playerID);
                    player.put("player1", player1);
                    player.put("slotBooked", slotBooked);
                    player.put("payment",paymentStatus);

                    collectionReference.add(player).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Solo.this, "Registration Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            id = documentReference.getId();
                            Log.d("playerID","sid:"+sID+"\nlid:"+lID+"\ndoc:"+id+"\nplayer1:"+player1+"\nuID:"+uID);
                            Toast.makeText(Solo.this, "\t\tComplete Payment to \n confirm your participation",
                                    Toast.LENGTH_SHORT).show();
                            startPayment();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    Toast.makeText(Solo.this, "Empty fields not allowed",
                            Toast.LENGTH_LONG).show();
                }
            }

    });

        //set progress bar
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
//                        Toast.makeText(Solo.this, "Participants joined = "+count,Toast.LENGTH_SHORT).show();
                        statusBar.setProgress(count);
                    }else{
                        Toast.makeText(Solo.this,"Slot Filled",Toast.LENGTH_SHORT).show();
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
//        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
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
            db.collection("Match List").document(uID).update("players",FieldValue.arrayUnion(playerID));

            Intent intent = new Intent(Solo.this, AfterPayment.class);
            intent.putExtra("id",uID);
            startActivity(intent);
            finish();
        }catch (Exception e){
            Toast.makeText(Solo.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        //change status in DB
        Map<String, String> team = new HashMap<>();
        team.put("ID", playerID);
        team.put("player1",player1);
        team.put("slotBooked",uID);
        team.put("payment", "paid");
        collectionReference.document(id).set(team)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Solo.this, "Updating Payment Status failed\nPlease check after sometime", Toast.LENGTH_LONG).show();
            }
        });
//        //subscribe to message channel
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
    }
}
