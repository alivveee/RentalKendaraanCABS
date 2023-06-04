package com.example.cabs.CariKendaraan;

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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaKendaraan, tvTarifKendaraan;
        CardView cardItemKendaraan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaKendaraan = itemView.findViewById(R.id.tv_namaKendaraan);
            tvTarifKendaraan = itemView.findViewById(R.id.tv_tarifKendaraan);
            cardItemKendaraan = itemView.findViewById(R.id.card_item_kendaraan);

        }
    }
}
