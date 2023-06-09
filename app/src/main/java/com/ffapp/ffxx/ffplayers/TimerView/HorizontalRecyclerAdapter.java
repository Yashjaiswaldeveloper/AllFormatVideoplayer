package com.ffapp.ffxx.ffplayers.TimerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;
import com.ffapp.ffxx.ffplayers.player.VideoPlayActivity;

import java.io.File;
import java.util.ArrayList;
public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<HorizontalRecyclerAdapter.MyHolder> {
    ArrayList<TimelineObject> list;
    ArrayList<VideoModel> videoModels=new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public HorizontalRecyclerAdapter(ArrayList<TimelineObject> list, Context context) {
        this.list = list;
        this.context=context;
        inflater=LayoutInflater.from(context);
        videoModels.clear();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view=inflater.inflate(R.layout.timelinevideo,parent,false);
      MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        Glide.with(context)
                .load("file://"+list.get(position).getImageUrl())
                .apply(new RequestOptions().centerCrop())
                .into(holder.iv_horizontal_card_image);
        File file=new File(list.get(position).getImageUrl());

        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(context, Uri.fromFile(file));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);
            retriever.release();
            String time_du = Utils.timeConversion(timeInMillisec);
            VideoModel modal = new VideoModel();
            modal.setVideoUri(Uri.parse(file.getAbsolutePath()));
            modal.setVideoTitle(file.getName());
            modal.setVideoDuration(time_du);
            videoModels.add(modal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TimeLineConfig.getListener() != null) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.videoArrayList=videoModels;
                    Intent intent = new Intent(context, VideoPlayActivity.class);
                    intent.putExtra("pos", position);
                    context.startActivity(intent);

                    TimeLineConfig.getListener().onTimelineObjectClicked(list.get(position));
                }
            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TimeLineConfig.getListener().onTimelineObjectLongClicked(list.get(position));
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_horizontal_card_image;
        CardView cardView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            iv_horizontal_card_image=itemView.findViewById(R.id.iv_horizontal_card_image);
            cardView=itemView.findViewById(R.id.timeline_obj_cardview);
        }
    }
}
