package com.example.doctorapp.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.doctorapp.Adapter.Admin_All_DoctorAdapter;
import com.example.doctorapp.Adapter.Admin_confirmAdapter;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.R;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class AdminConfrim_ALLDoctorFragment extends Fragment {

    RecyclerView recyclerView;
    List<All_Doctor> all_doctorList;
    Admin_confirmAdapter admin_all_doctorAdapter;

    DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_confrim__a_l_l_doctor, container, false);

        recyclerView=view.findViewById(R.id.admin_all_doctor_confirmRecycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        all_doctorList=new ArrayList<>();
        redAllDoctor();

        return view;
    }

    private void redAllDoctor() {

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("Doctor List");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                all_doctorList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    All_Doctor all_doctor=dataSnapshot.getValue(All_Doctor.class);
                    all_doctorList.add(all_doctor);
                }
                admin_all_doctorAdapter=new Admin_confirmAdapter(getContext(),all_doctorList);
                recyclerView.setAdapter(admin_all_doctorAdapter);

                admin_all_doctorAdapter.setOnClickListener(new Admin_confirmAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                    }

                    @Override
                    public void setStatus(int position,String userID) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        View view= LayoutInflater.from(getContext()).inflate(R.layout.doctor_dialouge_item,null,false);
                        builder.setView(view);

                        AlertDialog alertDialog=builder.show();

                        CircleImageView profileImage=view.findViewById(R.id.admin_doctor_image_ID);
                        //EditText imageUrlEdit=view.findViewById(R.id.admin_doctor_imageUrl_ID);
                        //EditText nameEdit=view.findViewById(R.id.admin_doctor_Name_ID);
                        EditText informationEdit=view.findViewById(R.id.admin_doctor_information_ID);
                        EditText dayEdit=view.findViewById(R.id.admin_doctor_day_ID);
                        EditText feeEdit=view.findViewById(R.id.admin_doctor_payment_ID);

                        Button submit=view.findViewById(R.id.admin_doctor_submit_ID);

                        All_Doctor all_doctor=all_doctorList.get(position);
                        //imageUrlEdit.setText(all_doctor.getImageUrl());
                        //nameEdit.setText(all_doctor.getName());
                        informationEdit.setText(all_doctor.getInformation());

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //String imageUrl=imageUrlEdit.getText().toString().trim();
                                //String name=nameEdit.getText().toString();
                                String information=informationEdit.getText().toString();
                                String day=dayEdit.getText().toString();
                                String fee=feeEdit.getText().toString();

                                reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);

                                HashMap<String,Object> hashMap=new HashMap<>();

                                //hashMap.put("name",name);
                                //hashMap.put("imageUrl",imageUrl);
                                hashMap.put("information",information);
                                hashMap.put("day",day);
                                hashMap.put("fees",fee);
                                //hashMap.put("id",userID);

                                reference.updateChildren(hashMap);

                                alertDialog.dismiss();


                            }
                        });

                        //FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}