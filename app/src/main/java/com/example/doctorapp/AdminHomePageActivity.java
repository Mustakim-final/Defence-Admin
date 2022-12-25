package com.example.doctorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AdminHomePageActivity extends AppCompatActivity {
    CardView doctorCard,productCard,postCard,healthCard,hospitalCard,ambulanceCard,diagnosticCard;
    CardView pharmacyCard,suggestionCard,blogCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        doctorCard=findViewById(R.id.doctorCard_ID);
        productCard=findViewById(R.id.productCard_ID);
        postCard=findViewById(R.id.photoCard_ID);
        healthCard=findViewById(R.id.helthCard_ID);
        hospitalCard=findViewById(R.id.hospitalCard_ID);
        ambulanceCard=findViewById(R.id.ambulanceCard_ID);
        diagnosticCard=findViewById(R.id.diagnosticCard_ID);
        pharmacyCard=findViewById(R.id.pharmacyCard_ID);
        suggestionCard=findViewById(R.id.suggestionCard_ID);
        blogCard=findViewById(R.id.blogCard);

        doctorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this, Admin_Doctor_MainActivity.class);
                startActivity(intent);
            }
        });
        productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,AdminProductActivity.class);
                startActivity(intent);
            }
        });

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this, Admin_post_image_slider_Activity.class);
                startActivity(intent);
            }
        });

        healthCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,AddminHealthActivity.class);
                startActivity(intent);
            }
        });

        hospitalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,AdminHospitalActivity.class);
                startActivity(intent);
            }
        });

        ambulanceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,AdminAmbulanceActivity.class);
                startActivity(intent);
            }
        });

        diagnosticCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,DiagnosticActivity.class);
                startActivity(intent);
            }
        });


        pharmacyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,PharmacyActivity.class);
                startActivity(intent);
            }
        });

        suggestionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,SuggestionActivity.class);
                startActivity(intent);
            }
        });

        blogCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomePageActivity.this,BlogActivity.class);
                startActivity(intent);
            }
        });
    }
}