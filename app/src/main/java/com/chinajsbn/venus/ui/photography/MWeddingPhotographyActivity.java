package com.chinajsbn.venus.ui.photography;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.LocalSubModule;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.MBaseFragment;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.fragment.photography.JSMVFragment;
import com.chinajsbn.venus.ui.fragment.photography.MPhotoCustomerFragment;
import com.chinajsbn.venus.ui.fragment.photography.MPhotoSimpleFragment;
import com.chinajsbn.venus.ui.fragment.photography.MTSelPhotographerFragment;
import com.chinajsbn.venus.ui.fragment.photography.MTSelStylistFragment;
import com.chinajsbn.venus.ui.fragment.photography.PhotoWedSuitFragment;
import com.chinajsbn.venus.ui.fragment.photography.TeamFragment;
import com.chinajsbn.venus.ui.fragment.plan.BestFilmFragment;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.ui.other.OtherActivity;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.MasterTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 13510 on 2015/9/8.
 */
//@ActivityFeature(layout = R.layout.activity_wedding_photography)
public class MWeddingPhotographyActivity extends AppCompatActivity {

    private final String YPXS = "27232";//样片欣赏
    private final String KPXS = "27233";//客片欣赏
    private final String TXBJ = "27234";//套系报价
    private final String HZJQ = "42305";//婚照技巧
    private final String HSJS = "42304";//婚纱纪实
    private final String XSYS = "27224";//选摄影师38035


//    private final String XZXS = "27225";//选造型师

    private Context context;

//    @ViewInject(R.id.nav_listView)
    private ListView menuListView;

    private ListMenuAdapter adapter;

//    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

//    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

//    private Toolbar toolbar;
//    @ViewInject(R.id.titleView)
//    private MasterTitleView titleView;

    ////////////////////////cache///////////////////////////
    private DbUtils db;

    /////////////////////end cache/////////////////////////


    private BaseFragment currentShowFragment;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ActionBarDrawerToggle toggle;

    private int currentSelect = -1;

    private ArrayList<SubModule> menuData;      //网络数据

    private List<LocalSubModule> localSubModules;//本地数据

