package com.example.cabs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.ktx.Firebase;


public class RegisterActivity extends AppCompatActivity {

    EditText editText_email,editText_password,editText_nama;
    Button bt_continue;
    FirebaseAuth firebaseauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editText_email = findViewById(R.id.et_emailSignUp);
        editText_password = findViewById(R.id.et_passwordSignUp);
        bt_continue = findViewById(R.id.bt_continueSignUp);
        editText_nama = findViewById(R.id.et_namaSignUp);


        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email,password,nama;
                email = String.valueOf(editText_email.getText());
                password = String.valueOf(editText_password.getText());
                nama = String.valueOf(editText_nama.getText());


                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseauth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseauth.getCurrentUser();
                                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(nama).build();
                                    user.updateProfile(request);
                                    Toast.makeText(RegisterActivity.this, "Authentication Successfull", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            };
                        });

                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
