package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.FolderVideoadapter;
import com.ffapp.ffxx.ffplayers.comman.OnItemClickListener;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.model.FolderModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class FolderVideoActivity extends AppCompatActivity implements OnItemClickListener {
    private final String[] projection1 = new String[]{ MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA };
    private final String[] projection2 = new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};
    RecyclerView recycle_video;
    String[] listVideo;
    FolderVideoadapter folderVideoadapter;
    private List<String> bucketNames= new ArrayList<>();
    private List<String> bitmapList=new ArrayList<>();
    ArrayList<FolderModel> list;
    boolean checked;
    private String link="";
    SharePreferencess sp;
    File pro;
    ArrayList<String> listvideo=new ArrayList<>();
    RelativeLayout accending_btn;
    ImageView navi_btn;
    OnItemClickListener onItemClickListener;
    SwipeRefreshLayout swip_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_video);
        recycle_video=findViewById(R.id.recycle_video);
        accending_btn=findViewById(R.id.accending_btn);
        swip_btn=findViewById(R.id.swip_btn);
        navi_btn=findViewById(R.id.navi_btn);
        sp=new SharePreferencess(this);
        this.onItemClickListener=this;
        list=new ArrayList<>();
        list.clear();
        new loadData(MediaStore.Video.Media.DISPLAY_NAME,this).execute(MediaStore.Video.Media.DISPLAY_NAME);
        recycle_video.setNestedScrollingEnabled(false);
        recycle_video.setLayoutManager(new LinearLayoutManager(this));

        accending_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(FolderVideoActivity.this, accending_btn);
                popup.getMenuInflater().inflate(R.menu.sort_refersh, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.name:
                                  runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {

                                          new loadData(MediaStore.Video.Media.DISPLAY_NAME,onItemClickListener).execute(MediaStore.Video.Media.DISPLAY_NAME);

                                      }
                                  });
                                break;
                            case R.id.date:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new loadData(MediaStore.Video.Media.DATE_ADDED,onItemClickListener).execute(MediaStore.Video.Media.DATE_ADDED);

                                    }
                                });
                                break;
                            case R.id.size:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new loadData(MediaStore.Video.Media.SIZE,onItemClickListener).execute(MediaStore.Video.Media.SIZE);

                                    }
                                });
                                break;
                            case R.id.as_duration:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new loadData(MediaStore.Video.Media.DURATION,onItemClickListener).execute(MediaStore.Video.Media.DURATION);

                                    }
                                });
                                break;
                            case R.id.ascending:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new loadData(MediaStore.Video.Media.BUCKET_DISPLAY_NAME,onItemClickListener).execute(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                                    }
                                });
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        swip_btn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swip_btn.setRefreshing(false);
                new loadData(MediaStore.Video.Media.DISPLAY_NAME,onItemClickListener).execute(MediaStore.Video.Media.DISPLAY_NAME);
            }
        });
}

public class loadData extends AsyncTask<String, Void, Void>
{
    OnItemClickListener listener;
    String orderby;

