package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorapp.Adapter.MessageAdapter;
import com.example.doctorapp.Model.Chats;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageWithPatientMainActivity extends AppCompatActivity {

    private EditText smsEditText;
    private ImageButton sendBtn;
    private TextView userName;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    List<Chats> chatsList;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;

    ApiService apiService;
    boolean notify=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_with_patient_main);

        apiService= Client.getCLient("https://fcm.googleapis.com/").create(ApiService.class);

        userName=findViewById(R.id.usermegNameBar_ID);

        recyclerView=findViewById(R.id.recyclermegView_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        smsEditText=findViewById(R.id.messageEdit_ID);
        sendBtn=findViewById(R.id.mesSend_ID);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        intent=getIntent();
        String userID=intent.getStringExtra("userID");

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                userName.setText(users.getUsername());

                readMessage(myID,userID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String message=smsEditText.getText().toString().trim();
                if (!message.equals("")){
                    sendMessage(myID,userID,message);
                }else {
                    Toast.makeText(MessageWithPatientMainActivity.this,"Type message!!!",Toast.LENGTH_SHORT).show();
                }

                smsEditText.setText("");
            }
        });
    }

    private void readMessage(String myID, String userID) {
        chatsList=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Chats chats=dataSnapshot.getValue(Chats.class);
                    if (chats.getSender().equals(myID) && chats.getReciver().equals(userID) || chats.getSender().equals(userID) && chats.getReciver().equals(myID)){
                        chatsList.add(chats);
                    }
                    messageAdapter=new MessageAdapter(MessageWithPatientMainActivity.this,chatsList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String myID, String userID, String message) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myID);
        hashMap.put("reciver",userID);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);

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

        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);

                if (notify){
                    sendNotitfication(userID,users.getUsername(), msg);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("token");
        Token token=new Token(refreshToken);
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

    private void sendNotitfication(String userID, String username, String msg) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("token");

        Query query=tokens.orderByKey().equalTo(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.main_icon,username+":"+msg,"New Message",userID);
                    Notification notification=new Notification(username,msg,R.mipmap.main_icon);

                    Sender sender=new Sender(token.getToken(),data,notification);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if (response.body().success!=1){
                                            Toast.makeText(MessageWithPatientMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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