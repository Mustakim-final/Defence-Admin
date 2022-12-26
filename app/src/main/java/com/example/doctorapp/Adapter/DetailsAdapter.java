package com.example.doctorapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.MessageWithPatientMainActivity;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.Model.Users;
import com.example.doctorapp.R;
import com.example.doctorapp.SetNotification.ApiService;
import com.example.doctorapp.SetNotification.Client;
import com.example.doctorapp.SetNotification.Data;
import com.example.doctorapp.SetNotification.MyResponse;
import com.example.doctorapp.SetNotification.Notification;
import com.example.doctorapp.SetNotification.Sender;
import com.example.doctorapp.SetNotification.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.MyHolder> {
    Context context;
    List<All_Doctor> all_doctorList;
    String userID;
    DatabaseReference reference;

    ApiService apiService;
    boolean notify=false;

    FirebaseUser firebaseUser;

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

        apiService= Client.getCLient("https://fcm.googleapis.com/").create(ApiService.class);

        holder.nameText.setText("Name: "+all_doctor.getName());
        holder.ageText.setText("Age: "+all_doctor.getAge());
        holder.genderText.setText("Gender: "+all_doctor.getGender());
        holder.phoneText.setText("Phone: "+all_doctor.getPhone());
        //holder.addressText.setText(all_doctor.getAddress());

        holder.informationText.setText(all_doctor.getInformation());

        if (all_doctor.getImageUrl().equals("imageUrl")){
            holder.circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);

        }else {
            Glide.with(context).load(all_doctor.getImageUrl()).into(holder.circleImageView);
        }

        if (all_doctor.getImagePost().equals("imagePost")){
            holder.imageView.setVisibility(View.GONE);

        }else {
            Glide.with(context).load(all_doctor.getImagePost()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return all_doctorList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        ImageView imageView;
        TextView informationText,nameText,ageText,genderText,phoneText,addressText;
        Button conBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.doctorProfile_ID);
            nameText=itemView.findViewById(R.id.doctorName);
            ageText=itemView.findViewById(R.id.doctorAge);
            genderText=itemView.findViewById(R.id.doctorGender);
            phoneText=itemView.findViewById(R.id.doctorPhone);
            addressText=itemView.findViewById(R.id.doctorAddress);
            imageView=itemView.findViewById(R.id.viewImage_ID);

            informationText=itemView.findViewById(R.id.doctorInformation_ID);
            conBtn=itemView.findViewById(R.id.confirm_button_ID);

            conBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    notify=true;
                    All_Doctor users=all_doctorList.get(getAdapterPosition());
                    userID=users.getId();

                    reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(userID);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("status","yes");
                    reference.updateChildren(hashMap);



                    Toast.makeText(context, ""+userID, Toast.LENGTH_LONG).show();


                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isComplete()){
                                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                        String refreshToken=task.getResult().getToken();
                                        if (firebaseUser!=null){
                                            updateToken(refreshToken);
                                        }


                                    }
                                }
                            });

                    final String msg="Confirm";
                    final String admin="Confirm form Admin";
                    sendNotitfication(userID,users.getUsername(), msg);
                }
            });
        }
    }



    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("token");
        Token token=new Token(refreshToken);
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

    private void sendNotitfication(String userID, String username, String msg) {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("token");
        Query query=tokens.orderByKey().equalTo(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data("admin",R.mipmap.main_icon,username+":"+msg,"New Message",userID);
                    Notification notification=new Notification(username,msg,R.mipmap.main_icon);

                    Sender sender=new Sender(token.getToken(),data,notification);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if (response.body().success!=1){
                                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
