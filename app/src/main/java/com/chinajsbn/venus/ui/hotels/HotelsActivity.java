package com.chinajsbn.venus.ui.hotels;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Html.ImageGetter;
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
import com.chinajsbn.venus.net.bean.Banquet;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.net.bean.FilterBean;
import com.chinajsbn.venus.net.bean.Hotel;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.utils.MTDBUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
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

@ActivityFeature(layout = R.layout.activity_hotels)
public class HotelsActivity extends MBaseFragmentActivity implements OnRecyclerItemClickListener, SwipeRefreshLayout.OnRefreshListener, MasterListView.OnRefreshListener ,OnClickListener {

    public static final String TAG = "HotelsActivity";

    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

//    @ViewInject(R.id.tab_layout)
//    private TabLayout tabLayout;
    @ViewInject(R.id.m_tab)
    private LinearLayout mTab;

    @ViewInject(R.id.head_tab_layout)
    private LinearLayout headTablayout;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    //------------------多条件搜索----------------------
    private String gDiscount = "";
    private String gGift     = "";
    private String gTable = "";
    private String gPrice = "";

    //------------------end 多条件搜索----------------------


    //--------------------filter--------------------
    @ViewInject(R.id.filter_discount_switch)
    private Switch discountSwitch;

    @ViewInject(R.id.filter_gift_switch)
    private Switch giftSwitch;
    //-----------------end filter------------------


    //--------------------Cache--------------------
    private DbUtils db;
    //------------------end Cache------------------

    /**
     * head
     */
    @ViewInject(R.id.filter_table_txt)
    private TextView tableTxt;

    @ViewInject(R.id.filter_head_layout)
    private LinearLayout headlayout;

    /**
     * foot
     */
    @ViewInject(R.id.filter_foot_layout)
    private LinearLayout footlayout;

    @ViewInject(R.id.filter_foot_listView)
    private ListView fListView;

    @ViewInject(R.id.filter_foot_cancel_img)
    private ImageView fBackImg;

    @ViewInject(R.id.filter_foot_confirm_txt)
    private TextView fConfirmTxt;

    @ViewInject(R.id.filter_foot_title_txt)
    private TextView fTitleTxt;

    @ViewInject(R.id.filter_selected_table_txt)
    private TextView tableSelectedTxt;

    @ViewInject(R.id.filter_selected_price_txt)
    private TextView priceSelectedTxt;

    //---------------- end filter-------------------

    //----------------my tab ----------------------
    @ViewInject(R.id.m_tab_table_img)
    private ImageView tableArrowImg;

    @ViewInject(R.id.m_tab_price_img)
    private ImageView priceArrowImg;

    /**当前的Tab选择项*/
    private int currentSelected = 1;

    /**
     * 全部
     */
    @ViewInject(R.id.m_tab_all_txt)
    private TextView mTabAllTxt;

