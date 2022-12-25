package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentActivity extends AppCompatActivity {

    Intent intent;
    CircleImageView imageView;
    TextView nameText,problemText,dateText,feeText;
    Button button;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        intent=getIntent();
        String image=intent.getStringExtra("image");
        String name=intent.getStringExtra("name");
        String problem=intent.getStringExtra("problem");
        String date=intent.getStringExtra("date");
        String time=intent.getStringExtra("time");
        String fee=intent.getStringExtra("fee");
        String id=intent.getStringExtra("id");

        imageView=findViewById(R.id.patientImage_ID);
        if (image.equals("imageUrl")){
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }else {
            Glide.with(this).load(image).into(imageView);
        }
        nameText=findViewById(R.id.patientName_ID);
        nameText.setText(name);
        problemText=findViewById(R.id.patientProblem);
        problemText.setText(problem);
        dateText=findViewById(R.id.patientDate);
        dateText.setText("Date: "+date+" Time: "+time);
        feeText=findViewById(R.id.patientFee);
        feeText.setText(fee);

        button=findViewById(R.id.nextBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                String myId=firebaseUser.getUid();
                sentData(id,myId);
            }
        });
    }

    private void sentData(String id, String myId) {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view1= LayoutInflater.from(AppointmentActivity.this).inflate(R.layout.genarel_dialouge_item,null,false);
        builder.setView(view1);

        AlertDialog alertDialog=builder.show();

        EditText editTextMeetLink=view1.findViewById(R.id.gen_meetLink);
        EditText dateText=view1.findViewById(R.id.gen_date_and_time);


        Button submitBtn=view1.findViewById(R.id.gen_submitAppointment);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String meet=editTextMeetLink.getText().toString();
                String date=dateText.getText().toString();

                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                String myId=firebaseUser.getUid();

                reference=FirebaseDatabase.getInstance().getReference("Doctor List").child(myId);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);
                        String name=all_doctor.getName();
                        String imageUrl=all_doctor.getImageUrl();

                        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("name",name);
                        hashMap.put("imageUrl",imageUrl);
                        hashMap.put("meet",meet);
                        hashMap.put("date",date);
                        hashMap.put("id",id);
                        hashMap.put("d_id",myId);
                        reference1.child("appointment").push().setValue(hashMap);

                        alertDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}