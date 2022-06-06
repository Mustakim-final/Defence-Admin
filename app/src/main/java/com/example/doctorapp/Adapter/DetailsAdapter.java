package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.R;

import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyHolder> {
    Context context;
    List<All_Doctor> all_doctorList;
    String userID;

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

        holder.informationText.setText(all_doctor.getInformation());

    }

    @Override
    public int getItemCount() {
        return all_doctorList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView informationText;
        Button conBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            informationText=itemView.findViewById(R.id.doctorInformation_ID);
            conBtn=itemView.findViewById(R.id.confirm_button_ID);
        }
    }
}
