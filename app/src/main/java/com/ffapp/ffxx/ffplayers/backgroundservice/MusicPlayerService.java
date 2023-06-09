package com.ffapp.ffxx.ffplayers.backgroundservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.player.VideoModel;
import com.ffapp.ffxx.ffplayers.player.VideoPlayActivity;

import java.util.ArrayList;

public class MusicPlayerService extends Service {

    public static ArrayList<VideoModel> list = new ArrayList<>();
    String uri;
    int pos = 0, duration;
    int count = 0;
    MediaPlayer videoView;
    RemoteViews views;
    public boolean status=false;
    public MusicPlayerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        videoView = new MediaPlayer();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uri = intent.getStringExtra("uri");
        pos = intent.getIntExtra("pos", 0);
        duration = intent.getIntExtra("duration", 0);
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            play(pos, duration);
            showNotification(Uri.parse(uri));
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            videoView.stop();

            if (count > 0) {
                count--;
                Uri uri = list.get(count).getVideoUri();
                nextplay(uri);
            } else {
                count = list.size() - 1;
                Uri uri = list.get(count).getVideoUri();
                nextplay(uri);
            }


        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {


            if(status==false)
            {
                videoView.pause();
                status=true;
            }
            else
            {
                status=false;
                videoView.start();
            }
            showNotification(Uri.parse(uri));

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            videoView.stop();

            if (count < (list.size() - 1)) {
                count++;
                Uri uri = list.get(count).getVideoUri();
                nextplay(uri);
            } else {
                count = 0;
                Uri uri = list.get(count).getVideoUri();
                nextplay(uri);
            }

        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {


            startActivity(new Intent(getApplicationContext(), VideoPlayActivity.class));

        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void showNotification(Uri uri) {

        try {

            Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(
                    getContentResolver(), uri,
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null );
            if( cursor != null && cursor.getCount() > 0 ) {
                cursor.moveToFirst();
                String thumnail = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Thumbnails.DATA ) );
            }
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            views= new RemoteViews(getPackageName(), R.layout.notification_lay);

            Intent notificationIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent previousIntent = new Intent(this, MusicPlayerService.class);
            previousIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent playIntent = new Intent(this, MusicPlayerService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);

            PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent nextIntent = new Intent(this, MusicPlayerService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);

            PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent closeIntent = new Intent(this, MusicPlayerService.class);
            closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);

            PendingIntent pcloseIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.play_pause, pplayIntent);
            views.setOnClickPendingIntent(R.id.next_video, pnextIntent);
            views.setOnClickPendingIntent(R.id.previous_btn, ppreviousIntent);
            views.setOnClickPendingIntent(R.id.pclose_intent, pcloseIntent);
            if(status==false)
            {

                views.setImageViewResource(R.id.play_pause, R.drawable.ic_pause_circle_filled_black_24dp);
            }
            else
            {
                views.setImageViewResource(R.id.play_pause, R.drawable.ic_play_circle_filled_black_24dp);
            }
            views.setTextViewText(R.id.title, list.get(count).getVideoTitle());
            NotificationCompat.Builder notificationBuilder = createNotificationBuilder("downloader_channel");
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setCustomContentView(views);
            notificationBuilder.setTicker("Start downloading from the server");
            notificationBuilder.setOngoing(true);
            notificationBuilder.setAutoCancel(false);
            notificationBuilder.setVibrate(null);

            notificationBuilder.setSmallIcon(android.R.drawable.ic_media_play);
            notificationManagerCompat.notify(1, notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private NotificationCompat.Builder createNotificationBuilder(String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelName = getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        return new NotificationCompat.Builder(getApplicationContext(), channelId);
    }

    public void play(int pos, int duration) {

        try {

            if (duration == 0) {
                videoView = MediaPlayer.create(this, list.get(pos).getVideoUri());
                videoView.setLooping(true);
                videoView.start();
            } else {
                videoView = MediaPlayer.create(this, list.get(pos).getVideoUri());
                videoView.setLooping(true);
                videoView.seekTo(duration);
                videoView.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void nextplay(Uri uris) {

        try {
            Log.e("Log_uri",""+uris);
            videoView = MediaPlayer.create(this, uris);
            videoView.setLooping(true);
            videoView.start();
            showNotification(uris);
            status=false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }
}
