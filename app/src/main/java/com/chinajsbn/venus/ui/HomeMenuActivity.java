package com.chinajsbn.venus.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.HomeMenu;
import com.chinajsbn.venus.net.bean.MenuLink;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.MenuConfig;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.common.Button;
import com.tool.widget.dialog.Holder;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 主菜单
 * <p/>
 * create by masterFan
 */
@ActivityFeature(layout = R.layout.activity_home_menu, statusBarColor = R.color.gray_700)
public class HomeMenuActivity extends MBaseFragmentActivity {

    //menu data
    private List<HomeMenu> menuData;
    private HomeMenu homeMenu;
    //------------

    @ViewInject(R.id.home_container)
    private LinearLayout container;

    //-----*------
    private Holder holder;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static Fragment[] mFragments;
    private DbUtils dbUtils;

    @Override
    public boolean onKeydown() {
        return true;
    }

    /**
     * 初始化
     */
    @Override
    public void initialize() {
//        menuData = new ArrayList<>();
//        HttpClient.getInstance().getHomeMenu(cb);
//        dbUtils = DbUtils.create(context);
//
//        try {
//            List<SubModule> listData = dbUtils.findAll(Selector.from(SubModule.class));
//            if(listData != null ) {
//                System.out.println("listData.size():" + listData.size());
//            }
//        } catch (DbException e) {
//            System.out.println("------------------------------Error ->" + e.getMessage());
//        }
    }

    Callback<Base<ArrayList<HomeMenu>>> cb = new Callback<Base<ArrayList<HomeMenu>>>() {
        @Override
        public void success(Base<ArrayList<HomeMenu>> homeMenus, Response response) {
//            try {
////                dbUtils.saveAll(homeMenuResp.getData());
//
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            T.s(context, "获取菜单完成");

            //获取首页有用菜单
            if (homeMenus != null && homeMenus.getData() != null && homeMenus.getData().size() > 0) {
                final int len = homeMenus.getData().size();
                for (int i = 0; i < len; i++) {
                    final HomeMenu homeMenu = homeMenus.getData().get(i);
//                    if(homeMenu.getMenuLink() == null || homeMenu.getMenuLink() .size() <= 0){
//                        continue;
//                    }else{
                    menuData.add(homeMenu);
//                    }
                }
            }

            //生成首页菜单
            final int len = menuData.size();
            for (int i = 0; i < len; i++) {
                final HomeMenu tempHomeMenu = menuData.get(i);
                Button tempBtn = (Button) LayoutInflater.from(context).inflate(R.layout.single_btn_layout, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 60, 0, 0);
                tempBtn.setText(tempHomeMenu.getModuleName());
                tempBtn.setTag(tempHomeMenu);
                tempBtn.setOnClickListener(homeBtnClickListener);
                tempBtn.setLayoutParams(lp);
                container.addView(tempBtn);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("Error");
        }
    };

    /**
     * Home button click listener
     */
    private View.OnClickListener homeBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final HomeMenu tempHomeMenu = (HomeMenu) v.getTag();
            List<MenuLink> menuLinks = tempHomeMenu.getMenuLink();

            //配置菜单
            if(menuLinks != null && menuLinks.size() > 0){

                //婚纱摄影
                if(menuLinks.get(0).getMenuLinkUrl().contains(MenuConfig.WEDDING)){

//                    Intent intent = new Intent(context, TestMaterialNavDrawerActivity.class);
//                    intent.putExtra("subMenu", (Serializable) tempHomeMenu.getSubModule());
//                    animStart(intent);
                }else { //
                    Intent intent = new Intent(context, TempSimpleActivity.class);
                    intent.putExtra("subMenu", (Serializable) tempHomeMenu.getSubModule());
                    animStart(intent);
                }

            }else { //
                Intent intent = new Intent(context, TempSimpleActivity.class);
                intent.putExtra("subMenu", (Serializable) tempHomeMenu.getSubModule());
                animStart(intent);
            }


            //... more else if(){}
        }
    };

    public void testList(View view) {
        if (menuData != null && menuData.size() > 0) {
            final int len = menuData.size();
            for (int i = 0; i < len; i++) {
                if (menuData.get(i).getModuleName().equals("婚纱摄影")) {
                    homeMenu = menuData.get(i);
                    break;
                }
            }
        }
//        Intent intent = new Intent(context, TestMaterialNavDrawerActivity.class);
//        intent.putExtra("homeMenu", homeMenu);
//        animStart(intent);
    }

    //    @OnClick(R.id.menu_btn2)
    public void testResid(View view) {
        animStart(ResideActivity.class);
    }

    //    @OnClick(R.id.menu_btn3)
    public void testOther(View view) {
        animStart(TempSimpleActivity.class);
    }

    @OnClick(R.id.menu_plus_txt)
    public void plusClick(View view) {

            //animStart(LoginActivity.class);
        holder = new ViewHolder(R.layout.content);
        MTDialog dialog = new MTDialog.Builder(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setHeader(R.layout.header)
                .setOnClickListener(clickListener)
                .create();
        dialog.show();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mFragments = new Fragment[4];
//        mFragments[0] = new Opt1Fragment();
//        mFragments[1] = new Opt2Fragment();
//        mFragments[2] = new Opt3Fragment();
//        mFragments[3] = new Opt4Fragment();

        fragmentTransaction.add(R.id.dialog_container, mFragments[0]);
        fragmentTransaction.add(R.id.dialog_container, mFragments[1]);
        fragmentTransaction.add(R.id.dialog_container, mFragments[2]);
        fragmentTransaction.add(R.id.dialog_container, mFragments[3]);

        fragmentTransaction.commit();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction
                .hide(mFragments[1])
                .hide(mFragments[2]).hide(mFragments[3]).show(mFragments[0]).commit();

    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(MTDialog dialog, View view) {
            switch (view.getId()) {
                case R.id.header_container:
                    Toast.makeText(HomeMenuActivity.this, "Header clicked", Toast.LENGTH_LONG).show();
                    break;
                case R.id.footer_confirm_button:
                    Toast.makeText(HomeMenuActivity.this, "Confirm button clicked", Toast.LENGTH_LONG).show();
                    break;
                case R.id.footer_close_button:
                    Toast.makeText(HomeMenuActivity.this, "Close button clicked", Toast.LENGTH_LONG).show();
                    break;
                case R.id.dialog_option1:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction
                            .hide(mFragments[1])
                            .hide(mFragments[2]).hide(mFragments[3]).show(mFragments[0]).commit();
                    break;
                case R.id.dialog_option2:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction
                            .hide(mFragments[0])
                            .hide(mFragments[2]).hide(mFragments[3]).show(mFragments[1]).commit();
                    break;
                case R.id.dialog_option3:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction
                            .hide(mFragments[0])
                            .hide(mFragments[1]).hide(mFragments[3]).show(mFragments[2]).commit();
                    break;
                case R.id.dialog_option4:
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction
                            .hide(mFragments[0])
                            .hide(mFragments[1]).hide(mFragments[2]).show(mFragments[3]).commit();
                    break;
            }
        }
    };
}
