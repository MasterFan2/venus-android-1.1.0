package com.tool.widget.MyScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by 13510 on 2015/12/18.
 */
public class MyScrollView extends ScrollView {

    GestureDetector gestureDetector;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(new Yscroll());
        setFadingEdgeLength(0);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
    }

    class Yscroll extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //控制手指滑动的距离
            if (Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
}
