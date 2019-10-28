package com.lukefire.dummyevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class clubSignup extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button chooseImage;
    private ImageView uploadImageView;


    private EditText clubname;
    private EditText clubemail;
    private EditText clubpwd;
    private EditText clubcfmpwd;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private Button clubsignup;
    private StorageTask mUploadTask;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageReference mStorageRe;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_signup);

        clubname=(EditText) findViewById(R.id.clubname);
        clubemail=(EditText) findViewById(R.id.clubemail);
        clubpwd= (EditText) findViewById(R.id.clubpwd);
        clubcfmpwd=(EditText) findViewById(R.id.clubcfmpwd);
        clubsignup=(Button) findViewById(R.id.clubsignup);
        chooseImage=(Button) findViewById(R.id.chooseImage);
        uploadImageView=(ImageView) findViewById(R.id.uploadimg);
        sAuth = FirebaseAuth.getInstance();

        mStorageRef= FirebaseStorage.getInstance().getReference("clubs");


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        clubsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageUri != null){
                    registerclub();
                }
                else{
                    Toast.makeText(clubSignup.this, "Please select image file for your club's logo", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void registerclub(){
        final String mail= clubemail.getText().toString();
        final String cluname= clubname.getText().toString();
        final String pwd= clubpwd.getText().toString();
        final String cfmpwd= clubcfmpwd.getText().toString();

        if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(cluname)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(cfmpwd)){
            Toast.makeText(clubSignup.this,"fill up the club details completely",Toast.LENGTH_SHORT).show();
        }

        else{
            if (pwd.length() < 6) {
                Toast.makeText(clubSignup.this, "password must contain atleast 6 character", Toast.LENGTH_SHORT).show();
            }
            else {
                if (pwd.equals(cfmpwd)) {
                    sAuth.createUserWithEmailAndPassword(mail, pwd)
                            .addOnCompleteListener(clubSignup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = user.getUid();

                                        database = FirebaseDatabase.getInstance();
                                        mDatabase = database.getReference();
                                        //mDatabase.child(uid);
                                        final DatabaseReference emid = mDatabase.child("club").child(uid);
                                        final DatabaseReference flag = mDatabase.child("deptCheck").child(uid);
                                        final DatabaseReference logo = emid.child("logo_url");
                                        flag.setValue("1");
                                        emid.child("Club Email").setValue(mail);
                                        emid.child("Club Name").setValue(cluname);

                                        mStorageRef = FirebaseStorage.getInstance().getReference(uid);
                                        mStorageRe=mStorageRef.child("ClubLogo");
                                        final StorageReference fileReference = mStorageRe.child("logo" + "." + getFileExtension(mImageUri));

                                        /*mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Got the download URL for 'users/me/profile.png'
                                                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
                                                generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle any errors
                                            }
                                        });*/



                                        mUploadTask = fileReference.putFile(mImageUri)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                                        /////////////
                                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                logo.setValue(uri.toString());
                                                            }
                                                        });

                                                        ////////////////


                                                        //logo.setValue(taskSnapshot.getStorage().getDownloadUrl().toString());
                                                        Toast.makeText(clubSignup.this, "CLUB SUCCESSFULLY REGISTERED!!", Toast.LENGTH_LONG).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        startActivity(new Intent(clubSignup.this, MainActivity.class));
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(clubSignup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(clubSignup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(clubSignup.this, "password confirmation failed", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data !=null && data.getData()!=null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(uploadImageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }



}
