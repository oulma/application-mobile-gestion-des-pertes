package com.ceft.perto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtregister,btnlogin,btnresetpass;
    private EditText edtemail,edtpass;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtregister= findViewById(R.id.register);
        txtregister.setOnClickListener(this);
        btnlogin=findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(this);
        edtemail=findViewById(R.id.edtemail);
        edtpass=findViewById(R.id.edtpass);
        progressBar=findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        btnresetpass=findViewById(R.id.btnresetpass);
        btnresetpass.setOnClickListener(this);
    }

    public void register(View view) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                Intent in = new Intent(this,registerActivity.class);
                startActivity(in);

                break;
            case R.id.btnlogin:
                userlogin();
                break;
            case R.id.btnresetpass:
                in = new Intent(this,ResetPasswordActivity.class);
                startActivity(in);
                break;

        }
    }

    private void userlogin() {
        String email = edtemail.getText().toString().trim();
        String password= edtpass.getText().toString().trim();
        if (email.isEmpty()){
            edtemail.setError("Veuiller entrer l`email");
            edtemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            edtemail.setError("Veuiller entrer une email valide");
            edtemail.requestFocus();

        }
        if (password.isEmpty()){

            edtpass.setError("Veuiller entrer le mot de passe");
            edtpass.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Intent in = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(in);
                    finish();

                }else{
                    Toast.makeText(LoginActivity.this,"email ou mot de passe incorrect",Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}