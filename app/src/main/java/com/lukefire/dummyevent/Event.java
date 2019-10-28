package com.lukefire.dummyevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Event extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button create;
    private ImageView poster;
    private EditText desc;
    private EditText link;
    private  EditText name;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    public String logou;
    private StorageReference mStorageRef;
    private StorageReference mStorageRe;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        desc= (EditText) findViewById(R.id.desc);
        name= (EditText) findViewById(R.id.eventName);
        link=(EditText) findViewById(R.id.link);
        create=(Button) findViewById(R.id.create);
        poster=(ImageView) findViewById(R.id.poster);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageUri != null){
                    createevent();
                }
                else{
                    Toast.makeText(Event.this, "Please select image file for your club's poster", Toast.LENGTH_LONG).show();
                }

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    private void createevent() {
        final String description = desc.getText().toString();
        final String googleFormLink = link.getText().toString();
        final String eventName = name.getText().toString();
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(googleFormLink)|| TextUtils.isEmpty(eventName) ) {
            Toast.makeText(Event.this, "fill up the event details completely", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!URLUtil.isValidUrl(googleFormLink)) {
                Toast.makeText(Event.this, "Enter the correct registration link with https or http initials.", Toast.LENGTH_SHORT).show();
            }
            else{
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference();
            final DatabaseReference emid = mDatabase.child("club").child(uid);
            ///
            final DatabaseReference ch1 = mDatabase.child("club").child("eventdisplay");
            final DatabaseReference ch2 = emid.child("logo_url");
            //String logou;


            ///
            final DatabaseReference emid2 = emid.child("events");
            String eventName1 = String.valueOf(System.currentTimeMillis());
            final DatabaseReference events = emid2.child(eventName1);
            //
            final DatabaseReference evedis = ch1.child(eventName1);
            //
            final DatabaseReference logo = events.child("event_url");
            final DatabaseReference logo1 = evedis.child("event_url");
            events.child("Description").setValue(description);
            //
            evedis.child("Description").setValue(description);
            events.child("Registration_Link").setValue(googleFormLink);
            evedis.child("name").setValue(eventName);
            events.child("name").setValue(eventName);
                evedis.child("EventUID").setValue(eventName1);
                events.child("EventUID").setValue(eventName1);

            ch2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    logou = dataSnapshot.getValue().toString();
                    evedis.child("Logo_url").setValue(logou);
                    events.child("Logo_url").setValue(logou);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    logou = "error";
                }
            });


            evedis.child("Registration_Link").setValue(googleFormLink);
            mStorageRef = FirebaseStorage.getInstance().getReference(uid);
            mStorageRe = mStorageRef.child("Events");

            final StorageReference fileReference = mStorageRe.child(eventName + "." + getFileExtension(mImageUri));
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                            ////
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    logo.setValue(uri.toString());
                                    logo1.setValue(uri.toString());
                                }
                            });


                            ////

                            //logo.setValue(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            Toast.makeText(Event.this, "CLUB EVENT SUCCESSFULLY REGISTERED!!", Toast.LENGTH_LONG).show();
                            //FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(Event.this, clubAcc.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Event.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
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

            Picasso.with(this).load(mImageUri).into(poster);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
