package com.chinajsbn.venus.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Advert;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.net.bean.HomeMenu;
import com.chinajsbn.venus.net.bean.LocalSubModule;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.car.CarNavActivity;
import com.chinajsbn.venus.ui.diamond.DiamondActivity;
import com.chinajsbn.venus.ui.dresses.DressesNavActivity;
import com.chinajsbn.venus.ui.hotels.HotelActivity;
import com.chinajsbn.venus.ui.microfilm.MicrofilmNavActivity;
import com.chinajsbn.venus.ui.photography.MWeddingPhotographyActivity;
import com.chinajsbn.venus.ui.plan.MPlanActivity;
import com.chinajsbn.venus.ui.supplies.SuppliesNavActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.MTDBUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 主菜单
 * create by master fan on 2015/07/29
 */
@ActivityFeature(layout = R.layout.activity_home)
public class HomeActivity extends MBaseFragmentActivity implements OnClickListener {

    public static final String TAG = "HomeActivity";
    public static final String TAG_ADVERT = "HomeActivity_advert";

    private Snackbar sbar = null;

    @ViewInject(R.id.listView)
    private ListView listView;
    private HomeMenuAdapter adapter;

    //    @ViewInject(R.id.home_viewPager)
    private ViewPager viewPager;

    //    @ViewInject(R.id.viewPager_indicator)
    private CircleIndicator indicator;

    @ViewInject(R.id.home_min_txt)
    private TextView minTxt;

    //menu data
    private List<SubModule> menuData;

    private List<LocalSubModule> localList = new ArrayList<>();
    private HomeMenu homeMenu;

    private MTDialog dialog;

    //------------------advert----------------------
    private SubModule homeAdvertModule;
    private List<Advert> adverts;
    private Timer timer;
    private TimerTask task;
    private int currentPagerPosition = 0;
    private boolean isFirstIn = true;//第一次进入 onResume

    //--------------------cache----------------------
    private DbUtils db;
    //--------------------end cache----------------------


    //本地服务器
//    private final int HSSY = 21399;//婚纱摄影
//    private final int HYYD = 21400;//婚宴预订
//    private final int HQDZ = 21401;//婚庆定制

    //正式服务器
    public static final int HSSY = 27201;//婚纱摄影
    public static final int HYYD = 27202;//婚宴预订
    public static final int HQDZ = 27203;//婚庆定制
    public static final int HSLF = 27204;//婚纱礼服
    public static final int HJZS = 27205;//婚戒钻石
    public static final int WDY = 27206;//微电影
    public static final int HLYP = 27207;//婚礼用品
    public static final int HCZL = 27208;//婚车租赁

    private final int HMAD = 38487;//35639;//首页广告


    private boolean isAdd = false;

    @Override
    public void initialize() {

        db = DbUtils.create(context);
        db.configAllowTransaction(true);
//        DbUtils.DaoConfig config = new DbUtils.DaoConfig(context);
//        config.setDbName("venus"); //db名
//        config.setDbVersion(1);  //db版本
//        db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            minTxt.setVisibility(View.GONE);
        }

        initializeTimer();


        //init dailog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(this)
                .create();

        //init listview
        menuData = new ArrayList<>();
        adapter = new HomeMenuAdapter();

        View headerView = LayoutInflater.from(context).inflate(R.layout.header_home_menu, null);
        viewPager = (ViewPager) headerView.findViewById(R.id.home_viewPager);
        indicator = (CircleIndicator) headerView.findViewById(R.id.viewPager_indicator);

        if (!isAdd) {
            listView.addHeaderView(headerView);
            isAdd = true;
        }
        listView.setAdapter(adapter);

