package com.chinajsbn.venus.ui.other;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.text.Html;
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
import com.chinajsbn.venus.net.bean.Other;
import com.chinajsbn.venus.net.bean.OtherResp;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_other2)
public class OtherActivity extends MBaseFragmentActivity implements MasterListView.OnRefreshListener {

    ///used by cache
    public static final String TAG_HZJQ = "Other_tag_hzjq";//婚照技巧
    private DbUtils db;

    @ViewInject(R.id.tabLayout)
    private TabLayout tabLayout;

    @ViewInject(R.id.listView)
    private MyListView listView;
    private MyAdapter  adapter;
    private List<Other> dataList;

    private final String[] filters = new String[]{"婚照技巧", "婚宴知识", "婚礼学堂", "礼服知识", "表演技巧", "用品贴士", "租车经验"};
    private int moduleTypeId = 1;
    private int pageIndex = 1;
    private int pageSize = 100;

    @Override
    public void initialize() {

        //
        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        moduleTypeId = getIntent().getIntExtra("type", 0);

        adapter = new MyAdapter();
        listView.setOnRefreshListener(this, 0);
        listView.setAdapter(adapter);
        listView.setPullLoadEnable(false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, OtherDetailActivity.class);
                intent.putExtra("data", dataList.get(position - 1));
                animStart(intent);
            }
        });

        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                moduleTypeId = tab.getPosition();
                if(moduleTypeId == 5 || moduleTypeId == 6){
                    moduleTypeId += 1;
                }
                moduleTypeId += 1;
                if(NetworkUtil.hasConnection(context)){
                    HttpClient.getInstance().other(moduleTypeId, pageIndex, pageSize, cb);
                }else{
                    try {
                        dataList = db.findAll(Selector.from(Other.class).where("moduleTypeId", "=", moduleTypeId));
                        adapter.notifyDataSetChanged();
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

        if(moduleTypeId != 0){//设置默认选中的tab
            if(moduleTypeId == 7 || moduleTypeId == 6){
                tabLayout.setScrollPosition(moduleTypeId -1, 0, true);
            }else{
                tabLayout.setScrollPosition(moduleTypeId, 0, true);
            }
        }

        moduleTypeId = moduleTypeId + 1;

        if(NetworkUtil.hasConnection(context)){
            HttpClient.getInstance().other(moduleTypeId, pageIndex, pageSize, cb);
        }else{
            listView.setPullRefreshEnable(false);
            try {
                dataList = db.findAll(Selector.from(Other.class).where("moduleTypeId", "=", moduleTypeId));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

    }

    private Callback<OtherResp> cb = new Callback<OtherResp>() {
        @Override
        public void success(OtherResp resp, Response response) {
            listView.stopRefresh();
            if (resp.getCode() == 200) {
                dataList = resp.getData().getData();
                try {
                    db.delete(Other.class, WhereBuilder.b("moduleTypeId", "=", moduleTypeId));
                    db.saveAll(dataList);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            } else {
                T.s(context, "获取数据错误");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
            T.s(context, "服务器错误");
        }
    };

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
            final Other other = dataList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_other, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //
            holder.titleTxt.setText(other.getTitle());
            holder.contenTxt.setText(Html.fromHtml(other.getDescription()));
            holder.dateTxt.setText(other.getPublishTime());
            if (!TextUtils.isEmpty(other.getCoverImg())) {
                Picasso.with(context).load(other.getCoverImg()).placeholder(R.drawable.loading).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.img);
            }

            //
            return convertView;
        }

        class ViewHolder {
            TextView titleTxt;
            TextView contenTxt;
            TextView dateTxt;
            ImageView img;

            public ViewHolder(View view) {
                titleTxt  = (TextView) view.findViewById(R.id.other_title_txt);
                contenTxt = (TextView) view.findViewById(R.id.other_content_txt);
                dateTxt   = (TextView) view.findViewById(R.id.other_date_txt);
                img       = (ImageView) view.findViewById(R.id.other_img);
            }
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

    @Override
    public void onRefresh(int id) {
        pageIndex = 1;
        listView.startRefresh();
        HttpClient.getInstance().other(moduleTypeId, pageIndex, pageSize, cb);
    }

    @Override
    public void onLoadMore(int id) {

    }
}
