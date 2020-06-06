package com.example.piglet_mvvm.ui.knowledgesystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.app.AppViewModelFactory;
import com.example.piglet_mvvm.base.BaseFragment;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.bean.SystemBean;
import com.example.piglet_mvvm.databinding.FragmentSystemItemBinding;
import com.example.piglet_mvvm.ui.adapter.SystemItemAdapter;
import com.example.piglet_mvvm.ui.home.HomeViewModel;
import com.example.piglet_mvvm.ui.knowledgesystem.system.SystemListActivity;
import com.example.piglet_mvvm.utils.ShowLoadingDialog;
import com.wzq.mvvmsmart.event.StateLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By 刘纯贵
 * Created Time 2020/5/18
 */
public class SystemItemFragment extends BaseFragment<FragmentSystemItemBinding, KnowledgeSystemViewModel> {

    List<SystemBean> datas = new ArrayList<>();
    private SystemItemAdapter systemItemAdapter;
    private ShowLoadingDialog showLoadingDialog;

    @Override
    public KnowledgeSystemViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return ViewModelProviders.of(this, factory).get(KnowledgeSystemViewModel.class);
    }

    @Override
    public void initData() {
        super.initData();
        showLoadingDialog = new ShowLoadingDialog(getContext());
        showLoadingDialog.Show();
        viewModel.requestSystemData();
    }

    @Override
    public void initViewObservable() {
        viewModel.stateLiveDataSystem.observe(this,systemBeans -> {
            datas = systemBeans;
            binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rv.setItemViewCacheSize(datas.size());
            systemItemAdapter = new SystemItemAdapter(getContext(),datas);
            systemItemAdapter.setOnItemClickListener(new SystemItemAdapter.OnItemClickListener() {
                @Override
                public void onClick(SystemBean bean, int pos) {
                    //点击事件
                    Intent intent = new Intent(getContext(), SystemListActivity.class);
                    intent.putExtra("system",bean);
                    intent.putExtra("pos",pos);
                    startActivity(intent);
                }
            });
            binding.rv.setAdapter(systemItemAdapter);
        });

        viewModel.stateLiveDataSystem.stateEnumMutableLiveData
                .observe(getActivity(), new Observer<StateLiveData.StateEnum>() {
                    @Override
                    public void onChanged(StateLiveData.StateEnum stateEnum) {
                        if (stateEnum.equals(StateLiveData.StateEnum.Idle)){
                            showLoadingDialog.dismiss();
                        }
                        if (stateEnum.equals(StateLiveData.StateEnum.Error)){
                            showLoadingDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_system_item;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }


}
