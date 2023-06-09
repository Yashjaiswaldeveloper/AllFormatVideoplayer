package com.ffapp.ffxx.ffplayers.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.StatusImageActivity;
import com.ffapp.ffxx.ffplayers.activity.VideoStatusActivity;

import java.util.ArrayList;

public class DownloadVideoImageAdapter extends RecyclerView.Adapter<DownloadVideoImageAdapter.MyHolder> {
    ArrayList<String> list;
    Context context;
    LayoutInflater inflater;
    private AlertDialog alertDialog1;

    public DownloadVideoImageAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.save_video_image,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        Glide.with(context).load("file://" + list.get(position).trim()).into(holder.img_view);
        if(list.get(position).endsWith(".mp4"))
        {
            holder.play_video.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.play_video.setVisibility(View.GONE);
        }

        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list.get(position).endsWith(".mp4"))
                {
                    Intent intent=new Intent(context, VideoStatusActivity.class);
                    intent.putExtra("vid",list.get(position));
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(context, StatusImageActivity.class);
                    intent.putExtra("img",list.get(position));
                    intent.putExtra("save_img","save");
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView img_view,play_video;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img_view=itemView.findViewById(R.id.image_view);
            play_video=itemView.findViewById(R.id.play_video);
        }
    }

}
