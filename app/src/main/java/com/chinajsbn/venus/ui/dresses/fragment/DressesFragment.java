package com.chinajsbn.venus.ui.dresses.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Brands;
import com.chinajsbn.venus.net.bean.BrandsResp;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.dresses.DressDetailActivity;
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
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 婚纱
 * Created by 13510 on 2015/11/30.
 */
@FragmentFeature(layout = R.layout.fragment_dresses)
public class DressesFragment extends BaseFragment implements  MasterListView.OnRefreshListener , OnRecyclerItemClickListener, OnClickListener {

    ///used by cache
    public static final String TAG_GJHS = "dressesFragment_tag_gjhs";//国际婚纱
    public static final String TAG_MXLF = "dressesFragment_tag_xnlf";//新娘礼服
    public static final String TAG_NSLF = "dressesFragment_tag_nslf";//男士礼服
    private DbUtils db;

    private List<Brands> dataList;
    private int dressType = 1; //婚纱展示类型 1：国际婚纱 2：明星礼服 3:男士礼服

    private final String[] filters = new String[]{"国际婚纱", "新娘礼服", "男士礼服"};

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.listView)
    private MyListView listView;
    private MyAdapter adapter;

//    @OnClick(R.id.item_dresses_tab_gjhs)
//    public void gjhsClick(View view){
//        dressType = 1;
//        listView.setPullRefreshEnable(false);
//        HttpClient.getInstance().brandsList(dressType, cb);
//    }
//
//    @OnClick(R.id.item_dresses_tab_mxlf)
//    public void mxlfClick(View view){
//        dressType = 2;
//        listView.setPullRefreshEnable(false);
//        HttpClient.getInstance().brandsList(dressType, cb);
//    }

    @Override
    public void initialize() {

        ///无网络提示///
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        networkDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(DressesFragment.this)
                .create();

        //
        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        //
        adapter= new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(DressesFragment.this, 0);
        listView.setPullLoadEnable(false);

        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                dressType = tab.getPosition() + 1;
                if(NetworkUtil.hasConnection(getActivity())){
                    //
                    HttpClient.getInstance().brandsList(dressType, cb);
                }else{
                    try {
                        dataList = db.findAll(Selector.from(Brands.class).where("tab", "=", dressType));
                        if(dataList != null && dataList.size() > 0){
                            adapter.notifyDataSetChanged();
                        }else{
                            handler.sendEmptyMessageDelayed(10, 100);
                            handler.sendEmptyMessageDelayed(11, 4000);
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
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

        //
        if(NetworkUtil.hasConnection(getActivity())){
            HttpClient.getInstance().brandsList(dressType, cb);
        }else{
            try {
                dataList = db.findAll(Selector.from(Brands.class).where("tab", "=", dressType));
                if(dataList != null && dataList.size() > 0){
                    adapter.notifyDataSetChanged();
                }else{
                    handler.sendEmptyMessageDelayed(10, 100);
                    handler.sendEmptyMessageDelayed(11, 4000);
                }
            } catch (DbException e) {
                e.printStackTrace();
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

    /**缓存数据**/
    private void cacheData(int type, List<Brands> datas){
        switch (type){
            case 1:
                if (!MTDBUtil.todayChecked(getActivity(), TAG_GJHS)) {
                    S.o(">>>国际婚礼 TAG=" + TAG_GJHS + "缓存");
                    for (Brands brands : datas){
                        brands.setTab(1);
                    }
                    try {
                        db.delete(Brands.class, WhereBuilder.b("tab", "=", 1));
                        db.saveAll(datas);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{
                    S.o(">>>国际婚礼 TAG=" + TAG_GJHS + "已经缓存过了");
                }
                break;
            case 2:
                if (!MTDBUtil.todayChecked(getActivity(), TAG_MXLF)) {
                    S.o(">>>明星礼服 TAG=" + TAG_MXLF + "缓存");
                    for (Brands brands : datas){
                        brands.setTab(2);
                    }
                    try {
                        db.delete(Brands.class, WhereBuilder.b("tab", "=", 2));
                        db.saveAll(datas);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{
                    S.o(">>>明星礼服 TAG=" + TAG_MXLF + "已经缓存过了");
                }
                break;
            case 3:
                if (!MTDBUtil.todayChecked(getActivity(), TAG_NSLF)) {
                    S.o(">>>男士礼服 TAG=" + TAG_NSLF + "缓存");
                    for (Brands brands : datas){
                        brands.setTab(3);
                    }
                    try {
                        db.delete(Brands.class, WhereBuilder.b("tab", "=", 3));
                        db.saveAll(datas);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{
                    S.o(">>>男士礼服 TAG=" + TAG_NSLF + "已经缓存过了");
                }
                break;
        }
    }

    private Callback<BrandsResp> cb = new Callback<BrandsResp>() {
        @Override
        public void success(BrandsResp brandsResp, Response response) {
            listView.stopRefresh();
            if(brandsResp.getCode() == 200){
                dataList = brandsResp.getData();
                cacheData(dressType, dataList);
                adapter.notifyDataSetChanged();
            }else{
                T.s(getActivity(), "获取数据错误");
            }
            listView.setPullRefreshEnable(true);
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
            listView.setPullRefreshEnable(true);
            T.s(getActivity(), "服务器错误");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

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
            final Brands brands = dataList.get(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_dresses, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            //
            holder.nameTxt.setText(brands.getWeddingDressBrandName());
            if(!TextUtils.isEmpty(brands.getImageUrl())) {
                Picasso.with(getActivity()).load(brands.getImageUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.contentImg);
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NetworkUtil.hasConnection(getActivity())){
                        Intent intent = new Intent(getActivity(), DressDetailActivity.class);
                        intent.putExtra("brandId", brands.getWeddingDressBrandId());
                        animStart(intent);
                    }else{
                        handler.sendEmptyMessageDelayed(10, 100);
                        handler.sendEmptyMessageDelayed(11, 4000);
                    }
                }
            });

            //
            return convertView;
        }

        class ViewHolder {
            TextView nameTxt;
            ImageView contentImg;
            LinearLayout layout;
            public ViewHolder(View view){
                nameTxt     = (TextView) view.findViewById(R.id.item_dresses_name_txt);
                contentImg  = (ImageView) view.findViewById(R.id.item_dresses_content_img);
                layout      = (LinearLayout) view.findViewById(R.id.item_dresses_layout);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void onRefresh(int id) {
        listView.startRefresh();
        listView.saveRefreshStrTime();
        listView.setPullRefreshEnable(false);
        HttpClient.getInstance().brandsList(dressType, cb);
    }

    @Override
    public void onLoadMore(int id) {

    }
}
