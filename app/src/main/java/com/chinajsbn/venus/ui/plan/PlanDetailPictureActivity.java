package com.chinajsbn.venus.ui.plan;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.ImageItem;
import com.chinajsbn.venus.net.bean.SchemeDetail;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
import com.tool.widget.TouchImageView;

import java.util.ArrayList;

@ActivityFeature(layout = R.layout.activity_works_detail)
public class PlanDetailPictureActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.works_detail_viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.works_detail_bottom_layout)
    private RelativeLayout bottomLayout;

    @ViewInject(R.id.works_detail_digit_indicator_txt)
    private TextView digitTxt;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    private SchemeDetail detail;


    //标识文字是否    显示/隐藏    true:显示     false:隐藏
    private boolean show = true;
    private int size = 0;

    @Override
    public void initialize() {

        detail = (SchemeDetail) getIntent().getSerializableExtra("detail");

        detail.getImageList().remove(detail.getImageList().size() - 1);//因为添加了一个说明，所以在看大图的时候要把最后一个说明删除

        size = detail == null ? 0 : detail.getImageList().size();
        int tempPosition = getIntent().getIntExtra("position", 0);


        if(size > 0){
            viewPager.setAdapter(new MAdapter(detail.getImageList()));
            setDigitPosition(tempPosition);
            viewPager.setCurrentItem(tempPosition, true);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setDigitPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick(R.id.m_title_left_btn)
    public void onback(View view){
        animFinish();
    }

    private void setDigitPosition(int position) {
        digitTxt.setText((position + 1) + "/" + size);
    }

    class MAdapter extends PagerAdapter {
        ArrayList<ImageItem> imgList;

        public MAdapter(ArrayList<ImageItem> d) {
            this.imgList = d;
        }

        @Override
        public int getCount() {
            return imgList == null ? 0 : imgList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView img = (TouchImageView) LayoutInflater.from(context).inflate(R.layout.item_single_img_layout_centercrop, null);
            String url = imgList.get(position).getContentUrl();

            if (!TextUtils.isEmpty(url)) {
                if (DimenUtil.isHorizontal(url)) {
                    Picasso.with(context).load(url + "@" + DimenUtil.getHorizonalListViewDimension().getWidth() + "w_" + DimenUtil.getHorizonalListViewDimension().getHeight() + "h_50q").placeholder(R.drawable.loading).into(img);
                } else {
                    Picasso.with(context).load(url + "@" + DimenUtil.getVerticalListViewDimension().getWidth() + "w_" + DimenUtil.getVerticalListViewDimension().getHeight() + "h_50q").placeholder(R.drawable.loading).into(img);
                }

            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (show) {
                        hide();
                    } else {
                        show();
                    }
                    show = !show;
                }
            });
            container.addView(img);
            return img;
        }

        private void show() {
            titleView .animate().translationY( 0).setInterpolator(new DecelerateInterpolator(2)) ;
            bottomLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }

        private void hide() {
            titleView .animate().translationY(-titleView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomLayout.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            bottomLayout.animate().translationY(bottomLayout.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
