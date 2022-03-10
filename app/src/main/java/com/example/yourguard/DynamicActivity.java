package com.example.yourguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DynamicActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView timeTv;   //  时间文本
    private TextView bpTv;    //   血压文本
    private TextView rateTv;   // 心率文本
    private Button Dynamic_start;   // 开始按钮
    private Button clearBtn;    // 重置按钮
    private TextView nextTimeTv;    // 下次测量时间
    private LinearLayout predictTime;   // 预计下次
    private LinearLayout measuringLo;   // 测量中
    private ImageView resultImg;    // 结果表

    private  Paint paint = new Paint();
    private  Canvas canvas;
    private  Bitmap baseBitmap;

    private Boolean ifStop = true;
    private Boolean isWaiting = false;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (!ifStop && !isWaiting) {
                        ResetAll();
                        Dynamic_start.setClickable(false);
                        Thread Thread1 = new Thread(new Runnable() {  // 开启一个线程倒计时
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                    handler.sendEmptyMessage(2);
                            }
                        });
                        Thread1.start();
                    }
                    break;
                case 2:
                    Random r = new Random();
                    int sbp = r.nextInt(140 - 100) + 100;
                    int dbp = r.nextInt(100 - 90) + 90;
                    int rate = r.nextInt(90 - 65) + 65;
                    SetResult(sbp, dbp, rate);
                    DrawPoint(sbp, dbp);
                    Dynamic_start.setClickable(true);
                    handler.sendEmptyMessage(3);
                    break;
                case 3:
                    isWaiting = true;
                    Thread Thread2 = new Thread(new Runnable() {  // 开启一个线程倒计时
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            }catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isWaiting = false;
                            handler.sendEmptyMessage(1);
                        }
                    });
                    Thread2.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);

        // 隐藏标题栏
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        // 初始化控件
        // 退出按钮
        LinearLayout exit = findViewById(R.id.dynamic_exit);
        exit.setOnClickListener(this);
        Dynamic_start = findViewById(R.id.dynamic_start_btn);
        Dynamic_start.setOnClickListener(this);
        clearBtn = findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this);
        timeTv = findViewById(R.id.dynamic_time);
        bpTv = findViewById(R.id.dynamic_bp_tx);
        rateTv = findViewById(R.id.dynamic_rate_tx);
        nextTimeTv = findViewById(R.id.dynamic_next_time);
        predictTime = findViewById(R.id.dynamic_next_lo);
        measuringLo = findViewById(R.id.dynamic_measuring_lo);

        // 加载图像信息
        resultImg = findViewById(R.id.iv_canvas);
        paint.setStrokeWidth(6);
        paint.setColor(Color.parseColor("#707070"));
        resultImg.post(new Runnable() {
            @Override
            public void run() {
                baseBitmap = Bitmap.createBitmap(resultImg.getWidth(),
                        resultImg.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(baseBitmap);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dynamic_exit:
                Intent intent = new Intent(DynamicActivity.this, MainActivity.class);
                startActivity(intent);
                DynamicActivity.this.finish();
                break;
            case R.id.dynamic_start_btn:
                if (!ifStop) {
                    ifStop = true;
                    StopDynamic();
                }else if (ifStop) {
                    ifStop = false;
                    StartDynamic();
                }
                break;
            case R.id.clear_btn:
                clearAll();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DynamicActivity.this, MainActivity.class);
        startActivity(intent);
        DynamicActivity.this.finish();
    }


    private void StartDynamic() {
        ResetAll();
        Dynamic_start.setText("停止");
        handler.sendEmptyMessage(1);
    }

    private void StopDynamic() {
        Dynamic_start.setText("开始");
    }

    @SuppressLint("SetTextI18n")
    private void ResetAll() {
        String noneData = "__";
        bpTv.setText(noneData + " / " + noneData);
        rateTv.setText(noneData);
        predictTime.setVisibility(View.GONE);
        measuringLo.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void clearAll() {
        String noneData = "__";
        bpTv.setText(noneData + " / " + noneData);
        rateTv.setText(noneData);
        if (baseBitmap != null) {
            baseBitmap = Bitmap.createBitmap(resultImg.getWidth(), resultImg.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmap);
            resultImg.setImageBitmap(baseBitmap);
        }
    }

    private void DrawPoint(int sbp, int dbp) {
        // 图像位置信息
        int width = resultImg.getWidth();
        int height = resultImg.getHeight();
        float cx = (float) ((0.4412 * (sbp - 60) + 7.725) / 96.262 * width);
        float cy = (float) ((0.49254444 * (130 - dbp) + 11.635) / 62.193 * height);
        Log.d("lxy", "caculate_W = " + width + ", caculate_H = " + height + ", cx = " + cx + ", cy = " + cy);
//        canvas.drawCircle(cx, cy, 5, paint);
        canvas.drawRect(cx-4, cy-4, cx+4, cy+4, paint);
        resultImg.setImageBitmap(baseBitmap);
    }

    @SuppressLint("SetTextI18n")
    private void SetResult(int sbp, int dbp, int rate) {
        setTime();
        bpTv.setText(sbp+" / "+dbp);
        rateTv.setText(rate+"");
        Dynamic_start.setClickable(true);
        predictTime.setVisibility(View.VISIBLE);
        measuringLo.setVisibility(View.GONE);
    }

    @SuppressLint("SimpleDateFormat")
    private void setTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String sim = dateFormat.format(date);
        timeTv.setText(sim);
        Date afterDate = new Date(date.getTime() + 900000);
        dateFormat = new SimpleDateFormat("HH:mm");
        sim = dateFormat.format(afterDate);
        nextTimeTv.setText(sim);
    }
}
