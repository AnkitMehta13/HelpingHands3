package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.Volunteer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ResEditProfile extends AppCompatActivity {

    Uri imageUri;
    EditText name,email,phone,address,pswd;
    String rname,remail,rphone,radd,key,imageid;
    boolean clicked;
    ImageView img;
    StorageTask uploadTask;
    ImageButton imageButton;
    DatabaseReference myref;
    StorageReference storageReference,uploadref;
    ArrayList<Restuarant> restuarantArrayList;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode ==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_edit_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("Restaurant", MODE_PRIVATE);
        remail = sharedPreferences.getString("Remail", "");
        Log.e("Hello",remail);
        myref = FirebaseDatabase.getInstance().getReference("Restaurant");
        name = (EditText)findViewById(R.id.name_res);
        email = (EditText)findViewById(R.id.prof_email_res);
        phone = (EditText)findViewById(R.id.prof_phone_res);
        address = (EditText)findViewById(R.id.prof_address_res);
        pswd = (EditText)findViewById(R.id.prof_pswd_res);
        img = (ImageView) findViewById(R.id.prof_image_res);
        imageButton = (ImageButton)findViewById(R.id.save_res);
        uploadref = FirebaseStorage.getInstance().getReference().child("Images");
        restuarantArrayList = new ArrayList<>();
        clicked = false;
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Restuarant restuarant = snapshot.getValue(Restuarant.class);
                    restuarantArrayList.add(restuarant);

                }

                for(int i=0; i<restuarantArrayList.size(); i++){
                    int num = restuarantArrayList.size();
                    String st = String.valueOf(num);
                    Log.e("Size",st);
                    if(remail.matches(restuarantArrayList.get(i).getMail())){
                        email.setText(restuarantArrayList.get(i).getMail());
                        name.setText(restuarantArrayList.get(i).getResname());
                        address.setText(restuarantArrayList.get(i).getAdd());
                        phone.setText(restuarantArrayList.get(i).getPhone());
                        key = restuarantArrayList.get(i).getKey();
                        imageid = restuarantArrayList.get(i).getImageid();
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
                    }


                }
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rname = name.getText().toString();
                        rphone = phone.getText().toString().trim();
                        radd = address.getText().toString();
                        myref.child(key).child("resname").setValue(rname);
                        myref.child(key).child("phone").setValue(rphone);
                        myref.child(key).child("add").setValue(radd);
                        if (clicked == true){
                            String imageid = System.currentTimeMillis()+ "."+extension(imageUri);
                            if (uploadTask!= null && uploadTask.isInProgress()){
                                Toast.makeText(ResEditProfile.this,"Uploading is in progress",Toast.LENGTH_SHORT).show();
                            }else{
                                myref.child(key).child("imageid").setValue(imageid);
                                Fileupload(imageid);
                            }
                        }
                        else{
                            Toast.makeText(ResEditProfile.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResEditProfile.this,Main3Activity.class);
                            startActivity(intent);
                        }

                    }
                });

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,1);
                        clicked = true;
                    }
                });



                Log.e("Name",restuarantArrayList.get(0).getResname());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });









        email.setEnabled(false);



        pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResEditProfile.this,ResChangePassword.class);
                startActivity(intent);
            }
        });


    }

    private void Fileupload(String imageid) {

        StorageReference reference = uploadref.child(imageid);
        uploadTask = reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ResEditProfile.this,"Profile Updated Succcessfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResEditProfile.this,Main3Activity.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private String extension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
