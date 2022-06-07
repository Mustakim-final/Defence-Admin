package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doctorapp.Fragment.ChatFragment;
import com.example.doctorapp.Fragment.PrescriptionReFragment;
import com.example.doctorapp.Fragment.UserFragment;
import com.example.doctorapp.Model.All_Doctor;
import com.example.doctorapp.Model.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Doctor_Activity extends AppCompatActivity {
    CircleImageView profileImage;
    private TextView myname;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileImage=findViewById(R.id.doctorProfile_ID);
        myname=findViewById(R.id.doctorName_ID);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Doctor List").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                All_Doctor all_doctor=snapshot.getValue(All_Doctor.class);

                myname.setText(all_doctor.getName());

                if (all_doctor.getImageUrl().equals("imageUrl")){
                    profileImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);

                }else {
                    Glide.with(getApplicationContext()).load(all_doctor.getImageUrl()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tabLayout=findViewById(R.id.tabLayout_ID);
        viewPager=findViewById(R.id.viewPager_ID);

        FragmentPager fragmentPager=new FragmentPager(getSupportFragmentManager());

        fragmentPager.addFragment(new PrescriptionReFragment(),"প্রেসক্রিপশন");
        fragmentPager.addFragment(new UserFragment(),"user");
        fragmentPager.addFragment(new ChatFragment(),"প্রোফাইল");

        viewPager.setAdapter(fragmentPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout_ID){
            firebaseAuth=FirebaseAuth.getInstance();
            firebaseAuth.signOut();
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    class FragmentPager extends FragmentPagerAdapter{
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public FragmentPager(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}