package com.ffapp.ffxx.ffplayers.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.StatusImageActivity;

import java.io.File;
import java.util.ArrayList;

public class ImageStatusAdapter extends RecyclerView.Adapter<ImageStatusAdapter.MyHolder> {
    ArrayList<String> list;
    Context context;
    LayoutInflater inflater;
    ProgressDialog progressBar;

    public ImageStatusAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.status_image,parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final File hiddenpath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/.Statuses/");
        Bitmap myBitmap = BitmapFactory.decodeFile(hiddenpath.getAbsolutePath()+"/"+list.get(position));
        holder.image_view.setImageBitmap(myBitmap);
        File file=new File(hiddenpath.getAbsolutePath()+"/"+list.get(position));

        holder.relative_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img=hiddenpath.getAbsolutePath()+"/"+list.get(position);
                Intent intent=new Intent(context, StatusImageActivity.class);
                intent.putExtra("img",img);
                intent.putExtra("save_img","not_save");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView image_view;
        TextView title;
        RelativeLayout relative_lay;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image_view=itemView.findViewById(R.id.image_view);
            relative_lay=itemView.findViewById(R.id.relative_lay);

        }
    }

   /* public void saveImageToExternal(Bitmap bm) throws IOException {
        SimpleDateFormat sd = new SimpleDateFormat("yy mm hh");
        String date = sd.format(new Date());
        String name = "Status_" + date + ".png";
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "StatusSaver";
        File rootFile = new File(root);
        rootFile.mkdirs();
        File imageFile = new File(rootFile, name);
        FileOutputStream out = new FileOutputStream(imageFile);
        try{
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            context.startActivity(new Intent(context, StatusImageListActivity.class));
            progressBar.dismiss();
            MediaScannerConnection.scanFile(context,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch(Exception e) {
            throw new IOException();
        }
    }*/
}
