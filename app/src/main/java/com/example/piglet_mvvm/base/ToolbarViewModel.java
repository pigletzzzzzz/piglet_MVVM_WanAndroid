package com.example.piglet_mvvm.base;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.wzq.mvvmsmart.base.BaseModelMVVM;
import com.wzq.mvvmsmart.binding.command.BindingAction;
import com.wzq.mvvmsmart.binding.command.BindingCommand;

public class ToolbarViewModel<M extends BaseModelMVVM> extends BaseViewModel<M> {
    //标题文字
    public ObservableField<String> titleText = new ObservableField<>("");
    //右边文字
    public ObservableField<String> rightText = new ObservableField<>("更多");
    //右边文字的观察者
    public ObservableInt rightTextVisibleObservable = new ObservableInt(View.GONE);
    //右边图标的观察者
    public ObservableInt rightIconVisibleObservable = new ObservableInt(View.GONE);
    //兼容databinding，去泛型化
    public ToolbarViewModel toolbarViewModel;

    public ToolbarViewModel(@NonNull Application application) {
        this(application, null);
    }

    public ToolbarViewModel(@NonNull Application application, M model) {
        super(application, model);
        toolbarViewModel = this;
    }

    /**
     * 设置标题
     * @param text 标题文字
     */
    public void setTitleText(String text) {
        titleText.set(text);
    }

    /**
     * 设置右边文字
     *
     * @param text 右边文字
     */
    public void setRightText(String text) {
        rightText.set(text);
    }

    /**
     * 设置右边文字的显示和隐藏
     *
     * @param visibility
     */
    public void setRightTextVisible(int visibility) {
        rightTextVisibleObservable.set(visibility);
    }

    /**
     * 设置右边图标的显示和隐藏
     *
     * @param visibility
     */
    public void setRightIconVisible(int visibility) {
        rightIconVisibleObservable.set(visibility);
    }

    /**
     * 返回按钮的点击事件
     * 命令可以在UI层进行重写,重写后直接走重写逻辑,任意处理返回逻辑,重写后相当于废弃了当前命令.
     */
    public final BindingCommand backOnClick = new BindingCommand(new BindingAction() {
        // 使用navigation,需要特殊处理返回键
        @Override
        public void call() {
            // 发送livedata事件儿,Fragment中监听;
            sendBackPressEvent();
        }
    });

    /**
     * 命令可以在UI层进行重写
     */
    public BindingCommand rightTextOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
    /**
     * 命令可以在UI层进行重写
     */
    public BindingCommand rightIconOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
        }
    });


}
