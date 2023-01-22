package com.example.doctorapp.Adapter.Pharmacy;

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
import com.example.doctorapp.Model.Pharmacy;
import com.example.doctorapp.PharmacyFromActivity;
import com.example.doctorapp.R;


import java.util.List;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.MyHolder>{
    Context context;
    List<Pharmacy> pharmacyList;
    private onItemClickListener listener;
    String locality,address;

    public PharmacyAdapter(Context context, List<Pharmacy> pharmacyList) {
        this.context = context;
        this.pharmacyList = pharmacyList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.diagnostic_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Pharmacy pharmacy=pharmacyList.get(position);
        if (pharmacy.getPharmacy().equals("pharmacy")){
            holder.pharmacyImage.setImageResource(R.mipmap.main_icon);
        }else {
            Glide.with(context).load(pharmacy.getPharmacy()).into(holder.pharmacyImage);
        }

        holder.textViewTitle.setText(pharmacy.getTitle());
        holder.textViewAddress.setText(pharmacy.getAddress());
        holder.textViewPhone.setText(pharmacy.getPhone());
    }

    @Override
    public int getItemCount() {
        return pharmacyList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView pharmacyImage;
        TextView textViewTitle,textViewAddress,textViewPhone;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            pharmacyImage=itemView.findViewById(R.id.diagnosticImage_ID);
            textViewTitle=itemView.findViewById(R.id.diagnosticTitle_ID);
            textViewAddress=itemView.findViewById(R.id.diagnosticAddress_ID);
            textViewPhone=itemView.findViewById(R.id.diagnosticPhone_ID);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            Pharmacy pharmacy=pharmacyList.get(getAdapterPosition());
            String phrname=pharmacy.getTitle();

            Intent intent=new Intent(context, PharmacyFromActivity.class);
            intent.putExtra("phrname",phrname);
            context.startActivity(intent);
//            if (listener!=null){
//                int position=getAdapterPosition();
//                if (position!=RecyclerView.NO_POSITION){
//                    listener.selected(position);
//                }
//            }
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
            MenuItem delete=contextMenu.add(Menu.NONE,1,1,"Delete");

            delete.setOnMenuItemClickListener(this);
        }
    }

    public interface onItemClickListener{
        void selected(int position);
        void delete(int position);
    }

    public void setOnClickListener(onItemClickListener listener){
        this.listener=listener;
    }

}
