package com.example.piglet_mvvm.ui.my.ranking;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.bean.RankingBean;
import com.example.piglet_mvvm.databinding.FragmentRankingBinding;
import com.example.piglet_mvvm.ui.my.MyViewModel;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.event.StateLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/29
 */
public class RankingActivity extends BaseActivity<FragmentRankingBinding, MyViewModel> {

    private List<RankingBean> rankingBeans = new ArrayList<>();
    private RankingAdapter rankingAdapter;
    private ShowLoadingDialog showLoadingDialog;

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(this);
        showLoadingDialog.Show();
        viewModel.getRankingData();
        initView();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.lltoolbar.ivRightIcon.setVisibility(View.GONE);
        binding.lltoolbar.tvTitle.setText("积分排行");

        viewModel.stateLiveRankingData.observe(this,rankingBeans1 -> {
            rankingBeans = rankingBeans1;
            rankingAdapter.setNewData(rankingBeans);
        });

        viewModel.stateLiveRankingData.stateEnumMutableLiveData
                .observe(this, new Observer<StateLiveData.StateEnum>() {
                    @Override
                    public void onChanged(StateLiveData.StateEnum stateEnum) {
                        if (stateEnum.equals(StateLiveData.StateEnum.Idle)){
                            showLoadingDialog.dismiss();
                        }
                        if (stateEnum.equals(StateLiveData.StateEnum.Error)){
                            showLoadingDialog.dismiss();
                        }
                    }
                });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum++;
                viewModel.getRankingData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum = 1;
                viewModel.getRankingData();
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

        rankingAdapter = new RankingAdapter(R.layout.ranking_item,rankingBeans);
        binding.setLayoutManager(new LinearLayoutManager(this));
        binding.setAdapter(rankingAdapter);

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
        return R.layout.fragment_ranking;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
