package com.ceft.perto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class annonceDetailActivity extends AppCompatActivity {

    ImageView img;
    TextView tv1, tv2, tv3, tv4;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annonce_detail);

        img = (ImageView) findViewById(R.id.ann_img);
        tv1 = (TextView) findViewById(R.id.ann_titre);
        tv2 = (TextView) findViewById(R.id.ann_desc);
        tv3 = (TextView) findViewById(R.id.ann_ville);
        tv4 = (TextView) findViewById(R.id.ann_contact);

        Glide.with(this)
                .load(getIntent().getStringExtra("imagename"))
                .placeholder(R.mipmap.ic_launcher)
                .into(img);
        // img.setImageResource(getIntent().getStringExtra("imagename"));
        tv1.setText(getIntent().getStringExtra("Titre"));
        tv2.setText(getIntent().getStringExtra("desc"));
        tv3.setText(getIntent().getStringExtra("ville"));
        tv4.setText(getIntent().getStringExtra("tel"));


    }
}