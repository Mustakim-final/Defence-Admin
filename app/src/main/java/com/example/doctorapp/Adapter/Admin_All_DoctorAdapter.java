package com.example.doctorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Admin_allDoctor_detailsActivity;
import com.example.doctorapp.MessageWithPatientMainActivity;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_All_DoctorAdapter extends RecyclerView.Adapter<Admin_All_DoctorAdapter.MyHolder> {
    Context context;
    List<All_Doctor> all_doctorList;
    String userID;

    public Admin_All_DoctorAdapter(Context context, List<All_Doctor> all_doctorList) {
        this.context = context;
        this.all_doctorList = all_doctorList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.admin_all_doctor_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        All_Doctor all_doctor=all_doctorList.get(position);
        holder.nameText.setText(all_doctor.getName());
        holder.degreeText.setText(all_doctor.getInformation());

        if (all_doctor.getImageUrl().equals("imageUrl")){
            holder.circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(all_doctor.getImageUrl()).into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return all_doctorList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView circleImageView;
        TextView nameText,degreeText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.admin_doctor_profile_image_ID);
            nameText=itemView.findViewById(R.id.admin_doctor_profile_name_ID);
            degreeText=itemView.findViewById(R.id.admin_doctor_degree_ID);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            All_Doctor users=all_doctorList.get(getAdapterPosition());
            userID=users.getId();

            Intent intent=new Intent(context, Admin_allDoctor_detailsActivity.class);
            intent.putExtra("userID",userID);
            context.startActivity(intent);
        }
    }

}
