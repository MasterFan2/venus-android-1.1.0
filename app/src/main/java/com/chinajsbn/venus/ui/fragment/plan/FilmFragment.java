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
import com.chinajsbn.venus.ui.plan.VideoActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
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
 * 婚礼视频
 * Created by 13510 on 2015/12/4.
 */
@FragmentFeature(layout = R.layout.fragment_film)
public class FilmFragment extends BaseFragment implements MasterListView.OnRefreshListener, OnClickListener {

    private final String[] filters = new String[]{"最佳视频", "分季欣赏"};

    ///used by cache
    private DbUtils db;
    private final String TAG = "FilmFragment_tag_film";
    private MTDialog dialog;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

    //--------------data-----------------
    private List<Film> dataList;    //Adapter 显示数据
    private List<Film> bestList;    //最佳视频
    private List<Film> filterList;  //分季视频

    //------------------season-------------------
    private int tabCustomSeason = 0;//0: 最佳跟拍      1：分季欣赏     default:0
    private int seasonPosition = -1; //分季数据选择的位置
    private ViewPager multiViewPager;
    private List<MasterFanSeason> seasonList;

    //--------------page-----------------
    private boolean isRefresh = false;// pull down refresh
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private boolean isSeasonNextPage = false;//true:获取下一页数据    false:获取第一页数据

    @ViewInject(R.id.viewPager)
    private ViewPager viewPager;

    @Override
    public void onClick(MTDialog dialog, View view) {
        dialog.dismiss();
    }

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
            Picasso.with(getActivity()).load(seasonList.get(position).getCoverUrl()).placeholder(R.drawable.loading).into(img);
            container.addView(view);
            return view;
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

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
                    multiViewPager.setVisibility(View.GONE);
                    dataList = bestList;
                    adapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {//分季欣赏
                    multiViewPager.setVisibility(View.VISIBLE);
                    if (filterList == null) {
                        if (seasonPosition != -1 && seasonList != null && seasonList.size() > 0) {
                            HttpClient.getInstance().filmListBySeasonId("hits", "3", pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), filmListBySeasonIdCallback);
                        }
                    } else {
                        dataList = filterList;
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
                HttpClient.getInstance().filmListBySeasonId("hits", "3", pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), filmListBySeasonIdCallback);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        listView.setPullLoadEnable(false);
        listView.setOnRefreshListener(FilmFragment.this, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(NetworkUtil.hasConnection(getActivity())){
                    Intent intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra("url", dataList.get(position - 2).getUrl());
                    animStart(intent);
                }else {
                    handler.sendEmptyMessageDelayed(10, 10);
                    handler.sendEmptyMessageDelayed(11, 3000);
                }

            }
        });
        //init dailog
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(this)
                .create();
