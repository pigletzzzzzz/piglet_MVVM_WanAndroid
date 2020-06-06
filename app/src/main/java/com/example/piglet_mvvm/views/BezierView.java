package com.example.piglet_mvvm.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;


/**
 * Created By 刘纯贵
 * Created Time 2020/6/5
 */
public class BezierView extends View {

    private int mWidth,mHeight;//画布的宽带和高度
    private float cycleWidth,cycleHeight=60f;//周期的宽度和高度
    private Path pathRipple;//画的路径
    private Paint paintRipple;//画笔
    private float moveSet=0;//移动的值
    private ValueAnimator animator;//动画
    private boolean isStart=false;

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAnimator();
    }

    /**
     * 初始化动画
     */
    private void initAnimator() {
        animator=ValueAnimator.ofFloat(0,mWidth);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                moveSet= (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 初始化画的路径
     */
    private void initPath() {
        pathRipple=new Path();
        pathRipple.moveTo(-6*cycleWidth+moveSet,0);
        pathRipple.quadTo(-5*cycleWidth+moveSet,cycleHeight,-4*cycleWidth+moveSet,0);
        pathRipple.quadTo(-3*cycleWidth+moveSet,-cycleHeight,-2*cycleWidth+moveSet,0);
        pathRipple.quadTo(-cycleWidth+moveSet,cycleHeight,moveSet,0);
        pathRipple.quadTo(cycleWidth+moveSet,-cycleHeight,2*cycleWidth+moveSet,0);
        pathRipple.lineTo(2*cycleWidth+moveSet,mHeight/2);
        pathRipple.lineTo(-6*cycleWidth+moveSet,mHeight/2);
        pathRipple.close();
        pathRipple.setFillType(Path.FillType.WINDING);

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        paintRipple=new Paint();
        paintRipple.setStrokeWidth(2);
        paintRipple.setStyle(Paint.Style.FILL);
        paintRipple.setColor(Color.parseColor("#FFFFFF"));
        paintRipple.setAntiAlias(true);

    }

    /**
     * 画波纹
     * @param canvas
     */
    private void drawRipple(Canvas canvas) {
        initPath();
        canvas.drawPath(pathRipple,paintRipple);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight=h;
        mWidth=w;
        cycleWidth=mWidth/4;
        initAnimator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth/2,mHeight/2);
        drawRipple(canvas);
    }

    /**
     * 开始动画
     */
    public void start(){
        if(isStart) return;
        isStart=true;
        if(animator==null){
            initAnimator();
        }
        animator.start();
    }

    /**
     * 结束动画
     */
    public void stop(){
        if(!isStart) return;
        isStart=false;
        moveSet=0;
        animator.cancel();
        animator=null;
        invalidate();
    }
}
