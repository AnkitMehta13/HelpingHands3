package com.example.helpinghands;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpinghands.Restaurant.Res_login;

public class chooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);

        ImageView img1, img2;
        img1 = (ImageView) findViewById(R.id.Res);
        img2 = (ImageView) findViewById(R.id.Vol);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chooser.this, Res_login.class);
                startActivity(intent);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chooser.this, Login.class);
                startActivity(intent);
            }
        });


    }
}
