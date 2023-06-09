package com.ffapp.ffxx.ffplayers.TimerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ffapp.ffxx.ffplayers.R;

import java.util.ArrayList;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.MyHolder> {
    ArrayList<String> list;
    Context context;
    LayoutInflater inflater;
    HorizontalRecyclerAdapter adapter;
    public VerticalRecyclerAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater=LayoutInflater.from(context);

    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.vertical_timeline_layout,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv_timeline_time.setText(TimeLineConfig.headerList.get(position));
        adapter=new HorizontalRecyclerAdapter(TimeLineConfig.timelineObjMap.get(TimeLineConfig.headerList.get(position)),context);
        holder.timeline.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return TimeLineConfig.headerList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        RecyclerView timeline;
        TextView tv_timeline_time;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            timeline=itemView.findViewById(R.id.timeline);
            tv_timeline_time=itemView.findViewById(R.id.tv_timeline_time);
            tv_timeline_time.setTextColor(Color.parseColor(TimeLineConfig.getTimelineHeaderTextColour()));
            tv_timeline_time.setTextSize(TimeLineConfig.getTimelineHeaderSize());
           // timelineindicator_line.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineIndicatorLineColour()));
           // timelineindicator_container.setBackgroundColor(Color.parseColor(TimeLineConfig.getTimelineIndicatorBackgroundColour()));

           // LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(context);
            //timeline.setLayoutManager(recyclerViewLayoutManager);

            timeline.setLayoutManager(new GridLayoutManager(context,3));
            LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            timeline.setLayoutManager(horizontalLinearLayoutManager);
        }
    }
    public void notifyDataSetChangedToHorizontalView() {
        if (adapter == null){
            return;
        }
        adapter.notifyDataSetChanged();
    }
}
