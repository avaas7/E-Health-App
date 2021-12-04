package com.example.myhealthapplication.Newsfeed;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myhealthapplication.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem mHome,mScience,mSports,mEntertainment,mTech;
    com.example.myhealthapplication.Newsfeed.PageAdapter pageAdapter;
    Toolbar mtoolbar;
    ViewPager viewPager;

    String apiKey = "be39e2e137c342af9305bf21683cf23d";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.newsTabLayout);
        mHome = findViewById(R.id.Home_tab);
        mScience = findViewById(R.id.Science_tab);
        mSports = findViewById(R.id.Sports_tab);
        mEntertainment = findViewById(R.id.Entertainment_tab);
        mTech = findViewById(R.id.Tech_tab);

 //       mtoolbar = findViewById(R.id.toolBarNews);
        viewPager = findViewById(R.id.newsViewPager);

        pageAdapter = new com.example.myhealthapplication.Newsfeed.PageAdapter(getSupportFragmentManager(),5);
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0 || tab.getPosition()==1 || tab.getPosition()==2 || tab.getPosition()==3 || tab.getPosition()==4)
                {
                    pageAdapter.notifyDataSetChanged();
                    Log.e("hello world","heloooooo");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}