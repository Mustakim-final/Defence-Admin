package com.example.doctorapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doctorapp.Adapter.UserAdapter;
import com.example.doctorapp.Model.Emergency_prescription;
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

public class emergency_prescriptionFragment extends Fragment {


    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<Emergency_prescription> emergency_prescriptionList;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_emergency_prescription, container, false);

        recyclerView=view.findViewById(R.id.recyclerView_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        emergency_prescriptionList=new ArrayList<>();
        readUser();

        return view;
    }

    private void readUser() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("emergency prescription");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emergency_prescriptionList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Emergency_prescription emergency_prescription=dataSnapshot.getValue(Emergency_prescription.class);

                    emergency_prescriptionList.add(emergency_prescription);

                }
                userAdapter=new UserAdapter(getContext(),emergency_prescriptionList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}