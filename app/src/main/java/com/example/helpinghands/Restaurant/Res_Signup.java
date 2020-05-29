package com.example.helpinghands.Restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.Login;
import com.example.helpinghands.Main2Activity;
import com.example.helpinghands.R;
import com.example.helpinghands.Signup;
import com.example.helpinghands.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Res_Signup extends AppCompatActivity {

    Uri imageUri;
    ImageView img;
    TextView txt;
    Button sup;
    FirebaseAuth fAuth;
    DatabaseReference myRef;
    StorageReference imguploader;
    String resname, mail, pswd, phone, add, licno,owner;
    StorageTask uploadTask;
    private static final int PICK_IMAGE = 100;
    EditText rname, oname, email, password, mono, address, gender, lic;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode ==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

    private String extension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res__signup);

        sup = (Button) findViewById(R.id.sbtn_res);
        txt = (TextView) findViewById(R.id.txt);
        rname = (EditText) findViewById(R.id.rname);
        oname = (EditText) findViewById(R.id.oname);
        img = (ImageView) findViewById(R.id.profile);
        email = (EditText) findViewById(R.id.res_email);
        password = (EditText) findViewById(R.id.passwrd);
        mono = (EditText) findViewById(R.id.mopho);
        address = (EditText) findViewById(R.id.ad_res);
        lic = (EditText) findViewById(R.id.lic_res);
        fAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Restaurant");
        imguploader = FirebaseStorage.getInstance().getReference("Images");

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Res_Signup.this, Res_login.class);
                startActivity(intent);
            }
        });
/*
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });*/

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });



        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean notnull;

                notnull = isnotnull();

                if (notnull) {
                    /*savedata();*/

                    Log.e("Hello","Outside");


                    fAuth.createUserWithEmailAndPassword(mail,pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast toast = Toast.makeText(Res_Signup.this, "Sign up Successful", Toast.LENGTH_LONG);

                                Log.e("Hello","Inside");
                                String key = myRef.push().getKey();
                                String imageid = System.currentTimeMillis()+ "."+extension(imageUri);
                                Log.e("Hello",key);
                                if (uploadTask!= null && uploadTask.isInProgress()){
                                    Toast.makeText(Res_Signup.this,"Uploading is in progress",Toast.LENGTH_SHORT).show();
                                }else{
                                    Fileupload(imageid);
                                }
                                Restuarant restuarant;
                                restuarant = new Restuarant(key,resname,mail,pswd,phone,add,licno,owner,imageid);
                                myRef.child(key).setValue(restuarant);
                                Intent it = new Intent(Res_Signup.this, Res_login.class);
                                startActivity(it);


                            }
                            else {
                                Toast toast = Toast.makeText(Res_Signup.this, "Sign up Failed" + task.getException().getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(Res_Signup.this, "Something is Wrong", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

    }

    private void Fileupload(String imageid) {

        StorageReference reference = imguploader.child(imageid);
        uploadTask = reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(Res_Signup.this,"Signup Succcessful",Toast.LENGTH_SHORT).show();

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


    private Boolean isnotnull() {
        boolean notnull = false;
        resname = rname.getText().toString();
        mail= email.getText().toString();
        pswd = password.getText().toString();
        phone = mono.getText().toString();
        add = address.getText().toString();
        licno = lic.getText().toString();
        owner = oname.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = mail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        Boolean check = true;
        if (resname.length() == 0) {
            rname.setError("Restaurant name cannot be Empty");
            check = false;
        }
        if (owner.length() == 0) {
            oname.setError("Owner Name cannot be Empty");
            check = false;
        }

        if (!matcher.matches()) {
            email.setError("Email not Valid");
            check = false;
        }
        if (pswd.length() == 0) {
            password.setError("Password cannot be Empty");
            check = false;
        }
        if (phone.length() != 10) {
            mono.setError("Number should be of 10 digits");
            check = false;
        }

        if (pswd.length() < 6){
            password.setError("Password should be atleast 6 characters");
            check = false;
        }

        if (add.length() == 0) {
            address.setError("Address cannot be Empty");
            check = false;
        }
        if (licno.length() == 0) {
            lic.setError("License cannot be Empty");
            check = false;
        }
        if (check == true) {
            return true;
        } else {
            return false;
        }
    }



}
