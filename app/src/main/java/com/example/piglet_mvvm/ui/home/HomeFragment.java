package com.example.piglet_mvvm.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BannerBean;
import com.example.piglet_mvvm.databinding.FragmentHomeBinding;
import com.example.piglet_mvvm.ui.webview.WebViewActivity;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.squareup.picasso.Picasso;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.http.ResponseThrowable;
import com.wzq.mvvmsmart.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/30
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding,HomeViewModel> {

    private HomeAdapter mAdapter;
    private List<ArticleBean> beans = new ArrayList<>();
    private List<BannerBean> bannerBeans;
    private List<String> urls = new ArrayList<>();;
    private ArticleBean articleBean = new ArticleBean();
    private ShowLoadingDialog showLoadingDialog;

    @Override
    public HomeViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(HomeViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(getContext());
        showLoadingDialog.Show();
        viewModel.requestData();
        viewModel.requestBannerData();
        initRecyclerView();
    }


    private void initRecyclerView() {
        binding.lltoolbar.ivBack.setVisibility(View.GONE);
        binding.lltoolbar.ivRightIcon.setVisibility(View.GONE);
        binding.lltoolbar.tvTitle.setText("玩安卓");
        mAdapter = new HomeAdapter(R.layout.home_item,beans);
        binding.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.setAdapter(mAdapter);

        //监听下拉刷新完成
        viewModel.uc.finishRefreshing.observe(HomeFragment.this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object o) {
                binding.refreshLayout.finishRefresh();    //结束刷新
            }
        });
        //监听上拉加载完成
        viewModel.uc.finishLoadMore.observe(HomeFragment.this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object o) {
                binding.refreshLayout.finishLoadMore();   //结束刷新
            }
        });

        binding.banner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, Object model, View view, int position) {
                BannerBean bannerBean = bannerBeans.get(position);
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",bannerBean.getUrl());
                intent.putExtra("title",bannerBean.getTitle());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = beans.get(position);
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",articleBean.getLink());
                intent.putExtra("title",articleBean.getTitle());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = beans.get(position);
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
                                    //等待收藏接口回调成功后再请求列表数据接口，更新界面
                                    viewModel.requestData();
                                }
                            }
                        });

                viewModel.stateLiveUnCollData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    viewModel.requestData();
                                }
                            }
                        });

                viewModel.stateLiveData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                                    mAdapter.notifyItemChanged(position);
                                }
                            }
                        });
            }
        });

        viewModel.stateLiveData.stateEnumMutableLiveData
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

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort("点击add");
            }
        });
    }

    @Override
    public void initViewObservable() {
        viewModel.stateLiveData.observe(this,articleBeans -> {
            beans = articleBeans;
            mAdapter.setNewData(beans);
        });

        viewModel.stateBannerLiveData.observe(this,bannerBeans1 -> {
            bannerBeans = bannerBeans1;

            for (BannerBean bean : bannerBeans){
                urls.add(bean.getImagePath());
            }
            if (urls != null && urls.size() > 0){
                binding.banner.setData(urls,null);
                binding.banner.loadImage(new XBanner.XBannerAdapter() {
                    @Override
                    public void loadBanner(XBanner banner, Object model, View view, int position) {
                        Picasso.with(getActivity()).load(urls.get(position)).into((ImageView) view);
                    }
                });
            }
            binding.banner.setPageTransformer(Transformer.Default);
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum++;
                viewModel.requestData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum = 0;
                viewModel.requestData();
//                viewModel.requestBannerData();
            }
        });

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_home;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
