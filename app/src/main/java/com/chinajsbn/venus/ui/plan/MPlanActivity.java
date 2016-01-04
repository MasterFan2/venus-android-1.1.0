package com.chinajsbn.venus.ui.plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.LocalSubModule;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.fragment.plan.BestFilmFragment;
import com.chinajsbn.venus.ui.fragment.plan.F4Fragment;
import com.chinajsbn.venus.ui.fragment.plan.FilmFragment;
import com.chinajsbn.venus.ui.fragment.plan.PlanSimpleFragment;
import com.chinajsbn.venus.ui.fragment.plan.PlannerFragment;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.ui.other.OtherActivity;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.tool.widget.MasterTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 13510 on 2015/9/8.
 */
//@ActivityFeature(layout = R.layout.activity_plan)
public class MPlanActivity extends AppCompatActivity {

    public static final String TAG = "MPlanActivity";

    private final String SJAL = "27239";//实景案例
    private final String HLGP = "42308";//婚礼跟拍
    private final String HLSP = "42309";//婚礼视频
    private final String XCHS = "38489";//选策划师
    private final String XHLR = "38490";//选婚礼人
    private final String HLXT = "42310";//婚礼学堂

    private ListView menuListView;
    private ListMenuAdapter adapter;
    private DrawerLayout drawerLayout;

    private MasterTitleView titleView;

    private int currentSelect = -1;

    private ArrayList<SubModule> menuData = new ArrayList<>();       //网络数据

    private List<LocalSubModule> localSubModules;//本地缓存数据

    private HashMap<String, BaseFragment> menuFragments = new HashMap<>();
    private HashMap<String, Integer> menuPosition = new HashMap<>();

    private Fragment currentShowFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private BaseFragment planSimpleFragment,//实景案例
                         bestFilmFragment,  //婚礼跟拍
                         filmFragment,      //婚礼视频
                         chsFragment,       //策划师
                         f4Fragment;        //选婚礼人

    ////////////////////////cache///////////////////////////
    private DbUtils db;
    /////////////////////end cache/////////////////////////

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        menuListView = (ListView) findViewById(R.id.nav_listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleView = (MasterTitleView) findViewById(R.id.titleView);

        initialize();
    }

//    @Override
    public void initialize() {
        context = getApplicationContext();
        db = DbUtils.create(context);
        init();
    }

