package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;

public class ThanksActivity extends AppCompatActivity {
    RelativeLayout banner_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);
        banner_ads=findViewById(R.id.banner_ads);
        AppManage.getInstance(this).showNative(((ViewGroup) banner_ads), ADMOB_N, this);
    }

    @Override
    public void onBackPressed() {

        finishAffinity();
        System.exit(0);
    }
}