//
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

    class MyAdapter extends BaseAdapter {
        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Film getItem(int i) {
            return dataList.get(i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            Film film = dataList.get(position);
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_film, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (film.getCoverImage() != null && !TextUtils.isEmpty(film.getCoverImage().getImageUrl()))
                Picasso.with(getActivity()).load(film.getCoverImage().getImageUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).placeholder(R.drawable.loading).into(holder.coverImg);
            else{
                Picasso.with(getActivity()).load(film.getImgUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).placeholder(R.drawable.loading).into(holder.coverImg);
            }
            holder.nameTxt.setText(film.getName());
            holder.hintTxt.setText("浏览:" + film.getHits());
            //
            return view;
        }

        class ViewHolder {
            ImageView coverImg;
            TextView nameTxt;
            TextView hintTxt;

            public ViewHolder(View itemView) {
                coverImg = (ImageView) itemView.findViewById(R.id.item_film_content_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_film_name_txt);
                hintTxt = (TextView) itemView.findViewById(R.id.item_film_hint_txt);
            }
        }
    }

    @Override
    public void show() {
//        HttpClient.getInstance().filmSeasonList(seasonCallback);
        if(NetworkUtil.hasConnection(getActivity())){
            HttpClient.getInstance().filmList("hits", "3", pageIndex, pageSize, filmCallback);
        }else {
            listView.setPullRefreshEnable(false);
            try {
                dataList = db.findAll(Selector.from(Film.class).where("tag", "=", TAG));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过分季筛选 callback
     */
    Callback<Base<ArrayList<Film>>> filmListBySeasonIdCallback = new Callback<Base<ArrayList<Film>>>() {
        @Override
        public void success(Base<ArrayList<Film>> arrayListBase, Response response) {
            if (isRefresh) {
                listView.stopRefresh();
                isRefresh = false;
            }
            if (arrayListBase.getCode() == 200) {
                if (isSeasonNextPage) {
                    filterList.addAll(arrayListBase.getData());
                } else {
                    filterList = arrayListBase.getData();
                }
                dataList = filterList;

                if (filterList != null && filterList.size() >= pageSize) {
                    listView.setPullLoadEnable(true);
                } else {
                    listView.setPullLoadEnable(false);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (isRefresh) {
                listView.stopRefresh();
                isRefresh = false;
            }
        }
    };

    /**
     * film callback
     */
    Callback<Base<ArrayList<Film>>> filmCallback = new Callback<Base<ArrayList<Film>>>() {
        @Override
        public void success(Base<ArrayList<Film>> arrayListBase, Response response) {
            if (isRefresh) {
                listView.stopRefresh();
                isRefresh = false;
            }
            if (arrayListBase.getCode() == 200) {
                if (isNextPage) {
                    bestList.addAll(arrayListBase.getData());
                    try {
                        for (Film film : arrayListBase.getData()) {
                            film.setTag(TAG);
                            film.setImgUrl(film.getCoverImage().getImageUrl());
                        }
                        db.saveAll(arrayListBase.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    bestList = arrayListBase.getData();
                    try {
                        db.delete(Film.class, WhereBuilder.b("tag", "=", TAG));
                        for (Film film : bestList) {
                            film.setTag(TAG);
                            film.setImgUrl(film.getCoverImage().getImageUrl());
                        }
                        db.saveAll(bestList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                dataList = bestList;
                if (arrayListBase.getData() != null && arrayListBase.getData().size() >= pageSize) {
                    listView.setPullLoadEnable(true);
                } else {
                    listView.setPullLoadEnable(false);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (isRefresh) {
                listView.stopRefresh();
                isRefresh = false;
            }
        }
    };

    /**
     * season callback
     */
    Callback<Base<ArrayList<MasterFanSeason>>> seasonCallback = new Callback<Base<ArrayList<MasterFanSeason>>>() {
        @Override
        public void success(Base<ArrayList<MasterFanSeason>> resp, Response response) {
            if (resp.getCode() == 200) {
                if (resp.getData() != null && resp.getData().size() > 0) {//有数据，设置默认选中1
                    seasonPosition = 0;
                    seasonList = resp.getData();

                    List<View> viewList = new ArrayList<>();
                    for (MasterFanSeason season : seasonList) {
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.guide_view, null);
                        viewList.add(view);
                    }
                    multiViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
                    multiViewPager.setOffscreenPageLimit(viewList.size());
                    multiViewPager.setPageMargin(-dip2px(135));
                    multiViewPager.setAdapter(new MyPagerAdapter(viewList));
                } else {

                }
            }
        }

        @Override
        public void failure(RetrofitError error) {

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
            HttpClient.getInstance().filmList("hits", "3", pageIndex, pageSize, filmCallback);
        } else if (tabCustomSeason == 1) {
            isSeasonNextPage = false;
            if (seasonList != null && seasonList.size() > 0)
                HttpClient.getInstance().filmListBySeasonId("hits", "3", pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), filmListBySeasonIdCallback);
        }
    }

    @Override
    public void onLoadMore(int id) {
        pageIndex++;
        if (tabCustomSeason == 0) {
            isNextPage = true;
            HttpClient.getInstance().filmList("hits", "3", pageIndex, pageSize, filmCallback);
        } else if (tabCustomSeason == 1) {
            isSeasonNextPage = false;
            if (seasonList != null && seasonList.size() > 0)
                HttpClient.getInstance().filmListBySeasonId("hits", "3", pageIndex, pageSize, seasonList.get(seasonPosition).getSeasonId(), filmListBySeasonIdCallback);
        }
    }
}
