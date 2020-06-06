package com.example.piglet_mvvm.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.databinding.FragmentMyBinding;
import com.example.piglet_mvvm.ui.login.LoginActivity;
import com.example.piglet_mvvm.ui.login.LoginViewModel;
import com.example.piglet_mvvm.ui.my.aboutTheAuthor.AboutTheAuthorActivity;
import com.example.piglet_mvvm.ui.my.aboutTheAuthor.TestActivity;
import com.example.piglet_mvvm.ui.my.collection.CollectionActivity;
import com.example.piglet_mvvm.ui.my.points.MyPointsActivity;
import com.example.piglet_mvvm.ui.my.ranking.RankingActivity;
import com.example.piglet_mvvm.utils.AppConstants;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.utils.SPUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;


/**
 * Created By 刘纯贵
 * Created Time 2020/5/28
 */
public class MyFragment extends BaseFragment<FragmentMyBinding, LoginViewModel> {


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.bz.stop();
    }

    @Override
    public void initData() {
        super.initData();
        if (IsLogin()){
            binding.username.setText(SPUtils.getInstance().getString(AppConstants.USERNAME));
            binding.userLevel.setText("等级"+SPUtils.getInstance().getString(AppConstants.LEVEL));
            binding.userId.setText(SPUtils.getInstance().getString(AppConstants.USERID));
            binding.userRanking.setText(SPUtils.getInstance().getString(AppConstants.RANK));
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        getUserIntegralData();

        binding.llMyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsLogin()){
                    ToastUtils.showLong("请登录！");
                }else {
                    Intent intent = new Intent(getActivity(), MyPointsActivity.class);
                    intent.putExtra("points",SPUtils.getInstance().getString(AppConstants.COINCOUNT));
                    startActivity(intent);
                }
            }
        });

        binding.rlInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsLogin()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    return;
                }
            }
        });

        binding.llMyIntegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RankingActivity.class);
                startActivity(intent);
            }
        });

        binding.llMyCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsLogin()){
                    ToastUtils.showLong("请登录！");
                }else {
                    Intent intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }
            }
        });

        binding.tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsLogin()){
                    loginOut();
                }else {
                    ToastUtils.showLong("请登录！");
                }
            }
        });

        binding.llMyExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutTheAuthorActivity.class);
                startActivity(intent);
            }
        });

        binding.llMyOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginOut(){
        viewModel.loginOut();

        viewModel.stateLiveLoginOutData.stateEnumMutableLiveData
                .observe(this, new Observer<StateLiveData.StateEnum>() {
                    @Override
                    public void onChanged(StateLiveData.StateEnum stateEnum) {
                        if (stateEnum.equals(StateLiveData.StateEnum.Success)){
                            SPUtils.getInstance().clear();
                            binding.username.setText("请登录");
                            binding.userLevel.setText("等级0");
                            binding.userId.setText("-");
                            binding.userRanking.setText("-");
                        }
                    }
                });
    }


    //获取登录者个人信息
    private void getUserIntegralData(){
        LiveEventBus.get("loginfinsh",boolean.class).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.username.setText(SPUtils.getInstance().getString(AppConstants.USERNAME));
                    binding.userLevel.setText("等级"+SPUtils.getInstance().getString(AppConstants.LEVEL));
                    binding.userId.setText(SPUtils.getInstance().getString(AppConstants.USERID));
                    binding.userRanking.setText(SPUtils.getInstance().getString(AppConstants.RANK));
                }
            }
        });
    }

    //判断是否登录
    private boolean IsLogin(){
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(AppConstants.USERNAME))){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public LoginViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_my;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
