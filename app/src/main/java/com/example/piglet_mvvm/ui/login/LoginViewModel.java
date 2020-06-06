package com.example.piglet_mvvm.ui.login;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.bean.LoginBean;
import com.example.piglet_mvvm.bean.UserIntegralBean;
import com.example.piglet_mvvm.data.Repository;
import com.example.piglet_mvvm.ui.MainActivity;
import com.example.piglet_mvvm.utils.AppConstants;
import com.google.gson.Gson;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.binding.command.BindingAction;
import com.wzq.mvvmsmart.binding.command.BindingCommand;
import com.wzq.mvvmsmart.binding.command.BindingConsumer;
import com.wzq.mvvmsmart.event.SingleLiveEvent;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.http.ResponseThrowable;
import com.wzq.mvvmsmart.http.cookie.store.PersistentCookieStore;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.RxUtils;
import com.wzq.mvvmsmart.utils.SPUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/30
 */
public class LoginViewModel extends BaseViewModel<Repository> {
    public StateLiveData<Object> stateLiveData;
    public StateLiveData<UserIntegralBean> stateLiveUserIntegralData;
    public StateLiveData<Object> stateLiveLoginOutData;

    //用户名的绑定,使用的是databinding的ObservableField
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application, Repository model) {
        super(application, model);
        stateLiveData = new StateLiveData<>();
        stateLiveData.setValue(new Object());

        stateLiveUserIntegralData = new StateLiveData<>();
        stateLiveUserIntegralData.setValue(new UserIntegralBean());

        stateLiveLoginOutData = new StateLiveData<>();
        stateLiveLoginOutData.setValue(new Object());

        //回显数据,从本地取得数据绑定到View层,
        userName.set(model.getUserName());
        password.set(model.getPassword());
        uc.pSwitchEvent.setValue(false);
    }

    //清除用户名的点击事件, 逻辑从View层转换到ViewModel层
    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            userName.set("");
        }
    });
    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
            uc.pSwitchEvent.setValue(!uc.pSwitchEvent.getValue());
        }
    });


    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            if (hasFocus) {
                clearBtnVisibility.set(View.VISIBLE);
            } else {
                clearBtnVisibility.set(View.INVISIBLE);
            }
        }
    });

    protected void login(){
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }

        model.login(userName.get(),password.get())
                .compose(RxUtils.observableToMain()) //线程调度,compose操作符是直接对当前Observable进行操作（可简单理解为不停地.方法名（）.方法名（）链式操作当前Observable）
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(LoginViewModel.this)
                .subscribe(new Observer<BaseResponse<LoginBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<LoginBean> loginBeanBaseResponse) {
                        if (loginBeanBaseResponse.getErrorCode() == 0){
                            stateLiveData.postSuccess();
                            //保存账号密码
                            model.saveUserName(userName.get());
                            model.savePassword(password.get());
                            ToastUtils.showLong("登录成功！");

                            //保存登录成功回传数据，持久化为cookie
                            String json = new Gson().toJson(loginBeanBaseResponse.getData());
                            KLog.e("LOGIN/////" + json);
                            SPUtils.getInstance().put("smart_cookie", json);

                            requestUserIntegralData();
//                            //关闭页面
//                            LiveEventBus.get("loginfinsh").post(true);
                        }else {
                            stateLiveData.postError();
                            ToastUtils.showLong("登录失败！错误代码："+loginBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveData.postIdle();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //关闭对话框
                        stateLiveData.postIdle();
                    }
                });
    }

    //获取个人信息数据
    public void requestUserIntegralData() {
        model.getIntegral()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<UserIntegralBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        KLog.e("请求到数据userIntegral......onSubscribe");
                        stateLiveUserIntegralData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<UserIntegralBean> userIntegralBeanBaseResponse) {
                        if (userIntegralBeanBaseResponse.getErrorCode() == 0){
                            UserIntegralBean userIntegralBean = userIntegralBeanBaseResponse.getData();
                            stateLiveUserIntegralData.postValueAndSuccess(userIntegralBean);
                            KLog.e("请求到数据userIntegral" + userIntegralBean);
                            SPUtils.getInstance().put(AppConstants.USERNAME,userIntegralBean.getUsername());
                            SPUtils.getInstance().put(AppConstants.COINCOUNT,userIntegralBean.getCoinCount()+"");
                            SPUtils.getInstance().put(AppConstants.LEVEL,userIntegralBean.getLevel()+"");
                            SPUtils.getInstance().put(AppConstants.RANK,userIntegralBean.getRank()+"");
                            SPUtils.getInstance().put(AppConstants.USERID,userIntegralBean.getUserId()+"");
                            //登录成功关闭页面
                            LiveEventBus.get("loginfinsh").post(true);
                        }else {
                            ToastUtils.showShort(userIntegralBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort(((ResponseThrowable) e).message);
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("请求到数据userIntegral......进入onComplete");
                    }
                });
    }

    public void loginOut(){
        model.loginout()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveLoginOutData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.getErrorCode() == 0){
                            stateLiveLoginOutData.postSuccess();
                            ToastUtils.showLong("退出成功！");
                        }else {
                            stateLiveLoginOutData.postError();
                            ToastUtils.showLong("退出失败！"+baseResponse.getErrorMsg());
                            KLog.e("退出失败" + baseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stateLiveLoginOutData.postError();
                        ToastUtils.showLong("退出失败！"+e.getMessage());
                        KLog.e("退出失败" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
