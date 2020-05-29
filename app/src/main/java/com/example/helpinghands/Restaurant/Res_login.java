package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.ForgotPassword;
import com.example.helpinghands.Login;
import com.example.helpinghands.Main2Activity;
import com.example.helpinghands.R;
import com.example.helpinghands.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Res_login extends AppCompatActivity {

    EditText pswd,usrusr;
    TextView sup,lin, forgotpass;
    FirebaseAuth fAuth;
    String email, password;
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_login2);

        forgotpass = (TextView) findViewById(R.id.forgotpass);
        lin = findViewById(R.id.login);
        usrusr = findViewById(R.id.user);
        pswd = findViewById(R.id.password_res);
        sup = findViewById(R.id.signup);
        fAuth = FirebaseAuth.getInstance();
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Res_login.this, Res_Signup.class);
                startActivity(it);
            }
        });

        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                email = usrusr.getText().toString().trim();
                Log.e("email",email);
                password = pswd.getText().toString().trim();
                if (isnotnull()){
                    savedata();
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Res_login.this,"Login Sucess",Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(Res_login.this, Main3Activity.class);
                                startActivity(it);
                            }
                            else{
                                Toast toast = Toast.makeText(Res_login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Res_login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });





    }

    private void savedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Remail", email);
        editor.commit();
    }


    private Boolean isnotnull() {
        boolean notnull = false;
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        Boolean check = true;
        if (!matcher.matches()) {
            usrusr.setError("Email not Valid");
            check = false;
        }
        if (email.length() == 0){
            usrusr.setError("Email cannot be empty");
            check = false;
        }
        if (password.length() == 0)
        {
            pswd.setError("Password cannot be empty");
            check = false;
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }
}
