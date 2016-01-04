package com.tool.widget.mt_custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.chinajsbn.venus.R;

/**
 * Created by master on 15-8-5.
 */
public class MTView extends View {

    public MTView(Context context) {
        super(context);
    }

    public MTView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MTView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(getResources().getColor(R.color.green));
        canvas.drawColor(getResources().getColor(R.color.green));

        Paint txtPaint = new Paint();
        txtPaint.setColor(getResources().getColor(android.R.color.white));
        canvas.drawText("MTView", 50, 50, txtPaint);


    }
}
