package com.lukefire.dummyevent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class clubAcc extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    private ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_acc);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("YOUR EVENTS LISTS");

        mRecyclerView= findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        logout= (ImageView) findViewById(R.id.logout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase= FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mRef=mFirebaseDatabase.getReference("club").child(uid).child("events");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                startActivity(new Intent(clubAcc.this,Event.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(clubAcc.this,MainActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<clubModel, clubViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<clubModel, clubViewHolder>(
                        clubModel.class,
                        R.layout.clubrow,
                        clubViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(clubViewHolder viewHolder, clubModel model, int i) {
                        viewHolder.setDetails(getApplicationContext(),model.getName(),model.getEvent_url(),model.getEventUID());
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }

}
