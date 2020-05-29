package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helpinghands.Restaurant.Main3Activity;
import com.example.helpinghands.Restaurant.ResEditProfile;
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

public class EditProfile extends AppCompatActivity {

    Uri imageUri;
    EditText name,email,phone,address,pswd;
    String vname,vemail,vphone,vadd,key,imageid;
    boolean clicked;
    ImageView img;
    ImageButton imageButton;
    DatabaseReference myref;
    StorageTask uploadTask;
    ArrayList<Volunteer> volunteers;
    StorageReference storageReference,uploadref;

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
        setContentView(R.layout.activity_edit_profile);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.prof_email);
        phone = (EditText)findViewById(R.id.prof_phone);
        address = (EditText)findViewById(R.id.prof_address);
        pswd = (EditText)findViewById(R.id.prof_pswd);
        img = (ImageView) findViewById(R.id.prof_image);
        imageButton = (ImageButton)findViewById(R.id.save);
        volunteers = new ArrayList<>();
        uploadref = FirebaseStorage.getInstance().getReference().child("Images");
        myref = FirebaseDatabase.getInstance().getReference("Volunteer");
        clicked = false;
        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        vemail = sharedPreferences.getString("Vemail", "");
        Log.e("Hello",vemail);

        email.setEnabled(false);



        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Volunteer volunteer = snapshot.getValue(Volunteer.class);
                    Log.e("Hello", volunteer.getEmail());
                    volunteers.add(volunteer);
                }
                for(int i=0; i<volunteers.size(); i++){
                    if(vemail.equals(volunteers.get(i).getEmail())){
                        vname = volunteers.get(i).getName();
                        vphone = volunteers.get(i).getPhone();
                        vadd = volunteers.get(i).getAddress();
                        key = volunteers.get(i).getKey();
                        email.setText(vemail);
                        name.setText(vname);
                        phone.setText(vphone);
                        address.setText(vadd);
                        imageid = volunteers.get(i).getImageid();
                        storageReference = FirebaseStorage.getInstance().getReference().child("Images").child(imageid);
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) { Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
                        vname = name.getText().toString().trim();
                        vphone = phone.getText().toString().trim();
                        vadd = address.getText().toString();
                        myref.child(key).child("name").setValue(vname);
                        myref.child(key).child("phone").setValue(vphone);
                        myref.child(key).child("address").setValue(vadd);
                        if (clicked == true){
                            String imageid = System.currentTimeMillis()+ "."+extension(imageUri);
                            if (uploadTask!= null && uploadTask.isInProgress()){
                                Toast.makeText(EditProfile.this,"Uploading is in progress",Toast.LENGTH_SHORT).show();
                            }else{
                                myref.child(key).child("imageid").setValue(imageid);
                                Fileupload(imageid);
                            }
                        }
                        else{
                            Toast.makeText(EditProfile.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
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



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        pswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this,ChangePassword.class);
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
                        Toast.makeText(EditProfile.this,"Profile Updated Succcessfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfile.this,MainActivity.class);
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