//package com.chinajsbn.venus.ui.photography;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.chinajsbn.venus.R;
//import com.chinajsbn.venus.net.bean.SubModule;
//import com.chinajsbn.venus.ui.fragment.Opt1Fragment;
//import com.chinajsbn.venus.ui.fragment.photography.PhotoSelPhotographerFragment;
//import com.chinajsbn.venus.ui.fragment.photography.PhotoSelStylistFragment;
//import com.chinajsbn.venus.ui.fragment.photography.PhotoSelectWeddingFragment;
//import com.chinajsbn.venus.ui.fragment.photography.PhotoSimpleFragment;
//import com.chinajsbn.venus.ui.fragment.photography.PhotoWedSuitFragment;
//import com.chinajsbn.venus.ui.navigationdrawer.MaterialNavigationDrawer;
//import com.chinajsbn.venus.ui.navigationdrawer.elements.MaterialAccount;
//import com.chinajsbn.venus.utils.MenuConfig;
//import com.chinajsbn.venus.utils.T;
//
//import java.util.ArrayList;
//
///**
// * 婚纱摄影
// * create by master fan
// */
//public class TestMaterialNavDrawerActivity extends MaterialNavigationDrawer {
//
//    private ArrayList<SubModule> modules;
//
//    public void init(Bundle savedInstanceState) {
////        MaterialAccount account = new MaterialAccount(this.getResources(), "ZhangSan", "my name is zhangsan", R.mipmap.ic_launcher, R.drawable.bamboo);
//        MaterialAccount account1 = new MaterialAccount(this.getResources(), "张大三", "my name is 张大三",R.mipmap.ic_launcher, R.drawable.mat2);
////        MaterialAccount account2 = new MaterialAccount(this.getResources(), "王麻子", "my name is 王麻子", R.mipmap.ic_launcher, R.drawable.mat3);
//
////
////        this.addAccount(account);
//        this.addAccount(account1);
////        this.addAccount(account2);
//
////        this.setDrawerHeaderImage(R.drawable.mat2);// header image
//
//
//
//        modules = (ArrayList<SubModule>) getIntent().getSerializableExtra("subMenu");
//        final int len = modules.size();
//
//        for (int i = 0; i < len; i++) {
//            final SubModule subModule = modules.get(i);
//
//            if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkUrl().equals(MenuConfig.SIMPLE)) {//样片
//                PhotoSimpleFragment photoSimpleFragment = new PhotoSimpleFragment();
//                Bundle args = new Bundle();
//                args.putSerializable("subModule", subModule.getSubModule());
//                args.putString("simpleOrCustom", "simple");
//                photoSimpleFragment.setArguments(args);
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, photoSimpleFragment).setSectionColor(Color.parseColor("#03a9aa")));
//
//            } else if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkUrl().equals(MenuConfig.CUSTOM)) {//客片
//                PhotoSimpleFragment photoSimpleFragment = new PhotoSimpleFragment();
//                Bundle args = new Bundle();
//                args.putSerializable("subModule", subModule.getSubModule());
//                args.putString("simpleOrCustom", "custom");
//                photoSimpleFragment.setArguments(args);
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, photoSimpleFragment).setSectionColor(Color.parseColor("#03a9aa")));
//
//            } else if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkUrl().equals(MenuConfig.WEDDING_SUIT)) {//套系
//                PhotoWedSuitFragment wedSuitFragment = new PhotoWedSuitFragment();
//                Bundle args = new Bundle();
//                args.putSerializable("subModule", subModule.getSubModule());
//                wedSuitFragment.setArguments(args);
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, wedSuitFragment).setSectionColor(Color.parseColor("#444153")));
//
//            } else if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkName().equals("选摄影师")){//选摄影师
//                //选摄影师
//                PhotoSelPhotographerFragment photoSelPhotographerFragment = new PhotoSelPhotographerFragment();
//
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, photoSelPhotographerFragment).setSectionColor(Color.parseColor("#444153")));
//
//            }else if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkName().equals("选造型师")){//选造型师
//                //选造型师
//                PhotoSelStylistFragment selStylistFragment = new PhotoSelStylistFragment();
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, selStylistFragment).setSectionColor(Color.parseColor("#444153")));
//
//            }else if(subModule.getMenuLink() != null && subModule.getMenuLink().size() > 0 && subModule.getMenuLink().get(0).getMenuLinkName().equals("选婚纱")){//选婚纱
//                //选婚纱
//                PhotoSelectWeddingFragment selectWeddingFragment = new PhotoSelectWeddingFragment();
//                Bundle args = new Bundle();
//                args.putString("id", subModule.getMenuLink().get(0).getMenuId());
//                selectWeddingFragment.setArguments(args);
//                this.addSection(newSection(subModule.getModuleName(), R.mipmap.ic_launcher, selectWeddingFragment).setSectionColor(Color.parseColor("#444153")));
//
//            }
//        }
//
////        setDefaultSectionLoaded(10);
//        // create bottom section
////        this.addBottomSection(newSection("更多 ...", R.mipmap.ic_launcher, new Opt4Fragment()));
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_ttest, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.simple_toolbar_ou) {
//            T.s(this, "欧式风格");
//            return true;
//        }else if (id == R.id.simple_toolbar_han) {
//            T.s(this, "韩式风格");
//            return true;
//        }else if (id == R.id.simple_toolbar_chn) {
//            T.s(this, "中国风 风格");
//            return true;
//        }else if (id == R.id.simple_toolbar_entity) {
//            return true;
//        }else if (id == R.id.simple_toolbar_garden) {
//            return true;
//        }else if (id == R.id.simple_toolbar_laser) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
