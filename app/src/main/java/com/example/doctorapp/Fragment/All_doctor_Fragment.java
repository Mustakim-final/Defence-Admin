package com.example.doctorapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.doctorapp.Adapter.Admin_All_DoctorAdapter;
import com.example.doctorapp.Adapter.UserAdapter;
import com.example.doctorapp.Model.All_Doctor;
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
import java.util.HashMap;
import java.util.List;


public class All_doctor_Fragment extends Fragment {

    RecyclerView recyclerView;
    List<All_Doctor> all_doctorList;
    Admin_All_DoctorAdapter admin_all_doctorAdapter;

    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_doctor_, container, false);

        recyclerView=view.findViewById(R.id.admin_all_doctorRecycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        all_doctorList=new ArrayList<>();
        redAllDoctor();
        return view;

    }

    private void redAllDoctor() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Doctor List");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_doctorList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    All_Doctor all_doctor=dataSnapshot.getValue(All_Doctor.class);
                    all_doctorList.add(all_doctor);
                }
                admin_all_doctorAdapter=new Admin_All_DoctorAdapter(getContext(),all_doctorList);
                recyclerView.setAdapter(admin_all_doctorAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}