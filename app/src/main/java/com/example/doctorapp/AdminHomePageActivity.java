package com.example.doctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminHomePageActivity extends AppCompatActivity {
    CardView doctorCard,productCard,postCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        doctorCard=findViewById(R.id.doctorCard_ID);
        productCard=findViewById(R.id.productCard_ID);
        postCard=findViewById(R.id.photoCard_ID);

        doctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this, Admin_post_image_slider_Activity.class);
                startActivity(intent);
            }
        });
    }
}