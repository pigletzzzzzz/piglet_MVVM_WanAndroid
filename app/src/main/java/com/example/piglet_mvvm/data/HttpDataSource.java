package com.example.piglet_mvvm.data;

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

import java.util.List;

import io.reactivex.Observable;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/29
 */
public interface HttpDataSource {

    Observable<BaseResponse<LoginBean>> login (String username,String password);

    Observable<BaseResponse<LoginBean>> register (String username,String password,String repassword);

    Observable<BaseResponse> loginout();

    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getArticle(int pageNum);

    Observable<BaseResponse<UserIntegralBean>> getIntegral();

    Observable<BaseResponse<List<SystemBean>>> getSystemItem();

    Observable<BaseResponse<List<NavigationBean>>> getNavigationItem();

    Observable<BaseResponse<List<SystemBean>>> getProjectList();

    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getProjectArticleList(int pageNum,
                                                                                       int id);

    Observable<BaseResponse<List<WeChatTabListBean>>> getWeChatTabList();

    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getWechatArticleItem(int pageNum,
                                                                                      int id);

    Observable<BaseResponse<List<BannerBean>>> getBanner();

    Observable<BaseResponse<BaseArticleBean<List<LevelDataBean>>>> getLevelData(int pageNum);

    Observable<BaseResponse<BaseArticleBean<List<RankingBean>>>> getRankingData(int pageNum);

    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getCollectionData(int pageNum);

    Observable<BaseResponse> collectionArticle(int id);

    Observable<BaseResponse> unCollectionArticle(int id);

    Observable<BaseResponse> unCollectionArticle(int id, int originId);

    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getSystemTypeItem(int pageNum,
                                                                                   int id);

}
