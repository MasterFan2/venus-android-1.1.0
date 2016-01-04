package com.chinajsbn.venus.ui.fragment.plan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Film;
import com.chinajsbn.venus.net.bean.MasterFanSeason;
import com.chinajsbn.venus.net.bean.Scheme;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.plan.MPlanDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;
import com.tool.widget.viewpager.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 婚礼跟拍
 * Created by 13510 on 2015/12/4.
 */
@FragmentFeature(layout = R.layout.fragment_best_film)
public class BestFilmFragment extends BaseFragment implements MasterListView.OnRefreshListener, OnRecyclerItemClickListener, OnClickListener {

    private final String[] filters = new String[]{"最佳跟拍", "分季欣赏"};

    ///used by cache
    private DbUtils db;
    private final String TAG = "BestFilmFragment_tag_best_film";
    private MTDialog dialog;

//    @ViewInject(R.id.viewPager)
//    private ViewPager viewPager;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

    //--------------data-----------------
    private List<Scheme> dataList;
    private List<Scheme> bestList;
    private List<Scheme> filterList;

    //------------------season-------------------
    private int tabCustomSeason = 0;//0: 最佳跟拍      1：分季欣赏     default:0
    private int seasonPosition = -1; //分季数据选择的位置
    private ViewPager multiViewPager;
    private List<MasterFanSeason> seasonList;

    //--------------page-----------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private boolean isSeasonNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private final String moduleId = "42311";

    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        //init dailog
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(this)
                .create();

        //-------------------tabLayout-----------------------
        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabCustomSeason = tab.getPosition();

