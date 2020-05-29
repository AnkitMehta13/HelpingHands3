package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.helpinghands.FoodRequestAcceptance;
import com.example.helpinghands.R;
import com.example.helpinghands.Volunteer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView listView;
    DatabaseReference myref,resref;
    String remail,rname;
    ArrayList<Restuarant> restuarantArrayList;
    ArrayList<FoodRequestAcceptance> foodRequestAcceptances;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = (ListView)findViewById(R.id.vol_list);
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        remail = sharedPreferences.getString("Remail", "???");
        myref = FirebaseDatabase.getInstance().getReference("FoodRequestAcceptance");
        resref = FirebaseDatabase.getInstance().getReference("Restaurant");
        foodRequestAcceptances = new ArrayList<>();
        restuarantArrayList = new ArrayList<>();
        resref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Restuarant restuarant = snapshot.getValue(Restuarant.class);
                    restuarantArrayList.add(restuarant);
                }
                for(int i=0;i<restuarantArrayList.size();i++){
                    if (remail.matches(restuarantArrayList.get(i).getMail())){
                        rname = restuarantArrayList.get(i).getResname();
                        myref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    FoodRequestAcceptance foodRequestAcceptance = snapshot.getValue(FoodRequestAcceptance.class);
                                    if (rname.matches(foodRequestAcceptance.getRname())){
                                        foodRequestAcceptances.add(foodRequestAcceptance);
                                    }
                                }
                                Res_recent_adapter resRecentAdapter = new Res_recent_adapter(History.this,foodRequestAcceptances);
                                listView.setAdapter(resRecentAdapter);
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
