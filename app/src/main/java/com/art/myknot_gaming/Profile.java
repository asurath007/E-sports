package com.art.myknot_gaming;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.art.myknot_gaming.Admin.AdminAddMatch;
import com.art.myknot_gaming.Admin.AdminAddMatchDetail;
import com.art.myknot_gaming.DashBoard.DashBoard;
import com.art.myknot_gaming.Login.LogIn;
import com.art.myknot_gaming.Notifications.Notifications;
import com.art.myknot_gaming.Util.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Profile extends AppCompatActivity {

    private BottomNavigationView nav_bar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private Vibrator vibrator;
    private Context ctx;
    private CollectionReference userRef = firestoreDB.collection("Users");

    private EditText firstName, lastName, mobile, pubgUsername, codUsername, game3Username;
    private Button btn_prof_edit;
    private FloatingActionButton btn_prof_save,btn_prof_logout;
    private TextView userEmail,userName, tv_fn,tv_ln,tv_pun,tv_cun,tv_gun,tv_mobile;
    private ProgressBar prof_progress;
    private ImageView img_prof;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firstName = findViewById(R.id.prof_first_name);
        lastName = findViewById(R.id.prof_last_name);
        pubgUsername = findViewById(R.id.pubg_username);
        codUsername = findViewById(R.id.cod_username);
        mobile = findViewById(R.id.prof_mobile);
        game3Username = findViewById(R.id.game3_username);
        btn_prof_edit = findViewById(R.id.btn_prof_edit);
        btn_prof_logout = findViewById(R.id.btn_prof_logout);
        btn_prof_save = findViewById(R.id.btn_prof_save);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ImageView iv_prof = findViewById(R.id.iv_prof);

        userEmail = findViewById(R.id.prof_email);
        userName = findViewById(R.id.prof_username);
        tv_fn = findViewById(R.id.tv_first_name);
        tv_ln = findViewById(R.id.tv_last_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_pun = findViewById(R.id.tv_pubg_username);
        tv_cun = findViewById(R.id.tv_cod_username);
        tv_gun = findViewById(R.id.tv_game3_username);
        prof_progress = findViewById(R.id.prof_progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        final CollectionReference userRef = firestoreDB.collection("Users");

        loadUserDetails();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(), LogIn.class));
        }
//        String uid = user.getUid();
        //getting doc id from login
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginKey", MODE_PRIVATE);
        final String uID = sharedPreferences.getString("value","");
        Log.d("Prof","id: "+uID);


        nav_bar = findViewById(R.id.prof_nav_bar);
        nav_bar.setSelectedItemId(R.id.navigation_profile);
        nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_home:
                        startActivity(new Intent(Profile.this, HomeActivity.class));finish();
                        break;
                    case R.id.navigation_dashboard:
                        startActivity(new Intent(Profile.this, DashBoard.class));finish();
                        break;
                    case R.id.navigation_notifications:
                        startActivity(new Intent(Profile.this, Notifications.class));finish();
                        break;
                    case R.id.navigation_profile:
//                        Toast.makeText(Profile.this,"Profile Page", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        btn_prof_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting fields editable on button click and display save button
                Log.d("prof","edit");
                editDetails();
//                btn_prof_save.setVisibility(View.VISIBLE);
//                btn_prof_edit.setVisibility(View.INVISIBLE);
            }
        });

        btn_prof_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* display confirmation popup */
                Log.d("prof","log_out");
                vibrations(1);
                logoutPopup();
            }
        });
        btn_prof_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrations(0);
                //UPDATE & save to db
                Log.d("prof","save");
                saveDetails();

