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
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/29
 */
public interface ApiService {

    //登录
    @POST("user/login")
    Observable<BaseResponse<LoginBean>> login(@Query("username") String username,
                                              @Query("password") String password);

    //注册
    @POST("user/login")
    Observable<BaseResponse<LoginBean>> register(@Query("username") String username,
                                                @Query("password") String password,
                                                @Query("repassword") String repassword);

    //退出
    @GET("user/logout/json")
    Observable<BaseResponse> loginout();

    @GET("lg/coin/userinfo/json")
    Observable<BaseResponse<UserIntegralBean>> getIntegral();

    //获取首页轮播图
    @GET("banner/json")
    Observable<BaseResponse<List<BannerBean>>> getBanner();

    //首页文章列表
    @GET("article/list/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getArticle(@Path("page") int pageNum);

    //获取体系列表
    @GET("tree/json")
    Observable<BaseResponse<List<SystemBean>>> getSystemItem();

    //获取导航列表
    @GET("navi/json")
    Observable<BaseResponse<List<NavigationBean>>> getNavigationItem();

    //获取项目分类
    @GET("project/tree/json")
    Observable<BaseResponse<List<SystemBean>>> getProjectList();

    //获取单个项目类别数据列表
    @GET("project/list/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getProjectArticleItem(@Path("page") int page,@Query("cid") int id);

    //获取公众号分类
    @GET("wxarticle/chapters/json")
    Observable<BaseResponse<List<WeChatTabListBean>>> WeChatTabList();

    //获取公众号作者数据列表
    @GET("wxarticle/list/{id}/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getWechatArticleItem(@Path("page") int page,@Path("id") int id);

    //获取个人积分
    @GET("lg/coin/list/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<LevelDataBean>>>> getLevelData(@Path("page") int page);

    //获取排名列表
    @GET("coin/rank/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<RankingBean>>>> getRankingData(@Path("page") int page);

    //获取收藏文章列表
    @GET("lg/collect/list/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getCollectionData(@Path("page") int page);

    //收藏文章
    @POST("lg/collect/{id}/json")
    Observable<BaseResponse> collectionArticle(@Path("id") int id);

    //取消收藏文章
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse> unCollectionArticle(@Path("id") int id);

    /**
     *  取消收藏文章(收藏界面)
     * originId 代表的是你收藏之前的那篇文章本身的id； 但是收藏支持主动添加，这种情况下，
     * 没有originId则为-1
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<BaseResponse> unCollectionArticle(@Path("id") int id,
                                                 @Field("originId") int originId);

    //获取体系某个类别数据列表
    @GET("article/list/{page}/json")
    Observable<BaseResponse<BaseArticleBean<List<ArticleBean>>>> getSystemTypeItem(@Path("page") int pageNum,
                                                                                   @Query("cid") int id);

}
