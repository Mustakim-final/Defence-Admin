package com.example.doctorapp.Adapter.Question;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.AnswerActivity;
import com.example.doctorapp.Answer_DoctorActivity;
import com.example.doctorapp.Model.Question;
import com.example.doctorapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class S_D_QuestionAdapter extends RecyclerView.Adapter<S_D_QuestionAdapter.MyHolder>{
    Context context;
    List<Question> questionList;

    public S_D_QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.s_question_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Question question=questionList.get(position);

        if (question.getImageUrl().equals("imageUrl")){
            holder.profileImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(question.getImageUrl()).into(holder.profileImage);
        }

        holder.textViewName.setText(question.getUsername());
        holder.textViewType.setText(question.getQuestionType());

        if (question.getQuestion().equals("")){
            holder.textViewQuestion.setVisibility(View.GONE);
        }else {
            holder.textViewQuestion.setText(question.getQuestion());
        }


        if (question.getQuestionImage().equals("questionImage")){
            holder.imageViewQuestion.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(question.getQuestionImage()).into(holder.imageViewQuestion);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView profileImage;
        TextView textViewName,textViewQuestion,textViewType;
        ImageView imageViewQuestion,imageViewAns;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            profileImage=itemView.findViewById(R.id.s_questionProfileImage_ID);
            textViewName=itemView.findViewById(R.id.s_questionName_ID);
            textViewQuestion=itemView.findViewById(R.id.s_question_ID);
            imageViewQuestion=itemView.findViewById(R.id.s_questionPhoto);
            textViewType=itemView.findViewById(R.id.s_questionType_ID);

            imageViewAns=itemView.findViewById(R.id.s_questionCommentBtn_ID);

            imageViewAns.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            String myId=firebaseUser.getUid();
            if (view.getId()==R.id.s_questionCommentBtn_ID){
                Question question=questionList.get(getAdapterPosition());

                String imageUrl=question.getImageUrl();
                String profileName=question.getUsername();
                String u_question=question.getQuestion();
                String questionImage=question.getQuestionImage();
                String questionType=question.getQuestionType();
                String id=question.getId();

                Intent intent=new Intent(context, Answer_DoctorActivity.class);
                intent.putExtra("imageUrl",imageUrl);
                intent.putExtra("profileName",profileName);
                intent.putExtra("u_question",u_question);
                intent.putExtra("questionImage",questionImage);
                intent.putExtra("questionType",questionType);
                intent.putExtra("id",id);
                intent.putExtra("myID",myId);
                context.startActivity(intent);

            }
        }
    }
}
