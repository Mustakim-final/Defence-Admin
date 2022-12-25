package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doctorapp.Adapter.Diagnostic.DiagnosticAdapter;
import com.example.doctorapp.Model.Diagnostic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiagnosticItemActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DiagnosticAdapter diagnosticAdapter;
    List<Diagnostic> diagnosticList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_item);

        recyclerView=findViewById(R.id.diagnosticRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        diagnosticList=new ArrayList<>();

        readData();
    }

    private void readData() {
        reference= FirebaseDatabase.getInstance().getReference("diagnostic_info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                diagnosticList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Diagnostic diagnostic=dataSnapshot.getValue(Diagnostic.class);
                    diagnosticList.add(diagnostic);
                }

                diagnosticAdapter=new DiagnosticAdapter(DiagnosticItemActivity.this,diagnosticList);
                recyclerView.setAdapter(diagnosticAdapter);

                diagnosticAdapter.setOnClickListener(new DiagnosticAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(DiagnosticItemActivity.this,""+position,Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void setDiagnostic(int position,String userID) {
                        Toast.makeText(DiagnosticItemActivity.this,"position"+userID,Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder builder=new AlertDialog.Builder(DiagnosticItemActivity.this);
                        View view= getLayoutInflater().inflate(R.layout.diagnostic_item1,null);
                        builder.setView(view);


                        AlertDialog dialog=builder.show();


                        TextView textViewName=view.findViewById(R.id.diagnosticName);
                        EditText editTextItem=view.findViewById(R.id.diagnosticItem_ID);
                        EditText editTextPrice=view.findViewById(R.id.diagnosticPrice_ID);

                        Button subBtn=view.findViewById(R.id.diagnosticBtn_ID);

                        Diagnostic diagnostic=diagnosticList.get(position);

                        textViewName.setText(diagnostic.getTitle());

                        subBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String title=textViewName.getText().toString();
                                String item=editTextItem.getText().toString();
                                String price=editTextPrice.getText().toString();
                                String myID=diagnostic.getId();
                                String address=diagnostic.getAddress();
                                String imageUri=diagnostic.getDiagnostic();
                                String phone=diagnostic.getPhone();

                                reference=FirebaseDatabase.getInstance().getReference();
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("id",myID);
                                hashMap.put("title",title);
                                hashMap.put("address",address);
                                hashMap.put("phone",phone);
                                hashMap.put("diagnostic",imageUri);

                                hashMap.put("price",price);
                                hashMap.put("item",item);

                                reference.child("diagnostic_info1").push().setValue(hashMap);

                                Toast.makeText(DiagnosticItemActivity.this,"Submit",Toast.LENGTH_SHORT).show();


                            }
                        });





                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}