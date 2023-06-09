package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.TimerView.ImageLoad;
import com.ffapp.ffxx.ffplayers.TimerView.TimeLineModel;
import com.ffapp.ffxx.ffplayers.TimerView.TimelineFragment;
import com.ffapp.ffxx.ffplayers.TimerView.TimelineGroupType;
import com.ffapp.ffxx.ffplayers.TimerView.TimelineObject;
import com.ffapp.ffxx.ffplayers.TimerView.TimelineObjectClickListener;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.util.ArrayList;


import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;


public class TimeLineVideoActivity extends AppCompatActivity implements TimelineObjectClickListener {
    public  ArrayList<TimelineObject> videoArrayList=new ArrayList<>();
    private TimelineFragment mFragment;
    RelativeLayout timeline_btn;
    RelativeLayout back_btn,banner_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line_video);
        timeline_btn=findViewById(R.id.timeline_btn);
        back_btn=findViewById(R.id.back_btn);
        banner_ads=findViewById(R.id.banner_ads);
        videoArrayList.clear();

        getVideos();
       // AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        mFragment = new TimelineFragment();
        ArrayList<TimelineObject> objs = videoArrayList;
        mFragment.setData(objs, TimelineGroupType.DAY);
        mFragment.addOnClickListener(this);
        mFragment.setImageLoadEngine(new ImageLoad(getApplicationContext()));
        loadFragment(mFragment);

        timeline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TimeLineVideoActivity.this, timeline_btn);
                popup.getMenuInflater().inflate(R.menu.timeline, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.share_btn:
                                shareApp();
                                break;
                            case R.id.sett:
                               startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                                break;

                        }

                        return true;
                    }
                });
                popup.show();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "Download app";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getVideos() {
        System.gc();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                try {
                    long timer= Long.parseLong(duration);
                    File file=new File(String.valueOf(Uri.parse(data)));
                    videoArrayList.add(new TimeLineModel(timer,title,file.getAbsolutePath()));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }

    }
    private void loadFragment(Fragment newFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.commit();
    }

    @Override
    public void onTimelineObjectClicked(TimelineObject timelineObject) {

    }

    @Override
    public void onTimelineObjectLongClicked(TimelineObject timelineObject) {

    }
}
