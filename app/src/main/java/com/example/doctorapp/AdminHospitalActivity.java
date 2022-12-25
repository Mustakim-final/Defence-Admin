package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Adapter.Hospital.HospitalTitleAdapter;
import com.example.doctorapp.Model.Hospital;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminHospitalActivity extends AppCompatActivity {
    ImageView imageViewHospital;
    EditText editTextTitle,editTextAddress,editTextPhone,editTextHospitalInfo;

    Button submitBtn;
    ProgressDialog progressDialog;

    private static final int IMAGE_REQUEST=1;
    private static final int CAMERA_CODE=200;
    private static final int GALLERY_CODE=200;
    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;

    private Uri imageUri=null;
    FirebaseAuth firebaseAuth;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    RecyclerView recyclerView;
    HospitalTitleAdapter hospitalTitleAdapter;
    List<Hospital> hospitalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hospital);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Upload");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);


        imageViewHospital=findViewById(R.id.hospitalImage);
        editTextTitle=findViewById(R.id.hospitalTitle);
        editTextAddress=findViewById(R.id.hospitalAddress);
        editTextPhone=findViewById(R.id.hospitalPhone);
        editTextHospitalInfo=findViewById(R.id.hospitalInfo);
        submitBtn=findViewById(R.id.hospitalsubmitBtn_ID);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        imageViewHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=editTextTitle.getText().toString().trim();
                String address=editTextAddress.getText().toString().trim();
                String phone=editTextPhone.getText().toString();
                String hospitalInfo=editTextHospitalInfo.getText().toString();

                submitData(title,address,phone,myID,hospitalInfo);
            }
        });


        recyclerView=findViewById(R.id.recyclerView_ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        hospitalList=new ArrayList<>();
        readData();


    }

    private void readData() {
        reference=FirebaseDatabase.getInstance().getReference("hospital_info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitalList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Hospital hospital=dataSnapshot.getValue(Hospital.class);
                    hospital.setKey(dataSnapshot.getKey());
                    hospitalList.add(hospital);
                }

                hospitalTitleAdapter=new HospitalTitleAdapter(AdminHospitalActivity.this,hospitalList);
                recyclerView.setAdapter(hospitalTitleAdapter);

                hospitalTitleAdapter.setOnClickListener(new HospitalTitleAdapter.onCLickListener() {
                    @Override
                    public void delete(int position) {

                        Toast.makeText(AdminHospitalActivity.this,""+position,Toast.LENGTH_SHORT).show();
                        Hospital selected=hospitalList.get(position);
                        String key=selected.getKey();
                        reference.child(key).removeValue();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void openGallary() {
        Intent intent=new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK &&data!=null && data.getData()!=null ) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageViewHospital);
        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void submitData(String title, String address, String phone, String myID,String hospitalInfo) {
        if (imageUri==null){
            progressDialog.show();
            reference= FirebaseDatabase.getInstance().getReference("hospital_info");
            String new_key=reference.push().getKey();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("id",myID);
            hashMap.put("title",title);
            hashMap.put("address",address);
            hashMap.put("phone",phone);
            hashMap.put("info",hospitalInfo);
            hashMap.put("new_key",new_key);
            hashMap.put("hospital","hospital");

            reference.child(new_key).setValue(hashMap);
            progressDialog.dismiss();

            Toast.makeText(AdminHospitalActivity.this,"Submit",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("hospital_info");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            reference=FirebaseDatabase.getInstance().getReference("hospital_info");
                            String new_key=reference.push().getKey();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",myID);
                            hashMap.put("title",title);
                            hashMap.put("address",address);
                            hashMap.put("phone",phone);
                            hashMap.put("info",hospitalInfo);
                            hashMap.put("hospital",imageUri);
                            hashMap.put("new_key",new_key);
                            reference.child(new_key).setValue(hashMap);
                            progressDialog.dismiss();

                            Toast.makeText(AdminHospitalActivity.this,"Submit",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}