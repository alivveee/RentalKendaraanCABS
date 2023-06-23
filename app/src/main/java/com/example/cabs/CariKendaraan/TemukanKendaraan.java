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

public class TemukanKendaraan extends AppCompatActivity {
    ImageView btAdd, btBack;
    AdapterKendaraan adapterKendaraan;
    DatabaseReference database;
    ArrayList<ModelKendaraan> listKendaraan;
    RecyclerView rvKendaraan;
    FirebaseUser user;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar_kendaraan);
        btAdd = findViewById(R.id.bt_addKendaraan);
        btBack = findViewById(R.id.bt_backedt);
        rvKendaraan = findViewById(R.id.rv_kendaraan);

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
    }

    private void tampilData() {
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
}