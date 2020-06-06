package com.example.piglet_mvvm.base;


import androidx.databinding.ViewDataBinding;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.piglet_mvvm.utils.MaterialDialogUtils;
import com.wzq.mvvmsmart.base.BaseFragmentMVVM;


public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseFragmentMVVM<V, VM> {
    public final String TAG = getClass().getSimpleName();
    private MaterialDialog dialog;

    public void showLoading(String title) {
        if (dialog != null) {
            dialog = dialog.getBuilder().title(title).build();
            dialog.show();
        } else {
            MaterialDialog.Builder builder = MaterialDialogUtils.showIndeterminateProgressDialog(getActivity(), title, true);
            dialog = builder.show();
        }
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
