package com.chinajsbn.venus.ui.photography;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/10/21.
 */
public class MTMajordomoDetailActivity extends AppCompatActivity {

    private Context context;

    //    @ViewInject(R.id.view_pager)
    private ViewPager viewPager;

    private final String[] filters = new String[]{"作品", "简介"};
    private String currentSelectName = filters[0];

    //    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    //    @ViewInject(R.id.head_img)
    private ImageView headImg;

    //    @ViewInject(R.id.head_name_txt)
    private TextView nameTxt;

    //    @ViewInject(R.id.head_area_txt)
    private TextView areaTxt;

    private String personId = "";
    private String headUrl = "";
    private boolean isStylist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_majordomo_detail);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        headImg = (ImageView) findViewById(R.id.head_img);
        nameTxt = (TextView) findViewById(R.id.head_name_txt);
        areaTxt = (TextView) findViewById(R.id.head_area_txt);

        setupToolbar();

        setupViewPager();

        setupCollapsingToolbar();
    }

    private void initialize() {
        personId = getIntent().getStringExtra("personId");
        headUrl  = getIntent().getStringExtra("headUrl");
        isStylist= getIntent().getBooleanExtra("isStylist", false);

        if(isStylist) {//造型师
            HttpClient.getInstance().getStylistDetail(personId, cb);
        }else {
            HttpClient.getInstance().getPhotographerDetail(personId, cb);
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
    }

    private void doFiltrate(TabLayout.Tab tab) {
        String tName = tab.getText().toString();
        if(tName.equals(currentSelectName)){
            return;
        }
        currentSelectName = tName;

        if(tName.equals(filters[0])){
            viewPager.setCurrentItem(0, true);
        }else if(tName.equals(filters[1])){
            viewPager.setCurrentItem(1, true);
        }

    }

    private Callback<Base<ArrayList<Photographer>>> cb = new Callback<Base<ArrayList<Photographer>>>() {

        @Override
        public void success(Base<ArrayList<Photographer>> resp, Response response) {

            if(resp.getCode() == 200){
                if(resp.getData() != null && resp.getData().size() > 0){
                    Photographer photographer = resp.getData().get(0);
                   /* factory = new PagerModelManager();
                    factory.addCommonFragment(getFragments(photographer), getTitles());
                    adapter = new ModelPagerAdapter(getSupportFragmentManager(), factory);
                    viewPager.setAdapter(adapter);*/

//                    //headImg
                    nameTxt.setText(photographer.getPersonName());
                    areaTxt.setText(photographer.getOwnedCompany());
                    Picasso.with(context).load(headUrl).into(headImg);

                    viewPager.setCurrentItem(0, true);
                    tabLayout.setScrollPosition(0, 0, true);
                }else {
                    //无数据
                }
            }else {
                //
                T.s(context, "服务器维护中...");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "获取数据错误");
        }
    };

    private void setupCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(
                R.id.collapse_toolbar);

        collapsingToolbar.setTitleEnabled(false);
    }

    private void setupViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TabbedCoordinatorLayout");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
       /* adapter.addFrag(new TabFragment(), "Tab 1");
        adapter.addFrag(new TabFragment(), "Tab 2");*/

        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
