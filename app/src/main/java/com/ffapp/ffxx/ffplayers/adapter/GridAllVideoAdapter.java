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
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import static com.pesonal.adsdk.AppManage.ADMOB_N;


public class GridAllVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<VideoModel> list;
    LayoutInflater inflater;
    public OnItemRestoreListener onItemClickListener;
    SharePreferencess sp;

    public GridAllVideoAdapter(Context context, ArrayList<VideoModel> list, OnItemRestoreListener onItemClickListener) {
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
                View view = inflater.inflate(R.layout.gridviewlist, parent, false);
                holder = new MyHolder(view);
            }

            break;
            case 2: {
                View view = inflater.inflate(R.layout.grid_ads_layout, parent, false);
                holder = new AdBanner(view);
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

            final MyHolder holder1 = (MyHolder) holder;
            final File file = new File(String.valueOf(videodata.getVideoUri()));
            Glide.with(context).load(file.getAbsolutePath()).apply(new RequestOptions().override(153, 160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into(holder1.image_view);
            holder1.title_txt.setText(videodata.getVideoTitle());
            holder1.duration.setText(videodata.getVideoDuration());
            Log.e("Name_sh", "" + SpecificVideoActivity.name);
            if (sp.getTagVideo(file.getPath())) {
                int pos = position + 1;
                sp.setNewTag(SpecificVideoActivity.name, pos);
                holder1.tag_lay.setVisibility(View.GONE);
            } else {
                holder1.tag_lay.setVisibility(View.VISIBLE);
            }
            holder1.title.setOnClickListener(new View.OnClickListener() {
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
                AdBanner adBanner= (AdBanner) holder;
               // AppManage.getInstance(context).showNative((ViewGroup) adBanner.grid_layout , ADMOB_N, context);
                //AppManage.getInstance(context).show_NATIVE((ViewGroup) adBanner.grid_layout, ADMOB_N1, FACEBOOK_N1,context);
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
        ImageView image_view;
        RelativeLayout title, navi_btn;
        TextView title_txt, duration;
        TextView tag_lay;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            navi_btn = itemView.findViewById(R.id.navi_btn);
            image_view = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
            title_txt = itemView.findViewById(R.id.title_txt);
            duration = itemView.findViewById(R.id.time_duration);
            tag_lay = itemView.findViewById(R.id.tag_lay);

        }
    }

    public class AdBanner extends RecyclerView.ViewHolder {
        RelativeLayout grid_layout;
        public AdBanner(@NonNull View itemView) {
            super(itemView);
            grid_layout=itemView.findViewById(R.id.grid_layout);
        }
    }
}