    @OnClick(R.id.m_tab_all_txt)
    public void mTabAllClick(View view) {
        if (currentSelected != 1) {
            currentSelected = 1;
            mTabAllTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
            mTabTableTxt.setTextColor(getResources().getColor(R.color.gray_600));
            mTabPriceTxt.setTextColor(getResources().getColor(R.color.gray_600));

            if (!tableAsc) {
                tableArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
            } else {
                tableArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
            }

            if (!priceAsc) {
                priceArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
            }else {
                priceArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
            }

            if(NetworkUtil.hasConnection(context)) {
                getFirstPages();
            }else {
                try {
                    dataList = db.findAll(Selector.from(Hotel.class));
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 桌数
     */
    @ViewInject(R.id.m_tab_table_txt)
    private TextView mTabTableTxt;

    @OnClick(R.id.m_tab_table_layout)
    public void mTabTableClick(View view) {
        mTabAllTxt.setTextColor(getResources().getColor(R.color.gray_600));
        mTabTableTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        mTabPriceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        currentSelected = 2;
        String tableOrder = "";
        if (tableAsc) {
            tableOrder = "asc";
            tableArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow_press);
        } else {
            tableOrder = "desc";
            tableArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow_press);
        }
        if (!priceAsc) {
            priceArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
        }else {
            priceArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
        }
        if(NetworkUtil.hasConnection(context)){
            dialog.show();
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "table", tableOrder, cb);
        }else {
            try {
                dataList = db.findAll(Selector.from(Hotel.class).orderBy("capacityPerTable", tableAsc));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        tableAsc = !tableAsc;
        pageIndex = 1;
        isNextPage = false;

    }

    /**
     * 价格
     */
    @ViewInject(R.id.m_tab_price_txt)
    private TextView mTabPriceTxt;

    @OnClick(R.id.m_tab_price_layout)
    public void mTabPriceClick(View view) {
        mTabAllTxt.setTextColor(getResources().getColor(R.color.gray_600));
        mTabTableTxt.setTextColor(getResources().getColor(R.color.gray_600));
        mTabPriceTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        currentSelected = 3;
        String priceOrder = "";
        if (priceAsc) {
            priceOrder = "asc";
            priceArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow_press);
        } else {
            priceOrder = "desc";
            priceArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow_press);
        }
        if (!tableAsc) {
            tableArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
        } else {
            tableArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
        }


        if(NetworkUtil.hasConnection(context)){
            dialog.show();
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "price", priceOrder, cb);
        }else {
            try {
                dataList = db.findAll(Selector.from(Hotel.class).orderBy("highestConsumption", priceAsc));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        priceAsc = !priceAsc;
        pageIndex = 1;
        isNextPage = false;
    }

    /**
     * 筛选
     */
    @ViewInject(R.id.m_tab_filter_txt)
    private TextView mTabFilterTxt;

    @OnClick(R.id.m_tab_filter_txt)
    public void mTabFilterClick(View view) {

        mTabFilterTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
//        if (!tableAsc) {
//            tableArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
//        } else {
//            tableArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
//        }
//
//        if (!priceAsc) {
//            priceArrowImg.setImageResource(R.mipmap.ic_filter_down_arrow);
//        }else {
//            priceArrowImg.setImageResource(R.mipmap.ic_filter_up_arrow);
//        }
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    //---------------- end my tab ----------------------

    @ViewInject(R.id.myListView)
    private MyListView listView;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @OnClick(R.id.filter_clear_btn)
    public void clear(View v) {
        gGift = "";
        gDiscount = "";
        tableSelect = -1;
        priceSelect = -1;

        if(discountSwitch.isChecked()){
            discountSwitch.setChecked(false);
        }

        if(giftSwitch.isChecked()){
            giftSwitch.setChecked(false);
        }

        mTabFilterTxt.setTextColor(getResources().getColor(R.color.gray_600));
        tableSelectedTxt.setTextColor(getResources().getColor(R.color.gray_500));
        tableSelectedTxt.setText("全部");
        priceSelectedTxt.setTextColor(getResources().getColor(R.color.gray_500));
        priceSelectedTxt.setText("全部");
    }

    private final String[] filters = new String[]{"全部", "桌数", "价格", "礼包", "优惠"};

    private LinearLayoutManager layoutManager;
    private MyAdapter adapter;

//    private ArrayList<SubModule> subModules;
    private String parentId = "";

    private ArrayList<Hotel> data;

    private boolean isdo = false;

    private ArrayList<Banquet> banquets;

    //--------------page-----------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private int lastVisibleItem;
    private int totalCount = -1;

    //----------------------------------
    private List<Hotel> dataList;

    //---------------filtrate-----------------
    private String currentSelect = "全部";
    private ArrayList<FilterBean> tables = new ArrayList<>();
    private ArrayList<FilterBean> prices = new ArrayList<>();
    private String tablePrice = "";//选择的桌数还是价格，以选中item

    private int tableSelect = -1;//选择的桌数量
    private int priceSelect = -1;//选择的价格数量


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10://show
                    mtdialog.show();
                    break;
                case 11://close
                    if(mtdialog != null && mtdialog.isShowing()) mtdialog.dismiss();
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

    class FilterAdapter extends BaseAdapter {
        private ArrayList<FilterBean> lists;

        public FilterAdapter(ArrayList<FilterBean> l) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_filter_layout, parent, false);
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

    @OnClick(R.id.filter_cancel_txt)
    public void cancelClick(View view) {
        drawerLayout.closeDrawers();
    }

    @OnClick(R.id.filter_confirm_txt)
    public void confirmClick(View view) {
        drawerLayout.closeDrawers();
        pageIndex = 1;
        isNextPage = false;

        mTabFilterTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));

        if((tableSelect != -1 && tableSelect != 0) && (priceSelect != -1 && priceSelect != 0)){//桌数/价格筛选

            int table_min = 0;
            int table_max = 0;
            switch (tableSelect){
                case 1:
                    table_min = 1;
                    table_max = 10;
                    break;
                case 2:
                    table_min = 10;
                    table_max = 20;
                    break;
                case 3:
                    table_min = 20;
                    table_max = 30;
                    break;
                case 4:
                    table_min = 30;
                    table_max = 999;
                    break;
            }

            int price_min = 0;
            int price_max = 0;
            switch (priceSelect){
                case 1:
                    price_min = 1;
                    price_max = 1999;
                    break;
                case 2:
                    price_min = 2000;
                    price_max = 3000;
                    break;
                case 3:
                    price_min = 3000;
                    price_max = 4000;
                    break;
                case 4:
                    price_min = 4000;
                    price_max = 99999;
                    break;
            }

            if(NetworkUtil.hasConnection(context)){
                dialog.show();
                HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize, gDiscount, gGift, table_min, table_max, price_min, price_max, cb);
            }else {
                try {
//                    dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{table_min, table_max}));

                    if(currentSelected == 2){//桌数
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{table_min, table_max})
                                .and("highestConsumption", "between", new int[]{price_min, price_max}).and("isDiscount", "=", gDiscount).and("isGift", "=", gGift).orderBy("capacityPerTable", tableAsc));
                    }else if(currentSelected == 3){//价格
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{table_min, table_max})
                                .and("highestConsumption", "between", new int[]{price_min, price_max}).and("isDiscount", "=", gDiscount).and("isGift", "=", gGift).orderBy("highestConsumption", priceAsc));
                    }else {
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{table_min, table_max})
                                .and("highestConsumption", "between", new int[]{price_min, price_max}).and("isDiscount", "=", gDiscount).and("isGift", "=", gGift));
                    }

                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }else if(tableSelect != -1 && tableSelect != 0) {//只有桌数筛选
            int min = 0;
            int max = 0;
            switch (tableSelect){
                case 1:
                    min = 1;
                    max = 10;
                    break;
                case 2:
                    min = 10;
                    max = 20;
                    break;
                case 3:
                    min = 20;
                    max = 30;
                    break;
                case 4:
                    min = 30;
                    max = 999;
                    break;
            }
            if(NetworkUtil.hasConnection(context)){

               // HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else {
                try {
                    if(currentSelected == 2){//桌数
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{min, max}).orderBy("capacityPerTable", tableAsc));
                    }else if(currentSelected == 3){//价格
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{min, max}).orderBy("highestConsumption", !priceAsc));
                    }else {
                        dataList = db.findAll(Selector.from(Hotel.class).where("capacityPerTable", "between", new int[]{min, max}));
                    }

                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }else if(priceSelect != -1 && priceSelect != 0) {//只有价格筛选
            int min = 0;
            int max = 0;
            switch (priceSelect){
                case 1:
                    min = 1;
                    max = 1999;
                    break;
                case 2:
                    min = 2000;
                    max = 3000;
                    break;
                case 3:
                    min = 3000;
                    max = 4000;
                    break;
                case 4:
                    min = 4000;
                    max = 99999;
                    break;
            }

            if(NetworkUtil.hasConnection(context)){
               // HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else {
                try {
                    if(currentSelected == 2){//桌数
                        dataList = db.findAll(Selector.from(Hotel.class).where("highestConsumption", "between", new int[]{min, max}).orderBy("capacityPerTable", tableAsc));
                    }else if(currentSelected == 3){//价格
                        dataList = db.findAll(Selector.from(Hotel.class).where("highestConsumption", "between", new int[]{min, max}).orderBy("highestConsumption", priceAsc));
                    }else {
                        dataList = db.findAll(Selector.from(Hotel.class).where("highestConsumption", "between", new int[]{min, max}));
                    }
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

        }else {//只有优惠和礼包的选择

            if(NetworkUtil.hasConnection(context)){
                //HttpClient.getInstance().gFiltrateHotelsByDiscountGift(parentId, pageIndex, pageSize, gDiscount, gGift, cb);
            }else {
                try {
                    dataList = db.findAll(Selector.from(Hotel.class).where("isDiscount", "=", gDiscount).and("isGift", "=", gGift));
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

        }

        //--------------------------------------------------------------------------------------------------------------
//        if((!TextUtils.isEmpty(gTable) && !gTable.equals("全部")) && (!TextUtils.isEmpty(gPrice) && !gPrice.equals("全部") )){//两个条件都有
//            String [] tables = gTable.split("-");
//            String [] prices = gPrice.split("-");
//            HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize,
//                    gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//        }else if(!TextUtils.isEmpty(gTable) && !gTable.equals("全部")){//只有桌数
//            String [] tables = gTable.split("-");
//            HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), cb);
//        }else if(!TextUtils.isEmpty(gPrice) && !gPrice.equals("全部")){//只有价格
//            String [] prices = gPrice.split("-");
//            HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//        }else{
//            HttpClient.getInstance().gFiltrateHotelsByDiscountGift(parentId, pageIndex, pageSize, gDiscount, gGift, cb);
//        }
    }

    @OnClick(R.id.filter_table_layout)
    public void tableClick(View view) {
//        layout.setVisibility(View.VISIBLE);
        tablePrice = "table";
        fTitleTxt.setText("桌数");
        if (tableSelect != -1) {
            tables.get(tableSelect).setChecked(true);
        }
        fListView.setAdapter(new FilterAdapter(tables));
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        anim.setFillAfter(true);
        headlayout.setVisibility(View.GONE);
        footlayout.setVisibility(View.VISIBLE);
        footlayout.startAnimation(anim);
    }

    @OnClick(R.id.filter_price_layout)
    public void priceClick(View view) {
        tablePrice = "price";
        fTitleTxt.setText("价格");
        if (priceSelect != -1) {
            prices.get(priceSelect).setChecked(true);
        }
        fListView.setAdapter(new FilterAdapter(prices));
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
        anim.setFillAfter(true);
        headlayout.setVisibility(View.GONE);
        footlayout.setVisibility(View.VISIBLE);
        footlayout.startAnimation(anim);
    }

    //关闭二级菜单
    @OnClick(R.id.filter_foot_cancel_img)
    public void footCancelClick(View view) {
//        layout.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_out_right);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                headlayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        footlayout.setVisibility(View.VISIBLE);
        footlayout.startAnimation(anim);
    }


    //--------------------------------
    private void getFirstPages() {
        isNextPage = false;
        pageIndex = 1;
        listView.startRefresh();
        listView.saveRefreshStrTime();

        if       (currentSelected == 1){
            HttpClient.getInstance().getHotels(parentId, pageIndex, pageSize, cb);
        }else if (currentSelected == 2){
            String tableOrder = "";
            if (tableAsc) {
                tableOrder = "asc";
            } else {
                tableOrder = "desc";
            }
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "table", tableOrder, cb);
        }else if (currentSelected == 3){
            String priceOrder = "";
            if (priceAsc) {
                priceOrder = "asc";
            } else {
                priceOrder = "desc";
            }
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "price", priceOrder, cb);
        }else if (currentSelected == 4){

            if((tableSelect != -1 && tableSelect != 0) && (priceSelect != -1 && priceSelect != 0)){//桌数/价格筛选

                int table_min = 0;
                int table_max = 0;
                switch (tableSelect){
                    case 1:
                        table_min = 1;
                        table_max = 10;
                        break;
                    case 2:
                        table_min = 10;
                        table_max = 20;
                        break;
                    case 3:
                        table_min = 20;
                        table_max = 30;
                        break;
                    case 4:
                        table_min = 30;
                        table_max = 999;
                        break;
                }

                int price_min = 0;
                int price_max = 0;
                switch (priceSelect){
                    case 1:
                        price_min = 1;
                        price_max = 1999;
                        break;
                    case 2:
                        price_min = 2000;
                        price_max = 3000;
                        break;
                    case 3:
                        price_min = 3000;
                        price_max = 4000;
                        break;
                    case 4:
                        price_min = 4000;
                        price_max = 99999;
                        break;
                }
                HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize, gDiscount, gGift, table_min, table_max, price_min, price_max, cb);

            }else if(tableSelect != -1 && tableSelect != 0) {//只有桌数筛选
                int min = 0;
                int max = 0;
                switch (tableSelect){
                    case 1:
                        min = 1;
                        max = 10;
                        break;
                    case 2:
                        min = 10;
                        max = 20;
                        break;
                    case 3:
                        min = 20;
                        max = 30;
                        break;
                    case 4:
                        min = 30;
                        max = 999;
                        break;
                }
               // HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else if(priceSelect != -1 && priceSelect != 0) {//只有价格筛选
                int min = 0;
                int max = 0;
                switch (priceSelect){
                    case 1:
                        min = 1;
                        max = 1999;
                        break;
                    case 2:
                        min = 2000;
                        max = 3000;
                        break;
                    case 3:
                        min = 3000;
                        max = 4000;
                        break;
                    case 4:
                        min = 4000;
                        max = 99999;
                        break;
                }
               // HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else {//只有优惠和礼包的选择
              //  HttpClient.getInstance().gFiltrateHotelsByDiscountGift(parentId, pageIndex, pageSize, gDiscount, gGift, cb);
            }
//            if(!TextUtils.isEmpty(gTable) && !TextUtils.isEmpty(gPrice)){//两个条件都有
//                String [] tables = gTable.split("-");
//                String [] prices = gPrice.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize,
//                        gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//            }else if(!TextUtils.isEmpty(gTable)){//只有桌数
//                String [] tables = gTable.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), cb);
//            }else if(!TextUtils.isEmpty(gPrice)){//只有价格
//                String [] prices = gPrice.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//            }
        }
    }

    private void getNextPages() {
        isNextPage = true;
        pageIndex++;
        if       (currentSelected == 1){
            HttpClient.getInstance().getHotels(parentId, pageIndex, pageSize, cb);
        }else if (currentSelected == 2){

            String tableOrder = "";
            if (tableAsc) {
                tableOrder = "asc";
            } else {
                tableOrder = "desc";
            }
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "table", tableOrder, cb);
        }else if (currentSelected == 3){
            String priceOrder = "";
            if (priceAsc) {
                priceOrder = "asc";
            } else {
                priceOrder = "desc";
            }
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "price", priceOrder, cb);
        }else if (currentSelected == 4){


            if((tableSelect != -1 && tableSelect != 0) && (priceSelect != -1 && priceSelect != 0)){//桌数/价格筛选
                int table_min = 0;
                int table_max = 0;
                switch (tableSelect){
                    case 1:
                        table_min = 1;
                        table_max = 10;
                        break;
                    case 2:
                        table_min = 10;
                        table_max = 20;
                        break;
                    case 3:
                        table_min = 20;
                        table_max = 30;
                        break;
                    case 4:
                        table_min = 30;
                        table_max = 999;
                        break;
                }

                int price_min = 0;
                int price_max = 0;
                switch (priceSelect){
                    case 1:
                        price_min = 1;
                        price_max = 1999;
                        break;
                    case 2:
                        price_min = 2000;
                        price_max = 3000;
                        break;
                    case 3:
                        price_min = 3000;
                        price_max = 4000;
                        break;
                    case 4:
                        price_min = 4000;
                        price_max = 99999;
                        break;
                }
                HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize, gDiscount, gGift, table_min, table_max, price_min, price_max, cb);
            }else if(tableSelect != -1 && tableSelect != 0) {//只有桌数筛选
                int min = 0;
                int max = 0;
                switch (tableSelect){
                    case 1:
                        min = 1;
                        max = 10;
                        break;
                    case 2:
                        min = 10;
                        max = 20;
                        break;
                    case 3:
                        min = 20;
                        max = 30;
                        break;
                    case 4:
                        min = 30;
                        max = 999;
                        break;
                }
               // HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else if(priceSelect != -1 && priceSelect != 0) {//只有价格筛选
                int min = 0;
                int max = 0;
                switch (priceSelect){
                    case 1:
                        min = 1;
                        max = 1999;
                        break;
                    case 2:
                        min = 2000;
                        max = 3000;
                        break;
                    case 3:
                        min = 3000;
                        max = 4000;
                        break;
                    case 4:
                        min = 4000;
                        max = 99999;
                        break;
                }
               // HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, min, max, cb);
            }else {//只有优惠和礼包的选择
               // HttpClient.getInstance().gFiltrateHotelsByDiscountGift(parentId, pageIndex, pageSize, gDiscount, gGift, cb);
            }

