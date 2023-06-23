package com.example.cabs.CariPenyewa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TemukanPenyewa extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ImageView tambah_penyewa;
    AdapterPenyewa adapterPenyewa;
    DatabaseReference database;
    ArrayList<ModelPenyewa> listPenyewa;
    RecyclerView rvPenyewa;
    FirebaseUser user;

    SearchView search;

    private List<ModelPenyewa> filteredList;


    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temukan_penyewa);

        rvPenyewa = findViewById(R.id.rv_penyewa);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        tambah_penyewa = findViewById(R.id.bt_tambah_penyewa);
        search = findViewById(R.id.search);


        tambah_penyewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TemukanPenyewa.this,TambahPenyewaa.class);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        rvPenyewa.setLayoutManager(mLayout);
        rvPenyewa.setItemAnimator(new DefaultItemAnimator());

        tampilData();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterRecyclerView(newText);
                return true;
            }
        });

    }

    private void tampilData() {
        filteredList = new ArrayList<>();
        if (listPenyewa != null) {
            filteredList.addAll(listPenyewa);
        }
        database.child(user.getUid()).child("Penyewa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPenyewa = new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()){
                    if (item.getValue() instanceof HashMap) {
                        ModelPenyewa penyewa = item.getValue(ModelPenyewa.class);
                        penyewa.setKey(item.getKey());
                        listPenyewa.add(penyewa);
                    }
                }
                adapterPenyewa = new AdapterPenyewa(listPenyewa, TemukanPenyewa.this);
                rvPenyewa.setAdapter(adapterPenyewa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Kesalahan saat membaca data: " + error.getMessage());

            }
        });

    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filterRecyclerView(newText);
        return true;
    }

    private void filterRecyclerView(String filterText) {
        filteredList.clear();

        if (filterText.isEmpty()) {
            filteredList.addAll(listPenyewa);
        } else {
            String filterPattern = filterText.toLowerCase().trim();

            for (ModelPenyewa penyewa : listPenyewa) {
                if (penyewa.getTanggal().toLowerCase().contains(filterPattern)) {
                    filteredList.add(penyewa);
                }else if (penyewa.getNama().toLowerCase().contains(filterPattern)) {
                    filteredList.add(penyewa);
                }else if (penyewa.getNama_kendaraan().toLowerCase().contains(filterPattern)) {
                    filteredList.add(penyewa);
                }else if (penyewa.getTotal().toLowerCase().contains(filterPattern)) {
                    filteredList.add(penyewa);
                }
            }
        }

        adapterPenyewa.filterList(filteredList);
    }





}
