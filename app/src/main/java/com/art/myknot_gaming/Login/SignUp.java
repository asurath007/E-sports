package com.art.myknot_gaming.Login;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText signUp_username;
    private EditText signUp_email;
    private EditText signUp_password;
    private Button btn_signUp;
    private ProgressBar signUp_progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp_username = findViewById(R.id.signup_username);
        signUp_email = findViewById(R.id.signup_email);
        signUp_password = findViewById(R.id.signup_password);
        signUp_progressBar = findViewById(R.id.signup_progressBar);

        btn_signUp = findViewById(R.id.btn_signup);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null){
                    //user already logged in
                }else{
                    //no user yet
                }
            }
        };

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(signUp_username.getText().toString())
                        && !TextUtils.isEmpty(signUp_password.getText().toString())
                        && !TextUtils.isEmpty(signUp_email.getText().toString())){

                    String email = signUp_email.getText().toString().trim();
                    String password = signUp_password.getText().toString().trim();
                    String username = signUp_username.getText().toString().trim();

                    createUserEmailAccount(email,password,username);

                    //hide keyboard
//                    InputMethodManager inputManager = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    assert inputManager != null;
//                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    Toast.makeText(SignUp.this, "Empty fields not allowed",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private  void createUserEmailAccount(final String email, String password, final String username){
        if (TextUtils.isEmpty(email)){Toast.makeText(SignUp.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();}
        if (TextUtils.isEmpty(password)){Toast.makeText(SignUp.this, "Please enter a valid password", Toast.LENGTH_SHORT).show();}
        if (password.length()<6){Toast.makeText(SignUp.this, "Please enter a strong password", Toast.LENGTH_SHORT).show();}
        if (TextUtils.isEmpty(username)){Toast.makeText(SignUp.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();}
        {

            signUp_progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //take user to next page
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();

                                //map to create collection in db
//                                Map<String , String> userObj = new HashMap<>();
//                                userObj.put("userId",currentUserId);
//                                userObj.put("userName", username);
//                                userObj.put("userEmail",email);
                                UserDetail userObj = new UserDetail();
                                userObj.setEmail(email);
                                userObj.setUserName(username);
                                userObj.setId(currentUserId);
                                Map<String,Object> User = new UserDetail( email, username,currentUserId).newUser();

                                //save to db
                                collectionReference.add(User)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(final DocumentReference documentReference) {
                                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (Objects.requireNonNull(task.getResult()).exists()){
                                                            signUp_progressBar.setVisibility(View.INVISIBLE);

                                                            String name = task.getResult().getString("userName");
                                                            //passing doc id
//                                                            String id = (documentReference.getId());
//                                                            Log.d("KEY","SID:"+id);
//                                                            SharedPreferences sharedPref = getSharedPreferences("signupKey", MODE_PRIVATE);
//                                                            SharedPreferences.Editor editor = sharedPref.edit();
//                                                            editor.putString("value", id);
//                                                            editor.apply();

                                                            UserDetail ud = UserDetail.getInstance();
                                                            ud.setUserName(name);
                                                            ud.setEmail(email);
                                                            ud.setId((documentReference.getId()));

                                                            Intent intent = new Intent(SignUp.this, LogIn.class);
                                                            intent.putExtra("userName", name);
                                                            intent.putExtra("userId", currentUserId);
                                                            intent.putExtra("userEmail",email);
                                                            startActivity(intent);
                                                        }else {
                                                            signUp_progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignUp.this, "User registration failed",
                                                        Toast.LENGTH_SHORT).show();

                                                signUp_progressBar.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                //verification e-mail
                                currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignUp.this, "\t\t\t\tRegistered Successfully \n Please verify your email to continue",
                                                    Toast.LENGTH_SHORT).show();
                                            signUp_progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignUp.this, "User registration failed",
                                                Toast.LENGTH_SHORT).show();
                                        signUp_progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }else{
                                //something went wrong
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "SignUp failed\nAccount already exists",
                                    Toast.LENGTH_LONG).show();
                            signUp_progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}