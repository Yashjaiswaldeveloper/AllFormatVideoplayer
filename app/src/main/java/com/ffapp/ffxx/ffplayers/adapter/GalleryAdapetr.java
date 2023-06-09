package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.ffapp.ffxx.ffplayers.activity.ImageDisplay;
import com.ffapp.ffxx.ffplayers.model.ImageFolder;

import java.util.ArrayList;

public class GalleryAdapetr extends RecyclerView.Adapter<GalleryAdapetr.MyHolder> {
    ArrayList<ImageFolder> list;
    Context context;
    LayoutInflater inflater;
    String folderSizeString;
    int count;
    public GalleryAdapetr(ArrayList<ImageFolder> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);

    }
    @NonNull
    @Override
    public GalleryAdapetr.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.image_folder,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapetr.MyHolder holder, final int position) {
        ImageFolder data=list.get(position);
        Glide.with(context)
                .load(data.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.image_view);
        String text = ""+data.getFolderName();
         folderSizeString=""+data.getNumberOfPics();
        holder.count_btn.setText(folderSizeString);
        holder.folder_name.setText(text);

        holder.next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFolder data=list.get(position);
                count=data.getNumberOfPics();
                Intent move = new Intent(context, ImageDisplay.class);
                move.putExtra("folderPath",data.getPath());
                move.putExtra("folderName",data.getFolderName());
                move.putExtra("count",count);
                context.startActivity(move);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        TextView folder_name,count_btn;
        RelativeLayout next_btn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image_view=itemView.findViewById(R.id.image_view);
            folder_name=itemView.findViewById(R.id.folder_name);
            count_btn=itemView.findViewById(R.id.count_btn);
            next_btn=itemView.findViewById(R.id.next_btn);

        }
    }
}
