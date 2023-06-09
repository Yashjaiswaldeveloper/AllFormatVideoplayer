package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ffapp.ffxx.ffplayers.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;


public class HomeActivity extends AppCompatActivity {
    RelativeLayout start_btn,share_btn,native_container,banner_ads;
    String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_SETTINGS,Manifest.permission.MEDIA_CONTENT_CONTROL};
    int PERMISSION_ALL = 303;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        start_btn=findViewById(R.id.start_btn);
        share_btn=findViewById(R.id.share_btn);
        native_container=findViewById(R.id.native_container);
        banner_ads=findViewById(R.id.banner_ads);
        if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        AppManage.getInstance(this).showNative((ViewGroup) banner_ads, ADMOB_N, this);
       // AppManage.getInstance(this).showNative((ViewGroup) native_container, ADMOB_N, FACEBOOK_N,this);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManage.getInstance(HomeActivity.this).showInterstitialAd(HomeActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                });

            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sax Video Player");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (!(Build.VERSION.SDK_INT < 23 || context == null || permissions == null)) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }


}
