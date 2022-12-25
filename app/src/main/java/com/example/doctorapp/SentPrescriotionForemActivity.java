package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.HashMap;

public class SentPrescriotionForemActivity extends AppCompatActivity {

    Intent intent;

    Switch presType;
    RelativeLayout textLayout,imageLayout;



    EditText presEdit,meetLinkEdit;
    ImageButton imageButton;
    Button presBtn1,presBtn2;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;
    FirebaseUser firebaseUser;

    private Uri imageUri=null;

    private static final int IMAGE_REQUEST=1;
    private static final int CAMERA_CODE=200;
    private static final int GALLERY_CODE=200;
    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_prescriotion_forem);


        intent=getIntent();
        String userID=intent.getStringExtra("userId");


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        presType=findViewById(R.id.presType_ID);
        textLayout=findViewById(R.id.em_textLayout_ID);
        imageLayout=findViewById(R.id.em_imageLayout);
        presEdit=findViewById(R.id.em_prescriptionEdit_ID);
        meetLinkEdit=findViewById(R.id.em_meetLinkEdit_ID);
        imageButton=findViewById(R.id.em_image_ID);
        presBtn1=findViewById(R.id.em_presSent1_ID);
        presBtn2=findViewById(R.id.em_presSent_ID);


       presType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if (b){
                   textLayout.setVisibility(View.GONE);
                   imageLayout.setVisibility(View.VISIBLE);
               }else {
                   textLayout.setVisibility(View.VISIBLE);
                   imageLayout.setVisibility(View.GONE);
               }
           }
       });


       imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               openGallary();
           }
       });


       presBtn1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String prescription=presEdit.getText().toString().trim();
               String meet=meetLinkEdit.getText().toString().trim();
               sentPres(prescription,meet,myID,userID);
           }
       });

       presBtn2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               senPresImage(myID,userID);
           }
       });


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

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.image_view_item,null,false);
        builder.setView(view);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imageUri=null;
            }
        });

        AlertDialog alertDialog=builder.show();

        ImageView imageView=view.findViewById(R.id.viewImage_ID);

        if (requestCode == GALLERY_CODE1 && resultCode == RESULT_OK &&data!=null && data.getData()!=null ) {
            imageUri = data.getData();
            Glide.with(view).load(imageUri).into(imageView);
        }

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void senPresImage(String myID, String userID) {
        if (imageUri==null){
            Toast.makeText(this,"please select a image!!!",Toast.LENGTH_SHORT).show();
        }else {
            storageReference = FirebaseStorage.getInstance().getReference("Prescription");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            reference=FirebaseDatabase.getInstance().getReference("Doctor List").child(myID);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);

                                    String name=all_doctor.getName();
                                    String imageUrl=all_doctor.getImageUrl();
                                    String info=all_doctor.getInformation();
                                    String day=all_doctor.getDay();

                                    String imageUri=uri.toString();
                                    reference=FirebaseDatabase.getInstance().getReference();
                                    HashMap<String,Object> hashMap=new HashMap<>();

                                    hashMap.put("name",name);
                                    hashMap.put("imageUrl",imageUrl);
                                    hashMap.put("img_pres",imageUri);
                                    hashMap.put("information",info);
                                    hashMap.put("day",day);
                                    hashMap.put("myId",myID);
                                    hashMap.put("userId",userID);

                                    reference.child("em_d_prescription").push().setValue(hashMap);

                                    Toast.makeText(SentPrescriotionForemActivity.this,"Sent",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            });
        }
    }

    private void sentPres(String prescription, String meet, String myID, String userID) {

        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String myId = firebaseUser.getUid();

         */

        reference=FirebaseDatabase.getInstance().getReference("Doctor List").child(myID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);

                String name=all_doctor.getName();
                String imageUrl=all_doctor.getImageUrl();
                String info=all_doctor.getInformation();
                String day=all_doctor.getDay();

                reference=FirebaseDatabase.getInstance().getReference();
                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("prescription",prescription);
                hashMap.put("name",name);
                hashMap.put("imageUrl",imageUrl);
                hashMap.put("information",info);
                hashMap.put("day",day);
                hashMap.put("meet",meet);
                hashMap.put("myId",myID);
                hashMap.put("userId",userID);

                reference.child("em_d_prescription").push().setValue(hashMap);

                Toast.makeText(SentPrescriotionForemActivity.this,"Sent",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





}