package com.example.piglet_mvvm.ui.knowledgesystem;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BaseArticleBean;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.bean.NavigationBean;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.data.Repository;
import com.example.piglet_mvvm.ui.home.HomeViewModel;
import com.example.piglet_mvvm.ui.wechat.WeChatViewModel;
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
 * Created Time 2020/5/18
 */
public class KnowledgeSystemViewModel extends BaseViewModel<Repository> {

    public int pageNum = 0;
    public StateLiveData<List<SystemBean>> stateLiveDataSystem;
    public StateLiveData<List<NavigationBean>> stateLiveDataNavigation;
    public StateLiveData<List<ArticleBean>> stateLiveArticleData;
    public StateLiveData<Object> stateLiveCollData;
    public StateLiveData<Object> stateLiveUnCollData;

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent<Object> finishRefreshing = new SingleLiveEvent<Object>();
        //上拉加载完成
        public SingleLiveEvent<Object> finishLoadMore = new SingleLiveEvent<Object>();
    }

    public KnowledgeSystemViewModel(@NonNull Application application, Repository model) {
        super(application, model);
        stateLiveDataSystem = new StateLiveData<>();
        stateLiveDataNavigation = new StateLiveData<>();

        stateLiveDataSystem.setValue(new ArrayList<>());
        stateLiveDataNavigation.setValue(new ArrayList<>());

        stateLiveArticleData = new StateLiveData<>();
        stateLiveArticleData.setValue(new ArrayList<>());

        stateLiveCollData = new StateLiveData<>();
        stateLiveCollData.setValue(new Object());

        stateLiveUnCollData = new StateLiveData<>();
        stateLiveUnCollData.setValue(new Object());
    }

    //获取体系列表数据
    public void requestSystemData() {
        model.getSystemItem()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(KnowledgeSystemViewModel.this)
                .subscribe(new Observer<BaseResponse<List<SystemBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveDataSystem.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<List<SystemBean>> listBaseResponse) {
                        if (listBaseResponse.getErrorCode() == 0){
                            List<SystemBean> data = listBaseResponse.getData();
                            if (data.size() > 0){
                                stateLiveDataSystem.postSuccess();
                                stateLiveDataSystem.getValue().addAll(data);
                                stateLiveDataSystem.postValueAndSuccess(stateLiveDataSystem.getValue());
                            }else {
                                ToastUtils.showShort("没有更多数据了");
                                KLog.e("请求到数据students.size" + data.size());
                            }
                        }else {
                            KLog.e("数据返回null");
                            stateLiveDataSystem.postError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveDataSystem.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        stateLiveDataSystem.postIdle();
                    }
                });
    }

    //获取导航列表数据
    public void requestNavigationData() {
        model.getNavigationItem()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(KnowledgeSystemViewModel.this)
                .subscribe(new Observer<BaseResponse<List<NavigationBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveDataNavigation.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<List<NavigationBean>> listBaseResponse) {
                        if (listBaseResponse.getErrorCode() == 0){
                            List<NavigationBean> data = listBaseResponse.getData();
                            if (data.size() > 0){
                                stateLiveDataNavigation.getValue().addAll(data);
                                stateLiveDataNavigation.postValueAndSuccess(stateLiveDataNavigation.getValue());
                            }else {
                                ToastUtils.showShort("没有更多数据了");
                                KLog.e("请求到数据students.size" + data.size());
                            }
                        }else {
                            KLog.e("数据返回null");
                            stateLiveDataNavigation.postError();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveDataNavigation.postIdle();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //关闭对话框
                        stateLiveDataNavigation.postIdle();
                    }
                });
    }

    //获取体系类别列表数据
    public void requestTypeSystemData(int id) {
        model.getSystemTypeItem(pageNum,id)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(KnowledgeSystemViewModel.this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<ArticleBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveArticleData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<ArticleBean>>> baseArticleBeanBaseResponse) {
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0){
                            List<ArticleBean> datas = baseArticleBeanBaseResponse.getData().getDatas();
                            if (datas != null){
                                if (datas.size() > 0){
                                    if (pageNum == 0){
                                        stateLiveArticleData.getValue().clear();
                                    }
                                    stateLiveArticleData.getValue().addAll(datas);
                                    stateLiveArticleData.postValueAndSuccess(stateLiveArticleData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveArticleData.postError();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveArticleData.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //请求刷新完成收回
                        stateLiveArticleData.postIdle();
                        uc.finishRefreshing.call();
                        uc.finishLoadMore.call();
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
