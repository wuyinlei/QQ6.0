package com.example.qqsliding.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by 若兰 on 2016/1/28.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class SwipeLayout extends FrameLayout {

    /**
     * 默认状态是关闭
     */
    private Status status = Status.Close;
    private OnSwipeLayoutListener swipeLayoutListener;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OnSwipeLayoutListener getSwipeLayoutListener() {
        return swipeLayoutListener;
    }

    public void setSwipeLayoutListener(OnSwipeLayoutListener swipeLayoutListener) {
        this.swipeLayoutListener = swipeLayoutListener;
    }

    /**
     * 定义三种状态
     */
    public enum Status {
        Close, Open, Draging
    }

    /**
     * 定义回调接口    这个在我们
     */
    public  interface OnSwipeLayoutListener {

        /**
         * 关闭
         *
         * @param mSwipeLayout
         */
        void onClose(SwipeLayout mSwipeLayout);

        /**
         * 打开
         *
         * @param mSwipeLayout
         */
        void onOpen(SwipeLayout mSwipeLayout);

        /**
         * 绘制
         *
         * @param mSwipeLayout
         */
        void onDraging(SwipeLayout mSwipeLayout);

        /**
         * 要去关闭
         */
        void onStartClose(SwipeLayout mSwipeLayout);

        /**
         * 要去开启
         */
        void onStartOpen(SwipeLayout mSwipeLayout);
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
    }

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        // c. 重写监听
        @Override
        public boolean tryCaptureView(View view, int id) {
            return true;
        }

        // 限定移动范围
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            // left
            if (child == mFrontView) {
                if (left > 0) {  //如果前view的左侧大于0，就返回0，不能右滑
                    return 0;
                } else if (left < -mRange) { //如果前view的左侧坐标小于了后view的宽度，就停止左滑
                    return -mRange;
                }
            } else if (child == mBackView) {
                if (left > mWidth) {   //如果后view的左侧大于了前view的宽度，就停止右滑
                    return mWidth;
                } else if (left < mWidth - mRange) {
                    //如果后view的左侧小于了，停止左滑
                    return mWidth - mRange;
                }
            }
            return left;
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            // 传递事件
            if (changedView == mFrontView) {
                //滑动的是前view，就去监听后view的偏移量
                mBackView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {
                //如果是滑动的后view，就去监听前view的偏移量
                mFrontView.offsetLeftAndRight(dx);
            }

            dispatchSwipeEvent();

            // 兼容老版本
            invalidate();

        }


        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            if (xvel == 0 && mFrontView.getLeft() < -mRange / 2.0f) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }

        }

    };
    private ViewDragHelper mDragHelper;
    private View mBackView;
    private View mFrontView;
    private int mHeight;
    private int mWidth;
    private int mRange;

    // b. 传递触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    protected void dispatchSwipeEvent() {

        //判断是否为空
        if (swipeLayoutListener != null) {
            swipeLayoutListener.onDraging(this);
        }

        // 记录上一次的状态
        Status preStatus = status;
        // 更新当前状态
        status = updateStatus();
        if (preStatus != status && swipeLayoutListener != null) {
            if (status == Status.Close) {
                swipeLayoutListener.onClose(this);
            } else if (status == Status.Open) {
                swipeLayoutListener.onOpen(this);
            } else if (status == Status.Draging) {
                if (preStatus == Status.Close) {
                    swipeLayoutListener.onStartOpen(this);
                } else if (preStatus == Status.Open) {
                    swipeLayoutListener.onStartClose(this);
                }
            }
        }
    }

    /**
     * 更新状态
     *
     * @return
     */
    private Status updateStatus() {

        //得到前view的左边位置
        int left = mFrontView.getLeft();
        if (left == 0) {
            //如果位置是0，就是关闭状态
            return Status.Close;
        } else if (left == -mRange) {
            //如果左侧边距是后view的宽度的负值，状态为开
            return Status.Open;
        }
        //其他状态就是拖拽
        return Status.Draging;
    }

    public void close() {
        close(true);
    }

    /**
     * 关闭
     *
     * @param isSmooth
     */
    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            //开始动画
            if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(false);
        }
    }

    public void open() {
        open(true);
    }

    /**
     * 打开
     *
     * @param isSmooth
     */
    public void open(boolean isSmooth) {
        int finalLeft = -mRange;
        if (isSmooth) {
            //开始动画
            if (mDragHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layoutContent(true);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 重置摆放位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 摆放位置
        layoutContent(false);
    }

    private void layoutContent(boolean isOpen) {
        // 摆放前View
        Rect frontRect = computeFrontViewRect(isOpen);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        // 摆放后View
        Rect backRect = computeBackViewViaFront(frontRect);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);

        // 调整顺序, 把mFrontView前置
        bringChildToFront(mFrontView);
    }

    private Rect computeBackViewViaFront(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, 0 + mHeight);
    }

    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, 0 + mHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 当xml被填充完毕时调用
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

    /**
     * 重写此方法，在这里面得到前后view的宽高，我们也可以重写onMessure（）方法
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = mFrontView.getMeasuredHeight();
        mWidth = mFrontView.getMeasuredWidth();

        mRange = mBackView.getMeasuredWidth();

    }

}
