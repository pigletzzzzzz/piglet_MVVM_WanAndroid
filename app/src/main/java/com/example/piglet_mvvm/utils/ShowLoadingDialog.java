package com.example.piglet_mvvm.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.piglet_mvvm.R;
import com.mingle.widget.LoadingView;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/3
 */
public class ShowLoadingDialog {
    private Context mContext;
    private Dialog dialog;
    private View inflate;
    private LoadingView loadingView;
    public ShowLoadingDialog(Context context){
        this.mContext = context;
        dialog = new Dialog(context,R.style.MyDialog);
        inflate = LayoutInflater.from(context).inflate(R.layout.layout_loadingview, null);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        loadingView = inflate.findViewById(R.id.loadView);
        loadingView.setLoadingText("加载中...");
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity( Gravity.CENTER_VERTICAL);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
    }

    public void Show(){
        dialog.show();
    }

    public void dismiss(){
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
