package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;



public class StartActivity extends AppCompatActivity {
   RelativeLayout start_btn;
   RelativeLayout native_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        start_btn=findViewById(R.id.start_btn);
        native_container=findViewById(R.id.native_container);
        //AppManage.getInstance(this).showNative((ViewGroup) findViewById(R.id.native_container), ADMOB_N, FACEBOOK_N, StartActivity.this);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppManage.getInstance(StartActivity.this).showInterstitialAd(StartActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(),GameAndPlayScreenActivity.class));
                        finish();
                    }
                });

            }
        });
    }


}
