package com.chinajsbn.venus.ui.dresses;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Dress;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.SimpleDetailPictureActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MyRecyclerView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_dress_list)
public class DressDetailActivity extends MBaseFragmentActivity implements  OnRecyclerItemClickListener{

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private MyAdapter    adapter;

    private ArrayList<Dress> dataList;
    private int brandId = 0;

    @Override
    public void initialize() {
        brandId = getIntent().getIntExtra("brandId", 0);
        adapter = new MyAdapter(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        if(brandId != 0){
            HttpClient.getInstance().dressListByBrandId(brandId, cb);
        }
    }

    private Callback<Base<ArrayList<Dress>>> cb = new Callback<Base<ArrayList<Dress>>>() {
        @Override
        public void success(Base<ArrayList<Dress>> resp, Response response) {
            if(resp.getCode() == 200){
                dataList = resp.getData();
                adapter.notifyDataSetChanged();
            }else {
                T.s(context, "获取数据错误");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "服务器错误");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        Intent intent = new Intent(context, SimpleDetailPictureActivity.class);
        intent.putExtra("dataList", dataList);
        intent.putExtra("isDress", true);
        intent.putExtra("url", dataList.get(position).getImageUrl());
        animStart(intent);
    }

    class MyAdapter extends MyRecyclerView.Adapter<MyAdapter.MViewHolder> implements View.OnClickListener {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener l){
            this.onRecyclerItemClickListener = l;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_dresses_detail, parent, false);
            return new MViewHolder(view, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            Dress dress = dataList.get(position);
            if(!TextUtils.isEmpty(dress.getImageUrl())){
                Picasso.with(context).load(dress.getImageUrl()+ DimenUtil.getVertical50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.contentImg);
            }
            holder.nameTxt.setText(dress.getWeddingDressName());
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public void onClick(View v) {

        }

        class MViewHolder extends MyRecyclerView.ViewHolder implements View.OnClickListener {

            ImageView contentImg;
            TextView nameTxt;

            public MViewHolder(View itemView, OnRecyclerItemClickListener l) {
                super(itemView);
                contentImg = (ImageView) itemView.findViewById(R.id.dresses_detail_content_img);
                nameTxt    = (TextView) itemView.findViewById(R.id.dresses_detail_name_txt);
                onRecyclerItemClickListener = l;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
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

}
