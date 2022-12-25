package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.doctorapp.Adapter.Em_prescriptionAdapter;
import com.example.doctorapp.Model.Emergency_prescription;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Em_Presctiption_detailsActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    String userID;

    Button sentPrescription;

    RecyclerView recyclerView;
    List<Emergency_prescription> emergency_prescriptionList;
    Em_prescriptionAdapter em_prescriptionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_em_presctiption_details);

        intent=getIntent();
        userID=intent.getStringExtra("userID");
        sentPrescription=findViewById(R.id.em_submitSerial_ID);
        sentPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Em_Presctiption_detailsActivity.this,SentPrescriotionForemActivity.class);
                intent.putExtra("userId",userID);
                startActivity(intent);
            }
        });




        recyclerView=findViewById(R.id.em_prescriptionRecycler_ID);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        emergency_prescriptionList=new ArrayList<>();

        redData();
    }

    private void redData() {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("emergency prescription");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emergency_prescriptionList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Emergency_prescription emergency_prescription=dataSnapshot.getValue(Emergency_prescription.class);
                    if (emergency_prescription.getMyID().equals(userID)){
                        emergency_prescriptionList.add(emergency_prescription);
                    }

                }

                em_prescriptionAdapter=new Em_prescriptionAdapter(Em_Presctiption_detailsActivity.this,emergency_prescriptionList);
                recyclerView.setAdapter(em_prescriptionAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}