package com.example.cabs.CariPenyewa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.example.cabs.CariKendaraan.EditKendaraan;
import com.example.cabs.CariKendaraan.ModelKendaraan;
import com.example.cabs.CariKendaraan.TemukanKendaraan;
import com.example.cabs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.github.muddz.styleabletoast.StyleableToast;

public class EditPenyewaa extends AppCompatActivity {
    List<String> itemList;
    ArrayAdapter<String> adapterItems;

    AutoCompleteTextView NamaKendaraan;
    ImageView date;
    TextInputEditText Tanggal, Nama, Alamat, No_HP;
    TextView Total;

    TextInputLayout il_Nama, il_Tanggal, il_Alamat, il_NoHP;

    DatabaseReference database;
    FirebaseUser user;

    ImageView upload_image;
    Button Save;

    CardView bt_upload;


    private static final int RC_Take_Photo = 0;
    private static final int RC_Take_From_Gallery = 1;
    private static final int RC_SIGN_IN = 999;
    String currentPhotoPath;
    private StorageReference mStorageRef;
    String urlGambar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_penyewa);

        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase database_1 = FirebaseDatabase.getInstance();

        Tanggal = findViewById(R.id.et_tanggal);
        Nama = findViewById(R.id.et_nama);
        Alamat = findViewById(R.id.et_alamat);
        No_HP = findViewById(R.id.edt_et_no_hp);
        upload_image = findViewById(R.id.edt_upload_image);
        Save = findViewById(R.id.bt_submit);
        il_Nama = findViewById(R.id.editinput_nama);
        il_Alamat = findViewById(R.id.editinput_alamat);
        il_NoHP = findViewById(R.id.editinput_no_hp);
        il_Tanggal = findViewById(R.id.editinput_tanggal);
        NamaKendaraan = findViewById(R.id.tv_merk_kendaraan);
        bt_upload = findViewById(R.id.card_upload_image);
        Total = findViewById(R.id.tv_total);

        fetchDataFromFirebase(); // Panggil method untuk mengambil data dari Firebase

        TextInputEditText textInputEditText = Tanggal;

        mStorageRef = FirebaseStorage.getInstance().getReference();
        Intent getData = getIntent();
        String key = getData.getStringExtra("key");
        String nama = getData.getStringExtra("Nama");
        String alamat = getData.getStringExtra("Alamat");
        String no_Hp = getData.getStringExtra("Nomor HP");
//        String jenis = getData.getStringExtra("Jenis");
        String tanggal = getData.getStringExtra("Tanggal");
        String namakendaraan = getData.getStringExtra("Nama Kendaraan");
        String total = getData.getStringExtra("Total");
        String gambar = getData.getStringExtra("gambar");
        Picasso.get().load(gambar).into(upload_image);

        Nama.setText(nama);
        Alamat.setText(alamat);
        No_HP.setText(no_Hp);
