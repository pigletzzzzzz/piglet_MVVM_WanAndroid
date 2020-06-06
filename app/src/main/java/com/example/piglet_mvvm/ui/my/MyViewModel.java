package com.example.piglet_mvvm.ui.my;

import android.app.Application;

import androidx.annotation.NonNull;

import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.bean.BaseArticleBean;
import com.example.piglet_mvvm.bean.BaseResponse;
import com.example.piglet_mvvm.bean.LevelDataBean;
import com.example.piglet_mvvm.bean.RankingBean;
import com.example.piglet_mvvm.data.Repository;
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
 * Created Time 2020/5/29
 */
public class MyViewModel extends BaseViewModel<Repository> {

    public int pageNum = 1;
    public int CollpageNum = 0;
    public StateLiveData<List<LevelDataBean>> stateLiveLevelData;
    public StateLiveData<List<RankingBean>> stateLiveRankingData;
    public StateLiveData<List<ArticleBean>> stateLiveCollectionData;
    public StateLiveData<Object> stateLiveCollData;
    public StateLiveData<Object> stateLiveUnCollData;
    public StateLiveData<Object> stateLiveNewUnCollData;

    public MyViewModel(@NonNull Application application, Repository model) {
        super(application, model);
        stateLiveLevelData = new StateLiveData<>();
        stateLiveLevelData.setValue(new ArrayList<>());

        stateLiveRankingData = new StateLiveData<>();
        stateLiveRankingData.setValue(new ArrayList<>());

        stateLiveCollectionData = new StateLiveData<>();
        stateLiveCollectionData.setValue(new ArrayList<>());

        stateLiveCollData = new StateLiveData<>();
        stateLiveCollData.setValue(new Object());

        stateLiveNewUnCollData = new StateLiveData<>();
        stateLiveNewUnCollData.setValue(new Object());
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public SingleLiveEvent<Object> finishRefreshing = new SingleLiveEvent<Object>();
        //上拉加载完成
        public SingleLiveEvent<Object> finishLoadMore = new SingleLiveEvent<Object>();
    }

    //获取个人积分详情
    public void getLevelData(){
        model.getLevelData(pageNum)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<LevelDataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveLevelData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<LevelDataBean>>> baseArticleBeanBaseResponse) {
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0){
                            List<LevelDataBean> dataBeans = baseArticleBeanBaseResponse.getData().getDatas();
                            if (dataBeans != null){
                                if (dataBeans.size() > 0){
                                    if (pageNum == 1){
                                        stateLiveLevelData.getValue().clear();
                                    }
                                    stateLiveLevelData.getValue().addAll(dataBeans);
                                    stateLiveLevelData.postValueAndSuccess(stateLiveLevelData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + dataBeans.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveLevelData.postError();
                            }
                        }else {
                            ToastUtils.showShort(baseArticleBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveLevelData.postIdle();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //关闭对话框
                        stateLiveLevelData.postIdle();
                        //请求刷新完成收回
                        uc.finishRefreshing.call();
                        uc.finishLoadMore.call();
                    }
                });
    }

    //获取积分排行详情
    public void getRankingData(){
        model.getRankingData(pageNum)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<RankingBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveRankingData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<RankingBean>>> baseArticleBeanBaseResponse) {
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0){
                            List<RankingBean> datas = baseArticleBeanBaseResponse.getData().getDatas();
                            if (datas != null){
                                if (datas.size() > 0){
                                    if (pageNum == 1){
                                        stateLiveRankingData.getValue().clear();
                                    }
                                    stateLiveRankingData.getValue().addAll(datas);
                                    stateLiveRankingData.postValueAndSuccess(stateLiveRankingData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveRankingData.postError();
                            }
                        }else {
                            ToastUtils.showShort(baseArticleBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        //关闭对话框
                        stateLiveRankingData.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        //关闭对话框
                        stateLiveRankingData.postIdle();
                        //请求刷新完成收回
                        uc.finishRefreshing.call();
                        uc.finishLoadMore.call();
                    }
                });
    }

    //获取文章收藏列表
    public void getCollectionData(){
        model.getCollectionData(CollpageNum)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse<BaseArticleBean<List<ArticleBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse<BaseArticleBean<List<ArticleBean>>> baseArticleBeanBaseResponse) {
                        if (baseArticleBeanBaseResponse.getErrorCode() == 0){
                            List<ArticleBean> datas = baseArticleBeanBaseResponse.getData().getDatas();
                            if (datas != null){
                                if (datas.size() > 0){
                                    if (CollpageNum == 0){
                                        stateLiveCollectionData.getValue().clear();
                                    }
                                    stateLiveCollectionData.getValue().addAll(datas);
                                    stateLiveCollectionData.postValueAndSuccess(stateLiveCollectionData.getValue());
                                }else {
                                    ToastUtils.showShort("没有更多数据了");
                                    KLog.e("请求到数据students.size" + datas.size());
                                }
                            }else {
                                KLog.e("数据返回null");
                                stateLiveCollectionData.postError();
                            }
                        }else {
                            ToastUtils.showShort(baseArticleBeanBaseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("进入onError" + e.getMessage());
                        stateLiveCollectionData.postError();
                        if (e instanceof ResponseThrowable) {
                            ToastUtils.showShort(((ResponseThrowable) e).message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                        stateLiveCollectionData.postIdle();
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

    // 取消收藏文章(收藏界面)
    public void unCollectionArticle(int id,int originId){
        model.unCollectionArticle(id, originId)
                .compose(RxUtils.observableToMain())
                .compose(RxUtils.exceptionTransformer())
                .doOnSubscribe(this)
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        stateLiveNewUnCollData.postLoading();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        if (baseResponse.getErrorCode() == 0){
                            stateLiveNewUnCollData.postSuccess();
                            ToastUtils.showShort("取消收藏成功！");
                        }else {
                            stateLiveNewUnCollData.postError();
                            ToastUtils.showShort("取消收藏失败！"+baseResponse.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        stateLiveNewUnCollData.postError();
                        ToastUtils.showShort("取消收藏失败！"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        KLog.e("进入onComplete");
                    }
                });
    }

    /**
     * 删除条目
     */
    public void deleteItem(ArticleBean articleBean){
        stateLiveCollectionData.getValue().remove(articleBean);
    }
}
