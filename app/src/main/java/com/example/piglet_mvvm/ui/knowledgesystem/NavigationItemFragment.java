package com.example.piglet_mvvm.ui.knowledgesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.NavigationBean;
import com.example.piglet_mvvm.databinding.FragmentNavigationItemBinding;
import com.example.piglet_mvvm.databinding.FragmentSystemItemBinding;
import com.example.piglet_mvvm.ui.adapter.NavigationAdapter;
import com.example.piglet_mvvm.ui.webview.WebViewActivity;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.wzq.mvvmsmart.event.StateLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/18
 */
public class NavigationItemFragment extends BaseFragment<FragmentNavigationItemBinding, KnowledgeSystemViewModel> {

    List<NavigationBean> datas = new ArrayList<>();
    private NavigationAdapter navigationAdapter;
    private ShowLoadingDialog showLoadingDialog;

    @Override
    public KnowledgeSystemViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(KnowledgeSystemViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(getContext());
        showLoadingDialog.Show();
        viewModel.requestNavigationData();
    }

    @Override
    public void initViewObservable() {
        viewModel.stateLiveDataNavigation.observe(this,navigationBeans -> {
            datas = navigationBeans;
            binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rv.setItemViewCacheSize(datas.size());
            navigationAdapter = new NavigationAdapter(getContext(),datas);
            navigationAdapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
                @Override
                public void onClick(ArticleBean bean, int pos) {
                    Intent intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra("link",bean.getLink());
                    intent.putExtra("title",bean.getTitle());
                    startActivity(intent);
                }
            });
            binding.rv.setAdapter(navigationAdapter);
        });

        viewModel.stateLiveDataNavigation.stateEnumMutableLiveData
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
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_navigation_item;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


}
