package com.aman.health;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aman.health.SearchFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new StepActivity();
            case 1:
                return new ProFileFragment();
            case 2:
                return new SearchFragment();
            default:
                return new StepActivity();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}