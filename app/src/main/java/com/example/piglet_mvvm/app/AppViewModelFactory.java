package com.example.piglet_mvvm.app;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piglet_mvvm.data.Repository;
import com.example.piglet_mvvm.ui.home.HomeViewModel;
import com.example.piglet_mvvm.ui.knowledgesystem.KnowledgeSystemViewModel;
import com.example.piglet_mvvm.ui.login.LoginViewModel;
import com.example.piglet_mvvm.ui.my.MyViewModel;
import com.example.piglet_mvvm.ui.project.ProjectViewModel;
import com.example.piglet_mvvm.ui.wechat.WeChatViewModel;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/29
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final Repository mRepository;

    public static AppViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository());
                }
            }
        }
        return INSTANCE;
    }

    private AppViewModelFactory(Application application, Repository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(LoginViewModel.class)){
            return (T) new LoginViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(KnowledgeSystemViewModel.class)){
            return (T) new KnowledgeSystemViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(ProjectViewModel.class)){
            return (T) new ProjectViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(WeChatViewModel.class)){
            return (T) new WeChatViewModel(mApplication, mRepository);
        }else if (modelClass.isAssignableFrom(MyViewModel.class)){
            return (T) new MyViewModel(mApplication, mRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
