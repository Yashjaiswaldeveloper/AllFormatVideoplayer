package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.RecycleBinActivity;
import com.ffapp.ffxx.ffplayers.comman.OnItemRestoreListener;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RecycleBinAdapter extends RecyclerView.Adapter<RecycleBinAdapter.MyHolder> {
    Context context;
    ArrayList<VideoModel> list;
    LayoutInflater inflater;
    public OnItemRestoreListener onItemClickListener;

    public RecycleBinAdapter(Context context, ArrayList<VideoModel> list, OnItemRestoreListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener=onItemClickListener;
        inflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.specific_video_list,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }
    public void filterList(ArrayList<VideoModel> filterllist) {

        list = filterllist;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        VideoModel videodata=list.get(position);
        final File file=new File(String.valueOf(videodata.getVideoUri()));
        Glide.with(context).load(file.getAbsolutePath()).apply(new RequestOptions().override(153,160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into(holder.thumb);
        holder.video_name.setText(videodata.getVideoTitle());
        holder.time_duration.setText(videodata.getVideoDuration());
        String size= Utils.getFileSize(file);
        holder.video_size.setText(""+size);
        long lastcreat = file.lastModified();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        holder.video_date.setText("Modified:"+strDate);

       holder.next_video.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onItemClickListener.OnClick(position);
           }
       });

        holder.navi_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(position,file,holder.navi_btn);
            }
        });
        if(Utils.AllSelect==true)
        {
            holder.check_btn.setVisibility(View.VISIBLE);
            holder.navi_img.setVisibility(View.GONE);
            holder.check_btn.setChecked(true);
            Utils.list_select.addAll(list);
            RecycleBinActivity.select_count.setText(""+Utils.list_select.size()+"/Selected");
        }
        else
        {
            holder.check_btn.setVisibility(View.GONE);
            holder.navi_img.setVisibility(View.VISIBLE);
        }
        holder.check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status=holder.check_btn.isChecked();
                if(status)
                {
                    Utils.list_select.add(list.get(position));
                    RecycleBinActivity.select_count.setText(""+Utils.list_select.size()+"/Selected");

                }
                else
                {
                    Utils.list_select.remove(position);
                    RecycleBinActivity.select_count.setText(""+Utils.list_select.size()+"/Selected");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView thumb,navi_img;
        RelativeLayout next_video,navi_btn;
        TextView video_name,video_date,video_size,time_duration;
        CheckBox check_btn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumb=itemView.findViewById(R.id.thumb);
            video_name=itemView.findViewById(R.id.video_name);
            video_date=itemView.findViewById(R.id.video_date);
            video_size=itemView.findViewById(R.id.video_size);
            time_duration=itemView.findViewById(R.id.time_duration);
            next_video=itemView.findViewById(R.id.next_video);
            navi_btn=itemView.findViewById(R.id.navi_btn);
            check_btn=itemView.findViewById(R.id.check_btn);
            navi_img=itemView.findViewById(R.id.navi_img);
        }
    }
}
