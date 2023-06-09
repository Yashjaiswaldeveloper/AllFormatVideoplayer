package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.ImageAdapter;
import com.ffapp.ffxx.ffplayers.model.PictureFace;
import com.pesonal.adsdk.AppManage;

import java.util.ArrayList;



public class ImageDisplay extends AppCompatActivity {
    TextView title;
    RecyclerView recycle_view;
    String image_path,foldername;
    int count;
    private ImageAdapter adapter;
    RelativeLayout back_btn,banner_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        title=findViewById(R.id.title);
        recycle_view=findViewById(R.id.recycle_view);
        back_btn=findViewById(R.id.back_btn);
       // banner_ads=findViewById(R.id.banner_ads);
        recycle_view.setLayoutManager(new GridLayoutManager(this,3));

        Intent intent=getIntent();
        image_path=intent.getStringExtra("folderPath");
        foldername=intent.getStringExtra("folderName");
        count=intent.getIntExtra("count",0);

        //AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        title.setText(""+foldername+" ("+count+")");
        getAllImagesByFolder(foldername);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public ArrayList<PictureFace> getAllImagesByFolder(String path){
        ArrayList<PictureFace> images = new ArrayList<>();
        Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplay.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                PictureFace pic = new PictureFace();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));
                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<PictureFace> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }

            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter=new ImageAdapter(images,this);
        recycle_view.setAdapter(adapter);
        return images;
    }
}
