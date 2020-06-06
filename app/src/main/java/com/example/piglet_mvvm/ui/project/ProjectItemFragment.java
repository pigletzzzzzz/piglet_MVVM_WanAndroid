package com.example.piglet_mvvm.ui.project;

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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.databinding.FragmentProjectBinding;
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
 * Created Time 2020/5/27
 */
public class ProjectItemFragment extends BaseFragment<FragmentProjectBinding,ProjectViewModel> {

    private int id;
    private ProjectAdapter projectAdapter;
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
        Fragment fragment= new ProjectItemFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(getContext());
        showLoadingDialog.Show();
        getIDData();
        viewModel.requestProjectArticleListData(id);
        initView();
    }

    private void initView(){
        projectAdapter = new ProjectAdapter(R.layout.project_item,articleBeanList);
        binding.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.setAdapter(projectAdapter);

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

        projectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                articleBean = articleBeanList.get(position);
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra("link",articleBean.getLink());
                intent.putExtra("title",articleBean.getTitle());
                startActivity(intent);
            }
        });

        projectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
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
                                viewModel.requestProjectArticleListData(id);
                            }
                        });

                viewModel.stateLiveUnCollData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                viewModel.requestProjectArticleListData(id);
                            }
                        });

                viewModel.stateArticleLiveData.stateEnumMutableLiveData
                        .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                            @Override
                            public void onChanged(StateLiveData.StateEnum stateEnum) {
                                projectAdapter.notifyItemChanged(position);
                            }
                        });
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.stateArticleLiveData.observe(this,articleBeans -> {
            articleBeanList = articleBeans;
            projectAdapter.setNewData(articleBeanList);
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum++;
                viewModel.requestProjectArticleListData(id);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.pageNum = 1;
                viewModel.requestProjectArticleListData(id);
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
    public ProjectViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(ProjectViewModel.class);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_project;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
