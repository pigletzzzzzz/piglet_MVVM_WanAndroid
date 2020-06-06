package com.example.piglet_mvvm.ui.project;

import android.text.Html;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.bean.ArticleBean;
import com.example.piglet_mvvm.utils.StringUtils;
import com.wzq.mvvmsmart.utils.GlideLoadUtils;

import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/27
 */
public class ProjectAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {
    public ProjectAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.tv_author,item.getAuthor());
        helper.setText(R.id.tv_time,item.getNiceDate());
        helper.setText(R.id.tv_title,item.getTitle());
        if (item.getTags() != null && item.getTags().size() > 0){
            helper.setText(R.id.tv_tag,item.getTags().get(0).getName());
            helper.setVisible(R.id.tv_tag,true);
        }else {
            helper.setVisible(R.id.tv_tag,false);
        }
        if (TextUtils.isEmpty(item.getDesc())){
            helper.setVisible(R.id.tv_desc,false);
        }else {
            helper.setVisible(R.id.tv_desc,true);
            String desc = Html.fromHtml(item.getDesc()).toString();
            desc = StringUtils.removeAllBank(desc,2);
            helper.setText(R.id.tv_desc,desc);
        }
        if (item.isCollect()){
            helper.setImageResource(R.id.zan,R.mipmap.zan_y);
        }else {
            helper.setImageResource(R.id.zan,R.mipmap.zan_n);
        }

        helper.setText(R.id.tv_chapter_name,Html.fromHtml(formatChapterName(item.getChapterName(),item.getSuperChapterName())));
        GlideLoadUtils.loadRoundCornerImg(helper.getView(R.id.iv_img),item.getEnvelopePic(),R.mipmap.ic_launcher,5);
        helper.addOnClickListener(R.id.zan);
    }

    private static String formatChapterName(String... names) {
        StringBuilder format = new StringBuilder();
        for (String name : names) {
            if (!TextUtils.isEmpty(name)) {
                if (format.length() > 0) {
                    format.append("--");
                }
                format.append(name);
            }
        }
        return format.toString();
    }

    @Override
    public void setNewData(@Nullable List<ArticleBean> data) {
        super.setNewData(data);
    }
}