    private HashMap<String, BaseFragment> menuFragments = new HashMap<>();
    private HashMap<String, Integer> menuPosition = new HashMap<>();
    private BaseFragment
            jsmvFragment,
            stylistFragment,
            photographyFragment,
            simpleFragment,
            customFragment,
            suitFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_photography);

        menuListView = (ListView) findViewById(R.id.nav_listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleView = (MasterTitleView) findViewById(R.id.titleView);

        initialize();
    }

    //    @Override
    public void initialize() {
        context = getApplicationContext();

        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        init();
    }

    private void init() {

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

        titleView.setTitleText("作品欣赏");
        drawerLayout.setDrawerListener(toggle);

        //
        menuData = (ArrayList<SubModule>) getIntent().getSerializableExtra("subModule");

        if (NetworkUtil.hasConnection(context)) {//有网络
            if (menuData != null && menuData.size() > 0) {//有数据
                final int len = menuData.size();
                for (int i = 0; i < len; i++) {
                    SubModule subModle = menuData.get(i);

                    if (subModle.getContentId().equals(HSJS)) {       //婚纱纪实MV
                        jsmvFragment = new JSMVFragment();
                        menuFragments.put(subModle.getModuleName(), jsmvFragment);
                        menuPosition.put(subModle.getModuleName(), i);

                    }else if (subModle.getContentId().equals(HZJQ)) {//婚照技巧
                        stylistFragment = new MTSelStylistFragment();
                        menuFragments.put(subModle.getModuleName(), stylistFragment);
                        menuPosition.put(subModle.getModuleName(), i);

                    } else if (subModle.getContentId().equals(XSYS)) { //
                        photographyFragment = new TeamFragment();
                        menuFragments.put(subModle.getModuleName(), photographyFragment);
                        menuPosition.put(subModle.getModuleName(), i);

                    } else if (subModle.getContentId().equals(YPXS)) {//样片欣赏
                        simpleFragment = new MPhotoSimpleFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("contentId", subModle.getSubModule().get(0).getContentId());
                        simpleFragment.setArguments(bundle);

                        menuFragments.put(subModle.getModuleName(), simpleFragment);
                        menuPosition.put(subModle.getModuleName(), i);

                    } else if (subModle.getContentId().equals(KPXS)) {//客片欣赏
                        customFragment = new MPhotoCustomerFragment();
//                        customFragment = new BestFilmFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("contentId", subModle.getSubModule().get(0).getContentId());
                        customFragment.setArguments(bundle);

                        menuFragments.put(subModle.getModuleName(), customFragment);
                        menuPosition.put(subModle.getModuleName(), i);

                    } else if (subModle.getContentId().equals(TXBJ)) {//婚纱套系报价
                        suitFragment = new PhotoWedSuitFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("contentId", subModle.getSubModule().get(0).getContentId());
                        suitFragment.setArguments(bundle);
                        menuFragments.put(subModle.getModuleName(), suitFragment);
                        menuPosition.put(subModle.getModuleName(), i);
                    }
                }

                //listView initialize
                adapter = new ListMenuAdapter();
                menuListView.setAdapter(adapter);
                menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (currentSelect == position) {
                            drawerLayout.closeDrawers();
                            return;
                        }

                        if(menuData.get(position).getContentId().equals(HZJQ)) {
                            Intent intent = new Intent(context, OtherActivity.class);
                            intent.putExtra("type", 0);
                            startActivity(intent);
//                            animStart(intent);
                            return;
                        }

                        BaseFragment fragment = menuFragments.get(menuData.get(position).getModuleName());
                        if (fragment == currentShowFragment) {
                            return;
                        }


                        //
                        titleView.setTitleText(menuData.get(position).getModuleName());
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();

                        fragmentTransaction.hide(currentShowFragment).show(fragment).commit();
                        currentShowFragment.hide();
                        fragment.show();

                        currentShowFragment = fragment;
                        currentSelect = position;
                        drawerLayout.closeDrawers();
                    }
                });
            } else {
                T.s(context, "获取数据失败，请稍后再试!");
                return;
            }
        } else {//无网络
            try {
                localSubModules = db.findAll(Selector.from(LocalSubModule.class).where("parentId", "=", HomeActivity.HSSY));
                if (localSubModules != null && localSubModules.size() > 0) {
                    final int len = localSubModules.size();
                    for (int i = 0; i < len; i++) {
                        LocalSubModule localSubModule = localSubModules.get(i);
                        if (localSubModule.getContentId().equals(HSJS)) {       //婚纱纪实
                            jsmvFragment = new JSMVFragment();
                            menuFragments.put(localSubModule.getModuleName(), jsmvFragment);
                            menuPosition.put(localSubModule.getModuleName(), i);
                        } else if (localSubModule.getContentId().equals(XSYS)) {//选摄影师
                            photographyFragment = new MTSelPhotographerFragment();
                            menuFragments.put(localSubModule.getModuleName(), photographyFragment);
                            menuPosition.put(localSubModule.getModuleName(), i);

                        } else if (localSubModule.getContentId().equals(YPXS)) {//样片欣赏
                            simpleFragment = new MPhotoSimpleFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("contentId", localSubModule.getContentId());
                            simpleFragment.setArguments(bundle);

                            menuFragments.put(localSubModule.getModuleName(), simpleFragment);
                            menuPosition.put(localSubModule.getModuleName(), i);

                        } else if (localSubModule.getContentId().equals(KPXS)) {//客片欣赏
                            customFragment = new MPhotoCustomerFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("contentId", localSubModule.getContentId());
                            customFragment.setArguments(bundle);

                            menuFragments.put(localSubModule.getModuleName(), customFragment);
                            menuPosition.put(localSubModule.getModuleName(), i);

                        } else if (localSubModule.getContentId().equals(TXBJ)) {//婚纱套系报价
                            suitFragment = new PhotoWedSuitFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("contentId", localSubModule.getContentId());
                            suitFragment.setArguments(bundle);

                            menuFragments.put(localSubModule.getModuleName(), suitFragment);
                            menuPosition.put(localSubModule.getModuleName(), i);
                        }
                    }

                    //listView initialize
                    adapter = new ListMenuAdapter(localSubModules);
                    menuListView.setAdapter(adapter);
                    menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (currentSelect == position) {
                                drawerLayout.closeDrawers();
                                return;
                            }

                            BaseFragment fragment = menuFragments.get(localSubModules.get(position).getModuleName());
                            if (fragment == currentShowFragment) {
                                return;
                            }

                            if(localSubModules.get(position).getContentId().equals(HZJQ)) {
                                Intent intent = new Intent(context, OtherActivity.class);
                                intent.putExtra("type", 0);
                                startActivity(intent);
//                            animStart(intent);
                                return;
                            }
//                            if(position == 5) {
//                                Intent intent = new Intent(context, OtherActivity.class);
//                                intent.putExtra("type", 0);
//                                startActivity(intent);
////                            animStart(intent);
//                                return;
//                            }

                            //
                            titleView.setTitleText(localSubModules.get(position).getModuleName());
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();

                            fragmentTransaction.hide(currentShowFragment).show(fragment).commit();
                            currentShowFragment.hide();
                            fragment.show();

                            currentShowFragment = fragment;
                            currentSelect = position;
                            drawerLayout.closeDrawers();
                        }
                    });
                } else {
                    T.s(context, "请连接网络再试");
                    return;
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }// init end

        //fragment initialize
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (menuFragments == null || menuFragments.size() <= 0) {
            T.s(context, "系统维护中 ...");
            return;
        }

        //--------------------------------------------------------------
        Iterator iter = menuFragments.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String k = (String) entry.getKey();
            BaseFragment v = (BaseFragment) entry.getValue();
            fragmentTransaction.add(R.id.photography_container, v);
        }

        //
        currentSelect = 0;
        Iterator iterator = menuFragments.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            BaseFragment v = (BaseFragment) entry.getValue();
            fragmentTransaction.hide(v);
        }

        if (NetworkUtil.hasConnection(context)) {
            currentShowFragment = menuFragments.get(menuData.get(0).getModuleName());
            fragmentTransaction.show(currentShowFragment).commit();
        } else {
            currentShowFragment = menuFragments.get(localSubModules.get(0).getModuleName());
            fragmentTransaction.show(currentShowFragment).commit();
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


            if (contentId.equals(XSYS)) {       //选团队
                holder.menuImg.setImageResource(R.mipmap.xtd);
            } else if (contentId.equals(TXBJ)) {
                holder.menuImg.setImageResource(R.mipmap.ic_right_menu_txbj);
            } else if (contentId.equals(YPXS)) {
                holder.menuImg.setImageResource(R.mipmap.ic_right_menu_ypxs);
            } else if (contentId.equals(KPXS)) {
                holder.menuImg.setImageResource(R.mipmap.ic_right_menu_kpxs);
            }else if (contentId.equals(HZJQ)) {
                holder.menuImg.setImageResource(R.mipmap.hzjq);
            }else if (contentId.equals(HSJS)) {
                holder.menuImg.setImageResource(R.mipmap.hsjs);
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
//        finish();
//        return true;
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.gc();
//    }
}
