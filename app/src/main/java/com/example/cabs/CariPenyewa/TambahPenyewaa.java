package com.example.cabs.CariPenyewa;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.cabs.CariKendaraan.TambahKendaraan;
import com.example.cabs.CariKendaraan.TemukanKendaraan;
import com.example.cabs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.github.muddz.styleabletoast.StyleableToast;

public class TambahPenyewaa extends AppCompatActivity {

    String [] item = {"Mio Karbu","Ora Beat Ora Sweet","RX King Filter","Vespa Kaleng","Mio Mber","Vixion Jari-jari"};
    String imageUrl;

    Uri uri;

    ArrayAdapter<String> adapterItems;

    AutoCompleteTextView autoCompleteTextView;

    ImageView date;
    TextInputEditText tanggal,nama,alamat,no_hp;

    TextInputLayout il_nama,il_tanggal,il_alamat,il_nohp;

    DatabaseReference database;
    FirebaseUser user;

    ImageView upload_image;

    Button submit;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_penyewa);

        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        tanggal = findViewById(R.id.et_tanggal);
        nama = findViewById(R.id.et_nama);
        alamat = findViewById(R.id.et_alamat);
        no_hp = findViewById(R.id.et_no_hp);
        upload_image = findViewById(R.id.upload_image);
        submit = findViewById(R.id.bt_submit);
        date = findViewById(R.id.date);
        il_nama = findViewById(R.id.input_nama);
        il_alamat = findViewById(R.id.input_alamat);
        il_nohp = findViewById(R.id.input_no_hp);
        il_tanggal = findViewById(R.id.input_tanggal);

        TextInputEditText textInputEditText = tanggal;

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(),"Material_Range");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textInputEditText.setText(datePicker.getHeaderText());
                        StyleableToast.makeText(TambahPenyewaa.this,"tanggal disewa dari " + datePicker.getHeaderText(),R.style.mytoast).show();
                    }
                });
            }
        });

        autoCompleteTextView = findViewById(R.id.tv_merk_kendaraan);
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_merk_kendaraan,item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                StyleableToast.makeText(TambahPenyewaa.this,item + " dipilih",R.style.mytoast).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sNama = nama.getText().toString();
                String sAlamat = alamat.getText().toString();
                String sTanggal = tanggal.getText().toString();
                String sNohp = no_hp.getText().toString();

                if(TextUtils.isEmpty(sNama)) {
                    il_nama.setError("Enter Customer Name !");
                    return;
                }else {
                    il_nama.setError("");
                    il_nama.setHelperText("");
                }

                if(TextUtils.isEmpty(sAlamat)) {
                    il_alamat.setError("Enter Customer Address !");
                    return;
                } else {
                    il_alamat.setError("");
                    il_alamat.setHelperText("");
                }
                if(TextUtils.isEmpty(sTanggal)) {
                    il_tanggal.setError("Enter Date !");
                    return;
                }else {
                    il_tanggal.setError("");
                    il_tanggal.setHelperText("");
                }
                if(TextUtils.isEmpty(sNohp)) {
                    il_nohp.setError("Enter Customer Phone Number !");
                    return;
                }else {
                    il_nohp.setError("");
                    il_nohp.setHelperText("");
                }

                ModelPenyewa model = new ModelPenyewa(sNama,sNohp,sAlamat,sTanggal);
                database.child(user.getUid()).child("Penyewa").push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(TambahPenyewaa.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TambahPenyewaa.this, TemukanPenyewa.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TambahPenyewaa.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
