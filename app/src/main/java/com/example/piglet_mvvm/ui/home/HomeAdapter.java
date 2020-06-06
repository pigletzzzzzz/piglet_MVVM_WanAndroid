package com.example.piglet_mvvm.ui.home;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.bean.ArticleBean;

import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/4/30
 */
public class HomeAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    public HomeAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleBean item) {
        if (TextUtils.isEmpty(item.getAuthor())){
            helper.setText(R.id.tx_author,"转发者");
        }else {
            helper.setText(R.id.tx_author,"作者");
        }

        if (TextUtils.isEmpty(item.getAuthor())){
            helper.setText(R.id.base_author,item.getShareUser());
        }else {
            helper.setText(R.id.base_author,item.getAuthor());
        }

        if (item.isCollect()){
            helper.setImageResource(R.id.zan,R.mipmap.zan_y);
        }else {
            helper.setImageResource(R.id.zan,R.mipmap.zan_n);
        }

        helper.setText(R.id.base_time,item.getNiceDate());
        helper.setText(R.id.artile_title,item.getTitle());
        helper.setText(R.id.article_type,item.getSuperChapterName());
        helper.addOnClickListener(R.id.zan);
    }

    @Override
    public void setNewData(@Nullable List<ArticleBean> data) {
        super.setNewData(data);
    }
}