    private void init() {

        titleView.setTitleText("实景案例");

        titleView.setLeftBtnText("首页");
        findViewById(R.id.m_title_right_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        findViewById(R.id.m_title_left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        toggle= new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.open, R.string.close);
//        toggle.syncState();
//        drawerLayout.setDrawerListener(toggle);

        menuData = (ArrayList<SubModule>) getIntent().getSerializableExtra("subModule");
//        for (SubModule subModule : tempMenuData){
//            if(subModule.getContentId().equals(HLGP) || subModule.getContentId().equals(HLSP)){//屏蔽了婚礼跟拍和婚礼视频
//
//            }else{
//                menuData.add(subModule);
//            }
//        }

        if (NetworkUtil.hasConnection(context)) {//有网络
            final int len = menuData == null ? 0 : menuData.size();
            for (int i = 0; i < len; i++) {
                SubModule subModle = menuData.get(i);
                if (subModle.getContentId().equals(SJAL)) {//实景案例
                    planSimpleFragment = new PlanSimpleFragment();
                    menuFragments.put(subModle.getContentId(), planSimpleFragment);
                    menuPosition.put(subModle.getContentId(), i);
                } else if (subModle.getContentId().equals(XCHS)) {//选策划师
                    chsFragment = new PlannerFragment();
                    menuFragments.put(subModle.getContentId(), chsFragment);
                    menuPosition.put(subModle.getContentId(), i);
                } else if (subModle.getContentId().equals(XHLR)) {//选婚礼人
                    f4Fragment = new F4Fragment();
                    menuFragments.put(subModle.getContentId(), f4Fragment);
                    menuPosition.put(subModle.getContentId(), i);
                } else if (subModle.getContentId().equals(HLGP)) {//婚礼跟拍
                    bestFilmFragment = new BestFilmFragment();
                    menuFragments.put(subModle.getContentId(), bestFilmFragment);
                    menuPosition.put(subModle.getContentId(), i);
                } else if (subModle.getContentId().equals(HLSP)) {//婚礼视频
                    filmFragment  = new FilmFragment();
                    menuFragments.put(subModle.getContentId(), filmFragment);
                    menuPosition.put(subModle.getContentId(), i);
                } else if (subModle.getContentId().equals(HLXT)) {//婚礼学堂
                }
            }

            adapter = new ListMenuAdapter();
            menuListView.setAdapter(adapter);
            menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (currentSelect == position) {
                        drawerLayout.closeDrawers();
                        return;
                    }

                    if (menuData.get(position).getContentId().equals(HLXT)) {
                        Intent intent = new Intent(context, OtherActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        return;
                    }

                    BaseFragment fragment = menuFragments.get(menuData.get(position).getContentId());
                    if (fragment == currentShowFragment) {
                        S.o(":: fragment == currentShowFragment");
                        return;
                    }
                    if (fragment == null) {
                        S.o("::error fragment is null.");
                        return;
                    }
                    //
                    titleView.setTitleText(menuData.get(position).getModuleName());
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();


                    fragmentTransaction.hide(currentShowFragment).show(fragment).commit();
                    fragment.show();
                    currentShowFragment = fragment;
                    currentSelect = position;
                    drawerLayout.closeDrawers();
                }
            });

            //fragment initialize
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            if (menuFragments == null || menuFragments.size() <= 0) {
                T.s(context, "系统维护中 ...");
                return;
            }

            Iterator iter = menuFragments.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String k = (String) entry.getKey();
                Fragment v = (Fragment) entry.getValue();
                fragmentTransaction.add(R.id.photography_container, v);
            }

            //
            currentSelect = 0;

