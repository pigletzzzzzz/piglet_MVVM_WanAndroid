package com.example.piglet_mvvm.base;

import androidx.databinding.ViewDataBinding;

import com.wzq.mvvmsmart.base.BaseActivityMVVM;
import com.wzq.mvvmsmart.base.BaseViewModelMVVM;

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModelMVVM> extends BaseActivityMVVM<V, VM> {
    public final String TAG = getClass().getSimpleName();



}
