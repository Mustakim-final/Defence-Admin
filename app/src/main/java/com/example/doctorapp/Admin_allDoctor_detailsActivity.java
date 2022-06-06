package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.doctorapp.Adapter.DetailsAdapter;
import com.example.doctorapp.Model.All_Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_allDoctor_detailsActivity extends AppCompatActivity {

    Intent intent;
    RecyclerView recyclerView;

    DetailsAdapter detailsAdapter;
    List<All_Doctor> all_doctorList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_doctor_details);

        recyclerView=findViewById(R.id.doctorInformation_Recycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        intent=getIntent();
        String userID=intent.getStringExtra("userID");

        all_doctorList=new ArrayList<>();

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Doctor List");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_doctorList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    All_Doctor all_doctor=dataSnapshot.getValue(All_Doctor.class);

                    if (all_doctor.getId().equals(userID)){
                        all_doctorList.add(all_doctor);
                    }
                }

                detailsAdapter=new DetailsAdapter(Admin_allDoctor_detailsActivity.this,all_doctorList);
                recyclerView.setAdapter(detailsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}