package com.example.cabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    EditText editText_email,editText_password;
    Button signIn,signUp;

    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        editText_email = findViewById(R.id.et_emailLogIn);
        editText_password = findViewById(R.id.et_passwordLogIn);
        signIn = findViewById(R.id.bt_login);
        signUp = findViewById(R.id.bt_signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                email = String.valueOf(editText_email.getText());
                password = String.valueOf(editText_password.getText());

                if(TextUtils.isEmpty(email)) {
                    StyleableToast.makeText(MainActivity.this, "Please Enter Your Email", R.style.mytoast).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    StyleableToast.makeText(MainActivity.this, "Please Enter Your Password", R.style.mytoast).show();
                    return;
                }

                firebaseauth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    StyleableToast.makeText(MainActivity.this, "Login Successfull", R.style.mytoast).show();
                                    Intent intent = new Intent(MainActivity.this,HomepageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    StyleableToast.makeText(MainActivity.this, "Login Failed", R.style.mytoast).show();

                                }
                            }
                        });
            }
        });
    }
}