//                refreshDetails();
                prof_progress.setVisibility(View.VISIBLE);

                //update
                String nfn= firstName.getText().toString().trim();
                String nln= lastName.getText().toString().trim();
                String pun = pubgUsername.getText().toString().trim();
                String cun = codUsername.getText().toString().trim();
                String un = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String no = mobile.getText().toString().trim();
                String id = userID;

                final Map<String,Object> updateUser = new UserDetail(email,no,id,un,nfn,nln,pun,cun).newUser();
                userRef.document(uID).set(updateUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Profile.this, "Profile Details Updated", Toast.LENGTH_SHORT).show();
                                prof_progress.setVisibility(View.INVISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "Updating Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void logoutPopup() {
        dialogBuilder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.delete_dialog,null);

        TextView pop_title = v.findViewById(R.id.tv_alert);
        TextView pop_detail = v.findViewById(R.id.tv_alert2);
        Button yesButton = v.findViewById(R.id.btn_yes);
        Button noButton = v.findViewById(R.id.btn_no);

        dialogBuilder.setView(v);
        dialog = dialogBuilder.create();
        dialog.show();

        pop_title.setText("Do you want to log out ?");
        pop_detail.setText("You won't be able to receive notifications any more");
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View vi) {
                // log out user from app
                SharedPreferences sp = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
                sp.edit().putBoolean("logged",false).apply();
                // change notification status
                JSONObject tags = new JSONObject();
                try {
                    tags.put("logged_in",false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OneSignal.sendTags(tags);
                OneSignal.getTags(new OneSignal.GetTagsHandler() {
                    @Override
                    public void tagsAvailable(JSONObject tags) {
                        OneSignal.deleteTag("key1");
                        OneSignal.deleteTag("matchID");
                    }
                });
                if ((dialog!=null)&&(dialog.isShowing())) {
                    dialog.dismiss();
                }
                //show login screen
                Intent intent = new Intent(Profile.this, LogIn.class);
//                        intent.putExtra("logged",false);
                startActivity(intent);finish();
                Toast.makeText(Profile.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void loadUserDetails() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("loginKey", MODE_PRIVATE);
        String uID = sharedPreferences.getString("value","");
        userRef.document(uID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    assert doc != null;
                    //tv field to get data
                    userName.setText(doc.getString("userName"));
                    userEmail.setText(doc.getString("userEmail"));
                    tv_mobile.setText(doc.getString("Mobile No"));
                    tv_fn.setText(doc.getString("FirstName"));
                    tv_ln.setText(doc.getString("LastName"));
                    tv_pun.setText(doc.getString("PUBG"));
                    tv_cun.setText(doc.getString("COD"));
                    userID = (doc.getString("userId"));
                    //et fields to get data
                    firstName.setText(doc.getString("FirstName"));
                    mobile.setText(doc.getString("Mobile No"));
                    lastName.setText(doc.getString("LastName"));
                    pubgUsername.setText(doc.getString("PUBG"));
                    codUsername.setText(doc.getString("COD"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profile.this, "Failed getting User Data",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveDetails() {
        loadUserDetails();
        firstName.setVisibility(View.GONE);
        tv_fn.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.GONE);
        tv_ln.setVisibility(View.VISIBLE);
        pubgUsername.setVisibility(View.GONE);
        tv_pun.setVisibility(View.VISIBLE);
        codUsername.setVisibility(View.GONE);
        tv_cun.setVisibility(View.VISIBLE);
        game3Username.setVisibility(View.GONE);
        tv_gun.setVisibility(View.VISIBLE);
        tv_mobile.setVisibility(View.VISIBLE);
        mobile.setVisibility(View.GONE);

        //button animations
        btn_prof_save.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_out));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_prof_save.setVisibility(View.GONE);
                btn_prof_save.setClickable(false);
                btn_prof_logout.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_in));
                btn_prof_edit.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_in));
                btn_prof_edit.setVisibility(View.VISIBLE);
                btn_prof_logout.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void editDetails() {
        firstName.setVisibility(View.VISIBLE);
        tv_fn.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.VISIBLE);
        tv_ln.setVisibility(View.INVISIBLE);
        pubgUsername.setVisibility(View.VISIBLE);
        tv_pun.setVisibility(View.INVISIBLE);
        codUsername.setVisibility(View.VISIBLE);
        tv_cun.setVisibility(View.INVISIBLE);
        game3Username.setVisibility(View.VISIBLE);
        tv_gun.setVisibility(View.INVISIBLE);
        tv_mobile.setVisibility(View.INVISIBLE);
        mobile.setVisibility(View.VISIBLE);

        //button animations
        btn_prof_logout.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_out));
        btn_prof_edit.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_out));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_prof_logout.setVisibility(View.GONE);
                btn_prof_edit.setVisibility(View.GONE);
                btn_prof_save.startAnimation(AnimationUtils.loadAnimation(Profile.this, R.anim.splash_in));
                btn_prof_save.setVisibility(View.VISIBLE);
                btn_prof_save.setClickable(true);
            }
        }, 500);
    }
    public void vibrations(int level){
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
