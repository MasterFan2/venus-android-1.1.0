package com.chinajsbn.venus.ui.hotels.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.net.bean.FilterBean;
import com.chinajsbn.venus.net.bean.Hotel;
import com.chinajsbn.venus.net.bean.SearchHotelParam;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.hotels.HotelDetailActivity;
import com.chinajsbn.venus.utils.MTDBUtil;
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
import com.tool.widget.MaterialRippleLayout;
import com.tool.widget.common.Switch;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/12/24.
 */
@FragmentFeature(layout = R.layout.fragment_mt_hotel)
public class MTHotelFragment extends BaseFragment implements OnClickListener, MasterListView.OnRefreshListener {

    //////////////////////final var///////////////////
    public static final String TAG = "MTHotelFragment";//缓存用

    //------------------Widget----------------------
    //---------------------------------------------
    //////////////////////main///////////////////
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;  //右侧滑动
    @ViewInject(R.id.myListView)
    private MyListView listView;      //主显示
    private MyAdapter adapter;

    ///////////////////parameters////////////////
    private String parentId = "";   //酒店moduleID
    private List<FilterBean> tables = new ArrayList<>();//second listView data
    private List<FilterBean> prices = new ArrayList<>();//second ListView data
    private List<FilterBean> areas = new ArrayList<>(); //second ListView data
    private int tablePriceArea = 1;    //1:桌数         2:价格      3:区域
    private int tableSelectedItem = -1;//桌数选择的项
    private int priceSelectedItem = -1;//价格选择的项
    private int areaSelectedItem = -1;//区域选择的项

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    /////////////////filter layout///////////////
    /**
     * 一级
     **/
    @ViewInject(R.id.filter_cancel_txt)
    private TextView cancelTxt;     //取消
    @ViewInject(R.id.filter_confirm_txt)
    private TextView confirmTxt;    //确定搜索
    @ViewInject(R.id.filter_gift_switch)
    private Switch giftSwitch;      //礼包
    @ViewInject(R.id.filter_discount_switch)
    private Switch discountSwitch;  //优惠
    @ViewInject(R.id.filter_selected_table_txt)
    private TextView filterTableTxt;//显示/选择 桌数
    @ViewInject(R.id.filter_selected_price_txt)
    private TextView filterPriceTxt;//显示/选择 价钱
    @ViewInject(R.id.filter_selected_area_txt)
    private TextView filterAreaTxt;//显示/选择 区域
    @ViewInject(R.id.filter_clear_btn)
    private MaterialRippleLayout clearRpl;//清除
    @ViewInject(R.id.filter_head_layout)
    private LinearLayout headLayout;//一级的头
    /**
     * 二级
     **/
    @ViewInject(R.id.filter_foot_layout)
    private LinearLayout secondFilterLayout;            //二级整个布局
    @ViewInject(R.id.filter_foot_cancel_rpl)
    private MaterialRippleLayout secondFilterCancelRpl;//二级 返回按钮
    @ViewInject(R.id.filter_foot_listView)
    private ListView secondFilterListview;             //二级 显示列表

    ////////////////custom tab///////////////////
    @ViewInject(R.id.m_tab_all_txt)
    private TextView tabAllTxt;         //所有筛选
    @ViewInject(R.id.m_tab_table_txt)
    private TextView tabTableTxt;       //桌数多少排序
    @ViewInject(R.id.m_tab_price_txt)
    private TextView tabPriceTxt;       //价钱多少排序
    @ViewInject(R.id.m_tab_filter_txt)
    private TextView tabFilterTxt;      //弹出筛选

    //-------------多条件搜索&page-----------------
    //---------------------------------------------
    /**
     * 筛选条件
     **/
    private SearchHotelParam param = new SearchHotelParam();
    /**
     * 显示的数据
     **/
    private List<Hotel> dataList;//列表数据
    /**
     * 分页
     **/
    private int pageIndex = 1;    //第几页
    private int pageSize = 10;   //每页大小
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private int lastVisibleItem;
    private int totalCount = -1;
    private boolean isTabAll = true;//只有true 缓存数据
    /**
     * 搜索
     **/
    private int tag_table_state = -1;//桌数选择状态   -1：从未选过      1：从小到大      2：从大到小
    private int tag_price_state = -1;//价钱选择状态   -1：从未选过      1：从小到大      2：从大到小
    //------------------end 多条件搜索-------------


