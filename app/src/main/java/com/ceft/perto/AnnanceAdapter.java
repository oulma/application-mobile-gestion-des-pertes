package com.ceft.perto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceft.perto.Model.Annonce;
import com.ceft.perto.ui.home.HomeFragment;

import java.util.ArrayList;

public class AnnanceAdapter  extends RecyclerView.Adapter<AnnanceAdapter.AnnonceViewHolder> {

    ArrayList<Annonce> listAnn;
    Context context;

    public AnnanceAdapter(ArrayList<Annonce> listAnn, Context context) {
        this.listAnn = listAnn;
        this.context = context;
    }
    @NonNull
    @Override
    public AnnonceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_anonce,parent,false);
        return new AnnanceAdapter.AnnonceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnonceViewHolder holder, int position) {


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
                Intent intent=new Intent(context,annonceDetailActivity.class);
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

    public class AnnonceViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView t1,t2,t3;
        public AnnonceViewHolder(@NonNull View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img1);
            t1= itemView.findViewById(R.id.txt1);
            t2= itemView.findViewById(R.id.txt2);
            t3 = itemView.findViewById(R.id.txt3);

        }
    }
}
