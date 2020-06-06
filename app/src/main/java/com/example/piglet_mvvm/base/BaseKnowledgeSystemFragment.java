package com.example.piglet_mvvm.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.databinding.FragmentKnowledgeSystemBinding;
import com.example.piglet_mvvm.ui.adapter.BaseFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/20
 */
public abstract class BaseKnowledgeSystemFragment extends BaseFragment<FragmentKnowledgeSystemBinding,BaseViewModel>{

    private List<Fragment> mFragments;
    private List<String> titlePager;

    protected abstract List<Fragment> pagerFragment();

    protected abstract List<String> pagerTitleString();

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_knowledge_system;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        mFragments = pagerFragment();
        titlePager = pagerTitleString();
        //设置Adapter
        BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), mFragments, titlePager);
        binding.vp.setAdapter(pagerAdapter);
        binding.tab.setupWithViewPager(binding.vp);
        binding.vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tab));
    }
}
