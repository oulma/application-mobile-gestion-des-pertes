package com.ceft.perto.ui.Annonces;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ceft.perto.LoginActivity;
import com.ceft.perto.Model.Annonce;
import com.ceft.perto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnnoncesFragment extends Fragment {
    FirebaseUser user;

    private LinearLayout txtlogin;
    private ScrollView scrollView;
    private DashboardViewModel dashboardViewModel;
    private EditText titre, description, ville,phone;
    private ImageView ann_img;
    private Button btn_img,btn_ann,btn_login;
    private TextView textlogine;
    Uri selectedImage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    private DatabaseReference myRef ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_annonces, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();


        titre= root.findViewById(R.id.titre_ann);
        description= root.findViewById(R.id.desc_ann);
        ville= root.findViewById(R.id.ville_ann);
        ann_img= root.findViewById(R.id.img_ann);
        phone= root.findViewById(R.id.tel_ann);
        btn_ann=root.findViewById(R.id.btn_ann);
        btn_img=root.findViewById(R.id.btn_img);
       // txtlogin=root.findViewById(R.id.textlogine);
        txtlogin = root.findViewById(R.id.ann_text);
        scrollView=root.findViewById(R.id.scrollview);


        if (user !=null) {
            scrollView.setVisibility(View.VISIBLE);
            txtlogin.setVisibility(View.GONE);

            btn_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 45);


                }
            });

            btn_ann.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String titre_ann = titre.getText().toString();
                    String desc_ann = description.getText().toString();
                    String ville_ann = ville.getText().toString();
                    String phone_ann = phone.getText().toString();
                    String type = spinner.getSelectedItem().toString();


                    if (titre_ann.isEmpty()) {
                        titre.setError("Please type a name");
                        titre.requestFocus();
                    }
                    if (desc_ann.isEmpty()) {
                        description.setError("Please type a name");
                        description.requestFocus();
                    }
                    if (ville_ann.isEmpty()) {
                        ville.setError("Please type a name");
                        ville.requestFocus();
                    }

                    if (phone_ann.isEmpty()) {
                        phone.setError("Please type a name");
                        phone.requestFocus();
                    }

                    if (selectedImage != null) {

                        StorageReference reference = storage.getReference("Annonces").child(auth.getCurrentUser().getUid());
                        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            String uId = auth.getCurrentUser().getUid();

                                            String titre_ann = titre.getText().toString();
                                            String desc_ann = description.getText().toString();
                                            String ville_ann = ville.getText().toString();
                                            String type = spinner.getSelectedItem().toString();
                                            String phone_ann = phone.getText().toString();

                                            Annonce annonce = new Annonce(uId, titre_ann, type, desc_ann, ville_ann, imageUrl, phone_ann);
                                            myRef = database.getReference("annances");
                                            String rvID = myRef.push().getKey();

                                            myRef.child(rvID).setValue(annonce)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(), "votre Annonce a ete cree avec succes", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        String uid = auth.getCurrentUser().getUid();

                        Annonce annonce = new Annonce(uid, titre_ann, type, desc_ann, ville_ann, "no image", phone_ann);

                        database.getReference()
                                .child("annonces")
                                .child(uid)
                                .setValue(annonce)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "votre Annonce a ete cree avec succes", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }


                }

            });

        }else {
            scrollView.setVisibility(View.GONE);
            txtlogin.setVisibility(View.VISIBLE);
            btn_login = root.findViewById(R.id.btn_login);

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            });

        }


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (data!=null){

            if (data.getData() != null){
                selectedImage=data.getData();

                ann_img.setImageURI(data.getData());

            }
        }
    }
}