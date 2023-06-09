package com.ffapp.ffxx.ffplayers.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ffapp.ffxx.ffplayers.R;

import java.io.File;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class DuplicateVideoAdapter extends RecyclerView.Adapter<DuplicateVideoAdapter.MyHolder> {
    ArrayList<String> list;
    Context context;
    LayoutInflater inflater;

    public DuplicateVideoAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=inflater.inflate(R.layout.duplicate_video,parent,false);
    MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        Glide.with(context).load("file://"+list.get(position)).apply(new RequestOptions().override(153,160).centerCrop().dontAnimate().skipMemoryCache(true)).transition(withCrossFade()).into( holder.image_view);

        holder.select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file=new File(list.get(position));
                DeleteVideo(context,position,file);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        ImageView select_image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image_view=itemView.findViewById(R.id.image_view);
            select_image=itemView.findViewById(R.id.select_image);

        }
    }

    public void DeleteVideo(final Context context, final int pos, final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.delete_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        RelativeLayout layout=view.findViewById(R.id.check_temp);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        layout.setVisibility(View.GONE);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

               boolean del=file.delete();
               if(del)
               {
                   list.remove(pos);
                   Toast.makeText(context,"Delete file Sucessfully!",Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(context,"!Opps can't file ",Toast.LENGTH_SHORT).show();
               }

            }
        });

    }
}
