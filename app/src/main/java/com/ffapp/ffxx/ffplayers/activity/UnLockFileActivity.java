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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.PrivateAdapter;
import com.ffapp.ffxx.ffplayers.adapter.PrivateGridAdapter;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;
import com.ffapp.ffxx.ffplayers.player.VideoPlayActivity;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class UnLockFileActivity extends AppCompatActivity implements PrivateAdapter.OnItemClickListener {
    RecyclerView recycle_video;
    ArrayList<VideoModel> list = new ArrayList<>();
    PrivateAdapter adapter;
    PrivateGridAdapter privateGridAdapter;
    RelativeLayout unlock_navigation;
    SharePreferencess sp;
    ImageView grid_btn;
    ImageView search_btn;
    SearchView search_view;
    TextView title_name;
    SwipeRefreshLayout swip_btn;
    RelativeLayout back_btn,native_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_lock_file);
        recycle_video = findViewById(R.id.recycle_video);
        unlock_navigation=findViewById(R.id.unlock_navigation);
        grid_btn=findViewById(R.id.grid_btn);
        search_btn=findViewById(R.id.search_btn);
        search_view=findViewById(R.id.search_view);
        title_name=findViewById(R.id.title_name);
        swip_btn=findViewById(R.id.swip_btn);
        back_btn=findViewById(R.id.back_btn);
        native_ads=findViewById(R.id.native_ads);
        sp=new SharePreferencess(this);
        list.clear();
        new  loadVideo().execute();
        //AppManage.getInstance(this).showNative((ViewGroup) native_ads, ADMOB_N, FACEBOOK_N,this);
        unlock_navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(UnLockFileActivity.this, unlock_navigation);
                popup.getMenuInflater().inflate(R.menu.private_video_header, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.refersh_btn:
                                new  loadVideo().execute();
                                break;
                            case R.id.share_btn:
                                shareApp();
                                break;
                            case R.id.change_pin_btn:
                                startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
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
                    new  loadVideo().execute();
                }
                else
                {
                    grid_btn.setImageResource(R.drawable.ic_grid_1);
                    sp.setChangeLayout(true);
                    new  loadVideo().execute();
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
                new  loadVideo().execute();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class loadVideo extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {

            File directory = getApplicationContext().getExternalFilesDir(getResources().getString(R.string.private_videos));

            File[] files = directory.listFiles();

            for (int i = 0; i < files.length; i++) {

                File file = files[i];
                String title = file.getName();
                try {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(UnLockFileActivity.this, Uri.fromFile(file));
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
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(sp.getChangeLayout())
            {
                recycle_video.setLayoutManager(new GridLayoutManager(UnLockFileActivity.this,2));
                privateGridAdapter = new PrivateGridAdapter(UnLockFileActivity.this, list);
                recycle_video.setAdapter(privateGridAdapter);
            }
            else
            {
                recycle_video.setLayoutManager(new LinearLayoutManager(UnLockFileActivity.this));
                adapter = new PrivateAdapter(UnLockFileActivity.this, list);
                recycle_video.setAdapter(adapter);
            }

        }
    }

    @Override
    public void OnClickVideo(int pos) {
        Intent intent = new Intent(getApplicationContext(), VideoPlayActivity.class);
        intent.putExtra("pos", pos);
        startActivity(intent);
    }

    @Override
    public void OnMenuNavigate(int pos, final File file, RelativeLayout layout) {
        PopupMenu popup = new PopupMenu(UnLockFileActivity.this, layout);
        popup.getMenuInflater().inflate(R.menu.private_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.unlock_btn:
                        Log.e("File",file.getAbsolutePath());
                        boolean status = Utils.unlockFile(file, UnLockFileActivity.this);
                        if (status) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
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
                privateGridAdapter.filterList(filteredlist);
            }
            else
            {
                adapter.filterList(filteredlist);
            }

        }
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

    public void PropertiesBox(File file) {
        String size = Utils.getFileSize(file);
        long lastcreat = file.lastModified();


        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        AlertDialog.Builder builder = new AlertDialog.Builder(UnLockFileActivity.this);
        View view = LayoutInflater.from(UnLockFileActivity.this).inflate(R.layout.properties_file, null);
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

    public void DeleteVideo(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UnLockFileActivity.this);
        View view = LayoutInflater.from(UnLockFileActivity.this).inflate(R.layout.delete_file, null);
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
                    new  loadVideo().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
