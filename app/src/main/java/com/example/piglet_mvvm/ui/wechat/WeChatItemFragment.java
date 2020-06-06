package com.example.piglet_mvvm.ui.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.databinding.FragmentWechatBinding;
import com.example.piglet_mvvm.ui.webview.WebViewActivity;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/28
 */
public class WeChatItemFragment extends BaseFragment<FragmentWechatBinding,WeChatViewModel> {

    private int id;
    private WeChatAdapter weChatAdapter;
    private List<ArticleBean> articleBeanList = new ArrayList<>();
    private ArticleBean articleBean = new ArticleBean();
    private ShowLoadingDialog showLoadingDialog;

    /**
     * 获取id
     */
    private void getIDData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt("id", -1);
            KLog.e("mCurrentItemCount"+"getData,"+id);
        }
    }

    public static Fragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        Fragment fragment= new WeChatItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(getContext());
        showLoadingDialog.Show();
        getIDData();
        viewModel.requestWeChatArticleData(id);
        iniView();
    }

    private void iniView(){
        weChatAdapter = new WeChatAdapter(R.layout.wechat_item,articleBeanList);
        binding.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.setAdapter(weChatAdapter);

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

        weChatAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = articleBeanList.get(position);
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",articleBean.getLink());
                intent.putExtra("title",articleBean.getTitle());
                startActivity(intent);
            }
        });

        weChatAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = articleBeanList.get(position);
                if (!articleBean.isCollect()){
                    viewModel.collectionArticle(articleBean.getId());
                }else {
                    viewModel.unCollectionArticle(articleBean.getId());
                }

                viewModel.stateLiveCollData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    //等待收藏接口回传成功在调用列表接口，更新数据
                                    viewModel.requestWeChatArticleData(id);
                                }
                            }
                        });

                viewModel.stateLiveUnCollData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    viewModel.requestWeChatArticleData(id);
                                }
                            }
                        });

                viewModel.stateArticleLiveData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    weChatAdapter.notifyItemChanged(position);
                                }
                            }
                        });
            }
        });

        viewModel.stateArticleLiveData.stateEnumMutableLiveData
                .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
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
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.stateArticleLiveData.observe(this,articleBeans -> {
            articleBeanList = articleBeans;
            weChatAdapter.setNewData(articleBeanList);
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum++;
                viewModel.requestWeChatArticleData(id);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum = 1;
                viewModel.requestWeChatArticleData(id);
            }
        });
    }

    @Override
    public WeChatViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(WeChatViewModel.class);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_wechat;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
