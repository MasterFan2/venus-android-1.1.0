package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Custom;
import com.chinajsbn.venus.net.bean.MasterFanSeason;
import com.chinajsbn.venus.net.bean.MenuLink;
import com.chinajsbn.venus.net.bean.SimpleStyles;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.SimpleDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.CircleImageView;
import com.tool.widget.MultiViewPager;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;
import com.tool.widget.transformation.FabTransformation;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 样片
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_customer_mtf)
public class MPhotoCustomerFragment extends BaseFragment implements OnRecyclerItemClickListener, MasterListView.OnRefreshListener, OnClickListener {

    public static final String TAG = "MPhotoCustomerFragment";

    private final String[] filters = new String[]{"最佳客片", "分季欣赏"};

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

    @ViewInject(R.id.fab)
    private TextView fab;

    @ViewInject(R.id.sheet)
    private View sheet;

    @ViewInject(R.id.overlay)
    private View overlay;

    @ViewInject(R.id.listView)
    private ListView styleListView;

    private List<Custom> dataList;  //列表显示的客片

    private List<Custom> zjList;    //最佳客片
    private List<Custom> seasonList;//分季客片

    private List<MasterFanSeason> sList;

    private MultiViewPager multiViewPager;

    //--------------page-----------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private boolean isStylePage = false;//
    private int lastVisibleItem;
    private int totalCount = -1;
    private DataType dataType = DataType.FIRST_PAGE;//当前页或下一页

    //------------------season-------------------
    private int tabCustomSeason = 0;//0: 最佳客片      1：分季欣赏     default:0
    private int seasonPosition = -1; //分季数据选择的位置

    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

    //列表和详情
    private ArrayList<MenuLink> menuLinks;

    private MenuLink listMenuLink;

    //模块ID
    private String parentId       = "42303";
    private String seasonParentId = "27236";

    //客片、样片
    private String simpleOrCustom = "custom";

    //------------------Cache------------------
    private DbUtils db;
    //------------------Cache end---------------

    @Override
    public void onRefresh(int id) {
        getFirstPages();
    }

    @Override
    public void onLoadMore(int id) {
        getNextPages();
    }

    private void getFirstPages() {
        isNextPage = false;
        pageIndex = 1;
        dataType = DataType.FIRST_PAGE;

        listView.saveRefreshStrTime();

        if(tabCustomSeason == 0){
            HttpClient.getInstance().getCustomList(parentId, pageIndex, pageSize, cb);
        }else if(tabCustomSeason == 1){
            HttpClient.getInstance().customListBySeasonId(seasonParentId, pageIndex, pageSize, sList.get(seasonPosition).getSeasonId(), cb);
        }
    }

//    private void getPagesByStyle() {
//        pageIndex = 1;
//        if (dataList != null && dataList.size() > 0) {
//            listView.smoothScrollToPosition(0);
//        }
//        HttpClient.getInstance().getCustomListByStyleId(styleId, parentId, pageIndex, pageSize, cb);
//    }

    private void getNextPages() {
        isNextPage = true;
        pageIndex++;
        dataType = DataType.NEXT;
        if(tabCustomSeason == 0){
            HttpClient.getInstance().getCustomList(parentId, pageIndex, pageSize, cb);
        }else if(tabCustomSeason == 1){
            HttpClient.getInstance().customListBySeasonId(seasonParentId, pageIndex, pageSize, sList.get(seasonPosition).getSeasonId(), cb);
        }
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        if (fab.getVisibility() == View.VISIBLE) {
            FabTransformation.with(fab).setOverlay(overlay).transformTo(sheet);
        }
    }

    @OnClick(R.id.overlay)
    public void onClickOverlay(View view) {
        if (fab.getVisibility() != View.VISIBLE) {
            FabTransformation.with(fab).setOverlay(overlay).transformFrom(sheet);
        }
    }

