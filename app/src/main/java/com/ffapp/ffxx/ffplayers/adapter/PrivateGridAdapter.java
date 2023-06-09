package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.player.VideoModel;

import java.io.File;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PrivateGridAdapter extends RecyclerView.Adapter<PrivateGridAdapter.MyHolder> {
    Context context;
    ArrayList<VideoModel> list;
    LayoutInflater inflater;
    public OnItemClickListener onItemClickListener;
    SharePreferencess sp;
    public interface OnItemClickListener
    {
        void OnClickVideo(int pos);
        void OnMenuNavigate(int pos, File file, RelativeLayout layout);
    }
    public PrivateGridAdapter(Context context, ArrayList<VideoModel> list) {
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
        sp=new SharePreferencess(context);
        try {
            onItemClickListener= (OnItemClickListener) context;
        }
        catch (ClassCastException e)
        {
            e.printStackTrace();
        }
    }
    public void filterList(ArrayList<VideoModel> filterllist) {

        list = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view=inflater.inflate(R.layout.gridviewlist,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        VideoModel videodata=list.get(position);
        final File file=new File(String.valueOf(videodata.getVideoUri()));
        Glide.with(context).load(file.getAbsolutePath()).apply(new RequestOptions().override(153,160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into(holder.imageView);
        holder.video_name.setText(videodata.getVideoTitle());
        holder.time_duration.setText(videodata.getVideoDuration());
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

        holder.navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnMenuNavigate(position,file,holder.navi_btn);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        RelativeLayout next_video,navi_btn;
        TextView video_name,video_date,video_size,time_duration;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
            video_name=itemView.findViewById(R.id.title_txt);
            time_duration=itemView.findViewById(R.id.duration);
            next_video=itemView.findViewById(R.id.title);
            navi_btn=itemView.findViewById(R.id.navi_btn);


        }
    }
}
