package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;


public class GameAndPlayScreenActivity extends AppCompatActivity {
   RelativeLayout play_btn,game_btn;
   RelativeLayout banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_and_play_screen);
        play_btn=findViewById(R.id.play_btn);
        banner_ads=findViewById(R.id.banner_ads);
        game_btn=findViewById(R.id.game_btn);
        //AppManage.getInstance(this).showNative((ViewGroup) banner_ads, ADMOB_N, FACEBOOK_N,this);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManage.getInstance(GameAndPlayScreenActivity.this).showInterstitialAd(GameAndPlayScreenActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    }
                });
            }
        });

        game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Toast.makeText(getApplicationContext(),"Coming soon...",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
