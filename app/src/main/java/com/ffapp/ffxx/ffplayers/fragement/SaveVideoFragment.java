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
import com.ffapp.ffxx.ffplayers.adapter.DownloadVideoImageAdapter;

import java.io.File;
import java.util.ArrayList;


public class SaveVideoFragment extends Fragment {
    View view;
    ArrayList<String> list=new ArrayList<>();
    private DownloadVideoImageAdapter adapters;
    RecyclerView grid_view;
    TextView data_not;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_save_video, container, false);
        grid_view=view.findViewById(R.id.grid_view);
        data_not=view.findViewById(R.id.data_not);
        list.clear();
        load();
        grid_view.setLayoutManager(new GridLayoutManager(getContext(),3));
        return view;
    }
    public void load()
    {
        if(!list.isEmpty())
        {
            list.clear();
            adapters.notifyDataSetChanged();
        }

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "SaxHdVideo";
        File dir = new File(root);
        listAllVideo(new File(dir.getAbsolutePath()));
        adapters=new DownloadVideoImageAdapter(list,getContext());

        grid_view.setAdapter(adapters);
    }

    public  void listAllVideo(File filepath) {
        File[] files = filepath.listFiles();

        if (files != null) {
            for (int j = files.length - 1; j >= 0; j--) {
                String ss = files[j].toString();
                File check = new File(ss);
                Log.d("" + check.length(), "" + check.length());
                if (check.length() <= 1024) {

                } else  {
                    list.add(ss);

                }

            }
            return;
        }
        else
        {
            data_not.setVisibility(View.VISIBLE);
            System.out.println("Empty Folder");
        }


    }
}
