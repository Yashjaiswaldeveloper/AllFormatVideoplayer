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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.RecycleBinAdapter;
import com.ffapp.ffxx.ffplayers.adapter.RecycleBinGridAdapter;
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



public class RecycleBinActivity extends AppCompatActivity implements OnItemRestoreListener {
  RecyclerView recycle_view;
  ArrayList<VideoModel> list=new ArrayList<>();
  RecycleBinAdapter adapter;
  RecycleBinGridAdapter recycleBinGridAdapter;
  SharePreferencess sp;
  OnItemRestoreListener listener;
  RelativeLayout navigation_btn,select_temp,main_menu,back_btn;
  ImageView grid_btn;
  ImageView search_btn;
  SearchView search_view;
  public static TextView select_count;
  TextView all_title;
  SwipeRefreshLayout swip_btn;
  RelativeLayout banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);
        recycle_view=findViewById(R.id.recycle_view);
        grid_btn=findViewById(R.id.grid_btn);
        search_btn=findViewById(R.id.search_btn);
        search_view=findViewById(R.id.search_view);
        swip_btn=findViewById(R.id.swip_btn);
        all_title=findViewById(R.id.all_title);
        back_btn=findViewById(R.id.back_btn);
        //banner_ads=findViewById(R.id.banner_ads);
        sp=new SharePreferencess(this);
        list.clear();
        listener=this;
        //AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        new getRecycleBindata(listener).execute();
        navigation_btn=findViewById(R.id.unlock_navigation);
        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(RecycleBinActivity.this, navigation_btn);
                popup.getMenuInflater().inflate(R.menu.recycle_header, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.refersh_btn:
                                new getRecycleBindata(listener).execute();
                                break;
                            case R.id.select_all:
                                startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                                /*Utils.list_select.clear();
                                main_menu.setVisibility(View.GONE);
                                select_temp.setVisibility(View.VISIBLE);
                                Utils.AllSelect=true;
                                new getRecycleBindata(listener).execute();*/
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
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
                    new getRecycleBindata(listener).execute();
                }
                else
                {
                    grid_btn.setImageResource(R.drawable.ic_grid_1);
                    sp.setChangeLayout(true);
                    new getRecycleBindata(listener).execute();
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
                new getRecycleBindata(listener).execute();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class getRecycleBindata extends AsyncTask<Void,Void,Void>
    {
        OnItemRestoreListener listener;

        public getRecycleBindata(OnItemRestoreListener listener) {
            this.listener = listener;
            list.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File directory = getApplicationContext().getExternalFilesDir(getResources().getString(R.string.recycle));
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String title = file.getName();
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(RecycleBinActivity.this, Uri.fromFile(file));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long timeInMillisec = Long.parseLong(time);
                    retriever.release();
                    String time_du = Utils.timeConversion(timeInMillisec);
                    VideoModel modal = new VideoModel();
                    modal.setVideoUri(Uri.parse(file.getAbsolutePath()));
                    modal.setVideoTitle(title);
                    modal.setVideoDuration(time_du);
                    list.add(modal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(sp.getChangeLayout())
            {
                recycle_view.setLayoutManager(new GridLayoutManager(RecycleBinActivity.this,2));
                recycleBinGridAdapter=new RecycleBinGridAdapter(RecycleBinActivity.this,list,listener);
                recycle_view.setAdapter(recycleBinGridAdapter);
            }
            else
            {
                recycle_view.setLayoutManager(new LinearLayoutManager(RecycleBinActivity.this));
                adapter=new RecycleBinAdapter(RecycleBinActivity.this,list,listener);
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
        PopupMenu popup = new PopupMenu(RecycleBinActivity.this, layout);
        popup.getMenuInflater().inflate(R.menu.recycle_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.restore_btn:
                        String reciverFile=file.getName();
                        ArrayList<String> list = sp.getRecoverVideoPath();
                        if(list!=null)
                        {
                            for(int i=0;i<list.size();i++)
                            {
                                File file1 = new File(list.get(i));
                                String videoName = file1.getName();
                               if(videoName.equals(reciverFile))
                               {
                                   boolean status=Utils.moveFromPrivate(file,videoName,RecycleBinActivity.this,list.get(i));
                                   if(status)
                                   {
                                       list.remove(i);
                                       sp.updateRecoverVideoPath(list);
                                       adapter.notifyDataSetChanged();
                                   }
                                   break;
                               }
                            }
                        }

                        break;
                    case R.id.share_btn:
                        shareVideo(file);
                        break;
                    case R.id.Delete:
                        DeleteVideo(file);
                        break;
                    case R.id.properties:
                        PropertiesBox(file);
                        break;


                }

                return true;
            }
        });
        popup.show();
    }

    public void PropertiesBox(File file) {
        String size = Utils.getFileSize(file);
        long lastcreat = file.lastModified();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        AlertDialog.Builder builder = new AlertDialog.Builder(RecycleBinActivity.this);
        View view = LayoutInflater.from(RecycleBinActivity.this).inflate(R.layout.properties_file, null);
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
    public void shareVideo(File file)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),getPackageName()+".provider",file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Video Title");
        startActivity(Intent.createChooser(sharingIntent, "Share Video!"));

    }
    public void DeleteVideo(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecycleBinActivity.this);
        View view = LayoutInflater.from(RecycleBinActivity.this).inflate(R.layout.delete_file, null);
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
                boolean result = file.delete();

                if (result) {
                    Toast.makeText(getApplicationContext(), "File deleted Sucessfully...", Toast.LENGTH_SHORT).show();
                    new getRecycleBindata(listener).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void filter(String text) {

        ArrayList<VideoModel> filteredlist = new ArrayList<>();
        for (VideoModel item : list) {

            if (item.getVideoTitle().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {

            if(sp.getChangeLayout())
            {
                recycleBinGridAdapter.filterList(filteredlist);
            }
            else
            {
                adapter.filterList(filteredlist);
            }

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
