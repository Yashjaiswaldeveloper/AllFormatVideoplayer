package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class SpecificVideoAdapter extends RecyclerView.Adapter<SpecificVideoAdapter.MyHolder> {
    Context context;
    ArrayList<VideoModel> list;
    LayoutInflater inflater;
    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener
    {
        void OnClickVideo(int pos);
    }
     public SpecificVideoAdapter(Context context, ArrayList<VideoModel> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
         try {
             onItemClickListener= (OnItemClickListener) context;
         }
         catch (ClassCastException e)
         {
             e.printStackTrace();
         }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.specific_video_list,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        VideoModel model=list.get(position);
        Glide.with(context).load("file://"+model.getVideoUri()).apply(new RequestOptions().override(153,160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into(holder.thumb);
        File file=new File(String.valueOf(model.getVideoUri()));
        String size=Utils.getFileSize(file);
        long lastcreat = file.lastModified();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        holder.video_name.setText(file.getName());
        holder.video_size.setText(""+size);
        holder.video_date.setText("Modified:"+strDate);
        holder.time_duration.setText(""+model.getVideoDuration());
        holder.next_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnClickVideo(position);
                try {
                    Utils.videoArrayList=list;

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        RelativeLayout next_video;
        TextView video_name,video_date,video_size,time_duration;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumb=itemView.findViewById(R.id.thumb);
            video_name=itemView.findViewById(R.id.video_name);
            video_date=itemView.findViewById(R.id.video_date);
            video_size=itemView.findViewById(R.id.video_size);
            time_duration=itemView.findViewById(R.id.time_duration);
            next_video=itemView.findViewById(R.id.next_video);
        }


    }


}
