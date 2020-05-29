package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentVol extends AppCompatActivity {

    ListView listView;
    DatabaseReference myref,volref;
    String vemail,vname;
    ArrayList<Volunteer> volunteerArrayList;
    ArrayList<FoodRequestAcceptance> foodRequestAcceptances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_vol);

        listView = (ListView)findViewById(R.id.volrecentlist);
        myref = FirebaseDatabase.getInstance().getReference("FoodRequestAcceptance");
        volref = FirebaseDatabase.getInstance().getReference("Volunteer");
        foodRequestAcceptances = new ArrayList<>();
        volunteerArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        vemail = sharedPreferences.getString("Vemail", "???");


        volref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Volunteer volunteer = snapshot.getValue(Volunteer.class);
                    if (vemail.matches(volunteer.getEmail())) {
                        vname = volunteer.getName();
                        Log.e("name", vname);
                        myref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    FoodRequestAcceptance foodRequestAcceptance = snapshot.getValue(FoodRequestAcceptance.class);
                                    if (vname.matches(foodRequestAcceptance.getVname())){
                                        foodRequestAcceptances.add(foodRequestAcceptance);
                                        Log.e("VNAME",foodRequestAcceptances.get(0).getRname());
                                    }
                                }
                                Vol_res_adapter vol_res_adapter =new Vol_res_adapter(RecentVol.this,foodRequestAcceptances);
                                listView.setAdapter(vol_res_adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
