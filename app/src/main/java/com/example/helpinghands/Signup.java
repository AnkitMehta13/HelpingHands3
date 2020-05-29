package com.example.helpinghands;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpinghands.Restaurant.Res_Signup;
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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    Uri imageUri;
    ImageView img;
    Spinner gender;
    Button sup;
    TextView lin;
    FirebaseAuth fAuth;
    DatabaseReference myRef;
    StorageReference imguploader;
    StorageTask uploadTask;
    String fname, mail, password, mobile, add, licno, gen, bday;


    EditText vname, email, phone, pswd, address, lic, dob;

    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";



    private String extension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sup = (Button) findViewById(R.id.sbtn);
        vname = (EditText) findViewById(R.id.vol_name);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.mono);
        pswd = (EditText) findViewById(R.id.pass);
        address = (EditText) findViewById(R.id.add);
        lic = (EditText) findViewById(R.id.lic);
        img = (ImageView) findViewById(R.id.profile_image);
        dob = (EditText) findViewById(R.id.dob);
        gender = (Spinner) findViewById(R.id.gen);
        lin = (TextView) findViewById(R.id.login);
        fAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Volunteer");
        imguploader = FirebaseStorage.getInstance().getReference("Images");
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar crdate = Calendar.getInstance();
                    int yr = crdate.get(Calendar.YEAR);
                    int mn = crdate.get(Calendar.MONTH);
                    int dd = crdate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog;
                    datePickerDialog = new DatePickerDialog(Signup.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            dob.setText(year + "-" + month + "-" + dayOfMonth);
                        }
                    }, yr, mn, dd);

                    datePickerDialog.setTitle("Select_Date");
                    datePickerDialog.show();
                }
            }
        });

        if (fAuth.getCurrentUser() != null){
            Intent it = new Intent(Signup.this, Main2Activity.class);
            startActivity(it);
            finish();
        }

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
        //Image Selector
        /*img.setOnClickListener(new View.OnClickListener() {
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

        //Signup Button Listener
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean notnull;

                notnull = isnotnull();

                if (notnull) {
                    
                    Log.e("Hello","Outside");


                    fAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Toast toast = Toast.makeText(Signup.this, "Sign up Successful", Toast.LENGTH_LONG);

                                Log.e("Hello","Inside");
                                String key = myRef.push().getKey();
                                String imageid = System.currentTimeMillis()+ "."+extension(imageUri);
                                Log.e("Hello",key);
                                if (uploadTask!= null && uploadTask.isInProgress()){
                                    Toast.makeText(Signup.this,"Uploading is in progress",Toast.LENGTH_SHORT).show();
                                }else{
                                    Fileupload(imageid);
                                }

                                Log.e("Hello",key);
                                Volunteer vol = new Volunteer(key,fname,mail,password,mobile,add,licno,gen,bday,imageid);
                                myRef.child(key).setValue(vol);
                                Intent it = new Intent(Signup.this, Login.class);
                                startActivity(it);
                            }
                            else {
                                Toast toast = Toast.makeText(Signup.this, "Sign up Failed" + task.getException().getMessage(), Toast.LENGTH_LONG);
                            }
                        }
                    });

                } else {
                    Toast toast = Toast.makeText(Signup.this, "Something is Wrong", Toast.LENGTH_LONG);
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
                        Toast.makeText(Signup.this,"Signup Succcessful",Toast.LENGTH_SHORT).show();

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

    /*private void savedata() {
        SharedPreferences sharedPreferences = getSharedPreferences("Volunteer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", vname.getText().toString());
        editor.putString("Email", email.getText().toString());
        editor.putString("Phone", phone.getText().toString());
        editor.putString("Address", address.getText().toString());
        editor.putString("lic", lic.getText().toString());
        editor.putString("Password", pswd.getText().toString());
        editor.putString("Gender", gender.getSelectedItem().toString());
        editor.putString("Profile", path);
        editor.commit();

    }*/

    private Boolean isnotnull() {
        boolean notnull = false;

        fname = vname.getText().toString();
        mail = email.getText().toString();
        password = pswd.getText().toString();
        mobile = phone.getText().toString();
        add = address.getText().toString();
        licno = lic.getText().toString();
        gen = gender.getSelectedItem().toString();
        bday = dob.getText().toString();
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = mail;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        Boolean check = true;
        if (fname.length() == 0) {
            vname.setError("Name cannot be Empty");
            check = false;
        }
        if (gen.length() == 0) {
            gender.setPrompt("Gender cannot be Empty");
            check = false;
        }
        if (bday.length() == 0) {
            dob.setError("Date of birth cannot be Empty");
            check = false;
        }


        if (!matcher.matches()) {
            email.setError("Email not Valid");
            check = false;
        }
        if (password.length() == 0) {
            pswd.setError("Password cannot be Empty");
            check = false;
        }
        if (mobile.length() != 10) {
            phone.setError("Phone cannot be Empty");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode ==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
        }
    }

}

