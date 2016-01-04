package com.chinajsbn.venus.ui.supplies.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Brands;
import com.chinajsbn.venus.net.bean.CarType;
import com.chinajsbn.venus.net.bean.Supplie;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.supplies.SupplieDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.MTDBUtil;
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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/12/10.
 */
@FragmentFeature(layout = R.layout.fragment_supplies)
public class SuppliesFragment extends BaseFragment implements OnRecyclerItemClickListener, SwipeRefreshLayout.OnRefreshListener,OnClickListener {

    ///used by cache
    public static final String TAG = "SuppliesFragment_tag_supplies";//国际婚纱
    public static final String TAG_TYPE = "SuppliesFragment_tag_supplies_type";//国际婚纱
    private DbUtils db;
    private  List<CarType> localCarTypeList;

    private final String MODULE_ID = "42322";

    //--------------------------data-------------------------
    private List<Supplie> dataList;
    private ArrayList<CarType> typeList = new ArrayList<>();

    //--------------------------widget-------------------------
    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    private MyAdapter adapter;
    private GridLayoutManager layoutManager;
    private boolean canRefresh = false;

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    //----------------------page-----------------------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据

    private int previousPosition = -1;
    private boolean loading = false;//标识是否正在读取， 正在读取的时候不能切换

    @ViewInject(R.id.refreshLayout)
    private SwipeRefreshLayout refreshLayout;

    private Snackbar snackbar;

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
                .setOnClickListener(SuppliesFragment.this)
                .create();

