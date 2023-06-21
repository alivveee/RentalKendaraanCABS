package com.example.cabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import io.github.muddz.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity {

    EditText editText_email,editText_password;

    TextInputLayout il_password,il_email;

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
        il_password = findViewById(R.id.il_passwordLogin);
        il_email = findViewById(R.id.il_emailLogin);

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
                    il_email.setError("Enter Your Email !");
                    return;
                }else {
                    il_email.setError("");
                    il_email.setHelperText("");
                }

                if(TextUtils.isEmpty(password)) {
                    il_password.setError("Enter Your Passowrd !");
                    return;
                } else {
                    il_password.setError("");
                    il_password.setHelperText("");
                }

                firebaseauth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    StyleableToast.makeText(MainActivity.this, "Login Successful", R.style.mytoast).show();
                                    Intent intent = new Intent(MainActivity.this, HomepageActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    il_password.setError("Wrong Password !");
                                }
                            }
                        });
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseauth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
    private void reload(){
        FirebaseUser currentUser = firebaseauth.getCurrentUser();
        StyleableToast.makeText(MainActivity.this, "Welcome "+ currentUser.getDisplayName(), R.style.mytoast).show();
        startActivity(new Intent(this, HomepageActivity.class));
    }
}