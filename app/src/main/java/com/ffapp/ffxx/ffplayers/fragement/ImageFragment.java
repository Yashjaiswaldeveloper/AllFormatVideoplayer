package com.ffapp.ffxx.ffplayers.fragement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.ImageStatusAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageFragment extends Fragment {
    View view;
    RecyclerView recycle_view;
    TextView data_not;
    ArrayList<String> imagelist=new ArrayList<>();
    private ImageStatusAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_image, container, false);
        recycle_view=view.findViewById(R.id.recycle_view);
        data_not=view.findViewById(R.id.data_not);
        imagelist.clear();
        data();
        recycle_view.setLayoutManager(new GridLayoutManager(getActivity(),3));
        return view;
    }

    public void data()
    {
        List<Integer> images=new ArrayList<>();
        File file=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/");
        String[] filename=file.list();
        if(filename!=null)
        {
            for(String img:filename)
            {
                if(img.endsWith(".jpg")||img.endsWith(".jpeg")||img.endsWith(".gif"))
                {
                    Log.e("Videos",img);
                    imagelist.add(img);
                }
            }

        }
        else
        {
            data_not.setVisibility(View.VISIBLE);

        }


        adapter=new ImageStatusAdapter(imagelist,getActivity());
        recycle_view.setAdapter(adapter);

    }

}
