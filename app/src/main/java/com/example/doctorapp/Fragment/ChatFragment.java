package com.example.doctorapp.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Adapter.Question.S_D_QuestionAdapter;
import com.example.doctorapp.BlogActivity;
import com.example.doctorapp.DoctorBlogActivity;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.Model.Question;
import com.example.doctorapp.R;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    CircleImageView profileImage;
    TextView myname;

    private Uri imageUri = null;
    private static final int IMAGE_REQUEST = 1;
    private static final int CAMERA_CODE = 200;
    private static final int GALLERY_CODE = 200;
    private static final int GALLERY_CODE1 = 300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;

    RecyclerView recyclerView;
    S_D_QuestionAdapter s_d_questionAdapter;
    List<Question> questionList;

    Button blogBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        Permission();

        profileImage = view.findViewById(R.id.doctorProfile_ID);
        myname = view.findViewById(R.id.doctorName_ID);

        recyclerView = view.findViewById(R.id.s_questionRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        questionList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                All_Doctor all_doctor = snapshot.getValue(All_Doctor.class);

                myname.setText(all_doctor.getName());

                if (all_doctor.getImageUrl().equals("imageUrl")) {
                    profileImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);

                } else {
                    Glide.with(getContext()).load(all_doctor.getImageUrl()).into(profileImage);
                }

                String type=all_doctor.getType();

                readData(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        blogBtn=view.findViewById(R.id.blogDoctorBtn);
        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        All_Doctor all_doctor = snapshot.getValue(All_Doctor.class);
                        String name=all_doctor.getName();
                        String imageUrl=all_doctor.getImageUrl();
                        String type=all_doctor.getType();
                        String id=all_doctor.getId();

                        Intent intent=new Intent(getContext(), DoctorBlogActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("imageUrl",imageUrl);
                        intent.putExtra("unique","1");
                        intent.putExtra("id",id);
                        getActivity().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;
    }


    private void openImage() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    openCamera();
                }
                if (i == 1) {
                    openGallery();
                }
            }

        });

        builder.create().show();
    }

    private void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, GALLERY_CODE);
    }

    private void Permission() {
        Dexter.withContext(getContext())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            String filepath = "Images/" + "profile" + firebaseUser.getUid();
            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageUrl", imageUri);
                            reference1.updateChildren(hashMap);

                        }
                    });
                }
            });
        }


        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            String filepath = "Images/" + "profile" + firebaseUser.getUid();
            StorageReference reference = FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri = uri.toString();
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("imageUrl", imageUri);
                            reference1.updateChildren(hashMap);

                        }
                    });
                }
            });
        }


    }

    private void readData(String type) {
        reference= FirebaseDatabase.getInstance().getReference("user_question");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Question question=dataSnapshot.getValue(Question.class);
                    if (question.getQuestionType().equals(type)){
                        questionList.add(question);
                    }

                }

                s_d_questionAdapter=new S_D_QuestionAdapter(getContext(),questionList);
                recyclerView.setAdapter(s_d_questionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}