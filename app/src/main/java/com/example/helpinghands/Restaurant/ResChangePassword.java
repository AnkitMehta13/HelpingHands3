package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResChangePassword extends AppCompatActivity {

    EditText oldpswd, newpswd,confirmpswd;
    String remail,key,old,newps,confirm,password;
    ImageButton img;
    DatabaseReference myref;
    ArrayList<Restuarant> restuarantArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_change_password);
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        remail = sharedPreferences.getString("Remail", "???");
        myref = FirebaseDatabase.getInstance().getReference("Restaurant");
        restuarantArrayList = new ArrayList<>();
        oldpswd = (EditText)findViewById(R.id.oldpassword_res);
        newpswd = (EditText)findViewById(R.id.newpassword_res);
        confirmpswd = (EditText)findViewById(R.id.confirmpassword_res);
        img = (ImageButton)findViewById(R.id.changepassword_res);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Restuarant restuarant = snapshot.getValue(Restuarant.class);
                    restuarantArrayList.add(restuarant);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                old = oldpswd.getText().toString();
                newps = newpswd.getText().toString();
                confirm = confirmpswd.getText().toString();
                if(!newps.matches(confirm)){
                    confirmpswd.setError("Password should match");
                }
                else{
                    if (newps.length() < 6){
                        newpswd.setError("Password should be atleast 6 characters");
                    }
                    else{
                        for (int i=0;i<restuarantArrayList.size();i++){
                            if (remail.matches(restuarantArrayList.get(i).getMail())){
                                password = restuarantArrayList.get(i).getPswd();
                                if (!password.matches(old)){
                                    oldpswd.setError("Password Incorrect");
                                }
                                else{
                                    key = restuarantArrayList.get(i).getKey();
                                    myref.child(key).child("pswd").setValue(newps);
                                    Toast.makeText(ResChangePassword.this,"Password Changed",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ResChangePassword.this,Main3Activity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }

                }
            }
        });
    }
}
