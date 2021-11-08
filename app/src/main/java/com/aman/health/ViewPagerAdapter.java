package com.aman.health;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new ProFileFragment();
            case 2:
                return new StepActivity();
            case 3:
                return new ExerciseFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}