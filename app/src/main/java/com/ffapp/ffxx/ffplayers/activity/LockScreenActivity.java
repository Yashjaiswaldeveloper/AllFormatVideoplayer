package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView fill_btn1,fill_btn,fill_btn2,fill_btn3;
    RelativeLayout one_btn,two_btn,three_btn,four_btn,five_btn,six_btn,seven_btn,eight_btn,nine_btn,lock_btn,zero_btn,clear_btn;
    String num="";
    EditText edit_text;
    SharePreferencess sp;
    android.os.Vibrator Vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        edit_text=findViewById(R.id.edit_text);
        sp=new SharePreferencess(this);
        Vibrator = (android.os.Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);
        init();
        edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (s.length()) {
                    case 4:
                        fill_btn3.setImageResource(R.drawable.circle2);
                        break;
                    case 3:
                        fill_btn3.setImageResource(R.drawable.circle);
                        fill_btn2.setImageResource(R.drawable.circle2);
                        break;
                    case 2:
                        fill_btn2.setImageResource(R.drawable.circle);
                        fill_btn1.setImageResource(R.drawable.circle2);
                        break;
                    case 1:
                        fill_btn1.setImageResource(R.drawable.circle);
                        fill_btn.setImageResource(R.drawable.circle2);
                        break;
                    default:
                        fill_btn.setImageResource(R.drawable.circle);
                }
                String obj = edit_text.getText().toString();
                if(obj.length()<4)
                {

                }
                else
                {
                    if(obj.equals(sp.getPassword()))
                    {
                       sp.setlogin(true);
                      startActivity(new Intent(getApplicationContext(),UnLockFileActivity.class));
                      finish();
                    }
                    else
                    {
                        Vibrator.vibrate(100);
                         num="";
                        fill_btn.setImageResource(R.drawable.circle);
                        fill_btn1.setImageResource(R.drawable.circle);
                        fill_btn2.setImageResource(R.drawable.circle);
                        fill_btn3.setImageResource(R.drawable.circle);
                    }
                }
            }
        });
    }
    public void init()
    {
        one_btn=findViewById(R.id.one_btn);
        one_btn.setOnClickListener(this);
        two_btn=findViewById(R.id.two_btn);
        two_btn.setOnClickListener(this);
        three_btn=findViewById(R.id.three_btn);
        three_btn.setOnClickListener(this);
        four_btn=findViewById(R.id.four_btn);
        four_btn.setOnClickListener(this);
        five_btn=findViewById(R.id.five_btn);
        five_btn.setOnClickListener(this);
        six_btn=findViewById(R.id.six_btn);
        six_btn.setOnClickListener(this);
        seven_btn=findViewById(R.id.seven_btn);
        seven_btn.setOnClickListener(this);
        eight_btn=findViewById(R.id.eight_btn);
        eight_btn.setOnClickListener(this);
        nine_btn=findViewById(R.id.nine_btn);
        nine_btn.setOnClickListener(this);
        zero_btn=findViewById(R.id.zero_btn);
        zero_btn.setOnClickListener(this);
        clear_btn=findViewById(R.id.clear_btn);
        clear_btn.setOnClickListener(this);
        fill_btn1=findViewById(R.id.fill_btn1);
        fill_btn=findViewById(R.id.fill_btn);
        fill_btn2=findViewById(R.id.fill_btn2);
        fill_btn3=findViewById(R.id.fill_btn3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.one_btn:
                num +=1;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.two_btn:
                num +=2;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.three_btn:
                num +=3;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.four_btn:
                num +=4;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.five_btn:
                num +=5;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.six_btn:
                num +=6;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.seven_btn:
                num +=7;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.eight_btn:
                break;
            case R.id.nine_btn:
                num +=8;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.zero_btn:
                num +=9;
                edit_text.setText(num);
                edit_text.setSelection(edit_text.getText().length());
                break;
            case R.id.clear_btn:
                String obj = edit_text.getText().toString();
                if(obj.length()>0)
                {
                    num = obj.substring(0, obj.length() - 1);
                    edit_text.setText(obj.substring(0, obj.length() - 1));
                    edit_text.setSelection(this.edit_text.getText().length());
                }
                break;
        }

    }
}
