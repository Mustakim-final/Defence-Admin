package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.example.doctorapp.Adapter.Blog.BlogAdapter;
import com.example.doctorapp.Model.Blog;
import com.example.doctorapp.Model.Diagnostic;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main_Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    RecyclerView recyclerView;
    BlogAdapter blogAdapter;
    List<Blog> blogList;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        toolbar=findViewById(R.id.toolBar_ID);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.blogRecycler_ID);


        drawerLayout=findViewById(R.id.drawer_ID);
        NavigationView navigationView=findViewById(R.id.navigation_ID);
        navigationView.setNavigationItemSelectedListener(this);
        toggle=new ActionBarDrawerToggle(Main_Home_Activity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        blogList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Blog");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                blogList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Blog blog=dataSnapshot.getValue(Blog.class);
                    blogList.add(blog);
                }

                blogAdapter=new BlogAdapter(Main_Home_Activity.this,blogList);
                recyclerView.setAdapter(blogAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.doctorLogin_ID){
            Intent intent=new Intent(Main_Home_Activity.this,MainActivity.class);
            startActivity(intent);
        }else if (item.getItemId()==R.id.adminLogin){
            Intent intent=new Intent(Main_Home_Activity.this,AdminHomePageActivity.class);
            startActivity(intent);
        }
        return false;
    }
}