        // get data from network
        if (NetworkUtil.hasConnection(context)) {//有网，访问网络
            HttpClient.getInstance().getHomeMenu("APP", cb);
        } else {  //无网， 读取本地数据
            try {
                //获取菜单
                List<SubModule> localMenuData = db.findAll(SubModule.class);

                if (localMenuData != null && localMenuData.size() > 0) {
                    for (SubModule subModule : localMenuData) {
                        if (subModule.getIadvert() == 1) {
                            homeAdvertModule = subModule;
                        } else {
                            menuData.add(subModule);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    //获取广告
                    adverts = db.findAll(Advert.class);
                    if (adverts != null && adverts.size() > 0) {
                        viewPager.setAdapter(new MPagerAdapter(adverts));
                        indicator.setViewPager(viewPager);
                        timer.schedule(task, 1000, 3500);
                    }
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            handler.sendEmptyMessageDelayed(10, 1000);
            handler.sendEmptyMessageDelayed(11, 4000);
        }
    }

    private void initializeTimer() {

        if (timer == null)
            timer = new Timer();

        if (task == null)
            task = new TimerTask() {
                @Override
                public void run() {
                    if (currentPagerPosition >= adverts.size()) {
                        currentPagerPosition = 0;
                    } else {
                        currentPagerPosition++;
                    }
                    handler.sendEmptyMessage(1);
                }
            };
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    viewPager.setCurrentItem(currentPagerPosition, true);
                    break;
                case 10://show
                    dialog.show();
                    break;
                case 11://close
                    if (dialog != null && dialog.isShowing()) dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    class MPagerAdapter extends PagerAdapter {
        List<Advert> list;

        public MPagerAdapter(List<Advert> l) {
            this.list = l;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView img = (ImageView) LayoutInflater.from(context).inflate(R.layout.item_single_img_hotel_head_layout, null);
            String url = list.get(position).getContentUrl();
            if (!TextUtils.isEmpty(url)) {
                Picasso.with(context).load(url + "@" + DimenUtil.screenWidth + "w_" + DimenUtil.screenWidth * 2 / 3 + "h_40Q").into(img);
            }
            container.addView(img);
            return img;
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

    @OnItemClick(R.id.listView)
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        i -= 1;
        if(NetworkUtil.hasConnection(context)){
            String contentId = menuData.get(i).getContentId();
            Intent intent;
            if (contentId.equals("" + HSSY)) {      //婚纱摄影
                intent = new Intent(context, MWeddingPhotographyActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HYYD)) {//婚宴预定
                intent = new Intent(context, HotelActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HQDZ)) {//婚庆定制
                intent = new Intent(context, MPlanActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HSLF)) {//婚纱礼服
                intent = new Intent(context, DressesNavActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HJZS)) {//婚戒钻石
                intent = new Intent(context, DiamondActivity.class);
                animStart(intent);
            } else if (contentId.equals("" + WDY)) {//微电影
                intent = new Intent(context, MicrofilmNavActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HLYP)) {//婚礼用品
                intent = new Intent(context, SuppliesNavActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            } else if (contentId.equals("" + HCZL)) {//婚车租赁
                intent = new Intent(context, CarNavActivity.class);
                intent.putExtra("subModule", menuData.get(i).getSubModule());
                animStart(intent);
            }
        }else{
            try {
                List<SubModule> localMenuData = db.findAll(SubModule.class);
                String contentId = localMenuData.get(i).getContentId();
                Intent intent;
                if (contentId.equals("" + HSSY)) {      //婚纱摄影
                    intent = new Intent(context, MWeddingPhotographyActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HYYD)) {//婚宴预定
                    intent = new Intent(context, HotelActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HQDZ)) {//婚庆定制
                    intent = new Intent(context, MPlanActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HSLF)) {//婚纱礼服
                    intent = new Intent(context, DressesNavActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HJZS)) {//婚戒钻石
                    intent = new Intent(context, DiamondActivity.class);
                    animStart(intent);
                } else if (contentId.equals("" + WDY)) {//微电影
                    intent = new Intent(context, MicrofilmNavActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HLYP)) {//婚礼用品
                    intent = new Intent(context, SuppliesNavActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                } else if (contentId.equals("" + HCZL)) {//婚车租赁
                    intent = new Intent(context, CarNavActivity.class);
                    intent.putExtra("subModule", localMenuData.get(i).getSubModule());
                    animStart(intent);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * get main menu call back
     */
    Callback<Base<ArrayList<HomeMenu>>> cb = new Callback<Base<ArrayList<HomeMenu>>>() {
        @Override
        public void success(Base<ArrayList<HomeMenu>> homeMenus, Response response) {

            if (homeMenus.getData() != null && homeMenus.getData().size() > 0) {

                try {
                    db.deleteAll(menuData);
                    db.deleteAll(localList);
                } catch (DbException e) {
                    e.printStackTrace();
                }

                ArrayList<SubModule> temp = homeMenus.getData().get(0).getSubModule();

                for (int i = 0; i < temp.size(); i++) {
                    SubModule subModuleTemp = temp.get(i);
                    if (subModuleTemp.getContentId().equals("" + HSSY)) {//婚纱摄影
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HYYD)) {//婚宴预订
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HQDZ)) {//婚庆案例
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HSLF)) {//婚纱礼服
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HJZS)) {//婚戒钻石
                        menuData.add(subModuleTemp);
                    } else if (subModuleTemp.getContentId().equals("" + WDY)) {//微电影
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HLYP)) {//婚礼用品
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HCZL)) {//婚车租赁
                        menuData.add(subModuleTemp);
                        int size = subModuleTemp.getSubModule() == null ? 0 : subModuleTemp.getSubModule().size();
                        LocalSubModule localSubModule = null;
                        for (int j = 0; j < size; j++) {
                            localSubModule = new LocalSubModule();
                            localSubModule.setContentId(subModuleTemp.getSubModule().get(j).getContentId());
                            localSubModule.setModuleName(subModuleTemp.getSubModule().get(j).getModuleName());
                            localSubModule.setParentId(subModuleTemp.getContentId());
                            localList.add(localSubModule);
                        }
                    } else if (subModuleTemp.getContentId().equals("" + HMAD)) {//首页广告
                        homeAdvertModule = subModuleTemp.getSubModule().get(0);
                    }
                }


                try {
                    db.saveAll(menuData);
                    if (homeAdvertModule != null) {
                        homeAdvertModule.setIadvert(1);
                        db.saveOrUpdate(homeAdvertModule);
                    }
                    db.saveAll(localList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
//                //缓存到本地数据库
//                if (!MTDBUtil.todayChecked(context, HomeActivity.TAG)) {
//                    try {
//                        //1.保存首页菜单
//                        db.saveOrUpdateAll(menuData);
//
//                        if(homeAdvertModule != null) {
//                            homeAdvertModule.setIadvert(1);
//                            db.saveOrUpdate(homeAdvertModule);
//                        }
//
//                        //2.保存二级菜单
//                        db.saveOrUpdateAll(localList);
//
//                        //更新缓存的日期为当天
//                        Cache cache = new Cache(HomeActivity.TAG, MTDBUtil.getToday());
//                        db.saveOrUpdate(cache);
//
//
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }

                adapter.notifyDataSetChanged();

                if (homeAdvertModule != null) {
                    HttpClient.getInstance().getHomeAdvert(homeAdvertModule.getContentId(), advertCallback);
                }
            } else {
                T.s(context, "服务器维护中...");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("Error");
        }
    };

    /**
     *
     */
    Callback<Base<ArrayList<Advert>>> advertCallback = new Callback<Base<ArrayList<Advert>>>() {
        @Override
        public void success(Base<ArrayList<Advert>> advertList, Response response) {
            if (advertList.getCode() == 200) {
                if (advertList.getData() != null && advertList.getData().size() > 0) {

                    adverts = advertList.getData();
                    viewPager.setAdapter(new MPagerAdapter(adverts));
                    indicator.setViewPager(viewPager);
                    initializeTimer();
                    timer.schedule(task, 1000, 3500);

                    try {
                        db.deleteAll(Advert.class);
                        db.saveAll(advertList.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

//                    if (!MTDBUtil.todayChecked(context, HomeActivity.TAG_ADVERT)) {
//                        try {
//                            db.saveOrUpdateAll(adverts);
//                            Cache cache = new Cache(HomeActivity.TAG_ADVERT, MTDBUtil.getToday());
//                            db.saveOrUpdate(cache);
//                        } catch (DbException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        S.o("home::onPause");
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        S.o("home::onResume");
        if (isFirstIn) {
            isFirstIn = false;
            return;
        }
        if (adverts != null && adverts.size() > 0) {
            initializeTimer();
            if (timer != null) timer.schedule(task, 1000, 3500);
        }
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }

    class HomeMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return menuData == null ? 0 : menuData.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_home_menu, null);

            }

            ImageView img = (ImageView) view.findViewById(R.id.home_menu_img);

            SubModule subModule = menuData.get(i);

            //正式服务器
            if (subModule.getContentId().equals("" + HSSY)) {//婚纱摄影
                img.setImageResource(R.mipmap.home_photographer);
            } else if (subModule.getContentId().equals("" + HYYD)) {//婚宴预定
                img.setImageResource(R.mipmap.home_hotel);
            } else if (subModule.getContentId().equals("" + HQDZ)) {//婚庆定制
                img.setImageResource(R.mipmap.hqdz);
            } else if (subModule.getContentId().equals("" + HSLF)) {//婚纱礼服
                img.setImageResource(R.mipmap.home_hslf);
            } else if (subModule.getContentId().equals("" + HJZS)) {//婚戒钻石
                img.setImageResource(R.mipmap.home_hjzs);
            } else if (subModule.getContentId().equals("" + WDY)) {//微电影
                img.setImageResource(R.mipmap.home_wdy);
            } else if (subModule.getContentId().equals("" + HLYP)) {//婚礼用品
                img.setImageResource(R.mipmap.home_hlyp);
            } else if (subModule.getContentId().equals("" + HCZL)) {//婚车租赁
                img.setImageResource(R.mipmap.home_hczl);
            }
            return view;
        }
    }

    @Override
    public boolean onKeydown() {

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
