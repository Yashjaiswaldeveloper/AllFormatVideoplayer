package com.ffapp.ffxx.ffplayers.fragement;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.GalleryAdapetr;
import com.ffapp.ffxx.ffplayers.model.ImageFolder;
import com.pesonal.adsdk.AppManage;

import java.util.ArrayList;

import static com.pesonal.adsdk.AppManage.ADMOB_N;


public class GalleryFragment extends Fragment {
    View view;
    String orderby;
    RecyclerView recycle_view;
    private GalleryAdapetr allAdapter;
    RelativeLayout native_ads_layout;
    public GalleryFragment(String orderby) {
    this.orderby=orderby;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_gallery, container, false);
        recycle_view=view.findViewById(R.id.recycle_view);
        native_ads_layout=view.findViewById(R.id.native_ads_layout);
        AppManage.getInstance(getActivity()).showNative((ViewGroup) native_ads_layout, ADMOB_N, getContext());
       // AppManage.getInstance(getActivity()).show_Banner_google_NATIVE((ViewGroup) native_ads_layout, ADMOB_N1, FACEBOOK_N1,getContext());
        getPicturePaths();

        recycle_view.setLayoutManager(new GridLayoutManager(getContext(),3));
        return view;
    }
    public ArrayList<ImageFolder> getPicturePaths(){
        ArrayList<ImageFolder> picFolders = new ArrayList<>();
        ArrayList<String> picPaths = new ArrayList<>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};
        Cursor cursor =getActivity().getContentResolver().query(allImagesuri, projection, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
            }
            do{
                ImageFolder folds = new ImageFolder();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder+"/"));
                folderpaths = folderpaths+folder+"/";
                if (!picPaths.contains(folderpaths)) {
                    picPaths.add(folderpaths);
                    folds.setPath(folderpaths);
                    folds.setFolderName(folder);
                    folds.setFirstPic(datapath);
                    folds.addpics();
                    picFolders.add(folds);
                }else{
                    for(int i = 0;i<picFolders.size();i++){
                        if(picFolders.get(i).getPath().equals(folderpaths)){
                            picFolders.get(i).setFirstPic(datapath);
                            picFolders.get(i).addpics();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i = 0;i < picFolders.size();i++){
            Log.d("picture folders",picFolders.get(i).getFolderName()+" and path = "+picFolders.get(i).getPath()+" "+picFolders.get(i).getNumberOfPics());
        }

        //reverse order ArrayList
       /* ArrayList<imageFolder> reverseFolders = new ArrayList<>();
        for(int i = picFolders.size()-1;i > reverseFolders.size()-1;i--){
            reverseFolders.add(picFolders.get(i));
        }*/

        allAdapter=new GalleryAdapetr(picFolders,getContext());
        recycle_view.setAdapter(allAdapter);
        return picFolders;
    }
}
