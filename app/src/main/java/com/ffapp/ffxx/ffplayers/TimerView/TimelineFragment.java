package com.ffapp.ffxx.ffplayers.TimerView;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ffapp.ffxx.ffplayers.R;

import java.util.ArrayList;


public class TimelineFragment extends Fragment {
    View view;
    RecyclerView recycle_view;
    LinearLayoutManager manager;
    LinearLayoutManager managers;
    VerticalRecyclerAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_timeline, container, false);



        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycle_view=view.findViewById(R.id.recycle_view);
        manager=new LinearLayoutManager(getContext());
        recycle_view.setLayoutManager(manager);

        adapter=new VerticalRecyclerAdapter(null,getActivity());
        managers=new LinearLayoutManager(getActivity());
        recycle_view.setLayoutManager(managers);
        recycle_view.setAdapter(adapter);


    }
    public void addOnClickListener(TimelineObjectClickListener listener){
        TimeLineConfig.addOnClickListener(listener);
    }
    public void setImageLoadEngine(ImageLoadingEngine engine){
        TimeLineConfig.setImageLoadEngine(engine);
    }
    public void setData(ArrayList<TimelineObject> dataList, TimelineGroupType type ){
        TimeLineConfig.setData(dataList, type);
    }





}
