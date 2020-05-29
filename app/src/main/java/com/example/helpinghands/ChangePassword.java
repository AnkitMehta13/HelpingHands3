package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.helpinghands.Restaurant.Main3Activity;
import com.example.helpinghands.Restaurant.ResChangePassword;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangePassword extends AppCompatActivity {
    EditText oldpswd, newpswd,confirmpswd;
    String vemail,key,old,newps,confirm,password;
    ImageButton img;
    DatabaseReference myref;
    ArrayList<Volunteer> volunteerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        vemail = sharedPreferences.getString("Vemail", "???");
        myref = FirebaseDatabase.getInstance().getReference("Restaurant");
        volunteerArrayList = new ArrayList<>();
        oldpswd = (EditText)findViewById(R.id.oldpassword);
        newpswd = (EditText)findViewById(R.id.newpassword);
        confirmpswd = (EditText)findViewById(R.id.confirmpassword);
        img = (ImageButton)findViewById(R.id.changepassword);
        //volunteer refernce
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Volunteer volunteer= snapshot.getValue(Volunteer.class);
                    volunteerArrayList.add(volunteer);
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
                    else {
                        for (int i=0;i<volunteerArrayList.size();i++){
                            if (vemail.matches(volunteerArrayList.get(i).getEmail())){
                                password = volunteerArrayList.get(i).getPassword();
                                if (!password.matches(old)){
                                    oldpswd.setError("Password Incorrect");
                                }
                                else{
                                    key = volunteerArrayList.get(i).getKey();
                                    myref.child(key).child("password").setValue(newps);
                                    Toast.makeText(ChangePassword.this,"Password Changed",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePassword.this, MainActivity.class);
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
