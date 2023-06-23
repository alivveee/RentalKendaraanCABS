package com.example.cabs.CariKendaraan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cabs.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdapterKendaraan  extends RecyclerView.Adapter<AdapterKendaraan.MyViewHolder> {
    private List<ModelKendaraan> mList;
    private Context context;
    private Activity activity;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterKendaraan(List<ModelKendaraan> mList, Activity activity, Context context) {
        this.mList = mList;
        this.activity = activity;
        this.context = context; // Inisialisasi objek context
    }

    @NonNull
    @Override
    public AdapterKendaraan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item_kendaraam, parent, false );
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKendaraan.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ModelKendaraan data = mList.get(position);

        holder.tvNamaKendaraan.setText(data.getNamaKendaraan());
        holder.tvTarifKendaraan.setText(data.getTarifKendaraan()+" / hari");

        // Memuat gambar menggunakan Glide
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Opsional: Menggunakan cache untuk gambar yang dimuat
        Glide.with(activity)
                .load(data.getUrlGambar())
                .apply(requestOptions).centerCrop()
                .into(holder.gambarKendaraan);

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hapus item dari database menggunakan referensi
                database.child("Kendaraan").child(data.getKey()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Item berhasil dihapus dari database
                                Toast.makeText(activity, "Item dihapus", Toast.LENGTH_SHORT).show();

                                mList.remove(position);
                                notifyDataSetChanged();

                                // hapus gambar dari storage
                                StorageReference fileRef = storage.child(user.getUid()).child("kendaraan").child(data.getKey());
                                fileRef.delete();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Terjadi kesalahan saat menghapus item dari database
                                Toast.makeText(activity, "Gagal menghapus item", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        holder.bteditt.setOnClickListener(v -> {
            Intent editForm = new Intent(activity, EditKendaraan.class);
            editForm.putExtra("key", data.getKey());
            editForm.putExtra("Nama", data.getNamaKendaraan());
            editForm.putExtra("Tahun", data.getTahunKendaraan());
            editForm.putExtra("Tarif", data.getTarifKendaraan());
            editForm.putExtra("JenisMesin", data.getJenisMesin());
            editForm.putExtra("Jumlah Penumpang", data.getJumlahPenumpang());
            editForm.putExtra("Jumlah Kendaraan", data.getJumlahKendaraan());
            editForm.putExtra("Deskripsi", data.getDeskripsi());
            editForm.putExtra("gambar", data.getUrlGambar());
            activity.startActivity(editForm);
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaKendaraan, tvTarifKendaraan;
        EditText edtnamaKendaraan, edttahunKendaraan, edttarifKendaraan
            , edtjenisMesin, edtjumlahPenumpang, edtjumlahKendaraan, edtdeskripsiKendaraan; ;
        ImageView gambarKendaraan, uploadImage,bteditt, btDelete;
        AutoCompleteTextView etJenisMesin;
        Button btsave;
        CardView cardItemKendaraan, btUpload;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKendaraan = itemView.findViewById(R.id.tv_namaKendaraan);
            tvTarifKendaraan = itemView.findViewById(R.id.tv_tarifKendaraan);
            cardItemKendaraan = itemView.findViewById(R.id.card_item_kendaraan);
            gambarKendaraan = itemView.findViewById(R.id.img_kendaraan);
            edtnamaKendaraan = itemView.findViewById(R.id.edt_namaKendaraan);
            edttahunKendaraan = itemView.findViewById(R.id.edt_tahunKendaraan);
            edttarifKendaraan = itemView.findViewById(R.id.edt_tarifKendaraan);
            edtjenisMesin = itemView.findViewById(R.id.edt_jenisMesin);
            edtjumlahPenumpang = itemView.findViewById(R.id.edt_jumlahPenumpang);
            edtjumlahKendaraan = itemView.findViewById(R.id.edt_jumlahKendaraan);
            edtdeskripsiKendaraan = itemView.findViewById(R.id.edt_deskripsiKendaraan);
            btsave = itemView.findViewById(R.id.bt_edt_submit);
            btUpload = itemView.findViewById(R.id.card_upload_image);
            uploadImage = itemView.findViewById(R.id.upload_imgg);
            bteditt = itemView.findViewById(R.id.bt_editt);
            btDelete = itemView.findViewById(R.id.bt_delete);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            // Dapatkan data ModelKendaraan yang sesuai dengan posisi
            ModelKendaraan data = mList.get(getBindingAdapterPosition());

            // Lakukan intent ke halaman detail dengan membawa data yang diperlukan
            Intent intent = new Intent(activity, DetailKendaraan.class);
            intent.putExtra("namaKendaraan", data.getNamaKendaraan());
            intent.putExtra("tahunKendaraan", data.getTahunKendaraan());
            intent.putExtra("tarifKendaraan", data.getTarifKendaraan());
            intent.putExtra("jenisMesin", data.getJenisMesin());
            intent.putExtra("jumlahPenumpang", data.getJumlahPenumpang());
            intent.putExtra("jumlahKendaraan", data.getJumlahKendaraan());
            intent.putExtra("deskripsi", data.getDeskripsi());
            intent.putExtra("url", data.getUrlGambar());

            activity.startActivity(intent);
            }
        }
    }
