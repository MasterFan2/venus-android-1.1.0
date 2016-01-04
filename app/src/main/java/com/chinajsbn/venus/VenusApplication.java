package com.chinajsbn.venus;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.ui.fragment.photography.MPhotoCustomerFragment;
import com.chinajsbn.venus.ui.fragment.photography.MPhotoSimpleFragment;
import com.chinajsbn.venus.ui.fragment.photography.MTSelPhotographerFragment;
import com.chinajsbn.venus.ui.fragment.photography.MTSelStylistFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoWedSuitFragment;
import com.chinajsbn.venus.ui.fragment.plan.F4Fragment;
import com.chinajsbn.venus.ui.fragment.plan.PlanSimpleFragment;
import com.chinajsbn.venus.ui.fragment.plan.PlannerFragment;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.ui.hotels.HotelDetailActivity;
import com.chinajsbn.venus.ui.hotels.HotelsActivity;
import com.chinajsbn.venus.ui.hotels.fragment.MTHotelFragment;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.PreUtil;
import com.chinajsbn.venus.utils.S;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.tool.crash.CustomActivityOnCrash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MasterFan on 2015/6/3.
 * description:
 */
public class VenusApplication extends Application {

    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        //保存异常信息
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        context = getApplicationContext();

        CustomActivityOnCrash.install(this);

        //初始化接口
        HttpClient.getInstance().initialize(this);

        //
        getScreenDimen();

        //
        getMemoryInfo(this);

        //
        savePages();

        //
//        tempTest();
    }

    private void getMemoryInfo(Context context) {
        //   ActivityManager.
        ActivityManager actMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        actMgr.getMemoryInfo(memoryInfo);
        S.o("availMem::" + (memoryInfo.availMem >> 20) + "M");
        S.o("threshold::" + (memoryInfo.threshold >> 20) + "M and isLowMemory=" + memoryInfo.lowMemory);
    }

//    private void tempTest(){
//        DbUtils db = DbUtils.create(this);
//        db.configAllowTransaction(true);
//        try {
//            List<Photographer> listMajordomo = db.findAll(Selector.from(Photographer.class).where("identify", "=", MTDBUtil.PHOTO_MAJORDOMO));
//            if (listMajordomo != null && listMajordomo.size() > 0) {
//                for (Photographer photographer : listMajordomo) {
//                    S.o("listMajordomo;::::::::::::::::::::::::::::::::::" + photographer.getPersonName());
//                }
//            }
//
//            List<Photographer> listSenior = db.findAll(Selector.from(Photographer.class).where("identify", "=", MTDBUtil.PHOTO_SENIOR));
//            S.o(":::::::::::::::::::::::::listSenior == null ? " + (listSenior == null));
//            if (listSenior != null && listSenior.size() > 0) {
//                for (Photographer photographer : listMajordomo) {
//                    S.o("listSenior;::::::::::::::::::::::::::::::::::" + photographer.getPersonName());
//                }
//            }
//
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void savePages() {
        DbUtils db = DbUtils.create(this);
        db.configAllowTransaction(true);

        try {
            if(PreUtil.isFirstByTag("1_1_0", this)){
                db.dropDb();
                PreUtil.setFirstByTag("1_1_0", this);
                S.o(">>>exec drop Db");
            }

            List<Cache> localCacheList = db.findAll(Cache.class);

            if (localCacheList != null && localCacheList.size() > 0) return;     //有数据则不进行以下操作

            ArrayList<Cache> cacheList = new ArrayList<>();

            ///////////////////////////////////首页/////////////////////////////////////////
            cacheList.add(new Cache(HomeActivity.TAG, "2015-5-20"));            //首页
            cacheList.add(new Cache(HomeActivity.TAG_ADVERT, "2015-5-20"));     //首页广告

            /////////////////////////////////婚纱摄影///////////////////////////////////////
            cacheList.add(new Cache(MPhotoSimpleFragment.TAG, "2015-5-20"));    //样片
            cacheList.add(new Cache(MPhotoCustomerFragment.TAG, "2015-5-20"));  //客片
            cacheList.add(new Cache(PhotoWedSuitFragment.TAG, "2015-5-20"));    //套系
            cacheList.add(new Cache(MTSelPhotographerFragment.TAG_PHOTO_MAJORDOME, "2015-5-20"));//摄影师总监
            cacheList.add(new Cache(MTSelPhotographerFragment.TAG_PHOTO_SENIOR, "2015-5-20"));   //摄影师资深

            cacheList.add(new Cache(MTSelStylistFragment.TAG_STYLIST_MAJORDOMO, "2015-5-20"));   //造型师总监
            cacheList.add(new Cache(MTSelStylistFragment.TAG_STYLIST_SENIOR, "2015-5-20"));      //造型师资深

            /////////////////////////////////婚宴预订///////////////////////////////////////
            cacheList.add(new Cache(MTHotelFragment.TAG, "2015-5-20"));          //酒店列表
            cacheList.add(new Cache(HotelDetailActivity.TAG, "2015-5-20"));     //酒店详情

            /////////////////////////////////案例欣赏///////////////////////////////////////
            cacheList.add(new Cache(F4Fragment.TAG_EMCEE, "2015-5-20"));        //主持人
            cacheList.add(new Cache(F4Fragment.TAG_DRESSER, "2015-5-20"));      //化妆师
            cacheList.add(new Cache(F4Fragment.TAG_PHOTOGRAPHER, "2015-5-20")); //摄影师
            cacheList.add(new Cache(F4Fragment.TAG_CAMERAMAN, "2015-5-20"));    //摄像师

            cacheList.add(new Cache(PlanSimpleFragment.TAG, "2015-5-20"));      //案例
            cacheList.add(new Cache(PlanSimpleFragment.TAG_STYLE, "2015-5-20"));//案例样式

            cacheList.add(new Cache(PlannerFragment.TAG, "2015-5-20"));//策划师


            db.saveOrUpdateAll(cacheList);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /**
     * get screen dimension
     */
    private void getScreenDimen() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DimenUtil.screenWidth = wm.getDefaultDisplay().getWidth();
//        if(DimenUtil.screenWidth >= 1080){
//            DimenUtil.screenWidth = 900;
//        }
        DimenUtil.screenHeight = wm.getDefaultDisplay().getHeight();
//        DimenUtil.listViewHorizontalStringDimension = DimenUtil.getHorizontal();
//        DimenUtil.listViewVerticalStringDimension   = DimenUtil.getVerticalListViewStringDimension();
    }
}
