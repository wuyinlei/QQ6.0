package com.example.qqsliding.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.qqsliding.R;
import com.example.qqsliding.utils.Utils;

/**
 * Created by 若兰 on 2016/2/5.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class ViscosityListener implements ViscosityView.OnDisappearListener,View.OnTouchListener {


    private WindowManager mWm;
    private WindowManager.LayoutParams mParams;
    private ViscosityView mViscosityView;
    private View point;
    private int number;
    private final Context mContext;

    private Handler mHandler;

    public ViscosityListener(Context mContext, View point) {
        this.mContext = mContext;
        this.point = point;
        this.number = (Integer) point.getTag();

        mViscosityView = new ViscosityView(mContext);

        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSLUCENT;

        mHandler= new Handler(mContext.getMainLooper());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        ViewParent parent = v.getParent();
        int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            // 当按下时，将自定义View添加到WindowManager中


            // 请求其父级View不拦截Touch事件
            parent.requestDisallowInterceptTouchEvent(true);

            point.setVisibility(View.INVISIBLE);

            Log.d("TAG",
                    "rawX: " + event.getRawX() + " rawY: " + event.getRawY());

            // 初始化当前点击的item的信息，数字及坐标
            mViscosityView.setStatusBarHeight(Utils.getStatusBarHeight(v));
            mViscosityView.setNumber(number);
            mViscosityView.initCenter(event.getRawX(), event.getRawY());
            mViscosityView.setOnDisappearListener(this);

            // 执行添加方法
            mWm.addView(mViscosityView, mParams);
        } if (action == MotionEvent.ACTION_UP){
            parent.requestDisallowInterceptTouchEvent(false);
        }

        // 将所有touch事件转交给GooView处理
        mViscosityView.onTouchEvent(event);
        return true;
    }


    @Override
    public void onDisappear(PointF mDragCenter) {
        if (mWm != null && mViscosityView.getParent() != null) {
            mWm.removeView(mViscosityView);

            //播放气泡爆炸动画
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.anim_bubble_pop);
            AnimationDrawable mAnimDrawable = (AnimationDrawable) imageView
                    .getDrawable();

            final BubbleLayout bubbleLayout = new BubbleLayout(mContext);
            bubbleLayout.setCenter((int) mDragCenter.x, (int) mDragCenter.y
                    - Utils.getStatusBarHeight(mViscosityView));

            bubbleLayout.addView(imageView, new FrameLayout.LayoutParams(
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.FrameLayout.LayoutParams.WRAP_CONTENT));

            mWm.addView(bubbleLayout, mParams);

            mAnimDrawable.start();

            // 播放结束后，删除该bubbleLayout
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mWm.removeView(bubbleLayout);
                }
            }, 500);
        }

    }

    @Override
    public void onReset(boolean isOutOfRange) {
        // 当气泡弹回时，去除该View，等下次ACTION_DOWN的时候再添加
        if (mWm != null && mViscosityView.getParent() != null) {
            mWm.removeView(mViscosityView);
        }
    }
}
