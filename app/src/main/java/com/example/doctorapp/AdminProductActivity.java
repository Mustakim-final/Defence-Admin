package com.example.doctorapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AdminProductActivity extends AppCompatActivity {
    EditText productNameLeft,productPrizeLeft;
    ImageView imageViewProductLeft;
    Button productBtnLeft;

    private static final int IMAGE_REQUEST=1;
    private static final int CAMERA_CODE=200;
    private static final int GALLERY_CODE=200;
    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;

    private Uri imageUri=null;

    private Uri imageUri1=null;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    String[] category;
    Spinner categorySpinner;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);
        progressDialog=new ProgressDialog(AdminProductActivity.this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");

        category=getResources().getStringArray(R.array.category);


        imageViewProductLeft=findViewById(R.id.productImage_ID);


        productNameLeft=findViewById(R.id.productName_ID);

        productPrizeLeft=findViewById(R.id.productPrize_ID);
        categorySpinner=findViewById(R.id.spinnerID);


        productBtnLeft=findViewById(R.id.fastSubmitBtn);


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();


        imageViewProductLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallary();
            }
        });

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AdminProductActivity.this,R.layout.reg_spinner_item,R.id.spinner_text,category);
        categorySpinner.setAdapter(arrayAdapter);

        productBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameLeft=productNameLeft.getText().toString().trim();
                String prizeLeft=productPrizeLeft.getText().toString().trim();
                String category=categorySpinner.getSelectedItem().toString();

                if (nameLeft.isEmpty()){
                    productNameLeft.setError("Enter product name !!!");
                    productNameLeft.requestFocus();
                    return;
                }else if (prizeLeft.isEmpty()){
                    productPrizeLeft.setError("Enter prize !!!");
                    productPrizeLeft.requestFocus();
                    return;
                }else {
                    leftData(nameLeft,prizeLeft,myID,category);
                }
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
            Glide.with(this).load(imageUri).into(imageViewProductLeft);
        }
    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void leftData(String nameLeft, String prizeLeft, String myID,String category) {
        if (imageUri==null){
            progressDialog.show();
            reference=FirebaseDatabase.getInstance().getReference("product image");
            String new_id=reference.push().getKey();
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("id",myID);
            hashMap.put("left_name",nameLeft);
            hashMap.put("left_prize",prizeLeft);
            hashMap.put("left_image","left_image");
            hashMap.put("category",category);
            hashMap.put("new_id",new_id);
            reference.child(new_id).setValue(hashMap);
            progressDialog.dismiss();
            Toast.makeText(AdminProductActivity.this,"Submit",Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.show();
            storageReference = FirebaseStorage.getInstance().getReference("product image");
            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();

                            reference=FirebaseDatabase.getInstance().getReference("product image");
                            String new_id=reference.push().getKey();
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",myID);
                            hashMap.put("left_name",nameLeft);
                            hashMap.put("left_prize",prizeLeft);
                            hashMap.put("left_image",imageUri);
                            hashMap.put("category",category);
                            hashMap.put("new_id",new_id);
                            reference.child(new_id).setValue(hashMap);
                            progressDialog.dismiss();
                            Toast.makeText(AdminProductActivity.this,"Submit",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

}