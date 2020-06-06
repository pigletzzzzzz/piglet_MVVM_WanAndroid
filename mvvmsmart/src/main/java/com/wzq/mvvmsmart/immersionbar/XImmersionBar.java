package com.wzq.mvvmsmart.immersionbar;

import android.app.Activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

/**
 * Created by 黄卫华(wayhua@126.com) on 2019/11/26 0026.
 * 主要是统一ImmersionBar的编写风格，使用
 * XImmersionBar后可以保证风格统一，同时
 * 修改起来方便，如果觉得不够灵活，特殊情况下也可以
 * 参照原来的编写。
 * 主要使用到三个参数，Fragment/activity ,toolbar(id,实体），是否显示深色字体颜色
 */
public class XImmersionBar {
    /***
     * 简单的使用，传入fragment以及toolbar的Id
     * @param fragment
     * @param toolbar id
     */
    public static void immersion(@NonNull Fragment fragment, @IdRes int toolbar) {
        immersion(fragment, toolbar, true);
    }

    public static void immersion(@NonNull Fragment fragment, @IdRes int toolbar, boolean isDarkFont) {
        getWith(fragment)
                .statusBarDarkFont(isDarkFont)
                .titleBar(toolbar)
                .init();
    }

    public static void immersion(@NonNull Fragment fragment, Toolbar toolbar) {
        immersion(fragment, toolbar, true);
    }

    public static void immersion(@NonNull Fragment fragment, Toolbar toolbar, boolean isDarkFont) {
        getWith(fragment)
                .statusBarDarkFont(isDarkFont)
                .titleBar(toolbar)
                .init();
    }

    public static void immersion(@NonNull Activity activity, @IdRes int toolbar) {
        immersion(activity, toolbar, true);
    }

    public static void immersion(@NonNull Activity activity, @IdRes int toolbar, boolean isDarkFont) {
        getWith(activity)
                .statusBarDarkFont(isDarkFont)
                .titleBar(toolbar)
                .init();

    }

    public static void immersion(@NonNull Activity activity, Toolbar toolbar) {
        immersion(activity, toolbar, true);
    }

    public static void immersion(@NonNull Activity activity, Toolbar toolbar, boolean isDarkFont) {
        getWith(activity)
                .statusBarDarkFont(true)
                .titleBar(toolbar)
                .init();

    }

    public static void immersion(@NonNull Fragment fragment) {
        immersion(fragment, true);
    }

    public static void immersion(@NonNull Fragment fragment, boolean isDarkFont) {
        getWith(fragment)
                .statusBarDarkFont(isDarkFont)
                .init();
    }

    public static void immersion(@NonNull Activity activity) {
        immersion(activity, true);
    }

    public static void immersion(@NonNull Activity activity, boolean isDarkFont) {
        getWith(activity)
                .statusBarDarkFont(isDarkFont)
                .init();
    }


    /***
     * 进行了一些简单的初始化，
     * 如navigationBarColor为黑色
     *
     * @param fragment
     * @return
     */
    public static ImmersionBar getWith(@NonNull Fragment fragment) {
        return ImmersionBar
                .with(fragment)
                .navigationBarColor(android.R.color.black);
    }

    /***
     * 进行了一些简单的初始化，
     * 如navigationBarColor为黑色
     * @param activity
     * @return
     */
    public static ImmersionBar getWith(@NonNull Activity activity) {
        return ImmersionBar.with(activity)
                .navigationBarColor(android.R.color.black)
                .statusBarDarkFont(true);
    }
}
