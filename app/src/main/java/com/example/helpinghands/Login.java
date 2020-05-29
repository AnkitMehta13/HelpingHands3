package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText pswd,usrusr;
    TextView sup,lin, forgotpass;
    FirebaseAuth fAuth;
    String email, password;
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen
        setContentView(R.layout.activity_login);

        forgotpass = (TextView) findViewById(R.id.forgot_pass);
        lin = findViewById(R.id.lin);
        usrusr = findViewById(R.id.usrusr);
        pswd = findViewById(R.id.pswrdd);
        sup = findViewById(R.id.sup);
        fAuth = FirebaseAuth.getInstance();
        sup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Login.this, Signup.class);
                startActivity(it);
            }
        });

        lin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                email = usrusr.getText().toString().trim();
                password = pswd.getText().toString().trim();
                if (isnotnull()){
                    savedata();
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(Login.this,"Login Sucess",Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(Login.this, Main2Activity.class);
                                startActivity(it);
                            }
                            else{
                                Toast toast = Toast.makeText(Login.this, "Login Failed" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });
                }

            }


        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });





    }

    private void savedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Vemail", email);
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
