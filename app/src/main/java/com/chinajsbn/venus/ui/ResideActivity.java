package com.chinajsbn.venus.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.reside.ResideMenu;
import com.tool.widget.reside.ResideMenuItem;

@ActivityFeature(layout = R.layout.activity_reside)
public class ResideActivity extends MBaseFragmentActivity implements View.OnClickListener{

    private ResideMenu resideMenu;
    private HomeMenuActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemOpt1;
    private ResideMenuItem itemOpt2;
    private ResideMenuItem itemOpt3;

    @Override
    public void initialize() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.mipmap.ic_launcher);
        resideMenu.attachToActivity(this);

        itemHome = new ResideMenuItem(this, 0,     "Home");
        itemOpt1 = new ResideMenuItem(this, 0,  "Profile");
        itemOpt2 = new ResideMenuItem(this, 0, "Calendar");
        itemOpt3 = new ResideMenuItem(this, 0, "Settings");

        itemHome.setOnClickListener(this);
        itemOpt1.setOnClickListener(this);
        itemOpt2.setOnClickListener(this);
        itemOpt3.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemOpt1, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemOpt2, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemOpt3, ResideMenu.DIRECTION_RIGHT);

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    @OnClick(R.id.title_bar_left_menu)
    public void leftOnClick(View view){
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public boolean onKeydown()
    {
        animFinish();
        return true;
    }

    @Override
    public void onClick(View view) {
//        if (view == itemHome){
//            changeFragment(new Opt1Fragment());
//        }else if (view == itemOpt1){
//            changeFragment(new Opt2Fragment());
//        }else if (view == itemOpt2){
//            changeFragment(new Opt3Fragment());
//        }else if (view == itemOpt3){
//            changeFragment(new Opt4Fragment());
//        }

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
}
