package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference myref;
    ArrayList<FoodRequest> foodRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.foodrequests);
        myref = FirebaseDatabase.getInstance().getReference("FoodRequest");
        /*myref2 = FirebaseDatabase.getInstance().getReference("FoodRequestAcceptance");*/
        foodRequests = new ArrayList<>();
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FoodRequest foodRequest = snapshot.getValue(FoodRequest.class);
                    foodRequests.add(foodRequest);

                }
                Food_request_adapter food_request_adapter = new Food_request_adapter(MainActivity.this,foodRequests);
                listView.setAdapter(food_request_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

}
