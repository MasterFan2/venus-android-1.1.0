package com.chinajsbn.venus.ui.photography;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.WeddingSuitDetail;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.detail.PhotoSuitTagCosmeticsFragment;
import com.chinajsbn.venus.ui.fragment.detail.PhotoSuitTagDetailFragment;
import com.chinajsbn.venus.ui.fragment.detail.PhotoSuitTagSceneryFragment;
import com.chinajsbn.venus.ui.fragment.detail.PhotoSuitTagclothingFragment;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.dragtop.DragTopLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * create by master fan on 15/07/17
 * suit detail
 */
@ActivityFeature(layout = R.layout.activity_suit_detail)
public class SuitDetailActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.drag_layout)
    private DragTopLayout dragLayout;

    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    private ModelPagerAdapter adapter;

    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip pagerSlidingTabStrip;

    //head element
    @ViewInject(R.id.suit_detail_head_img)
    private ImageView topImageView;

    @ViewInject(R.id.suit_detail_price_txt)
    private TextView priceTxt;

    @ViewInject(R.id.suit_detail_order_price_txt)
    private TextView orderTxt;

    @ViewInject(R.id.suit_detail_name_txt)
    private TextView nameTxt;


    @ViewInject(R.id.suit_detail_select_photographer_stylist_txt)
    private TextView selectPhotographerStylistTxt;

    private PagerModelManager factory;

    private WeddingSuitDetail data;

    @Override
    public void initialize() {
        String suitId   = getIntent().getStringExtra("suitId");
        String detailId = getIntent().getIntExtra("detailId", 0) + "";
        String url      =getIntent().getStringExtra("url");

        if(url != null && !TextUtils.isEmpty(url))
            Picasso.with(context).load(url + DimenUtil.getHorizontal() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(topImageView);
        HttpClient.getInstance().getWeddingSuitDetail(suitId, detailId, cb);
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    /**
     * @param detail
     */
    private void setData(WeddingSuitDetail detail) {
        if (detail.getCoverImage() != null && data.getCoverImage().size() > 11) {
            Picasso.with(context).load(detail.getCoverImage().get(0).getImageUrl() + DimenUtil.getHorizontal()).into(topImageView);
        }
        priceTxt.setText("￥ " + detail.getPrice());
        orderTxt.setText("订金￥ " + detail.getPrice());
        nameTxt.setText(detail.getProductName());
        String str = "";
        if(detail.getIsOptionalStylist() == 1){
            str = "可自选造型师";
        }

        if(detail.getIsOptionalCameraman() == 1){
            if(TextUtils.isEmpty(str)){
                str = "可自选摄影师";
            }else {
                str += "/摄影师";
            }
        }

        if(TextUtils.isEmpty(str)){
            selectPhotographerStylistTxt.setText("不可自选造型师/摄影师");
        }else{
            selectPhotographerStylistTxt.setText("(" + str +")");
        }
    }

    private Callback<Base<ArrayList<WeddingSuitDetail>>> cb = new Callback<Base<ArrayList<WeddingSuitDetail>>>() {

        @Override
        public void success(Base<ArrayList<WeddingSuitDetail>> suitDetailResp, Response response) {
            data = suitDetailResp.getData().get(0);
            setData(data);
            factory = new PagerModelManager();
            factory.addCommonFragment(getFragments(data), getTitles());
            adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
            viewPager.setAdapter(adapter);
            pagerSlidingTabStrip.setViewPager(viewPager);
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "获取数据错误");
            System.out.println(0);
        }
    };

    // Handle scroll event from fragments
    public void onEvent(Boolean b) {
        dragLayout.setTouchMode(b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private List<String> getTitles() {
        ArrayList<String> lists = new ArrayList<>();
        lists.add("详情");
        lists.add("服务");
        lists.add("服装");
        lists.add("化妆品");
        lists.add("景点");
        lists.add("流程");
        return lists;
    }

    private List<Fragment> getFragments(WeddingSuitDetail detail) {
        List<Fragment> list = new ArrayList<>();

        //详情
        Fragment tagDetailFragment = new PhotoSuitTagDetailFragment();
        Bundle detailArgs = new Bundle();
        detailArgs.putSerializable("detailArgs", detail.getDetailImages());
        tagDetailFragment.setArguments(detailArgs);
        list.add(tagDetailFragment);

        //服务
        Fragment tagServiceFragment = new PhotoSuitTagDetailFragment();
        Bundle serviceArgs = new Bundle();
        serviceArgs.putSerializable("detailArgs", detail.getServiceImages());
        tagServiceFragment.setArguments(serviceArgs);
        list.add(tagServiceFragment);

        //服装
        Fragment tagClothingFragment = new PhotoSuitTagclothingFragment();
        Bundle clothingArgs = new Bundle();
        clothingArgs.putSerializable("clothingArgs", detail.getClothShootImages());
        tagClothingFragment.setArguments(clothingArgs);
        list.add(tagClothingFragment);

        //化妆品
        Fragment tagCosmeticsFragment = new PhotoSuitTagCosmeticsFragment();
        Bundle cosmeticsArgs = new Bundle();
        cosmeticsArgs.putSerializable("cosmeticsArgs", detail.getCosmeticImages());
        tagCosmeticsFragment.setArguments(cosmeticsArgs);
        list.add(tagCosmeticsFragment);

        //景点
        Fragment tagSceneryFragment = new PhotoSuitTagSceneryFragment();
        Bundle sceneryArgs = new Bundle();
        sceneryArgs.putSerializable("suitArgs", detail.getBaseSampleImages());
        tagSceneryFragment.setArguments(sceneryArgs);
        list.add(tagSceneryFragment);

        //流程
        Fragment tagProcedureFragment = new PhotoSuitTagSceneryFragment();
        Bundle procedureArgs = new Bundle();
        procedureArgs.putSerializable("suitArgs", detail.getProcessImages());
        tagProcedureFragment.setArguments(procedureArgs);
        list.add(tagProcedureFragment);

        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
