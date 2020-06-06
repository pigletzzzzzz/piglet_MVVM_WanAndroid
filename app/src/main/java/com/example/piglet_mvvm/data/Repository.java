package com.example.piglet_mvvm.data;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

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
import com.example.piglet_mvvm.data.service.LocalDataSource;
import com.wzq.mvvmsmart.base.BaseModelMVVM;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/29
 */
public class Repository extends BaseModelMVVM implements HttpDataSource, LocalDataSource {

    private volatile static Repository INSTANCE = null;
    private final HttpDataSource mHttpDataSource;
    private final LocalDataSource mLocalDataSource;


    private Repository(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static Repository getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (Repository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Repository(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveUserName(String userName) {
        mLocalDataSource.saveUserName(userName);
    }

    @Override
    public void savePassword(String password) {
        mLocalDataSource.savePassword(password);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public String getPassword() {
        return mLocalDataSource.getPassword();
    }

    @Override
    public Observable<BaseResponse<LoginBean>> login(String username, String password) {
        return mHttpDataSource.login(username, password);
    }

    @Override
    public Observable<BaseResponse<LoginBean>> register(String username, String password, String repassword) {
        return mHttpDataSource.register(username, password, repassword);
    }

    @Override
    public Observable<BaseResponse> loginout() {
        return mHttpDataSource.loginout();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getArticle(int pageNum) {
        return mHttpDataSource.getArticle(pageNum);
    }

    @Override
    public Observable<BaseResponse<UserIntegralBean>> getIntegral() {
        return mHttpDataSource.getIntegral();
    }

    @Override
    public Observable<BaseResponse<List<SystemBean>>> getSystemItem() {
        return mHttpDataSource.getSystemItem();
    }

    @Override
    public Observable<BaseResponse<List<NavigationBean>>> getNavigationItem() {
        return mHttpDataSource.getNavigationItem();
    }

    @Override
    public Observable<BaseResponse<List<SystemBean>>> getProjectList() {
        return mHttpDataSource.getProjectList();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getProjectArticleList(int pageNum, int id) {
        return mHttpDataSource.getProjectArticleList(pageNum, id);
    }

    @Override
    public Observable<BaseResponse<List<WeChatTabListBean>>> getWeChatTabList() {
        return mHttpDataSource.getWeChatTabList();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getWechatArticleItem(int pageNum, int id) {
        return mHttpDataSource.getWechatArticleItem(pageNum, id);
    }

    @Override
    public Observable<BaseResponse<List<BannerBean>>> getBanner() {
        return mHttpDataSource.getBanner();
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<LevelDataBean>>>> getLevelData(int pageNum) {
        return mHttpDataSource.getLevelData(pageNum);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<RankingBean>>>> getRankingData(int pageNum) {
        return mHttpDataSource.getRankingData(pageNum);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getCollectionData(int pageNum) {
        return mHttpDataSource.getCollectionData(pageNum);
    }

    @Override
    public Observable<BaseResponse> collectionArticle(int id) {
        return mHttpDataSource.collectionArticle(id);
    }

    @Override
    public Observable<BaseResponse> unCollectionArticle(int id) {
        return mHttpDataSource.unCollectionArticle(id);
    }

    @Override
    public Observable<BaseResponse> unCollectionArticle(int id, int originId) {
        return mHttpDataSource.unCollectionArticle(id, originId);
    }

    @Override
    public Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getSystemTypeItem(int pageNum, int id) {
        return mHttpDataSource.getSystemTypeItem(pageNum, id);
    }


}
