package com.example.piglet_mvvm.ui.knowledgesystem.system;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.databinding.FragmentSystemTabBinding;
import com.example.piglet_mvvm.ui.adapter.BaseFragmentPagerAdapter;
import com.example.piglet_mvvm.ui.knowledgesystem.KnowledgeSystemViewModel;
import com.example.piglet_mvvm.ui.project.ProjectItemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/2
 */
public class SystemListActivity extends BaseActivity<FragmentSystemTabBinding, KnowledgeSystemViewModel> {

    private int mCurrentItemCount;
    private List<String> tabName = new ArrayList<>();
    private List<Integer> tabIdList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private int pos;
    private SystemBean datas = new SystemBean();

    @Override
    public KnowledgeSystemViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(KnowledgeSystemViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        datas = (SystemBean) intent.getSerializableExtra("system");
        pos = intent.getIntExtra("pos",-1);

        binding.tvTitle.setText(datas.getName());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (pos != -1 && datas != null){
            List<SystemBean> systemBeanList = datas.getChildren();

            mCurrentItemCount = systemBeanList.size();
            for (SystemBean systemBean : systemBeanList){
                tabName.add(systemBean.getName());
                tabIdList.add(systemBean.getId());
            }
            for(int i=0;i<mCurrentItemCount;i++){
                fragmentList.add(SystemTypeListFragment.newInstance(tabIdList.get(i)));
            }

            BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(
                    getSupportFragmentManager(), fragmentList, tabName);
            binding.vp.setAdapter(pagerAdapter);
            binding.tabSpl.setViewPager(binding.vp);
            if (pos != -1 && pos > 0){
                binding.tabSpl.setCurrentTab(pos);
            }
        }

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_system_tab;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
