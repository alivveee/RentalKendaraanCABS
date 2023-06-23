package com.example.cabs.onBoarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cabs.R;

public class OnBoardingActivity2 extends AppCompatActivity {
    ImageButton next ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_dua);
        next = findViewById(R.id.bttNext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity2.this, OnBoardingActivity3.class));
            }
        });
    }
}