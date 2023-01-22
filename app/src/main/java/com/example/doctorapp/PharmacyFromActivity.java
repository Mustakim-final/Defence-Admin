package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.doctorapp.Adapter.Pharmacy.PharmacyApplyAdapter;
import com.example.doctorapp.Model.Pharmacy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PharmacyFromActivity extends AppCompatActivity {
    Intent intent;
    DatabaseReference reference;
    RecyclerView recyclerView;
    List<Pharmacy> pharmacyList;
    PharmacyApplyAdapter applyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_from);

        intent=getIntent();
        String phrname=intent.getStringExtra("phrname");

        recyclerView=findViewById(R.id.recyclerView_ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        pharmacyList=new ArrayList<>();

        readData(phrname);
    }

    private void readData(String phrname) {
        reference= FirebaseDatabase.getInstance().getReference("pharmacy_apply");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pharmacyList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                    if (pharmacy.getPharmacy_title().equals(phrname)){
                        pharmacyList.add(pharmacy);
                    }
                }
                applyAdapter=new PharmacyApplyAdapter(PharmacyFromActivity.this,pharmacyList);
                recyclerView.setAdapter(applyAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}