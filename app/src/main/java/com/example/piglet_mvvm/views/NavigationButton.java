package com.example.piglet_mvvm.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.piglet_mvvm.R;


public class NavigationButton extends FrameLayout {
    private Fragment mFragment = null;
    private Class<?> mClx;
    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mDot;
    private String mTag;

    public NavigationButton(Context context) {
        super(context);
        init();
    }

    public NavigationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NavigationButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_nav_item, this, true);

        mIconView = (ImageView) findViewById(R.id.nav_iv_icon);
        mTitleView = (TextView) findViewById(R.id.nav_tv_title);
        mDot = (TextView) findViewById(R.id.nav_tv_dot);
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTitleView.setSelected(selected);

        if(selected) {
            mTitleView.setTextColor(ContextCompat.getColor(mTitleView.getContext(), R.color.text_select_color));
        } else {
            mTitleView.setTextColor(ContextCompat.getColor(mTitleView.getContext(), R.color.text_normal_color));
        }
    }

    public void showRedDot(int count) {
        mDot.setVisibility(count > 0 ? VISIBLE : GONE);
     //  count=132;
        String scount = String.valueOf(count);

        if (count > 99) {
            scount = "99+";
        }
        mDot.setText(scount);
//        int dp = 12;
//        if (count > 9 && count <= 99) {
//            dp = 14;
//        } else if (count > 99) {
//            dp = 16;
//        }
//        int width = DensityUtils.dip2px(getContext(), dp);
//
//        mDot.setWidth(width);
//        mDot.refreshDrawableState();
//        mDot. requestLayout();
//        mDot.invalidate();

    }

    public void init(@DrawableRes int resId, @StringRes int strId, Class<?> clx) {
        mIconView.setImageResource(resId);
        mTitleView.setText(strId);
        mClx = clx;
        mTag = mClx.getName();
    }

    public Class<?> getClx() {
        return mClx;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment( Fragment fragment) {
        this.mFragment = fragment;
    }

    public String getTag() {
        return mTag;
    }


}
