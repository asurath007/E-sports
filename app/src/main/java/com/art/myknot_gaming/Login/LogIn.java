package com.art.myknot_gaming.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.art.myknot_gaming.Admin.AdminAddMatch;
import com.art.myknot_gaming.Admin.AdminLogin;
import com.art.myknot_gaming.Admin.IntroAdmin;
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
    TextView host_link,login_consent,reset_link;
    SharedPreferences sp;
    CheckBox login_check;
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

        reset_link = findViewById(R.id.reset_link);
        login_check = findViewById(R.id.login_check);
        login_consent= findViewById(R.id.login_consent);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.btn_login);
        host_link = findViewById(R.id.host_link);
        login_progressBar = findViewById(R.id.login_progressBar);

        btn_create_account = findViewById(R.id.btn_create_account);

        //set textview to open website
        btn_login.setEnabled(false);
        String text = "I agree with the Privacy Policy and Terms of Service of MyKnot Gaming Services";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://myknot-pubggaming.online/public/privacy.html")));
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://myknot-pubggaming.online/public/terms.html")));
            }
        };
        ss.setSpan(clickableSpan1,17,31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2,36,52,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        login_consent.setText(ss);
        login_consent.setMovementMethod(LinkMovementMethod.getInstance());

        sp = getSharedPreferences("login", MODE_PRIVATE);
        //check if previously logged
        if (sp.getBoolean("logged", false)) {
            skipLogin();
        }

        //enable login when checkBox is clicked
        login_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    btn_login.setEnabled(true);
                    btn_login.setBackgroundColor(getColor(R.color.colorPrimaryLight));
                }else{
                    btn_login.setEnabled(false);
                    btn_login.setBackgroundColor(getColor(R.color.colorTextSecondary));
                    Toast.makeText(LogIn.this, "Please agree with our Privacy Policy & Terms of Service to continue", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String pwd = login_password.getText().toString().trim();

//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(LogIn.this, "Invalid email", Toast.LENGTH_SHORT).show();
//                }
//                if (TextUtils.isEmpty(pwd)) {
//                    Toast.makeText(LogIn.this, "Invalid password", Toast.LENGTH_SHORT).show();
//                }
                if (TextUtils.isEmpty(email)||(TextUtils.isEmpty(pwd))){
                    Toast.makeText(LogIn.this, "Please enter Email & Password", Toast.LENGTH_LONG).show();
                }
                else {
                    loginEmailPasswordUser(email, pwd);
                }
                //hide keyboard
//                InputMethodManager inputManager = (InputMethodManager)
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                assert inputManager != null;
//                inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });

        //host page linking
        host_link.setClickable(true);
        host_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, AdminLogin.class));
            }
        });
        //reset-link
        reset_link.setClickable(true);
        reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LogIn.this, "Enter email to send reset password link", Toast.LENGTH_LONG).show();
                }else{
                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                Toast.makeText(LogIn.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            }else{
                                    Toast.makeText(LogIn.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
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
                    //skip login condition
                    sp.edit().putBoolean("logged", true).apply();
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
