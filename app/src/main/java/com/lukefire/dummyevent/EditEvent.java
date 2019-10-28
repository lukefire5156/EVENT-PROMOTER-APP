package com.lukefire.dummyevent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
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

public class EditEvent extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button save;
    private  Button delete;
    private ImageView poster;
    private EditText desc;
    private EditText link;
    private EditText name;
    private Uri mImageUri;
    private StorageTask mUploadTask;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private  FirebaseStorage mstorage;
    public String logou;
    private StorageReference mStorageRef;
    private StorageReference mStorageRe;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Bundle bundle = getIntent().getExtras();
        String UID = bundle.getString("message");

        desc= (EditText) findViewById(R.id.desc);
        name= (EditText) findViewById(R.id.eventName);
        link=(EditText) findViewById(R.id.link);
        save=(Button) findViewById(R.id.create);
        poster=(ImageView) findViewById(R.id.poster);
        mstorage= FirebaseStorage.getInstance();
        delete=(Button) findViewById(R.id.delete);

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        final DatabaseReference emid = mDatabase.child("club").child(uid).child("events").child(UID);
        final DatabaseReference emid2= mDatabase.child("club").child("eventdisplay").child(UID);

        emid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String description = dataSnapshot.child("Description").getValue().toString();
                    String name1 = dataSnapshot.child("name").getValue().toString();
                    String link1 = dataSnapshot.child("Registration_Link").getValue().toString();
                    String imgurl = dataSnapshot.child("event_url").getValue().toString();
                    //mImageUri=Uri.parse(imgurl);
                    desc.setText(description);
                    name.setText(name1);
                    link.setText(link1);
                    Picasso.with(EditEvent.this).load(imgurl).into(poster);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description= desc.getText().toString();
                String googleFormLink= link.getText().toString();
                String eventName= name.getText().toString();
                if (TextUtils.isEmpty(description) || TextUtils.isEmpty(googleFormLink)|| TextUtils.isEmpty(eventName)){
                    Toast.makeText(EditEvent.this, "Fill up the details completely!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!URLUtil.isValidUrl(googleFormLink)) {
                        Toast.makeText(EditEvent.this, "Enter the correct registration link with https or http initials.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                    if (mImageUri != null) {
                        mStorageRef = FirebaseStorage.getInstance().getReference(uid);
                        mStorageRe = mStorageRef.child("Events");
                        String newEventName = String.valueOf(System.currentTimeMillis());
                        final StorageReference fileReference = mStorageRe.child(newEventName + "." + getFileExtension(mImageUri));
                        mUploadTask = fileReference.putFile(mImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //Toast.makeText(clubSignup.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                        ////
                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                emid.child("event_url").setValue(uri.toString());
                                                emid2.child("event_url").setValue(uri.toString());
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditEvent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    emid.child("Description").setValue(description);
                    emid.child("Registration_Link").setValue(googleFormLink);
                    emid.child("name").setValue(eventName);
                    emid2.child("Description").setValue(description);
                    emid2.child("Registration_Link").setValue(googleFormLink);
                    emid2.child("name").setValue(eventName);
                    Toast.makeText(EditEvent.this, "Information saved successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditEvent.this, clubAcc.class));

                }
                }



            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emid.removeValue();
                emid2.removeValue();
                Toast.makeText(EditEvent.this, "Event Deleted Successfully.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditEvent.this,clubAcc.class));
            }
        });

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
