package com.example.yourguard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class SingleActivity extends AppCompatActivity implements View.OnClickListener {

    final int SINGLE_DIALOG = 0x113;

    private Button start_btn;   // 开始测量按钮
    private LinearLayout measuringLayout;   // 测量loading
    private TextView evaluateTv; // 结果评估文本
    private TextView timeTv;    // 时间文本
    private TextView sbpTv; // 收缩压
    private TextView dbpTv; // 舒张压
    private TextView rateTv;    // 脉搏

    private int TIME = 5;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Random r = new Random();
                    int sbp = r.nextInt(220 - 60) + 60;
                    int dbp = r.nextInt(80 - 65) + 65;
                    int rate = r.nextInt(80 - 65) + 65;
                    SetResult(sbp, dbp, rate);
                default:
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        // 隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // 初始化控件
        // 退出按钮
        LinearLayout exit = findViewById(R.id.single_exit);
        exit.setOnClickListener(this);

        start_btn = findViewById(R.id.single_start_btn);
        start_btn.setOnClickListener(this);

        measuringLayout = findViewById(R.id.measuring_lo);
        evaluateTv = findViewById(R.id.single_result_tx);
        timeTv = findViewById(R.id.single_time);
        sbpTv = findViewById(R.id.single_sbp);
        dbpTv = findViewById(R.id.single_dbp);
        rateTv = findViewById(R.id.single_rate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.single_exit:
                Intent intent = new Intent(SingleActivity.this, MainActivity.class);
                startActivity(intent);
                SingleActivity.this.finish();
                break;
            case R.id.single_start_btn:
                ResetAll();
                // 倒计时线程
                // 开启一个线程倒计时
                // TIME初始化为3
                Thread myThread = new Thread(new Runnable() {  // 开启一个线程倒计时
                    @Override
                    public void run() {
                        for (; TIME > 0; TIME--) {  // TIME初始化为3
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(1);
                    }
                });
                myThread.start();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SingleActivity.this, MainActivity.class);
        startActivity(intent);
        SingleActivity.this.finish();
    }

    private void ResetAll() {
        TIME = 5;
        String noneData = "__";
        sbpTv.setText(noneData);
        dbpTv.setText(noneData);
        rateTv.setText(noneData);

        evaluateTv.setVisibility(View.GONE);
        measuringLayout.setVisibility(View.VISIBLE);
        start_btn.setClickable(false);
    }

    private void SetResult(int sbp, int dbp, int rate) {
        setTime();
        sbpTv.setText(sbp+"");
        dbpTv.setText(dbp+"");
        rateTv.setText(rate+"");
        setEvaluate(sbp, dbp);
        start_btn.setClickable(true);
    }

    private void setEvaluate(int sbp, int dbp) {
        String result = "血压水平正常";
        String color = "#D5E9C9";
        if (60 <= sbp && sbp<= 85 && 40<=dbp && dbp<=60){
            result = "血压水平偏低";
            color = "#7ECECC";
        }else if ((85<=sbp && sbp<=120 && 80<=dbp) || (60 <= sbp && sbp<= 85 && 60<=dbp && dbp<=80)){
            result = "血压水平正常";
            color = "#D5E9C9";
        }else if ((120<=sbp && sbp<=140 && dbp<=90) || (60<=sbp && sbp<=120 && 80<=dbp && dbp<=90)) {
            result = "血压水平偏高";
            color = "#EDEAAB";
        }else if ((140<=sbp && sbp<=160 && dbp<=100) || (60<=sbp && sbp<=140 && 90<=dbp && dbp<=100)) {
            result = "轻度高血压";
            color = "#EAC37A";
        }else if ((160<=sbp && sbp<=180 && dbp<=110) || (60<=sbp && sbp<=160 && 100<=dbp && dbp<=110)) {
            result = "中度高血压";
            color = "#E07839";
        }else if (sbp>=180 || dbp>=110) {
            result = "重度高血压";
            color = "#DD5128";
        }

        evaluateTv.setText(result);
        evaluateTv.setTextColor(Color.parseColor(color));
        measuringLayout.setVisibility(View.GONE);
        evaluateTv.setVisibility(View.VISIBLE);
    }

    private void setTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String sim = dateFormat.format(date);
        timeTv.setText(sim);
    }
}
