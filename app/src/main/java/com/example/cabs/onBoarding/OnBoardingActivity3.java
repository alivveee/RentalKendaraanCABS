package com.example.cabs.onBoarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cabs.HomepageActivity;
import com.example.cabs.MainActivity;
import com.example.cabs.R;

public class OnBoardingActivity3 extends AppCompatActivity {
    ImageButton next ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboard_tiga);
        next = findViewById(R.id.btNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity3.this, MainActivity.class));
            }
        });
    }
}