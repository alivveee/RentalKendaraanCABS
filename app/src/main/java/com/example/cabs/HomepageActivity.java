package com.example.cabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabs.CariKendaraan.TemukanKendaraan;
import com.example.cabs.CariPenyewa.TemukanPenyewa;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomepageActivity extends AppCompatActivity {
    LinearLayout logOut;
    Button bt_informasiKendaraan;
    Button bt_daftarSewa;
    TextView displayname;

    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

    private boolean doubleBackToExitPressedOnce = false;
    private static final long DOUBLE_BACK_PRESS_DURATION = 2000; // Durasi jeda dalam milidetik

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true); // Menutup aplikasi
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, DOUBLE_BACK_PRESS_DURATION);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        logOut = findViewById(R.id.logOut);
        displayname = findViewById(R.id.textView3);
        bt_informasiKendaraan = findViewById(R.id.bt_informasiKendaraan);
        bt_daftarSewa = findViewById(R.id.bt_daftarSewa);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseauth.signOut();
                Intent logout = new Intent(HomepageActivity.this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
            }
        });

        bt_informasiKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, TemukanKendaraan.class);
                startActivity(intent);
            }
        });

        bt_daftarSewa.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this, TemukanPenyewa.class);
                startActivity(intent);
            }
        }));
    }


}