package com.example.yourguard;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConnectActivity extends AppCompatActivity {

    private Button connect; // 连接按钮

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        connect = findViewById(R.id.connect_btn);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConnectActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                startActivity(intent);
                ConnectActivity.this.finish();
            }
        });

    }
}
