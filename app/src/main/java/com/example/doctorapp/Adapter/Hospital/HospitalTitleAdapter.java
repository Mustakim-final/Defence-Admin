package com.example.doctorapp.Adapter.Hospital;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.Hospital;
import com.example.doctorapp.R;


import java.util.List;

public class HospitalTitleAdapter extends RecyclerView.Adapter<HospitalTitleAdapter.Myholder>{

    Context context;
    List<Hospital> hospitalList;
    private onCLickListener listener;

    public HospitalTitleAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.hospital_itme,parent,false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        Hospital hospital=hospitalList.get(position);

        Glide.with(context).load(hospital.getHospital()).into(holder.imageViewHospital);
        holder.textViewTitle.setText(hospital.getTitle());
        holder.textViewAddress.setText(hospital.getAddress());
        holder.textViewPhone.setText(hospital.getPhone());
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView imageViewHospital,imageViewCall;
        TextView textViewTitle,textViewAddress,textViewPhone;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageViewHospital=itemView.findViewById(R.id.hospitalImage);
            textViewTitle=itemView.findViewById(R.id.hospitalTitle);
            textViewAddress=itemView.findViewById(R.id.hospitalAddress);
            textViewPhone=itemView.findViewById(R.id.hospitalPhone);

            imageViewCall=itemView.findViewById(R.id.hospitalCall);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            Hospital hospital= hospitalList.get(getAdapterPosition());

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if (listener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            listener.delete(position);
                            return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Options");
            MenuItem delete=contextMenu.add(Menu.NONE,1,1,"delete");
            delete.setOnMenuItemClickListener(this);
        }
    }

    public interface onCLickListener{
        void delete(int position);
    }

    public void setOnClickListener(onCLickListener listener){
        this.listener=listener;
    }
}