    @Override
    public void initialize() {

        //cache
        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        ///tabLayout
        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabCustomSeason = tab.getPosition();
                if (tab.getPosition() == 0) {
                    multiViewPager.setVisibility(View.GONE);
                    if(NetworkUtil.hasConnection(getActivity())){
                        dataList = zjList;
                    }else{
                        try {
                            dataList = db.findAll(Selector.from(Custom.class).where("seasonId", "=", "-1"));
                            adapter.notifyDataSetChanged();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (tab.getPosition() == 1) {
                    multiViewPager.setVisibility(View.VISIBLE);

                    if(NetworkUtil.hasConnection(getActivity())){
                        if (seasonList == null) {
                            if (seasonPosition != -1 && sList != null && sList.size() > 0) {
                                HttpClient.getInstance().customListBySeasonId(seasonParentId, pageIndex, pageSize, sList.get(seasonPosition).getSeasonId(), cb);
                            }
                        }else{
                            dataList = seasonList;
                            adapter.notifyDataSetChanged();
                        }
                    }else{//not connect
                        if (seasonPosition != -1 && sList != null && sList.size() > 0) {
                            try {
                                dataList = db.findAll(Selector.from(Custom.class).where("seasonId", "=", sList.get(seasonPosition).getSeasonId()));
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }else{
                            dataList = null;
                        }

                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //-------------------tabLayout-----------------------

        //隐藏客片风格搜索
        fab.setVisibility(View.GONE);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.customer_header_layout, null);
        multiViewPager = (MultiViewPager) headView.findViewById(R.id.viewPager);
        multiViewPager.setVisibility(View.GONE);
        multiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seasonPosition = position;
                if(NetworkUtil.hasConnection(getActivity())){
                    dialog.show();
                    HttpClient.getInstance().customListBySeasonId(seasonParentId, pageIndex, pageSize, sList.get(seasonPosition).getSeasonId(), cb);
                }else{
                    if(sList != null && sList.size() > 0){
                        try {
                            dataList = db.findAll(Selector.from(Custom.class).where("seasonId", "=", sList.get(seasonPosition).getSeasonId()));
                            adapter.notifyDataSetChanged();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(MPhotoCustomerFragment.this)
                .create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        listView.setPullLoadEnable(false);
        listView.setOnRefreshListener(MPhotoCustomerFragment.this, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(NetworkUtil.hasConnection(getActivity())){
                    Custom simple;
                    if (tabCustomSeason == 0) {
                        simple = dataList.get(position - 2);
                    } else {
                        simple = dataList.get(position - 2);
                    }

                    //SimpleDetailResp
                    Intent intent = new Intent(getActivity(), SimpleDetailActivity.class);
                    intent.putExtra("parentId", parentId);
                    intent.putExtra("contentId", simple.getContentId());
                    intent.putExtra("simpleOrCustom", simpleOrCustom);
                    intent.putExtra("photographer", simple.getPhotographer());
                    intent.putExtra("stylist", simple.getStylist());
                    intent.putExtra("date", simple.getCreateDate());
                    intent.putExtra("name", simple.getContentName());


                    ArrayList<SimpleStyles> styles = simple.getShootingStyles();
                    String strStyles = "";
                    if (styles != null && styles.size() > 0) {
                        for (int i = 0; i < styles.size(); i++) {
                            strStyles += styles.get(i).getShootingStyleName() + ",";
                        }
                        strStyles = strStyles.substring(0, strStyles.lastIndexOf(","));
                    }
                    intent.putExtra("styles", strStyles);

                    getActivity().startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
                }else{
                    handler.sendEmptyMessageDelayed(10, 10);
                    handler.sendEmptyMessageDelayed(11, 4000);
                }
            }
        });

        adapter = new MyAdapter();
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);

        //获取数据
       // parentId = "42303";//getArguments().getString("contentId");

        if(NetworkUtil.hasConnection(getActivity())){
            //获取分季
            HttpClient.getInstance().seasonList(seasonCallback);
        }else{//not connect
            try {
                seasonPosition = 0;
                sList = db.findAll(MasterFanSeason.class);
                if(sList != null && sList.size() > 0){
                    FragmentStatePagerAdapter pAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {

                        @Override
                        public int getCount() {
                            return sList.size();
                        }

                        @Override
                        public Fragment getItem(int position) {
                            Fragment fragment = new PageFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("url", sList.get(position).getMobileUrl());
                            bundle.putSerializable("text", sList.get(position).getSeasonName());
                            fragment.setArguments(bundle);
                            return fragment;
                        }
                    };
                    multiViewPager.setAdapter(pAdapter);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void show() {

        if (NetworkUtil.hasConnection(getActivity())) {
            handler.sendEmptyMessageDelayed(0, 500);
//            HttpClient.getInstance().getStyleList(styleListCallback);
        } else {
            try {
                dataList = db.findAll(Selector.from(Custom.class).where("seasonId", "=", "-1"));

                if (dataList == null || dataList.size() <= 0) {
                    handler.sendEmptyMessageDelayed(10, 1000);
                    handler.sendEmptyMessageDelayed(11, 4000);
                } else {
                    adapter.notifyDataSetChanged();
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hide() {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    listView.startRefresh();
                    getFirstPages();
                    break;
                case 10://show
                    mtdialog.show();
                    break;
                case 11://close
                    if (mtdialog != null && mtdialog.isShowing()) mtdialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }

    private Callback<Base<ArrayList<MasterFanSeason>>> seasonCallback = new Callback<Base<ArrayList<MasterFanSeason>>>() {
        @Override
        public void success(final Base<ArrayList<MasterFanSeason>> arrayListBase, Response response) {
            if (arrayListBase.getCode() == 200) {

                if (arrayListBase.getData() != null && arrayListBase.getData().size() > 0) {//有数据，设置默认选中1
                    seasonPosition = 0;
                    sList = arrayListBase.getData();
                    try {
                        db.deleteAll(MasterFanSeason.class);
                        db.saveAll(sList);
//                        for (MasterFanSeason season : sList){
//                            S.o(":::保存的分季ID" + season.getSeasonId());
//                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    FragmentStatePagerAdapter pAdapter = new FragmentStatePagerAdapter(getFragmentManager()) {

                        @Override
                        public int getCount() {
                            return sList.size();
                        }

                        @Override
                        public Fragment getItem(int position) {
                            Fragment fragment = new PageFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("url", sList.get(position).getMobileUrl());
                            bundle.putString("text", sList.get(position).getSeasonName());
                            fragment.setArguments(bundle);
                            return fragment;
                        }
                    };
                    multiViewPager.setAdapter(pAdapter);
                }
            }else {

            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private Callback<Base<List<Custom>>> cb = new Callback<Base<List<Custom>>>() {
        @Override
        public void success(Base<List<Custom>> simpleResp, Response response) {

            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            listView.stopRefresh();

            if (simpleResp.getCode() == 200) {

                if (tabCustomSeason == 0) {//最佳客片
                    if (dataType == DataType.FIRST_PAGE) {
                        zjList = simpleResp.getData();
                        try {
                            db.delete(Custom.class, WhereBuilder.b("seasonId", "=", -1));
                            for (Custom custom : zjList){
                                custom.setSeasonId("-1");
                                if(custom.getShootingStyles() != null && custom.getShootingStyles().size() > 0){
                                    custom.setShootingStyleName(custom.getShootingStyles().get(0).getShootingStyleName());
                                }
                            }
                            db.saveAll(zjList);
                            dataList = zjList;
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                    } else if (dataType == DataType.NEXT) {
                        zjList.addAll(simpleResp.getData());
                        try {
                            for (Custom custom : simpleResp.getData()){
                                custom.setSeasonId("-1");
                            }
                            db.saveAll(simpleResp.getData());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        dataList = zjList;
                    }
                } else {//分季欣赏
                    if (dataType == DataType.FIRST_PAGE) {
                        seasonList = simpleResp.getData();
                        try {
                            db.delete(Custom.class, WhereBuilder.b("seasonId", "=", sList.get(seasonPosition).getSeasonId()));
                            for (Custom custom : seasonList){
                                custom.setSeasonId(sList.get(seasonPosition).getSeasonId());
                                if(custom.getShootingStyles() != null && custom.getShootingStyles().size() > 0){
                                    custom.setShootingStyleName(custom.getShootingStyles().get(0).getShootingStyleName());
                                }
                            }
                            db.saveAll(seasonList);

//                            List<Custom> list = db.findAll(Selector.from(Custom.class).where("seasonId", "=", sList.get(seasonPosition).getSeasonId()));
//                            S.o(":通过分季保存的AAA:"+ list.size() +":AAA: seasonId=" + sList.get(seasonPosition).getSeasonId());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        dataList = seasonList;
                    } else if (dataType == DataType.NEXT) {
                        seasonList.addAll(simpleResp.getData());
                        try {
                            for (Custom custom : simpleResp.getData()){
                                custom.setSeasonId(sList.get(seasonPosition).getSeasonId());
                            }
                            db.saveAll(simpleResp.getData());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        dataList = seasonList;
                    }
                }

                //
                adapter.notifyDataSetChanged();

                //是否有下一页
                if (simpleResp.getData() != null && simpleResp.getData().size() > 0) {
                    if (simpleResp.getData().size() < pageSize) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                } else {
                    listView.setPullLoadEnable(false);
                }
            } else {
                T.s(getActivity(), "获取数据错误");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "获取客片数据错误,请稍后再试...");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        Custom simple = adapter.getItem(position);
        //SimpleDetailResp
        Intent intent = new Intent(getActivity(), SimpleDetailActivity.class);
        intent.putExtra("parentId", parentId);
        intent.putExtra("contentId", simple.getContentId());
        intent.putExtra("simpleOrCustom", simpleOrCustom);
        intent.putExtra("photographer", simple.getPhotographer());
        intent.putExtra("stylist", simple.getStylist());
        intent.putExtra("date", simple.getCreateDate());
        intent.putExtra("name", simple.getContentName());


        ArrayList<SimpleStyles> styles = simple.getShootingStyles();
        String strStyles = "";
        if (styles != null && styles.size() > 0) {
            for (int i = 0; i < styles.size(); i++) {
                strStyles += styles.get(i).getShootingStyleName() + ",";
            }
            strStyles = strStyles.substring(0, strStyles.lastIndexOf(","));
        }
        intent.putExtra("styles", strStyles);

        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Custom getItem(int i) {
            if (tabCustomSeason == 0) {
                return dataList.get(i - 1);
            } else {
                return seasonList.get(i);
            }
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            Custom custom = dataList.get(position);

            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_msimple_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (custom.getShootingStyles() != null && custom.getShootingStyles().size() > 0)
                holder.styleTxt.setText("风格:" + custom.getShootingStyles().get(0).getShootingStyleName());
            else
                holder.styleTxt.setText("风格:" + custom.getShootingStyleName());

            //加载摄影师
            if (custom.getPhotographer() != null && custom.getStylist() != null) {
                holder.name2Txt.setVisibility(View.GONE);
                holder.headImgLayout.setVisibility(View.VISIBLE);
                holder.nameTxt.setText(custom.getContentName());
                Picasso.with(getActivity()).load(custom.getPhotographer().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.photographerImg);
                holder.phtographerNameTxt.setText(custom.getPhotographer().getPersonName());
                //加造型师
                if (custom.getStylist() != null && !TextUtils.isEmpty(custom.getStylist().getPhotoUrl())) {
                    Picasso.with(getActivity()).load(custom.getStylist().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.stylistImg);
                }
            } else {
                holder.name2Txt.setVisibility(View.VISIBLE);
                holder.name2Txt.setText(custom.getContentName());
                holder.headImgLayout.setVisibility(View.GONE);
            }
            //加载内容
            if (TextUtils.isEmpty(custom.getContentUrl())) {
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            } else {
                Picasso.with(getActivity()).load(custom.getContentUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).error(getResources().getDrawable(R.mipmap.ic_launcher)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.contentImg);
            }
            return view;
        }

        class ViewHolder {
            TextView styleTxt;
            TextView nameTxt;
            TextView name2Txt;
            ImageView contentImg;
            CircleImageView photographerImg;
            TextView phtographerNameTxt;
            CircleImageView stylistImg;
            LinearLayout headImgLayout;


            public ViewHolder(View itemView) {
                contentImg = (ImageView) itemView.findViewById(R.id.item_simple_pic_img);
                photographerImg = (CircleImageView) itemView.findViewById(R.id.item_simple_photographer_head_img);
                stylistImg = (CircleImageView) itemView.findViewById(R.id.item_simple_stylist_head_img);
                styleTxt = (TextView) itemView.findViewById(R.id.item_simple_styles_txt);
                nameTxt = (TextView) itemView.findViewById(R.id.item_simple_name_txt);
                headImgLayout = (LinearLayout) itemView.findViewById(R.id.item_simple_headimg_layout);
                phtographerNameTxt = (TextView) itemView.findViewById(R.id.item_simple_photographer_name_txt);
                name2Txt = (TextView) itemView.findViewById(R.id.item_simple_name_2_txt);
            }
        }

    }

    enum DataType {
        FIRST_PAGE,//第一次，通过styleId第一次筛选
        NEXT,      //下一页
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
