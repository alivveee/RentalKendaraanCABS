//package com.example.cabs.CariPenyewa;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.util.Pair;
//import androidx.transition.Visibility;
//
//import com.example.cabs.MainActivity;
//import com.example.cabs.R;
//import com.example.cabs.RegisterActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.datepicker.MaterialDatePicker;
//import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import io.github.muddz.styleabletoast.StyleableToast;
//
//
//
//public class TambahPenyewa extends AppCompatActivity {
//
//    String [] item = {"Mio Karbu","Ora Beat Ora Sweet","RX King Filter","Vespa Kaleng","Mio Mber","Vixion Jari-jari"};
//    String imageUrl;
//
//    Uri uri;
//
//    ArrayAdapter<String> adapterItems;
//
//    AutoCompleteTextView autoCompleteTextView;
//
//    ImageView date;
//    TextInputEditText tanggal ,nama,alamat,no_hp;
//
//
//    ImageView upload_image;
//
//    Button submit;
//
//    @Override
//    protected void onCreate( Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tambah_penyewa);
//
//        tanggal = findViewById(R.id.et_tanggal);
//        nama = findViewById(R.id.et_nama);
//        alamat = findViewById(R.id.et_alamat);
//        no_hp = findViewById(R.id.et_no_hp);
//        upload_image = findViewById(R.id.upload_image);
//        submit = findViewById(R.id.bt_submit);
//        date = findViewById(R.id.date);
//
//        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if(result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = result.getData();
//                            uri = data.getData();
//                            upload_image.setImageURI(uri);
//
//                        }else {
//                            StyleableToast.makeText(TambahPenyewa.this, "No Image Selected", R.style.mytoast).show();
//                        }
//                    }
//                }
//        );
//
//
//       upload_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent photoPicker = new Intent(Intent.ACTION_PICK);
//                photoPicker.setType("image/*");
//                activityResultLauncher.launch(photoPicker);
//            }
//        });
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveData();
//            }
//        });
//
//
//
//
//        TextInputEditText textInputEditText = tanggal;
//
//        MaterialDatePicker datePicker = MaterialDatePicker.Builder.dateRangePicker()
//                .setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),
//                        MaterialDatePicker.todayInUtcMilliseconds())).build();
//
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePicker.show(getSupportFragmentManager(),"Material_Range");
//                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//                    @Override
//                    public void onPositiveButtonClick(Object selection) {
//                        textInputEditText.setText(datePicker.getHeaderText());
//                        StyleableToast.makeText(TambahPenyewa.this,"tanggal disewa dari " + datePicker.getHeaderText(),R.style.mytoast).show();
//                    }
//                });
//            }
//        });
//
//        autoCompleteTextView = findViewById(R.id.tv_merk_kendaraan);
//        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_merk_kendaraan,item);
//
//        autoCompleteTextView.setAdapter(adapterItems);
//
//        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String item = adapterView.getItemAtPosition(position).toString();
//                StyleableToast.makeText(TambahPenyewa.this,item + " dipilih",R.style.mytoast).show();
//            }
//        });
//
//
//    }
//    public void saveData() {
//
//        if (uri == null) {
//            StyleableToast.makeText(TambahPenyewa.this, "No Image Selected", R.style.mytoast).show();
//            return;
//        }
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Penyewa")
//                .child(uri.getLastPathSegment());
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(TambahPenyewa.this);
//        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                while (!uriTask.isComplete());
//                Uri urlImage = uriTask.getResult();
//                imageUrl = urlImage.toString();
//                uploadData();
//
//                dialog.dismiss();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                dialog.dismiss();
//            }
//        });
//
//    }
//
//    public void uploadData() {
//
//        String upload_nama = nama.getText().toString();
//        String upload_alamat = alamat.getText().toString();
//        String upload_no_hp = no_hp.getText().toString();
//        String upload_merk_kendaraan = autoCompleteTextView.getText().toString();
//        String upload_tanggal = tanggal.getText().toString();
//
////        ModelPenyewa modelPenyewa = new ModelPenyewa(upload_nama,upload_alamat,upload_no_hp,upload_merk_kendaraan,upload_tanggal);
//
////        FirebaseDatabase.getInstance().getReference("Tambah Penyewa").child(upload_nama)
////                .setValue(modelPenyewa).addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        if(task.isSuccessful()){
////                            StyleableToast.makeText(TambahPenyewa.this, "Saved", R.style.mytoast).show();
////                            finish();
////                        }
////                    }
////                }).addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        StyleableToast.makeText(TambahPenyewa.this, e.getMessage().toString(), R.style.mytoast).show();
////                    }
////                });
//
//
//
//
//
//
//
//    }
//}
//
//
