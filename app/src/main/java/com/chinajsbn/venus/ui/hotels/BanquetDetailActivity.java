package com.chinajsbn.venus.ui.hotels;

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
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Banquet;
import com.chinajsbn.venus.net.bean.BanquetDetail;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.S;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
import com.tool.widget.TouchImageView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * create by master fan on 2015/09/01
 */
@ActivityFeature(layout = R.layout.activity_banquet_detail)
public class BanquetDetailActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @ViewInject(R.id.banquet_detail_bottom_layout)
    private RelativeLayout bottomLayout;

    @ViewInject(R.id.banquet_detail_viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.banquet_detail_tables_txt)
    private TextView tableTxt;

    @ViewInject(R.id.banquet_detail_pillar_txt)
    private TextView pillarTxt;

    @ViewInject(R.id.banquet_detail_area_txt)
    private TextView areaTxt;

    @ViewInject(R.id.banquet_detail_shap_txt)
    private TextView shapTxt;

    @ViewInject(R.id.banquet_detail_layerHeight_txt)
    private TextView heightTxt;

    @ViewInject(R.id.banquet_detail_price_txt)
    private TextView priceTxt;

    @ViewInject(R.id.banquet_detail_digit_indicator_txt)
    private TextView digitTxt;

    private Banquet banquets = null;

    private ArrayList<BanquetDetail> datas;

    private int size = 0;

    //标识文字是否    显示/隐藏    true:显示     false:隐藏
    private boolean show = true;

    @Override
    public void initialize() {

        banquets = (Banquet) getIntent().getSerializableExtra("args");

        setBanquet(banquets);

        String hotelId = getIntent().getStringExtra("hotelId");
        String detailId = getIntent().getStringExtra("detailId");

        HttpClient.getInstance().getBanquetDetail(hotelId, detailId, banquets.getBanquetHallId(), cb);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Banquet banquet = banquets.get(position);
//                setBanquet(position);
                digitTxt.setText((position + 1) + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private Callback<Base<ArrayList<BanquetDetail>>> cb = new Callback<Base<ArrayList<BanquetDetail>>>() {
        @Override
        public void success(Base<ArrayList<BanquetDetail>> detail, Response response) {
            if(detail.getCode() == 200){
                if(detail.getData() != null && detail.getData().size() > 0){
                    datas = detail.getData();
                    size = datas.get(0).getDetailImgList().length;
                    viewPager.setAdapter(new MAdapter(datas.get(0).getDetailImgList()));
                    digitTxt.setText(1 + "/" + size);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            S.o("获取数据失败， 请稍后再试...");
        }
    };

    /**
     *
     */
    private void setBanquet(Banquet banquet){
        titleView.setTitleText(banquet.getBanquetHallName());
        tableTxt.setText("桌数:" + banquet.getCapacity()+"桌");
        pillarTxt.setText("柱子:" + (banquet.getPillarNumber().equals("1") ? "有" : "无"));
        areaTxt.setText("面积:" +banquet.getArea()+"m²");
        shapTxt.setText("形状:" +banquet.getShape());
        heightTxt.setText("层高:" + banquet.getHeight() + "m");
        priceTxt.setText("消费:" + banquet.getLeastConsumption() + "/桌");

       // digitTxt.setText((position + 1) + "/" + size);
    }

    @OnClick(R.id.m_title_left_btn)
    public void back(View view){
        animFinish();
    }

    class MAdapter extends PagerAdapter {

        String [] imgList;
        public MAdapter(String [] imgs){
            imgList = imgs;
        }

        @Override
        public int getCount() {
            return imgList == null ? 0 : imgList.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TouchImageView img = (TouchImageView) LayoutInflater.from(context).inflate(R.layout.item_single_img_layout_centercrop, null);
            String url =imgList[position];
            if (!TextUtils.isEmpty(url)) {
                Picasso.with(context).load(url + DimenUtil.getHorizontalListViewStringDimension80Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(img);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(show){
                        hide();
                    }else{
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
            bottomLayout .animate().translationY( 0).setInterpolator(new DecelerateInterpolator(2)).start() ;
        }

        private void hide() {
            titleView .animate().translationY(- titleView.getHeight()).setInterpolator(new AccelerateInterpolator( 2));

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bottomLayout.getLayoutParams();
            int fabBottomMargin = lp. bottomMargin;
            bottomLayout .animate().translationY( bottomLayout.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator( 2)).start();
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
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
