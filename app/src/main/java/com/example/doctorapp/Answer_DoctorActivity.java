package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doctorapp.Model.All_Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Answer_DoctorActivity extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;
    EditText editTextAnswer;
    Button buttonSubmit;
    DatabaseReference reference;
    String imageUrl,profileName,u_question,questionImage,questionType,id,myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_doctor);

        toolbar=findViewById(R.id.tool_bar_ID);
        toolbar.setTitle("প্রশ্ন ও উত্তর");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        imageUrl=intent.getStringExtra("imageUrl");
        profileName=intent.getStringExtra("profileName");
        u_question=intent.getStringExtra("u_question");
        questionImage=intent.getStringExtra("questionImage");
        questionType=intent.getStringExtra("questionType");
        id=intent.getStringExtra("id");
        myId=intent.getStringExtra("myID");

        editTextAnswer=findViewById(R.id.anserEdit_ID);
        buttonSubmit=findViewById(R.id.answerBtn_ID);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer=editTextAnswer.getText().toString();
                if (answer.isEmpty()){
                    editTextAnswer.setError("Enter answer!");
                    editTextAnswer.requestFocus();
                    return;
                }else {
                    sentData(imageUrl,profileName,u_question,questionImage,questionType,id,answer,myId);
                }


            }
        });
    }


    private void sentData(String imageUrl, String profileName, String u_question, String questionImage, String questionType, String id, String answer,String myId) {


        reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(myId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);

                String doctorImage=all_doctor.getImageUrl();
                String doctorName=all_doctor.getName();
                String doctorType=all_doctor.getType();

                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("doctorImage",doctorImage);
                hashMap.put("doctorName",doctorName);
                hashMap.put("doctorType",doctorType);
                hashMap.put("imageUrl",imageUrl);
                hashMap.put("username",profileName);
                hashMap.put("question",u_question);
                hashMap.put("questionImage",questionImage);
                hashMap.put("questionType",questionType);
                hashMap.put("id",id);
                hashMap.put("answer",answer);

                reference1.child("user_answer").push().setValue(hashMap);

                Toast.makeText(Answer_DoctorActivity.this, "submit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}