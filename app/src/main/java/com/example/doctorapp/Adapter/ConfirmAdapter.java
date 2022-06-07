package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmAdapter extends ArrayAdapter<All_Doctor> {

    List<All_Doctor> all_doctorList;
    Context context;

    public ConfirmAdapter(Context context, List<All_Doctor> all_doctorList) {
        super(context, R.layout.admin_all_doctor_item,all_doctorList);
        this.context=context;
        this.all_doctorList=all_doctorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.admin_all_doctor_item,parent,false);
        TextView name,status;
        CircleImageView circleImageView;

        name=convertView.findViewById(R.id.admin_doctor_profile_name_ID);
        circleImageView=convertView.findViewById(R.id.admin_doctor_profile_image_ID);
        status=convertView.findViewById(R.id.admin_doctor_degree_ID);

        All_Doctor all_doctor=all_doctorList.get(position);
        name.setText(all_doctor.getName());
        if (all_doctor.getImageUrl().equals("default")){
            circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(all_doctor.getImageUrl()).into(circleImageView);
        }

        status.setText(all_doctor.getInformation());

        return convertView;
    }
}
