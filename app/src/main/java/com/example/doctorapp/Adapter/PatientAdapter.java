package com.example.doctorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.AppointmentActivity;
import com.example.doctorapp.MessageWithPatientMainActivity;
import com.example.doctorapp.Model.General;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyAdapter> {

    Context context;
    List<General> generalList;


    public PatientAdapter(Context context, List<General> generalList) {
        this.context = context;
        this.generalList = generalList;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.patient_prescription_user,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        General general=generalList.get(position);

        if (general.getImage().equals("imageUrl")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else{
            Glide.with(context).load(general.getImage()).into(holder.imageView);
        }

        holder.textView.setText(general.getName());
    }

    @Override
    public int getItemCount() {
        return generalList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView imageView;
        TextView textView;
        ImageView messageBtn;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.patientImage);
            textView=itemView.findViewById(R.id.patientName);
            messageBtn=itemView.findViewById(R.id.messageBtn_ID);

            imageView.setOnClickListener(this);
            textView.setOnClickListener(this);
            messageBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            General general=generalList.get(getAdapterPosition());
            String image=general.getImage();
            String name=general.getName();
            String problem=general.getProblem();
            String date=general.getDate();
            String time=general.getTime();
            String fee=general.getFee();
            String id=general.getId();


            if (view.getId()==R.id.messageBtn_ID){
                Intent intent1=new Intent(context, MessageWithPatientMainActivity.class);
                intent1.putExtra("userID",id);
                context.startActivity(intent1);
            }else if (view.getId()==R.id.patientImage ){

                Intent intent=new Intent(context, AppointmentActivity.class);
                intent.putExtra("image",image);
                intent.putExtra("name",name);
                intent.putExtra("problem",problem);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                intent.putExtra("fee",fee);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }else if (view.getId()==R.id.patientName){
                Intent intent=new Intent(context, AppointmentActivity.class);
                intent.putExtra("image",image);
                intent.putExtra("name",name);
                intent.putExtra("problem",problem);
                intent.putExtra("date",date);
                intent.putExtra("time",time);
                intent.putExtra("fee",fee);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        }
    }
}
