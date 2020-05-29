package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.FoodRequest;
import com.example.helpinghands.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Res_home extends AppCompatActivity {

    TextView s1,s2,s3,s4,s5;
    EditText it1,it2,it3,it4,it5,qty1,qty2,qty3,qty4,qty5;
    String item1,item2,item3,item4,item5,qnty1,qnty2,qnty3,qnty4,qnty5,senddata,key,remail,rphone,rname,radd;
    Button placeorder;
    DatabaseReference myref,resref;
    ArrayList<Restuarant> restuarantArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_home);

        s1 = (TextView) findViewById(R.id.serial1);
        s2 = (TextView) findViewById(R.id.serial2);
        s3 = (TextView) findViewById(R.id.serial3);
        s4 = (TextView) findViewById(R.id.serial4);
        s5 = (TextView) findViewById(R.id.serial5);
        it1 = (EditText) findViewById(R.id.item1);
        it2 = (EditText) findViewById(R.id.item2);
        it3 = (EditText) findViewById(R.id.item3);
        it4 = (EditText) findViewById(R.id.item4);
        it5 = (EditText) findViewById(R.id.item5);
        qty1 = (EditText) findViewById(R.id.quantity1);
        qty2 = (EditText) findViewById(R.id.quantity2);
        qty3 = (EditText) findViewById(R.id.quantity3);
        qty4 = (EditText) findViewById(R.id.quantity4);
        qty5 = (EditText) findViewById(R.id.quantity5);
        placeorder = (Button) findViewById(R.id.place_order);
        restuarantArrayList = new ArrayList<>();
        myref = FirebaseDatabase.getInstance().getReference("FoodRequest");
        resref = FirebaseDatabase.getInstance().getReference("Restaurant");

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
            }
        });
    }



    private void getdata() {
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        remail = sharedPreferences.getString("Remail", "");
        resref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Restuarant restuarant = snapshot.getValue(Restuarant.class);
                    if (remail.matches(restuarant.getMail())){
                        rphone = restuarant.getPhone();
                        rname = restuarant.getResname();
                        radd = restuarant.getAdd();
                        item1 = it1.getText().toString();
                        if (item1.length() != 0){
                            qnty1 = qty1.getText().toString();
                            if (qnty1.length() == 0){
                                qty1.setError("Please Enter Quantity");
                                Toast.makeText(Res_home.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                item2 = it2.getText().toString();
                                if (item2.length() == 0){
                                    senddata = item1 + " " + qnty1;
                                    key = myref.push().getKey();
                                    FoodRequest foodRequest = new FoodRequest(key,remail,rname,rphone,radd,senddata);
                                    myref.child(key).setValue(foodRequest);
                                    Toast.makeText(Res_home.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
                                    it1.setText("");
                                    qty1.setText("");
                                }
                                else{
                                    qnty2 = qty2.getText().toString();
                                    if (qnty2.length() == 0){
                                        qty2.setError("Please Enter Quantity");
                                        Toast.makeText(Res_home.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        item3 = it3.getText().toString();
                                        if (item3.length() == 0){
                                            senddata = item1 + " " + qnty1 + " " + item2 + " " + qnty2;
                                            key = myref.push().getKey();
                                            FoodRequest foodRequest = new FoodRequest(key,remail,rname,rphone,radd,senddata);
                                            myref.child(key).setValue(foodRequest);
                                            Toast.makeText(Res_home.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
                                            it1.setText("");
                                            qty1.setText("");
                                            it2.setText("");
                                            qty2.setText("");
                                        }
                                        else{
                                            qnty3 = qty3.getText().toString();
                                            if (qnty3.length() == 0){
                                                qty3.setError("Please Enter Quantity");
                                                Toast.makeText(Res_home.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                item4 = it4.getText().toString();
                                                if (item4.length() == 0){
                                                    senddata = item1 + " " + qnty1 +  " " + item2 + " " + qnty2 +  " " +item3 + " " + qnty3;
                                                    key = myref.push().getKey();
                                                    FoodRequest foodRequest = new FoodRequest(key,remail,rname,rphone,radd,senddata);
                                                    myref.child(key).setValue(foodRequest);
                                                    Toast.makeText(Res_home.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
                                                    it1.setText("");
                                                    qty1.setText("");
                                                    it2.setText("");
                                                    qty2.setText("");
                                                    it3.setText("");
                                                    qty3.setText("");
                                                }
                                                else{
                                                    qnty4 = qty4.getText().toString();
                                                    if (qnty4.length() == 0){
                                                        qty4.setError("Please Enter Quantity");
                                                        Toast.makeText(Res_home.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        item5 = it5.getText().toString();
                                                        if (item5.length() == 0){
                                                            senddata = item1 + " " + qnty1 +  " " + item2 + " " + qnty2 +  " " + item3 + " " + qnty3 +  " " + item4 + " " + qnty4;
                                                            key = myref.push().getKey();
                                                            FoodRequest foodRequest = new FoodRequest(key,remail,rname,rphone,radd,senddata);
                                                            myref.child(key).setValue(foodRequest);
                                                            Toast.makeText(Res_home.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
                                                            it1.setText("");
                                                            qty1.setText("");
                                                            it2.setText("");
                                                            qty2.setText("");
                                                            it3.setText("");
                                                            qty3.setText("");
                                                            it4.setText("");
                                                            qty4.setText("");
                                                        }
                                                        else{
                                                            qnty5 = qty5.getText().toString();
                                                            if (qnty5.length() == 0){
                                                                qty5.setError("Please Enter Quantity");
                                                                Toast.makeText(Res_home.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                                                            }
                                                            else{
                                                                senddata = item1 + " " + qnty1 +  " " + item2 + " " + qnty2 +  " " + item3 + " " + qnty3 +  " " + item4 + " " + qnty4 +  " " + item5 + " " + qnty5;
                                                                key = myref.push().getKey();
                                                                FoodRequest foodRequest = new FoodRequest(key,remail,rname,rphone,radd,senddata);
                                                                myref.child(key).setValue(foodRequest);
                                                                Toast.makeText(Res_home.this,"Order Sent Successfully",Toast.LENGTH_SHORT).show();
                                                                it1.setText("");
                                                                qty1.setText("");
                                                                it2.setText("");
                                                                qty2.setText("");
                                                                it3.setText("");
                                                                qty3.setText("");
                                                                it4.setText("");
                                                                qty4.setText("");
                                                                it5.setText("");
                                                                qty5.setText("");
                                                            }

                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        else{
                            Toast.makeText(Res_home.this,"Please enter the item!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}

