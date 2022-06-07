package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyHolder> {
    Context context;
    List<All_Doctor> all_doctorList;
    String userID;
    DatabaseReference reference;

    public DetailsAdapter(Context context, List<All_Doctor> all_doctorList) {
        this.context = context;
        this.all_doctorList = all_doctorList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.confirmation_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {


        All_Doctor all_doctor=all_doctorList.get(position);

        holder.nameText.setText(all_doctor.getName());
        holder.ageText.setText(all_doctor.getAge());
        holder.genderText.setText(all_doctor.getGender());
        holder.phoneText.setText(all_doctor.getPhone());
        holder.addressText.setText(all_doctor.getAddress());

        holder.informationText.setText(all_doctor.getInformation());

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

    public class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView informationText,nameText,ageText,genderText,phoneText,addressText;
        Button conBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.doctorProfile_ID);
            nameText=itemView.findViewById(R.id.doctorName);
            ageText=itemView.findViewById(R.id.doctorAge);
            genderText=itemView.findViewById(R.id.doctorGender);
            phoneText=itemView.findViewById(R.id.doctorPhone);
            addressText=itemView.findViewById(R.id.doctorAddress);

            informationText=itemView.findViewById(R.id.doctorInformation_ID);
            conBtn=itemView.findViewById(R.id.confirm_button_ID);

            conBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    All_Doctor users=all_doctorList.get(getAdapterPosition());
                    userID=users.getId();

                    reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","yes");
                    reference.updateChildren(hashMap);



                    Toast.makeText(context, ""+userID, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
