package com.chinajsbn.venus.ui.plan;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.ImageItem;
import com.chinajsbn.venus.net.bean.SchemeDetail;
import com.chinajsbn.venus.net.bean.SchemeStyle;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.fragment.plan.PlanSimpleFragment;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 案例详情
 */
@ActivityFeature(layout = R.layout.activity_plan_detail)
public class PlanDetailActivity extends MBaseFragmentActivity implements OnRecyclerItemClickListener {

    private String parentId;
    private String detailId;
    private String name;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private SchemeDetail detail;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @Override
    public void initialize() {

        //固定写法。
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        parentId = getIntent().getStringExtra("parentId");
        detailId = getIntent().getStringExtra("detailId");
        name     = getIntent().getStringExtra("name");

        titleView.setTitleText("案例详情");

        //get data
        HttpClient.getInstance().getPlanDetail(parentId, detailId, cb);
    }


    private Callback<Base<ArrayList<SchemeDetail>>> cb = new Callback<Base<ArrayList<SchemeDetail>>>() {
        @Override
        public void success(Base<ArrayList<SchemeDetail>> schemeDetail, Response response) {
            if (schemeDetail == null || schemeDetail.getData().size() <= 0)
                return;
            detail = schemeDetail.getData().get(0);

            recyclerView.setAdapter(new MyAdapter(detail.getImageList(), PlanDetailActivity.this));
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "Get data error! ");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        if(detail.getImageList() != null && detail.getImageList().size() > 0){
            String url =  detail.getImageList().get(position).getContentUrl();

            if(!TextUtils.isEmpty(url)){
                Intent intent = new Intent(context, PlanDetailPictureActivity.class);
                intent.putExtra("detail", detail);
                intent.putExtra("position", position);
                animStart(intent);
            }
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private ArrayList<ImageItem> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(ArrayList<ImageItem> list, OnRecyclerItemClickListener listener) {
            this.dataList = list;
            String strStyle = "";
            if (detail.getSchemeStyles() != null && detail.getSchemeStyles().size() > 0) {
                for (SchemeStyle style : detail.getSchemeStyles()) {
                    strStyle += style.getStyleName() + ",";
                }
                strStyle = strStyle.substring(0, strStyle.lastIndexOf(","));
            }

            dataList.add(new ImageItem("", name, "", detail.getSchemeDesc(), strStyle));
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_detail_layout, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
//            if(dataList.get(position).getShootingStyles() != null && dataList.get(position).getShootingStyles().size() > 0)
//                holder.styleTxt.setText("样式:" + dataList.get(position).getShootingStyles().get(0).getShootingStyleName());

//            //加载摄影师
//            if( dataList.get(position).getPhotographer() != null && !TextUtils.isEmpty(dataList.get(position).getPhotographer().getPhotoUrl())){
//                Picasso.with(getActivity()).load(dataList.get(position).getPhotographer().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.photographerImg);
//            }
//
//            //加造型师
//            if( dataList.get(position).getStylist() != null && !TextUtils.isEmpty(dataList.get(position).getStylist().getPhotoUrl())){
//                Picasso.with(getActivity()).load(dataList.get(position).getStylist().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.stylistImg);
//            }

            ImageItem item = dataList.get(position);

            if (!TextUtils.isEmpty(item.getContentUrl())) {
                holder.imgLayout.setVisibility(View.VISIBLE);
                holder.descLayout.setVisibility(View.GONE);
                //加载内容
                if (TextUtils.isEmpty(item.getContentUrl())) {
                    Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.contentImg);
                } else {
                    Picasso.with(context).load(item.getContentUrl()+ DimenUtil.getHorizontal()).placeholder(R.drawable.loading).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.contentImg);
                }
            } else {
                holder.imgLayout.setVisibility(View.GONE);
                holder.descLayout.setVisibility(View.VISIBLE);
                holder.moreTxt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(detail.getSchemeStyles() != null && detail.getSchemeStyles().size() > 0){
                            PlanSimpleFragment.styleId = detail.getSchemeStyles().get(0).getStyleId()+"";
                            PlanSimpleFragment.refreshed = true;
                            animFinish();
                        }
                    }
                });
                holder.descTxt.setText(item.getContentName() + "\n\n" +item.getDesc());
                holder.styleTxt.setText("风格:" + item.getStyle());
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView contentImg;
            LinearLayout descLayout;
            RelativeLayout imgLayout;
            TextView descTxt;
            TextView styleTxt;
            TextView moreTxt;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                contentImg  = (ImageView) itemView.findViewById(R.id.item_plan_detail_pic_img);
                descLayout  = (LinearLayout) itemView.findViewById(R.id.item_plan_detail_desc_layout);
                imgLayout   = (RelativeLayout) itemView.findViewById(R.id.item_plan_detail_head_layout);
                descTxt     = (TextView) itemView.findViewById(R.id.item_plan_detail_description_txt);
                moreTxt     = (TextView) itemView.findViewById(R.id.item_plan_detail_more_txt);
                styleTxt    = (TextView) itemView.findViewById(R.id.item_plan_detail_style_txt);

                onRecyclerItemClickListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }

    @OnClick(R.id.m_title_left_btn)
    public void back(View view) {
        animFinish();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
