package com.example.cabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cabs.CariKendaraan.TemukanKendaraan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomepageActivity extends AppCompatActivity {
    LinearLayout logOut;
    Button bt_informasiKendaraan;
    TextView displayname;

    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseauth.getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        logOut = findViewById(R.id.logOut);
        displayname = findViewById(R.id.textView3);
        bt_informasiKendaraan = findViewById(R.id.bt_informasiKendaraan);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_informasiKendaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageActivity.this,TemukanKendaraan.class);
                startActivity(intent);
                finish();
            }
        });
    }


}