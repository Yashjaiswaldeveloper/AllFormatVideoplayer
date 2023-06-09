package com.ffapp.ffxx.ffplayers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.model.PictureFace;

import java.util.ArrayList;

public class MyCustomPagerAdapter extends PagerAdapter {
    private ArrayList<PictureFace> pictureList=new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    int pos;

    public MyCustomPagerAdapter(Context context,int pos) {
        this.context = context;
        this.pos=pos;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addData(@NonNull ArrayList<PictureFace> data) {
        pictureList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pictureList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,  int position) {
        View itemView = layoutInflater.inflate(R.layout.slide_image, container, false);
        ImageView imageView =itemView.findViewById(R.id.image);
        position=pos;
        final PictureFace image = pictureList.get(position);
        Glide.with(context)
                .load(image.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(imageView);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}