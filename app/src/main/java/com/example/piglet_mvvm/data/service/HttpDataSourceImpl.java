package com.example.piglet_mvvm.data.service;

import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BannerBean;
import com.example.piglet_mvvm.bean.BaseArticleBean;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.bean.LevelDataBean;
import com.example.piglet_mvvm.bean.LoginBean;
import com.example.piglet_mvvm.bean.NavigationBean;
import com.example.piglet_mvvm.bean.RankingBean;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.bean.UserIntegralBean;
import com.example.piglet_mvvm.bean.WeChatTabListBean;
import com.example.piglet_mvvm.data.ApiService;
import com.example.piglet_mvvm.data.HttpDataSource;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/29
 */
public class HttpDataSourceImpl implements HttpDataSource {
    private ApiService apiService;
    private volatile static HttpDataSourceImpl INSTANCE = null;

    public static HttpDataSourceImpl getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (HttpDataSourceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpDataSourceImpl(apiService);
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private HttpDataSourceImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<BaseResponse<LoginBean>> login(String username, String password) {
        return apiService.login(username, password);
    }

    @Override
    public Observable<BaseResponse<LoginBean>> register(String username, String password, String repassword) {
        return apiService.register(username, password, repassword);
    }

    @Override
    public Observable<BaseResponse> loginout() {
        return apiService.loginout();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getArticle(int pageNum) {
        return apiService.getArticle(pageNum);
    }

    @Override
    public Observable<BaseResponse<UserIntegralBean>> getIntegral() {
        return apiService.getIntegral();
    }

    @Override
    public Observable<BaseResponse<List<SystemBean>>> getSystemItem() {
        return apiService.getSystemItem();
    }

    @Override
    public Observable<BaseResponse<List<NavigationBean>>> getNavigationItem() {
        return apiService.getNavigationItem();
    }

    @Override
    public Observable<BaseResponse<List<SystemBean>>> getProjectList() {
        return apiService.getProjectList();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getProjectArticleList(int pageNum, int id) {
        return apiService.getProjectArticleItem(pageNum, id);
    }

    @Override
    public Observable<BaseResponse<List<WeChatTabListBean>>> getWeChatTabList() {
        return apiService.WeChatTabList();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getWechatArticleItem(int pageNum, int id) {
        return apiService.getWechatArticleItem(pageNum, id);
    }

    @Override
    public Observable<BaseResponse<List<BannerBean>>> getBanner() {
        return apiService.getBanner();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<LevelDataBean>>>> getLevelData(int pageNum) {
        return apiService.getLevelData(pageNum);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<RankingBean>>>> getRankingData(int pageNum) {
        return apiService.getRankingData(pageNum);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getCollectionData(int pageNum) {
        return apiService.getCollectionData(pageNum);
    }

    @Override
    public Observable<BaseResponse> collectionArticle(int id) {
        return apiService.collectionArticle(id);
    }

    @Override
    public Observable<BaseResponse> unCollectionArticle(int id) {
        return apiService.unCollectionArticle(id);
    }

    @Override
    public Observable<BaseResponse> unCollectionArticle(int id, int originId) {
        return apiService.unCollectionArticle(id, originId);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getSystemTypeItem(int pageNum, int id) {
        return apiService.getSystemTypeItem(pageNum, id);
    }

}
