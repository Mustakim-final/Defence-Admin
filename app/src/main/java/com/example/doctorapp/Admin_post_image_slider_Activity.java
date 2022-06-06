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

public class Admin_post_image_slider_Activity extends AppCompatActivity implements View.OnClickListener {
    Button choseImage,saveImage;
    ImageView imageView;

    private Uri imageUri=null;
    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;


    FirebaseUser firebaseUser;
    DatabaseReference reference;


    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post_image_slider);

        choseImage=findViewById(R.id.admin_choseImage_ID);
        choseImage.setOnClickListener(this);
        imageView=findViewById(R.id.admin_imageView_ID);
        saveImage=findViewById(R.id.admin_image_saveBtn_ID);
        saveImage.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.admin_choseImage_ID){
            openGallary();
        }

        if (view.getId()==R.id.admin_image_saveBtn_ID){

            if (storageTask!=null && storageTask.isInProgress()){
                Toast.makeText(this, "ছবি আপলোড হচ্ছে", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Admin_post_image_slider_Activity.this,"click",Toast.LENGTH_SHORT).show();
                savePost();
            }
        }
    }

    private void openGallary() {
        Intent intent=new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_CODE1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE1 && resultCode == RESULT_OK &&data!=null && data.getData()!=null ) {
            imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).into(imageView);

        }

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void savePost() {
        storageReference = FirebaseStorage.getInstance().getReference("advertise");

        StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

        sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();


                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = firebaseUser.getUid();

                        String imageUri = uri.toString();
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();


                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userID);
                        hashMap.put("imageUrl", imageUri);

                        reference1.child("advertise").push().setValue(hashMap);

                        imageView.setVisibility(View.INVISIBLE);

                        Toast.makeText(Admin_post_image_slider_Activity.this, "Post done", Toast.LENGTH_SHORT).show();





                    }
                });

            }
        });
    }
}