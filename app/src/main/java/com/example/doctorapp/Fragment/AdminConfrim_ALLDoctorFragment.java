package com.example.doctorapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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

                    if (all_doctor.getStatus().equals("yes")){
                        all_doctorList.add(all_doctor);
                    }

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

                    @Override
                    public void setDayAndTime(int position, String userId) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        View view= LayoutInflater.from(getContext()).inflate(R.layout.doctor_dialouge_item1,null,false);
                        builder.setView(view);

                        AlertDialog alertDialog=builder.show();

                        CircleImageView profileImage=view.findViewById(R.id.admin_doctor_image_ID);
                        //EditText imageUrlEdit=view.findViewById(R.id.admin_doctor_imageUrl_ID);
                        //EditText nameEdit=view.findViewById(R.id.admin_doctor_Name_ID);

                        TextView date1Text=view.findViewById(R.id.date1_ID);
                        TextView date2Text=view.findViewById(R.id.date2_ID);
                        TextView date3Text=view.findViewById(R.id.date3_ID);
                        TextView date4Text=view.findViewById(R.id.date4_ID);
                        TextView date5Text=view.findViewById(R.id.date5_ID);
                        TextView date6Text=view.findViewById(R.id.date6_ID);
                        TextView date7Text=view.findViewById(R.id.date7_ID);

                        TextView time1Text=view.findViewById(R.id.time1_ID);
                        TextView time2Text=view.findViewById(R.id.time2_ID);
                        TextView time3Text=view.findViewById(R.id.time3_ID);
                        TextView time4Text=view.findViewById(R.id.time4_ID);
                        TextView time5Text=view.findViewById(R.id.time5_ID);
                        TextView time6Text=view.findViewById(R.id.time6_ID);
                        TextView time7Text=view.findViewById(R.id.time7_ID);

                        int t1Hour,t1Minute;

                        DatePicker datePicker=new DatePicker(getContext());

                        int currentDay=datePicker.getDayOfMonth();
                        int currentMonth=(datePicker.getMonth())+1;
                        int currentYear=datePicker.getYear();

                        date1Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                        date1Text.setText(day+"/"+(month+1)+"/"+year);
                                    }
                                },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date2Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date2Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date3Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date3Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date4Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date4Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date5Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date5Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date6Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date6Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });
                        date7Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(),

                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                                date7Text.setText(day+"/"+(month+1)+"/"+year);
                                            }
                                        },currentYear,currentMonth,currentDay);

                                datePickerDialog.show();
                            }
                        });

                        TimePicker timePicker=new TimePicker(getContext());

                        int currentHour=timePicker.getCurrentHour();
                        int currentMinute=timePicker.getCurrentMinute();

                        time1Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time1Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time2Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time2Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time3Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time3Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time4Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time4Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time5Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time5Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time6Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time6Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });
                        time7Text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                                time7Text.setText(hour+" : "+minute);
                                            }
                                        },currentHour,currentMinute,true);

                                timePickerDialog.show();
                            }
                        });

                        Button submit=view.findViewById(R.id.admin_doctor_submit_ID);

                        All_Doctor all_doctor=all_doctorList.get(position);
                        //imageUrlEdit.setText(all_doctor.getImageUrl());
                        //nameEdit.setText(all_doctor.getName());

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String date1=date1Text.getText().toString();
                                String date2=date2Text.getText().toString();
                                String date3=date3Text.getText().toString();
                                String date4=date4Text.getText().toString();
                                String date5=date5Text.getText().toString();
                                String date6=date6Text.getText().toString();
                                String date7=date7Text.getText().toString();

                                String time1=time1Text.getText().toString();
                                String time2=time2Text.getText().toString();
                                String time3=time3Text.getText().toString();
                                String time4=time4Text.getText().toString();
                                String time5=time5Text.getText().toString();
                                String time6=time6Text.getText().toString();
                                String time7=time7Text.getText().toString();

                                reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userId);

                                HashMap<String,Object> hashMap=new HashMap<>();


                                hashMap.put("date1",date1);
                                hashMap.put("date2",date2);
                                hashMap.put("date3",date3);
                                hashMap.put("date4",date4);
                                hashMap.put("date5",date5);
                                hashMap.put("date6",date6);
                                hashMap.put("date7",date7);

                                hashMap.put("time1",time1);
                                hashMap.put("time2",time2);
                                hashMap.put("time3",time3);
                                hashMap.put("time4",time4);
                                hashMap.put("time5",time5);
                                hashMap.put("time6",time6);
                                hashMap.put("time7",time7);

                                reference.updateChildren(hashMap);

                                alertDialog.dismiss();

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