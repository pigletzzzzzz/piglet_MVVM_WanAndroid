package com.example.piglet_mvvm.ui.my.aboutTheAuthor;

import android.os.Bundle;
import android.view.View;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.databinding.ActivityAboutAuthorBinding;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/4
 */
public class AboutTheAuthorActivity extends BaseActivity<ActivityAboutAuthorBinding, BaseViewModel> {

    @Override
    public void initData() {
        super.initData();
        binding.lltoolbar.ivRightIcon.setVisibility(View.GONE);
        binding.lltoolbar.tvTitle.setText("说明");
        binding.lltoolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_about_author;
    }

    @Override
    public int initVariableId() {
        return 0;
    }
}
