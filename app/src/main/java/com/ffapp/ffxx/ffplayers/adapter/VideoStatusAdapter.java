package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.VideoStatusActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoStatusAdapter extends RecyclerView.Adapter<VideoStatusAdapter.MyHolder> {
    ArrayList<String> list;
    Context context;
    LayoutInflater inflater;

    public VideoStatusAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.status_image,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final File hiddenpath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/");
        Glide.with(context).load(hiddenpath.getAbsolutePath()+"/"+list.get(position)).into(holder.image_view);
        File file=new File(hiddenpath.getAbsoluteFile()+"/"+list.get(position));


        holder.download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=hiddenpath.getAbsolutePath()+"/"+list.get(position);
                Intent intent=new Intent(context, VideoStatusActivity.class);
                intent.putExtra("vid",url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        RelativeLayout download_btn;
        TextView title;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image_view=itemView.findViewById(R.id.image_view);
            download_btn=itemView.findViewById(R.id.relative_lay);

        }
    }

    /*private void saveVideoToInternalStorage (String filePath) {

        File newfile;

        try {
            SimpleDateFormat sd = new SimpleDateFormat("yy mm hh");
            String date = sd.format(new Date());
            String name = "Status_" + date + ".mp4";

            File currentFile = new File(filePath);
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "StatusSaver";
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
                context.startActivity(new Intent(context, VideoStatusSaveActivity.class));
                Log.v("", "Video file saved successfully.");

            }else{
                Log.v("", "Video saving failed. Source file missing.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/



}

