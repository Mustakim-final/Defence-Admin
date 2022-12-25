package com.example.doctorapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class DiagnosticActivity extends AppCompatActivity {

    ImageView imageViewHospital;
    EditText editTextTitle,editTextAddress,editTextPhone;

    Button submitBtn,nextBtn;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic);

        imageViewHospital=findViewById(R.id.diagnosticImage);
        editTextTitle=findViewById(R.id.diagnosticTitle);
        editTextAddress=findViewById(R.id.diagnosticAddress);
        editTextPhone=findViewById(R.id.diagnosticPhone);
        submitBtn=findViewById(R.id.diagnosticsubmitBtn_ID);
        nextBtn=findViewById(R.id.diagnosticNextBtn_ID);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DiagnosticActivity.this,DiagnosticItemActivity.class);
                startActivity(intent);
            }
        });

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

                submitData(title,address,phone,myID);
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


    private void submitData(String title, String address, String phone, String myID) {
        if (imageUri==null){
            reference= FirebaseDatabase.getInstance().getReference();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("id",myID);
            hashMap.put("title",title);
            hashMap.put("address",address);
            hashMap.put("phone",phone);
            hashMap.put("diagnostic","diagnostic");

            reference.child("diagnostic_info").push().setValue(hashMap);

            Toast.makeText(DiagnosticActivity.this,"Submit",Toast.LENGTH_SHORT).show();
        }else {
            storageReference = FirebaseStorage.getInstance().getReference("diagnostic_info");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();

                            reference=FirebaseDatabase.getInstance().getReference();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",myID);
                            hashMap.put("title",title);
                            hashMap.put("address",address);
                            hashMap.put("phone",phone);
                            hashMap.put("diagnostic",imageUri);
                            reference.child("diagnostic_info").push().setValue(hashMap);

                            Toast.makeText(DiagnosticActivity.this,"Submit",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}