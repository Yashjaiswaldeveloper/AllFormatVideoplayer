package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_B;
import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_NB;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.AllVideoAdapter;
import com.ffapp.ffxx.ffplayers.adapter.GridAllVideoAdapter;
import com.ffapp.ffxx.ffplayers.comman.OnItemRestoreListener;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;
import com.ffapp.ffxx.ffplayers.player.VideoPlayActivity;
import com.pesonal.adsdk.AppManage;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllVideoActivity extends AppCompatActivity implements OnItemRestoreListener {
    public static ArrayList<VideoModel> videoArrayList=new ArrayList<>();
    RecyclerView recycle_video;
    AllVideoAdapter adapter;
    GridAllVideoAdapter allVideoAdapter;
    boolean checked;
    private String link="";
    SharePreferencess sp;
    RelativeLayout navigation_btn,back_btn;
    OnItemRestoreListener onItemRestoreListener;
    TextView all_title;
    ImageView grid_btn;
    ImageView lock_btn;
    SearchView search_view;
    SwipeRefreshLayout swip_btn;
    RelativeLayout banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_video);
        recycle_video=findViewById(R.id.recycle_video);
        navigation_btn=findViewById(R.id.navigation_btn);
        lock_btn=findViewById(R.id.lock_btn);
        grid_btn=findViewById(R.id.grid_btn);
        all_title=findViewById(R.id.all_title);
        search_view=findViewById(R.id.search_view);
        swip_btn=findViewById(R.id.swip_btn);
        back_btn=findViewById(R.id.back_btn);
        banner_ads=findViewById(R.id.banner_ads);
        sp=new SharePreferencess(this);
        onItemRestoreListener=this;
        videoArrayList.clear();
        AppManage.getInstance(AllVideoActivity.this).showNativeBanner((ViewGroup) findViewById(R.id.banner_ads), ADMOB_B[0], FACEBOOK_NB[0]);
        new LoadData(MediaStore.Video.Media.DISPLAY_NAME,onItemRestoreListener).execute();

        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AllVideoActivity.this, navigation_btn);
                popup.getMenuInflater().inflate(R.menu.main_manu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.name:
                                 break;
                            case R.id.ascending:
                                 break;
                            case R.id.refersh_btn:
                                 new LoadData(MediaStore.Video.Media.DISPLAY_NAME,onItemRestoreListener).execute();
                                 break;
                            case R.id.date:
                                 new LoadData(MediaStore.Video.Media.DATE_ADDED,onItemRestoreListener).execute();
                                 break;
                            case R.id.size:
                                 new LoadData(MediaStore.Video.Media.SIZE,onItemRestoreListener).execute();
                                 break;
                            case R.id.as_duration:
                                 new LoadData(MediaStore.Video.Media.DURATION,onItemRestoreListener).execute();
                                 break;
                            case R.id.share_btn:
                                  shareApp();
                                 break;
                            case R.id.sett_btn:
                                 startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                                 break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        grid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getChangeLayout())
                {
                    grid_btn.setImageResource(R.drawable.grid_2);
                    sp.setChangeLayout(false);
                    new LoadData(MediaStore.Video.Media.DISPLAY_NAME,onItemRestoreListener).execute();
                }
                else
                {
                    grid_btn.setImageResource(R.drawable.ic_grid_1);
                    sp.setChangeLayout(true);
                    new LoadData(MediaStore.Video.Media.DISPLAY_NAME,onItemRestoreListener).execute();
                }
            }
        });

        if(sp.getChangeLayout())
        {
            grid_btn.setImageResource(R.drawable.grid_2);
        }
        else
        {
            grid_btn.setImageResource(R.drawable.ic_grid_1);
        }

        lock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getLogin())
                {
                    startActivity(new Intent(getApplicationContext(),LockScreenActivity.class));
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(),LockConfirmActivity.class));
                }

            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        swip_btn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip_btn.setRefreshing(false);
                new LoadData(MediaStore.Video.Media.DISPLAY_NAME,onItemRestoreListener).execute();
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
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "All Format Video player 2022");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.LIBRARY_PACKAGE_NAME + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class LoadData extends AsyncTask<Void,Void,Void>
    {
        String orderby;
        OnItemRestoreListener listener;
        public LoadData(String orderby,OnItemRestoreListener listener) {
            this.orderby=orderby;
            this.listener=listener;
            videoArrayList.clear();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, null, null, null, orderby);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    @SuppressLint("Range") String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    try {
                        if(sp.getTagVideo(data))
                        {

                        }
                        else
                        {
                            sp.setTageVideo(data,false);
                        }
                        VideoModel videoModel  = new VideoModel();
                        videoModel.setVideoTitle(title);
                        videoModel.setVideoUri(Uri.parse(data));
                        videoModel.setVideoDuration(timeConversion(Long.parseLong(duration)));
                        videoModel.setGetViewType(1);
                        videoArrayList.add(videoModel);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext());
            }

            int size=videoArrayList.size();
            int mod=size/3;
            for(int i=1;i<=mod;i++)
            {
                VideoModel videoModel  = new VideoModel();
                videoModel.setGetViewType(2);
                videoArrayList.add(3*i,videoModel);
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(sp.getChangeLayout())
            {
                GridLayoutManager layoutManager=new GridLayoutManager(AllVideoActivity.this,2);
                recycle_video.setLayoutManager(layoutManager);
                allVideoAdapter= new GridAllVideoAdapter(AllVideoActivity.this, videoArrayList,listener);
                recycle_video.setAdapter(allVideoAdapter);
            }
            else
            {
                recycle_video.setLayoutManager(new LinearLayoutManager(AllVideoActivity.this));
                adapter= new AllVideoAdapter (AllVideoActivity.this, videoArrayList,listener);
                recycle_video.setAdapter(adapter);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==107 && resultCode==-1)
        {

        }
    }

    public String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }
    public void DeleteVideo(final File file) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AllVideoActivity.this);
        View view = LayoutInflater.from(AllVideoActivity.this).inflate(R.layout.delete_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        final CheckBox check_box = view.findViewById(R.id.check_box);

        check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = ((CheckBox) v).isChecked();
                if (checked) {
                    check_box.setChecked(true);

                } else {
                    check_box.setChecked(false);
                }
            }
        });

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
                if (checked) {

                   boolean status=Utils.moveToPrivate(file,file.getName(),AllVideoActivity.this,getResources().getString(R.string.recycle));
                   if(status)
                   {
                       sp.setRecoverVideoPath(file.getAbsolutePath());
                       adapter.notifyDataSetChanged();
                   }
                   else
                   {
                       Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
                   }

                } else {
                    Toast.makeText(getApplicationContext(),"click check box",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void Rename(final File file) {
        link=file.getAbsolutePath();
        AlertDialog.Builder builder = new AlertDialog.Builder(AllVideoActivity.this);
        View view = LayoutInflater.from(AllVideoActivity.this).inflate(R.layout.rename_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        final EditText rename_txt = view.findViewById(R.id.rename_txt);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String videoName = Utils.getFileTitleFromFileName(file.getName());
        rename_txt.setText(videoName);
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
                String name=rename_txt.getText().toString();
                if(name.isEmpty()||name.equals(""))
                {
                    rename_txt.setError("please enter name");
                }
                else
                {
                    String filepath = link.substring(0, link.lastIndexOf("/"));
                    String videoName = file.getName();
                    String extension = videoName.substring(videoName.lastIndexOf("."));
                    String newPath = filepath + File.separator + rename_txt.getText().toString() + extension;
                    File file = new File(link);
                    boolean status= Utils.renameFile(file,name+extension,AllVideoActivity.this);

                    if(status)
                    {
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }
    public void PropertiesBox(File file) {
        String size = Utils.getFileSize(file);
        long lastcreat = file.lastModified();


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        AlertDialog.Builder builder = new AlertDialog.Builder(AllVideoActivity.this);
        View view = LayoutInflater.from(AllVideoActivity.this).inflate(R.layout.properties_file, null);
        TextView file_txt = view.findViewById(R.id.file_txt);
        TextView duration_txt = view.findViewById(R.id.duration_txt);
        TextView file_size_txt = view.findViewById(R.id.file_size_txt);
        TextView location_txt = view.findViewById(R.id.location_txt);
        TextView date_txt = view.findViewById(R.id.date_txt);
        RelativeLayout ok_btn = view.findViewById(R.id.ok_btn);
        file_txt.setText(file.getName());
        try {
            duration_txt.setText(timeConversion(lastcreat));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        date_txt.setText(strDate);
        file_size_txt.setText(size);
        location_txt.setText("" + file.getAbsolutePath());

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void shareVideo(File file)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider",file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Video Title");
        startActivity(Intent.createChooser(sharingIntent, "Share Video!"));

    }
    @Override
    public void OnClick(int postion) {

        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
        intent.putExtra("pos", postion);
        startActivityForResult(intent,107);
    }
    @Override
    public void OnItemClick(int pos, final File file, RelativeLayout layout) {
        PopupMenu popup = new PopupMenu(AllVideoActivity.this, layout);
        popup.getMenuInflater().inflate(R.menu.specific_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.lock_btn:
                        boolean status= Utils.lockFile(file,AllVideoActivity.this);
                        if(status)
                        {
                            sp.setPrivateVideoPath(file.getAbsolutePath());
                            adapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.share_btn:
                        shareVideo(file);
                        break;
                    case R.id.del_btn:
                        DeleteVideo(file);

                        break;
                    case R.id.proper_btn:
                        PropertiesBox(file);
                        break;
                    case R.id.rename_btn:
                        Rename(file);

                        break;
                    case R.id.retag_btn:
                        break;
                    case R.id.sett_btn:
                        startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                        break;

                }

                return true;
            }
        });
        popup.show();
    }

    private void filter(String text) {

        ArrayList<VideoModel> filteredlist = new ArrayList<>();
        for (VideoModel item : videoArrayList) {

            if (item.getVideoTitle().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {

            if(sp.getChangeLayout())
            {
                allVideoAdapter.filterList(filteredlist);
            }
            else
            {
                adapter.filterList(filteredlist);
            }

        }
    }
}
