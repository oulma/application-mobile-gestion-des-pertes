package com.ceft.perto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class editAnnonce extends AppCompatActivity {

    TextView name, email, tel;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference uidref, myRef2;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private Query qr;
    private FirebaseAuth mAuth;
    private RecyclerView rv;
    String userid;
    String usId;
    Button btn_edit;
    String fn, mail, phn, usname, ccname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}