package com.ffapp.ffxx.ffplayers.activity;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
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

import com.ffapp.ffxx.ffplayers.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.AllVideoAdapter;
import com.ffapp.ffxx.ffplayers.adapter.GridAllVideoAdapter;
import com.ffapp.ffxx.ffplayers.adapter.GridNativeAdAdapter;
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
import java.util.HashSet;



public class SpecificVideoActivity extends AppCompatActivity implements OnItemRestoreListener {
    RecyclerView recycle_view;
    public static ArrayList<VideoModel> videosList=new ArrayList<>();
    private final String[] projection2 = new String[]{MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.DATA };
    public static String name;
    AllVideoAdapter adapter;
    GridAllVideoAdapter allVideoAdapter;
    TextView title;
    ImageView grid_btn;
    SharePreferencess sp;
    boolean checked;
    private String link="";
    RelativeLayout navigation_btn;
    OnItemRestoreListener listener;
    ImageView lock_btn;
    SearchView search_view;
    RelativeLayout battry_lock,banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_video);
        title=findViewById(R.id.all_title);
        recycle_view=findViewById(R.id.recycle_view);
        navigation_btn=findViewById(R.id.navigation_btn);
        lock_btn=findViewById(R.id.lock_btn);
        grid_btn=findViewById(R.id.grid_btn);
        search_view=findViewById(R.id.search_view);
        battry_lock=findViewById(R.id.battry_lock);
        banner_ads=findViewById(R.id.banner_ads);
        //AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        videosList.clear();

        listener=this;
        sp=new SharePreferencess(this);
        name=getIntent().getStringExtra("name");
        title.setText(name);
        new getVideoData(name,this,MediaStore.Video.Media.DISPLAY_NAME).execute();


        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(SpecificVideoActivity.this, navigation_btn);
                popup.getMenuInflater().inflate(R.menu.main_manu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.name:
                                new getVideoData(name,listener,MediaStore.Video.Media.DISPLAY_NAME).execute();
                                break;
                            case R.id.date:
                                new getVideoData(name,listener,MediaStore.Video.Media.DATE_ADDED).execute();
                                break;
                            case R.id.size:
                                new getVideoData(name,listener,MediaStore.Video.Media.SIZE).execute();
                                break;
                            case R.id.as_duration:
                                new getVideoData(name,listener,MediaStore.Video.Media.DURATION).execute();
                                break;
                            case R.id.ascending:
                                new getVideoData(name,listener,MediaStore.Video.Media.DISPLAY_NAME).execute();
                                break;
                            case R.id.refersh_btn:
                                new getVideoData(name,listener,MediaStore.Video.Media.DISPLAY_NAME).execute();
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

        lock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LockScreenActivity.class));
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

        grid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getChangeLayout())
                {
                    grid_btn.setImageResource(R.drawable.grid_2);
                    sp.setChangeLayout(false);
                    new getVideoData(name,listener,MediaStore.Video.Media.DISPLAY_NAME).execute();
                }
                else
                {
                    grid_btn.setImageResource(R.drawable.ic_grid_1);
                    sp.setChangeLayout(true);
                    new getVideoData(name,listener,MediaStore.Video.Media.DISPLAY_NAME).execute();
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
        battry_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class getVideoData extends AsyncTask<Void,Void,Void>
    {
        String name;
        OnItemRestoreListener onItemRestoreListener;
        String oredrby;
        public getVideoData(String name, OnItemRestoreListener onItemRestoreListener, String oredrby) {
            this.name = name;
            this.onItemRestoreListener = onItemRestoreListener;
            this.oredrby = oredrby;
            videosList.clear();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection2,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME+" =?",new String[]{name},MediaStore.Video.Media.DATE_ADDED);
            ArrayList<String> imagesTEMP = new ArrayList<>(cursor.getCount());
            HashSet<String> albumSet = new HashSet<>();
            File file;
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return null;
                    }
                    @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
                    file = new File(path);
                    if (file.exists() && !albumSet.contains(path)) {
                        imagesTEMP.add(path);
                        albumSet.add(path);

                    }
                } while (cursor.moveToPrevious());
            }
            cursor.close();
            if (imagesTEMP == null) {

                imagesTEMP = new ArrayList<>();
            }
            videosList.clear();

            for(int i=0;i<imagesTEMP.size();i++)
            {
                File file1=new File(imagesTEMP.get(i));
                String uri=file1.getAbsolutePath();
                String title=file1.getName();
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(SpecificVideoActivity.this, Uri.fromFile(file1));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    String time_du = Utils.timeConversion(timeInMillisec);
                    VideoModel modal=new VideoModel();
                    modal.setVideoUri(Uri.parse(uri));
                    modal.setVideoTitle(title);
                    modal.setVideoDuration(time_du);
                    modal.setGetViewType(1);
                    videosList.add(modal);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            int size=videosList.size();
            int mod=size/5;
            for(int i=1;i<=mod;i++)
            {
                VideoModel videoModel  = new VideoModel();
                videoModel.setGetViewType(2);
                videosList.add(5*i,videoModel);
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
                GridLayoutManager manager=new GridLayoutManager(SpecificVideoActivity.this,2);
                recycle_view.setLayoutManager(manager);
                allVideoAdapter=new GridAllVideoAdapter(SpecificVideoActivity.this,videosList,onItemRestoreListener);
                GridNativeAdAdapter adAdapter= GridNativeAdAdapter.Builder.with("YOUR_PLACEMENT_ID",allVideoAdapter,SpecificVideoActivity.this).enableSpanRow(manager).build();
                recycle_view.setAdapter(adAdapter);
            }
            else
            {
                recycle_view.setLayoutManager(new LinearLayoutManager(SpecificVideoActivity.this));
                adapter=new AllVideoAdapter(SpecificVideoActivity.this,videosList,onItemRestoreListener);
               // BannerAdAdapter adAdapter= BannerAdAdapter.Builder.with("YOUR_PLACEMENT_ID",adapter,SpecificVideoActivity.this).build();
                recycle_view.setAdapter(adapter);
            }
        }


    }


    @Override
    public void OnClick(int postion) {
        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
        intent.putExtra("pos", postion);
        startActivity(intent);
    }

    @Override
    public void OnItemClick(int pos, final File file, RelativeLayout layout) {
        PopupMenu popup = new PopupMenu(SpecificVideoActivity.this, layout);
        popup.getMenuInflater().inflate(R.menu.specific_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.lock_btn:
                        boolean status= Utils.lockFile(file,SpecificVideoActivity.this);
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

    public void DeleteVideo(final File file) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SpecificVideoActivity.this);
        View view = LayoutInflater.from(SpecificVideoActivity.this).inflate(R.layout.delete_file, null);
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

                    boolean status=Utils.moveToPrivate(file,file.getName(),SpecificVideoActivity.this,getResources().getString(R.string.recycle));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SpecificVideoActivity.this);
        View view = LayoutInflater.from(SpecificVideoActivity.this).inflate(R.layout.rename_file, null);
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
                    boolean status= Utils.renameFile(file,name+extension,SpecificVideoActivity.this);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(SpecificVideoActivity.this);
        View view = LayoutInflater.from(SpecificVideoActivity.this).inflate(R.layout.properties_file, null);
        TextView file_txt = view.findViewById(R.id.file_txt);
        TextView duration_txt = view.findViewById(R.id.duration_txt);
        TextView file_size_txt = view.findViewById(R.id.file_size_txt);
        TextView location_txt = view.findViewById(R.id.location_txt);
        TextView date_txt = view.findViewById(R.id.date_txt);
        RelativeLayout ok_btn = view.findViewById(R.id.ok_btn);
        file_txt.setText(file.getName());
        try {
            duration_txt.setText(Utils.timeConversion(lastcreat));
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

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String shareMessage = "Download app";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filter(String text) {

        ArrayList<VideoModel> filteredlist = new ArrayList<>();
        for (VideoModel item : videosList) {

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
