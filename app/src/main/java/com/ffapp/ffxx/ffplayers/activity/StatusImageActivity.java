package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ffapp.ffxx.ffplayers.R;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;


public class StatusImageActivity extends AppCompatActivity {
    ImageView image_view;
    TextView image_down_btn,share_btn;
    String image,status;
    RelativeLayout back_btn,banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_image);
        image_down_btn=findViewById(R.id.save_btn);
        image_view=findViewById(R.id.image_view);
        back_btn=findViewById(R.id.back_btn);
        banner_ads=findViewById(R.id.banner_ads);
        image=getIntent().getStringExtra("img");
        status=getIntent().getStringExtra("save_img");
        share_btn=findViewById(R.id.share_btn);
        //AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        Glide.with(this).load(image).into(image_view);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(image);
            }
        });
        image_down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap myBitmap = BitmapFactory.decodeFile(image);
                try {
                    saveImageToExternal(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        if(status.equals("save"))
        {

            image_down_btn.setClickable(false);
        }
        else
        {
            image_down_btn.setVisibility(View.VISIBLE);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void saveImageToExternal(Bitmap bm) throws IOException {
        SimpleDateFormat sd = new SimpleDateFormat("yy mm hh");
        String date = sd.format(new Date());
        String name = "Status_" + date + ".png";
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "SaxHdVideo";
        File rootFile = new File(root);
        rootFile.mkdirs();
        File imageFile = new File(rootFile, name);
        FileOutputStream out = new FileOutputStream(imageFile);
        try{
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                }
            });
            Toast.makeText(getApplicationContext(),"Save Image Sucessfully !",Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            throw new IOException();
        }
    }

    private void shareImage(String img) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        File imageFileToShare = new File(img);
        Uri uri= FileProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider",imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image!"));
    }

}
