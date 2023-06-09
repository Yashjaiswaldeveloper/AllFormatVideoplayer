package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ffapp.ffxx.ffplayers.BuildConfig;
import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.adapter.NavigationMenuAdapter;
import com.ffapp.ffxx.ffplayers.comman.ModelMenu;
import com.ffapp.ffxx.ffplayers.comman.NavigationClickListener;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.ffapp.ffxx.ffplayers.fragement.FolderVideoFragment;
import com.ffapp.ffxx.ffplayers.fragement.GalleryFragment;
import com.ffapp.ffxx.ffplayers.fragement.WhateAppStatusFragment;
import com.pesonal.adsdk.AppManage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationClickListener {
    CardView all_video_folder, all_video, status_video, Gallery_image, filter_video, timeline_video, Lock_video_btn, recycle_btn;
    String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS};
    int PERMISSION_ALL = 303;
    SharePreferencess sp;
    RelativeLayout navi_btn, folder_btn, gall_btn, status_btn, short_btn;
    DrawerLayout draw_layout;
    NavigationView navigationView;
    RecyclerView recycle_view;
    TextView title_txt,privacybtn;
    ArrayList<ModelMenu> modelMenus = new ArrayList<>();
    ImageView img_btn, gall_img, status_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
        }
        sp = new SharePreferencess(this);
        draw_layout = findViewById(R.id.draw_layout);
        navigationView = findViewById(R.id.navigationView);
        navi_btn = findViewById(R.id.navi_btn);
        folder_btn = findViewById(R.id.folder_btn);
        gall_btn = findViewById(R.id.gall_btn);
        status_btn = findViewById(R.id.status_btn);
        title_txt = findViewById(R.id.title_txt);
        title_txt.setText("All Format Video player 2022");
        all_video_folder = findViewById(R.id.all_video_folder);
        all_video = findViewById(R.id.all_video);
        status_video = findViewById(R.id.status_video);
        Gallery_image = findViewById(R.id.Gallery_image);
        filter_video = findViewById(R.id.filter_video);
        timeline_video = findViewById(R.id.timeline_video);
        Lock_video_btn = findViewById(R.id.Lock_video_btn);
        recycle_btn = findViewById(R.id.recycle_btn);
        short_btn = findViewById(R.id.short_btn);
        recycle_view = findViewById(R.id.recycle_view);
        img_btn = findViewById(R.id.img_btn);
        privacybtn = findViewById(R.id.privacybtn);
        gall_img = findViewById(R.id.gall_img);
        status_img = findViewById(R.id.status_img);



        draw_layout.closeDrawers();

        img_btn.setBackgroundResource(R.drawable.folder_bg);
        img_btn.setImageResource(R.drawable.ic_play);
        nevigationMenu();
        setFragment(new FolderVideoFragment(MediaStore.Video.Media.DISPLAY_NAME));
        navi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_layout.openDrawer(Gravity.LEFT);
            }
        });

        folder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                img_btn.setBackgroundResource(R.drawable.folder_bg);
                img_btn.setImageResource(R.drawable.ic_play);

                status_img.setBackgroundResource(0);
                status_img.setImageResource(R.drawable.ic_status);
                status_img.setColorFilter(null);

                gall_img.setBackgroundResource(0);
                gall_img.setImageResource(R.drawable.ic_gallery);
                gall_img.setColorFilter(null);

                short_btn.setVisibility(View.VISIBLE);

                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        setFragment(new FolderVideoFragment(MediaStore.Video.Media.DISPLAY_NAME));
                    }
                });
                title_txt.setText("Sax Video Player");
            }
        });

        gall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gall_img.setBackgroundResource(R.drawable.folder_bg);
                gall_img.setImageResource(R.drawable.ic_gallery);
                gall_img.setColorFilter(getResources().getColor(R.color.white));

                img_btn.setBackgroundResource(0);
                img_btn.setImageResource(R.drawable.ic_player);

                status_img.setBackgroundResource(0);
                status_img.setImageResource(R.drawable.ic_status);
                status_img.setColorFilter(null);

                short_btn.setVisibility(View.GONE);

                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        setFragment(new GalleryFragment(MediaStore.Video.Media.DISPLAY_NAME));
                    }
                });

                title_txt.setText("Gallery");
            }
        });

        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status_img.setBackgroundResource(R.drawable.folder_bg);
                status_img.setImageResource(R.drawable.ic_status);
                status_img.setColorFilter(getResources().getColor(R.color.white));


                gall_img.setBackgroundResource(0);
                gall_img.setImageResource(R.drawable.ic_gallery);
                gall_img.setColorFilter(null);

                img_btn.setBackgroundResource(0);
                img_btn.setImageResource(R.drawable.ic_player);

                short_btn.setVisibility(View.GONE);
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        setFragment(new WhateAppStatusFragment());
                    }
                });

                title_txt.setText("Whatsapp Status");
            }
        });

        short_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, short_btn);
                popup.getMenuInflater().inflate(R.menu.sort_refersh, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.name:
                                setFragment(new FolderVideoFragment(MediaStore.Video.Media.DISPLAY_NAME));
                                break;
                            case R.id.date:
                                setFragment(new FolderVideoFragment(MediaStore.Video.Media.DATE_TAKEN));
                                break;
                            case R.id.size:
                                setFragment(new FolderVideoFragment(MediaStore.Video.Media.SIZE));
                                break;
                            case R.id.as_duration:
                                setFragment(new FolderVideoFragment(MediaStore.Video.Media.DURATION));
                                break;
                            case R.id.ascending:
                                setFragment(new FolderVideoFragment(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });


        all_video_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        all_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        status_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StatusActivity.class));
            }
        });
        Gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GalleryImageActivity.class));
            }
        });

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (!(Build.VERSION.SDK_INT < 23 || context == null || permissions == null)) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void nevigationMenu() {
        modelMenus.add(new ModelMenu("Video Folder", R.drawable.ic_video_folder));
        modelMenus.add(new ModelMenu("All Video", R.drawable.ic_all_video));
        modelMenus.add(new ModelMenu("Timelines", R.drawable.ic_timeline));
        modelMenus.add(new ModelMenu("Video Guard", R.drawable.iccc_lock));
        modelMenus.add(new ModelMenu("Filter Duplicate", R.drawable.ic_duplicate));
        modelMenus.add(new ModelMenu("Recycler Bin", R.drawable.ic_recycle_bin));
        modelMenus.add(new ModelMenu("Setting", R.drawable.ic_setting));
        modelMenus.add(new ModelMenu("Rate", R.drawable.ic_rate));
        modelMenus.add(new ModelMenu("Share App", R.drawable.ic_share));
        modelMenus.add(new ModelMenu("Exit App", R.drawable.ic_exit_app));
        recycle_view.setLayoutManager(new LinearLayoutManager(this));
        recycle_view.setAdapter(new NavigationMenuAdapter(this, modelMenus, this));
    }


    @Override
    public void OnClick(int pos) {
        switch (pos) {
            case 0:
                short_btn.setVisibility(View.VISIBLE);
                draw_layout.closeDrawers();
                setFragment(new FolderVideoFragment(MediaStore.Video.Media.DISPLAY_NAME));
                title_txt.setText("All Format Video player 2022");
                break;
            case 1:
                draw_layout.closeDrawers();
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), AllVideoActivity.class));
                    }
                });

                break;
            case 2:
                draw_layout.closeDrawers();
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), TimeLineVideoActivity.class));
                    }
                });

                break;
            case 3:
                draw_layout.closeDrawers();
                if (sp.getLogin()) {
                    AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                        public void callbackCall() {
                            startActivity(new Intent(getApplicationContext(), LockScreenActivity.class));
                        }
                    });

                } else {
                    AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                        public void callbackCall() {
                            startActivity(new Intent(getApplicationContext(), LockConfirmActivity.class));
                        }
                    });

                }

                break;
            case 4:
                draw_layout.closeDrawers();
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), FilterDuplicateVideoActivity.class));
                    }
                });
                break;
            case 5:
                draw_layout.closeDrawers();
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), RecycleBinActivity.class));
                    }
                });

                break;
            case 6:
                draw_layout.closeDrawers();
                AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
                    public void callbackCall() {
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    }
                });

                break;
            case 7:
                draw_layout.closeDrawers();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                break;
            case 8:
                draw_layout.closeDrawers();
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "All Format Video player 2022");
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            case 9:
                draw_layout.closeDrawers();
                finishAffinity();
             /*   privacybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoUrl("https://techiemediaadvertising.blogspot.com/2022/06/techiemedia-inc.html");
                    }
                        private void gotoUrl(String s) {
                            Uri uri = Uri.parse(s);
                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                        }

                });
                Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();*/
                break;

        }
    }

    @Override
    public void onBackPressed() {
        AppManage.getInstance(MainActivity.this).showInterstitialAd(MainActivity.this, new AppManage.MyCallback() {
            public void callbackCall() {
                startActivity(new Intent(getApplicationContext(), ThanksActivity.class));
            }
        });


    }
}
