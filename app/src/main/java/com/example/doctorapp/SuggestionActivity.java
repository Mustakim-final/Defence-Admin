package com.example.doctorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.doctorapp.Fragment.Suggestion.S_CorunaFragment;
import com.example.doctorapp.Fragment.Suggestion.S_DentistFragment;
import com.example.doctorapp.Fragment.Suggestion.S_DermatologistFragment;
import com.example.doctorapp.Fragment.Suggestion.S_Diabetes_and_homonesFragment;
import com.example.doctorapp.Fragment.Suggestion.S_DoctorFragment;
import com.example.doctorapp.Fragment.Suggestion.S_Heart_diseaseFragment;
import com.example.doctorapp.Fragment.Suggestion.S_Nose_ear_and_throat_specialistFragment;
import com.example.doctorapp.Fragment.Suggestion.S_PilesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SuggestionActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        toolbar=findViewById(R.id.tool_bar_ID);
        toolbar.setTitle("প্রশ্ন ও উত্তর");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout=findViewById(R.id.suggestion_doctor_tabLayout_ID);
        viewPager=findViewById(R.id.suggestion_doctor_viewPager_ID);


        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new S_DoctorFragment(),"সব");
        viewPagerAdapter.addFragment(new S_CorunaFragment(),"করোনা");
        viewPagerAdapter.addFragment(new S_Heart_diseaseFragment(),"হৃদরোগ");
        viewPagerAdapter.addFragment(new S_PilesFragment(),"কোলোরেক্টাল সার্জারি(পাইলস)");
        viewPagerAdapter.addFragment(new S_DentistFragment(),"ডেন্টিষ্ট");
        viewPagerAdapter.addFragment(new S_DermatologistFragment(),"চর্মরোগ বিশেষজ্ঞ");
        viewPagerAdapter.addFragment(new S_Diabetes_and_homonesFragment(),"ডায়াবেটিস ও হরমোন");
        viewPagerAdapter.addFragment(new S_Nose_ear_and_throat_specialistFragment(),"নাক,কান ও গলা বিশেষজ্ঞ");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;
        ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
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