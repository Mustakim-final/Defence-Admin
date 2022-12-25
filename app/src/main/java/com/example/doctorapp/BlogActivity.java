package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BlogActivity extends AppCompatActivity {
    EditText editTextTitle,editTextDescription;
    Button buttonSubmit;


    DatabaseReference reference;
    String[] blogType;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);




        spinner=findViewById(R.id.blogSpinner);

        blogType=getResources().getStringArray(R.array.blog_type);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.reg_spinner_item,R.id.spinner_text,blogType);
        spinner.setAdapter(arrayAdapter);


        editTextTitle=findViewById(R.id.blogTitle);
        editTextDescription=findViewById(R.id.blogDescription);

        buttonSubmit=findViewById(R.id.blogSubmitBtn);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker=new DatePicker(getApplicationContext());

                int currentDay=datePicker.getDayOfMonth();
                int currentMonth=(datePicker.getMonth())+1;
                int currentYear=datePicker.getYear();


                String day,month,year;
                day=String.valueOf(currentDay);
                month=String.valueOf(currentMonth);
                year=String.valueOf(currentYear);


                String title=editTextTitle.getText().toString();
                String description=editTextDescription.getText().toString();
                String type=spinner.getSelectedItem().toString();




                Blog(day,month,year,title,description,type);


            }
        });
    }



    private void Blog(String day, String month, String year, String title, String description,String type) {
        reference= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("day",day);
        hashMap.put("month",month);
        hashMap.put("year",year);
        hashMap.put("title",title);
        hashMap.put("description",description);
        hashMap.put("name","Owner");
        hashMap.put("imageUrl","imageUrl");
        hashMap.put("type",type);
        reference.child("Blog").push().setValue(hashMap);
        Toast.makeText(BlogActivity.this,"Submit",Toast.LENGTH_SHORT).show();
    }
}