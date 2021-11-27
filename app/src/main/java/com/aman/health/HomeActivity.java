package com.aman.health;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    public static Context mContext;




    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.view_pager);

        //FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.Alarm, 0, AlarmManager.INTERVAL_DAY);
        FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.wtdayaction, 0, 1000);
        FbData.resetAlarm(getApplicationContext(), MyReceiver.class, FbData.wtweekaction, 0, 1000);


        Log.d("메인화면", "메인화면 시작");

        setViewPager();

        navigationView.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.test:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.action_profile:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.action_step:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.action_exercise:
                    viewPager.setCurrentItem(3);
            }
            return true;
        });
    }


    private void setViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationView.getMenu().findItem(R.id.test).setChecked(true);
                        break;
                    case 1:
                        navigationView.getMenu().findItem(R.id.action_profile).setChecked(true);
                        break;
                    case 2:
                        navigationView.getMenu().findItem(R.id.action_step).setChecked(true);
                        break;
                    case 3:
                        navigationView.getMenu().findItem(R.id.action_exercise).setChecked(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