//            if(!TextUtils.isEmpty(gTable) && !TextUtils.isEmpty(gPrice)){//两个条件都有
//                String [] tables = gTable.split("-");
//                String [] prices = gPrice.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByAll(parentId, pageIndex, pageSize,
//                        gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//            }else if(!TextUtils.isEmpty(gTable)){//只有桌数
//                String [] tables = gTable.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByTableCount(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(tables[0]), Integer.parseInt(tables[1]), cb);
//            }else if(!TextUtils.isEmpty(gPrice)){//只有价格
//                String [] prices = gPrice.split("-");
//                HttpClient.getInstance().gFiltrateHotelsByPrice(parentId, pageIndex, pageSize, gDiscount, gGift, Integer.parseInt(prices[0]), Integer.parseInt(prices[1]), cb);
//            }
        }
    }

    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            doFiltrate(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            doFiltrate(tab);
        }
    };

    @Override
    public void initialize() {

        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(HotelsActivity.this)
                .create();

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();

        discountSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) gDiscount = "1";
                else        gDiscount = "";
            }
        });

        giftSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) gGift = "1";
                else        gGift = "";
            }
        });

        mTabAllTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
        String tempStr[] = getResources().getStringArray(R.array.filterTable);

        for (int i = 0; i < tempStr.length; i++) {
            tables.add(new FilterBean(tempStr[i], false));
        }
        tempStr = getResources().getStringArray(R.array.filterPrice);
        for (int i = 0; i < tempStr.length; i++) {
            prices.add(new FilterBean(tempStr[i], false));
        }

        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tablePrice.equals("table")) {
                    if (tableSelect != -1) {
                        tables.get(tableSelect).setChecked(false);
                    }
                    tableSelect = position;

                    gTable = tables.get(tableSelect).getName();

                    tableSelectedTxt.setText(tables.get(tableSelect).getName());
                    if(tableSelect == 0) {
                        tableSelectedTxt.setTextColor(getResources().getColor(R.color.gray_500));
                    }else {
                        tableSelectedTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
                    }
                } else {
                    if (priceSelect != -1) {
                        prices.get(priceSelect).setChecked(false);
                    }
                    priceSelect = position;
                    gPrice = prices.get(priceSelect).getName();
                    priceSelectedTxt.setText(prices.get(priceSelect).getName());

                    if(priceSelect == 0) {
                        priceSelectedTxt.setTextColor(getResources().getColor(R.color.gray_500));
                    }else {
                        priceSelectedTxt.setTextColor(getResources().getColor(R.color.filter_red_txt));
                    }
                }
                footCancelClick(null);
            }
        });

        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataList != null && dataList.size() > 0) {
                    Intent intent = new Intent(context, HotelDetailActivity.class);
                    intent.putExtra("hotelId", parentId);
                    intent.putExtra("detailId", dataList.get(position).getHotelId());
                    intent.putExtra("name", dataList.get(position).getHotelName());
                    animStart(intent);
                }
            }
        });

        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                listView.setEnabled(false);
                listView.setPullRefreshEnable(false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                listView.setEnabled(true);
                listView.setPullRefreshEnable(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        listView.setOnRefreshListener(this, 0);

//        tabLayout.setOnTabSelectedListener(listener);
//
//        //add tabs
//        for (int i = 0; i < filters.length; i++) {
//            TabLayout.Tab tab = tabLayout.newTab();
//            tab.setText(filters[i]);
//            tabLayout.addTab(tab);
//        }

        parentId = getIntent().getStringExtra("parentId");
//        subModules = (ArrayList<SubModule>) getIntent().getSerializableExtra("subModule");

//        layoutManager = new LinearLayoutManager(context);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    getNextPages();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//            }
//        });
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
//        recyclerView.setAdapter(adapter);

        titleView.setLeftBtnText("首页");

//        refreshLayout.setRefreshing(true);

        if(NetworkUtil.hasConnection(context)){
            getFirstPages();
        }else {
            try {
                dataList = db.findAll(Hotel.class);
                if(dataList == null || dataList.size() <= 0){
                    headTablayout.setVisibility(View.GONE);
                    handler.sendEmptyMessageDelayed(10, 1000);
                    handler.sendEmptyMessageDelayed(11, 4000);
                    return;
                }
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.m_title_left_btn)
    public void back(View view) {
        animFinish();
    }

    //-------------------------
    private boolean tableAsc = true;
    private boolean priceAsc = true;

    private String tableOrder = "";

    /**
     * filtrate hotel
     *
     * @param tab
     */
    private void doFiltrate(TabLayout.Tab tab) {
        String tName = tab.getText().toString();
        int isGift = 1;//礼包
        int isDiscount = 1;//优惠

        if (tName.equals(currentSelect) && (tName.equals(filters[0]) || tName.equals(filters[3]) || tName.equals(filters[4]))) {
//            S.o("选择重复");
            return;
        }
        pageIndex = 1;
        pageSize = 10;

        if ("全部".equals(tName)) {
            HttpClient.getInstance().getHotels(parentId, pageIndex, pageSize, cb);
        } else if ("桌数".equals(tName)) {
            String tableOrder = "";
            if (tableAsc) {
                tableOrder = "asc";
            } else {
                tableOrder = "desc";
            }


            if(NetworkUtil.hasConnection(context)){
                HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "table", tableOrder, cb);
            }else {
                try {
                    List<Hotel> hotelList = db.findAll(Selector.from(Hotel.class).orderBy("table", tableAsc));

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }

            tableAsc = !tableAsc;
        } else if ("价格".equals(tName)) {

            String priceOrder = "";
            if (priceAsc) {
                priceOrder = "asc";
            } else {
                priceOrder = "desc";
            }
            priceAsc = !priceAsc;
            HttpClient.getInstance().filtrateHotelsByTableOrPrice(parentId, pageIndex, pageSize, "price", priceOrder, cb);

        } else if ("礼包".equals(tName)) {
            HttpClient.getInstance().filtrateHotelsByGift(parentId, pageIndex, pageSize, isGift, cb);
        } else if ("优惠".equals(tName)) {
            HttpClient.getInstance().filtrateHotelsByDiscount(parentId, pageIndex, pageSize, isDiscount, cb);
        }

        currentSelect = tName;
    }

    /**
     * call back
     */
    private Callback<Base<ArrayList<Hotel>>> cb = new Callback<Base<ArrayList<Hotel>>>() {

        @Override
        public void success(Base<ArrayList<Hotel>> hotels, Response response) {
            listView.stopRefresh();
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            if (hotels.getCode() == 200) {
                data = hotels.getData();
                if (totalCount == -1) {
                    totalCount = hotels.getTotalCount();
                }

                if (data != null && data.size() > 0)
                    banquets = data.get(0).getBanquetHallList();
                //
                if (isNextPage) {//下一页
                    dataList.addAll(hotels.getData());
                    adapter.notifyDataSetChanged();

                    try {
                        db.saveOrUpdateAll(hotels.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }

                } else {       //refresh
//                    refreshLayout.setRefreshing(false);
                    dataList = hotels.getData();
                    try {
                        List<Hotel> hotelList = db.findAll(Hotel.class);
                        if(hotelList == null || hotelList.size() <= 0){
                            if (!MTDBUtil.todayChecked(context, HotelsActivity.TAG)) {
                                MTDBUtil.saveHotel(context, dataList);
                                Cache cache = new Cache(HotelsActivity.TAG, MTDBUtil.getToday());
                                db.saveOrUpdate(cache);
                            }

                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    adapter.notifyDataSetChanged();
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
                T.s(context, "获取数据错误, 请稍后重试...");
            }

            adapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "获取数据错误");
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        if (data != null && data.size() > 0) {
            Intent intent = new Intent(this, HotelDetailActivity.class);
            intent.putExtra("hotelId", parentId);
            intent.putExtra("detailId", data.get(position).getHotelId());
            intent.putExtra("name", data.get(position).getHotelName());
            animStart(intent);
        }
    }

    @Override
    public void onRefresh() {
        //getFirstPages();
    }

    @Override
    public void onRefresh(int id) {
        getFirstPages();
    }

    @Override
    public void onLoadMore(int id) {
        getNextPages();
    }

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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_hotel, parent, false);
                myViewHolder = new MyViewHolder(convertView);

                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            //
            final Hotel hotel = dataList.get(position);
            myViewHolder.nameTxt.setText(hotel.getHotelName() + " ");

            ImageGetter imgGetter = new ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    // TODO Auto-generated method stub
                    int id = Integer.parseInt(source);
                    Drawable d = getResources().getDrawable(id);
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    return d;
                }
            };
            String strImgDiscount = "<img src='" + R.mipmap.ic_discount + "'>";
            String strImgGift = "<img src='" + R.mipmap.ic_gift + "'>";

            if (hotel.getIsDiscount().equals("1")) {
                CharSequence discountCharSequence = Html.fromHtml(strImgDiscount, imgGetter, null);
                myViewHolder.nameTxt.append(discountCharSequence);
            }
            if (hotel.getIsGift().equals("1")) {
                CharSequence giftCharSequence = Html.fromHtml(strImgGift, imgGetter, null);
                myViewHolder.nameTxt.append(giftCharSequence);
            }

            String talbe_str_1 = "桌数：";
            String talbe_str_2 = talbe_str_1 + hotel.getBanquetHallCount();
            String talbe_str_3 = talbe_str_2 + "个宴会厅,容纳";
            String talbe_str_4 = talbe_str_3 + hotel.getCapacityPerTable();
            String talbe_str_5 = talbe_str_4 + "桌";

            SpannableStringBuilder tableStyle = new SpannableStringBuilder(talbe_str_5);

            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 3, talbe_str_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), talbe_str_3.length(), talbe_str_4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), talbe_str_2.length(), talbe_str_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), talbe_str_4.length(), talbe_str_5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.typeTxt.setText(hotel.getTypeName());

            myViewHolder.tableTxt.setText(tableStyle);

            String addrs = "位置：" + hotel.getAddress();
            SpannableStringBuilder addressStyle = new SpannableStringBuilder(addrs);
            addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), 3, addrs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            myViewHolder.addrTxt.setText(addressStyle);
            myViewHolder.priceTxt.setText("￥ " + hotel.getLowestConsumption() + " - " + hotel.getHighestConsumption());

            if (!TextUtils.isEmpty(hotel.getImageUrl())) {
                Picasso.with(context).load(hotel.getImageUrl() + "@400w_267h").error(R.mipmap.load_error).into(myViewHolder.logoImg);
            }

