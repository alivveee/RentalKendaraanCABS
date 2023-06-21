package com.example.cabs.CariPenyewa;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cabs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterPenyewa extends RecyclerView.Adapter<AdapterPenyewa.MyViewHolder> {
    private List<ModelPenyewa> mList;
    Context mContext;
    private Activity activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    private List<ModelPenyewa> mListFull;

    Dialog dialog;

    public AdapterPenyewa(List<ModelPenyewa> mList,Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.mListFull = new ArrayList<>(mList);
    }

    @NonNull
    @Override
    public AdapterPenyewa.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View viewItem = inflater.inflate(R.layout.layout_item_penyewa, parent, false );

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.layout_item_penyewa,parent,false);

        final MyViewHolder vHolder = new MyViewHolder(v);

        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_contact);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        vHolder.bt_dialog_call = dialog.findViewById(R.id.bt_dialog_call);
        vHolder.bt_dialog_wa = dialog.findViewById(R.id.bt_dialog_wa);



        vHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dialog_name = (TextView) dialog.findViewById(R.id.dialog_name);
                TextView dialog_phone_number = (TextView) dialog.findViewById(R.id.dialog_phone_number);
                ImageView dialog_image = (ImageView) dialog.findViewById(R.id.dialog_image);
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String imageUrl = "gs://cabs-9cb90.appspot.com/" + user.getUid() + "/image/" + user.getUid() + ".jpg";


                Glide.with(mContext)
                        .load(mList.get(vHolder.getAdapterPosition()).getUri())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .circleCropTransform()) // Mengatur efek lingkaran pada gambar
                        .into(dialog_image);

                dialog_name.setText(mList.get(vHolder.getAdapterPosition()).getNama());
                dialog_phone_number.setText(mList.get(vHolder.getAdapterPosition()).getNo_hp());
                Toast.makeText(mContext, "Test Click : " + String.valueOf(vHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
//                Picasso.get()
//                        .load(imageUrl)
//                        .into(dialog_image);
                dialog.show();




            }
        });

        return vHolder;
    }







    @Override
    public void onBindViewHolder(@NonNull AdapterPenyewa.MyViewHolder holder, int position) {
        final ModelPenyewa data = mList.get(position);
        holder.tvNama.setText(data.getNama());
        holder.tvTanggal.setText(data.getTanggal());
        holder.tvNamaKendaraan.setText(data.getNama_kendaraan());
        holder.tvTotal.setText(data.getTotal());
        Glide.with(mContext)
                .load(data.getUri())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.gambar))  // Placeholder image while loading
                .into(holder.iv_kendaraan);


        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        holder.bt_dialog_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = data.getNo_hp();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                if (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                mContext.startActivity(intent);
            }
        });

        holder.bt_dialog_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getPhoneNumber = data.getNo_hp();
                String phoneNumber = "+" + getPhoneNumber;
                String message = "Halo, Kami Dari Cabs";
                String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvTanggal,tvNamaKendaraan,tvTotal;
        CardView cardItemKendaraan;
        LinearLayout item;
        Button bt_dialog_call,bt_dialog_wa;

        ImageView iv_kendaraan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            cardItemKendaraan = itemView.findViewById(R.id.card_item_kendaraan);
            item = itemView.findViewById(R.id.item);
            tvNamaKendaraan = itemView.findViewById(R.id.tv_namaKendaraan);
            tvTotal = itemView.findViewById(R.id.tv_harga);
            iv_kendaraan = itemView.findViewById(R.id.iv_kendaraan);



        }
    }


}
