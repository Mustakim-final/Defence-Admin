package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorapp.Model.All_Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Doctor_WatingActivity extends AppCompatActivity {

    TextView textView;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_wating);

        textView=findViewById(R.id.textApply);

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String userID=firebaseUser.getUid();

        reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);
                if (all_doctor.getStatus().equals("yes")){
                    Intent intent=new Intent(Doctor_WatingActivity.this,Home_Doctor_Activity.class);
                    startActivity(intent);
                    finish();
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}