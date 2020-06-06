package com.example.piglet_mvvm.ui;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.piglet_mvvm.R;
import com.wzq.mvvmsmart.crash.CaocConfig;
import com.wzq.mvvmsmart.crash.CustomActivityOnCrash;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/3
 */
public class CrashActivity extends AppCompatActivity implements View.OnClickListener {

    private CaocConfig mCaocConfig;
    private TextView seeLog,tvExit,tvLog,tvRestart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaocConfig = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (mCaocConfig == null) {
            finish();
            return;
        }

        setContentView(R.layout.activity_crash);

        seeLog = findViewById(R.id.see_log);
        tvLog = findViewById(R.id.tv_log);
        tvExit = findViewById(R.id.tv_exit);
        tvRestart = findViewById(R.id.tv_restart);

        seeLog.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        tvRestart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.see_log:
                tvLog.setText(CustomActivityOnCrash.getAllErrorDetailsFromIntent
                        (CrashActivity.this, getIntent()));
                break;
            case R.id.tv_exit:
                CustomActivityOnCrash.closeApplication(CrashActivity.this, mCaocConfig);
                break;
            case R.id.tv_restart:
                CustomActivityOnCrash.restartApplication(CrashActivity.this, mCaocConfig);
                break;
        }
    }
}
