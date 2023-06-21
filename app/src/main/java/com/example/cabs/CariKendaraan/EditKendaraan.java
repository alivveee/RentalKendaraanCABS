package com.example.cabs.CariKendaraan;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.cabs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class EditKendaraan extends AppCompatActivity {
    EditText edtnamaKendaraan, edttahunKendaraan, edttarifKendaraan
            , edtjenisMesin, edtjumlahPenumpang, edtjumlahKendaraan, edtdeskripsiKendaraan;
    CardView btUpload;
    ImageView uploadImage;
    AutoCompleteTextView etJenisMesin;
    Button btsave;
    DatabaseReference database;
    FirebaseUser user;
    private static final int RC_Take_Photo = 0;
    private static final int RC_Take_From_Gallery = 1;
    private StorageReference mStorageRef;
    String currentPhotoPath, urlGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_kendaraan);
        edtnamaKendaraan = findViewById(R.id.edt_namaKendaraan);
        edttahunKendaraan = findViewById(R.id.edt_tahunKendaraan);
        edttarifKendaraan = findViewById(R.id.edt_tarifKendaraan);
        etJenisMesin = findViewById(R.id.edt_jenisMesin);
        edtjumlahPenumpang = findViewById(R.id.edt_jumlahPenumpang);
        edtjumlahKendaraan = findViewById(R.id.edt_jumlahKendaraan);
        edtdeskripsiKendaraan = findViewById(R.id.edt_deskripsiKendaraan);
        btsave = findViewById(R.id.bt_edt_submit);
        btUpload = findViewById(R.id.imgbt);
        uploadImage = findViewById(R.id.upload_imgg);


        String[] jenisMesinOptions = {"Matic", "Manual"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jenisMesinOptions);
        etJenisMesin.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent getData = getIntent();
        String key = getData.getStringExtra("key");
        String nama = getData.getStringExtra("Nama");
        String tahun = getData.getStringExtra("Tahun");
        String tarif = getData.getStringExtra("Tarif");
//        String jenis = getData.getStringExtra("Jenis");
        String jumlahpenumpang = getData.getStringExtra("Jumlah Penumpang");
        String jumlahkendaraan = getData.getStringExtra("Jumlah Kendaraan");
        String deskripsi = getData.getStringExtra("Deskripsi");
        String gambar = getData.getStringExtra("gambar");
        Picasso.get().load(gambar).into(uploadImage);

        edtnamaKendaraan.setText(nama);
        edttahunKendaraan.setText(tahun);
        edttarifKendaraan.setText(tarif);
//        edtjenisMesin.setText(jenis);
        edtjumlahPenumpang.setText(jumlahpenumpang);
        edtjumlahKendaraan.setText(jumlahkendaraan);
        edtdeskripsiKendaraan.setText(deskripsi);


        btUpload.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String[] Permisions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(Permisions, 100);
                } else {
                    selectImage(EditKendaraan.this);
                }
            } else {
                selectImage(EditKendaraan.this);

            }
        });

        btsave.setOnClickListener(view -> {
            String namaKendaraan =  edtnamaKendaraan.getText().toString();
            String tahunKendaraan = edttahunKendaraan.getText().toString();
            String tarifKendaraan = edttarifKendaraan.getText().toString();
            String jenisMesin = etJenisMesin.getText().toString();
            String jumlahPenumpang = edtjumlahPenumpang.getText().toString();
            String jumlahKendaraan = edtjumlahKendaraan.getText().toString();
            String deskrpsi= edtdeskripsiKendaraan.getText().toString();

            ModelKendaraan model = new ModelKendaraan(namaKendaraan, tahunKendaraan, tarifKendaraan,
                    jenisMesin, jumlahPenumpang, jumlahKendaraan, deskrpsi, urlGambar);
             if (namaKendaraan.isEmpty()){
                 edtnamaKendaraan.setError("Masukkan nama kendaraan");
             } else if (tarifKendaraan.isEmpty()) {
                 edttarifKendaraan.setError("Masukkan tarif kendaraan");
             }else {
                 database.child(user.getUid()).child("Kendaraan").child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Toast.makeText(EditKendaraan.this, "data berhasil disimpan", Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(EditKendaraan.this, TemukanKendaraan.class));
                         finish();
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(EditKendaraan.this, "gagal menambah data, silahkan coba kembali", Toast.LENGTH_SHORT).show();
                     }
                 });
             }
        });
    }

    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(EditKendaraan.this,
                                    "com.example.cabs.fileprovider",
                                    photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, RC_Take_Photo);
                        }
                    }
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, RC_Take_From_Gallery);//one can be replaced with any action code

                } else if (options[item].equals("Cancel")) {
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

                        Glide.with(this).load(new File(currentPhotoPath)).centerCrop().into(uploadImage);
                        File f = new File(currentPhotoPath);
                        Uri contentUri = Uri.fromFile(f);

                        ////scaning masuk ke gallery android (opsional)//////////////////
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
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
                                Glide.with(this).load(new File(picturePath)).centerCrop().into(uploadImage);
                                cursor.close();
                            }
                            //upload file dari galeri ke storage
                            uploadToStorage(selectedImage);
                        }

                    }
                    break;

            }
        }
    }
    private void uploadToStorage(Uri file) {
        UploadTask uploadTask;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        String key;
        key = UUID.randomUUID().toString();

        // Buat path di Firebase Storage
        StorageReference fotoRef = mStorageRef.child(user.getUid() + "/kendaraan/" + key);
        uploadTask = fotoRef.putFile(file);
        Toast.makeText(EditKendaraan.this,
                "uploading Image",
                Toast.LENGTH_SHORT).show();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(EditKendaraan.this,
                        "can't upload Image, " + exception.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(EditKendaraan.this,
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
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage(EditKendaraan.this);

        } else {
            Toast.makeText(this, "denied", Toast.LENGTH_LONG).show();
        }
    }

}