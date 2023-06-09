package com.ffapp.ffxx.ffplayers.player;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PictureInPictureParams;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.activity.SettingActivity;
import com.ffapp.ffxx.ffplayers.adapter.MediaAdapter;
import com.ffapp.ffxx.ffplayers.backgroundservice.Constants;
import com.ffapp.ffxx.ffplayers.backgroundservice.MusicPlayerService;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.comman.Utils;
import com.ffapp.ffxx.ffplayers.equalizer.DialogEqualizerFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class VideoPlayActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    String link = "";
    private static final int STEP_PROGRESS = 0;
    private static int GESTURE_FLAG = 0;
    VideoView videoView;
    ImageView prev, next, pause, vol_btn, left_roted, right_roted, crop_free, audio_mix_btn;
    SeekBar seekBar, volume_sek;
    int video_index = 0;
    double current_pos, total_duration;
    TextView current, total, volume_count;
    RelativeLayout showProgress, left_temp, tool_bg, screen_shot_s;
    Handler mHandler, handler;
    boolean isVisible = true;
    RelativeLayout relativeLayout, media_temp;
    public static final int PERMISSION_READ = 0;
    private AudioManager audioManager;
    private int seekBackwardTime = 10000;
    private int seekForwardTime = 10000;
    ImageView lock_screen, open_lock, rotetion_btn, reapet_btn, pip_video;
    public int landscape = 1;
    public boolean reapet = false;
    TextView speed_btn;
    MediaPlayer player;
    int count_num = 16;
    ImageView back_btn, media_librery, screen_shot, bright_ness, nevigation_btn, on_line_subtitle;
    TextView title_txt;
    RecyclerView recycle_view;
    private float brightness = -1;
    private ContentResolver cResolver;
    private Window window;
    ArrayList<VideoModel> videoArrayList = new ArrayList<>();
    private int curBrightnessValue;
    ViewGroup.LayoutParams params;
    int screenWidht;
    int screenHight;
    int crop = 0;
    boolean playcheck = false;
    CountDownTimer timer;
    private GestureDetectorCompat mDetector;
    ScaleGestureDetector mScaleDetector;
    boolean firstScroll;
    private AudioManager mAudioManager;
    private BrightnessControl mBrightnessControl;
    public static int boostLevel = 0;
    RelativeLayout vol_temp, bright_temp;
    SeekBar bright_sek;
    TextView bright_count;
    private static final int SWIPE_THRESHOLD = 60;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private int mCurVolume = -1;
    private int mMaxVolume;
    private float mCurBrightness = -1;
    private static final long PROGRESS_SEEK = 500;
    private int videoTotalTime;
    SharePreferencess sp;
    boolean checked;
    String track_status;
    private int stopPosition;
    int orientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_play);
        if (checkPermission()) {
            sp = new SharePreferencess(this);
            mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mBrightnessControl = new BrightnessControl(this);
            cResolver = getContentResolver();
            window = getWindow();
            videoArrayList = Utils.videoArrayList;
            mScaleDetector = new ScaleGestureDetector(this, this);
            mDetector = new GestureDetectorCompat(this, this);
            mFormatBuilder = new StringBuilder();
            mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            orientation = display.getOrientation();
            setVideo();
        }
    }

    public void setVideo() {
        bright_count = findViewById(R.id.bright_count);
        bright_sek = findViewById(R.id.bright_sek);
        bright_temp = findViewById(R.id.bright_temp);
        vol_temp = findViewById(R.id.vol_temp);
        volume_count = findViewById(R.id.volume_count);
        volume_sek = findViewById(R.id.volume_sek);
        on_line_subtitle = findViewById(R.id.on_line_subtitle);
        nevigation_btn = findViewById(R.id.nevigation_btn);
        pip_video = findViewById(R.id.pip_video);
        crop_free = findViewById(R.id.crop_free);
        bright_ness = findViewById(R.id.bright_ness);
        screen_shot_s = findViewById(R.id.screen_shot_s);
        screen_shot = findViewById(R.id.screen_shot);
        recycle_view = findViewById(R.id.recycle_view);
        media_temp = findViewById(R.id.media_temp);
        media_librery = findViewById(R.id.media_librery);
        title_txt = findViewById(R.id.title_txt);
        back_btn = findViewById(R.id.back_btn);
        tool_bg = findViewById(R.id.tool_bg);
        reapet_btn = findViewById(R.id.reapet_btn);
        audio_mix_btn = findViewById(R.id.audio_mix_btn);
        speed_btn = findViewById(R.id.speed_btn);
        rotetion_btn = findViewById(R.id.rotetion_btn);
        open_lock = findViewById(R.id.open_lock);
        lock_screen = findViewById(R.id.lock_screen);
        left_temp = findViewById(R.id.left_temp);
        left_roted = findViewById(R.id.left_roted);
        right_roted = findViewById(R.id.right_roted);
        videoView = findViewById(R.id.videoview);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        pause = findViewById(R.id.pause);
        seekBar = findViewById(R.id.seekbar);
        current = findViewById(R.id.current);
        total = findViewById(R.id.total);
        vol_btn = findViewById(R.id.vol_btn);
        showProgress = findViewById(R.id.showProgress);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        video_index = getIntent().getIntExtra("pos", 0);
        mHandler = new Handler();
        handler = new Handler();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video_index++;
                if (sp.getAutoPlay()) {
                    if (video_index < (videoArrayList.size())) {
                        playcheck = false;
                        playVideo(video_index);
                    } else {
                        video_index = 0;
                        playVideo(video_index);
                    }
                }


            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setVideoProgress();
                player = mp;
            }
        });
        adapterFile();
        playVideo(video_index);
        prevVideo();
        nextVideo();
        setPause();
        hideLayout();
        setVolume();
        openLock();
        lockScreen();
        rotedScreen();
        setSpeedVideo();
        setAudioMixer();
        repeatVideo();
        AllFile();
        ScreenShort();
        brightness();
        CropFree();
        PipVideoDisplay();
        navigationSett();
        subTitleOnline();
        left_roted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = videoView.getCurrentPosition();
                if (currentPosition - seekBackwardTime >= 0) {
                    videoView.seekTo(currentPosition - seekBackwardTime);
                } else {
                    videoView.seekTo(0);
                }
            }
        });
        right_roted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = videoView.getCurrentPosition();
                if (currentPosition + seekForwardTime <= videoView.getDuration()) {

                    videoView.seekTo(currentPosition + seekForwardTime);
                } else {

                    videoView.seekTo(videoView.getDuration());
                }
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setResult(-1);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (sp.getReapetVideo() == false) {

            reapet_btn.setImageResource(R.drawable.repeat_one_icc);
        } else {

            reapet_btn.setImageResource(R.drawable.repeat_icc);
        }
        if (sp.getOrientation() == "1") {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            rotetion_btn.setImageResource(R.drawable.portaintan_icc);
            landscape = 1;
        } else if (sp.getOrientation().equals("2")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            rotetion_btn.setImageResource(R.drawable.land_scape_icc);
            landscape = 2;
        } else if (sp.getOrientation().equals("3")) {
            landscape = 3;
            rotetion_btn.setRotation(90);
            rotetion_btn.setImageResource(R.drawable.portaintan_icc);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

    }

    public void subTitleOnline() {
        try {
            on_line_subtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubTitle();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigationSett() {
        try {
            nevigation_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationBox();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PipVideoDisplay() {
        try {
            pip_video.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    enterPipMode();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CropFree() {
        try {
            crop_free.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (crop == 0) {
                        getDeviceWidthandHight();
                        params.width = screenWidht - 50;
                        params.height = screenHight - 50;
                        videoView.setLayoutParams(params);
                        crop_free.setImageResource(R.drawable.full_screen_icc);
                        crop = 1;
                    } else if (crop == 1) {

                        getDeviceWidthandHight();
                        params.width = screenWidht - 300;
                        params.height = screenHight - 100;
                        videoView.setLayoutParams(params);
                        crop_free.setImageResource(R.drawable.crop_icc);
                        crop = 2;
                    } else if (crop == 2) {
                        params.width = screenWidht;
                        params.height = screenHight - 500;
                        videoView.setLayoutParams(params);
                        crop_free.setImageResource(R.drawable.ic_full_screen);
                        crop = 0;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void brightness() {
        try {
            bright_ness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrightnessBox();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adapterFile() {
        recycle_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        MediaAdapter adapter = new MediaAdapter(this, videoArrayList);
        recycle_view.setAdapter(adapter);

        adapter.setOnItemClickListener(new MediaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                playVideo(pos);
                media_temp.setVisibility(View.GONE);
            }
        });
    }

    public void ScreenShort() {
        try {
            screen_shot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screen_shot_s.setDrawingCacheEnabled(true);
                    Bitmap bitmap = screen_shot_s.getDrawingCache();
                    try {
                        saveImageToExternal(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AllFile() {
        try {
            media_librery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    media_temp.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void repeatVideo() {
        try {
            reapet_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sp.getReapetVideo() == false) {
                        sp.setReapetVideo(true);
                        player.setLooping(true);
                        reapet = true;
                        reapet_btn.setImageResource(R.drawable.repeat_one_icc);
                    } else {
                        sp.setReapetVideo(false);
                        player.setLooping(false);
                        reapet = false;
                        reapet_btn.setImageResource(R.drawable.repeat_icc);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAudioMixer() {
        try {
            audio_mix_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInDialog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpeedVideo() {
        try {
            speed_btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    speedBox();
                    // player.setPlaybackParams(player.getPlaybackParams().setSpeed(0.25f));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rotedScreen() {
        try {
            rotetion_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (landscape == 1) {
                        rotetion_btn.setRotation(180);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        rotetion_btn.setImageResource(R.drawable.portaintan_icc);
                        sp.setOrientation("1");
                        landscape = 2;
                    } else if (landscape == 2) {
                        sp.setOrientation("2");
                        rotetion_btn.setImageResource(R.drawable.land_scape_icc);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        landscape = 3;
                    } else if (landscape == 3) {
                        sp.setOrientation("3");
                        rotetion_btn.setRotation(90);
                        rotetion_btn.setImageResource(R.drawable.portaintan_icc);
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        landscape = 1;
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    public void openLock() {
        try {
            open_lock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open_lock.setVisibility(View.GONE);
                    showProgress.setVisibility(View.VISIBLE);
                    left_temp.setVisibility(View.VISIBLE);
                    relativeLayout.setClickable(true);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void lockScreen() {
        try {
            lock_screen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress.setVisibility(View.GONE);
                    left_temp.setVisibility(View.GONE);
                    open_lock.setVisibility(View.VISIBLE);
                    relativeLayout.setClickable(false);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume() {
        try {
            vol_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    volBox();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playVideo(final int pos) {
        try {
            final String id = String.valueOf(videoArrayList.get(pos));
            int log = sp.getResume(id);
            if (sp.getResume(id) == 0) {
                playcheck = false;
                title_txt.setText("" + videoArrayList.get(pos).getVideoTitle());
                videoView.setVideoURI(videoArrayList.get(pos).getVideoUri());
                videoView.start();
                videoView.seekTo(sp.getResume(id));
                pause.setImageResource(R.drawable.pause_ic);
                video_index = pos;
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sp.getResumeDefault().equals("3")) {
                            playcheck = false;
                            title_txt.setText("" + videoArrayList.get(pos).getVideoTitle());
                            videoView.setVideoURI(videoArrayList.get(pos).getVideoUri());
                            videoView.start();
                            videoView.seekTo(sp.getResume(id));
                            pause.setImageResource(R.drawable.pause_ic);
                            video_index = pos;

                        } else if (sp.getResumeDefault().equals("1")) {
                            Log.e("check_status", "Sucessfully");
                            ResumeBox(pos, id);

                        } else {
                            sp.setResume(id, 0);
                            playcheck = false;
                            title_txt.setText("" + videoArrayList.get(pos).getVideoTitle());
                            videoView.setVideoURI(videoArrayList.get(pos).getVideoUri());
                            videoView.start();
                            videoView.seekTo(sp.getResume(id));
                            pause.setImageResource(R.drawable.pause_ic);
                            video_index = pos;
                        }


                    }
                });


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVideoProgress() {
        //get the video duration
        current_pos = videoView.getCurrentPosition();
        total_duration = videoView.getDuration();

        //display video duration
        try {
            videoTotalTime = Integer.parseInt(timeConversion((long) total_duration));
        } catch (Exception e) {
            e.printStackTrace();
        }

        total.setText(timeConversion((long) total_duration));
        current.setText(timeConversion((long) current_pos));
        seekBar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = videoView.getCurrentPosition();
                    current.setText(timeConversion((long) current_pos));
                    seekBar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                videoView.seekTo((int) current_pos);
            }
        });
    }

    public void prevVideo() {
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_index > 0) {
                    video_index--;
                    playVideo(video_index);
                } else {
                    video_index = videoArrayList.size() - 1;
                    playVideo(video_index);
                }
            }
        });
    }

    public void nextVideo() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_index < (videoArrayList.size() - 1)) {
                    video_index++;
                    playVideo(video_index);
                } else {
                    video_index = 0;
                    playVideo(video_index);
                }
            }
        });
    }

    public void setPause() {
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    playcheck = true;
                    videoView.pause();
                    pause.setImageResource(R.drawable.play_ic);
                } else {
                    playcheck = false;
                    videoView.start();
                    pause.setImageResource(R.drawable.pause_ic);
                }
            }
        });
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

    public void hideLayout() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showProgress.setVisibility(View.GONE);
                left_temp.setVisibility(View.GONE);
                tool_bg.setVisibility(View.GONE);
                vol_temp.setVisibility(View.GONE);
                bright_temp.setVisibility(View.GONE);
                isVisible = false;
            }
        };
        handler.postDelayed(runnable, 5000);
        relativeLayout.setOnTouchListener(this);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                if (isVisible) {
                    showProgress.setVisibility(View.GONE);
                    left_temp.setVisibility(View.GONE);
                    tool_bg.setVisibility(View.GONE);
                    media_temp.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    showProgress.setVisibility(View.VISIBLE);
                    left_temp.setVisibility(View.VISIBLE);
                    tool_bg.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable, 5000);
                    isVisible = true;
                }
            }
        });

    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READ: {
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow storage permission", Toast.LENGTH_LONG).show();
                    } else {
                        setVideo();
                    }
                }
            }
        }
    }

    public void volBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.volume_file, null);
        ImageView close_btn = view.findViewById(R.id.close_btn);
        SeekBar seek_volume = view.findViewById(R.id.seek_volume);
        final TextView count = view.findViewById(R.id.count);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        try {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int volume_level = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            count.setText("" + volume_level);
            seek_volume.setProgress(volume_level);
            seek_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seek_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            seek_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                    count.setText("" + progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {

        }


    }

    public void BrightnessBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.bright_ness, null);
        ImageView close_btn = view.findViewById(R.id.close_btn);
        SeekBar seek_volume = view.findViewById(R.id.seek_volume);
        seek_volume.setMax(181);
        seek_volume.setKeyProgressIncrement(1);
        final TextView count = view.findViewById(R.id.count);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
            count.setText("" + curBrightnessValue);
            seek_volume.setProgress(curBrightnessValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        try {
            seek_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    count.setText("" + Integer.toString(progress));
                    setScreenBrightness((float) seekBar.getProgress() / 100);
                }


                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {

        }


    }

    public void ResumeBox(final int pos, final String id) {
        final AlertDialog.Builder alertdailog = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.resume_box, null);
        TextView title = view.findViewById(R.id.title);
        final CheckBox chek_default = view.findViewById(R.id.chek_default);
        TextView start_over = view.findViewById(R.id.start_over);
        TextView resume_video = view.findViewById(R.id.resume_video);
        title.setText("Resume Video");
        alertdailog.setView(view);
        final AlertDialog alertDialog = alertdailog.create();
        alertdailog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        chek_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = chek_default.isChecked();
                if (status) {
                    sp.setResumeDefault("3");
                } else {
                    sp.setResumeDefault("1");
                }
            }
        });

        start_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setResume(id, 0);
                playcheck = false;
                title_txt.setText("" + videoArrayList.get(pos).getVideoTitle());
                videoView.setVideoURI(videoArrayList.get(pos).getVideoUri());
                videoView.start();
                videoView.seekTo(sp.getResume(id));
                pause.setImageResource(R.drawable.pause_ic);
                video_index = pos;
                alertDialog.dismiss();
            }
        });
        resume_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playcheck = false;
                title_txt.setText("" + videoArrayList.get(pos).getVideoTitle());
                videoView.setVideoURI(videoArrayList.get(pos).getVideoUri());
                videoView.start();
                videoView.seekTo(sp.getResume(id));
                pause.setImageResource(R.drawable.pause_ic);
                video_index = pos;
                alertDialog.dismiss();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void speedBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.playbackspeed, null);
        ImageView add_speed_img = view.findViewById(R.id.add_speed_img);
        ImageView sub_speed_img = view.findViewById(R.id.sub_speed_img);
        final TextView count = view.findViewById(R.id.count_);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        count.setText("" + 100);
        speed_btn.setText("" + 1.0 + "X");
        player.setPlaybackParams(player.getPlaybackParams().setSpeed(1.0f));
        add_speed_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (count_num <= 46) {

                    Utils.IncreaseValue(count_num, count, speed_btn, player);
                    count_num++;
                }

            }
        });
        sub_speed_img.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (count_num >= 1) {
                    Utils.IncreaseValue(count_num, count, speed_btn, player);
                    count_num--;
                }


            }
        });


    }

    public void saveImageToExternal(Bitmap bm) throws IOException {
        SimpleDateFormat sd = new SimpleDateFormat("yy mm hh");
        String date = sd.format(new Date());
        String name = "Status_" + date + ".png";
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "ScreenShot";
        File rootFile = new File(root);
        rootFile.mkdirs();
        File imageFile = new File(rootFile, name);
        FileOutputStream out = new FileOutputStream(imageFile);
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(this, new String[]{imageFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {

                }
            });
            Toast.makeText(getApplicationContext(), "Save Image", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            throw new IOException();
        }
    }

    private void setScreenBrightness(float num) {

        WindowManager.LayoutParams layoutParams = super.getWindow().getAttributes();
        layoutParams.screenBrightness = mCurBrightness + num;
        super.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        if (player != null) {
            player.pause();

            String id = String.valueOf(videoArrayList.get(video_index));
            stopPosition = player.getCurrentPosition();
            sp.setResume(id, stopPosition);

        }
        setResult(-1);
        finish();


    }

    public void getDeviceWidthandHight() {
        params = videoView.getLayoutParams();
        screenWidht = getWindowManager().getDefaultDisplay().getWidth();
        screenHight = getWindowManager().getDefaultDisplay().getHeight();

    }

    private void showInDialog() {
        int sessionId = player.getAudioSessionId();
        if (sessionId > 0) {
            DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
                    .setAudioSessionId(sessionId)
                    .title(R.string.app_name)
                    .build();

            fragment.show(getSupportFragmentManager(), "eq");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void enterPipMode() {


        Rational rational = new Rational(videoView.getWidth(),
                videoView.getHeight());

        PictureInPictureParams params =
                new PictureInPictureParams.Builder()
                        .setAspectRatio(rational)
                        .build();
        videoView.setMediaController(null);
        enterPictureInPictureMode(params);
    }

    public void NavigationBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.navigationbox, null);
        RelativeLayout timer = view.findViewById(R.id.timer);
        TextView check_title = view.findViewById(R.id.check_title);
        RelativeLayout reapet_btn = view.findViewById(R.id.reapet_btn);
        final CheckBox reapet = view.findViewById(R.id.check_status);
        RelativeLayout share_video = view.findViewById(R.id.share_video);
        RelativeLayout track = view.findViewById(R.id.track);
        RelativeLayout setting = view.findViewById(R.id.setting);
        RelativeLayout play_back = view.findViewById(R.id.play_back);
        RelativeLayout other = view.findViewById(R.id.other);
        builder.setView(view);
        if (sp.getReapetVideo()) {
            check_title.setText("Repeat One");
            reapet.setClickable(true);
            reapet.setChecked(true);
        } else {
            check_title.setText("Repeat");
            reapet.setChecked(false);
        }
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 400;
        layoutParams.y = 60;
        dialog.getWindow().setAttributes(layoutParams);
        try {
            timer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (playcheck == true) {
                        DisiableTimer();
                    } else {
                        StartTimer();
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        reapet_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (sp.getReapetVideo()) {
                    reapet.setChecked(false);
                    sp.setReapetVideo(false);
                } else {
                    sp.setReapetVideo(true);
                    reapet.setChecked(true);
                }
            }
        });
        share_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri video = videoArrayList.get(video_index).videoUri;
                shareVideo(new File(String.valueOf(video)));
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Other();
            }
        });

        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Track();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });
        play_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri video = videoArrayList.get(video_index).videoUri;
                int duration = videoView.getDuration();
                MusicPlayerService.list = videoArrayList;
                Intent intent = new Intent(getApplicationContext(), MusicPlayerService.class);
                intent.putExtra("uri", "" + video);
                intent.putExtra("pos", video_index);
                intent.putExtra("duration", duration);
                intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(intent);
                finish();
            }
        });


    }

    public void SubTitle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.subtitle, null);
        RelativeLayout subtitle_btn = view.findViewById(R.id.subtitle_btn);
        RelativeLayout off_line_title = view.findViewById(R.id.off_line_title);
        CheckBox check_box = view.findViewById(R.id.check_box);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 400;
        layoutParams.y = 60;
        layoutParams.x = 20;
        dialog.getWindow().setAttributes(layoutParams);
    }

    public void Other() {
        final File file = new File(String.valueOf(videoArrayList.get(video_index).getVideoUri()));
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.other_file, null);
        RelativeLayout sw_decode = view.findViewById(R.id.sw_decode);
        RelativeLayout rename_btn = view.findViewById(R.id.rename_btn);
        RelativeLayout lock_btn = view.findViewById(R.id.lock_btn);
        RelativeLayout delete_btn = view.findViewById(R.id.delete_btn);
        RelativeLayout properties_btn = view.findViewById(R.id.properties_btn);
        CheckBox check_box = view.findViewById(R.id.check_box);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = 400;
        layoutParams.y = 60;
        layoutParams.x = 20;
        dialog.getWindow().setAttributes(layoutParams);

        properties_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PropertiesBox();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DeleteVideo();

            }
        });

        lock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                boolean status = Utils.lockFile(file, VideoPlayActivity.this);
                if (status) {
                    sp.setPrivateVideoPath(file.getAbsolutePath());
                    setResult(-1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Opps! Error occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rename_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Rename();
            }
        });
    }

    public void StartTimer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.sleepstartbox, null);
        TextView disable_btn = view.findViewById(R.id.disable_btn);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView ok_btnn = view.findViewById(R.id.ok_btnn);
        final TextView timer_txt = view.findViewById(R.id.timer_txt);
        SeekBar seek_timer = view.findViewById(R.id.seek_timer);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        seek_timer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timer_txt.setText("Stop Playing After: " + progress + " minute(s)");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ok_btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = timer_txt.getText().toString();
                try {
                    int time = Integer.parseInt(value);
                    timer = new CountDownTimer(time * 10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.e("Timer", "" + millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            player.stop();
                        }
                    };
                    timer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        disable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void DisiableTimer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.sleeptimerbox, null);
        RelativeLayout ok_btn = view.findViewById(R.id.ok_btn);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    public void PropertiesBox() {
        String name = videoArrayList.get(video_index).getVideoTitle();
        String duration = videoArrayList.get(video_index).getVideoDuration();
        String uri = String.valueOf(videoArrayList.get(video_index).getVideoUri());
        File file = new File(uri);
        String size = Utils.getFileSize(file);
        long lastcreat = file.lastModified();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date now = new Date(lastcreat);
        String strDate = df.format(now);
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.properties_file, null);
        TextView file_txt = view.findViewById(R.id.file_txt);
        TextView duration_txt = view.findViewById(R.id.duration_txt);
        TextView file_size_txt = view.findViewById(R.id.file_size_txt);
        TextView location_txt = view.findViewById(R.id.location_txt);
        TextView date_txt = view.findViewById(R.id.date_txt);
        RelativeLayout ok_btn = view.findViewById(R.id.ok_btn);
        file_txt.setText(file.getName());
        duration_txt.setText(duration);
        date_txt.setText(strDate);
        file_size_txt.setText(size);
        location_txt.setText("" + file.getAbsolutePath());

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void Track() {
        String name = videoArrayList.get(video_index).getVideoTitle();
        File file = new File(name);

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.track_file, null);
        RelativeLayout video_track_btn = view.findViewById(R.id.video_track_btn);
        RelativeLayout audio_track_btn = view.findViewById(R.id.audio_track_btn);
        final View audio_f = view.findViewById(R.id.audio_f);
        final View video_f = view.findViewById(R.id.video_f);
        final TextView video_txt = view.findViewById(R.id.video_txt);
        final TextView audio_txt = view.findViewById(R.id.audio_txt);
        TextView ok_v_butt = view.findViewById(R.id.ok_v_butt);
        TextView cancel_v_butt = view.findViewById(R.id.cancel_v_butt);
        TextView ok_butt = view.findViewById(R.id.ok_butt);
        TextView cancel_audio = view.findViewById(R.id.cancel_audio);
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(String.valueOf(videoArrayList.get(video_index).getVideoUri()));
            long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            retriever.release();
            TextView rezolation_title = view.findViewById(R.id.rezolation_title);
            rezolation_title.setText("" + width + "X" + height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final LinearLayout audio_temp = view.findViewById(R.id.audio_temp);
        final LinearLayout video_temp = view.findViewById(R.id.video_temp);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        video_track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_temp.setVisibility(View.VISIBLE);
                video_f.setVisibility(View.VISIBLE);
                audio_temp.setVisibility(View.GONE);
                audio_f.setVisibility(View.GONE);
                video_txt.setTextColor(getResources().getColor(R.color.white));
                audio_txt.setTextColor(getResources().getColor(R.color.white_f));
            }
        });

        audio_track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_txt.setTextColor(getResources().getColor(R.color.white_f));
                audio_txt.setTextColor(getResources().getColor(R.color.white));
                video_temp.setVisibility(View.GONE);
                audio_temp.setVisibility(View.VISIBLE);
                audio_f.setVisibility(View.VISIBLE);
                video_f.setVisibility(View.GONE);
            }
        });


        final RelativeLayout _none_v_btn = view.findViewById(R.id._none_v_btn);
        RelativeLayout auto_video_btn = view.findViewById(R.id.auto_video_btn);
        RelativeLayout rezulation_btn = view.findViewById(R.id.rezulation_btn);


        final RadioButton rezolation_check_txt = view.findViewById(R.id.rezolation_check_txt);
        final RadioButton auto_video = view.findViewById(R.id.auto_video);
        final RadioButton none_v_check = view.findViewById(R.id.none_v_check);

        if (sp.getVideoVisible()) {
            none_v_check.setChecked(true);
            videoView.setBackgroundColor(getResources().getColor(R.color.gray));
        } else {
            auto_video.setChecked(true);
            videoView.setBackgroundColor(0);

        }

        _none_v_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setVideoVisible(true);
                track_status = "none";
                none_v_check.setChecked(true);
                auto_video.setChecked(false);
                rezolation_check_txt.setChecked(false);
            }
        });
        auto_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setVideoVisible(false);
                track_status = "auto";
                auto_video.setChecked(true);
                none_v_check.setChecked(false);
                rezolation_check_txt.setChecked(false);

            }
        });

        rezulation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                track_status = "resulation";
                none_v_check.setChecked(false);
                rezolation_check_txt.setChecked(true);
                auto_video.setChecked(false);

            }
        });
        ok_v_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (sp.getVideoVisible()) {
                    none_v_check.setChecked(true);
                    videoView.setBackgroundColor(getResources().getColor(R.color.background));
                } else {
                    auto_video.setChecked(true);
                    videoView.setBackgroundColor(0);

                }
            }
        });
        cancel_v_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RelativeLayout _none_audio_btn = view.findViewById(R.id._none_audio_btn);
        RelativeLayout auto_audio_btn = view.findViewById(R.id.auto_audio_btn);
        RelativeLayout eng_stro = view.findViewById(R.id.eng_stro);
        TextView file_size_title = view.findViewById(R.id.file_size_title);


        final RadioButton auto_audio = view.findViewById(R.id.auto_audio);
        final RadioButton file_size_txt = view.findViewById(R.id.file_size_txt);
        final RadioButton none__audio_check = view.findViewById(R.id.none__audio_check);
        if (sp.getAudioTrack()) {
            none__audio_check.setChecked(true);
            player.setVolume(0, 0);
        } else {
            auto_audio.setChecked(true);
            player.setVolume(1, 1);

        }
        _none_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                none__audio_check.setChecked(true);
                auto_audio.setChecked(false);
                file_size_txt.setChecked(false);
                sp.setAudioTrack(true);

            }
        });
        auto_audio_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setAudioTrack(false);
                none__audio_check.setChecked(false);
                auto_audio.setChecked(true);
                file_size_txt.setChecked(false);

            }
        });
        eng_stro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                none__audio_check.setChecked(false);
                auto_audio.setChecked(false);
                file_size_txt.setChecked(true);
                int id = player.getAudioSessionId();
            }
        });
        ok_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (sp.getAudioTrack()) {
                    none__audio_check.setChecked(true);
                    player.setVolume(0, 0);
                } else {
                    auto_audio.setChecked(true);
                    player.setVolume(1, 1);

                }
            }
        });
        cancel_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    public void DeleteVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.delete_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        final CheckBox check_box = view.findViewById(R.id.check_box);

        check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = ((CheckBox) v).isChecked();
                if (checked) {
                    check_box.setChecked(true);

                } else {
                    check_box.setChecked(false);
                }
            }
        });

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (checked) {
                    String vid = String.valueOf(videoArrayList.get(video_index).getVideoUri());
                    File file = new File(vid);
                    boolean status = Utils.moveToPrivate(file, file.getName(), VideoPlayActivity.this, getResources().getString(R.string.recycle));
                    if (status) {
                        sp.setRecoverVideoPath(file.getAbsolutePath());
                        setResult(-1);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Oops! Error Occurred", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "click check box", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void Rename() {
        final File file = new File(String.valueOf(videoArrayList.get(video_index).getVideoUri()));
        link = file.getAbsolutePath();
        AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayActivity.this);
        View view = LayoutInflater.from(VideoPlayActivity.this).inflate(R.layout.rename_file, null);
        final TextView cancel_btn = view.findViewById(R.id.cancel_btn);
        TextView delete_btn = view.findViewById(R.id.delete_btn);
        final EditText rename_txt = view.findViewById(R.id.rename_txt);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        String videoName = Utils.getFileTitleFromFileName(file.getName());
        rename_txt.setText(videoName);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String name = rename_txt.getText().toString();
                if (name.isEmpty() || name.equals("")) {
                    rename_txt.setError("please enter name");
                } else {
                    String filepath = link.substring(0, link.lastIndexOf("/"));
                    String videoName = file.getName();
                    String extension = videoName.substring(videoName.lastIndexOf("."));
                    String newPath = filepath + File.separator + rename_txt.getText().toString() + extension;
                    File file = new File(link);
                    boolean status = Utils.renameFile(file, name + extension, VideoPlayActivity.this);

                    if (status) {
                        setResult(-1);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }

    @Override
    public boolean onDown(MotionEvent e) {

        firstScroll = true;

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent e2, float distanceX, float distanceY) {
        float deltaX = motionEvent.getRawX() - e2.getRawX();
        float deltaY = motionEvent.getRawY() - e2.getRawY();
        int y = (int) e2.getRawY();
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
                if (deltaX < 0) {
                    seekForWard();
                } else {
                    seekBackWard();
                }

            }
        } else {
            if (Math.abs(deltaY) > SWIPE_THRESHOLD) {

                if (motionEvent.getX() < getDeviceWidth(VideoPlayActivity.this) * 1.0 / 2) {

                    updateBrightness(deltaY / getDeviceHeight(VideoPlayActivity.this));
                } else if (motionEvent.getX() > getDeviceWidth(VideoPlayActivity.this) * 1.0 / 2) {

                    updateVolume(deltaY / getDeviceHeight(VideoPlayActivity.this));
                }
            }

        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                mCurVolume = -1;
                mCurBrightness = -1;
                vol_temp.setVisibility(View.GONE);
                bright_temp.setVisibility(View.GONE);
            default:
                if (mDetector != null)
                    mDetector.onTouchEvent(event);
        }
        return false;

    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    private String stringToTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void updateVolume(float percent) {

        vol_temp.setVisibility(View.VISIBLE);

        if (mCurVolume == -1) {
            mCurVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mCurVolume < 0) {
                mCurVolume = 0;
            }
        }
        int volume = (int) (percent * mMaxVolume) + mCurVolume;
        if (volume > mMaxVolume) {
            volume = mMaxVolume;
        }

        if (volume < 0) {
            volume = 0;
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);

        int progress = volume * 100 / mMaxVolume;
        volume_sek.setProgress(progress);
        volume_count.setText("" + progress);
    }

    private void updateBrightness(float percent) {
        Log.e("Bright_ness", "" + percent);
        bright_temp.setVisibility(View.VISIBLE);
        if (mCurBrightness == -1) {
            mCurBrightness = getWindow().getAttributes().screenBrightness;
            if (mCurBrightness <= 0.01f) {
                mCurBrightness = 0.01f;
            }
        }
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = mCurBrightness + percent;
        if (attributes.screenBrightness >= 1.0f) {
            attributes.screenBrightness = 1.0f;
        } else if (attributes.screenBrightness <= 0.01f) {
            attributes.screenBrightness = 0.01f;
        }
        getWindow().setAttributes(attributes);

        float p = attributes.screenBrightness * 100;
        bright_sek.setProgress((int) p);
        bright_count.setText("" + (int) p);
    }

    private void seekForWard() {
        if (videoView == null) {
            return;
        }

        int pos = videoView.getCurrentPosition();
        pos += PROGRESS_SEEK;
        videoView.seekTo(pos);
        setSeekProgress();
    }

    private void seekBackWard() {
        if (videoView == null) {
            return;
        }

        int pos = videoView.getCurrentPosition();
        pos -= PROGRESS_SEEK;
        videoView.seekTo(pos);
        setSeekProgress();


    }

    private int setSeekProgress() {
        if (videoView == null) {
            return 0;
        }

        int position = videoView.getCurrentPosition();
        int duration = videoView.getDuration();
        if (seekBar != null) {
            if (duration > 0) {

                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }

            int percent = videoView.getBufferPercentage();

            seekBar.setSecondaryProgress(percent * 10);
        }

        if (total != null)
            total.setText(stringToTime(duration));
        if (current != null) {

            current.setText(stringToTime(position));
            if (videoView.isPlaying()) {
                current.setText(stringToTime(duration));
            }
        }

        return position;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        int heigh = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, getResources().getDisplayMetrics());

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = heigh;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            relativeLayout.setLayoutParams(params);
            relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
            // video_lay.requestLayout();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            relativeLayout.requestLayout();
        }

        super.onConfigurationChanged(newConfig);
    }

    public void shareVideo(File file) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/mp4");
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", file);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Video Title");
        startActivity(Intent.createChooser(sharingIntent, "Share Video!"));

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
            String id = String.valueOf(videoArrayList.get(video_index));
            stopPosition = player.getCurrentPosition();
            sp.setResume(id, stopPosition);


        }

    }
}
