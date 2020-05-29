package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Food_request_status extends AppCompatActivity {

    TextView resname,resadd,desc;
    ImageView img;
    String rname,radd,fooddesc,keyfr,vemail,vname,status,imageid;
    ImageButton accept,decline;
    DatabaseReference myref,myref2,volref;
    StorageReference storageReference;
    ArrayList<Volunteer> volunteerArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_request_status);
        final Intent intent = getIntent();
        rname = intent.getStringExtra("Rname");
        radd = intent.getStringExtra("Location");
        fooddesc = intent.getStringExtra("Description");
        keyfr = intent.getStringExtra("Key");
        imageid = intent.getStringExtra("Imageid");
        resname = (TextView)findViewById(R.id.resname_foodrequeststatus);
        resadd = (TextView)findViewById(R.id.location_res);
        desc = (TextView)findViewById(R.id.fooddescription);
        accept = (ImageButton)findViewById(R.id.accept);
        decline = (ImageButton)findViewById(R.id.decline);
        img = (ImageView)findViewById(R.id.res_image_status);
        myref = FirebaseDatabase.getInstance().getReference("FoodRequestAcceptance");
        myref2 = FirebaseDatabase.getInstance().getReference("FoodRequest");
        volref = FirebaseDatabase.getInstance().getReference("Volunteer");
        volunteerArrayList = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        vemail = sharedPreferences.getString("Vemail", "???");


        resname.setText(rname);
        resadd.setText(radd);
        desc.setText(fooddesc);
        storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(imageid);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                img.setImageBitmap(bm);
            }
        });

        volref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Volunteer volunteer = snapshot.getValue(Volunteer.class);
                    //volunteerArrayList.add(volunteer);
                    if (vemail.matches(volunteer.getEmail())) {
                        vname = volunteer.getName();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("hi",vname);
                status = "Accepted";
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                String key = myref.push().getKey();
                FoodRequestAcceptance foodRequestAcceptance = new FoodRequestAcceptance(key,vname,rname,radd,formattedDate,status);
                myref.child(key).setValue(foodRequestAcceptance);
                Log.e("Key",key);
                Toast.makeText(Food_request_status.this,"Order Accepted",Toast.LENGTH_SHORT).show();
                myref2.child(keyfr).removeValue();
                Intent intent1 =  new Intent(Food_request_status.this,MainActivity.class);
                startActivity(intent1);
            }
        });




        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Food_request_status.this,"Order Rejected",Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(Food_request_status.this,Main2Activity.class);
                startActivity(intent);
            }
        });

    }
}