    public loadData(String orderBy,OnItemClickListener listener) {
        this.orderby=orderBy;
        this.listener=listener;
        list.clear();

    }
    @Override
    protected Void doInBackground(String... strings) {
        Cursor cursor = getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection1,
                        null, null, orderby);
        ArrayList<String> bucketNamesTEMP = new ArrayList<>(cursor.getCount());
        ArrayList<String> bitmapListTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return null;
                }
                @SuppressLint("Range") String album = cursor.getString(cursor.getColumnIndex(projection1[0]));
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(projection1[1]));
                pro = new File(image);
                if (pro.exists() && !albumSet.contains(album)) {
                    bucketNamesTEMP.add(album);
                    bitmapListTEMP.add(image);
                    albumSet.add(album);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        if (bucketNamesTEMP == null) {
            bucketNames = new ArrayList<>();
        }
        bucketNames.clear();
        bitmapList.clear();

        for(int i=0;i<bitmapListTEMP.size();i++)
        {
            String str=bitmapListTEMP.get(i);
            String name=bucketNamesTEMP.get(i);
            FolderModel model=new FolderModel();
            model.setName(name);
            model.setPath(str);
            model.setAlvideopath(bitmapListTEMP);
            list.add(model);
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
        folderVideoadapter=new FolderVideoadapter(FolderVideoActivity.this,list,listener);
        recycle_video.setAdapter(folderVideoadapter);
    }
}


    @Override
    public void OnclickItem(int pos) {

    }

    @Override
    public void OnNevigationMenuItem(final int pos, String name, RelativeLayout relativeLayout) {
         getVideos(name,getApplicationContext());
        PopupMenu popup = new PopupMenu(FolderVideoActivity.this, relativeLayout);
        popup.getMenuInflater().inflate(R.menu.folder_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.del_btn:
                        DeleteVideo(listvideo,pos);
                        break;
                    case R.id.proper_btn:
                        PropertiesBox(listvideo);
                        break;
                    case R.id.filter_btn:
                        String folderPath1 = "";
                        if (listvideo != null && listvideo.size() != 0) {
                            folderPath1 = listvideo.get(0).substring(0, listvideo.get(0).lastIndexOf("/"));

                        }
                        Intent intent = new Intent(getApplicationContext(), FilterDuplicateActivity.class);
                        intent.putExtra("Folder", folderPath1);
                        startActivity(intent);
                        break;
                    case R.id.share_btn:
                        shareVideo(listvideo);
                        break;
                    case R.id.lock_btn:
                        boolean status=Utils.lockFolder(listvideo,FolderVideoActivity.this);
                         if(status)
                         {
                             ArrayList<String> paths = sp.getPrivateVideoPath();
                             if (paths == null) {
                                 paths = new ArrayList<>();
                             }
                             paths.addAll(listvideo);
                             sp.updatePrivateVideoPath(paths);
                         }
                         else
                         {
                             Toast.makeText(getApplicationContext(), "Opps! Error occurred", Toast.LENGTH_SHORT).show();
                         }
                        break;
                    case R.id.rename_btn:
                        Rename(listvideo);
                        break;

                }
                return false;
            }
        });
        popup.show();
    }

    public void DeleteVideo(final ArrayList<String> lideolist, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FolderVideoActivity.this);
        View view = LayoutInflater.from(FolderVideoActivity.this).inflate(R.layout.delete_file, null);
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
                    for (int i = lideolist.size()-1; i >= 0; i--){
                        File file = new File(lideolist.get(i));
                        boolean status=Utils.moveToPrivate(file,file.getName(),FolderVideoActivity.this,getResources().getString(R.string.recycle));
                        if (status){
                            sp.setRecoverVideoPath(file.getPath());
                            lideolist.remove(i);
                        }
                    }

                    /*for(int i=0;i<lideolist.size();i++)
                    {
                        File file = new File(lideolist.get(i));
                        Log.e("Check_list", "onClick: " + file.getAbsolutePath());
                        boolean status=Utils.moveToPrivate(file,file.getName(),FolderVideoActivity.this,getResources().getString(R.string.recycle));
                        if (status) {
                            sp.setRecoverVideoPath(file.getPath());
                            lideolist.remove(i);
                        } else {
                            Log.e("", "onClick: " + "Error");
                        }
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(),"click check box",Toast.LENGTH_SHORT).show();
                }list.remove(position);
            }
        });

    }

    public void Rename(ArrayList<String> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FolderVideoActivity.this);
        View view = LayoutInflater.from(FolderVideoActivity.this).inflate(R.layout.rename_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        final EditText rename_txt = view.findViewById(R.id.rename_txt);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String renameFolderPath = "";
        if (listvideo != null && listvideo.size() != 0) {
            renameFolderPath = listvideo.get(0).substring(0, listvideo.get(0).lastIndexOf("/"));
            Log.e("Toast", "onMenuItemClick: Folder Path:  " + renameFolderPath);
        }
        final File folder = new File(renameFolderPath);
        String folderName = folder.getName();
        rename_txt.setText(folderName);

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


                    boolean status= Utils.renameFile(folder,name,FolderVideoActivity.this);

                    if(status)
                    {
                        folderVideoadapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


    }
    public void PropertiesBox(ArrayList<String> list) {
        String folderPath = "";
        if (list != null && list.size() != 0) {
            folderPath = list.get(0).substring(0, list.get(0).lastIndexOf("/"));

        }
        long fileSize = 0;
        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            fileSize = fileSize + file.length();

        }
        File file=new File(folderPath);
        String size = Utils.formatFileSize(fileSize);
        long lastcreat = file.lastModified();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        AlertDialog.Builder builder = new AlertDialog.Builder(FolderVideoActivity.this);
        View view = LayoutInflater.from(FolderVideoActivity.this).inflate(R.layout.properties_file, null);
        TextView file_txt = view.findViewById(R.id.file_txt);
        TextView duration_txt = view.findViewById(R.id.duration_txt);
        TextView file_size_txt = view.findViewById(R.id.file_size_txt);
        TextView location_txt = view.findViewById(R.id.location_txt);
        TextView date_txt = view.findViewById(R.id.date_txt);
        RelativeLayout ok_btn = view.findViewById(R.id.ok_btn);
        file_txt.setText(file.getName());
        duration_txt.setText(""+listvideo.size());


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

    public void shareVideo(ArrayList<String> list)
    {
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sharingIntent.setType("*/*");
            ArrayList<Uri> files = new ArrayList<Uri>();
            for(String data:list)
            {
                File file=new File(data);
                Uri uri;
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(FolderVideoActivity.this, getPackageName()+".provider", file);
                files.add(uri);
            }
            sharingIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            startActivity(sharingIntent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void getVideos(String bucket, Context context) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection2,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{bucket}, MediaStore.Video.Media.DATE_ADDED);
        ArrayList<String> imagesTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
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
        listvideo.clear();
        listvideo.addAll(imagesTEMP);
    }
}
