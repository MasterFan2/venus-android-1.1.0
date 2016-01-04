package com.chinajsbn.venus.ui.car.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Car;
import com.chinajsbn.venus.net.bean.CarType;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.car.CarDetailActivity;
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
import com.tool.widget.MyGridView;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/12/10.
 */
@FragmentFeature(layout = R.layout.fragment_car)
public class CarFragment extends BaseFragment implements OnRecyclerItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnClickListener {

    ///used by cache
    public static final String TAG_BRAND = "CarFragment_tag_brand";//品牌
    public static final String TAG_TYPE  = "CarFragment_tag_type"; //类型
    private DbUtils db;
    private int tab_selected = 0;//tab的选择项， 只有是选择的全部的时候才缓存数据

    private final String MODULE_ID = "42323";

    //--------------------------data-------------------------
    private List<Car> dataList; //主数据
    private List<CarType> brandList = new ArrayList<>();
    private List<CarType> typeList = new ArrayList<>();
    private List<CarType> priceList = new ArrayList<>();

    private String[] prices = {"1000以下", "1000-2000", "2000-3000", "3000以上"};

    //--------------------------widget-------------------------
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private GridLayoutManager layoutManager;
    private boolean canRefresh = false;

    @ViewInject(R.id.refreshLayout)
    private SwipeRefreshLayout refreshLayout;

    private Snackbar snackbar;

    @ViewInject(R.id.head_tab_layout)
    private LinearLayout headTablayout;

