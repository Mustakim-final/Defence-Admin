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
import com.example.doctorapp.Adapter.Pharmacy.PharmacyAdapter;
import com.example.doctorapp.Model.Pharmacy;
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

public class PharmacyActivity extends AppCompatActivity {
    ImageView imageViewPharmacy;
    EditText editTextTitle,editTextAddress,editTextPhone;

    Button submitBtn;

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
    List<Pharmacy> pharmacyList;
    PharmacyAdapter pharmacyAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");

        imageViewPharmacy=findViewById(R.id.pharmacyImage);
        editTextTitle=findViewById(R.id.pharmacyTitle);
        editTextAddress=findViewById(R.id.pharmacyAddress);
        editTextPhone=findViewById(R.id.pharmacyPhone);
        submitBtn=findViewById(R.id.pharmacysubmitBtn_ID);



        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        imageViewPharmacy.setOnClickListener(new View.OnClickListener() {
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

                submitData(title,address,phone,myID);
            }
        });


        recyclerView=findViewById(R.id.recyclerView_ID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        pharmacyList=new ArrayList<>();

        readData();
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
            Glide.with(this).load(imageUri).into(imageViewPharmacy);
        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void submitData(String title, String address, String phone, String myID) {
        progressDialog.show();
        if (imageUri==null){
            reference= FirebaseDatabase.getInstance().getReference();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("id",myID);
            hashMap.put("title",title);
            hashMap.put("address",address);
            hashMap.put("phone",phone);
            hashMap.put("pharmacy","pharmacy");

            reference.child("pharmacy_info").push().setValue(hashMap);

            progressDialog.dismiss();

            //Toast.makeText(PharmacyActivity.this,"Submit",Toast.LENGTH_SHORT).show();
        }else {
            storageReference = FirebaseStorage.getInstance().getReference("pharmacy_info");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.show();
                            String imageUri = uri.toString();

                            reference=FirebaseDatabase.getInstance().getReference();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",myID);
                            hashMap.put("title",title);
                            hashMap.put("address",address);
                            hashMap.put("phone",phone);
                            hashMap.put("pharmacy",imageUri);
                            reference.child("pharmacy_info").push().setValue(hashMap);

                            progressDialog.dismiss();

                            Toast.makeText(PharmacyActivity.this,"Submit",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }


    private void readData() {
        reference=FirebaseDatabase.getInstance().getReference("pharmacy_info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pharmacyList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                    pharmacy.setKey(dataSnapshot.getKey());
                    pharmacyList.add(pharmacy);
                }

                pharmacyAdapter=new PharmacyAdapter(PharmacyActivity.this,pharmacyList);
                recyclerView.setAdapter(pharmacyAdapter);
                pharmacyAdapter.setOnClickListener(new PharmacyAdapter.onItemClickListener() {
                    @Override
                    public void selected(int position) {

                    }

                    @Override
                    public void delete(int position) {
                        Pharmacy selected=pharmacyList.get(position);
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
}