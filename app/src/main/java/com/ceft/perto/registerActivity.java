package com.ceft.perto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.Validator;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ceft.perto.Model.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {


    TextView btnregister;
    EditText edtname, edtpass,edtconfirmpass,edtemail,edttel;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnregister= findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);
        edtname=findViewById(R.id.username);
        edttel= findViewById(R.id.tel);
        edtemail=findViewById(R.id.email);
        edtpass=findViewById(R.id.pass);
        edtconfirmpass=findViewById(R.id.confirmpass);
        progressBar=findViewById(R.id.progressBar);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnregister:
                registeruser();
        }


    }

    private void registeruser() {
        String fullname = edtname.getText().toString().trim();
        String email = edtemail.getText().toString().trim();
        String password = edtpass.getText().toString().trim();
        String confirmpass =edtconfirmpass.getText().toString().trim();
        String phone =  edttel.getText().toString() ;

        if (fullname.isEmpty()){

            edtname.setError("Veuiller entrer le nom complet");
            edtname.requestFocus();
            return;
        }
        if (phone.isEmpty()){
            edttel.setError("Veuiller entrer une numero de tel");
            edttel.requestFocus();
            return;
        }

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
        if (confirmpass.isEmpty()){

            edtconfirmpass.setError("Veuiller confirmer le mot de passe");
            edtconfirmpass.requestFocus();
            return;
        }

        if (!confirmpass.equals(password)){
            edtconfirmpass.setError("les mot de passe ne corespondant pas");
            edtconfirmpass.requestFocus();
            edtpass.requestFocus();

        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            myRef= database.getReference("users");
                            String id = mAuth.getCurrentUser().getUid();;
                            users user =new users(id,fullname,email,password,phone);
                            myRef.child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(registerActivity.this,"votre compte a ete cree avec succes",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(registerActivity.this,"erreur try again",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });


                        }else {
                            Toast.makeText(registerActivity.this,"Failed to register",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);


                        }
                    }
                });

    }
}