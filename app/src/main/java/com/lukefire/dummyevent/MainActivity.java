package com.lukefire.dummyevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button signup;
    private Button login;
    private EditText email;
    private EditText pwd;
    private Switch toggle;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup= (Button) findViewById(R.id.signup);
        login=(Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        pwd= (EditText)findViewById(R.id.pwd);
        //toggle = (Switch)findViewById(R.id.toggle);
        mAuth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_login();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));
            }
        });

    }

    public void start_login(){
        String emailid=email.getText().toString();
        String pswd = pwd.getText().toString();
        if (TextUtils.isEmpty(emailid)|| TextUtils.isEmpty(pswd)){
            Toast.makeText(MainActivity.this, "incomplete details", Toast.LENGTH_LONG).show();
        }
        else{

            mAuth.signInWithEmailAndPassword(emailid, pswd)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                final DatabaseReference emid = mDatabase.child("deptCheck").child(uid);
                                emid.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String check = dataSnapshot.getValue().toString();
                                        if (check.equals("0")){
                                            startActivity(new Intent(MainActivity.this,StudAccount.class));
                                        }
                                        else{
                                            startActivity(new Intent(MainActivity.this,clubAcc.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });


                            } else {
                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

   @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){

            String uid = currentUser.getUid();
            final DatabaseReference emid = mDatabase.child("deptCheck").child(uid);
            emid.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String check = dataSnapshot.getValue().toString();
                    if (check.equals("0")){
                        startActivity(new Intent(MainActivity.this,StudAccount.class));
                    }
                    else{
                        startActivity(new Intent(MainActivity.this,clubAcc.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });

        }
    }

    @Override
    public void onBackPressed(){
        finishAffinity();

    }
}
