package com.chinajsbn.venus.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.scroll.BezelImageView;
import com.tool.widget.scroll.BooheeScrollView;
import com.tool.widget.scroll.BuildLayerLinearLayout;

@ActivityFeature(layout = R.layout.activity_scroll, statusBarColor = R.color.base_color)
public class ScrollActivity extends BaseActivity {

    @ViewInject(R.id.scroll_horizon)
    private BooheeScrollView booheeScrollView;

    @ViewInject(R.id.scroll_linearLayout)
    private BuildLayerLinearLayout linearLayout;

    @Override
    public void onKeydown() {
        animFinish();
    }

    @Override
    public void initialize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (displayMetrics.widthPixels - 60)/3;
        int height = (int)(width/340f * 600);
        int viewWidth = 30;

        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(viewWidth, 0));
        linearLayout.addView(view);

//        CardView cardView1 = new CardView(this);
//        cardView1.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        linearLayout.addView(cardView1);
//        TextView textView = new TextView(this);
//        textView.setText("hello");
//        textView.setGravity(Gravity.CENTER);
//        cardView1.addView(textView);
//
//        cardView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ScrollActivity.this, ScrollDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//            }
//        });

        BezelImageView imageView1 = new BezelImageView(this);
        imageView1.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        linearLayout.addView(imageView1);
        imageView1.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.ic_launcher, width, height));
        imageView1.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.s(ScrollActivity.this, "This is one");
            }
        });

        BezelImageView imageView2 = new BezelImageView(this);
        imageView2.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        linearLayout.addView(imageView2);
        imageView2.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.ic_launcher, width, height));
        imageView2.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));

        BezelImageView imageView3 = new BezelImageView(this);
        imageView3.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        linearLayout.addView(imageView3);
        imageView3.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.ic_launcher, width, height));
        imageView3.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
//
//        BezelImageView imageView4 = new BezelImageView(this);
//        imageView4.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        linearLayout.addView(imageView4);
//        imageView4.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.pic4, width, height));
//        imageView4.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
//
//        BezelImageView imageView5 = new BezelImageView(this);
//        imageView5.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        linearLayout.addView(imageView5);
//        imageView5.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.pic5, width, height));
//        imageView5.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
//
//        BezelImageView imageView6 = new BezelImageView(this);
//        imageView6.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        linearLayout.addView(imageView6);
//        imageView6.setImageBitmap(decodeSampledBitmapFromResource(getResources(),  R.mipmap.pic6, width, height));
//        imageView6.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));
//
//        BezelImageView imageView7 = new BezelImageView(this);
//        imageView7.setLayoutParams(new LinearLayout.LayoutParams(width, height));
//        linearLayout.addView(imageView7);
//        imageView7.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.pic7, width, height));
//        imageView7.setMaskDrawable(getResources().getDrawable(R.drawable.roundrect));


//        imageView7.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T.s(ScrollActivity.this,"imageView7");
//            }
//        });

        view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(30, 0));
        linearLayout.addView(view);

        booheeScrollView.setChildViews(new View[]{
                imageView1, imageView2, imageView3});

        booheeScrollView.setScrollChangeListener(new BooheeScrollView.OnScrollChangeListener() {
            @Override
            public void OnScrollChange(int centerViewIndex) {

            }
        });
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

//    @OnClick(R.id.m_title_left_btn)
//    public void onBack(View view){
//        animFinish();
//    }
}
