package com.ceft.perto.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceft.perto.AnnanceAdapter;
import com.ceft.perto.Model.Annonce;
import com.ceft.perto.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private EditText searchBar;
    private HomeViewModel homeViewModel;
    private RecyclerView rcv;
    public  AnnanceAdapter adapter;
    ArrayList<Annonce> listAnn;
    DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rcv = view.findViewById(R.id.rcv);
        searchBar = view.findViewById(R.id.search_bar1);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            filter(editable.toString());
            }
        });
        //  homeViewModel =
        //   new ViewModelProvider(this).get(HomeViewModel.class);

        reference = FirebaseDatabase.getInstance().getReference().child("annances");
        listAnn = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listAnn.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Annonce annonce = dataSnapshot1.getValue(Annonce.class);
                    listAnn.add(annonce);

                }
                Collections.reverse(listAnn);
                adapter = new AnnanceAdapter(listAnn, getContext());
                rcv.setAdapter(adapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                rcv.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
    public void filter(String s)
    {
        ArrayList<Annonce> filtredList = new ArrayList<>();
        for(Annonce an: listAnn)
        {
            if(an.getTitre().toLowerCase().contains(s.toLowerCase()))
            {
                filtredList.add(an);
            }
        }
        adapter.filterList(filtredList);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    // public ArrayList<Annonce> dataAnnonce() {

    //   ArrayList<Annonce>Holder = new ArrayList<>();

    //Annonce ann0 = new Annonce("premier Annonce","salam mcha lia wahd telefone","demmande","Rabat","NO IMG","0628227015");
    //Holder.add(ann0);

    // return Holder;
    // }


}