package com.art.myknot_gaming.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Login.LogIn;
import com.art.myknot_gaming.Login.SignUp;
import com.art.myknot_gaming.R;
import com.art.myknot_gaming.Util.UserDetail;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.onesignal.ADMMessageHandler;

public class AdminLogin extends AppCompatActivity {
    EditText login_email;
    EditText login_password;
    Button btn_login,btn_create;
    ProgressBar login_progressBar;
    SharedPreferences sp;
    ImageView pwd_img;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private String TAG = "AdminLogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        firebaseAuth = FirebaseAuth.getInstance();
        login_email = findViewById(R.id.login_admin_email);
        login_password = findViewById(R.id.login_admin_password);
        btn_login = findViewById(R.id.btn_admin_login);
        btn_create = findViewById(R.id.btn_create_admin_account);
        login_progressBar = findViewById(R.id.login_admin_progressBar);
        pwd_img = findViewById(R.id.admin_pwd_img);


        //sp = getSharedPreferences("admin", MODE_PRIVATE);
        //check if previously logged
        //if (sp.getBoolean("adminLogged", false)) {
           // skipLogin();
        //}
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminLogin.this, IntroAdmin.class));
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim() + "@myknot.com";
                String pwd = login_password.getText().toString().trim();
                Log.d(TAG, "email: "+ email);
                if (TextUtils.isEmpty(email)||TextUtils.isEmpty(pwd)) {
                    Toast.makeText(AdminLogin.this, "Empty Fields not allowed", Toast.LENGTH_SHORT).show();
                }else {
                    loginEmailPasswordUser(email, pwd);
                }
                //hide keyboard
//                InputMethodManager inputManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                assert inputManager != null;
//                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

                //skip login condition
//                sp.edit().putBoolean("adminLogged", true).apply();
            }
        });
    }

    private void loginEmailPasswordUser(String email, String pwd) {
        login_progressBar.setVisibility(View.VISIBLE);


//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){
        {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
//                            final String currentUserId = user.getUid();

                            collectionReference
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                            login_progressBar.setVisibility(View.INVISIBLE);

                                            if (e != null) {

                                            }
                                            assert queryDocumentSnapshots != null;
                                            if (!queryDocumentSnapshots.isEmpty()) {

                                                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {

                                                    UserDetail ud = UserDetail.getInstance();
                                                    ud.setEmail(snapshot.getString("userEmail"));
                                                    ud.setUserName(snapshot.getString("userName"));
//                                                    ud.setId(snapshot.getId());
                                                    Log.d("KEY", "ALID:" + snapshot.getId());
//                                                    Log.d("signinID", "id:"+currentUserId);
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //next activity
                    startActivity(new Intent(AdminLogin.this, AdminAddMatch.class));
                    finish();
                    login_progressBar.setVisibility(View.INVISIBLE);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AdminLogin.this, "Admin Account not found",
                                    Toast.LENGTH_LONG).show();

                            login_progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    public void showHidePass(View view) {
        if(view.getId()==R.id.admin_pwd_img){
            if (login_password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                pwd_img.setImageResource(R.drawable.ic_baseline_lock_open_36);
                login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                pwd_img.setImageResource(R.drawable.ic_baseline_lock_36);
                login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
//        private void skipLogin () {
//            Intent prevLogIn = new Intent(getApplicationContext(), AdminAddMatch.class);
//            startActivity(prevLogIn);
//            finish();
//        }
}