    @ViewInject(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @ViewInject(R.id.type_gridView)
    private MyGridView typeGrid;

    @ViewInject(R.id.brand_gridView)
    private MyGridView brandGrid;

    @ViewInject(R.id.price_gridView)
    private MyGridView priceGrid;

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    snackbar.dismiss();
                    break;
            }
        }
    };

    //------------tab-------------
    @ViewInject(R.id.m_tab_all_txt)
    private TextView allTxt;
    @OnClick(R.id.m_tab_all_txt)
    public void allClick(View view) {

        isAll    = true;
        isNature = false;
        isFilter = false;
        carNature = -1;//没有车队筛选条件

        allTxt.setTextColor(getResources().getColor(R.color.pink));
        priceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        filterTxt.setTextColor(getResources().getColor(R.color.gray_600));

        tab_selected = 0;
        getFirstData();
    }

    @ViewInject(R.id.m_tab_price_txt)
    private TextView priceTxt;
    @OnClick(R.id.m_tab_price_txt)
    public void priceClick(View view) {//车队，  全部搜索

        tab_selected = 1;
        allTxt.setTextColor(getResources().getColor(R.color.gray_600));
        priceTxt.setTextColor(getResources().getColor(R.color.pink));
//        filterTxt.setTextColor(getResources().getColor(R.color.gray_600));

        if(carNature == -1 || carNature == 1){
            carNature = 2;
            priceTxt.setText("车队");
            isNature = true;
        } else if(carNature == 2){
            priceTxt.setText("单车");
            isNature = true;
            carNature = 1;
        }

        getFirstData();
    }

    @ViewInject(R.id.m_tab_filter_txt)
    private TextView filterTxt;
    @OnClick(R.id.m_tab_filter_txt)
    public void filterClick(View view) {
        tab_selected = 2;
        drawerLayout.openDrawer(GravityCompat.END);
        allTxt.setTextColor(getResources().getColor(R.color.gray_600));
//        priceTxt.setTextColor(getResources().getColor(R.color.gray_600));
        filterTxt.setTextColor(getResources().getColor(R.color.pink));
    }

    //----------------------page-----------------------------
    private int pageIndex = 1;
    private int pageSize = 50;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private boolean isFilter   = false;//标识是否有筛选条件,
    private boolean isNature   = false;//是否是车队筛选
    private boolean isAll      = true; //是否是全部，true:所有数据，

    //------------------------search-------------------------
    private String price_start = "0";
    private String price_end   = "0";
    private int brandId = 0;
    private int typeId  = 0;

    private int carNature = -1;//2：车队        /1：全部


    @Override
    public void initialize() {

        //
        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        ///无网络提示///
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        networkDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(CarFragment.this)
                .create();

        adapter = new MyAdapter(this);
        layoutManager = new GridLayoutManager(getActivity(), 2);

        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        allTxt.setTextColor(getResources().getColor(R.color.pink));

        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_300);
        refreshLayout.setColorSchemeResources(R.color.pink, R.color.green);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //加载更多
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && canRefresh) {
                    if(NetworkUtil.hasConnection(getActivity())){
                        if (snackbar == null) snackbar = Snackbar.make(recyclerView, "加载数据 ...", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        getNextData();
                    }else{
                        S.o(":::无网络连接");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        priceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                priceGridViewAdapter.setChecked(position);
            }
        });
        typeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeGridViewAdapter.setChecked(position);
            }
        });
        brandGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brandGridViewAdapter.setChecked(position);
            }
        });


        for (String s : prices) {
            CarType type = new CarType();
            type.setName(s);
            priceList.add(type);
        }
        priceGridViewAdapter = new GridViewAdapter(priceList);

        if(NetworkUtil.hasConnection(getActivity())){
            //获取所有车辆列表
            HttpClient.getInstance().carList(MODULE_ID, pageIndex, pageSize, cb);

            //获取品牌列表
            HttpClient.getInstance().carBrandList(brandCallback);

            //获取车类型列表
            HttpClient.getInstance().carTypeList(typeCallback);
        }else{

            refreshLayout.setEnabled(false);//禁用刷新
            try {

                dataList = db.findAll(Car.class);
                if(dataList == null || dataList.size() <= 0){
                    T.s(getActivity(), "请连网");
                }
                adapter.notifyDataSetChanged();

                typeList = db.findAll(Selector.from(CarType.class).where("tag", "=", TAG_TYPE));
                typeGridViewAdapter.setDataList(typeList);

                brandList = db.findAll(Selector.from(CarType.class).where("tag", "=", TAG_BRAND));
                brandGridViewAdapter.setDataList(brandList);

                if(typeList == null || typeList.size() <= 0 || brandList == null || brandList.size() <= 0){
                    T.s(getActivity(), "请连网");
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        priceGrid.setAdapter(priceGridViewAdapter);
        typeGrid.setAdapter(typeGridViewAdapter);
        brandGrid.setAdapter(brandGridViewAdapter);
    }

    /**
     * 获取第一页数据
     */
    private void getFirstData(){
        isNextPage = false;
        pageIndex = 1;

        if(NetworkUtil.hasConnection(getActivity())){
            refreshLayout.setRefreshing(true);
            if(isAll && !isFilter && !isNature){
                HttpClient.getInstance().carList(MODULE_ID, pageIndex, pageSize, cb);
            }else{
                if(isNature){ //车队
                    HttpClient.getInstance().carSearchParamAll(MODULE_ID, carNature, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
                }else{
                    HttpClient.getInstance().carSearch(MODULE_ID, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
                }
            }
        }else{// not connect
            if(isAll && !isFilter && !isNature){
                try {
                    dataList = db.findAll(Car.class);
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }else{
                if(isNature){ //车队
                    try {
                        Selector selector = Selector.from(Car.class).where("carNature", "=", carNature);
                        if(typeId != 0){
                            selector.and("carModelsId", "=", typeId);
                        }
                        if(brandId != 0){
                            selector.and("carBrandId", "=", brandId);
                        }

                        if(!price_end.equals("0")){
                            selector.and("rentalPrice", "between", new String[]{price_start, price_end});
                        }

                        dataList = db.findAll(selector);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        Selector selector = Selector.from(Car.class).where("title", "!=", "null");
                        if(typeId != 0){
                            selector.and("carModelsId", "=", typeId);
                        }
                        if(brandId != 0){
                            selector.and("carBrandId", "=", brandId);
                        }
                        if(!price_end.equals("0")){
                            selector.and("rentalPrice", "between", new String[]{price_start, price_end});
                        }

                        dataList = db.findAll(selector);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取下一页数据
     */
    private void getNextData(){
        pageIndex ++ ;
        isNextPage = true;

        if(NetworkUtil.hasConnection(getActivity())){
            if(isAll){
                HttpClient.getInstance().carList(MODULE_ID, pageIndex, pageSize, cb);
            }else{
                if(isNature){ //车队
                    HttpClient.getInstance().carSearchParamAll(MODULE_ID, carNature, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
                }else{
                    HttpClient.getInstance().carSearch(MODULE_ID, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
                }
            }
        }else{
            S.o("加载下一页，无网络");
        }
    }

    /**
     * 通过筛选条件搜索数据
     */
    private void getDataByFilter(){
        pageIndex = 1;
        isFilter = true;
        isNextPage = false;

        if(NetworkUtil.hasConnection(getActivity())){
            refreshLayout.setRefreshing(true);
            if(isNature) {//通过车队筛选
                HttpClient.getInstance().carSearchParamAll(MODULE_ID, carNature, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
            }else{
                HttpClient.getInstance().carSearch(MODULE_ID, typeId + "", brandId + "", price_start, price_end, pageIndex, pageSize, searchCallback);
            }
        }else{//not connect
            if(isNature){ //车队
                try {
                    Selector selector = Selector.from(Car.class).where("carNature", "=", carNature);
                    if(typeId != 0){
                        selector.and("carModelsId", "=", typeId);
                    }
                    if(brandId != 0){
                        selector.and("carBrandId", "=", brandId);
                    }
                    if(!price_end.equals("0")){
                        selector.and("rentalPrice", "between", new String[]{price_start, price_end});
                    }

                    dataList = db.findAll(selector);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Selector selector = Selector.from(Car.class).where("title", "!=", "null");
                    if(typeId != 0){
                        selector.and("carModelsId", "=", typeId);
                    }
                    if(brandId != 0){
                        selector.and("carBrandId", "=", brandId);
                    }

                    if(!price_end.equals("0")){
                        selector.and("rentalPrice", "between", new String[]{price_start, price_end});
                    }
                    dataList = db.findAll(selector);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private Callback<Base<ArrayList<CarType>>> brandCallback = new Callback<Base<ArrayList<CarType>>>() {
        @Override
        public void success(Base<ArrayList<CarType>> resp, Response response) {
            if (resp.getCode() == 200) {
                brandList = resp.getData();
                if (brandList != null && brandList.size() > 0) {
                    brandGridViewAdapter.setDataList(brandList);
                    try {
                        db.delete(CarType.class, WhereBuilder.b("tag", "=", TAG_BRAND));
                        for (CarType type : brandList){
                            type.setTag(TAG_BRAND);
                        }
                        db.saveAll(brandList);
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

    private Callback<Base<ArrayList<CarType>>> typeCallback = new Callback<Base<ArrayList<CarType>>>() {
        @Override
        public void success(Base<ArrayList<CarType>> resp, Response response) {
            if (resp.getCode() == 200) {
                typeList = resp.getData();
                if (typeList != null && typeList.size() > 0) {
                    typeGridViewAdapter.setDataList(typeList);
                    try {
                        db.delete(CarType.class, WhereBuilder.b("tag", "=", TAG_TYPE));
                        for (CarType type : typeList){
                            type.setTag(TAG_TYPE);
                        }
                        db.saveAll(typeList);
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


    @OnClick(R.id.filter_root)
    public void rootClick(View view) {
        //防止点击菜单后面的内容
    }

    @OnClick(R.id.filter_cancel_txt)
    public void cancelClick(View v) {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    @OnClick(R.id.car_filter_reset_btn)
    public void resetClick(View view) {
        //取消选择中
        priceGridViewAdapter.setChecked(-1);
        typeGridViewAdapter.setChecked(-1);
        brandGridViewAdapter.setChecked(-1);
    }

    /**
     * 搜索 XXA
     * @param view
     */
    @OnClick(R.id.filter_confirm_txt)
    public void confirmClick(View view) {

        tab_selected = 2;

        //搜索
        CarType price = priceGridViewAdapter.getCheckedItem();
        CarType brand = brandGridViewAdapter.getCheckedItem();
        CarType type = typeGridViewAdapter.getCheckedItem();

        //
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        //没有选择条件
        if (price == null && brand == null && type == null) {
            price_start = "0";
            price_end   = "0";
            typeId   = 0;
            brandId  = 0;
            isFilter = false;
            getFirstData();
            return;
        }

        isAll = false;
        if(price != null){//price
            if(price.getName().contains("以上")){
                price_start = "3001";
                price_end   = "1000000";
            }else if(price.getName().contains("以下")){
                price_start = "0";
                price_end   = "999";
            }else{
                price_start = price.getName().split("-")[0];
                price_end = price.getName().split("-")[1];
            }
        }else{
            price_start = "0";
            price_end   = "0";
        }

        if(brand != null){//brand
            brandId = brand.getId();
        }else{
            brandId = 0;
        }

        if(type != null){//type
            typeId = type.getId();
        }else{
            typeId = 0;
        }

        getDataByFilter();
    }

    /**
     * 查询回调
     */
    private Callback<Base<ArrayList<Car>>> searchCallback = new Callback<Base<ArrayList<Car>>>() {
        @Override
        public void success(Base<ArrayList<Car>> resp, Response response) {
            if (snackbar != null && snackbar.isShown()) {
                handler2.sendEmptyMessageDelayed(1, 800);
            }
            if(resp.getCode() == 200){
                if (resp.getData() != null && resp.getData().size() >= pageSize) {
                    canRefresh = true;
                } else {
                    canRefresh = false;
                }

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                if (isNextPage) {
                    dataList.addAll(resp.getData());
                } else {
                    dataList = resp.getData();
                }
            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void failure(RetrofitError error) {}
    };


    /**
     * 默认回调
     */
    private Callback<Base<ArrayList<Car>>> cb = new Callback<Base<ArrayList<Car>>>() {
        @Override
        public void success(Base<ArrayList<Car>> resp, Response response) {
            if(refreshLayout.isRefreshing())
                refreshLayout.setRefreshing(false);
            if (snackbar != null && snackbar.isShown()) {
                handler.sendEmptyMessageDelayed(1, 800);
            }
            if (resp.getData() != null && resp.getData().size() >= pageSize) {
                canRefresh = true;
            } else {
                canRefresh = false;
            }
            if (isNextPage) {//next page
                dataList.addAll(resp.getData());
                if(tab_selected == 0) {
                    try {
                        db.saveAll(resp.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            } else {//first page
                dataList = resp.getData();
                if(tab_selected == 0) {
                    try {
                        db.deleteAll(Car.class);
                        db.saveAll(dataList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        if(NetworkUtil.hasConnection(getActivity())){
            Intent intent = new Intent(getActivity(), CarDetailActivity.class);
            intent.putExtra("moduleId", MODULE_ID);
            intent.putExtra("detailId", dataList.get(position).getWeddingCarRentalId());
            animStart(intent);
        }else{
            handler.sendEmptyMessageDelayed(10, 100);
            handler.sendEmptyMessageDelayed(11, 4000);
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
    public void onRefresh() {
        pageIndex = 1;
        getFirstData();
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        dialog.dismiss();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.FilmHolder> {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener l) {
            this.onRecyclerItemClickListener = l;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FilmHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_car, null), onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            Car car = dataList.get(position);

            if (!TextUtils.isEmpty(car.getCoverUrl()))
                Picasso.with(getActivity()).load(car.getCoverUrl() + "@"+ (DimenUtil.screenWidth / 2)+"w_"+ (DimenUtil.screenWidth/ 3) +"h_60Q").resize(DimenUtil.screenWidth / 2, DimenUtil.screenWidth / 3).placeholder(R.drawable.loading).into(holder.coverImg);
//
            holder.nameTxt.setText(Html.fromHtml(car.getTitle()));
            if(car.getMarketRentalPrice() == car.getRentalPrice()){
                holder.oldPriceTxt.setVisibility(View.INVISIBLE);
            }else{
                holder.oldPriceTxt.setVisibility(View.VISIBLE);
            }
            if(car.getRentalPrice() == 0){
                holder.newPriceTxt.setText("面议");
            }else{
                holder.newPriceTxt.setText("￥" + car.getRentalPrice());
            }

            holder.oldPriceTxt.setText("￥" + car.getMarketRentalPrice());
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }


        class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView coverImg;
            TextView nameTxt;
            TextView oldPriceTxt;
            TextView newPriceTxt;

            public FilmHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                coverImg = (ImageView) itemView.findViewById(R.id.item_car_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_car_name_txt);
                oldPriceTxt = (TextView) itemView.findViewById(R.id.item_car_oldPrice_txt);

                //中间删除线
                oldPriceTxt.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
                oldPriceTxt.getPaint().setAntiAlias(true);// 抗锯齿

                newPriceTxt = (TextView) itemView.findViewById(R.id.item_car_newPrice_txt);
                onRecyclerItemClickListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    //--------------------------------
    private GridViewAdapter priceGridViewAdapter = new GridViewAdapter();//价钱
    private GridViewAdapter typeGridViewAdapter = new GridViewAdapter(); //车型
    private GridViewAdapter brandGridViewAdapter = new GridViewAdapter();//品牌

    class GridViewAdapter extends BaseAdapter {
        private int previousPosition = -1;//上一个选择的
        private List<CarType> dataList;

        public GridViewAdapter() {
        }

        public GridViewAdapter(List<CarType> d) {
            this.dataList = d;
        }

        public void setDataList(List<CarType> dataList) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        public CarType getCheckedItem() {
            if (previousPosition != -1)
                return dataList.get(previousPosition);
            return null;
        }

        /**
         * 设置选中项
         *
         * @param position
         */
        public void setChecked(int position) {
            if (position == -1) {
                if (previousPosition != -1) {
                    dataList.get(previousPosition).setChecked(false);
                    previousPosition = -1;
                }
                notifyDataSetChanged();
                return;
            }
            if (previousPosition == -1) {//first
                previousPosition = position;
                dataList.get(position).setChecked(true);
            } else if (previousPosition == position) {
                dataList.get(position).setChecked(false);
                previousPosition = -1;
            } else {
                dataList.get(position).setChecked(true);
                dataList.get(previousPosition).setChecked(false);
                previousPosition = position;
            }
            notifyDataSetChanged();
        }

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            CarType type = dataList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.car_single_textview, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt.setText(type.getName());
            if (type.isChecked()) {
                holder.txt.setBackgroundResource(R.drawable.filter_checked_bg);
            } else {
                holder.txt.setBackgroundResource(R.drawable.filter_none_bg);
            }
            return convertView;
        }

        class ViewHolder {
            TextView txt;

            public ViewHolder(View v) {
                txt = (TextView) v.findViewById(R.id.txt);
            }
        }
    }
}
