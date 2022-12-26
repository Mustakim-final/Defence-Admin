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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.SetNotification.ApiService;
import com.example.doctorapp.SetNotification.Client;
import com.example.doctorapp.SetNotification.Data;
import com.example.doctorapp.SetNotification.MyResponse;
import com.example.doctorapp.SetNotification.Notification;
import com.example.doctorapp.SetNotification.Sender;
import com.example.doctorapp.SetNotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity {

    Intent intent;
    CircleImageView imageView;
    TextView nameText,problemText,dateText,feeText;
    Button button;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ApiService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        apiService= Client.getCLient("https://fcm.googleapis.com/").create(ApiService.class);

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
                notify=true;
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


                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (task.isComplete()){
                                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                    String refreshToken=task.getResult().getToken();
                                    if (firebaseUser!=null){
                                        updateToken(refreshToken);
                                    }


                                }
                            }
                        });

                final String msg="Sent Appointment Time";
                reference=FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users=snapshot.getValue(Users.class);

                        if (notify){
                            sendNotitfication(id,users.getUsername(), msg);
                        }
                        notify=false;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }



    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("token");
        Token token=new Token(refreshToken);
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

    private void sendNotitfication(String id, String username, String msg) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("token");

        Query query=tokens.orderByKey().equalTo(id);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.main_icon,username+":"+msg,"New Message",id);
                    Notification notification=new Notification(username,msg,R.mipmap.main_icon);

                    Sender sender=new Sender(token.getToken(),data,notification);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if (response.body().success!=1){
                                            Toast.makeText(AppointmentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}