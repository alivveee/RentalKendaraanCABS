//package com.example.cabs.CariPenyewa;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.FileProvider;
//import androidx.core.util.Pair;
//
//import com.bumptech.glide.Glide;
//import com.example.cabs.CariKendaraan.TambahKendaraan;
//import com.example.cabs.CariKendaraan.TemukanKendaraan;
//import com.example.cabs.R;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.datepicker.MaterialDatePicker;
//import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.ListResult;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import io.github.muddz.styleabletoast.StyleableToast;
//
//public class TambahPenyewaa extends AppCompatActivity {
//    List<String> itemList;
//
//    submit.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            String sNama = nama.getText().toString();
//            String sAlamat = alamat.getText().toString();
//            String sTanggal = tanggal.getText().toString();
//            String sNohp = no_hp.getText().toString();
//            String sNamaKendaraan = autoCompleteTextView.getText().toString();
//
//
//            if (TextUtils.isEmpty(sNama)) {
//                il_nama.setError("Enter Customer Name!");
//                return;
//            } else {
//                il_nama.setError("");
//                il_nama.setHelperText("");
//            }
//
//            if (TextUtils.isEmpty(sAlamat)) {
//                il_alamat.setError("Enter Customer Address!");
//                return;
//            } else {
//                il_alamat.setError("");
//                il_alamat.setHelperText("");
//            }
//            if (TextUtils.isEmpty(sTanggal)) {
//                il_tanggal.setError("Enter Date!");
//                return;
//            } else {
//                il_tanggal.setError("");
//                il_tanggal.setHelperText("");
//            }
//            if (TextUtils.isEmpty(sNohp)) {
//                il_nohp.setError("Enter Customer Phone Number!");
//                return;
//            } else {
//                il_nohp.setError("");
//                il_nohp.setHelperText("");
//            }
//
//            ModelPenyewa model = new ModelPenyewa(sNama, sNohp, sAlamat, sNamaKendaraan, sTanggal);
//            database.child(user.getUid()).child("Penyewa").push().setValue(model)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(TambahPenyewaa.this, "Data Saved", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(TambahPenyewaa.this, TemukanPenyewa.class));
//                            finish();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(TambahPenyewaa.this, "Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//
//        }
//    });
//
//    bt_upload.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
//                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                    String[] Permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                    requestPermissions(Permissions,100);
//                }else {
//                    selectImage(TambahPenyewaa.this);
//                }
//
//            }else {
//                selectImage(TambahPenyewaa.this);
//            }
//        }
//    });
//
//    public void selectImage(Context context) {
//        final CharSequence[] options = {"Take Photo","Choose From Gallery","Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Choose yout profile picture");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if(options[item].equals("Take Photo")){
//                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                    if(takePicture.resolveActivity(context.getPackageManager()) != null) {
//                        File photofile = null;
//                        try {
//                            photofile = createImageFile();
//                        }catch (IOException ex) {
//
//                        }
//
//                        if(photofile != null) {
//                            Uri photoURI = FileProvider.getUriForFile(TambahPenyewaa.this,
//                                    "com.example.myapplication",
//                                    photofile);
//                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
//                            startActivityForResult(takePicture,RC_Take_Photo);
//
//                        }
//                    }
//
//                }else if(options[item].equals("Choose From Gallery")) {
//                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(pickPhoto,RC_Take_From_Gallery);
//                }else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case RC_Take_Photo:
//                    if (resultCode == RESULT_OK && currentPhotoPath != null) {
//
//                        Glide.with(this).load(new File(currentPhotoPath)).centerCrop().into(upload_image);
//
//                        ////scaning masuk ke gallery android (opsional)//////////////////
//                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        File f = new File(currentPhotoPath);
//                        Uri contentUri = Uri.fromFile(f);
//                        mediaScanIntent.setData(contentUri);
//                        this.sendBroadcast(mediaScanIntent);
//                        //////////////////////////////////
//
//                        //upload hasil foto ke storage database
//                        uploadToStorage(contentUri);
//                    }
//
//                    break;
//                case RC_Take_From_Gallery:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                Glide.with(this).load(new File(picturePath)).centerCrop().into(upload_image);
//                                cursor.close();
//                            }
//                            //upload file dari galeri ke storage
//                            uploadToStorage(selectedImage);
//                        }
//
//                    }
//                    break;
//                case RC_SIGN_IN:
//                    if (resultCode == RESULT_OK) {
//                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//
//                        mStorageRef = FirebaseStorage.getInstance().getReference();
//                        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/image");
//
//                        Task<ListResult> listPageTask = fotoRef.list(1);
//
//
//                        listPageTask
//                                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//                                    @Override
//                                    public void onSuccess(ListResult listResult) {
//
//                                        List<StorageReference> items = listResult.getItems();
//                                        if (!items.isEmpty()) { //menggunakan Library glide untuk memudahkan
//                                            Toast.makeText(TambahPenyewaa.this,
//                                                            "loading Foto",
//                                                            Toast.LENGTH_SHORT)
//                                                    .show();
//                                            items.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                @Override
//                                                public void onSuccess(Uri uri) {
//                                                    Glide.with(TambahPenyewaa.this /* context */)
//                                                            .load(uri)
//                                                            .centerCrop()
//                                                            .into(upload_image);
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception exception) {
//                                                    // Handle any errors
//                                                }
//                                            });
//                                        } else {
//                                            Toast.makeText(TambahPenyewaa.this,
//                                                            "Belum ada Foto",
//                                                            Toast.LENGTH_SHORT)
//                                                    .show();
//                                        }
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(TambahPenyewaa.this,
//                                                        "can't get Image, " + e.getMessage(),
//                                                        Toast.LENGTH_LONG)
//                                                .show();
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(this,
//                                        "We couldn't sign you in. Please try again later.",
//                                        Toast.LENGTH_LONG)
//                                .show();
//
//                    }
//
//            }
//        }
//    }
//
//    public void uploadToStorage(Uri file) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference penyewaRef = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("Penyewa");
//
//        penyewaRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot penyewaSnapshot : dataSnapshot.getChildren()) {
//                    String penyewaId = penyewaSnapshot.getKey();
//
//                    StorageReference fotoRef = mStorageRef.child(user.getUid() + "/Penyewa/" + penyewaId + "/" + penyewaId + ".jpg");
//                    UploadTask uploadTask = fotoRef.putFile(file);
//
//                    Toast.makeText(TambahPenyewaa.this, "Uploading Image", Toast.LENGTH_SHORT).show();
//
//                    // Register observers to listen for when the upload is done or if it fails
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            Toast.makeText(TambahPenyewaa.this, "Failed to upload Image: " + exception.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Handle successful uploads
//                            Toast.makeText(TambahPenyewaa.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle onCancelled event
//            }
//        });
//    }
//    private void fetchDataFromFirebase() {
//        DatabaseReference kendaraanRef = database.child(user.getUid()).child("Kendaraan");
//        kendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                itemList = new ArrayList<>();
//                final int[] itemCount = {0};
//
//                for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
//                    String kendaraanId = idSnapshot.getKey();
//                    DatabaseReference namaKendaraanRef = kendaraanRef.child(kendaraanId).child("namaKendaraan");
//                    namaKendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
//                                String item = itemSnapshot.getValue(String.class);
//                                itemList.add(item);
//                            }
//
//                            itemCount[0]++;
//                            if (itemCount[0] == dataSnapshot.getChildrenCount()) {
//                                setupAutoCompleteTextView(itemList);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle any errors that occur
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//    }
//
//    private void setupAutoCompleteTextView(List<String> itemList) {
//        String[] itemsArray = new String[itemList.size()];
//        itemsArray = itemList.toArray(itemsArray);
//
//        adapterItems = new ArrayAdapter<>(TambahPenyewaa.this, R.layout.list_item_merk_kendaraan, itemsArray);
//        autoCompleteTextView.setAdapter(adapterItems);
//        Toast.makeText(TambahPenyewaa.this, "Jumlah item: " + itemList.size(), Toast.LENGTH_SHORT).show();
//    }
//
//
//    //    String [] item = {"Mio Karbu","Ora Beat Ora Sweet","RX King Filter","Vespa Kaleng","Mio Mber","Vixion Jari-jari"};
//    String imageUrl;
//
//    Uri uri;
//
//    ArrayAdapter<String> adapterItems;
//
//    AutoCompleteTextView autoCompleteTextView;
//
//    ImageView date;
//    TextInputEditText tanggal,nama,alamat,no_hp;
//
//    TextInputLayout il_nama,il_tanggal,il_alamat,il_nohp;
//
//    DatabaseReference database;
//    FirebaseUser user;
//
//    ImageView upload_image;
//
//    Button submit;
//
//    fetchDataFromFirebase();
//
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tambah_penyewa);
//
//        database = FirebaseDatabase.getInstance().getReference();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        FirebaseDatabase database_1 = FirebaseDatabase.getInstance();
//        DatabaseReference kendaraanRef = database_1.getReference().child(user.getUid()).child("Kendaraan");
//
//        tanggal = findViewById(R.id.et_tanggal);
//        nama = findViewById(R.id.et_nama);
//        alamat = findViewById(R.id.et_alamat);
//        no_hp = findViewById(R.id.et_no_hp);
//        upload_image = findViewById(R.id.upload_image);
//        submit = findViewById(R.id.bt_submit);
//        il_nama = findViewById(R.id.input_nama);
//        il_alamat = findViewById(R.id.input_alamat);
//        il_nohp = findViewById(R.id.input_no_hp);
//        il_tanggal = findViewById(R.id.input_tanggal);
//        autoCompleteTextView = findViewById(R.id.tv_merk_kendaraan);
//
//
//        TextInputEditText textInputEditText = tanggal;
//
//        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
//                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                        MaterialDatePicker.todayInUtcMilliseconds())).build();
//
//        tanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePicker.show(getSupportFragmentManager(),"Material_Range");
//                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//                    @Override
//                    public void onPositiveButtonClick(Object selection) {
//                        textInputEditText.setText(datePicker.getHeaderText());
//                        StyleableToast.makeText(TambahPenyewaa.this,"tanggal disewa dari " + datePicker.getHeaderText(),R.style.mytoast).show();
//                    }
//                });
//            }
//        });
////        kendaraanRef.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                List<String> itemList = new ArrayList<>();
////
////                for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
////                    String kendaraanId = idSnapshot.getKey();
////                    DatabaseReference namaKendaraanRef = kendaraanRef.child(kendaraanId).child("namaKendaraan");
////                    namaKendaraanRef.addListenerForSingleValueEvent(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                            for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
////                                String item = itemSnapshot.getValue(String.class);
////                                itemList.add(item);
////                            }
////                        }
////
////                        @Override
////                        public void onCancelled(@NonNull DatabaseError databaseError) {
////                            // Handle any errors that occur
////                        }
////                    });
////                }
////
////                String[] itemsArray = new String[itemList.size()];
////                itemsArray = itemList.toArray(itemsArray);
////
////                adapterItems = new ArrayAdapter<String>(TambahPenyewaa.this, R.layout.list_item_merk_kendaraan, itemsArray);
////                autoCompleteTextView.setAdapter(adapterItems);
////                Toast.makeText(TambahPenyewaa.this, "Jumlah item: " + itemList.size(), Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////                // Handle error
////            }
////        });
//
//
////        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_merk_kendaraan,item);
////        autoCompleteTextView.setAdapter(adapterItems);
//
////        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////                String item = adapterItems.getItem(position);
////                StyleableToast.makeText(TambahPenyewaa.this, item + " dipilih", R.style.mytoast).show();
////            }
////        });
////        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
////                String item = adapterView.getItemAtPosition(position).toString();
////                StyleableToast.makeText(TambahPenyewaa.this,item + " dipilih",R.style.mytoast).show();
////            }
////        });
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String sNama = nama.getText().toString();
//                String sAlamat = alamat.getText().toString();
//                String sTanggal = tanggal.getText().toString();
//                String sNohp = no_hp.getText().toString();
//                String sNamaKendaraan = autoCompleteTextView.getText().toString();
//
//                if(TextUtils.isEmpty(sNama)) {
//                    il_nama.setError("Enter Customer Name !");
//                    return;
//                }else {
//                    il_nama.setError("");
//                    il_nama.setHelperText("");
//                }
//
//                if(TextUtils.isEmpty(sAlamat)) {
//                    il_alamat.setError("Enter Customer Address !");
//                    return;
//                } else {
//                    il_alamat.setError("");
//                    il_alamat.setHelperText("");
//                }
//                if(TextUtils.isEmpty(sTanggal)) {
//                    il_tanggal.setError("Enter Date !");
//                    return;
//                }else {
//                    il_tanggal.setError("");
//                    il_tanggal.setHelperText("");
//                }
//                if(TextUtils.isEmpty(sNohp)) {
//                    il_nohp.setError("Enter Customer Phone Number !");
//                    return;
//                }else {
//                    il_nohp.setError("");
//                    il_nohp.setHelperText("");
//                }
//
//                ModelPenyewa model = new ModelPenyewa(sNama,sNohp,sAlamat,sNamaKendaraan,sTanggal);
//                database.child(user.getUid()).child("Penyewa").push().setValue(model)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                Toast.makeText(TambahPenyewaa.this, "Data Saved", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(TambahPenyewaa.this, TemukanPenyewa.class));
//                                finish();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(TambahPenyewaa.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//    }
//}
