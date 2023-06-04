package com.example.cabs.CariKendaraan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cabs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TambahKendaraan extends AppCompatActivity {
    EditText etNamaKendaraan, etTahunKendaraan, etTarifKendaraan, etJenisMesin
            , etJumlahPenumpang, etJumlahKendaraan, etDeskripsi;
    Button btTambah;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kendaraan);
        etNamaKendaraan = findViewById(R.id.et_namaKendaraan);
        etTahunKendaraan = findViewById(R.id.et_tahunKendaraan);
        etTarifKendaraan  = findViewById(R.id.et_hargaSewa);
        etJenisMesin = findViewById(R.id.et_jenisMesin);
        etJumlahPenumpang = findViewById(R.id.et_jumlahPenumpang);
        etJumlahKendaraan = findViewById(R.id.et_jumlahPenumpang);
        etDeskripsi = findViewById(R.id.et_deskripsiKendaraan);
        btTambah = findViewById(R.id.bt_addKendaraan);

        database = FirebaseDatabase.getInstance().getReference();

        btTambah.setOnClickListener(view -> {
            String namaKendaraan =  etNamaKendaraan.getText().toString();
            String tahunKendaraan = etTahunKendaraan.getText().toString();
            String tarifKendaraan = etTarifKendaraan.getText().toString();
            String jenisMesin = etJenisMesin.getText().toString();
            String jumlahPenumpang = etJumlahPenumpang.getText().toString();
            String jumlahKendaraan = etJumlahKendaraan.getText().toString();
            String deskripsi = etDeskripsi.getText().toString();

            ModelKendaraan model = new ModelKendaraan(namaKendaraan, tahunKendaraan, tarifKendaraan,
                    jenisMesin, jumlahPenumpang, jumlahKendaraan, deskripsi);
             if (namaKendaraan.isEmpty()){
                 etNamaKendaraan.setError("Masukkan nama kendaraan");
             } else if (tarifKendaraan.isEmpty()) {
                 etTarifKendaraan.setError("Masukkan tarif kendaraan");
             }else {
                 database.child("Kendaraan").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Toast.makeText(TambahKendaraan.this, "data berhasil disimpan", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(TambahKendaraan.this, TemukanKendaraan.class));
                         finish();
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(TambahKendaraan.this, "gagal menambah data, silahkan coba kembali", Toast.LENGTH_SHORT).show();
                     }
                 });
             }
        });
    }
}