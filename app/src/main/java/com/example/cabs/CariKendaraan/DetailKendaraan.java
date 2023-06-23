package com.example.cabs.CariKendaraan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cabs.R;

public class DetailKendaraan extends AppCompatActivity {

    ImageView btBack, imgKendaraan;
    TextView tvNamaKendaraan, tvTahunKendaraan, tvTarifKendaraan, tvJenisMesin
            , tvJumlahPenumpang, tvJumlahKendaraan, tvDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_kendaraan);

        btBack = findViewById(R.id.bt_bekbek);
        imgKendaraan = findViewById(R.id.img_gambarKendaraan);
        tvNamaKendaraan = findViewById(R.id.tv_nama_kendaraan);
        tvTahunKendaraan = findViewById(R.id.tv_tahun_kendaraan);
        tvTarifKendaraan = findViewById(R.id.tv_harga_kendaraan);
        tvJenisMesin = findViewById(R.id.tv_tipeMesin);
        tvJumlahPenumpang = findViewById(R.id.tv_jumlahPenumpang);
        tvJumlahKendaraan = findViewById(R.id.tv_jumlahKendaraan);
        tvDeskripsi = findViewById(R.id.tv_deskripsi);


        Intent intent = getIntent();
        if (intent != null) {
            String namaKendaraan = intent.getStringExtra("namaKendaraan");
            String tarifKendaraan = intent.getStringExtra("tarifKendaraan");
            String tahunKendaraan = intent.getStringExtra("tahunKendaraan");
            String jenisMesin = intent.getStringExtra("jenisMesin");
            String jumlahPenumpang = intent.getStringExtra("jumlahPenumpang");
            String jumlahKendaraan = intent.getStringExtra("jumlahKendaraan");
            String deskripsi = intent.getStringExtra("deskripsi");
            String urlGambar = intent.getStringExtra("url");


            // Set TextView dengan data yang diterima
            tvNamaKendaraan.setText(namaKendaraan);
            tvTahunKendaraan.setText(tahunKendaraan);
            tvTarifKendaraan.setText(tarifKendaraan);
            tvJenisMesin.setText(jenisMesin);
            tvJumlahPenumpang.setText(jumlahPenumpang+ " orang");
            tvJumlahKendaraan.setText("Jumlah " +jumlahKendaraan);
            tvDeskripsi.setText(deskripsi);

            // Memuat gambar menggunakan Glide
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop(); // Opsional: Menggunakan cache untuk gambar yang dimuat
            Glide.with(DetailKendaraan.this)
                    .load(urlGambar)
                    .apply(requestOptions).centerCrop()
                    .into(imgKendaraan);
        }

        btBack.setOnClickListener(view -> {
            startActivity(new Intent(this, TemukanKendaraan.class));
            finish();
        });
    }
}