package com.example.cabs.CariPenyewa;

import android.app.Activity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cabs.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterPenyewa extends RecyclerView.Adapter<AdapterPenyewa.MyViewHolder> {
    private List<ModelPenyewa> mList;
    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public AdapterPenyewa(List<ModelPenyewa> mList, Activity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterPenyewa.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.layout_item_penyewa, parent, false );
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPenyewa.MyViewHolder holder, int position) {
        final ModelPenyewa data = mList.get(position);
        holder.tvNama.setText(data.getNama());
        holder.tvTanggal.setText(data.getTanggal());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal;
        CardView cardItemKendaraan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            cardItemKendaraan = itemView.findViewById(R.id.card_item_kendaraan);

        }
    }
}
