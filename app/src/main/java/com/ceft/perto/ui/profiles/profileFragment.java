package com.ceft.perto.ui.profiles;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceft.perto.AnnanceAdapter;
import com.ceft.perto.Model.Annonce;
import com.ceft.perto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class profileFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    TextView name , email , tel;

    private FirebaseDatabase database ;
    private FirebaseAuth auth;
    private DatabaseReference uidref , myRef2;
    private FirebaseDatabase mDatabase ;
    private DatabaseReference myRef ;
    private Query qr;
    private FirebaseAuth mAuth;
    private RecyclerView rv;
    String userid ;
    String usId;
    Button btn_edit ;
    String fn ,mail ,phn ,passwrd ;
    ArrayList<Annonce> listAnn;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        rv = root.findViewById(R.id.rycycler_home);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        usId = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("annances");
        qr = myRef.orderByChild("iduser").equalTo(usId);
        listAnn= new ArrayList<>();

        qr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listAnn.clear();


                for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    Annonce annonce = dataSnapshot1.getValue(Annonce.class);
                    listAnn.add(annonce);

                }

                Collections.reverse(listAnn);
               AnnanceAdapter adapter= new AnnanceAdapter(listAnn,getContext());
                rv.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
                rv.setLayoutManager(gridLayoutManager);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();


            }
        });
      //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
       // rv.setLayoutManager(gridLayoutManager);

        //////////////////////////////////////


        name = root.findViewById(R.id.display_name);
        email =root.findViewById(R.id.display_email);
        tel = root.findViewById(R.id.display_tel);
        btn_edit = root.findViewById(R.id.btn_ediit);

        database= FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("userBUSINESS");
        auth=FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String UId = user.getUid();

        uidref = database.getReference("users").child(UId);

        uidref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fn = snapshot.child("fullname").getValue(String.class);
                name.setText("Nom complet : "+fn);
                mail = snapshot.child("email").getValue(String.class);
                email.setText("email:  "+mail);
                passwrd = snapshot.child("password").getValue(String.class);
                phn = snapshot.child("phone").getValue(String.class);
                tel.setText("Tel:  "+phn);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customdial();
            }

        });





        return root;
    }


    public void customdial() {
        EditText fullname,email,number ,passw;
        Button edit;
        AlertDialog.Builder mydialog =new AlertDialog.Builder(getContext());

      LayoutInflater  inflater=LayoutInflater.from(getContext());
        View myview=inflater.inflate(R.layout.dialog_edit_profil,null);

        final AlertDialog dialog=mydialog.create();
        dialog.setView(myview);

        fullname = myview.findViewById(R.id.editfulname);
        email = myview.findViewById(R.id.editemail);
        number = myview.findViewById(R.id.editnumber);
        passw = myview.findViewById(R.id.editpasse);


        edit = myview.findViewById(R.id.editbtn);

        fullname.setText(fn);
        passw.setText(passwrd);
        email.setText(mail);
        number.setText(phn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(fullname.getText().toString())){
                    fullname.setError("Field required... ");
                    return;
                }
                if (TextUtils.isEmpty(passw.getText().toString())){
                    passw.setError("Field required... ");
                    return;
                }
                if (TextUtils.isEmpty(email.getText().toString())){
                    email.setError("Field required... ");
                    return;
                }
                if (TextUtils.isEmpty(number.getText().toString())){
                    number.setError("Field required... ");
                    return;
                }

                uidref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        uidref.child("fullname").setValue(fullname.getText().toString());
                        uidref.child("password").setValue(passw.getText().toString());
                        uidref.child("email").setValue(email.getText().toString());
                        uidref.child("phone").setValue(number.getText().toString());
                        Toast.makeText(getContext(), "Edited successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}