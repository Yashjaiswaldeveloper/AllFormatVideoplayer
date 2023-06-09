package com.ffapp.ffxx.ffplayers.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.VideoStatusAdapter;

import java.io.File;
import java.util.ArrayList;


public class VideoFragment extends Fragment {
  View view;
  RecyclerView recycle_view;
  TextView data_not;
    private ArrayList<String> videolist=new ArrayList<>();
    private VideoStatusAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_video, container, false);
        recycle_view=view.findViewById(R.id.recycle_view);
        data_not=view.findViewById(R.id.data_not);
        recycle_view.setLayoutManager(new GridLayoutManager(getActivity(),2));
        data();
        return view;
    }
    public void data()
    {
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/");
        String[] filename=file.list();
        if(filename!=null)
        {
            for(String img:filename)
            {
                if(img.endsWith(".mp4")||img.endsWith(".avi")||img.endsWith(".mkv"))
                {
                    videolist.add(img);
                }
            }
        }
        else
        {
            data_not.setVisibility(View.VISIBLE);
        }


        adapter=new VideoStatusAdapter(videolist,getActivity());
        recycle_view.setAdapter(adapter);
    }

}
