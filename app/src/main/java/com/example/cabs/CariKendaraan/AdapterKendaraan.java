package com.example.cabs.CariKendaraan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cabs.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterKendaraan  extends RecyclerView.Adapter<AdapterKendaraan.MyViewHolder> {
    private List<ModelKendaraan> mList;

    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterKendaraan(List<ModelKendaraan> mList, Activity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterKendaraan.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item_kendaraam, parent, false );
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterKendaraan.MyViewHolder holder, int position) {
        final ModelKendaraan data = mList.get(position);
        holder.tvNamaKendaraan.setText(data.getNamaKendaraan());
        holder.tvTarifKendaraan.setText(data.getTarifKendaraan()+" / hari");

        // Memuat gambar menggunakan Glide
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL); // Opsional: Menggunakan cache untuk gambar yang dimuat
        Glide.with(activity)
                .load(data.getUrlGambar())
                .apply(requestOptions)
                .into(holder.gambarKendaraan);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNamaKendaraan, tvTarifKendaraan;
        ImageView gambarKendaraan;
        CardView cardItemKendaraan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKendaraan = itemView.findViewById(R.id.tv_namaKendaraan);
            tvTarifKendaraan = itemView.findViewById(R.id.tv_tarifKendaraan);
            cardItemKendaraan = itemView.findViewById(R.id.card_item_kendaraan);
            gambarKendaraan = itemView.findViewById(R.id.img_kendaraan);

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

            activity.startActivity(intent);
            }
        }
    }
