package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;

public class RegActivity extends AppCompatActivity {

    private EditText usernameText,gmailText,passwordText,nameText,ageText,phoneText,infoText;
    private Spinner doctorTypeSp,genderTypeSp;
    private Button subBtn;
    private ProgressBar progressBar;
    private TextView goSignIn;
    private ImageButton cvImageBtn;

    private String[] doctorType,genderType;

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

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Upload");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        doctorType=getResources().getStringArray(R.array.doctor_type);
        genderType=getResources().getStringArray(R.array.gender);

        nameText=findViewById(R.id.name_editText);
        ageText=findViewById(R.id.ageEditText);
        phoneText=findViewById(R.id.phoneEditText);
        doctorTypeSp=findViewById(R.id.doctorType_ID);
        genderTypeSp=findViewById(R.id.genderSpinner_ID);
        infoText=findViewById(R.id.informationEditText);

        usernameText=findViewById(R.id.userName_ID);
        gmailText=findViewById(R.id.user_gmail_ID);
        passwordText=findViewById(R.id.user_password_ID);
        subBtn=findViewById(R.id.regSubBtn_ID);
        progressBar=findViewById(R.id.progressBar_ID);
        goSignIn=findViewById(R.id.goSignIn_ID);
        cvImageBtn=findViewById(R.id.cvBtn_ID);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(RegActivity.this,R.layout.reg_spinner_item,R.id.spinner_text,doctorType);
        doctorTypeSp.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(RegActivity.this,R.layout.reg_spinner_item,R.id.spinner_text,genderType);
        genderTypeSp.setAdapter(arrayAdapter1);



        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameText.getText().toString().trim();
                String age=ageText.getText().toString().trim();
                String gender=genderTypeSp.getSelectedItem().toString();
                String phone=phoneText.getText().toString().trim();
                String type=doctorTypeSp.getSelectedItem().toString();
                String info=infoText.getText().toString().trim();

                String username=usernameText.getText().toString().trim();
                String gmail=gmailText.getText().toString().trim();
                String password=passwordText.getText().toString().trim();

                if (name.isEmpty()){
                    nameText.setError("Enter name!!");
                    nameText.requestFocus();
                }else if (age.isEmpty()){
                    ageText.setError("Enter age!!");
                    ageText.requestFocus();
                }else if (phone.isEmpty()) {
                    phoneText.setError("Enter phone!!");
                    phoneText.requestFocus();
                }else if (info.isEmpty()) {
                    infoText.setError("Enter information!!");
                    infoText.requestFocus();
                }else if (username.isEmpty()){
                    usernameText.setError("Enter User Name!!");
                    usernameText.requestFocus();
                }else if (gmail.isEmpty()){
                    gmailText.setError("Enter Gmail !!");
                    gmailText.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(gmail).matches()){
                    gmailText.setError("Enter Valid gmail !!");
                    gmailText.requestFocus();
                }else if (password.isEmpty()){
                    passwordText.setError("Enter Password !!");
                    passwordText.requestFocus();
                }else if (password.length()<6){
                    passwordText.setError("Enter 6 digit password !!");
                    passwordText.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(name,info,age,gender,phone,type,username,gmail,password);
                }
            }
        });

        cvImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
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

    private void registerUser(String name,String info,String age, String gender, String phone, String type, String username, String gmail, String password) {
        firebaseAuth.createUserWithEmailAndPassword(gmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){

                    if (imageUri==null){

                        progressDialog.show();

                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        String userID=firebaseUser.getUid();
                        reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("name",name);
                        hashMap.put("information",info);
                        hashMap.put("age",age);
                        hashMap.put("gender",gender);
                        hashMap.put("phone",phone);
                        hashMap.put("type",type);
                        hashMap.put("id",userID);
                        hashMap.put("username",username);
                        hashMap.put("gmail",gmail);
                        hashMap.put("status","status");
                        hashMap.put("imageUrl","imageUrl");
                        hashMap.put("imagePost", "imagePost");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Intent intent=new Intent(RegActivity.this, SignInActivity.class);
                                intent.putExtra("userID",userID);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                Toast.makeText(RegActivity.this,"Registration successfully done",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else {

                        storageReference = FirebaseStorage.getInstance().getReference("Doctor");
                        StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                        sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                        String userID=firebaseUser.getUid();
                                        String imageUri = uri.toString();
                                        progressDialog.show();
                                        reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("name",name);
                                        hashMap.put("information",info);
                                        hashMap.put("age",age);
                                        hashMap.put("gender",gender);
                                        hashMap.put("phone",phone);
                                        hashMap.put("type",type);
                                        hashMap.put("id",userID);
                                        hashMap.put("username",username);
                                        hashMap.put("gmail",gmail);
                                        hashMap.put("status","status");
                                        hashMap.put("imageUrl","imageUrl");
                                        hashMap.put("imagePost", imageUri);

                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Intent intent=new Intent(RegActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(RegActivity.this,"Registration successfully done",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        });

                    }

                }
            }
        });
    }


}