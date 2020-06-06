package com.example.piglet_mvvm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.databinding.ActivityMainBinding;
import com.example.piglet_mvvm.ui.home.HomeFragment;
import com.example.piglet_mvvm.ui.knowledgesystem.KnowledgeSystemFragment;
import com.example.piglet_mvvm.ui.my.MyFragment;
import com.example.piglet_mvvm.ui.project.ProjectFragment;
import com.example.piglet_mvvm.ui.wechat.WeChatFragment;
import com.example.piglet_mvvm.views.NavigationButton;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, BaseViewModel> implements View.OnClickListener {

    private NavigationButton mCurrentNavButton;
    private FragmentManager mFragmentManager;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBottomBar();
    }

    private void initBottomBar() {
        mFragmentManager = getSupportFragmentManager();
        binding.mainContent.navItemZstx.init(R.drawable.tab_zstx,R.string.tab_zstx, KnowledgeSystemFragment.class);
        binding.mainContent.navItemGzh.init(R.drawable.tab_gzh,R.string.tab_gzh, WeChatFragment.class);
        binding.mainContent.navItemXm.init(R.drawable.tab_xm,R.string.tab_xm, ProjectFragment.class);
        binding.mainContent.navItemMe.init(R.drawable.tab_my,R.string.tab_my, MyFragment.class);

        binding.mainContent.navItemZstx.setOnClickListener(this);
        binding.mainContent.navItemGzh.setOnClickListener(this);
        binding.mainContent.navItemXm.setOnClickListener(this);
        binding.mainContent.navItemMe.setOnClickListener(this);
        binding.mainContent.llSy.setOnClickListener(this);

//        doSelect(binding.mainContent.navItemZstx);
        selcetHome(binding.mainContent.llSy);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof NavigationButton){
            NavigationButton nav = (NavigationButton) v;
            binding.mainContent.imgSy.setImageResource(R.mipmap.ic_home);
            binding.mainContent.tvSy.setTextColor(ContextCompat.getColor(this,R.color.text_normal_color));
            doSelect(nav);
        }else if (v.getId() == R.id.ll_sy){
            //跳转到主页
            if (mCurrentNavButton != null) {
                mCurrentNavButton.setSelected(false);
            }
            mCurrentNavButton = null;
            binding.mainContent.imgSy.setImageResource(R.mipmap.ic_home_check);
            binding.mainContent.tvSy.setTextColor(ContextCompat.getColor(this,R.color.text_select_color));
            Fragment fragment = new HomeFragment();
            setFragment(fragment);
        }
        switch (v.getId()){
            case R.id.nav_item_zstx:
                doSelect(binding.mainContent.navItemZstx);
                break;
            case R.id.nav_item_gzh:
                doSelect(binding.mainContent.navItemGzh);
                break;
            case R.id.nav_item_xm:
                doSelect(binding.mainContent.navItemXm);
                break;
            case R.id.nav_item_me:
                doSelect(binding.mainContent.navItemMe);
                break;
        }
    }

    private void selcetHome(LinearLayout linearLayout){
        //跳转到主页
        if (mCurrentNavButton != null) {
            mCurrentNavButton.setSelected(false);
        }
        mCurrentNavButton = null;
        binding.mainContent.imgSy.setImageResource(R.mipmap.ic_home_check);
        binding.mainContent.tvSy.setTextColor(ContextCompat.getColor(this,R.color.text_select_color));
        Fragment fragment = new HomeFragment();
        setFragment(fragment);
    }

    private void doSelect(NavigationButton newNavButton) {

        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            Fragment fragment = Fragment.instantiate(this,
                    newNavButton.getClx().getName(), null);
            setFragment(fragment);
        }
        ft.commit();
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rl, fragment)
                .commit();
    }

    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null) {
            fragment.onResume();//是否要重新刷新？
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSupportFragmentManager().getFragments();
        if (getSupportFragmentManager().getFragments().size() > 0) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment mFragment : fragments) {
                mFragment.onActivityResult(requestCode, resultCode, data);
            }
    }
    }
}
