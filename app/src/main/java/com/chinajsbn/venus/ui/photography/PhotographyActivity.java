package com.chinajsbn.venus.ui.photography;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.photography.PhotoCustomerFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoWedSuitFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoSelPhotographerFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoSelStylistFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoSelectWeddingFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoSimpleFragment;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.reside.ResideMenu;
import com.tool.widget.reside.ResideMenuItem;

/**
 * 婚纱摄影首页
 */
@ActivityFeature(layout = R.layout.activity_photography, statusBarColor = R.color.base_color)
public class PhotographyActivity extends MBaseFragmentActivity implements View.OnClickListener, ResideMenu.OnMenuListener {

    private ResideMenu resideMenu;

    private ResideMenuItem itemSimple;          //样片
    private ResideMenuItem itemCustom;          //客片
    private ResideMenuItem itemPrice;           //报价
    private ResideMenuItem itemSelectPhotographer;//选择摄影师
    private ResideMenuItem itemSelectStylist;     //选择造型师
    private ResideMenuItem itemSelectWedding;     //选婚纱


    @Override
    public void initialize() {
        resideMenu = new ResideMenu(this);
        resideMenu.attachToActivity(this);
        resideMenu.setBackground(R.color.gray);
        resideMenu.setMenuListener(this);

        itemSimple = new ResideMenuItem(this, R.mipmap.ic_launcher, "样片");                 //样片
        itemCustom = new ResideMenuItem(this, R.mipmap.ic_launcher, "客片");                 //客片
        itemPrice = new ResideMenuItem(this, R.mipmap.ic_launcher, "婚纱套系报价");          //报价
        itemSelectPhotographer = new ResideMenuItem(this, R.mipmap.ic_launcher, "选摄影师"); //选择摄影师
        itemSelectStylist = new ResideMenuItem(this, R.mipmap.ic_launcher, "选造型师");        //选择造型师
        itemSelectWedding = new ResideMenuItem(this, R.mipmap.ic_launcher, "选婚纱");         //选婚纱

        itemSimple.setOnClickListener(this);
        itemCustom.setOnClickListener(this);
        itemPrice.setOnClickListener(this);
        itemSelectPhotographer.setOnClickListener(this);
        itemSelectStylist.setOnClickListener(this);
        itemSelectWedding.setOnClickListener(this);

        resideMenu.addMenuItem(itemSimple, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemCustom, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemPrice, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSelectPhotographer, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSelectStylist, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemSelectWedding, ResideMenu.DIRECTION_LEFT);

        //
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
    }

    @OnClick(R.id.m_title_left_btn)
    public void openMenu(View view) {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v == itemSimple) {
            changeFragment(new PhotoSimpleFragment());
        } else if (v == itemCustom) {
            changeFragment(new PhotoCustomerFragment());
        } else if (v == itemPrice) {
            changeFragment(new PhotoWedSuitFragment());
        } else if (v == itemSelectPhotographer) {
            changeFragment(new PhotoSelPhotographerFragment());
        } else if (v == itemSelectStylist) {
            changeFragment(new PhotoSelStylistFragment());
        } else if (v == itemSelectWedding) {
            changeFragment(new PhotoSelectWeddingFragment());
        }

        resideMenu.closeMenu();
    }

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void openMenu() {
        //setStatusBarColor(getResources().getColor(R.color.gray));
    }

    @Override
    public void closeMenu() {
        //setStatusBarColor(getResources().getColor(R.color.base_color));
    }
}
