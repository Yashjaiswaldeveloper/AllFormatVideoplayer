package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ffapp.ffxx.ffplayers.fragement.ImageFragment;
import com.ffapp.ffxx.ffplayers.fragement.SaveVideoFragment;
import com.ffapp.ffxx.ffplayers.fragement.VideoFragment;

public class StatusAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;
    public StatusAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ImageFragment homeFragment = new ImageFragment();
                return homeFragment;
            case 1:
                VideoFragment sportFragment = new VideoFragment();
                return sportFragment;
            case 2:
                SaveVideoFragment saveVideoFragment=new SaveVideoFragment();
                return saveVideoFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
