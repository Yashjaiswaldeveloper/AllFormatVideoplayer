package com.ffapp.ffxx.ffplayers.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.DuplicateVideoAdapter;
import com.ffapp.ffxx.ffplayers.comman.FilterDuplicates;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterDuplicateActivity extends AppCompatActivity {
   RecyclerView recycle_view;
    String folderPath;
    String foldername = "";
    private ArrayList<File> files = new ArrayList<>();
    ArrayList<String> listfile=new ArrayList<>();
    RelativeLayout back_btn;
    AVLoadingIndicatorView loding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_duplicate);
        recycle_view=findViewById(R.id.recycle_view);
        back_btn=findViewById(R.id.back_btn);
        loding=findViewById(R.id.loding);
        folderPath=getIntent().getStringExtra("Folder");
        if (folderPath != null) {
            foldername = folderPath.substring(folderPath.lastIndexOf("/"));
        }
        getDuplicateData();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==-1)
        {
          finish();
        }
    }

    public void getDuplicateData()
    {
        Uri uri;
        Cursor cursor = null;
        int column_index_data;
        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media._ID, MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Media.BUCKET_ID};
        String selection = MediaStore.Video.Media.DATA + " like?";
        String folder = "%" +  foldername + "%";
        String[] selectionArgs = new String[]{folder};
        if (foldername.isEmpty()) {
            cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        } else {
            cursor = getApplicationContext().getContentResolver().query(uri, projection, selection, selectionArgs, null);
        }
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext())
        {
            absolutePathOfImage = cursor.getString(column_index_data);
            File file = new File(absolutePathOfImage);
            files.add(file);
        }
        if (files != null && files.size() != 0) {
            File dir = null;
            if (folderPath != null) {
                dir = new File(folderPath);
                if (!dir.isDirectory()) {

                }
            }
            Map<String, List<String>> lists = new HashMap<String, List<String>>();
            try {
                FilterDuplicates.findDuplicateFiles(lists, dir, FilterDuplicateActivity.this, files);
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            for (List<String> list : lists.values()) {
                if (list.size() > 1) {
                    for (String file1 : list) {
                        File file = new File(file1);
                        listfile.add(file.getAbsolutePath());

                    }

                }

            }
            if(listfile!=null && !listfile.isEmpty())
            {
                Log.e("List_file",""+listfile);
                loding.hide();
                DuplicateVideoAdapter duplicateAdapter = new DuplicateVideoAdapter(listfile,this);
                recycle_view.setAdapter(duplicateAdapter);
                GridLayoutManager manager = new GridLayoutManager(this, 3);
                recycle_view.setLayoutManager(manager);
            }
            else
            {
                loding.hide();
                Intent intent=new Intent(getApplicationContext(),NoDuplicateVideoActivity.class);
                startActivityForResult(intent,2);
            }


        }
    }

}
