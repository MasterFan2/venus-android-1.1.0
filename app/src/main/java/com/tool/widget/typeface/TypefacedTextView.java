package com.tool.widget.typeface;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.chinajsbn.venus.R;

public class TypefacedTextView extends TextView {
    public TypefacedTextView(Context context) {
        this(context, null);
    }

    public TypefacedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypefacedTextView(Context context, AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode())
            return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView, defStyle, 0);
        String typeface = a.getString(R.styleable.TypefacedTextView_typeface);
        if (!TextUtils.isEmpty(typeface)) {
            Typeface face = Typeface.createFromAsset(getContext().getAssets(), typeface);
            if (face != null)
                setTypeface(face);
        }
        a.recycle();
    }
}