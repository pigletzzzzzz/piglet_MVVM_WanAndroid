package com.example.piglet_mvvm.ui.my.points;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.LevelDataBean;
import com.example.piglet_mvvm.databinding.FragmentPointsBinding;
import com.example.piglet_mvvm.ui.my.MyViewModel;
import com.example.piglet_mvvm.ui.wechat.WeChatItemFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/29
 */
public class MyPointsActivity extends BaseActivity<FragmentPointsBinding, MyViewModel> {

    private String points;
    private List<LevelDataBean> dataBeans = new ArrayList<>();
    private MyPointsAdapter myPointsAdapter;


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        points = intent.getStringExtra("points");

        viewModel.getLevelData();
        initView();

        binding.points.setText(points);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.lltoolbar.ivRightIcon.setVisibility(View.GONE);
        binding.lltoolbar.tvTitle.setText("积分");
        viewModel.stateLiveLevelData.observe(this,levelDataBeans -> {
            dataBeans = levelDataBeans;
            myPointsAdapter.setNewData(dataBeans);
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum++;
                viewModel.getLevelData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum = 1;
                viewModel.getLevelData();
            }
        });

        binding.lltoolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        myPointsAdapter = new MyPointsAdapter(R.layout.points_item,dataBeans);
        binding.setLayoutManager(new LinearLayoutManager(this));
        binding.setAdapter(myPointsAdapter);

        viewModel.uc.finishRefreshing.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                binding.refreshLayout.finishRefresh();
            }
        });

        viewModel.uc.finishLoadMore.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                binding.refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public MyViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(MyViewModel.class);
    }


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_points;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
