package com.ceft.perto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ceft.perto.Model.users;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtregister, btnlogin, btnresetpass;
    private EditText edtemail, edtpass;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private  LoginButton loginButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        database = FirebaseDatabase.getInstance();

        //Setting the permission that we need to read
        //loginButton.setReadPermissions("public_profile","email", "user_birthday");
         loginButton = findViewById(R.id.login_button);
        txtregister = findViewById(R.id.register);
        txtregister.setOnClickListener(this);
        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);
        edtemail = findViewById(R.id.edtemail);
        edtpass = findViewById(R.id.edtpass);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        btnresetpass = findViewById(R.id.btnresetpass);
        btnresetpass.setOnClickListener(this);
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                Intent in = new Intent(this, registerActivity.class);
                startActivity(in);

                break;
            case R.id.btnlogin:
                userlogin();
                break;
            case R.id.btnresetpass:
                in = new Intent(this, ResetPasswordActivity.class);
                startActivity(in);
                break;

        }
    }

    private void userlogin() {
        String email = edtemail.getText().toString().trim();
        String password = edtpass.getText().toString().trim();
        if (email.isEmpty()) {
            edtemail.setError("Veuiller entrer l`email");
            edtemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            edtemail.setError("Veuiller entrer une email valide");
            edtemail.requestFocus();

        }
        if (password.isEmpty()) {

            edtpass.setError("Veuiller entrer le mot de passe");
            edtpass.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "email ou mot de passe incorrect", Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        mAuth = FirebaseAuth.getInstance();

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            myRef = database.getReference("users");
                            String id = mAuth.getCurrentUser().getUid();
                            Log.i(TAG, "onComplete: login completed with user: " + user.getDisplayName());
                        //    users User = new users(id, user.getDisplayName(), user.getEmail(), null, user.getPhoneNumber());

                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    myRef.child("fullname").setValue(user.getDisplayName());
                                    myRef.child("password").setValue(null);
                                    myRef.child("email").setValue(user.getEmail());
                                    myRef.child("phone").setValue(user.getPhoneNumber());
                                    Toast.makeText(LoginActivity.this, "Edited successfully", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.i(TAG, "onStart: Someone logged in <3");
        } else {
            Log.i(TAG, "onStart: No one logged in :/");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }



}