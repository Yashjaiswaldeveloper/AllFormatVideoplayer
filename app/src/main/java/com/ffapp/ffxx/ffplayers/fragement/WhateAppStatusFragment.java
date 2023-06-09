package com.ffapp.ffxx.ffplayers.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.StatusAdapter;
import com.pesonal.adsdk.AppManage;

import static com.pesonal.adsdk.AppManage.ADMOB_N;


public class WhateAppStatusFragment extends Fragment {
   View view;
   TabLayout tab_layout;
   ViewPager view_pager;
   RelativeLayout native_ads;

    public WhateAppStatusFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_whate_app_status, container, false);

        tab_layout=view.findViewById(R.id.tab_layout);
        view_pager=view.findViewById(R.id.view_pager);
        native_ads=view.findViewById(R.id.native_ads);
        tab_layout.addTab(tab_layout.newTab().setText("Image"));
        tab_layout.addTab(tab_layout.newTab().setText("Video"));
        tab_layout.addTab(tab_layout.newTab().setText("Save"));

        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        AppManage.getInstance(getActivity()).showNative((ViewGroup) native_ads, ADMOB_N, getContext());
       // AppManage.getInstance(getActivity()).show_Banner_google_NATIVE((ViewGroup) native_ads, ADMOB_N1, FACEBOOK_N1,getContext());
        final StatusAdapter adapter = new StatusAdapter(getContext(),getFragmentManager(), tab_layout.getTabCount());
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view_pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

}
