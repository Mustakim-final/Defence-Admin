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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_with_patient_main);

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
    }
}