//        edtjenisMesin.setText(jenis);
        Tanggal.setText(tanggal);
        Total.setText(total);
        NamaKendaraan.setText(namakendaraan);

        Save.setOnClickListener(view -> {
            String sNo_hp = No_HP.getText().toString();
            String sTotal = Total.getText().toString();
            String sNama = Nama.getText().toString();
            String sTanggal = Tanggal.getText().toString();
            String sAlamat = Alamat.getText().toString();
            String sNamaKendaraan = NamaKendaraan.getText().toString();


            ModelPenyewa model = new ModelPenyewa( sNama, sNo_hp, sAlamat, sNamaKendaraan, sTanggal, sTotal, urlGambar);
            if (sNama.isEmpty()){
                Nama.setError("Masukkan nama");
            } else if (sTanggal.isEmpty()) {
                Tanggal.setError("Masukkan tanggal sewa");
            }else {
                database.child(user.getUid()).child("Penyewa").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditPenyewaa.this, "data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditPenyewaa.this, TemukanPenyewa.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPenyewaa.this, "gagal menambah data, silahkan coba kembali", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image");
        Task<ListResult> listPageTask = fotoRef.list(1);
        listPageTask.addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                List<StorageReference> items = listResult.getItems();
                if(!items.isEmpty()) {
                    Toast.makeText(EditPenyewaa.this, "Loading Foto", Toast.LENGTH_SHORT).show();
                    items.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(EditPenyewaa.this)
                                    .load(uri)
                                    .into(upload_image);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditPenyewaa.this, "Upload Image Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(EditPenyewaa.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] Permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(Permissions,100);
                    }else {
                        selectImage(EditPenyewaa.this);
                    }

                }else {
                    selectImage(EditPenyewaa.this);
                }
            }
        });


        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())).build();

        NamaKendaraan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                StyleableToast.makeText(EditPenyewaa.this,item + " dipilih",R.style.mytoast).show();

                calculateTotal();
            }


        });

        Tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material_Range");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        textInputEditText.setText(datePicker.getHeaderText());
                        StyleableToast.makeText(EditPenyewaa.this, "tanggal disewa dari " + datePicker.getHeaderText(), R.style.mytoast).show();
                        calculateTotal();
                    }

                });

            }
        });



        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateTotal();
                String sNama = Nama.getText().toString();
                String sAlamat = Alamat.getText().toString();
                String sTanggal = Tanggal.getText().toString();
                String sNohp = No_HP.getText().toString();
                String sNamaKendaraan = NamaKendaraan.getText().toString();
                String sTotal = Total.getText().toString();




                if (TextUtils.isEmpty(sNama)) {
                    il_Nama.setError("Enter Customer Name!");
                    return;
                } else {
                    il_Nama.setError("");
                    il_Nama.setHelperText("");
                }

                if (TextUtils.isEmpty(sAlamat)) {
                    il_Alamat.setError("Enter Customer Address!");
                    return;
                } else {
                    il_Alamat.setError("");
                    il_Alamat.setHelperText("");
                }
                if (TextUtils.isEmpty(sTanggal)) {
                    il_Tanggal.setError("Enter Date!");
                    return;
                } else {
                    il_Tanggal.setError("");
                    il_Tanggal.setHelperText("");
                }
                if (TextUtils.isEmpty(sNohp)) {
                    il_NoHP.setError("Enter Customer Phone Number!");
                    return;
                } else {
                    il_NoHP.setError("");
                    il_NoHP.setHelperText("");
                }



                ModelPenyewa model = new ModelPenyewa(sNama, sNohp, sAlamat, sNamaKendaraan, sTanggal,sTotal,urlGambar);
                database.child(user.getUid()).child("Penyewa").child(key).setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditPenyewaa.this, "Data Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditPenyewaa.this, TemukanPenyewa.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditPenyewaa.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            selectImage(EditPenyewaa.this);
        }
    }

    public void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo","Choose From Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose yout profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if(options[item].equals("Take Photo")){
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if(takePicture.resolveActivity(context.getPackageManager()) != null) {
                        File photofile = null;
                        try {
                            photofile = createImageFile();
                        }catch (IOException ex) {

                        }

                        if(photofile != null) {
                            Uri photoURI = FileProvider.getUriForFile(EditPenyewaa.this,
                                    "com.example.myapplication",
                                    photofile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                            startActivityForResult(takePicture,RC_Take_Photo);

                        }
                    }

                }else if(options[item].equals("Choose From Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto,RC_Take_From_Gallery);
                }else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case RC_Take_Photo:
                    if (resultCode == RESULT_OK && currentPhotoPath != null) {

                        Glide.with(this).load(new File(currentPhotoPath)).fitCenter().into(upload_image);

                        ////scaning masuk ke gallery android (opsional)//////////////////
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        File f = new File(currentPhotoPath);
                        Uri contentUri = Uri.fromFile(f);
                        mediaScanIntent.setData(contentUri);
                        this.sendBroadcast(mediaScanIntent);
                        //////////////////////////////////

                        //upload hasil foto ke storage database
                        uploadToStorage(contentUri);
                    }

                    break;
                case RC_Take_From_Gallery:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                Glide.with(this).load(new File(picturePath)).centerCrop().into(upload_image);
                                cursor.close();
                            }
                            //upload file dari galeri ke storage
                            uploadToStorage(selectedImage);
                        }

                    }
                    break;
                case RC_SIGN_IN:
                    if (resultCode == RESULT_OK) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image");

                    } else {
                        Toast.makeText(this,
                                        "We couldn't sign you in. Please try again later.",
                                        Toast.LENGTH_LONG)
                                .show();

                    }

            }
        }
    }



    private void uploadToStorage(Uri file) {
        UploadTask uploadTask;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String key;
        key = UUID.randomUUID().toString();

        // Buat path di Firebase Storage
        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/penyewa/" + key);
        uploadTask = fotoRef.putFile(file);
        Toast.makeText(EditPenyewaa.this,
                "uploading Image",
                Toast.LENGTH_SHORT).show();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(EditPenyewaa.this,
                        "can't upload Image, " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(EditPenyewaa.this,
                        "Image Uploaded",
                        Toast.LENGTH_SHORT).show();
                // Mendapatkan URL unduhan gambar
                Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                downloadUrlTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Mendapatkan URL unduhan gambar
                        urlGambar = uri.toString();

                    }
                });
            }
        });


    }



    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
              imageFileName,".jpg",storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void fetchDataFromFirebase() {
        DatabaseReference kendaraanRef = database.child(user.getUid()).child("Kendaraan");
        kendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList = new ArrayList<>();

                for (DataSnapshot kendaraanSnapshot : dataSnapshot.getChildren()) {
                    String kendaraanId = kendaraanSnapshot.getKey();
                    DatabaseReference namaKendaraanRef = kendaraanRef.child(kendaraanId).child("namaKendaraan");
                    namaKendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String namaKendaraan = dataSnapshot.getValue(String.class);
                            if (namaKendaraan != null) {
                                itemList.add(namaKendaraan);
                            }

                            setupAutoCompleteTextView(itemList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors that occur
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setupAutoCompleteTextView(List<String> itemList) {
        adapterItems = new ArrayAdapter<>(EditPenyewaa.this, R.layout.list_item_merk_kendaraan, itemList);
        NamaKendaraan.setAdapter(adapterItems);
    }

    private void calculateTotal() {
        Log.d("MyApp", "calculateTotal() called");
        String selectedKendaraan = NamaKendaraan.getText().toString();
        String selectedTanggal = Tanggal.getText().toString();
        Log.d("MyApp", "selectedKendaraan: " + selectedKendaraan);
        Log.d("MyApp", "selectedTanggal: " + selectedTanggal);

        if (!TextUtils.isEmpty(selectedKendaraan) && !TextUtils.isEmpty(selectedTanggal)) {
            DatabaseReference kendaraanRef = database.child(user.getUid()).child("Kendaraan");
            kendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final int[] hargaKendaraan = {0}; // Harga kendaraan yang dipilih
                    final boolean[] kendaraanFound = {false}; // Untuk menandakan apakah kendaraan ditemukan atau tidak

                    for (DataSnapshot kendaraanSnapshot : dataSnapshot.getChildren()) {
                        String kendaraanId = kendaraanSnapshot.getKey();
                        DatabaseReference namaKendaraanRef = kendaraanRef.child(kendaraanId).child("namaKendaraan");
                        namaKendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String namaKendaraan = dataSnapshot.getValue(String.class);
                                Log.d("MyApp", "namaKendaraan: " + namaKendaraan);

                                if (namaKendaraan != null && namaKendaraan.equals(selectedKendaraan)) {
                                    kendaraanFound[0] = true;
                                    DatabaseReference hargaKendaraanRef = kendaraanRef.child(kendaraanId).child("tarifKendaraan");
                                    hargaKendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String hargaKendaraanStr = dataSnapshot.getValue(String.class);
                                            Log.d("MyApp", "hargaKendaraanStr: " + hargaKendaraanStr);
                                            if (hargaKendaraanStr != null) {
                                                try {
                                                    hargaKendaraan[0] = Integer.parseInt(hargaKendaraanStr);
                                                    calculateAndUpdateTotal(hargaKendaraan[0], selectedTanggal);
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                    // Handle the error or provide a default value for hargaKendaraan
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle any errors that occur
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle any errors that occur
                            }
                        });

                        if (kendaraanFound[0]) {
                            break; // Keluar dari loop jika kendaraan ditemukan
                        }
                    }

                    if (!kendaraanFound[0]) {
                        // Handle case when the selected kendaraan is not found
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void calculateAndUpdateTotal(int hargaKendaraan, String selectedTanggal) {
        Log.d("MyApp", "calculateAndUpdateTotal() called");
        Log.d("MyApp", "harga kendaraan: " + hargaKendaraan);
        Log.d("MyApp", "selectedTanggal: " + selectedTanggal);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d – MMM d");
            Log.d("MyApp", "totalHarga: " + 0);


            String startDateStr = selectedTanggal.split(" – ")[0].trim();
            Log.d("MyApp", "startDate: " + startDateStr);
            String endDateStr = selectedTanggal.split(" – ")[1].trim();
            Log.d("MyApp", "endDate: " + endDateStr);

            // Mem-parse tanggal tunggal tanpa bulan
            SimpleDateFormat sdfDateOnly = new SimpleDateFormat("MMM d");
            Date startDate = sdfDateOnly.parse(startDateStr);
            Date endDate = sdfDateOnly.parse(endDateStr);

            Log.d("MyApp", "startDate: " + startDate);
            Log.d("MyApp", "endDate: " + endDate);

            long diffInMilliseconds = endDate.getTime() - startDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds) ; // Menambahkan 1 karena inklusif rentang tanggal
            Log.d("MyApp", "days: " + days);

            int totalHarga = hargaKendaraan * (int) days;
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedTotalHarga = rupiahFormat.format(totalHarga);

            Log.d("MyApp", "totalHarga: " + formattedTotalHarga);
            Total.setText(formattedTotalHarga);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyApp", "Error: " + e.getMessage());
        }
    }


}
