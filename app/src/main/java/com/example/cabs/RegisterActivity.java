package com.example.cabs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.ktx.Firebase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.muddz.styleabletoast.StyleableToast;


public class RegisterActivity extends AppCompatActivity {

    EditText editText_email,editText_password,editText_nama;

    TextInputLayout il_password,il_nama,il_email;
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
        il_password = findViewById(R.id.il_passwordSignUp);
        il_email = findViewById(R.id.il_emailSignUp);
        il_nama = findViewById(R.id.il_namaSignUp);

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email,password,nama;
                email = String.valueOf(editText_email.getText());
                password = String.valueOf(editText_password.getText());
                nama = String.valueOf(editText_nama.getText());


                if(TextUtils.isEmpty(email)) {
                   il_email.setError("Enter Your Email !");
                   return;
                }else {
                    il_email.setError("");
                    il_email.setHelperText("");
                }


                if(TextUtils.isEmpty(nama)) {
                    il_nama.setError("Enter Your Name !");
                    return;
                }else {
                    il_nama.setError("");
                    il_nama.setHelperText("");
                }

                if(TextUtils.isEmpty(password)) {
                    il_password.setError("Enter Your Password !");
                    return;
                }else {
                    il_nama.setError("");
                    il_nama.setHelperText("");
                }



                firebaseauth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseauth.getCurrentUser();
                                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(nama).build();
                                    user.updateProfile(request);
                                    StyleableToast.makeText(RegisterActivity.this, "Authentication Successfull", R.style.mytoast).show();
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    StyleableToast.makeText(RegisterActivity.this, "Authentication Failed", R.style.mytoast).show();
                                }
                            };
                        });

                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        editText_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = charSequence.toString();
                if (password.length() >= 8) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(password);
                    boolean isPwdContainSpeChar = matcher.find();
                    if(isPwdContainSpeChar) {
                        il_password.setHelperText("Strong Password");
                        int helperTextColor = getResources().getColor(R.color.teal_200);
                        il_password.setError("");
//                        il_password.setCounterTextColor(ColorStateList.valueOf(helperTextColor));
//                        il_password.setHintTextColor(ColorStateList.valueOf(helperTextColor));
                        il_password.setBoxStrokeErrorColor(ColorStateList.valueOf(helperTextColor));
                        il_password.setHelperTextColor(ColorStateList.valueOf(helperTextColor));
                    }else {
                        il_password.setHelperText("");
                        il_password.setError("Weak Password. Include minimum 1 special char");
                    }
                }else {
                    il_password.setHelperText("Enter Minimum 8 char");
                    il_password.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
}
