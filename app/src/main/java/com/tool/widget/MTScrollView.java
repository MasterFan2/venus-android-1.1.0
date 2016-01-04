package com.tool.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by 13510 on 2015/9/2.
 */
public class MTScrollView extends ScrollView {
    public MTScrollView(Context context) {
        super(context);
    }

    public MTScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        //super.computeScroll();
    }
}