    //--------------------Cache--------------------
    //---------------------------------------------
    private DbUtils db;

    //--------------------OnClick------------------
    //---------------------------------------------
    @OnClick(R.id.m_tab_all_txt)
    public void mTabAllClick(View view) {//所有搜索
        isTabAll = true;
        param.resetAll();//重置参数
        clear(null);//清除选择

        //设置全部的选中状态
        tabAllTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        tabTableTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabPriceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabFilterTxt.setTextColor(getResources().getColor(R.color.gray_600));

        //设置箭头方向
        Drawable drawableTable = getResources().getDrawable(R.mipmap.ic_double_arrownone);
        drawableTable.setBounds(0, 0, drawableTable.getMinimumWidth(), drawableTable.getMinimumHeight());
        tabTableTxt.setCompoundDrawables(null, null, drawableTable, null);

        Drawable drawablePrice = getResources().getDrawable(R.mipmap.ic_double_arrownone);
        drawablePrice.setBounds(0, 0, drawablePrice.getMinimumWidth(), drawablePrice.getMinimumHeight());
        tabPriceTxt.setCompoundDrawables(null, null, drawablePrice, null);

        if (NetworkUtil.hasConnection(getActivity())) {
            param.pageIndex = 1;
            isNextPage = false;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {
            localSearch();
        }
    }

    @OnClick(R.id.m_tab_table_txt)
    public void mTabTableClick(View view) {//桌数多少排序
        isTabAll = false;
        tabAllTxt.setTextColor(getResources().getColor(R.color.gray_600));//设置全部的选中状态
        tabTableTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        tabPriceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabFilterTxt.setTextColor(getResources().getColor(R.color.gray_600));
        param.sort = "table";
        if (tag_table_state == -1) {
            tag_table_state = 1;
            param.order = "asc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabTableTxt.setCompoundDrawables(null, null, drawable, null);
        } else if (tag_table_state == 1) {
            tag_table_state = 2;
            param.order = "desc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabTableTxt.setCompoundDrawables(null, null, drawable, null);
        } else if (tag_table_state == 2) {
            tag_table_state = 1;
            param.order = "asc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabTableTxt.setCompoundDrawables(null, null, drawable, null);
        }

        Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrownone);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tabPriceTxt.setCompoundDrawables(null, null, drawable, null);

        if (NetworkUtil.hasConnection(getActivity())) {
            isNextPage = false;
            param.pageIndex = 1;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {
            localSearch();
        }
    }

    /**
     * 右侧点击， 防止在右侧菜单出来时点到 列表item或其它操作
     **/
    @OnClick(R.id.filter_layout)
    public void filterClick(View view) {
        S.o(":");
    }

    /**
     * 本地搜索
     **/
    private void localSearch() {
        try {
            Selector selector = Selector.from(Hotel.class).where("hotelName", "!=", "null");
            if (param.isGift != -1) {
                selector.and("isGift", "=", "1");
            }
            if (param.isDisaccount != -1) {
                selector.and("isDiscount", "=", "1");
            }
            if (param.cityId != -1) {
                selector.and("cityId", "=", param.cityId);
            }
            if (param.minPrice != -1) {//lowestConsumption
                //selector.and("highestConsumption", "between", new int[]{param.minPrice, param.maxPrice});
//                selector.or(WhereBuilder.b("lowestConsumption", "<=", param.minPrice).and("highestConsumption", ">=", param.minPrice));
//                selector.or(WhereBuilder.b("lowestConsumption", "<=", param.maxPrice).and("highestConsumption", ">=", param.maxPrice));
//                selector.and(WhereBuilder.b("highestConsumption", ">=", param.maxPrice).or("lowestConsumption", ">=", param.minPrice));

//                //
//                selector.or(WhereBuilder.b("lowestConsumption", "<=", param.minPrice).and("highestConsumption", ">=", param.minPrice))
//                        .or(WhereBuilder.b("lowestConsumption", "<=", param.maxPrice).and("highestConsumption", ">=", param.maxPrice))
//                        .or(WhereBuilder.b("lowestConsumption", ">=", param.minPrice).and("highestConsumption", "<=", param.maxPrice));

                //
                selector.and(WhereBuilder.b("highestConsumption", ">=", param.maxPrice).or("highestConsumption", ">=", param.minPrice))
//                        .or(WhereBuilder.b("lowestConsumption", ">=", param.minPrice).and("highestConsumption", "<=", param.maxPrice))
                ;
            }
            if (param.minTable != -1) {
                selector.and("capacityPerTable", "between", new int[]{param.minTable, param.maxTable});
            }
            if (param.sort.equals("price")) {//pricew
                selector.orderBy("highestConsumption", param.order.equals("desc") ? true : false);
            } else {//table
                selector.orderBy("capacityPerTable", param.order.equals("desc") ? true : false);
            }

            dataList = db.findAll(selector);
            adapter.notifyDataSetChanged();
            S.o("aaa");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.m_tab_price_txt)
    public void mTabPriceClick(View view) {//价钱高低排序
        isTabAll = false;
        tabAllTxt.setTextColor(getResources().getColor(R.color.gray_600));//设置全部的选中状态
        tabTableTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabPriceTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        tabFilterTxt.setTextColor(getResources().getColor(R.color.gray_600));
        param.sort = "price";
        if (tag_price_state == -1) {
            tag_price_state = 1;
            param.order = "asc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabPriceTxt.setCompoundDrawables(null, null, drawable, null);
        } else if (tag_price_state == 1) {
            tag_price_state = 2;
            param.order = "desc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabPriceTxt.setCompoundDrawables(null, null, drawable, null);
        } else if (tag_price_state == 2) {
            tag_price_state = 1;
            param.order = "asc";
            Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tabPriceTxt.setCompoundDrawables(null, null, drawable, null);
        }

        Drawable drawable = getResources().getDrawable(R.mipmap.ic_double_arrownone);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tabTableTxt.setCompoundDrawables(null, null, drawable, null);

        if (NetworkUtil.hasConnection(getActivity())) {
            isNextPage = false;
            param.pageIndex = 1;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {
            localSearch();
        }
    }

    @OnClick(R.id.m_tab_filter_txt)
    public void mTabFilterClick(View view) {//筛选界面
        tabAllTxt.setTextColor(getResources().getColor(R.color.gray_600));//设置全部的选中状态
        tabTableTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabPriceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tabFilterTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));

        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    @OnClick(R.id.filter_clear_btn)
    public void clear(View v) {             //filter 清除选择
        if (discountSwitch.isChecked()) {
            discountSwitch.setChecked(false);
        }

        if (giftSwitch.isChecked()) {
            giftSwitch.setChecked(false);
        }

        tabFilterTxt.setTextColor(getResources().getColor(R.color.gray_600));
        filterTableTxt.setTextColor(getResources().getColor(R.color.gray_500));//文字颜色
        filterTableTxt.setText("全部");
        filterPriceTxt.setTextColor(getResources().getColor(R.color.gray_500));
        filterPriceTxt.setText("全部");
        filterAreaTxt.setTextColor(getResources().getColor(R.color.gray_500));
        filterAreaTxt.setText("全部");
        //重置参数
        param.resetFilter();

    }

    @OnClick(R.id.filter_cancel_txt)
    public void cancelClick(View view) {   //取消搜索
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.filter_confirm_txt)
    public void confirmClick(View view) {  //确认搜索
        isTabAll = false;
        drawerLayout.closeDrawers();
        if (NetworkUtil.hasConnection(getActivity())) {
            isNextPage = false;
            param.pageIndex = 1;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {
            localSearch();
        }
    }

    @OnClick(R.id.filter_table_layout)
    public void tableClick(View view) {    //选择桌数   弹出第二界面
        tablePriceArea = 1;
        secondFilterListview.setAdapter(new FilterAdapter(tables));
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        anim.setFillAfter(true);
        headLayout.setVisibility(View.GONE);
        secondFilterLayout.setVisibility(View.VISIBLE);
        secondFilterLayout.startAnimation(anim);
    }

    @OnClick(R.id.filter_price_layout)
    public void priceClick(View view) {   //选择价钱   弹出第二界面
        tablePriceArea = 2;
        secondFilterListview.setAdapter(new FilterAdapter(prices));
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        anim.setFillAfter(true);
        headLayout.setVisibility(View.GONE);
        secondFilterLayout.setVisibility(View.VISIBLE);
        secondFilterLayout.startAnimation(anim);
    }

    @OnClick(R.id.filter_area_layout)
    public void areaClick(View view) {   //选择区域   弹出第二界面
        tablePriceArea = 3;
        secondFilterListview.setAdapter(new FilterAdapter(areas));
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        anim.setFillAfter(true);
        headLayout.setVisibility(View.GONE);
        secondFilterLayout.setVisibility(View.VISIBLE);
        secondFilterLayout.startAnimation(anim);
    }

    @OnClick(R.id.filter_foot_cancel_rpl)
    public void footCancelClick(View view) {//关闭二级菜单
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                headLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        secondFilterLayout.setVisibility(View.VISIBLE);
        secondFilterLayout.startAnimation(anim);
    }
    //----------------OnClick end------------------

    /**
     * INIT
     */
    @Override
    public void initialize() {

        //db init
        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        //获取 XML 配置好的酒店和桌数列表数据
        String tempStr[] = getResources().getStringArray(R.array.filterTable);
        for (int i = 0; i < tempStr.length; i++) {
            tables.add(new FilterBean(tempStr[i], false));
        }
        tempStr = getResources().getStringArray(R.array.filterPrice);
        for (int i = 0; i < tempStr.length; i++) {
            prices.add(new FilterBean(tempStr[i], false));
        }

        //酒店 或桌数 点击事件
        secondFilterListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tablePriceArea == 1) {//桌数
                    if (tableSelectedItem != -1) {//选择过
                        tables.get(tableSelectedItem).setChecked(false);//取消原来的选择
                    }
                    tableSelectedItem = position;
                    filterTableTxt.setText(tables.get(tableSelectedItem).getName());
                    if (tableSelectedItem == 0) {
                        filterTableTxt.setTextColor(getResources().getColor(R.color.gray_500));
                    } else {
                        filterTableTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
                    }
                    getSelectedTableOrPrice(position, true);
                } else if (tablePriceArea == 2) {           //价格
                    if (priceSelectedItem != -1) {//选择过
                        prices.get(priceSelectedItem).setChecked(false);//取消原来的选择
                    }
                    priceSelectedItem = position;
                    filterPriceTxt.setText(prices.get(priceSelectedItem).getName());

                    if (priceSelectedItem == 0) {
                        filterPriceTxt.setTextColor(getResources().getColor(R.color.gray_500));
                    } else {
                        filterPriceTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
                    }
                    getSelectedTableOrPrice(position, false);

                } else if (tablePriceArea == 3) {//areaSelectedItem
                    if (areaSelectedItem != -1) {//选择过
                        areas.get(areaSelectedItem).setChecked(false);//取消原来的选择
                    }
                    areaSelectedItem = position;
                    filterAreaTxt.setText(areas.get(areaSelectedItem).getName());

                    if (areaSelectedItem == 0) {
                        filterAreaTxt.setTextColor(getResources().getColor(R.color.gray_500));
                        param.cityId = -1;
                    } else {
                        filterAreaTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
                        param.cityId = areas.get(position).getId();
                    }

                }
                //关闭二级菜单
                footCancelClick(null);
            }
        });

