package com.example.cabs.CariPenyewa;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabs.CariKendaraan.AdapterKendaraan;
import com.example.cabs.CariKendaraan.ModelKendaraan;
import com.example.cabs.CariKendaraan.TemukanKendaraan;
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
import java.util.HashMap;
import java.util.List;

public class TemukanPenyewa extends AppCompatActivity{

    ImageView tambah_penyewa;
    AdapterPenyewa adapterPenyewa;
    DatabaseReference database;
    ArrayList<ModelPenyewa> listPenyewa;
    RecyclerView rvPenyewa;
    FirebaseUser user;


    Context mContext;


    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temukan_penyewa);

        rvPenyewa = findViewById(R.id.rv_penyewa);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        tambah_penyewa = findViewById(R.id.bt_tambah_penyewa);


        tambah_penyewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TemukanPenyewa.this,TambahPenyewaa.class);
                startActivity(intent);
                finish();
            }
        });

        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        rvPenyewa.setLayoutManager(mLayout);
        rvPenyewa.setItemAnimator(new DefaultItemAnimator());

        tampilData();

    }

    private void tampilData() {
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

    private void filterRecyclerView(String filterText) {
        List<ModelPenyewa> filteredList = new ArrayList<>();

        for (ModelPenyewa item : listPenyewa) {
            if (item.getTanggal().equals(filterText)) {
                filteredList.add(item);
            }
        }

        adapterPenyewa = new AdapterPenyewa(filteredList, TemukanPenyewa.this);
        rvPenyewa.setAdapter(adapterPenyewa);
    }
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String filterText = s.toString().trim(); // Dapatkan teks filter dan hapus spasi di awal dan akhir

        // Panggil metode filterRecyclerView untuk menerapkan filter pada RecyclerView
        filterRecyclerView(filterText);
    }


}
