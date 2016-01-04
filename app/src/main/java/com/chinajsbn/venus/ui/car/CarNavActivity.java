package com.chinajsbn.venus.ui.car;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.LocalSubModule;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.car.fragment.CarFragment;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.ui.microfilm.fragment.MVFragment;
import com.chinajsbn.venus.ui.microfilm.fragment.MicrofilmFragment;
import com.chinajsbn.venus.ui.other.OtherActivity;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.MasterTitleView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 婚车租赁
 */
@ActivityFeature(layout = R.layout.activity_car)
public class CarNavActivity extends MBaseFragmentActivity {

    private final String HCZL = "42320";//
    private final String ZCJY = "42321";//

    ////////////////////////cache///////////////////////////
    private DbUtils db;
    private List<SubModule> dataList;
    private List<LocalSubModule> localSubModules;//本地数据

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @ViewInject(R.id.nav_listView)
    private ListView menuListView;
    private ListMenuAdapter adapter;

    private HashMap<String, BaseFragment> menuFragments = new HashMap<>();
    private HashMap<String, Integer> menuPosition = new HashMap<>();
    private BaseFragment carFragment;
    private int currentSelect = -1;
    private Fragment currentShowFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @OnClick(R.id.m_title_right_btn)
    public void rightClick(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    @Override
    public void initialize() {

        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        if(NetworkUtil.hasConnection(context)){
            dataList = (List<SubModule>) getIntent().getSerializableExtra("subModule");
            if(dataList == null || dataList.size() <= 0){
                T.s(context, "服务器维护中...");
                return;
            }
            adapter = new ListMenuAdapter();
            menuListView.setAdapter(adapter);


            //
            for (int i=0;i<dataList.size();i++){
                SubModule subModule = dataList.get(i);
                if(subModule.getContentId().equals(HCZL)){
                    carFragment = new CarFragment();
                    menuFragments.put(subModule.getContentId(), carFragment);
                    menuPosition.put(subModule.getContentId(), i);
                }
            }

            //
            menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (currentSelect == position) {
                        drawerLayout.closeDrawers();
                        return;
                    }

                    if(position == 1){
                        Intent intent = new Intent(context, OtherActivity.class);
                        intent.putExtra("type", 7);
                        animStart(intent);
                        return;
                    }

                    BaseFragment fragment = menuFragments.get(dataList.get(position).getContentId());
                    if (fragment == currentShowFragment) {
                        S.o(":: fragment == currentShowFragment");
                        return;
                    }
                    if (fragment == null) {
                        S.o("::error fragment is null.");
                        return;
                    }
                    //
                    titleView.setTitleText(dataList.get(position).getModuleName());
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();


                    fragmentTransaction.hide(currentShowFragment).show(fragment).commit();
                    fragment.show();
                    currentShowFragment = fragment;
                    currentSelect = position;
                    drawerLayout.closeDrawers();
                }
            });

            //
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
            currentShowFragment = menuFragments.get(dataList.get(currentSelect).getContentId());//临时写死的position,后面项多了后修改
            fragmentTransaction.show(currentShowFragment).commit();

        }else{//not network
            try {
                localSubModules = db.findAll(Selector.from(LocalSubModule.class).where("parentId", "=", HomeActivity.HCZL));
                if (localSubModules == null || localSubModules.size() <= 0) {
                    T.s(context, "请连网");
                    return;
                }
                adapter = new ListMenuAdapter();
                menuListView.setAdapter(adapter);


                //
                for (int i=0;i<localSubModules.size();i++){
                    LocalSubModule subModule = localSubModules.get(i);
                    if(subModule.getContentId().equals(HCZL)){
                        carFragment = new CarFragment();
                        menuFragments.put(subModule.getContentId(), carFragment);
                        menuPosition.put(subModule.getContentId(), i);
                    }
                }

                //
                menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (currentSelect == position) {
                            drawerLayout.closeDrawers();
                            return;
                        }

                        if(position == 1){
                            Intent intent = new Intent(context, OtherActivity.class);
                            intent.putExtra("type", 7);
                            animStart(intent);
                            return;
                        }

                        BaseFragment fragment = menuFragments.get(localSubModules.get(position).getContentId());
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

                //
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
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    class ListMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList == null ? 0 : dataList.size();
            } else {
                return localSubModules == null ? 0 :localSubModules.size();
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
            String contentId = dataList != null ? dataList.get(position).getContentId() : localSubModules.get(position).getContentId();

            if (contentId.equals(HCZL)) {
                holder.menuImg.setImageResource(R.mipmap.hczl);
            } else if (contentId.equals(ZCJY)) {
                holder.menuImg.setImageResource(R.mipmap.zcjy);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView menuImg;
        }
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

}
