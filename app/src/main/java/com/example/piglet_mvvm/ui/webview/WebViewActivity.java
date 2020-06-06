package com.example.piglet_mvvm.ui.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.piglet_mvvm.BR;
import com.example.piglet_mvvm.R;
import com.example.piglet_mvvm.base.BaseActivity;
import com.example.piglet_mvvm.base.BaseViewModel;
import com.example.piglet_mvvm.databinding.FragmentWebviewBinding;

/**
 * Created By 刘纯贵
 * Created Time 2020/6/2
 */
public class WebViewActivity extends BaseActivity<FragmentWebviewBinding, BaseViewModel> {

    private String url = "http://www.baidu.com";
    private String title;

    @Override
    public void initData() {
        super.initData();

        Intent intent = getIntent();
        url = intent.getStringExtra("link");
        title = intent.getStringExtra("title");

        binding.edtSearchWeb.setText(title);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        //声明WebSettings子类
        WebSettings webSettings = binding.wv.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        binding.wv.loadUrl(url);
        binding.wv.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                binding.wv.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        binding.wv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    binding.pb.setVisibility(View.GONE);
                }else {
                    binding.pb.setVisibility(View.VISIBLE);
                    binding.pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.fragment_webview;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }
}
