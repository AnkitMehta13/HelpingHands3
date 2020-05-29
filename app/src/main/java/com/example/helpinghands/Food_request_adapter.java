package com.example.helpinghands;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.helpinghands.Restaurant.Restuarant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;

public class Food_request_adapter extends BaseAdapter {
    Context context;
    DatabaseReference myref,foodref;
    StorageReference storageReference;
    ArrayList<FoodRequest> foodRequests;
    String imageid,rname,location,desc,key;
    public Food_request_adapter(MainActivity mainActivity, ArrayList<FoodRequest> foodRequests) {
        context = mainActivity;
        this.foodRequests = foodRequests;
    }

    @Override
    public int getCount() {
        return foodRequests.size();
    }

    @Override
    public Object getItem(int i) {
        return foodRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.food_requests,null);

        final ImageView img;
        final TextView resname;




        img = (ImageView)view.findViewById(R.id.res_image);
        resname = (TextView)view.findViewById(R.id.resname_foodrequest);

        /*rname = foodRequests.get(i).getRname();
        location = foodRequests.get(i).getRadd();
        desc = foodRequests.get(i).getSenddata();
        key = foodRequests.get(i).getKey();*/
        myref = FirebaseDatabase.getInstance().getReference("Restaurant");
        foodref = FirebaseDatabase.getInstance().getReference("FoodRequest");


        foodref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    FoodRequest foodRequest = snapshot.getValue(FoodRequest.class);
                    rname = foodRequest.getRname();
                    Log.e("name",foodRequest.getRname());
                    myref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Restuarant restuarant = snapshot.getValue(Restuarant.class);
                                Log.e("name",rname);
                                if (rname.matches(restuarant.getResname())){

                                    imageid = restuarant.getImageid();
                                    storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(imageid);
                                    final long ONE_MEGABYTE = 1024 * 1024;
                                    storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            /*DisplayMetrics dm = new DisplayMetrics();
                                            getWindowManager().getDefaultDisplay().getMetrics(dm);*/
                                            img.setImageBitmap(bm);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        resname.setText(foodRequests.get(i).getRname());

        final int a = i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Food_request_status.class);
                intent.putExtra("Rname", foodRequests.get(a).getRname());
                intent.putExtra("Location",foodRequests.get(a).getRadd());
                intent.putExtra("Description",foodRequests.get(a).getSenddata());
                intent.putExtra("Key",foodRequests.get(a).getKey());
                intent.putExtra("Imageid",imageid);
                context.startActivity(intent);
            }
        });


        return view;
    }


}
