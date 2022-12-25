package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doctorapp.Model.Seba;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddminHealthActivity extends AppCompatActivity {
    EditText healthValidation,healthAppointmentDays,healthFess;
    EditText appointments,videoChat,prescription,service,getAppointment,anyTime;
    Spinner titleSpinner;
    String[] title;
    Button submitBtn,submitBtn1;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmin_health);



        title=getResources().getStringArray(R.array.health_type);


        healthValidation=findViewById(R.id.ad_healthValidation_ID);
        healthAppointmentDays=findViewById(R.id.ad_healthAppointmentDays_ID);
        healthFess=findViewById(R.id.ad_healthFess_ID);

        appointments=findViewById(R.id.ad_appointments_ID);
        videoChat=findViewById(R.id.ad_videoChat_ID);
        prescription=findViewById(R.id.ad_prescription_ID);
        service=findViewById(R.id.ad_service_ID);
        getAppointment=findViewById(R.id.ad_getAppointment_ID);
        anyTime=findViewById(R.id.ad_anyTime_ID);
        titleSpinner=findViewById(R.id.health_titleSpinner_ID);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AddminHealthActivity.this,R.layout.reg_spinner_item,R.id.spinner_text,title);
        titleSpinner.setAdapter(arrayAdapter);


        submitBtn=findViewById(R.id.ad_submitBtn_ID);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        myID=firebaseUser.getUid();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1=titleSpinner.getSelectedItem().toString();
                String value2=healthAppointmentDays.getText().toString().trim();
                String value3=healthValidation.getText().toString().trim();
                String value4=healthFess.getText().toString().trim();



                sentHealth(value1,value2,value3,value4,myID);

            }
        });

        submitBtn1=findViewById(R.id.ad_submit1Btn_ID);

        submitBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value1=titleSpinner.getSelectedItem().toString();

                String value5=appointments.getText().toString().trim();
                String value6=videoChat.getText().toString().trim();
                String value7=prescription.getText().toString().trim();
                String value8=service.getText().toString().trim();
                String value9=getAppointment.getText().toString().trim();
                String value10=anyTime.getText().toString().trim();

                sentPekage(value1,value5,value6,value7,value8,value9,value10,myID);


            }

        });
    }



    private void sentHealth(String value1, String value2, String value3, String value4,String myID) {

        reference= FirebaseDatabase.getInstance().getReference("health package");

        String new_key=reference.push().getKey();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("value1",value1);
        hashMap.put("value2",value2);
        hashMap.put("value3",value3);
        hashMap.put("value4",value4);
        hashMap.put("myID",myID);
        hashMap.put("new_key",new_key);
        reference.child(new_key).setValue(hashMap);


        Toast.makeText(AddminHealthActivity.this,"Submit",Toast.LENGTH_SHORT).show();
    }


    private void sentPekage(String value1,String value5, String value6, String value7, String value8, String value9, String value10, String myID) {

        reference= FirebaseDatabase.getInstance().getReference("health package1");
        String new_key=reference.push().getKey();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("value1",value1);
        hashMap.put("value5",value5);
        hashMap.put("value6",value6);
        hashMap.put("value7",value7);
        hashMap.put("value8",value8);
        hashMap.put("value9",value9);
        hashMap.put("value10",value10);
        hashMap.put("myID",myID);
        hashMap.put("new_key",new_key);
        reference.child(new_key).setValue(hashMap);



        Toast.makeText(AddminHealthActivity.this,"Submit",Toast.LENGTH_SHORT).show();
    }
}