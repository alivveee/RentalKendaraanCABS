package com.example.cabs.onBoarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cabs.MainActivity;
import com.example.cabs.R;
import com.example.cabs.onBoarding.OnBoardingActivity2;

public class OnBroardingActivity1 extends AppCompatActivity {

    Button next;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "onboarding_pref";
    private static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_satu);
        next = findViewById(R.id.btn_next);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Cek status onboarding
        boolean isOnboardingCompleted = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false);

        if (!isOnboardingCompleted) {
            LinearLayout linearLayoutOnboarding = findViewById(R.id.onBoardingSatuCard);
            Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
            translateAnimation.setDuration(500);
            linearLayoutOnboarding.startAnimation(translateAnimation);
        } else {
            // Jika onboarding sudah selesai, langsung lanjut ke halaman berikutnya
            startActivity(new Intent(OnBroardingActivity1.this, MainActivity.class));
            finish();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menandai bahwa onboarding telah selesai
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_ONBOARDING_COMPLETED, true);
                editor.apply();

                startActivity(new Intent(OnBroardingActivity1.this, OnBoardingActivity2.class));
                finish();
            }
        });
    }
}
