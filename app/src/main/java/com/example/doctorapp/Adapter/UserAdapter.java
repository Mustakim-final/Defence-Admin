package com.example.doctorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.Em_Presctiption_detailsActivity;
import com.example.doctorapp.MessageWithPatientMainActivity;
import com.example.doctorapp.Model.Emergency_prescription;
import com.example.doctorapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewAdapter> {

    Context context;
    List<Emergency_prescription> emergency_prescriptionList;
    String userID;

    public UserAdapter(Context context, List<Emergency_prescription> emergency_prescriptionList) {
        this.context = context;
        this.emergency_prescriptionList = emergency_prescriptionList;
    }

    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item_design,parent,false);
        return new ViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter holder, int position) {
        Emergency_prescription emergency_prescription=emergency_prescriptionList.get(position);

        userID=emergency_prescription.getMyID();
        holder.textView.setText(emergency_prescription.getName());


    }

    @Override
    public int getItemCount() {
        return emergency_prescriptionList.size();
    }

    public class ViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        public ViewAdapter(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.userName_ID);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Emergency_prescription emergency_prescription=emergency_prescriptionList.get(getAdapterPosition());

            userID=emergency_prescription.getMyID();

            Intent intent=new Intent(context, Em_Presctiption_detailsActivity.class);
            intent.putExtra("userID",userID);
            context.startActivity(intent);
        }
    }
}
