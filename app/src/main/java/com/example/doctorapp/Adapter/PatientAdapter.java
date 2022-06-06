package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;

import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyAdapter> {

    Context context;
    List<Users> patentList;
    String userID;

    public PatientAdapter(Context context, List<Users> patentList) {
        this.context = context;
        this.patentList = patentList;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.patient_prescription_user,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        Users users=patentList.get(position);

        holder.textView.setText(users.getUsername());
    }

    @Override
    public int getItemCount() {
        return patentList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder{
        TextView textView;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.patient_userName_ID);
        }
    }
}
