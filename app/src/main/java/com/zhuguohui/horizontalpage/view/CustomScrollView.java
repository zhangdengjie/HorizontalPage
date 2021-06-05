package com.zhuguohui.horizontalpage.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 还可以滚动的时候,拦截父布局的触摸事件
 */
public class CustomScrollView extends ScrollView {

    private static final String TAG = "CustomScrollView";

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent: " + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isOnBottom() && !isOnTop()) {
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onTouchEvent: " + ev.getAction());
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(TAG, "onScrollChanged: ");
    }

    /**
     * 判断是否在顶部
     *
     * @return
     */
    private boolean isOnTop() {
        return getScrollY() == 0;
    }

    /**
     * 判断是否已经到了底部
     *
     * @return
     */
    public boolean isOnBottom() {
        return getChildAt(0).getHeight() - getHeight() == getScrollY();
    }
}
