package com.example.piglet_mvvm.ui.wechat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.bean.WeChatTabListBean;
import com.example.piglet_mvvm.databinding.FragmentProjectTabBinding;
import com.example.piglet_mvvm.ui.adapter.BaseFragmentPagerAdapter;
import com.example.piglet_mvvm.ui.project.ProjectItemFragment;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/28
 */
public class WeChatFragment extends BaseFragment<FragmentProjectTabBinding,WeChatViewModel> {

    private List<Fragment> mFragments;
    private List<String> titlePager;
    List<WeChatTabListBean> datas = new ArrayList<>();
    private int mCurrentItemCount;
    private List<String> pjName = new ArrayList<>();
    private List<Integer> pjIdList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    public WeChatViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(WeChatViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        viewModel.requestData();
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.stateLiveData.observe(this,weChatTabListBeans -> {
            datas = weChatTabListBeans;

            mCurrentItemCount = datas.size();
            for (WeChatTabListBean weChatTabListBean : datas){
                pjName.add(weChatTabListBean.getName());
                pjIdList.add(weChatTabListBean.getId());
            }

            for(int i=0;i<mCurrentItemCount;i++){
                fragmentList.add(WeChatItemFragment.newInstance(pjIdList.get(i)));
            }

            mFragments = fragmentList;
            titlePager = pjName;

            //设置Adapter
            BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragments, titlePager);
            binding.vp.setAdapter(pagerAdapter);
            binding.tabSpl.setViewPager(binding.vp);
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_project_tab;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
