package com.example.piglet_mvvm.app;


import com.example.piglet_mvvm.data.ApiService;
import com.example.piglet_mvvm.data.HttpDataSource;
import com.example.piglet_mvvm.data.Repository;
import com.example.piglet_mvvm.data.RetrofitClient;
import com.example.piglet_mvvm.data.service.HttpDataSourceImpl;
import com.example.piglet_mvvm.data.service.LocalDataSource;
import com.example.piglet_mvvm.data.service.LocalDataSourceImpl;

public class Injection {
    public static Repository provideDemoRepository() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        HttpDataSource httpDataSource = HttpDataSourceImpl.getInstance(apiService);
//-------------------------------------------------------
        LocalDataSource localDataSource = LocalDataSourceImpl.getInstance();

        return Repository.getInstance(httpDataSource, localDataSource);
    }
}
