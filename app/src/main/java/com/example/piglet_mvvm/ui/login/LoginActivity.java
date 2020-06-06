package com.example.piglet_mvvm.ui.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.databinding.FragmentLoginBinding;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.utils.KLog;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/30
 */
public class LoginActivity extends BaseActivity<FragmentLoginBinding,LoginViewModel> {

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        LiveEventBus.get("loginfinsh",boolean.class)
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean){
                            finish();
                        }
                    }
                });
    }

    @Override
    public LoginViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        //  使用了factory ,factory向LoginView里注入了数据仓库
        return ViewModelProviders.of(this, factory).get(LoginViewModel.class);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        binding.btnLogin.setOnClickListener(v -> {
            viewModel.login();
        });

        //监听ViewModel中pSwitchObservable的变化, 当ViewModel中执行【uc.pSwitchObservable.set(!uc.pSwitchObservable.get());】时会回调该方法
        viewModel.uc.pSwitchEvent.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //pSwitchObservable是boolean类型的LiveData观察者,所以可以直接使用它的值改变密码开关的图标
                if (viewModel.uc.pSwitchEvent.getValue()) {
                    //密码可见
                    //在xml中定义id后,使用binding可以直接拿到这个view的引用,不再需要findViewById去找控件了
                    binding.ivSwichPasswrod.setImageResource(R.drawable.show_psw);
                    binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //密码不可见
                    binding.ivSwichPasswrod.setImageResource(R.drawable.show_psw_press);
                    binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
