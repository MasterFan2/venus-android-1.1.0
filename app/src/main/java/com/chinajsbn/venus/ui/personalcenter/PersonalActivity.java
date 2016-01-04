package com.chinajsbn.venus.ui.personalcenter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.center.CenterCommonFragment;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

@ActivityFeature(layout = R.layout.activity_personal)
public class PersonalActivity extends MBaseFragmentActivity {

    //
    private final int icons[] = {
            R.mipmap.ic_pcenter_modify_nor,
            R.mipmap.ic_pcenter_camera_nor,
            R.mipmap.ic_pcenter_home_nor,
            R.mipmap.ic_pcenter_plan_nor,
    };
    private final int icons_sel[] = {
            R.mipmap.ic_pcenter_modify_sel,
            R.mipmap.ic_pcenter_camera_sel,
            R.mipmap.ic_pcenter_home_sel,
            R.mipmap.ic_pcenter_plan_sel,
    };

    private int currentPosition = 0;

    private MPagerApdater adapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    @Override
    public void initialize() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                T.s(context, "position:=> " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //
        CenterCommonFragment firstFragment = new CenterCommonFragment();
        Bundle fiargs = new Bundle();
        fiargs.putInt("position", 0);
        firstFragment.setArguments(fiargs);

        CenterCommonFragment secendFragment = new CenterCommonFragment();
        Bundle seargs = new Bundle();
        seargs.putInt("position", 1);
        secendFragment.setArguments(seargs);

        CenterCommonFragment thirdFragment = new CenterCommonFragment();
        Bundle thargs = new Bundle();
        thargs.putInt("position", 2);
        thirdFragment.setArguments(thargs);

        CenterCommonFragment fourthFragment = new CenterCommonFragment();
        Bundle foargs = new Bundle();
        foargs.putInt("position", 3);
        fourthFragment.setArguments(foargs);

        fragments.add(firstFragment);
        fragments.add(secendFragment);
        fragments.add(thirdFragment);
        fragments.add(fourthFragment);

        //
        adapter = new MPagerApdater(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == currentPosition) return;
                tabLayout.getTabAt(currentPosition).setIcon(icons[currentPosition]);
                tabLayout.getTabAt(position).setIcon(icons_sel[position]);
//                tabLayout.getTabAt(currentPosition).setCustomView(adapter.getTabview(currentPosition, false));
//                tabLayout.getTabAt(position).setCustomView(adapter.getTabview(position, true));
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if(currentPosition == i){
                tab.setIcon(icons_sel[i]);
            }else{
                tab.setIcon(icons[i]);
            }
        }
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }


    @OnClick(R.id.m_title_left_btn)
    public void click(View view){
        animFinish();
    }


    class MPagerApdater extends FragmentPagerAdapter {


        public MPagerApdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        public View getTabview(int position, boolean checked){
            ImageView img = new ImageView(context);
            if(checked)
                img.setImageResource(icons_sel[position]);
            else
                img.setImageResource(icons[position]);
            return img;
        }
    }
}
