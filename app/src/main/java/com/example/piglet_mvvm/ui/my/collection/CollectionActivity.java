package com.example.piglet_mvvm.ui.my.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.databinding.FragmentCollectionBinding;
import com.example.piglet_mvvm.ui.my.MyViewModel;
import com.example.piglet_mvvm.ui.webview.WebViewActivity;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.event.StateLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/31
 */
public class CollectionActivity extends BaseActivity<FragmentCollectionBinding, MyViewModel> {

    private List<ArticleBean> articleBeanList = new ArrayList<>();
    private CollectionAdapter collectionAdapter;
    private ArticleBean articleBean = new ArticleBean();
    private ShowLoadingDialog showLoadingDialog;

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(this);
        showLoadingDialog.Show();
        viewModel.getCollectionData();
        initView();
    }

    private void initView() {
        collectionAdapter = new CollectionAdapter(R.layout.home_item,articleBeanList);
        binding.setLayoutManager(new LinearLayoutManager(this));
        binding.setAdapter(collectionAdapter);

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
    public void initViewObservable() {
        super.initViewObservable();
        binding.lltoolbar.ivRightIcon.setVisibility(View.GONE);
        binding.lltoolbar.tvTitle.setText("文章收藏");

        viewModel.stateLiveCollectionData.observe(this,articleBeans -> {
            articleBeanList = articleBeans;
            collectionAdapter.setNewData(articleBeanList);
        });

        viewModel.stateLiveCollectionData.stateEnumMutableLiveData
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
                viewModel.CollpageNum++;
                viewModel.getCollectionData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.CollpageNum = 0;
                viewModel.getCollectionData();
            }
        });

        collectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = articleBeanList.get(position);
                Intent intent = new Intent(CollectionActivity.this, WebViewActivity.class);
                intent.putExtra("link",articleBean.getLink());
                intent.putExtra("title",articleBean.getTitle());
                startActivity(intent);
            }
        });

        collectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = articleBeanList.get(position);
                viewModel.unCollectionArticle(articleBean.getId(),articleBean.getOriginId());

                viewModel.stateLiveNewUnCollData.stateEnumMutableLiveData
                        .observe(CollectionActivity.this, new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    viewModel.deleteItem(articleBean);
                                    collectionAdapter.notifyItemRemoved(position);
                                }
                            }
                        });
            }
        });

        binding.lltoolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        return R.layout.fragment_collection;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
