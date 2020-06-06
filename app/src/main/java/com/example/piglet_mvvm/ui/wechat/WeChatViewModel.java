package com.example.piglet_mvvm.ui.wechat;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BaseArticleBean;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.bean.WeChatTabListBean;
import com.example.piglet_mvvm.data.Repository;
import com.example.piglet_mvvm.ui.project.ProjectViewModel;
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
 * Created Time 2020/5/28
 */
public class WeChatViewModel extends BaseViewModel<Repository> {

    public int pageNum = 1;
    public StateLiveData<List<WeChatTabListBean>> stateLiveData;
    public StateLiveData<List<ArticleBean>> stateArticleLiveData;
    public StateLiveData<Object> stateLiveCollData;
    public StateLiveData<Object> stateLiveUnCollData;

    public WeChatViewModel(@NonNull Application application, Repository model) {
        super(application, model);
        stateLiveData = new StateLiveData<>();
        stateLiveData.setValue(new ArrayList<>());

        stateArticleLiveData = new StateLiveData<>();
        stateArticleLiveData.setValue(new ArrayList<>());

        stateLiveCollData = new StateLiveData<>();
        stateLiveCollData.setValue(new Object());

        stateLiveUnCollData = new StateLiveData<>();
        stateLiveUnCollData.setValue(new Object());
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent<Object> finishRefreshing = new SingleLiveEvent<Object>();
        //上拉加载完成
        public SingleLiveEvent<Object> finishLoadMore = new SingleLiveEvent<Object>();
    }

    //获取公众号作者列表数据
    public void requestData() {
        model.getWeChatTabList()
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<List<WeChatTabListBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<List<WeChatTabListBean>> listBaseResponse) {
                        if (listBaseResponse.getErrorCode() == 0){
                            List<WeChatTabListBean> datas = listBaseResponse.getData();
                            if (datas != null){
                                if (datas.size() > 0){
                                    stateLiveData.getValue().clear();
                                    stateLiveData.getValue().addAll(datas);
                                    stateLiveData.postValueAndSuccess(stateLiveData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveData.postError();
                            }
                        }else {
                            ToastUtils.showShort(listBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError"+e.getMessage());
                        ToastUtils.showShort(((ResponseThrowable) e).message);
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                    }
                });
    }


    //获取公众号作者文章列表数据
    public void requestWeChatArticleData(int id) {
        model.getWechatArticleItem(pageNum,id)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<ArticleBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateArticleLiveData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<ArticleBean>>> baseArticleBeanBaseResponse) {
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0){
                            List<ArticleBean> datas = baseArticleBeanBaseResponse.getData().getDatas();
                            if (datas != null){
                                if (datas.size() > 0){
                                    if (pageNum == 1){
                                        stateArticleLiveData.getValue().clear();
                                    }
                                    stateArticleLiveData.getValue().addAll(datas);
                                    stateArticleLiveData.postValueAndSuccess(stateArticleLiveData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveData.postError();
                            }
                        }else {
                            ToastUtils.showShort(baseArticleBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateArticleLiveData.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //关闭对话框
                        stateArticleLiveData.postIdle();
                        //请求刷新完成收回
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
