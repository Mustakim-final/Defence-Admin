package com.example.doctorapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorapp.MessageWithPatientMainActivity;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewAdapter> {

    Context context;
    List<Users> usersList;
    String userID;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_item_design,parent,false);
        return new ViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter holder, int position) {
        Users users=usersList.get(position);

        userID=users.getId();
        holder.textView.setText(users.getUsername());


    }

    @Override
    public int getItemCount() {
        return usersList.size();
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
            Users users=usersList.get(getAdapterPosition());
            userID=users.getId();

            Intent intent=new Intent(context, MessageWithPatientMainActivity.class);
            intent.putExtra("userID",userID);
            context.startActivity(intent);
        }
    }
}
