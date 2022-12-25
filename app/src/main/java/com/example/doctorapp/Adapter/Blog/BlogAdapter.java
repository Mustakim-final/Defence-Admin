package com.example.doctorapp.Adapter.Blog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Model.Blog;
import com.example.doctorapp.Model.Diagnostic;
import com.example.doctorapp.R;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyHolder>{

    Context context;
    List<Blog> blogList;


    public BlogAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.blog_item1,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Blog blog=blogList.get(position);

        if (blog.getImageUrl().equals("imageUrl")){
            holder.imageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(blog.getImageUrl()).into(holder.imageView);
        }

        holder.typeText.setText(blog.getType());
        holder.titleText.setText(blog.getTitle());
        holder.dateText.setText(blog.getDay()+"/"+blog.getMonth()+"/"+blog.getYear());
        holder.nameText.setText("Post by: "+blog.getName());
        holder.descriptionText.setText(blog.getDescription());
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView typeText,titleText,dateText,nameText,descriptionText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.blogImage_ID);
            titleText=itemView.findViewById(R.id.blogTitle_ID);
            typeText=itemView.findViewById(R.id.blogType_ID);
            dateText=itemView.findViewById(R.id.blogDate_ID);
            nameText=itemView.findViewById(R.id.blogName_ID);
            descriptionText=itemView.findViewById(R.id.blogDescription_ID);
        }
    }
}
