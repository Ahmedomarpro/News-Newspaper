package com.newsapp.my_design_new;

import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity   {
    DrawerLayout drawer;
    LinearLayout imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         ViewPager viewPager = findViewById(R.id.viewPager);
        if (viewPager != null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


             viewPager.setAdapter(adapter);
         }


//         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, 					R.string.navigation_drawer_close);
//        //drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START);
//                } else {
//                    drawer.closeDrawer(GravityCompat.START);
//
//                }
//            }
//        });

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);


        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    public void onBackPressed() {
         if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
             super.onBackPressed();
         }
    }
}