package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Res_Feedback extends AppCompatActivity {

    String mail,feedback,rname;
    EditText feedbck;
    Button sbtn;
    DatabaseReference myref,myref2;
    ArrayList<Restuarant> restuarantArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res__feedback);

        feedbck = (EditText)findViewById(R.id.feedback_res);
        myref = FirebaseDatabase.getInstance().getReference("FeedbackRes");
        myref2 = FirebaseDatabase.getInstance().getReference("Restaurant");
        sbtn = (Button)findViewById(R.id.sbtn_res_feedback);
        restuarantArrayList = new ArrayList<>();

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
                Intent intent = new Intent(Res_Feedback.this,Main3Activity.class);
                startActivity(intent);
            }
        });


    }

    private void getdata() {
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        mail = sharedPreferences.getString("Remail", "???");
        myref2.addValueEventListener(new ValueEventListener() {
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

        for(int i=0; i<restuarantArrayList.size(); i++){
            if (mail.matches(restuarantArrayList.get(i).getMail())){
                rname = restuarantArrayList.get(i).getResname();
            }
        }
        feedback = feedbck.getText().toString();
        String key = myref.push().getKey();
        Feedbackres feedbackres = new Feedbackres(key,mail,rname,feedback);
        myref.child(key).setValue(feedbackres);

    }


}
