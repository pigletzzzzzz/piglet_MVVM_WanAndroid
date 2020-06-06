package com.example.piglet_mvvm.ui.my.points;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.bean.LevelDataBean;

import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/29
 */
public class MyPointsAdapter extends BaseQuickAdapter<LevelDataBean, BaseViewHolder> {

    public MyPointsAdapter(int layoutResId, @Nullable List<LevelDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, LevelDataBean item) {
        helper.setText(R.id.tv_title,item.getDesc());
        helper.setText(R.id.tv_coin_count,item.getCoinCount()+"");
    }

    @Override
    public void setNewData(@Nullable List<LevelDataBean> data) {
        super.setNewData(data);
    }
}
