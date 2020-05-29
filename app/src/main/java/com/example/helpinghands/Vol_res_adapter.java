package com.example.helpinghands;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;

class Vol_res_adapter extends BaseAdapter {
    Context context;
    ArrayList<FoodRequestAcceptance> foodRequestAcceptances;
    DatabaseReference myref;
    StorageReference storageReference;
    String imageid,rname;

    public Vol_res_adapter(RecentVol recentVol, ArrayList<FoodRequestAcceptance> foodRequestAcceptances) {
        context = recentVol;
        this.foodRequestAcceptances = foodRequestAcceptances;
    }

    @Override
    public int getCount() {
        return foodRequestAcceptances.size();
    }

    @Override
    public Object getItem(int i) {
        return foodRequestAcceptances.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.vol_recent_sample,null);

        TextView resname,status,date;
        final ImageView img;

        img = (ImageView)view.findViewById(R.id.resimg_fra);
        resname = (TextView)view.findViewById(R.id.res_name_fra);
        status = (TextView)view.findViewById(R.id.statusinfo_fra);
        date = (TextView)view.findViewById(R.id.accepteddate_fra);
        myref = FirebaseDatabase.getInstance().getReference("Restaurant");
        rname = foodRequestAcceptances.get(i).getRname();

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Restuarant restuarant = snapshot.getValue(Restuarant.class);
                    if (rname.matches(restuarant.getResname())){

                        imageid = restuarant.getImageid();
                        storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(imageid);
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                /*DisplayMetrics dm = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(dm);
*/
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


        resname.setText(foodRequestAcceptances.get(i).getRname());
        status.setText(foodRequestAcceptances.get(i).getStatus());
        date.setText(foodRequestAcceptances.get(i).getDate());



        return view;
    }
}
