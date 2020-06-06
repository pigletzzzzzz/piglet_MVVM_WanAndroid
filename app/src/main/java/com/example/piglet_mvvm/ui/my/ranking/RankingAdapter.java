package com.example.piglet_mvvm.ui.my.ranking;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.bean.RankingBean;

import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/29
 */
public class RankingAdapter extends BaseQuickAdapter<RankingBean, BaseViewHolder> {
    public RankingAdapter(int layoutResId, @Nullable List<RankingBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RankingBean item) {
        helper.setText(R.id.tv_rank,item.getRank()+"");
        helper.setText(R.id.tv_name,item.getUsername());
        helper.setText(R.id.tv_level,item.getLevel()+"");
        helper.setText(R.id.tv_integral,item.getCoinCount()+"");
        if (item.getRank() == 1){
            helper.setTextColor(R.id.tv_rank, Color.parseColor("#FFFF00"));
        }else if (item.getRank() == 2){
            helper.setTextColor(R.id.tv_rank, Color.parseColor("#A0A0A0"));
        }else if (item.getRank() == 3){
            helper.setTextColor(R.id.tv_rank, Color.parseColor("#FFA500"));
        }else {
            helper.setTextColor(R.id.tv_rank, Color.parseColor("#333333"));
        }
    }

    @Override
    public void setNewData(@Nullable List<RankingBean> data) {
        super.setNewData(data);
    }
}
