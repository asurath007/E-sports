package com.art.myknot_gaming.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.art.myknot_gaming.Admin.AdminAddMatch;
import com.art.myknot_gaming.HomeActivity;
import com.art.myknot_gaming.Main2Activity;
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

public class LogIn extends AppCompatActivity {
    EditText login_email;
    EditText login_password;
    Button btn_login;
    Button btn_create_account;
    ProgressBar login_progressBar;
    TextView host_link;
    SharedPreferences sp;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private CollectionReference googleCR = db.collection("Google Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        firebaseAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("login", MODE_PRIVATE);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.btn_login);
        host_link = findViewById(R.id.host_link);
        login_progressBar = findViewById(R.id.login_progressBar);

        btn_create_account = findViewById(R.id.btn_create_account);

        //check if previously logged
        if (sp.getBoolean("logged", false)) {
            skipLogin();
        }

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEmailPasswordUser(login_email.getText().toString().trim(),
                        login_password.getText().toString().trim());

                //hide keyboard
//                InputMethodManager inputManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                assert inputManager != null;
//                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

                //skip login condition
                sp.edit().putBoolean("logged", true).apply();
            }
        });

        //host page linking
        host_link.setClickable(true);
        host_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, AdminAddMatch.class));
            }
        });

    }

    private void skipLogin() {
//        Intent prevLogIn = new Intent(getApplicationContext(), HomeActivity.class);
        Intent prevLogIn = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(prevLogIn);
        finish();
    }

    private void loginEmailPasswordUser(String email, String pwd) {

        login_progressBar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LogIn.this, "Invalid email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LogIn.this, "Invalid password", Toast.LENGTH_SHORT).show();
        }

//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)){
        {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();

                            collectionReference.whereEqualTo("userId", currentUserId)
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
                                                    ud.setId(snapshot.getId());
                                                    Log.d("KEY","LID:"+snapshot.getId());
                                                    //pass id to fetch prof info
                                                    SharedPreferences sharedPref = getSharedPreferences("loginKey", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("value", snapshot.getId());
                                                    editor.apply();
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
                    startActivity(new Intent(LogIn.this, HomeActivity.class));
                    finish();
                    login_progressBar.setVisibility(View.INVISIBLE);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LogIn.this, "User not found",
                                    Toast.LENGTH_LONG).show();

                            login_progressBar.setVisibility(View.INVISIBLE);

                        }
                    });
//        }else {
//            Toast.makeText(LoginActivity.this, "Please fill all fields",
//                    Toast.LENGTH_LONG).show();
        }

    }
}
