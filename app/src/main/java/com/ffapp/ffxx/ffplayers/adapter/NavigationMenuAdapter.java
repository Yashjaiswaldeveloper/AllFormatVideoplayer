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

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.ModelMenu;
import com.ffapp.ffxx.ffplayers.comman.NavigationClickListener;

import java.util.ArrayList;

public class NavigationMenuAdapter extends RecyclerView.Adapter<NavigationMenuAdapter.MyHolder> {
    Context context;
    ArrayList<ModelMenu> list;
    NavigationClickListener listener;

    public NavigationMenuAdapter(Context context, ArrayList<ModelMenu> list, NavigationClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NavigationMenuAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.navigation_file,parent,false);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NavigationMenuAdapter.MyHolder holder, final int position) {
        ModelMenu menu=list.get(position);
        holder.img_img.setImageResource(menu.getMenuIcon());
        holder.title_txt.setText(menu.getMenuText());
        holder.click_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView img_img;
        TextView title_txt;
        RelativeLayout click_btn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img_img=itemView.findViewById(R.id.img_img);
            title_txt=itemView.findViewById(R.id.title_txt);
            click_btn=itemView.findViewById(R.id.click_btn);
        }
    }
}
