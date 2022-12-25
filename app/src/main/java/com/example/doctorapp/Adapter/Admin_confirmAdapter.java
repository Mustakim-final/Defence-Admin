package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_confirmAdapter extends RecyclerView.Adapter<Admin_confirmAdapter.MyHolder> {
    Context context;
    List<All_Doctor> all_doctorList;
    String userID;

    private onItemClickListener listener;

    public Admin_confirmAdapter(Context context, List<All_Doctor> all_doctorList) {
        this.context = context;
        this.all_doctorList = all_doctorList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.admin_all_doctor_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        All_Doctor all_doctor=all_doctorList.get(position);
        holder.nameText.setText(all_doctor.getName());
        holder.degreeText.setText(all_doctor.getInformation());

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

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        CircleImageView circleImageView;
        TextView nameText,degreeText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.admin_doctor_profile_image_ID);
            nameText=itemView.findViewById(R.id.admin_doctor_profile_name_ID);
            degreeText=itemView.findViewById(R.id.admin_doctor_degree_ID);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

            if (listener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Options");
            MenuItem setStatus=contextMenu.add(Menu.NONE,1,1,"Set Status");
            MenuItem setDayAndTime=contextMenu.add(Menu.NONE,2,2,"Set day and Time");

            setStatus.setOnMenuItemClickListener(this);
            setDayAndTime.setOnMenuItemClickListener(this);


        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (listener!=null){
                All_Doctor users=all_doctorList.get(getAdapterPosition());
                userID=users.getId();

                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            listener.setStatus(position,userID);
                            return true;
                        case 2:
                            listener.setDayAndTime(position,userID);
                            return true;

                    }
                }
            }
            return false;
        }
    }


    public interface onItemClickListener{
        void onItemClick(int position);
        void setStatus(int position,String userId);
        void setDayAndTime(int position,String userId);
    }

    public void setOnClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