                if (tab.getPosition() == 0) {//最佳跟拍
                    dataList = bestList;
                    isNextPage = false;
                    multiViewPager.setVisibility(View.GONE);
                } else if (tab.getPosition() == 1) {//分季欣赏
                    dataList = filterList;
                    isNextPage = false;
                    multiViewPager.setVisibility(View.VISIBLE);
                    if (seasonList == null || seasonList.size() <= 0) {
                        HttpClient.getInstance().planSeasonList(seasonCallback);
                    } else {
                        HttpClient.getInstance().planListBySeasonId(moduleId, pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), bySeasonIdCallback);
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

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_season_head_layout, null);
        multiViewPager = (ViewPager) headView.findViewById(R.id.viewPager);
        multiViewPager.setVisibility(View.GONE);
        multiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seasonPosition = position;
                HttpClient.getInstance().planListBySeasonId(moduleId, pageIndex, pageSize, seasonList.get(position).getSeasonId(), bySeasonIdCallback);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setOnRefreshListener(BestFilmFragment.this, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(NetworkUtil.hasConnection(getActivity())){
                    Intent intent = new Intent(getActivity(), MPlanDetailActivity.class);
                    intent.putExtra("parentId", moduleId);
                    intent.putExtra("detailId", adapter.getItem(position - 1).getWeddingCaseId() + "");
                    intent.putExtra("name", adapter.getItem(position - 1).getSchemeName());
                    animStart(intent);
                }else {
                    handler.sendEmptyMessageDelayed(10, 10);
                    handler.sendEmptyMessageDelayed(11, 3000);
                }
            }
        });

        adapter = new MyAdapter();
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    /**
     * 分季菜单列表
     */
    Callback<Base<ArrayList<MasterFanSeason>>> seasonCallback = new Callback<Base<ArrayList<MasterFanSeason>>>() {
        @Override
        public void success(final Base<ArrayList<MasterFanSeason>> resp, Response response) {
            if (resp.getCode() == 200) {
                if (resp.getData() != null && resp.getData().size() > 0) {//有数据，设置默认选中1
                    seasonPosition = 0;
                    seasonList = resp.getData();
//                    viewPager.setAdapter(pAdapter);
//                    multiViewPager.setAdapter(pAdapter);
//                    viewPager.setAdapter(new MyPagerAdapter(seasonList));
                    List<View> viewList = new ArrayList<>();
                    for (MasterFanSeason season : seasonList) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.guide_view, null);
                        viewList.add(view);
                    }
                    multiViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                    multiViewPager.setOffscreenPageLimit(viewList.size());
                    multiViewPager.setPageMargin(-dip2px(135));
                    multiViewPager.setAdapter(new MyPagerAdapter(viewList));

//                    HttpClient.getInstance().planListBySeasonId(moduleId, pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), bySeasonIdCallback);
                } else {

                }
            }
        }

        private int dip2px(float dpValue) {
            final float scale = getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    class MyPagerAdapter extends PagerAdapter {

        private List<View> dataList;

        public MyPagerAdapter(List<View> d) {
            dataList = d;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            container.removeView(dataList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = dataList.get(position);
            ImageView img = (ImageView) view.findViewById(R.id.tv_pic);
            TextView textView = (TextView) view.findViewById(R.id.tv_desc);
            textView.setText(seasonList.get(position).getSeasonName());
            Picasso.with(getActivity()).load(seasonList.get(position).getCoverUrl()).into(img);
            container.addView(view);
            return view;
        }
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        dialog.dismiss();
    }

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    class MyAdapter extends BaseAdapter {

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Scheme getItem(int i) {
            return dataList.get(i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_simple_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.nameTxt.setText(dataList.get(position).getSchemeName());
            holder.styleTxt.setText(dataList.get(position).getStyleName());
            //加载内容
            if (TextUtils.isEmpty(dataList.get(position).getWeddingCaseImage())) {
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            } else {
                Picasso.with(getActivity()).load(dataList.get(position).getWeddingCaseImage() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).error(getResources().getDrawable(R.mipmap.ic_launcher)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.contentImg);
            }
            //
            return view;
        }

        class ViewHolder {
            TextView nameTxt;
            TextView styleTxt;
            ImageView contentImg;

            public ViewHolder(View itemView) {
                contentImg = (ImageView) itemView.findViewById(R.id.item_plan_simple_pic_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_plan_simple_name_txt);
                styleTxt = (TextView) itemView.findViewById(R.id.item_plan_simple_style_txt);
            }
        }
    }

    @Override
    public void show() {
        if (NetworkUtil.hasConnection(getActivity())) {
            HttpClient.getInstance().bestFilmList(moduleId, pageIndex, pageSize, cb);
        } else {//not connect
            listView.setPullRefreshEnable(false);
            try {
                dataList = db.findAll(Selector.from(Scheme.class).where("tag", "=", TAG));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

//        HttpClient.getInstance().seasonList(seasonCallback);//分季
    }

    private Callback<Base<ArrayList<Scheme>>> bySeasonIdCallback = new Callback<Base<ArrayList<Scheme>>>() {
        @Override
        public void success(Base<ArrayList<Scheme>> resp, Response response) {
            listView.stopRefresh();
            if (resp.getCode() == 200) {
                if (isSeasonNextPage) {//下一页
                    filterList.addAll(resp.getData());
                    dataList = filterList;
                    adapter.notifyDataSetChanged();
                } else {
                    filterList = resp.getData();
                    dataList = filterList;
                    adapter.notifyDataSetChanged();
                }

                if (resp.getData() != null && resp.getData().size() > 0) {
                    if (resp.getData().size() < pageSize) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                } else {
                    listView.setPullLoadEnable(false);
                }
            } else {
                T.s(getActivity(), "获取数据错误!");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
        }
    };

    private Callback<Base<ArrayList<Scheme>>> cb = new Callback<Base<ArrayList<Scheme>>>() {
        @Override
        public void success(Base<ArrayList<Scheme>> resp, Response response) {
            listView.stopRefresh();
            if (resp.getCode() == 200) {
                if (isNextPage) {//下一页
                    bestList.addAll(resp.getData());

                    //cache
                    try {
                        for (Scheme scheme : resp.getData()) {
                            scheme.setTag(TAG);
                        }
                        db.saveAll(resp.getData());

                       List<Scheme> tempList = db.findAll(Selector.from(Scheme.class).where("tag", "=", TAG));
                        for (Scheme scheme : tempList){
                            S.o(":::" + scheme.getSchemeName());
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    dataList = bestList;
                    adapter.notifyDataSetChanged();

                } else {//first page
                    bestList = resp.getData();
                    dataList = bestList;
                    try {
                        db.delete(Scheme.class, WhereBuilder.b("tag", "=", TAG));
                        for (Scheme scheme : dataList) {
                            scheme.setTag(TAG);
                        }
                        db.saveAll(dataList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
                }

                if (resp.getData() != null && resp.getData().size() > 0) {
                    if (resp.getData().size() < pageSize) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                } else {
                    listView.setPullLoadEnable(false);
                }
            } else {
                T.s(getActivity(), "获取数据错误!");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
        }
    };

    @Override
    public void hide() {

    }

    @Override
    public void onRefresh(int id) {
        listView.startRefresh();
        pageIndex = 1;
        if (tabCustomSeason == 0) {
            isNextPage = false;
            HttpClient.getInstance().bestFilmList(moduleId, pageIndex, pageSize, cb);
        } else {
            isSeasonNextPage = false;
            HttpClient.getInstance().planListBySeasonId(moduleId, pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), bySeasonIdCallback);
        }


    }

    @Override
    public void onLoadMore(int id) {

        pageIndex++;
        if (tabCustomSeason == 0) {
            isNextPage = true;
            HttpClient.getInstance().bestFilmList(moduleId, pageIndex, pageSize, cb);
        } else {
            isSeasonNextPage = true;
            HttpClient.getInstance().planListBySeasonId(moduleId, pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), bySeasonIdCallback);
        }
    }
}
