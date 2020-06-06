package com.example.piglet_mvvm.ui.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.databinding.FragmentKnowledgeSystemBinding;
import com.example.piglet_mvvm.databinding.FragmentProjectTabBinding;
import com.example.piglet_mvvm.ui.adapter.BaseFragmentPagerAdapter;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/27
 */
public class ProjectFragment extends BaseFragment<FragmentProjectTabBinding,ProjectViewModel> {

    private List<Fragment> mFragments;
    private List<String> titlePager;
    List<SystemBean> datas = new ArrayList<>();
    private int mCurrentItemCount;
    private List<String> pjName = new ArrayList<>();
    private List<Integer> pjIdList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();


    @Override
    public ProjectViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(ProjectViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.stateLiveData.observe(this,systemBeans -> {
            datas = systemBeans;

            mCurrentItemCount = datas.size();
            for (SystemBean systemBean : datas){
                pjName.add(systemBean.getName());
                pjIdList.add(systemBean.getId());
            }

            for(int i=0;i<mCurrentItemCount;i++){
                fragmentList.add(ProjectItemFragment.newInstance(pjIdList.get(i)));
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
    public void initData() {
        super.initData();
        viewModel.requestData();
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
