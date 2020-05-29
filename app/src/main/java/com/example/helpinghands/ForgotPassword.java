package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {
    Button btn;
    EditText email;
    String mail;
    FirebaseAuth fAuth;
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        setContentView(R.layout.activity_forgot_password);
        btn = (Button) findViewById(R.id.btn);
        email = (EditText) findViewById(R.id.forgotpassword);
        fAuth = FirebaseAuth.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mail = email.getText().toString().trim();
                if (isnotnull()) {
                    fAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(ForgotPassword.this, "A password reset link has been send to your Email Address, Kindly Reset your Password", Toast.LENGTH_LONG);
                                toast.show();
                                ForgotPassword.super.onBackPressed();
                            } else {
                                Toast toast = Toast.makeText(ForgotPassword.this, "Action Failed" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });
    }

    private Boolean isnotnull() {
        boolean notnull = false;
        CharSequence inputStr = mail;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        Boolean check = true;
        if (!matcher.matches()) {
            email.setError("Email not Valid");
            check = false;
        }
        if (mail.length() == 0){
            email.setError("Email cannot be empty");
            check = false;
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }
}
