package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.SpecificVideoActivity;
import com.ffapp.ffxx.ffplayers.comman.OnItemClickListener;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.model.FolderModel;
import com.pesonal.adsdk.AppManage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;


import static com.pesonal.adsdk.AppManage.ADMOB_N;


public class FolderVideoadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<FolderModel> list;
    OnItemClickListener onItemClickListener;
    SharePreferencess sp;
    public static ArrayList<String> videosList = new ArrayList<>();
    private final String[] projection2 = new String[]{MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA};
    public FolderVideoadapter(Context context, ArrayList<FolderModel> list, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
        inflater = LayoutInflater.from(context);
        sp = new SharePreferencess(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 1: {
                View view = inflater.inflate(R.layout.folder_video_list, parent, false);
                viewHolder = new MyHolder(view);
                break;
            }
            case 2: {
                View adview = inflater.inflate(R.layout.banner_video_list, parent, false);
                viewHolder = new AdViewBanner(adview);
                break;
            }
        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        FolderModel model = list.get(holder.getAdapterPosition());
        switch (holder.getItemViewType()) {
            case 1:
                final MyHolder holder1 = (MyHolder) holder;
                holder1.folder_nm.setText(model.getName());
                getVideos(model.getName(), context);
                holder1.videoCount.setText(videosList.size() + "Video " + "(S)");
                if (sp.getNewTag(model.getName()) == videosList.size()) {
                    holder1.tag_lay.setVisibility(View.GONE);
                } else {
                    holder1.tag_lay.setVisibility(View.VISIBLE);

                }
                holder1.next_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FolderModel model = list.get(position);
                        Intent intent = new Intent(context, SpecificVideoActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        notifyDataSetChanged();
                    }
                });
                holder1.navigation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FolderModel model = list.get(position);
                        onItemClickListener.OnNevigationMenuItem(position, model.getName(), holder1.navigation);
                    }
                });
                break;
            case 2:
                 Log.e("Log","Ads_Count");
                AdViewBanner holderds= (AdViewBanner) holder;
                AppManage.getInstance(context).showNative((ViewGroup) holderds.banner_ads, ADMOB_N, context);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + holder.getItemViewType());
        }


    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView folder_nm, videoCount, tag_lay;
        RelativeLayout next_video, navigation,ad_banerr;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            folder_nm = itemView.findViewById(R.id.folder_nm);
            videoCount = itemView.findViewById(R.id.videoCount);
            next_video = itemView.findViewById(R.id.next_video);
            navigation = itemView.findViewById(R.id.navigation);
            tag_lay = itemView.findViewById(R.id.tag_lay);
            ad_banerr=itemView.findViewById(R.id.ad_banerr);
        }
    }

    public class AdViewBanner extends RecyclerView.ViewHolder {
        RelativeLayout banner_ads;
        public AdViewBanner(@NonNull View itemView) {
            super(itemView);
            banner_ads=itemView.findViewById(R.id.banner_ads);
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
                String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
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
        videosList.clear();
        videosList.addAll(imagesTEMP);
    }
}
