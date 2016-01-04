package com.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.tool.widget.common.EditText;

/**
 * Created by MasterFan on 2015/7/9.
 * description:enhance EditText
 */
public class MTEditText extends FrameLayout {

    private int dividerPadding;

    private TextView leftLabel;//
    private TextView rightLebel;//
    private EditText inputEdit;//
    private TextView divider;

    /**
     * ********************************
     * //                             //
     * //          Constructor        //
     * //                             //
     * ********************************
     */

    public MTEditText(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MTEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MTEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /***********************************
     //                             //
     //          initialize        //
     //                             //
     **********************************/
    /**
     * 加载布局文件的配置
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        removeAllViews();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MTEditText, defStyleAttr, 0);

        //1.leftLabel
        leftLabel = new TextView(context);
        String labelText = a.getString(R.styleable.MTEditText_mt_labelText);

        if (!TextUtils.isEmpty(labelText)) {
            leftLabel.setText(labelText);
        }
        addView(leftLabel, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        //2.EditText
        inputEdit = new EditText(context);
        String hintText = a.getString(R.styleable.MTEditText_mt_hintText);
        if (!TextUtils.isEmpty(hintText)) {
            inputEdit.setHint(hintText);
        }
        addView(inputEdit, 1, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        //3.rightLabel;
        rightLebel = new TextView(context);
        rightLebel.setText(" X ");
        addView(rightLebel, 2, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        //4.divider
        divider = new TextView(context);
        divider.setBackgroundResource(R.color.gray_800);
        dividerPadding = a.getDimensionPixelOffset(R.styleable.MTEditText_mt_dividerPadding, 0);
        addView(divider, 3, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        a.recycle();
    }

    /**
     * ********************************
     * //                             //
     * //          implements        //
     * //                             //
     * ********************************
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        int childLeft = getPaddingLeft();
//        int childRight = right - left - getPaddingRight();
//        int childTop = getPaddingTop();
//        int childBottom = bottom - top - getPaddingBottom();
//
//        int tempLeftBottom =  childTop + leftLabel.getMeasuredHeight();
//        leftLabel.item_personal_center_prefix(childLeft, childTop, childRight, tempLeftBottom);
//        childTop += leftLabel.getMeasuredHeight();
//
//        int tempRightTop =  childBottom - rightLebel.getMeasuredHeight();
//        rightLebel.item_personal_center_prefix(childLeft, tempRightTop, childRight, childBottom);
//        childBottom -= rightLebel.getMeasuredHeight();
//
//
//        inputEdit.item_personal_center_prefix(childLeft, childTop, childRight, childBottom);
        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childBottom = leftLabel.getMeasuredHeight();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        leftLabel.measure(w, h);
        inputEdit.measure(w, h);
        rightLebel.measure(w, h);
        divider.measure(w, h);

        int childRight = leftLabel.getMeasuredWidth();
        leftLabel.layout(childLeft, childTop, childRight, childBottom);

        childLeft += leftLabel.getMeasuredWidth();
        childRight = right - getPaddingRight() - rightLebel.getMeasuredWidth();
        childBottom = inputEdit.getMeasuredHeight();
        inputEdit.layout(childLeft, childTop, childRight, childBottom);

        childLeft = right - rightLebel.getMeasuredWidth() - getPaddingRight();
        childRight = right - getPaddingRight();
        childBottom = rightLebel.getMeasuredHeight();
        rightLebel.layout(childLeft, childTop, childRight, childBottom);

        childLeft = getPaddingLeft();
        childRight= right - getPaddingRight();
        childTop  = getPaddingTop() + inputEdit.getMeasuredHeight();
        childBottom = childTop + 1;
        divider.layout(childLeft, childTop + dividerPadding, childRight, childBottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int tempWidthSpec = widthMode == MeasureSpec.UNSPECIFIED ? widthMeasureSpec : MeasureSpec.makeMeasureSpec(widthSize - getPaddingLeft() - getPaddingRight(), widthMode);
        int tempHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int labelWidth = 0;
        int labelHeight = 0;
        int inputWidth = 0;
        int inputHeight = 0;
        int supportWidth = 0;
        int supportHeight = 0;

        int dividerWidth = 0;
        int dividerHeight = 0;

        //
        leftLabel.measure(tempWidthSpec, tempHeightSpec);
        labelWidth = leftLabel.getMeasuredWidth();
        labelHeight = leftLabel.getMeasuredHeight();

        inputEdit.measure(tempWidthSpec, tempHeightSpec);
        inputWidth = inputEdit.getMeasuredWidth();
        inputHeight = inputEdit.getMeasuredHeight();

        rightLebel.measure(tempWidthSpec, tempHeightSpec);
        supportWidth = rightLebel.getMeasuredWidth();
        supportHeight = rightLebel.getMeasuredHeight();

        divider.measure(tempWidthSpec, tempHeightSpec);
        dividerWidth = divider.getMeasuredWidth();
        dividerHeight = divider.getMeasuredHeight();

        int width = 0;
        int height = 0;

        switch (widthMode) {//calculate view's width
            case MeasureSpec.UNSPECIFIED:
                width = Math.max(labelWidth, Math.max(Math.max(inputWidth, dividerWidth), supportWidth)) + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSize, Math.max(labelWidth, Math.max(Math.max(inputWidth, dividerWidth), supportWidth)) + getPaddingLeft() + getPaddingRight());
                break;
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
        }

        switch (heightMode) {//calculate view's height
            case MeasureSpec.UNSPECIFIED://未指定大小
                height = labelHeight + inputHeight + supportHeight + dividerHeight + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.AT_MOST://最大尺寸
                height = Math.min(heightSize, inputHeight + 1 + getPaddingTop() + getPaddingBottom());
                break;
            case MeasureSpec.EXACTLY://精确尺寸
                height = heightSize;
                break;
        }

        setMeasuredDimension(width, height);

        tempWidthSpec = MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY);

        leftLabel.measure(tempWidthSpec, tempHeightSpec);

        inputEdit.measure(tempWidthSpec, tempHeightSpec);

        rightLebel.measure(tempWidthSpec, tempHeightSpec);

        divider.measure(tempWidthSpec, tempHeightSpec);
    }
}
