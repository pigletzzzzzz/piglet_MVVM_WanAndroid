package com.example.piglet_mvvm.ui.knowledgesystem;

import androidx.fragment.app.Fragment;

import com.example.piglet_mvvm.base.BaseKnowledgeSystemFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/9
 */
public class KnowledgeSystemFragment extends BaseKnowledgeSystemFragment {

    @Override
    protected List<Fragment> pagerFragment() {
        List<Fragment> list = new ArrayList<>();
        list.add(new SystemItemFragment());
        list.add(new NavigationItemFragment());
        return list;
    }

    @Override
    protected List<String> pagerTitleString() {
        List<String> list = new ArrayList<>();
        list.add("体系");
        list.add("导航");
        return list;
    }
}
