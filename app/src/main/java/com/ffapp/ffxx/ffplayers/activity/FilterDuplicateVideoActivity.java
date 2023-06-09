package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.DuplicateVideoAdapter;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.model.VideoModal;
import com.pesonal.adsdk.AppManage;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class FilterDuplicateVideoActivity extends AppCompatActivity {
    HashMap<String, String> maplist;
    HashMap<String, List<String>> dupList;
    private String bigint;
    boolean isDuplicateFound = false;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<VideoModal> list=new ArrayList<>();
    private int intentCode=12;
    private int count;
    private int ids[];
    private String[] arrPath;
    boolean[] thumbnailsselection;
    RecyclerView recycle_view;
    AVLoadingIndicatorView loding;
    RelativeLayout del_btn,back_btn,banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_duplicate_video);
        recycle_view=findViewById(R.id.recycle_view);
        loding=findViewById(R.id.loding);
        del_btn=findViewById(R.id.del_btn);
        back_btn=findViewById(R.id.back_btn);

       // banner_ads=findViewById(R.id.banner_ads);
        //AppManage.getInstance(this).showNative((ViewGroup) banner_ads, ADMOB_N, FACEBOOK_N,this);
        //AppManage.getInstance(this).show_BANNER((ViewGroup) banner_ads, ADMOB_B1, FACEBOOK_B1,this,"full");
        arrayList.clear();
        maplist = new HashMap<>();
        dupList = new HashMap<>();
        if (intentCode == 12) {
            getVideoList();
            if (maplist != null) {
                maplist = new HashMap<>();
                for (VideoModal s : list) {
                    String a = getImputFromMd5(s.path);
                    maplist.put(s.path, a);
                }
            } else {
                for (VideoModal s : list) {
                    String a = getImputFromMd5(s.path);
                    maplist.put(s.path, a);
                }
            }
            getDuplicateValue(maplist, dupList);
            if (dupList.size() > 0) {

                Iterator my = dupList.keySet().iterator();
                while (my.hasNext()) {
                    String key = (String) my.next();
                    List<String> value = dupList.get(key);
                    if (value.size() > 1) {
                        isDuplicateFound = true;
                        Log.e("Duplecate_video",""+value);
                        arrayList.addAll(value);
                    }
                }

                if (isDuplicateFound) {
                    loding.hide();
                    DuplicateVideoAdapter duplicateAdapter = new DuplicateVideoAdapter( arrayList,this);
                    recycle_view.setAdapter(duplicateAdapter);
                    GridLayoutManager manager = new GridLayoutManager(this, 3);
                    recycle_view.setLayoutManager(manager);

                } else {

                    Intent intent=new Intent(getApplicationContext(),NoDuplicateVideoActivity.class);
                    startActivityForResult(intent,2);
                }

            } else {
                Intent intent=new Intent(getApplicationContext(),NoDuplicateVideoActivity.class);
                startActivityForResult(intent,2);

            }

        } else {

            ArrayList<String> list = new ArrayList<>();
            if (maplist != null) {
                maplist = new HashMap<>();
                for (String path : list) {
                    String a = getImputFromMd5(path);
                    maplist.put(path, a);
                }
            } else {
                for (String path : list) {
                    String a = getImputFromMd5(path);
                    maplist.put(path, a);
                }
            }

            getDuplicateValue(maplist, dupList);
            if (dupList.size() > 0) {

                Iterator my = dupList.keySet().iterator();
                while (my.hasNext()) {
                    String key = (String) my.next();
                    List<String> value = dupList.get(key);
                    if (value.size() > 1) {
                        isDuplicateFound = true;
                        arrayList.addAll(value);
                    }
                }

                if (isDuplicateFound) {

                    loding.hide();
                    DuplicateVideoAdapter duplicateAdapter = new DuplicateVideoAdapter( arrayList,this);
                    recycle_view.setAdapter(duplicateAdapter);
                    GridLayoutManager manager = new GridLayoutManager(this, 3);
                    recycle_view.setLayoutManager(manager);

                } else {
                    Intent intent=new Intent(getApplicationContext(),NoDuplicateVideoActivity.class);
                    startActivityForResult(intent,2);
                }

            } else {
                Intent intent=new Intent(getApplicationContext(),NoDuplicateVideoActivity.class);
                startActivityForResult(intent,2);

            }
        }
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteVideo();
            }
        });
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

    public void getDuplicateValue(HashMap<String, String> maplist, HashMap<String, List<String>> dupList) {
        Set<Map.Entry<String, String>> entrySet = maplist.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();

            if (dupList.containsKey(value)) {
                addToList(value, key, dupList);

            } else {
                ArrayList<String> a = new ArrayList<>();
                a.add(key);
                dupList.put(value, a);
            }

        }


    }
    public synchronized void addToList(String mapKey, String myItem, HashMap<String, List<String>> dupList) {

        List<String> itemsList = dupList.get(mapKey);

        // if list does not exist create it
        if (itemsList == null) {
            itemsList = new ArrayList<String>();
            itemsList.add(myItem);
            dupList.put(mapKey, itemsList);
        } else {
            // add if item is not already in list
            if (!itemsList.contains(myItem)) itemsList.add(myItem);
        }
    }
    public String getImputFromMd5(String s1) {
        MessageDigest instance = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(s1);
            instance = MessageDigest.getInstance("MD5");

            byte[] bArr = new byte[1024];
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (i >= 5) {
                    break;
                }
                int read = fileInputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                instance.update(bArr, 0, read);
                i = i2;
            }
            bigint = new BigInteger(1, instance.digest()).toString(16);

            try {
                fileInputStream.close();
            } catch (IOException e) {
                String message = e.getMessage();
                Log.e("e", message);

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bigint;
    }
    private void getVideoList() {
        String[] projection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION};
        Cursor cursor = getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, null);
        int image_column_index = cursor.getColumnIndex(MediaStore.Video.Media._ID);
        count = cursor.getCount();
        arrPath = new String[this.count];
        ids = new int[count];
        thumbnailsselection = new boolean[this.count];
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
                String bucketName = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(projection[3]));
                list.add(new VideoModal(path, bucketName, name, duration));

            } while (cursor.moveToNext());
        }
    }

    public void DeleteVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FilterDuplicateVideoActivity.this);
        View view = LayoutInflater.from(FilterDuplicateVideoActivity.this).inflate(R.layout.delete_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        RelativeLayout layout=view.findViewById(R.id.check_temp);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        layout.setVisibility(View.GONE);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                for (int i = Utils.duplication.size()-1; i >= 0; i--){
                    File file = Utils.duplication.get(i);
                   boolean status= file.delete();
                    if (status){

                        Utils.duplication.remove(i);
                    }
                }

            }
        });

    }
}
