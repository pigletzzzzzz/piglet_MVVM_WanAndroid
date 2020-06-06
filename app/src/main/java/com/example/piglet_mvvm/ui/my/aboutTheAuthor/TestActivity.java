package com.example.piglet_mvvm.ui.my.aboutTheAuthor;

import android.os.Bundle;
import android.view.View;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.databinding.ActivityTestBinding;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/5
 */
public class TestActivity extends BaseActivity<ActivityTestBinding, BaseViewModel> {

    @Override
    public void initData() {
        super.initData();
        binding.lltoolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_test;
    }

    @Override
    public int initVariableId() {
        return 0;
    }
}
