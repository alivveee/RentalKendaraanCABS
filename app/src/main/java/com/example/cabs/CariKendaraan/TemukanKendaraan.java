package com.example.cabs.CariKendaraan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.cabs.CariPenyewa.ModelPenyewa;
import com.example.cabs.HomepageActivity;
import com.example.cabs.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TemukanKendaraan extends AppCompatActivity implements SearchView.OnQueryTextListener{
    ImageView btAdd;
    AdapterKendaraan adapterKendaraan;
    DatabaseReference database;
    ArrayList<ModelKendaraan> listKendaraan;
    RecyclerView rvKendaraan;
    FirebaseUser user;

    Context context;

    SearchView search;

    LinearLayout btBack;

    private List<ModelKendaraan> filteredList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_kendaraan);
        btAdd = findViewById(R.id.bt_addKendaraan);
        btBack = findViewById(R.id.back_temukan_kendaraan);
        rvKendaraan = findViewById(R.id.rv_kendaraan);
        search = findViewById(R.id.search_kendaraan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        btAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, TambahKendaraan.class));
        });

        btBack.setOnClickListener(view -> {
            startActivity(new Intent(this, HomepageActivity.class));
            finish();
        });

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        rvKendaraan.setLayoutManager(mLayout);
        rvKendaraan.setItemAnimator(new DefaultItemAnimator());

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
        if (listKendaraan != null) {
            filteredList.addAll(listKendaraan);
        }
        database.child(user.getUid()).child("Kendaraan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listKendaraan = new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()){
                    ModelKendaraan kendaraan = item.getValue(ModelKendaraan.class);
                    kendaraan.setKey(item.getKey());
                    listKendaraan.add(kendaraan);
                }
                adapterKendaraan = new AdapterKendaraan(listKendaraan,TemukanKendaraan.this,context);
                rvKendaraan.setAdapter(adapterKendaraan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            filteredList.addAll(listKendaraan);
        } else {
            String filterPattern = filterText.toLowerCase().trim();

            for (ModelKendaraan kendaraan : listKendaraan) {
                if (kendaraan.getNamaKendaraan().toLowerCase().contains(filterPattern)) {
                    filteredList.add(kendaraan);
                }else if (kendaraan.getTarifKendaraan().toLowerCase().contains(filterPattern)) {
                    filteredList.add(kendaraan);
                }
            }
        }

        adapterKendaraan.filterList(filteredList);
    }
}