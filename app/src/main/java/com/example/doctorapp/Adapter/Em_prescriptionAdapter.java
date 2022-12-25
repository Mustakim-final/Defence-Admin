package com.example.doctorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.Em_Presctiption_detailsActivity;
import com.example.doctorapp.Model.Emergency_prescription;
import com.example.doctorapp.R;
import com.example.doctorapp.SentPrescriotionForemActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URISyntaxException;
import java.util.List;

public class Em_prescriptionAdapter extends RecyclerView.Adapter<Em_prescriptionAdapter.MyHolder>{
    Context context;
    List<Emergency_prescription> emergency_prescriptionList;
    String userID;
    DatabaseReference reference;
    Intent intent;

    public Em_prescriptionAdapter(Context context, List<Emergency_prescription> emergency_prescriptionList) {
        this.context = context;
        this.emergency_prescriptionList = emergency_prescriptionList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.em_pres_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Emergency_prescription emergency_prescription=emergency_prescriptionList.get(position);

        holder.nameText.setText(emergency_prescription.getName());
        holder.districtText.setText(emergency_prescription.getDistrict());
        holder.subdistrictText.setText(emergency_prescription.getSubDistrict());
        holder.localAddressText.setText(emergency_prescription.getLocalAddress());
        holder.illnessText.setText(emergency_prescription.getIllness());
        holder.phoneText.setText(emergency_prescription.getPhone());

    }

    @Override
    public int getItemCount() {
        return emergency_prescriptionList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView nameText,districtText,subdistrictText,localAddressText,illnessText,phoneText;
        Button sentPresBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameText=itemView.findViewById(R.id.em_nameSerial_ID);
            districtText=itemView.findViewById(R.id.em_districtSerial_ID);
            subdistrictText=itemView.findViewById(R.id.em_subdistrictSerial_ID);
            localAddressText=itemView.findViewById(R.id.em_localSerial_ID);
            illnessText=itemView.findViewById(R.id.em_illnessSerial_ID);
            phoneText=itemView.findViewById(R.id.em_phoneSerial_ID);

        }
    }
}
