package com.chinajsbn.venus.ui.photography;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.net.bean.Team;
import com.chinajsbn.venus.net.bean.Work;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.photography.MajordomoAlbumFragment;
import com.chinajsbn.venus.ui.fragment.photography.MajordomoDescFragment;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.CircleImageView;
import com.tool.widget.MasterTitleView;
import com.tool.widget.dragtop.DragTopLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_majordomo_detail)
public class MajordomoDetailActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    private ModelPagerAdapter adapter;
    private PagerModelManager factory;

    private final String[] filters = new String[]{"作品"};
    private String currentSelectName = filters[0];

    @ViewInject(R.id.tab_layout)
    TabLayout tabLayout;

    @ViewInject(R.id.photo_head_img)
    private CircleImageView photoImg;

    @ViewInject(R.id.stylist_head_img)
    private CircleImageView stylistImg;

    @ViewInject(R.id.photo_name_txt)
    private TextView  photoNameTxt;

    @ViewInject(R.id.stylist_name_txt)
    private TextView  stylistNameTxt;

    @ViewInject(R.id.head_team_name_txt)
    private TextView  teamNameTxt;

    private String personId = "";
    private String headUrl = "";
    private boolean isMajordomo = false;
    private Team team;

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view){
        animFinish();
    }

    @Override
    public void initialize() {
        team = (Team) getIntent().getSerializableExtra("data");
        isMajordomo= getIntent().getBooleanExtra("isMajordomo", false);

        Picasso.with(context).load(team.getPhotographerDetail().getHead()).into(photoImg);
        Picasso.with(context).load(team.getStylistDetail().getHead()).into(stylistImg);

        photoNameTxt.setText(team.getPhotographerDetail().getPersonName());
        stylistNameTxt.setText(team.getStylistDetail().getPersonName());

        teamNameTxt.setText(team.getTeamName());

        if(isMajordomo) {//总监
            titleView.setTitleText("总监级摄影团队");
        }else {
            titleView.setTitleText("资深级摄影团队");
        }

        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doFiltrate(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        factory = new PagerModelManager();
        factory.addCommonFragment(getFragments(team), getTitles());
        adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(0, true);
        tabLayout.setScrollPosition(0, 0, true);
    }

    private void doFiltrate(TabLayout.Tab tab) {
        String tName = tab.getText().toString();
        if(tName.equals(currentSelectName)){
            return;
        }
        currentSelectName = tName;

        if(tName.equals(filters[0])){
           viewPager.setCurrentItem(0, true);
        }

    }

//    private Callback<Base<ArrayList<Photographer>>> cb = new Callback<Base<ArrayList<Photographer>>>() {
//
//        @Override
//        public void success(Base<ArrayList<Photographer>> resp, Response response) {
//
//            if(resp.getCode() == 200){
//                if(resp.getData() != null && resp.getData().size() > 0){
//                    Photographer photographer = resp.getData().get(0);
//                    factory = new PagerModelManager();
//                    factory.addCommonFragment(getFragments(photographer), getTitles());
//                    adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
//                    viewPager.setAdapter(adapter);
//
//                    //headImg
//                    nameTxt.setText(photographer.getPersonName());
//                    areaTxt.setText(photographer.getOwnedCompany());
//                    Picasso.with(context).load(headUrl).into(headImg);
//
//                    viewPager.setCurrentItem(0, true);
//                    tabLayout.setScrollPosition(0, 0, true);
//                }else {
//                    //无数据
//                }
//            }else {
//                //
//                T.s(context, "服务器维护中...");
//            }
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            T.s(context, "获取数据错误");
//        }
//    };

    private List<String> getTitles() {
        ArrayList<String> lists = new ArrayList<>();
        lists.add("作品");
//        lists.add("简介");
        return lists;
    }

    private List<Fragment> getFragments(Team team) {
        List<Fragment> list = new ArrayList<>();

        Fragment albumFragment = new MajordomoAlbumFragment();
        Bundle descArgs = new Bundle();
        descArgs.putSerializable("team", team);
        albumFragment.setArguments(descArgs);

//        Fragment descFragment = new MajordomoDescFragment();
//        descArgs.putSerializable("photographer", photographer);
//        descFragment.setArguments(descArgs);

        list.add(albumFragment);
//        list.add(descFragment);

        return list;
    }

    // Handle scroll event from fragments

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
