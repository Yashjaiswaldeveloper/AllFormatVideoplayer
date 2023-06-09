package com.ffapp.ffxx.ffplayers.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffapp.ffxx.ffplayers.R;
import com.ffapp.ffxx.ffplayers.comman.SharePreferencess;

public class LockConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView fill_btn12,fill_btn11,fill_btn21,fill_btn31;
    ImageView fill_btn122,fill_btn112,fill_btn212,fill_btn312;
    RelativeLayout set_pass,confirm_btn,change_pass;

    RelativeLayout one_btn1,two_btn1,three_btn1,four_btn1,five_btn1,six_btn1,seven_btn1,eight_btn1,nine_btn1,lock_btn1,zero_btn1,clear_btn1;
    RelativeLayout one_btn12,two_btn12,three_btn12,four_btn12,five_btn12,six_btn12,seven_btn12,eight_btn12,nine_btn12,lock_btn12,zero_btn12,clear_btn12;

    String num1="";
    String num2="";
    EditText edit_text,edit_text1,edit_text12;
    TextView forget_pass,title_btn;
    SharePreferencess sp;
    AlertDialog alertDialog;
    Vibrator Vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_confirm);
        edit_text = findViewById(R.id.edit_text);
        edit_text1=findViewById(R.id.edit_text1);
        edit_text12=findViewById(R.id.edit_text12);
        forget_pass=findViewById(R.id.forget_pass);
        title_btn=findViewById(R.id.title_btn);
        change_pass=findViewById(R.id.change_pass);
        set_pass=findViewById(R.id.set_pass);
        confirm_btn=findViewById(R.id.confirm_btn);
        sp=new SharePreferencess(this);
        Vibrator = (android.os.Vibrator)getSystemService(MainActivity.VIBRATOR_SERVICE);
        init1();
        init2 ();
        edit_text1.addTextChangedListener(new TextWatcher() {
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
                        fill_btn31.setImageResource(R.drawable.circle2);
                        break;
                    case 3:
                        fill_btn31.setImageResource(R.drawable.circle);
                        fill_btn21.setImageResource(R.drawable.circle2);
                        break;
                    case 2:
                        fill_btn21.setImageResource(R.drawable.circle);
                        fill_btn11.setImageResource(R.drawable.circle2);
                        break;
                    case 1:
                        fill_btn11.setImageResource(R.drawable.circle);
                        fill_btn12.setImageResource(R.drawable.circle2);
                        break;
                    default:
                        fill_btn12.setImageResource(R.drawable.circle);
                }
                String obj = edit_text1.getText().toString();
                if(obj.length()<4)
                {

                }
                else
                {
                 sp.setPassword(obj);
                 set_pass.setVisibility(View.GONE);
                 confirm_btn.setVisibility(View.VISIBLE);
                }
            }
        });
        edit_text12.addTextChangedListener(new TextWatcher() {
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
                        fill_btn312.setImageResource(R.drawable.circle2);
                        break;
                    case 3:
                        fill_btn312.setImageResource(R.drawable.circle);
                        fill_btn212.setImageResource(R.drawable.circle2);
                        break;
                    case 2:
                        fill_btn212.setImageResource(R.drawable.circle);
                        fill_btn112.setImageResource(R.drawable.circle2);
                        break;
                    case 1:
                        fill_btn112.setImageResource(R.drawable.circle);
                        fill_btn122.setImageResource(R.drawable.circle2);
                        break;
                    default:
                        fill_btn122.setImageResource(R.drawable.circle);
                }
                String obj = edit_text12.getText().toString();
                if(obj.length()<4)
                {

                }
                else
                {
                    if(obj.equals(sp.getPassword()))
                    {
                      startActivity(new Intent(getApplicationContext(),LockScreenActivity.class));
                      finish();
                    }
                    else
                    {
                        Vibrator.vibrate(100);
                        num2="";
                        fill_btn312.setImageResource(R.drawable.circle);
                        fill_btn212.setImageResource(R.drawable.circle);
                        fill_btn112.setImageResource(R.drawable.circle);
                        fill_btn122.setImageResource(R.drawable.circle);

                    }
                }
            }
        });


    }

    public void init1 ()
    {
        one_btn1 = findViewById(R.id.one_btn1);
        one_btn1.setOnClickListener(this);
        two_btn1 = findViewById(R.id.two_btn1);
        two_btn1.setOnClickListener(this);
        three_btn1 = findViewById(R.id.three_btn1);
        three_btn1.setOnClickListener(this);
        four_btn1 = findViewById(R.id.four_btn1);
        four_btn1.setOnClickListener(this);
        five_btn1 = findViewById(R.id.five_btn1);
        five_btn1.setOnClickListener(this);
        six_btn1 = findViewById(R.id.six_btn1);
        six_btn1.setOnClickListener(this);
        seven_btn1 = findViewById(R.id.seven_btn1);
        seven_btn1.setOnClickListener(this);
        eight_btn1 = findViewById(R.id.eight_btn1);
        eight_btn1.setOnClickListener(this);
        nine_btn1 = findViewById(R.id.nine_btn1);
        nine_btn1.setOnClickListener(this);
        zero_btn1 = findViewById(R.id.zero_btn1);
        zero_btn1.setOnClickListener(this);
        clear_btn1 = findViewById(R.id.clear_btn1);
        clear_btn1.setOnClickListener(this);

        fill_btn12 = findViewById(R.id.fill_btn12);
        fill_btn11 = findViewById(R.id.fill_btn11);
        fill_btn21 = findViewById(R.id.fill_btn21);
        fill_btn31 = findViewById(R.id.fill_btn31);

    }
    public void init2 ()
    {
        one_btn12 = findViewById(R.id.one_btn12);
        one_btn12.setOnClickListener(this);
        two_btn12 = findViewById(R.id.two_btn12);
        two_btn12.setOnClickListener(this);
        three_btn12 = findViewById(R.id.three_btn12);
        three_btn12.setOnClickListener(this);
        four_btn12 = findViewById(R.id.four_btn12);
        four_btn12.setOnClickListener(this);
        five_btn12 = findViewById(R.id.five_btn12);
        five_btn12.setOnClickListener(this);
        six_btn12 = findViewById(R.id.six_btn12);
        six_btn12.setOnClickListener(this);
        seven_btn12 = findViewById(R.id.seven_btn12);
        seven_btn12.setOnClickListener(this);
        eight_btn12 = findViewById(R.id.eight_btn12);
        eight_btn12.setOnClickListener(this);
        nine_btn12 = findViewById(R.id.nine_btn12);
        nine_btn12.setOnClickListener(this);
        zero_btn12 = findViewById(R.id.zero_btn12);
        zero_btn12.setOnClickListener(this);
        clear_btn12 = findViewById(R.id.clear_btn12);
        clear_btn12.setOnClickListener(this);

        fill_btn122 = findViewById(R.id.fill_btn122);
        fill_btn112 = findViewById(R.id.fill_btn112);
        fill_btn212 = findViewById(R.id.fill_btn212);
        fill_btn312 = findViewById(R.id.fill_btn312);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.one_btn1:
                num1 += 1 ;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.two_btn1:
                num1 += 2;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.three_btn1:
                num1 += 3;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.four_btn1:
                num1 += 4;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.five_btn1:
                num1 += 5;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.six_btn1:
                num1 += 6;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.seven_btn1:
                num1 += 7;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.eight_btn1:
                num1 += 8;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.nine_btn1:
                num1 += 9;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.zero_btn1:
                num1 += 0;
                edit_text1.setText(num1);
                edit_text1.setSelection(edit_text1.getText().length());
                break;
            case R.id.clear_btn1:
                String obj1 = edit_text1.getText().toString();
                if (obj1.length() > 0) {
                    num1 = obj1.substring(0, obj1.length() - 1);
                    edit_text1.setText(obj1.substring(0, obj1.length() - 1));
                    edit_text1.setSelection(this.edit_text1.getText().length());
                }
                break;


            case R.id.one_btn12:
                num2 += 1 ;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.two_btn12:
                num2 += 2;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.three_btn12:
                num2 += 3;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.four_btn12:
                num2 += 4;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.five_btn12:
                num2 += 5;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.six_btn12:
                num2 += 6;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.seven_btn12:
                num2 += 7;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.eight_btn12:
                num2 += 8;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.nine_btn12:
                num2 += 9;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.zero_btn12:
                num2 += 0;
                edit_text12.setText(num2);
                edit_text12.setSelection(edit_text12.getText().length());
                break;
            case R.id.clear_btn12:
                String obj2 = edit_text12.getText().toString();
                if (obj2.length() > 0) {
                    num2 = obj2.substring(0, obj2.length() - 1);
                    edit_text12.setText(obj2.substring(0, obj2.length() - 1));
                    edit_text12.setSelection(this.edit_text12.getText().length());
                }
                break;
        }
    }
}
