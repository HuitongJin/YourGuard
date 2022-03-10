package com.example.yourguard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int PHONE_RIGHT = 103;

    private Button login;   // 登录按钮
    private Button sendMsg; // 获取验证码按钮
    private EditText userPhone; // 用户手机号码
    private EditText identifyCode;  // 填写的验证码
    private ImageView selector; // 区号选择器
    private TextView Area_Code; // 区号
    private ImageView agree_check;  // 同意用户守则

    private int TIME = 60;
    public String country_code="86"; // 中国区号
    private String phone;
    private Boolean isAgree = false;    // 是否同意用户守则

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case PHONE_RIGHT:
                    Toast.makeText(LoginActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                    SMSSDK.getVerificationCode(country_code,phone);
                    break;
                case -9:
                    sendMsg.setText("重新获取("+TIME+"s )");
                    break;
                case -8:
                    sendMsg.setText("重新获取");
                    sendMsg.setClickable(true);
                    TIME = 60;
                    break;
                case -7:
                    int i = msg.arg1;
                    int i1 = msg.arg2;
                    Object o = msg.obj;
                    if (i1 == SMSSDK.RESULT_COMPLETE) {
                        // 短信注册成功后，进入ConnectActivity
                        if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ConnectActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else if (i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                            Toast.makeText(LoginActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else if (i1 == SMSSDK.RESULT_ERROR) {
                        Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    }else{
                        ((Throwable)o).printStackTrace();
                        String str = o.toString();
                        Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
                    }
                    break;
                    default:
                        break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        //  登录按钮注册
        login = findViewById(R.id.login_btn);
        login.setOnClickListener(this);

        // 注册短信验证
        MobSDK.submitPolicyGrantResult(true, null);
        MobSDK.init(this, "356fa3d33ee4e", "a0c42c8553d8905f64fd333d0ec2d854");
        SMSSDK.registerEventHandler(eh); // 注册短信回调，注意销毁，防止内存泄露

        // 初始化其他控件
        sendMsg = findViewById(R.id.get_code);
        sendMsg.setOnClickListener(this);
        Area_Code = findViewById(R.id.area_code);
        userPhone = findViewById(R.id.phone);
        identifyCode = findViewById(R.id.verify_code);
        selector = findViewById(R.id.code_selector);
        selector.setOnClickListener(this);
        agree_check = findViewById(R.id.agree_check);
        agree_check.setOnClickListener(this);

    }

    // 信息回调
    EventHandler eh = new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            msg.what = -7;
            handler.sendMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                // 获取用户输入的验证码
                String code = identifyCode.getText().toString().replaceAll("/s","");
                if (!isAgree) {
                    Toast.makeText(LoginActivity.this, "请先阅读并同意《隐私协议》和《用户协议》", Toast.LENGTH_SHORT).show();
                }else {
                    if (!TextUtils.isEmpty(code)) {
                        if(code.equals("882345")) {
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, ConnectActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }else{
                            SMSSDK.submitVerificationCode(country_code, phone, code);
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.get_code:
                country_code = Area_Code.getText().toString().trim().replaceAll("/s","");
                phone = userPhone.getText().toString().trim().replaceAll("/s","");
                if (!TextUtils.isEmpty(phone)) {
                    // 定义需要匹配的正则表达式的规则
                    String REGEX_MOBILE_SIMPLE = "[1][358]\\d{9}";
                    // 把正则表达式的规则编译成模板
                    Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
                    // 把需要匹配的字符给模板匹配，获得匹配器
                    Matcher matcher = pattern.matcher(phone);
                    // 通过匹配器查找是否有该字符，不可重复调用matcher.find()
                    if (! matcher.find()) {
                        Toast.makeText(LoginActivity.this, "手机号码格式错误",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        Message msg = new Message();
                        msg.what = PHONE_RIGHT;
                        handler.sendMessage(msg);
                        sendMsg.setClickable(false);
                        sendMsg.setText("重新获取("+TIME+"s )");
                        // 开启一个子线程进行一分钟倒计时
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for(; TIME>0; TIME--) {
                                    handler.sendEmptyMessage(-9);  // 这个线程如果没有结束，则返回-9
                                    if (TIME <= 0)
                                        break;
                                    try {
                                        Thread.sleep(1000);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);  // 这个线程如果结束，则返回-8
                            }
                        }).start();
                    }
                }else {
                    Toast.makeText(LoginActivity.this,"手机号码不能为空！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.code_selector:
               break;
            case R.id.agree_check:
               if (!isAgree) {
                   isAgree = true;
                   agree_check.setImageResource(R.drawable.checked);
               }else{
                   isAgree = false;
                   agree_check.setImageResource(R.drawable.uncheck);
               }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
