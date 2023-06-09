package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;
import com.pesonal.adsdk.AppManage;

import static com.pesonal.adsdk.AppManage.ADMOB_N;
import static com.pesonal.adsdk.AppManage.FACEBOOK_N;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
RelativeLayout auto_play,recover_pin,change_password,feed_back,orientation_btn,battry_lock_sc,resume_video;
SwitchCompat switch_auto;
TextView resume_vid,rotation,privacybtn;
SharePreferencess sp;
RelativeLayout back_btn,banner_ads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back_btn=findViewById(R.id.back_btn);
        banner_ads=findViewById(R.id.banner_ads);
        privacybtn = findViewById(R.id.privacybtn);
        sp=new SharePreferencess(this);

        privacybtn.setAlpha(0f);
        privacybtn.setTranslationY(50);
        privacybtn.animate().alpha(1f).translationYBy(-50).setDuration(1500);

        //AppManage.getInstance(this).showNative((ViewGroup)banner_ads, ADMOB_N, FACEBOOK_N,this);
        initView();
        Log.e("Auto",""+sp.getAutoPlay());
        if(sp.getAutoPlay())
        {
            switch_auto.setChecked(true);
        }
        else
        {
            switch_auto.setChecked(false);
        }
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        privacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://techiemediaadvertising.blogspot.com/2022/06/techiemedia-inc.html");
            }
        });

    }

    public void initView()
    {
        auto_play=findViewById(R.id.auto_play);
        switch_auto=findViewById(R.id.switch_auto);
        auto_play.setOnClickListener(this);
        recover_pin=findViewById(R.id.recover_pin);
        recover_pin.setOnClickListener(this);
        change_password=findViewById(R.id.change_password);
        change_password.setOnClickListener(this);
        feed_back=findViewById(R.id.feed_back);
        feed_back.setOnClickListener(this);
        orientation_btn=findViewById(R.id.orientation_btn);
        orientation_btn.setOnClickListener(this);
        battry_lock_sc=findViewById(R.id.battry_lock_sc);
        battry_lock_sc.setOnClickListener(this);
        resume_video=findViewById(R.id.resume_video);
        resume_video.setOnClickListener(this);
        resume_vid=findViewById(R.id.resume_vid);
        rotation=findViewById(R.id.rotation);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.auto_play:
                boolean status=switch_auto.isChecked();
                if(status)
                {
                    switch_auto.setChecked(false);
                    sp.setAutoPlay(false);
                }
                else
                {
                    switch_auto.setChecked(true);
                    sp.setAutoPlay(true);
                }
                break;
            case R.id.recover_pin:
                 recoverPin();
                 break;
            case R.id.change_password:
                 startActivity(new Intent(getApplicationContext(),ChangePasswordActivity.class));
                 break;
            case R.id.feed_back:
                 feedback();
                 break;
            case R.id.orientation_btn:
                 orientatioPro();
                 break;
            case R.id.battry_lock_sc:
                 break;
            case R.id.resume_video:
                 ResumeVideo();
                break;
        }

    }

    public void recoverPin() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup =findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.recover_pin, viewGroup, false);
        TextView text_count=dialogView.findViewById(R.id.count_pin);
        ImageView img_view=dialogView.findViewById(R.id.close_btn);
        text_count.setText(sp.getPassword());
        builder.setView(dialogView);
        final AlertDialog  alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 350;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.show();
        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    public void  orientatioPro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup =findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.orientation, viewGroup, false);
        RelativeLayout sencer_btn=dialogView.findViewById(R.id.sencer_btn);
        RelativeLayout land_btn=dialogView.findViewById(R.id.land_btn);
        RelativeLayout portrat_btn=dialogView.findViewById(R.id.portrat_btn);
        ImageView close_btn=dialogView.findViewById(R.id.close_btn);
        final RadioButton Sencer_btn=dialogView.findViewById(R.id.Sencer_btn);
        final RadioButton landscap=dialogView.findViewById(R.id.landscap);
        final RadioButton portrat=dialogView.findViewById(R.id.portrat);
        builder.setView(dialogView);
        final AlertDialog  alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        if(sp.getOrientation().equals("1"))
        {
            portrat.setChecked(true);
            rotation.setText("Portrait");
        }
        else if(sp.getOrientation().equals("2"))
        {
            landscap.setChecked(true);
            rotation.setText("Landscape");
        }
        else if(sp.getOrientation().equals("3"))
        {
            Sencer_btn.setChecked(true);
            rotation.setText("Sensor");
        }
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sencer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sencer_btn.setChecked(true);
                landscap.setChecked(false);
                portrat.setChecked(false);
                sp.setOrientation("3");
            }
        });
        land_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sencer_btn.setChecked(false);
                landscap.setChecked(true);
                portrat.setChecked(false);
                sp.setOrientation("2");
            }
        });
        portrat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sencer_btn.setChecked(false);
                landscap.setChecked(false);
                portrat.setChecked(true);
                sp.setOrientation("1");
            }
        });

    }

    public void feedback()
    {
        String to = "rajaryan1993@gmail.com";
        String subject = "Report Item ID: 19469";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    public void  ResumeVideo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ViewGroup viewGroup =findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.resume_video_sett, viewGroup, false);
        RelativeLayout sencer_btn=dialogView.findViewById(R.id.sencer_btn);
        TextView title=dialogView.findViewById(R.id.title);
        title.setText("Resume Video");
        RelativeLayout land_btn=dialogView.findViewById(R.id.land_btn);
        RelativeLayout portrat_btn=dialogView.findViewById(R.id.portrat_btn);
        ImageView close_btn=dialogView.findViewById(R.id.close_btn);
        final RadioButton yes=dialogView.findViewById(R.id.Sencer_btn);
        final RadioButton no=dialogView.findViewById(R.id.landscap);
        final RadioButton startup=dialogView.findViewById(R.id.portrat);
        yes.setText("Yes");
        no.setText("No");
        startup.setText("Ask at Startup");
        builder.setView(dialogView);
        final AlertDialog  alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        if(sp.getResumeDefault().equals("1"))
        {
            startup.setChecked(true);
            resume_vid.setText("Ask at Startup");
        }
        else if(sp.getResumeDefault().equals("2"))
        {
            no.setChecked(true);
            resume_vid.setText("No");

        }
        else if(sp.getResumeDefault().equals("3"))
        {
            yes.setChecked(true);
            resume_vid.setText("Yes");
        }
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        sencer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes.setChecked(true);
                no.setChecked(false);
                startup.setChecked(false);
                sp.setResumeDefault("3");
            }
        });
        land_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes.setChecked(false);
                no.setChecked(true);
                startup.setChecked(false);
                sp.setResumeDefault("2");
            }
        });
        portrat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yes.setChecked(false);
                no.setChecked(false);
                startup.setChecked(true);
                sp.setResumeDefault("1");
            }
        });

    }
    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
