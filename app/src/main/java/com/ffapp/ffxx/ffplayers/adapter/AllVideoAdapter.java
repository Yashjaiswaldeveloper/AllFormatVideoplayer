package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.util.Log;
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
import com.ffapp.ffxx.ffplayers.activity.SpecificVideoActivity;
import com.ffapp.ffxx.ffplayers.comman.OnItemRestoreListener;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import static com.pesonal.adsdk.AppManage.ADMOB_B;
import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_NB;


public class AllVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<VideoModel> list;
    LayoutInflater inflater;
    public OnItemRestoreListener onItemClickListener;
    SharePreferencess sp;

    public AllVideoAdapter(Context context, ArrayList<VideoModel> list, OnItemRestoreListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        inflater = LayoutInflater.from(context);
        sp = new SharePreferencess(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 1: {
                View view = inflater.inflate(R.layout.specific_video_list, parent, false);
                holder = new MyHolder(view);
            }

            break;
            case 2: {
                View view = inflater.inflate(R.layout.banner_video_list, parent, false);
                holder = new AdsBanner(view);
            }
            break;
        }


        return holder;
    }

    public void filterList(ArrayList<VideoModel> filterllist) {

        list = filterllist;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        VideoModel videodata = list.get(position);

        switch (holder.getItemViewType()) {
            case 1:
               final MyHolder holder1= (MyHolder) holder;
                final File file = new File(String.valueOf(videodata.getVideoUri()));
                Glide.with(context).load(file.getAbsolutePath()).apply(new RequestOptions().override(153, 160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into(holder1.thumb);
                holder1.video_name.setText(videodata.getVideoTitle());
                holder1.time_duration.setText(videodata.getVideoDuration());
                String size = Utils.getFileSize(file);
                holder1.video_size.setText("" + size);
                long lastcreat = file.lastModified();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date now = new Date(lastcreat);
                String strDate = df.format(now);
                if (sp.getTagVideo(file.getPath())) {
                    int pos = position + 1;
                    sp.setNewTag(SpecificVideoActivity.name, pos);
                    holder1.tag_lay.setVisibility(View.GONE);
                } else {
                    holder1.tag_lay.setVisibility(View.VISIBLE);
                }
                holder1.video_date.setText("Modified:" + strDate);

                holder1.next_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp.setTageVideo(file.getPath(), true);
                        onItemClickListener.OnClick(position);
                        try {
                            Utils.videoArrayList = list;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                holder1.navi_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.OnItemClick(position, file, holder1.navi_btn);
                    }
                });

            break;
            case 2:
                Log.e("Log","Ads_Count");
                AdsBanner holderds= (AdsBanner) holder;
                break;
        }



    }

    @Override
    public int getItemViewType(int position) {

        return list.get(position).getGetViewType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        RelativeLayout next_video, navi_btn;
        TextView video_name, video_date, video_size, time_duration;
        TextView tag_lay;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            video_name = itemView.findViewById(R.id.video_name);
            video_date = itemView.findViewById(R.id.video_date);
            video_size = itemView.findViewById(R.id.video_size);
            time_duration = itemView.findViewById(R.id.time_duration);
            next_video = itemView.findViewById(R.id.next_video);
            navi_btn = itemView.findViewById(R.id.navi_btn);
            tag_lay = itemView.findViewById(R.id.tag_lay);
        }
    }

    public class AdsBanner extends RecyclerView.ViewHolder {
        RelativeLayout banner_ads;
        public AdsBanner(@NonNull View itemView) {
            super(itemView);
            banner_ads=itemView.findViewById(R.id.banner_ads);
        }
    }
}
