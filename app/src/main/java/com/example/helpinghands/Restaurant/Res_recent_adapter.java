package com.example.helpinghands.Restaurant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.helpinghands.FoodRequestAcceptance;
import com.example.helpinghands.R;
import com.example.helpinghands.Volunteer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Res_recent_adapter extends BaseAdapter {
    ArrayList<FoodRequestAcceptance> foodRequestAcceptances;
    ArrayList<FoodRequestAcceptance> foodRequestAcceptanceArrayList;
    Context context;
    DatabaseReference myref,fraref;
    StorageReference storageReference;
    String imageid,volname;
    public Res_recent_adapter(History history, ArrayList<FoodRequestAcceptance> foodRequestAcceptances) {
        context = history;
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
            view = layoutInflater.inflate(R.layout.vol_sample_cardview,null);

        final ImageView img;
        final TextView vname,place,status,date;
        img = (ImageView)view.findViewById(R.id.volimg_fra);
        vname =(TextView)view.findViewById(R.id.volname);
        place = (TextView)view.findViewById(R.id.location);
        status = (TextView)view.findViewById(R.id.statusinfo);
        date = (TextView)view.findViewById(R.id.accepteddate_fire);
        myref = FirebaseDatabase.getInstance().getReference("Volunteer");
        fraref = FirebaseDatabase.getInstance().getReference("FoodRequestAcceptance");
        volname = foodRequestAcceptances.get(i).getVname();
        foodRequestAcceptanceArrayList = new ArrayList<>();

        fraref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FoodRequestAcceptance foodRequestAcceptance = snapshot.getValue(FoodRequestAcceptance.class);
                    if (volname.matches(foodRequestAcceptance.getVname())){
                        //foodRequestAcceptanceArrayList.add(foodRequestAcceptance);
                        Log.e("VNAME",foodRequestAcceptance.getVname());
                        vname.setText(foodRequestAcceptance.getVname());
                        place.setText(foodRequestAcceptance.getLocation());
                        status.setText(foodRequestAcceptance.getStatus());
                        date.setText(foodRequestAcceptance.getDate());
                    }
                }
                /*for (int i=0;i<foodRequestAcceptanceArrayList.size();i++){
                    vname.setText(foodRequestAcceptanceArrayList.get(i).getVname());
                    place.setText(foodRequestAcceptanceArrayList.get(i).getLocation());
                    status.setText(foodRequestAcceptanceArrayList.get(i).getStatus());
                    date.setText(foodRequestAcceptanceArrayList.get(i).getDate());

                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Volunteer volunteer = snapshot.getValue(Volunteer.class);
                    if (volname.matches(volunteer.getName())){

                        imageid = volunteer.getImageid();
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



        return view;
    }
}