        //
        adapter = new MyAdapter(this);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        refreshLayout.setProgressBackgroundColorSchemeResource(R.color.gray_400);
        refreshLayout.setColorSchemeColors(R.color.pink, R.color.green, R.color.orange, R.color.blue);
//        refreshLayout.setColorSchemeResources(R.color.pink, R.color.green);
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && canRefresh) {
                    if(NetworkUtil.hasConnection(getActivity())){
                        if (snackbar == null) snackbar = Snackbar.make(recyclerView, "加载数据 ...", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                        isNextPage = true;
                        pageIndex++;
                        if (previousPosition == 0) {
                            HttpClient.getInstance().supplieList(MODULE_ID, pageIndex, pageSize, callback);
                        } else {
                            HttpClient.getInstance().supplieSearch(MODULE_ID, typeList.get(previousPosition).getId(), pageIndex, pageSize, callback);
                        }
                    }else{
                        S.o(":::用品无网络，加载下一页");
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        //
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (previousPosition != tab.getPosition()) {
                    previousPosition = tab.getPosition();
                    loading = true;
                    isNextPage = false;

                    if (NetworkUtil.hasConnection(getActivity())) {
                        if (tab.getPosition() == 0) {
                            HttpClient.getInstance().supplieList(MODULE_ID, pageIndex, pageSize, callback);
                        } else {
                            HttpClient.getInstance().supplieSearch(MODULE_ID, typeList.get(tab.getPosition()).getId(), pageIndex, pageSize, callback);
                        }
                    } else {// not connect
                        refreshLayout.setEnabled(false);
                        try {
                            if(previousPosition == 0){
                                dataList = db.findAll(Supplie.class);
                            }else{
                                dataList = db.findAll(Selector.from(Supplie.class).where("weddingSuppliesTypeId", "=", localCarTypeList.get(previousPosition -1).getId()));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
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

        if (NetworkUtil.hasConnection(getActivity())) {
            //获取类型
            HttpClient.getInstance().supplieTypes(suppliesTypeCallback);//
        } else { //not connect
            refreshLayout.setEnabled(false);
            try {
                localCarTypeList = db.findAll(Selector.from(CarType.class).where("tag", "=", TAG_TYPE));
                CarType type = new CarType();
                type.setName("全部");
                type.setId(0);
                typeList.add(type);
                typeList.addAll(localCarTypeList);

                for (int i = 0; i < typeList.size(); i++) {
                    TabLayout.Tab tab = tabLayout.newTab();
                    tab.setText(typeList.get(i).getName());
                    tabLayout.addTab(tab);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用品类型回调
     */
    private Callback<Base<ArrayList<CarType>>> suppliesTypeCallback = new Callback<Base<ArrayList<CarType>>>() {
        @Override
        public void success(Base<ArrayList<CarType>> resp, Response response) {
            if (resp.getCode() == 200) {
                CarType type = new CarType();
                type.setName("全部");
                type.setId(0);

                typeList.add(type);
                typeList.addAll(resp.getData());

                if (!MTDBUtil.todayChecked(getActivity(), TAG_TYPE)) {
                    S.o(">>>用品类型 TAG=" + TAG_TYPE + "缓存");
                    try {
                        db.delete(CarType.class, WhereBuilder.b("tag", "=", TAG_TYPE));
                        for (CarType carType : resp.getData()) {
                            carType.setTag(TAG_TYPE);
                        }
                        db.saveAll(resp.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    S.o(">>>用品类型 TAG=" + TAG_TYPE + "已经缓存");
                }

                for (int i = 0; i < typeList.size(); i++) {
                    TabLayout.Tab tab = tabLayout.newTab();
                    tab.setText(typeList.get(i).getName());
                    tabLayout.addTab(tab);
                }

            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        if(NetworkUtil.hasConnection(getActivity())){
            Intent intent = new Intent(getActivity(), SupplieDetailActivity.class);
            intent.putExtra("moduleId", MODULE_ID);
            intent.putExtra("detailId", dataList.get(position).getWeddingSuppliesId());
            intent.putExtra("oldPrice", dataList.get(position).getMarketPrice());
            intent.putExtra("newPrice", dataList.get(position).getSellingPrice());
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
        isNextPage = false;
        pageIndex = 1;
        if (previousPosition == 0) {
            HttpClient.getInstance().supplieList(MODULE_ID, pageIndex, pageSize, callback);
        } else {
            HttpClient.getInstance().supplieSearch(MODULE_ID, typeList.get(previousPosition).getId(), pageIndex, pageSize, callback);
        }
    }

    Callback<Base<ArrayList<Supplie>>> callback = new Callback<Base<ArrayList<Supplie>>>() {
        @Override
        public void success(Base<ArrayList<Supplie>> resp, Response response) {
            if (resp.getCode() == 200) {
                refreshLayout.setRefreshing(false);
                if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
                if (resp.getData() != null && resp.getData().size() >= pageSize) {
                    canRefresh = true;
                } else {
                    canRefresh = false;
                }
                if (isNextPage) {
                    dataList.addAll(resp.getData());
                    if (previousPosition == 0) {
                        try {
                            db.saveAll(resp.getData());
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    dataList = resp.getData();
                    if (previousPosition == 0) {
                        try {
                            db.deleteAll(Supplie.class);
                            db.saveAll(dataList);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getContext(), "获取数据错误");
        }
    };

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
            Supplie supplie = dataList.get(position);

            if (!TextUtils.isEmpty(supplie.getCoverUrl()))
                Picasso.with(getActivity()).load(supplie.getCoverUrl() + "@" + (DimenUtil.screenWidth / 2) + "w_" + (DimenUtil.screenWidth / 2) + "h_60Q").resize(DimenUtil.screenWidth / 2, DimenUtil.screenWidth / 2).placeholder(R.drawable.loading).into(holder.coverImg);
//
            holder.nameTxt.setText(supplie.getTitle());
            if (supplie.getMarketPrice().toString().equals(supplie.getSellingPrice().toString())) {
                holder.oldPriceTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.oldPriceTxt.setVisibility(View.VISIBLE);
            }
            holder.oldPriceTxt.setText("￥" + supplie.getMarketPrice());
            holder.newPriceTxt.setText("￥" + supplie.getSellingPrice());
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
                oldPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
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
}