//            myViewHolder.item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (dataList != null && dataList.size() > 0) {
//                        if(!NetworkUtil.hasConnection(context)) {
//                            handler.sendEmptyMessageDelayed(10, 100);
//                            handler.sendEmptyMessageDelayed(11, 4000);
//                            return;
//                        }
//                        Intent intent = new Intent(context, HotelDetailActivity.class);
//                        intent.putExtra("hotelId", parentId);
//                        intent.putExtra("detailId", dataList.get(position).getHotelId());
//                        intent.putExtra("name", dataList.get(position).getHotelName());
//                        animStart(intent);
//                    }
//                }
//            });

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

            public MyViewHolder(View itemView) {
                logoImg = (ImageView) itemView.findViewById(R.id.item_hotel_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_hotel_name_txt);
                tableTxt = (TextView) itemView.findViewById(R.id.item_hotel_table_txt);
                addrTxt = (TextView) itemView.findViewById(R.id.item_hotel_addr_txt);
                priceTxt = (TextView) itemView.findViewById(R.id.item_hotel_price_txt);
                typeTxt = (TextView) itemView.findViewById(R.id.item_hotel_type_txt);
            }
        }
    }

//    public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
//        private static final int TYPE_ITEM = 0;
//        private static final int TYPE_FOOTER = 1;
//
//        private ArrayList<Hotel> datas;
//        private OnRecyclerItemClickListener onRecyclerItemClickListener;
//
//        public void setDatas(ArrayList<Hotel> datas) {
//            this.datas = datas;
//        }
//
//        public ArrayList<Hotel> getDatas() {
//            return datas;
//        }
//
//        public MyAdapter(OnRecyclerItemClickListener l) {
//            onRecyclerItemClickListener = l;
//        }
//
//        public MyAdapter(ArrayList<Hotel> d, OnRecyclerItemClickListener l) {
//            this.datas = d;
//            this.onRecyclerItemClickListener = l;
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_ITEM) {
//                View view = View.inflate(parent.getContext(), R.layout.item_hotel, null);
//                return new MyViewHolder(view, onRecyclerItemClickListener);
//            } else if (viewType == TYPE_FOOTER) {
//                View view = View.inflate(parent.getContext(), R.layout.footerview, null);
//                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                return new MyFooterHolder(view);
//            }
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (holder instanceof MyViewHolder) {
//                MyViewHolder myViewHolder = (MyViewHolder) holder;
//                // set data info
//                final Hotel hotel = datas.get(position);
//                myViewHolder.nameTxt.setText(hotel.getHotelName() + " ");
//
//                ImageGetter imgGetter = new ImageGetter() {
//                    @Override
//                    public Drawable getDrawable(String source) {
//                        // TODO Auto-generated method stub
//                        int id = Integer.parseInt(source);
//                        Drawable d = getResources().getDrawable(id);
//                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                        return d;
//                    }
//                };
//                String strImgDiscount = "<img src='" + R.mipmap.ic_discount + "'>";
//                String strImgGift = "<img src='" + R.mipmap.ic_gift + "'>";
//
//                if (hotel.getIsDiscount().equals("1")) {
//                    CharSequence discountCharSequence = Html.fromHtml(strImgDiscount, imgGetter, null);
//                    myViewHolder.nameTxt.append(discountCharSequence);
//                }
//                if (hotel.getIsGift().equals("1")) {
//                    CharSequence giftCharSequence = Html.fromHtml(strImgGift, imgGetter, null);
//                    myViewHolder.nameTxt.append(giftCharSequence);
//                }
//
//                String talbe_str_1 = "桌数：";
//                String talbe_str_2 = talbe_str_1 + hotel.getBanquetHallCount();
//                String talbe_str_3 = talbe_str_2 + "个宴会厅,容纳";
//                String talbe_str_4 = talbe_str_3 + hotel.getCapacityPerTable();
//                String talbe_str_5 = talbe_str_4 + "桌";
//
//                SpannableStringBuilder tableStyle = new SpannableStringBuilder(talbe_str_5);
//
//                tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), 3, talbe_str_2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.pink)), talbe_str_3.length(), talbe_str_4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), talbe_str_2.length(), talbe_str_3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tableStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), talbe_str_4.length(), talbe_str_5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                myViewHolder.typeTxt.setText(hotel.getTypeName());
//
//                myViewHolder.tableTxt.setText(tableStyle);
//
//                String addrs = "位置：" + hotel.getAddress();
//                SpannableStringBuilder addressStyle = new SpannableStringBuilder(addrs);
//                addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_700)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                addressStyle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.subtitle_gray_text_color)), 3, addrs.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                myViewHolder.addrTxt.setText(addressStyle);
//                myViewHolder.priceTxt.setText("￥ " + hotel.getLowestConsumption() + " - " + hotel.getHighestConsumption());
//
//                if (!TextUtils.isEmpty(hotel.getImageUrl())) {
//                    Picasso.with(context).load(hotel.getImageUrl() + "@400w_267h").error(R.mipmap.load_error).into(myViewHolder.logoImg);
//                }
//            } else if (holder instanceof MyFooterHolder) {
//                MyFooterHolder footerHolder = (MyFooterHolder) holder;
//                if (datas.size() < totalCount) {
//                    footerHolder.progressBar.setVisibility(View.VISIBLE);
//                    footerHolder.txt.setText("正在加载...");
//                } else {
//                    footerHolder.progressBar.setVisibility(View.GONE);
//                    footerHolder.txt.setText("无更多数据");
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return datas == null ? 0 : datas.size();
//        }
//
//        public class MyFooterHolder extends ViewHolder {
//            ContentLoadingProgressBar progressBar;
//            TextView txt;
//
//            public MyFooterHolder(View itemView) {
//                super(itemView);
//                progressBar = (ContentLoadingProgressBar) itemView.findViewById(R.id.footer_progressbar);
//                txt = (TextView) itemView.findViewById(R.id.footer_txt);
//            }
//        }
//
//        public class MyViewHolder extends ViewHolder implements View.OnClickListener {
//
//            public ImageView logoImg;
//            public TextView nameTxt;
//            public TextView tableTxt;
//            public TextView addrTxt;
//            public TextView priceTxt;
//            public TextView typeTxt;
//
//            OnRecyclerItemClickListener onRecyclerItemClickListener;
//
//            public MyViewHolder(View itemView, OnRecyclerItemClickListener l) {
//                super(itemView);
//                logoImg = (ImageView) itemView.findViewById(R.id.item_hotel_img);
//                nameTxt = (TextView) itemView.findViewById(R.id.item_hotel_name_txt);
//                tableTxt = (TextView) itemView.findViewById(R.id.item_hotel_table_txt);
//                addrTxt = (TextView) itemView.findViewById(R.id.item_hotel_addr_txt);
//                priceTxt = (TextView) itemView.findViewById(R.id.item_hotel_price_txt);
//                typeTxt = (TextView) itemView.findViewById(R.id.item_hotel_type_txt);
//                this.onRecyclerItemClickListener = l;
//                itemView.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View view) {
//                onRecyclerItemClickListener.onRecyclerItemClick(view, getLayoutPosition());
//            }
//        }
//    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    @Override
    public boolean onKeydown() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawers();
            return false;
        }
        animFinish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.activity_empty);
    }

    @OnClick(R.id.filter_head_layout)
    public void headClick(View view){

    }

    @OnClick(R.id.filter_foot_layout)
    public void footClick(View view){

    }
}
