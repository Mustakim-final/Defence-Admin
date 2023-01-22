package com.example.doctorapp.Adapter.Pharmacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.Pharmacy;
import com.example.doctorapp.R;

import java.util.List;

public class PharmacyApplyAdapter extends RecyclerView.Adapter<PharmacyApplyAdapter.MyHolder>{
    Context context;
    List<Pharmacy> pharmacyList;

    public PharmacyApplyAdapter(Context context, List<Pharmacy> pharmacyList) {
        this.context = context;
        this.pharmacyList = pharmacyList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.phapplyitem,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Pharmacy pharmacy=pharmacyList.get(position);
        holder.nameText.setText(pharmacy.getName());
        holder.textAddress.setText(pharmacy.getAddress());
        holder.textPhone.setText(pharmacy.getPhone());
        if (pharmacy.getNote().equals("")){
            holder.textNote.setVisibility(View.GONE);
        }else {
            holder.textNote.setText(pharmacy.getNote());
        }

        if (pharmacy.getPrescription().equals("prescription")){
            holder.imageView.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(pharmacy.getPrescription()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView nameText,textAddress,textPhone,textNote;
        ImageView imageView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameText=itemView.findViewById(R.id.name);
            textAddress=itemView.findViewById(R.id.address);
            textPhone=itemView.findViewById(R.id.phone);
            textNote=itemView.findViewById(R.id.note);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
