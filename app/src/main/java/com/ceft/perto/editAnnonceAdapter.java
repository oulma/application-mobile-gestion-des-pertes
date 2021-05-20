package com.ceft.perto;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ceft.perto.Model.Annonce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class editAnnonceAdapter extends RecyclerView.Adapter<editAnnonceAdapter.editAnnonceViewHolder> {

    ArrayList<Annonce> listAnn;
    Context context;
    private FirebaseDatabase database ;
    private FirebaseAuth auth;
    private DatabaseReference uidref , myRef2;

    public editAnnonceAdapter(ArrayList<Annonce> listAnn, Context context) {
        this.listAnn = listAnn;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public editAnnonceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_anonce,parent,false);
        return new  editAnnonceAdapter.editAnnonceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull editAnnonceViewHolder holder, int position) {

        database= FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        final Annonce ann=listAnn.get(position);
        holder.t1.setText(ann.getTitre());
        holder.t2.setText(ann.getType());
        holder.t3.setText(ann.getVille());
        //holder.img.setImageResource(listAnn.get(position).getImg_annomca());
        Glide.with(context)
                .load(ann.getImg_annomca())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,editAnnonce.class);
             //  String id = myRef2.getRef().getKey();
              //  intent.putExtra("id",id);
                intent.putExtra("imagename",ann.getImg_annomca());
                intent.putExtra("Titre",ann.getTitre());
                intent.putExtra("desc",ann.getDescription());
                intent.putExtra("ville",ann.getVille());
                intent.putExtra("tel",ann.getTel());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAnn.size();
    }


    public class editAnnonceViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1,t2,t3;
        public editAnnonceViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img1);
            t1= itemView.findViewById(R.id.txt1);
            t2= itemView.findViewById(R.id.txt2);
            t3 = itemView.findViewById(R.id.txt3);

        }
    }
}
