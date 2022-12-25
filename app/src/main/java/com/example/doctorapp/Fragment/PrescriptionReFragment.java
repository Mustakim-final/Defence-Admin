package com.example.doctorapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorapp.Adapter.PatientAdapter;
import com.example.doctorapp.Model.General;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PrescriptionReFragment extends Fragment {


    RecyclerView recyclerView;
    List<General> generalList;
    PatientAdapter patientAdapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_prescription_re, container, false);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myId=firebaseUser.getUid();

        recyclerView=view.findViewById(R.id.prescriptionRecycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        generalList=new ArrayList<>();

        readPatitent(myId);
        return view;
    }

    private void readPatitent(String myId) {
        reference= FirebaseDatabase.getInstance().getReference("general prescription");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                generalList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    General general=dataSnapshot.getValue(General.class);

                    if (general.getD_id().equals(myId)){
                        generalList.add(general);
                    }


                }

                patientAdapter=new PatientAdapter(getContext(),generalList);
                recyclerView.setAdapter(patientAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}