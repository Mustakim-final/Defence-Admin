package com.example.doctorapp.Adapter.Diagnostic;

import android.content.Context;
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
import com.example.doctorapp.Adapter.Admin_confirmAdapter;
import com.example.doctorapp.Model.Diagnostic;
import com.example.doctorapp.R;

import java.util.List;

public class DiagnosticAdapter extends RecyclerView.Adapter<DiagnosticAdapter.MyHolder>{

    Context context;
    List<Diagnostic> diagnosticList;

    private onItemClickListener listener;

    public DiagnosticAdapter(Context context, List<Diagnostic> diagnosticList) {
        this.context = context;
        this.diagnosticList = diagnosticList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.diagnostic_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Diagnostic diagnostic=diagnosticList.get(position);
        Glide.with(context).load(diagnostic.getDiagnostic()).into(holder.diagnosticImage);

        holder.textViewTitle.setText(diagnostic.getTitle());
        holder.textViewAddress.setText(diagnostic.getAddress());
        holder.textViewPhone.setText(diagnostic.getPhone());
    }

    @Override
    public int getItemCount() {
        return diagnosticList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        ImageView diagnosticImage;
        TextView textViewTitle,textViewAddress,textViewPhone;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            diagnosticImage=itemView.findViewById(R.id.diagnosticImage_ID);
            textViewTitle=itemView.findViewById(R.id.diagnosticTitle_ID);
            textViewAddress=itemView.findViewById(R.id.diagnosticAddress_ID);
            textViewPhone=itemView.findViewById(R.id.diagnosticPhone_ID);

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
            contextMenu.setHeaderTitle("Option");
            MenuItem setDiagnostic=contextMenu.add(Menu.NONE,1,1,"Set Item");

            setDiagnostic.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (listener!=null){
                Diagnostic diagnostic=diagnosticList.get(getAdapterPosition());
                String userID=diagnostic.getId();
                int position=getAdapterPosition();

                if (position!=RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            listener.setDiagnostic(position,userID);
                            return true;
                    }
                }
            }
            return false;
        }



    }

    public interface onItemClickListener{
        void onItemClick(int position);
        void setDiagnostic(int position,String userID);
    }

    public void setOnClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
