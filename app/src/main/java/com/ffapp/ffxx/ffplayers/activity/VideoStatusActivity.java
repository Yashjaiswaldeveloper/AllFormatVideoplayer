package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;


public class VideoStatusActivity extends AppCompatActivity {
  VideoView video_play;
  String url;
  TextView share_btn,save_btn;
  RelativeLayout back_btn,banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_status);
        video_play=findViewById(R.id.video_play);
        share_btn=findViewById(R.id.share_btn);
        save_btn=findViewById(R.id.save_btn);
        back_btn=findViewById(R.id.back_btn);
        banner_ads=findViewById(R.id.banner_ads);
        //AppManage.getInstance(this).showNative((ViewGroup) banner_ads, ADMOB_N, FACEBOOK_N,this);
        url=getIntent().getStringExtra("vid");
        video_play.setVideoURI(Uri.parse(url));
        video_play.start();
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVideoToInternalStorage(url) ;
            }
        });

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(url);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveVideoToInternalStorage (String filePath) {

        File newfile;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yy mm hh");
            String date = sd.format(new Date());
            String name = "Status_" + date + ".mp4";

            File currentFile = new File(filePath);
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "SaxHdVideo";
            File rootFile = new File(root);
            newfile = new File(rootFile, name);

            if(currentFile.exists()){

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Toast.makeText(getApplicationContext(),"Save Video",Toast.LENGTH_SHORT).show();


            }else{
                Log.v("", "Video saving failed. Source file missing.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void shareImage(String vid) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("video/*");
        File imageFileToShare = new File(vid);
        Uri uri= FileProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider",imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

}
