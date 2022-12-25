package com.example.doctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AnswerActivity extends AppCompatActivity {
    Toolbar toolbar;
    Intent intent;
    EditText editTextAnswer;
    Button buttonSubmit;
    DatabaseReference reference;
    String imageUrl,profileName,u_question,questionImage,questionType,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

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
                    sentData(imageUrl,profileName,u_question,questionImage,questionType,id,answer);
                }


            }
        });
    }

    private void sentData(String imageUrl, String profileName, String u_question, String questionImage, String questionType, String id, String answer) {



        reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("doctorImage","imageUrl");
        hashMap.put("doctorName","");
        hashMap.put("doctorType","");
        hashMap.put("imageUrl",imageUrl);
        hashMap.put("username",profileName);
        hashMap.put("question",u_question);
        hashMap.put("questionImage",questionImage);
        hashMap.put("questionType",questionType);
        hashMap.put("id",id);
        hashMap.put("answer",answer);

        reference.child("user_answer").push().setValue(hashMap);

        Toast.makeText(this, "submit", Toast.LENGTH_SHORT).show();
    }
}