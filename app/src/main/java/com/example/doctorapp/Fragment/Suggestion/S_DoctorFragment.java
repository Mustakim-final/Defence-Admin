package com.example.doctorapp.Fragment.Suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.Adapter.Question.S_questionAdapter;
import com.example.doctorapp.Model.Question;
import com.example.doctorapp.R;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class S_DoctorFragment extends Fragment {

    private FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    DatabaseReference reference;
    S_questionAdapter s_questionAdapter;
    List<Question>questionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_s__doctor, container, false);




        recyclerView=view.findViewById(R.id.s_questionRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        questionList=new ArrayList<>();
        readData();
        return view;
    }

    private void readData() {
        reference= FirebaseDatabase.getInstance().getReference("user_question");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Question question=dataSnapshot.getValue(Question.class);

                    questionList.add(question);
                }

                s_questionAdapter=new S_questionAdapter(getContext(),questionList);
                recyclerView.setAdapter(s_questionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}