        ///无网络提示///
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        networkDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(MTHotelFragment.this)
                .create();

        ///礼包优惠///
        discountSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) param.isDisaccount = 1;
                else param.isDisaccount = -1;
            }
        });
        giftSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if (checked) param.isGift = 1;
                else param.isGift = -1;
            }
        });

        ///
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setOnRefreshListener(this, 0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataList != null && dataList.size() > 0) {
                    if (!NetworkUtil.hasConnection(getActivity())) {
                        handler.sendEmptyMessageDelayed(10, 100);
                        handler.sendEmptyMessageDelayed(11, 4000);
                        return;
                    }
                    Intent intent = new Intent(getActivity(), HotelDetailActivity.class);
                    intent.putExtra("hotelId", parentId);
                    intent.putExtra("detailId", dataList.get(position - 1).getHotelId());
                    intent.putExtra("name", dataList.get(position - 1).getHotelName());
                    animStart(intent);
                }
            }
        });
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        ///parameters ///
        parentId = getArguments().getString("parentId");

        tabAllTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));//设置全部的选中状态

        ///check network
        if (NetworkUtil.hasConnection(getActivity())) {
            HttpClient.getInstance().searchHotels(parentId, param, callback);
            HttpClient.getInstance().getHotelAreas(areaCallback);
        } else {
            try {
                dataList = db.findAll(Hotel.class);
                if (dataList != null && dataList.size() > 0) {
                    adapter.notifyDataSetChanged();
                    listView.setPullLoadEnable(false);
                } else {
                    T.s(getActivity(), "请连网");
                }
                areas = db.findAll(FilterBean.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    } //init end

    private Callback<Base<List<FilterBean>>> areaCallback = new Callback<Base<List<FilterBean>>>() {
        @Override
        public void success(Base<List<FilterBean>> resp, Response response) {
            if (resp.getCode() == 200) {
                FilterBean all = new FilterBean(false, 0, "全部");
                areas.add(all);
                if (resp.getData() != null && resp.getData().size() > 0) {
                    areas.addAll(resp.getData());
                    try {
                        db.delete(FilterBean.class, WhereBuilder.b("'name'", "!=", "null"));
                        db.saveAll(areas);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    /**
     * 设置筛选条件的桌数和价格
     **/
    private void getSelectedTableOrPrice(int position, boolean isTable) {
        switch (position) {
            case 0:
                if (isTable) {
                    param.minTable = -1;
                    param.maxTable = -1;
                } else {
                    param.minPrice = -1;
                    param.maxPrice = -1;
                }
                break;
            case 1:
                if (isTable) {
                    param.minTable = 0;
                    param.maxTable = 9;
                } else {
                    param.minPrice = 0;
                    param.maxPrice = 1999;
                }
                break;
            case 2:
                if (isTable) {
                    param.minTable = 10;
                    param.maxTable = 20;
                } else {
                    param.minPrice = 2000;
                    param.maxPrice = 3000;
                }
                break;
            case 3:
                if (isTable) {
                    param.minTable = 20;
                    param.maxTable = 30;
                } else {
                    param.minPrice = 3000;
                    param.maxPrice = 4000;
                }
                break;
            case 4:
                if (isTable) {
                    param.minTable = 31;
                    param.maxTable = 1000000;
                } else {
                    param.minPrice = 4001;
                    param.maxPrice = 1000000;
                }
                break;
            default:
                if (isTable) {
                    param.minTable = 0;
                    param.maxTable = 1000000;
                } else {
                    param.minPrice = 0;
                    param.maxPrice = 1000000;
                }
                break;
        }
    }

    @Override
    public void onRefresh(int id) {
        if (NetworkUtil.hasConnection(getActivity())) {
            isNextPage = false;
            param.pageIndex = 1;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {

        }
    }

    @Override
    public void onLoadMore(int id) {
        if (NetworkUtil.hasConnection(getActivity())) {
            param.pageIndex = param.pageIndex + 1;
            isNextPage = true;
            HttpClient.getInstance().searchHotels(parentId, param, callback);
        } else {
            T.s(getActivity(), "请连网");
        }
    }

    /**
     * call back
     */
    private Callback<Base<ArrayList<Hotel>>> callback = new Callback<Base<ArrayList<Hotel>>>() {

        @Override
        public void success(Base<ArrayList<Hotel>> hotels, Response response) {
            listView.stopRefresh();
            if (hotels.getCode() == 200) {
                if (isNextPage) {//下一页
                    dataList.addAll(hotels.getData());

                    if (isTabAll) {
                        try {
                            db.saveAll(hotels.getData());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

                } else {         //获取第一页
                    dataList = hotels.getData();
                    if (isTabAll) {
                        try {
                            db.deleteAll(Hotel.class);
                            db.saveAll(dataList);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //是否有下一页
                if (hotels.getData() != null && hotels.getData().size() > 0) {
                    if (hotels.getData().size() < pageSize) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                } else {
                    listView.setPullLoadEnable(false);
                }

            } else {
                T.s(getActivity(), "获取数据错误, 请稍后重试...");
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "数据库错误");
        }
    };

    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_hotel, parent, false);
                myViewHolder = new MyViewHolder(convertView);

                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            //
            final Hotel hotel = dataList.get(position);

            String strName = hotel.getHotelName();
            myViewHolder.nameTxt.setText(strName);

            if (hotel.getIsDiscount().equals("1")) {
                myViewHolder.icDisccountImg.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.icDisccountImg.setVisibility(View.GONE);
            }
            if (hotel.getIsGift().equals("1")) {
                myViewHolder.icGiftImg.setVisibility(View.VISIBLE);
            } else {
                myViewHolder.icGiftImg.setVisibility(View.GONE);
            }

            String talbe_str_2 = hotel.getBanquetHallCount();
            String talbe_str_3 = talbe_str_2 + "个宴会厅,容纳";
            String talbe_str_4 = talbe_str_3 + hotel.getCapacityPerTable();
            String talbe_str_5 = talbe_str_4 + "桌";

            SpannableStringBuilder tableStyle = new SpannableStringBuilder(talbe_str_5);

            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.filter_red_txt)), 0, talbe_str_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.filter_red_txt)), talbe_str_3.length(), talbe_str_4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_600)), talbe_str_2.length(), talbe_str_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_600)), talbe_str_4.length(), talbe_str_5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.typeTxt.setText(hotel.getTypeName());

            myViewHolder.tableTxt.setText(tableStyle);

//            String addrs = "位置：" + hotel.getAddress();
            String addrs = hotel.getAddress();
            SpannableStringBuilder addressStyle = new SpannableStringBuilder(addrs);
//            addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_400)), 0, addrs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.addrTxt.setText(addressStyle);
            myViewHolder.priceTxt.setText("￥ " + hotel.getLowestConsumption() + " - " + hotel.getHighestConsumption());

            if (!TextUtils.isEmpty(hotel.getImageUrl())) {
                Picasso.with(getActivity()).load(hotel.getImageUrl() + "@400w_267h").error(R.mipmap.load_error).into(myViewHolder.logoImg);
            }

            //
            return convertView;
        }

        public class MyViewHolder {

            public ImageView logoImg;
            public TextView nameTxt;
            public TextView tableTxt;
            public TextView addrTxt;
            public TextView priceTxt;
            public TextView typeTxt;
            public ImageView icDisccountImg;
            public ImageView icGiftImg;

            public MyViewHolder(View itemView) {
                logoImg = (ImageView) itemView.findViewById(R.id.item_hotel_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_hotel_name_txt);
                tableTxt = (TextView) itemView.findViewById(R.id.item_hotel_table_txt);
                addrTxt = (TextView) itemView.findViewById(R.id.item_hotel_addr_txt);
                priceTxt = (TextView) itemView.findViewById(R.id.item_hotel_price_txt);
                typeTxt = (TextView) itemView.findViewById(R.id.item_hotel_type_txt);
                icDisccountImg = (ImageView) itemView.findViewById(R.id.item_hotel_disccount_ic_img);
                icGiftImg = (ImageView) itemView.findViewById(R.id.item_hotel_gift_ic_img);
            }
        }
    }

    ///筛选ListView adapter///
    class FilterAdapter extends BaseAdapter {
        private List<FilterBean> lists;

        public FilterAdapter(List<FilterBean> l) {
            lists = l;
        }

        @Override
        public int getCount() {
            return lists == null ? 0 : lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_filter_layout, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(lists.get(position).getName());
            if (lists.get(position).isChecked()) {
                holder.img.setVisibility(View.VISIBLE);
                holder.textView.setTextColor(getResources().getColor(R.color.filter_red_txt));
            } else {
                holder.img.setVisibility(View.INVISIBLE);
                holder.textView.setTextColor(getResources().getColor(R.color.gray_600));
            }
            //
            return convertView;
        }

        class ViewHolder {
            TextView textView;
            ImageView img;

            public ViewHolder(View view) {
                textView = (TextView) view.findViewById(R.id.item_filter_txt);
                img = (ImageView) view.findViewById(R.id.item_filter_img);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10://show
                    networkDialog.show();
                    break;
                case 11://close
                    if (networkDialog != null && networkDialog.isShowing()) networkDialog.dismiss();
                    break;
                default:
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


    /***************************
     * 用不到
     ********************************/
    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }
}
