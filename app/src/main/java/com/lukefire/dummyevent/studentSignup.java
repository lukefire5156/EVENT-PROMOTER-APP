package com.lukefire.dummyevent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class studentSignup extends AppCompatActivity {

    private EditText studname;
    private EditText studreg;
    private EditText studmail;
    private EditText studpwd;
    private EditText confirmpwd;
    private EditText studroom;
    private EditText studphno;
    private FirebaseAuth sAuth;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private Button studsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup);

        studname=(EditText) findViewById(R.id.studname);
        studreg=(EditText) findViewById(R.id.studreg);
        studmail=(EditText) findViewById(R.id.studemail);
        studpwd=(EditText) findViewById(R.id.studPwd);
        confirmpwd=(EditText) findViewById(R.id.confirmPwd);
        studroom=(EditText) findViewById(R.id.studroom);
        studphno=(EditText) findViewById(R.id.studPhone);
        studsignup=(Button) findViewById(R.id.studSignup);
        sAuth = FirebaseAuth.getInstance();

        studsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerstud();
            }
        });

    }

    private void registerstud(){
        final String mail= studmail.getText().toString();
        final String username= studname.getText().toString();
        final String userreg= studreg.getText().toString();
        final String pwd= studpwd.getText().toString();
        final String cfmpwd= confirmpwd.getText().toString();
        final String room= studroom.getText().toString();
        final String phno= studphno.getText().toString();
        if(TextUtils.isEmpty(mail)||TextUtils.isEmpty(username)||TextUtils.isEmpty(userreg)||TextUtils.isEmpty(pwd)||TextUtils.isEmpty(cfmpwd)||TextUtils.isEmpty(room)||TextUtils.isEmpty(phno)){
            Toast.makeText(studentSignup.this,"fill up the details completely",Toast.LENGTH_SHORT).show();
        }
        else{
            if (pwd.length() < 6) {
                Toast.makeText(studentSignup.this, "password must contain atleast 6 character", Toast.LENGTH_SHORT).show();
            }
            else{
                if (pwd.equals(cfmpwd)) {
                    sAuth.createUserWithEmailAndPassword(mail, pwd)
                            .addOnCompleteListener(studentSignup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = user.getUid();
                                        database = FirebaseDatabase.getInstance();
                                        mDatabase = database.getReference();
                                        //mDatabase.child(uid);
                                        DatabaseReference emid = mDatabase.child("user").child(uid);
                                        DatabaseReference flag= mDatabase.child("deptCheck").child(uid);
                                        flag.setValue("0");
                                        emid.child("email").setValue(mail);
                                        emid.child("name").setValue(username);
                                        emid.child("reg").setValue(userreg);
                                        emid.child("room no").setValue(mail);
                                        emid.child("phno").setValue(phno);
                                        Toast.makeText(studentSignup.this, "SUCCESSFULLY REGISTERED!!", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(studentSignup.this,MainActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(studentSignup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(studentSignup.this, "password confirmation failed", Toast.LENGTH_SHORT).show();
                }
            }





        }
    }

}