            Iterator iterator = menuFragments.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Fragment v = (Fragment) entry.getValue();
                fragmentTransaction.hide(v);
            }
            currentShowFragment = menuFragments.get(menuData.get(currentSelect).getContentId());//临时写死的position,后面项多了后修改
            fragmentTransaction.show(currentShowFragment).commit();


        } else {//无网络
            try {
                localSubModules = db.findAll(Selector.from(LocalSubModule.class).where("parentId", "=", HomeActivity.HQDZ));
                for (LocalSubModule s : localSubModules){
                    S.o("无网络 MPlanActivity>>>" + s.getModuleName());
                }
                if (localSubModules != null && localSubModules.size() > 0) {
                    final int len = localSubModules.size();
                    for (int i = 0; i < len; i++) {
                        LocalSubModule localSubModule = localSubModules.get(i);
                        if (localSubModule.getContentId().equals(SJAL)) {       //实景案例
                            planSimpleFragment = new PlanSimpleFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("contentId", localSubModule.getContentId());
                            planSimpleFragment.setArguments(bundle);
                            menuFragments.put(localSubModule.getContentId(), planSimpleFragment);
                            menuPosition.put(localSubModule.getContentId(), i);
                        } else if (localSubModule.getContentId().equals(XCHS)) {//选策划师
                            chsFragment = new PlannerFragment();
                            menuFragments.put(localSubModule.getContentId(), chsFragment);
                            menuPosition.put(localSubModule.getContentId(), i);
                        } else if (localSubModule.getContentId().equals(XHLR)) {//F4
                            f4Fragment = new F4Fragment();
                            menuFragments.put(localSubModule.getContentId(), f4Fragment);
                            menuPosition.put(localSubModule.getContentId(), i);
                        } else if (localSubModule.getContentId().equals(HLGP)) {//婚礼跟拍
                            bestFilmFragment = new BestFilmFragment();
                            menuFragments.put(localSubModule.getContentId(), bestFilmFragment);
                            menuPosition.put(localSubModule.getContentId(), i);
                        } else if (localSubModule.getContentId().equals(HLSP)) {//婚礼视频
                            filmFragment  = new FilmFragment();
                            menuFragments.put(localSubModule.getContentId(), filmFragment);
                            menuPosition.put(localSubModule.getContentId(), i);
                        } else if (localSubModule.getContentId().equals(HLXT)) {//婚礼学堂
                        }
                    }
                    //fragment initialize
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();

                    if (menuFragments == null || menuFragments.size() <= 0) {
                        T.s(context, "系统维护中 ...");
                        return;
                    }

                    Iterator iter = menuFragments.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String k = (String) entry.getKey();
                        Fragment v = (Fragment) entry.getValue();
                        fragmentTransaction.add(R.id.photography_container, v);
                    }

                    //
                    currentSelect = 0;

                    Iterator iterator = menuFragments.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        Fragment v = (Fragment) entry.getValue();
                        fragmentTransaction.hide(v);
                    }
                    currentShowFragment = menuFragments.get(localSubModules.get(currentSelect).getContentId());//临时写死的position,后面项多了后修改
                    fragmentTransaction.show(currentShowFragment).commit();


                    adapter = new ListMenuAdapter(localSubModules);
                    menuListView.setAdapter(adapter);
                    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (currentSelect == position) {
                                drawerLayout.closeDrawers();
                                return;
                            }

                            BaseFragment fragment = menuFragments.get(localSubModules.get(position).getContentId());

                            if (localSubModules.get(position).getContentId().equals(HLXT)) {
                                Intent intent = new Intent(context, OtherActivity.class);
                                intent.putExtra("type", 2);
                                startActivity(intent);
//                            animStart(intent);
                                return;
                            }
                            if (fragment == currentShowFragment) {
                                S.o(":: fragment == currentShowFragment");
                                return;
                            }
                            if (fragment == null) {
                                S.o("::error fragment is null.");
                                return;
                            }
                            //
                            titleView.setTitleText(localSubModules.get(position).getModuleName());
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();


                            fragmentTransaction.hide(currentShowFragment).show(fragment).commit();
                            fragment.show();
                            currentShowFragment = fragment;
                            currentSelect = position;
                            drawerLayout.closeDrawers();
                        }
                    });
                } else {//请联网

                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    class ListMenuAdapter extends BaseAdapter {

        private List<LocalSubModule> dataList;

        public ListMenuAdapter() {
        }

        public ListMenuAdapter(List<LocalSubModule> d) {
            this.dataList = d;
        }

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList == null ? 0 : dataList.size();
            } else {
                return menuData == null ? 0 : menuData.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_photography_menu, null);
                holder.menuImg = (ImageView) convertView.findViewById(R.id.menu_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String contentId = null;

            if (dataList != null) {
                contentId = localSubModules.get(position).getContentId();
            } else {
                contentId = menuData.get(position).getContentId();
            }

            if (contentId.equals(SJAL)) {
                holder.menuImg.setImageResource(R.mipmap.sjal);
            } else if (contentId.equals(XCHS)) {
                holder.menuImg.setImageResource(R.mipmap.ic_right_menu_xchs);
            } else if (contentId.equals(XHLR)) {
                holder.menuImg.setImageResource(R.mipmap.xhlr);
            } else if (contentId.equals(HLGP)) {//婚礼跟拍
                holder.menuImg.setImageResource(R.mipmap.hlgp);
            } else if (contentId.equals(HLSP)) {//婚礼视频
                holder.menuImg.setImageResource(R.mipmap.hlsp);
            } else if (contentId.equals(HLXT)) {//婚礼学堂
                holder.menuImg.setImageResource(R.mipmap.hlxt);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView menuImg;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawers();
                return false;
            }
        }
        finish();
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public boolean onKeydown() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//            drawerLayout.closeDrawers();
//            return false;
//        }
////        animFinish();
//        return true;
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.gc();
//    }
}
