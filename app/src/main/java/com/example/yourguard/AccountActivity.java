package com.example.yourguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        LinearLayout exit = findViewById(R.id.account_setting_exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
        startActivity(intent);
        AccountActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_setting_exit:
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
                AccountActivity.this.finish();
                break;
        }
    }
}
