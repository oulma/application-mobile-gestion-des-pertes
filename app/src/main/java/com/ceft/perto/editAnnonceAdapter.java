package com.ceft.perto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ceft.perto.Model.Annonce;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class editAnnonceAdapter extends FirebaseRecyclerAdapter<Annonce, editAnnonceAdapter.editAnnonceViewHolder> {

    ArrayList<Annonce> listAnn;
    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public editAnnonceAdapter(@NonNull @NotNull FirebaseRecyclerOptions options,Context context) {
        super(options);
        this.context=context;
    }


    @NonNull
    @NotNull
    @Override
    public editAnnonceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_anonce2,parent,false);
        return new editAnnonceAdapter.editAnnonceViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull editAnnonceViewHolder holder, int position, @NonNull @NotNull Annonce model) {



        holder.t1.setText(model.getTitre());
        holder.t2.setText(model.getType());
        holder.t3.setText(model.getVille());
        //holder.img.setImageResource(listAnn.get(position).getImg_annomca());
        Glide.with(holder.img.getContext())
                .load(model.getImg_annomca())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button edit;

                EditText titre,type,description ,ville,tel;
                AlertDialog.Builder mydialog =new AlertDialog.Builder(holder.edit.getContext());
                LayoutInflater  inflater=LayoutInflater.from(context);
                View myview=inflater.inflate(R.layout.dialog_edit_annonce,null);
                final AlertDialog dialog=mydialog.create();
                dialog.setView(myview);
                titre = myview.findViewById(R.id.edit_titre);
                type = myview.findViewById(R.id.edit_type);
                description = myview.findViewById(R.id.edit_desc);
                ville = myview.findViewById(R.id.edit_ville);
                tel = myview.findViewById(R.id.edit_tel);
                edit = myview.findViewById(R.id.btn_valid_edt);

                titre.setText(model.getTitre());
                type.setText(model.getType());
                description.setText(model.getDescription());
                ville.setText(model.getVille());
                tel.setText(model.getTel());

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();

                        if (TextUtils.isEmpty(titre.getText().toString())) {
                            titre.setError("Field required... ");
                            return;
                        }
                        if (TextUtils.isEmpty(type.getText().toString())) {
                            type.setError("Field required... ");
                            return;
                        }
                        if (TextUtils.isEmpty(description.getText().toString())) {
                            description.setError("Field required... ");
                            return;
                        }
                        if (TextUtils.isEmpty(ville.getText().toString())) {
                            ville.setError("Field required... ");
                            return;
                        }
                        if (TextUtils.isEmpty(tel.getText().toString())) {
                            tel.setError("Field required... ");
                            return;
                        }

                        map.put("titre",titre.getText().toString());
                        map.put("description",description.getText().toString());
                        map.put("type",type.getText().toString());
                        map.put("ville",ville.getText().toString());
                        map.put("tel",tel.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("annances")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialog.dismiss();
                                        Toast.makeText(holder.edit.getContext(), "Edited successfully", Toast.LENGTH_LONG).show();


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(holder.edit.getContext(), "Edited Failed", Toast.LENGTH_LONG).show();

                                    }
                                });


                    }

                });

                dialog.show();


            }
        });

        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.delet.getContext());
                builder.setTitle("Delete Annonce");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("annances")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });

    }



    public class editAnnonceViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1,t2,t3;
        Button edit,delet;
        public editAnnonceViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            t1 = itemView.findViewById(R.id.txt1);
            t2 = itemView.findViewById(R.id.txt2);
            t3 = itemView.findViewById(R.id.txt3);
            edit= itemView.findViewById(R.id.btn_edt);
            delet= itemView.findViewById(R.id.btn_delet);


        }
    }



}
