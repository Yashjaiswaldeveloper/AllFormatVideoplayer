package com.ffapp.ffxx.ffplayers.comman;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.model.PictureFace;
import com.ffapp.ffxx.ffplayers.player.VideoModel;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Utils {
    private static final String TAG = "TAG";
    public static  boolean AllSelect=false;
    public static ArrayList<File> duplication=new ArrayList<>();
    public static ArrayList<VideoModel> list_select=new ArrayList<>();
    public static ArrayList<VideoModel> videoArrayList=new ArrayList<>();
    public static ArrayList<PictureFace> picture=new ArrayList<>();
    public static String timeConversion(long value) {
        String videoTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            videoTime = String.format("%02d:%02d", mns, scs);
        }
        return videoTime;
    }


    public static String getFileSize(File file) {
        String modifiedFileSize = null;
        double fileSize = 0.0;
        if (file.isFile()) {
            fileSize = (double) file.length();//in Bytes

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        } else {
            modifiedFileSize = "Unknown";
        }

        return modifiedFileSize;
    }
    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void IncreaseValue(int pos, TextView value, TextView speed, MediaPlayer player)
    {
        if(pos==1)
        {
            value.setText(""+25);
            speed.setText(""+0.25+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.25f));
        }
        else if(pos==2)
        {
            value.setText(""+30);
            speed.setText(""+0.3+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.30f));
        }
        else if(pos==3)
        {
            value.setText(""+35);
            speed.setText(""+0.35+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.35f));
        }
        else if(pos==4)
        {
            value.setText(""+40);
            speed.setText(""+0.4+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.40f));
        }
        else if(pos==5)
        {
            value.setText(""+45);
            speed.setText(""+0.45+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.45f));
        }
        else if(pos==6)
        {
            value.setText(""+50);
            speed.setText(""+0.5+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.50f));
        }
        else if(pos==7)
        {
            value.setText(""+55);
            speed.setText(""+0.55+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.55f));
        }
        else if(pos==8)
        {
            value.setText(""+60);
            speed.setText(""+0.6+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.60f));
        }
        else if(pos==9)
        {
            value.setText(""+65);
            speed.setText(""+0.65+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.65f));
        }
        else if(pos==10)
        {
            value.setText(""+70);
            speed.setText(""+0.7+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.70f));
        }
        else if(pos==11)
        {
            value.setText(""+75);
            speed.setText(""+0.75+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.75f));
        }
        else if(pos==12)
        {
            value.setText(""+80);
            speed.setText(""+0.8+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.80f));
        }
        else if(pos==13)
        {
            value.setText(""+85);
            speed.setText(""+0.85+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.85f));
        }
        else if(pos==14)
        {
            value.setText(""+90);
            speed.setText(""+0.9+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.90f));
        }
        else if(pos==15)
        {
            value.setText(""+95);
            speed.setText(""+0.95+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.95f));
        }
        else if(pos==16)
        {
            value.setText(""+100);
            speed.setText(""+1.0+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.0f));
        }
        else if(pos==17)
        {
            value.setText(""+110);
            speed.setText(""+1.1+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.10f));
        }
        else if(pos==18)
        {
            value.setText(""+120);
            speed.setText(""+1.2+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.20f));
        }
        else if(pos==19)
        {
            value.setText(""+130);
            speed.setText(""+1.3+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.30f));
        }
        else if(pos==20)
        {
            value.setText(""+140);
            speed.setText(""+1.4+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.40f));
        }
        else if(pos==21)
        {
            value.setText(""+150);
            speed.setText(""+1.5+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.50f));
        }
        else if(pos==22)
        {
            value.setText(""+160);
            speed.setText(""+1.6+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.60f));
        }
        else if(pos==23)
        {
            value.setText(""+170);
            speed.setText(""+1.7+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.70f));
        }
        else if(pos==24)
        {
            value.setText(""+180);
            speed.setText(""+1.8+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.80f));
        }
        else if(pos==25)
        {
            value.setText(""+190);
            speed.setText(""+1.9+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.90f));
        }
        else if(pos==26)
        {
            value.setText(""+200);
            speed.setText(""+2.0+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.0f));
        }else if(pos==27)
        {
            value.setText(""+210);
            speed.setText(""+2.1+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.10f));
        }
        else if(pos==28)
        {
            value.setText(""+220);
            speed.setText(""+2.2+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.20f));
        }
        else if(pos==29)
        {
            value.setText(""+230);
            speed.setText(""+2.3+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.30f));
        }
        else if(pos==30)
        {
            value.setText(""+240);
            speed.setText(""+2.4+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.40f));
        }
        else if(pos==31)
        {
            value.setText(""+250);
            speed.setText(""+2.5+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.50f));
        }else if(pos==32)
        {
            value.setText(""+260);
            speed.setText(""+2.6+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.60f));
        }
        else if(pos==33)
        {
            value.setText(""+270);
            speed.setText(""+2.7+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.70f));
        }
        else if(pos==34)
        {
            value.setText(""+280);
            speed.setText(""+2.8+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.80f));
        }
        else if(pos==35)
        {
            value.setText(""+290);
            speed.setText(""+2.9+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(2.90f));
        }
        else if(pos==36)
        {
            value.setText(""+300);
            speed.setText(""+3.0+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.0f));
        }
        else if(pos==37)
        {
            value.setText(""+310);
            speed.setText(""+3.1+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.10f));
        }
        else if(pos==38)
        {
            value.setText(""+320);
            speed.setText(""+3.2+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.20f));
        }
        else if(pos==39)
        {
            value.setText(""+330);
            speed.setText(""+3.3+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.30f));
        }
        else if(pos==40)
        {
            value.setText(""+340);
            speed.setText(""+3.4+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.40f));
        }
        else if(pos==41)
        {
            value.setText(""+350);
            speed.setText(""+3.5+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.50f));
        }
        else if(pos==42)
        {
            value.setText(""+360);
            speed.setText(""+3.6+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.60f));
        }
        else if(pos==43)
        {
            value.setText(""+370);
            speed.setText(""+3.7+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.70f));
        }
        else if(pos==44)
        {
            value.setText(""+380);
            speed.setText(""+3.8+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.80f));
        }
        else if(pos==45)
        {
            value.setText(""+390);
            speed.setText(""+3.9+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(3.90f));
        }
        else if(pos==46)
        {
            value.setText(""+400);
            speed.setText(""+4.0+"X");
            player.setPlaybackParams(player.getPlaybackParams().setSpeed(4.90f));
        }

    }

    public static boolean lockFile(File file, Context context) {

        String newPath = context.getExternalFilesDir(context.getResources().getString(R.string.private_videos)).getAbsolutePath();
        String name = file.getName();

        Log.e("TAG", "lockFile: " + newPath);
        boolean b = moveToPrivate(file, name, context, context.getResources().getString(R.string.private_videos));

        if (b) {
            Log.e("TAG", "lockFile: " + "Move SuccessFully");
            return true;
        } else {
            Log.e("TAG", "lockFile: " + "Error");
            return false;
        }

    }
    @NonNull
    public static String getFileTitleFromFileName(@NonNull String fileName) {
        final int lastIndex = fileName.lastIndexOf(".");
        return lastIndex == -1 ? fileName : fileName.substring(0, lastIndex);
    }
    public static boolean renameFile(File file, String newName, Context context) {

        String onlyPath = file.getParentFile().getAbsolutePath();
        String ext = file.getAbsolutePath();
        // ext = ext.substring(ext.lastIndexOf("."));
        Log.e(TAG, "renameFile: path" + onlyPath);
        String newPath = onlyPath + "/" + newName;
        File newFile = new File(newPath);

        if (newFile.exists()) {
            Toast.makeText(context, "File already exists...", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean rename = file.renameTo(newFile);
        if (rename) {
            ContentResolver resolver = context.getApplicationContext().getContentResolver();
            resolver.delete(
                    MediaStore.Files.getContentUri("external")
                    , MediaStore.MediaColumns.DATA + "=?", new String[]{file.getAbsolutePath()});
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(newFile));
            context.getApplicationContext().sendBroadcast(intent);


            return true;


        } else {
            Toast.makeText(context, "Oops! rename failed", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public static boolean moveToPrivate(File file, String newName, Context context, String folderName) {
        String onlyPath = context.getExternalFilesDir(folderName).getAbsolutePath();
        // ext = ext.substring(ext.lastIndexOf("."));
        Log.e("TAG", "renameFile: path" + onlyPath);
        String newPath = onlyPath + "/" + newName;
        File newFile = new File(newPath);

        if (newFile.exists()) {
            Toast.makeText(context, "File already exists...", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean rename = file.renameTo(newFile);
        if (rename) {
            ContentResolver resolver = context.getApplicationContext().getContentResolver();
            resolver.delete(
                    MediaStore.Files.getContentUri("external")
                    , MediaStore.MediaColumns.DATA + "=?", new String[]{file.getAbsolutePath()});
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(newFile));
            context.getApplicationContext().sendBroadcast(intent);


            return true;


        } else {
            Toast.makeText(context, "Oops! rename failed", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public static boolean unlockFile(File file, Context context) {
        SharePreferencess sp=new SharePreferencess(context);
        ArrayList<String> list=sp.getPrivateVideoPath();
        Log.e("Unlock_file", "unlockFile: " + list);
        Boolean result = false;
        String privateName = file.getName();
        if(list!=null)
        {
            for(int i=0;i<list.size();i++)
            {

                File file1 = new File(list.get(i));
                String videoName = file1.getName();
                if(videoName.equals(privateName))
                {
                    Log.e(TAG, "unlockFile: " + "Name Matched " + videoName + " " + privateName);
                    result = moveFromPrivate(file, videoName, context, list.get(i));
                    if(result)
                    {
                      list.remove(i);
                      sp.updatePrivateVideoPath(list);
                    }
                    break;

                }

            }


            if (result) {

                Toast.makeText(context,"Unlock Sucessfully",Toast.LENGTH_SHORT).show();
            }
        }
       /* String newPath = context.getExternalFilesDir("Private Video").getAbsolutePath();

        String name = file.getName();

        Log.e(TAG, "lockFile: " + newPath);
        boolean b = moveToPrivate(file, name, context);

        if (b) {
            Log.e(TAG, "lockFile: " + "Move SuccessFully");
            return true;
        } else {
            Log.e(TAG, "lockFile: " + "Error");
            return false;
        }
*/

        return result;
    }

    public static boolean moveFromPrivate(File file, String newName, Context context, String path) {

        File file1 = new File(path);

        String filePath = path.substring(0, path.lastIndexOf("/"));
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String onlyPath = file1.getParentFile().getAbsolutePath();
        Log.e(TAG, "moveFromPrivate: " + onlyPath);
        String newPath = onlyPath + "/" + newName;
        File newFile = new File(newPath);

        if (newFile.exists()) {
            Toast.makeText(context, "File already exists...", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean rename = file.renameTo(newFile);
        if (rename) {
            ContentResolver resolver = context.getApplicationContext().getContentResolver();
            resolver.delete(
                    MediaStore.Files.getContentUri("external")
                    , MediaStore.MediaColumns.DATA + "=?", new String[]{file.getAbsolutePath()});
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(newFile));
            context.getApplicationContext().sendBroadcast(intent);


            return true;


        } else {
            Toast.makeText(context, "Oops! rename failed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static boolean lockFolder(ArrayList<String> files, Context context) {
        String newPath = context.getExternalFilesDir(context.getResources().getString(R.string.private_videos)).getAbsolutePath();
        boolean b = false;
        for (int i = 0; i < files.size(); i++) {
            File file = new File(files.get(i));
            String name = file.getName();
            Log.e(TAG, "lockFolder: " + newPath);
            b = moveToPrivate(file, name, context, context.getResources().getString(R.string.private_videos));
        }
        if (b) {
            Log.e(TAG, "lockFile: " + "Move SuccessFully");
            return true;
        } else {
            Log.e(TAG, "lockFile: " + "Error");
            return false;
        }
    }


}
