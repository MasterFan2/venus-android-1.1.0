package com.chinajsbn.venus.ui.photography;

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
import com.chinajsbn.venus.net.bean.Dress;
import com.chinajsbn.venus.net.bean.ImageItem;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
import com.tool.widget.TouchImageView;

import java.util.ArrayList;
import java.util.List;

@ActivityFeature(layout = R.layout.activity_works_detail)
public class SimpleDetailPictureActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.works_detail_viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.works_detail_bottom_layout)
    private RelativeLayout bottomLayout;

    @ViewInject(R.id.works_detail_digit_indicator_txt)
    private TextView digitTxt;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    private ArrayList<Simple> orderList;

    //从案例过来的数据
    private ArrayList<ImageItem> orderPlanList;
    private boolean isPlan = false;

    //从婚纱礼服过来的数据
    private ArrayList<Dress> dressList;
    private boolean isDress = false;

    //标识文字是否    显示/隐藏    true:显示     false:隐藏
    private boolean show = true;
    private int size = 0;

    @Override
    public void initialize() {

        isPlan = getIntent().getBooleanExtra("isPlan", false);
        isDress= getIntent().getBooleanExtra("isDress", false);

        String url = getIntent().getStringExtra("url");
        int tempPosition = 0;

        if(isPlan) {
            orderPlanList = (ArrayList<ImageItem>) getIntent().getSerializableExtra("dataList");
            size = orderPlanList == null ? 0 : orderPlanList.size();
            for (int i = 0; i < size; i++) {
                if(orderPlanList.get(i).getContentUrl().equals(url)){
                    tempPosition = i;
                    break;
                }
            }
            if(size > 0){
                viewPager.setAdapter(new MAdapter(orderPlanList));
                setDigitPosition(tempPosition);
                viewPager.setCurrentItem(tempPosition, true);
            }
        }else if(isDress) {
            dressList = (ArrayList<Dress>) getIntent().getSerializableExtra("dataList");
            size = dressList == null ? 0 : dressList.size();
            for (int i = 0; i < size; i++) {
                if(dressList.get(i).getImageUrl().equals(url)){
                    tempPosition = i;
                    break;
                }
            }
            if(size > 0){
                viewPager.setAdapter(new MAdapter(dressList, 0));
                setDigitPosition(tempPosition);
                viewPager.setCurrentItem(tempPosition, true);
            }
        } else {
            orderList = (ArrayList<Simple>) getIntent().getSerializableExtra("dataList");
            size = orderList == null ? 0 : orderList.size();
            for (int i = 0; i < size; i++) {
                if(orderList.get(i).getContentUrl().equals(url)){
                    tempPosition = i;
                    break;
                }
            }
            if(size > 0){
                viewPager.setAdapter(new MAdapter(orderList));
                setDigitPosition(tempPosition);
                viewPager.setCurrentItem(tempPosition, true);
            }
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

    private void setDigitPosition(int position) {
        digitTxt.setText((position + 1) + "/" + size);
    }

    class MAdapter extends PagerAdapter {
        ArrayList<Simple> orderList;
        List<ImageItem> planOrderList;
        ArrayList<Dress> aDressList;

        public MAdapter(List<ImageItem> l){
            planOrderList = l;
        }

        public MAdapter(ArrayList<Simple> d) {
            this.orderList = d;
        }

        public MAdapter(ArrayList<Dress> d, int a) {
            this.aDressList = d;
        }

        @Override
        public int getCount() {
            if(isPlan)        return planOrderList == null ? 0 : planOrderList.size();
            else if(isDress)  return aDressList == null ? 0 : aDressList.size();
            else              return orderList == null ? 0 : orderList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView img = (TouchImageView) LayoutInflater.from(context).inflate(R.layout.item_single_img_layout_centercrop, null);
            String url ;
            if(isPlan)       url = planOrderList.get(position).getContentUrl();
            else if(isDress) url = aDressList.get(position).getImageUrl();
            else             url = orderList.get(position).getContentUrl();

            if (!TextUtils.isEmpty(url)) {
                if (DimenUtil.isHorizontal(url)) {
                    Picasso.with(context).load(url + DimenUtil.getHorizontalListViewStringDimension80Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(img);
                } else {
                    Picasso.with(context).load(url + DimenUtil.getVerticalListViewStringDimension80Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(img);
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

    @OnClick(R.id.m_title_left_btn)
    public void onback(View view){
        animFinish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
