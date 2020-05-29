package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Feedback extends AppCompatActivity {

    String mail,feedback,vname;
    EditText feedbck;
    Button sbtn;
    DatabaseReference myref,myref2;
    ArrayList<Volunteer> volunteerArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        mail = sharedPreferences.getString("Vemail", "???");
        feedbck = (EditText)findViewById(R.id.feedback);
        myref = FirebaseDatabase.getInstance().getReference("FeedbackVol");
        myref2 = FirebaseDatabase.getInstance().getReference("Volunteer");
        sbtn = (Button)findViewById(R.id.sbtn_feedback);
        volunteerArrayList = new ArrayList<>();
        myref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Volunteer volunteer = snapshot.getValue(Volunteer.class);
                    volunteerArrayList.add(volunteer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        for(int i=0; i<volunteerArrayList.size(); i++){
            if (mail.matches(volunteerArrayList.get(i).getEmail())){
                vname = volunteerArrayList.get(i).getName();
            }
        }

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback = feedbck.getText().toString();
                String key = myref.push().getKey();
                Feedbackvol feedbackvol = new Feedbackvol(key,mail,vname,feedback);
                myref.child(key).setValue(feedbackvol);
                Intent intent = new Intent(Feedback.this,Main2Activity.class);
            }
        });


    }
}
