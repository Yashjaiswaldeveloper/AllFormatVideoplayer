package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.StatusAdapter;

public class StatusActivity extends AppCompatActivity {
  ViewPager pager;
  TabLayout tab_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        pager=findViewById(R.id.view_pager);
        tab_layout=findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("Image"));
        tab_layout.addTab(tab_layout.newTab().setText("Video"));
        tab_layout.addTab(tab_layout.newTab().setText("Save"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        final StatusAdapter adapter = new StatusAdapter(this,getSupportFragmentManager(), tab_layout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
       // pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
    }
}
