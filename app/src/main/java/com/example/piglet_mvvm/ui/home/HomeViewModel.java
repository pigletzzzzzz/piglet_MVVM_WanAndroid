package com.example.piglet_mvvm.ui.home;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BannerBean;
import com.example.piglet_mvvm.bean.BaseArticleBean;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.data.Repository;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wzq.mvvmsmart.event.SingleLiveEvent;
import com.wzq.mvvmsmart.event.StateLiveData;
import com.wzq.mvvmsmart.http.ResponseThrowable;
import com.wzq.mvvmsmart.utils.KLog;
import com.wzq.mvvmsmart.utils.RxUtils;
import com.wzq.mvvmsmart.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/30
 */
public class HomeViewModel extends BaseViewModel<Repository> {

    public int pageNum = 0;
    public StateLiveData<List<ArticleBean>> stateLiveData;
    public StateLiveData<List<BannerBean>> stateBannerLiveData;
    public StateLiveData<Object> stateLiveCollData;
    public StateLiveData<Object> stateLiveUnCollData;

    public HomeViewModel(@NonNull Application application, Repository model) {
        super(application, model);
        stateLiveData = new StateLiveData<>();
        stateLiveData.setValue(new ArrayList<ArticleBean>());

        stateBannerLiveData = new StateLiveData<>();
        stateBannerLiveData.setValue(new ArrayList<>());

        stateLiveCollData = new StateLiveData<>();
        stateLiveCollData.setValue(new Object());

        stateLiveUnCollData = new StateLiveData<>();
        stateLiveUnCollData.setValue(new Object());
    }

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent<Object> finishRefreshing = new SingleLiveEvent<Object>();
        //上拉加载完成
        public SingleLiveEvent<Object> finishLoadMore = new SingleLiveEvent<Object>();
    }

    /**
     * 获取首页文章数据列表
     */
    public void requestData() {
        model.getArticle(pageNum)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(HomeViewModel.this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<ArticleBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<ArticleBean>>> baseArticleBeanBaseResponse) {
                        KLog.e("进入onNext");
                        //请求成功
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0) {
                            List<ArticleBean> data = baseArticleBeanBaseResponse.getData().getDatas();
                            if (data != null){
                                if (data.size() > 0){
                                    if (pageNum == 0) {
                                        stateLiveData.getValue().clear();
                                    }
                                    stateLiveData.postSuccess();
                                    stateLiveData.getValue().addAll(data);
                                    stateLiveData.postValueAndSuccess(stateLiveData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + data.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveData.postError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveData.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //请求刷新完成收回
                        stateLiveData.postIdle();
                        uc.finishRefreshing.call();
                        uc.finishLoadMore.call();
                    }
                });
    }

    //获取首页轮播图
    public void requestBannerData() {
        model.getBanner()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(HomeViewModel.this)
                .subscribe(new Observer<BaseResponse<List<BannerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateBannerLiveData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<List<BannerBean>> listBaseResponse) {
                        if (listBaseResponse.getErrorCode() == 0){
                            List<BannerBean> datas = listBaseResponse.getData();
                            if (datas != null){
                                if (datas.size() > 0){
                                    stateBannerLiveData.getValue().addAll(datas);
                                    stateBannerLiveData.postValueAndSuccess(stateBannerLiveData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateBannerLiveData.postError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        stateBannerLiveData.postIdle();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        stateBannerLiveData.postIdle();
                    }
                });
    }

    //收藏文章
    public void collectionArticle(int id){
        model.collectionArticle(id)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveCollData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.getErrorCode() == 0){
                            stateLiveCollData.postSuccess();
                            ToastUtils.showShort("收藏成功！");
                        }else {
                            stateLiveCollData.postError();
                            ToastUtils.showShort("收藏失败！"+baseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stateLiveCollData.postError();
                        ToastUtils.showShort("收藏失败！"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                    }
                });
    }

    // 取消收藏文章
    public void unCollectionArticle(int id){
        model.unCollectionArticle(id)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveUnCollData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.getErrorCode() == 0){
                            stateLiveUnCollData.postSuccess();
                            ToastUtils.showShort("取消收藏成功！");
                        }else {
                            stateLiveUnCollData.postError();
                            ToastUtils.showShort("取消收藏失败！"+baseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stateLiveUnCollData.postError();
                        ToastUtils.showShort("取消收藏失败！"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                    }
                });

    }
}
