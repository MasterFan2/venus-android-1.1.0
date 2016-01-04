package com.chinajsbn.venus.ui.plan;

import android.content.Intent;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.DoubleImageItem;
import com.chinajsbn.venus.net.bean.DoubleSimpleCustom;
import com.chinajsbn.venus.net.bean.ImageItem;
import com.chinajsbn.venus.net.bean.PlanDetail;
import com.chinajsbn.venus.net.bean.Scheme;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.Style;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.SimpleDetailPictureActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.mt_listview.MyListView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 案例详情
 * new plan detail
 */
@ActivityFeature(layout = R.layout.activity_mplan_detail)
public class MPlanDetailActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

    private String parentId;
    private String detailId;

    //----------------header--------------------
    private TextView descTxt;
    private TextView themeTxt;
    private TextView styleTxt;
    private TextView colourTxt;
    private TextView hotelTxt;
    private TextView newPriceTxt;
    private TextView oldPriceTxt;
    private TextView scenePriceTxt;
    private TextView f4preTxt, f4PriceTxt;

    private ArrayList<ImageItem> orderList = new ArrayList<>();

    @Override
    public void initialize() {

        View headerView = LayoutInflater.from(context).inflate(R.layout.header_plan_detail, null);

        descTxt = (TextView) headerView.findViewById(R.id.header_plan_desc_txt);
        themeTxt = (TextView) headerView.findViewById(R.id.header_plan_theme_txt);
        styleTxt = (TextView) headerView.findViewById(R.id.header_plan_style_txt);
        colourTxt = (TextView) headerView.findViewById(R.id.header_plan_colour_txt);

        hotelTxt = (TextView) headerView.findViewById(R.id.header_plan_hotel_txt);
        newPriceTxt = (TextView) headerView.findViewById(R.id.header_plan_newPrice_txt);
        oldPriceTxt = (TextView) headerView.findViewById(R.id.header_plan_oldPrice_txt);
        scenePriceTxt = (TextView) headerView.findViewById(R.id.header_plan_scen_txt);
        f4preTxt   = (TextView) headerView.findViewById(R.id.header_plan_f4_pre_txt);
        f4PriceTxt = (TextView) headerView.findViewById(R.id.header_plan_f4price_txt);

        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.addHeaderView(headerView);
        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);

        parentId = getIntent().getStringExtra("parentId");
        detailId = getIntent().getStringExtra("detailId");
        HttpClient.getInstance().getPlanNewDetail(parentId, detailId, cb);
    }

    private Callback<Base<ArrayList<PlanDetail>>> cb = new Callback<Base<ArrayList<PlanDetail>>>() {
        @Override
        public void success(Base<ArrayList<PlanDetail>> resp, Response response) {
            if (resp.getCode() == 200) {

                //1.设置header
                if (resp.getData() != null && resp.getData().size() > 0) {
                    PlanDetail detail = resp.getData().get(0);
                    descTxt.setText(Html.fromHtml(detail.getSchemeDesc()));
                    String tempStyle = "";
                    for (Style style : detail.getSchemeStyles()) {
                        tempStyle += style.getStyleName() + ",";
                    }
                    tempStyle = tempStyle.substring(0, tempStyle.lastIndexOf(","));
                    styleTxt.setText(tempStyle);
                    themeTxt.setText(detail.getSchemeName());
                    colourTxt.setText(detail.getSchemeColor());
                    hotelTxt.setText("于" + detail.getWeddingDate() + "在" + detail.getHotelName() + detail.getBanquetHallName());
                    hotelTxt.setVisibility(View.GONE);
                    newPriceTxt.setText(detail.getTotalPrice() + "");
                    oldPriceTxt.setText(detail.getOriginalPrice() + "");

                    //中间删除线
                    oldPriceTxt.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                    oldPriceTxt.getPaint().setAntiAlias(true);// 抗锯齿

                    scenePriceTxt.setText(detail.getSceneCost() + "");
                    f4PriceTxt.setText("￥" + detail.getHdpcCost() + "");

                    //1：主持人  2:化妆师  3：摄影师  4：摄像师    5：双机摄影    6：双机摄像
                    f4preTxt.setText("婚礼人(" + detail.getStandardWedding().replace(",", "/").replace("1", "主持人").replace("2", "化妆师").replace("3", "摄影师").replace("4", "摄像师").replace("5", "双机摄影").replace("6", "双机摄像")+"):");

                    //2.获取到 所有横图和竖图。
                    int size = detail.getImageList().size();
                    ArrayList<ImageItem> hList = new ArrayList<>();
                    ArrayList<ImageItem> vList = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        ImageItem item = detail.getImageList().get(i);
                        String url = item.getContentUrl();
                        url = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf("."));
                        int width = Integer.parseInt(url.split("x")[0]);
                        int height = Integer.parseInt(url.split("x")[1]);

                        if (width >= height) hList.add(item);
                        else if (width < height) vList.add(item);
                        else hList.add(item);
                    }

                    //3.合并
                    ArrayList<DoubleImageItem> finalList = new ArrayList<>();
                    int h_size = hList.size();
                    int v_size = vList.size();

                    if (h_size <= 0 && v_size <= 0) {
                        T.s(context, "无数据");
                        return;
                    }
                    int j = 0;
                    if (h_size > v_size) {//模图比较多
                        for (int i = 0; i < h_size; i++) {
                            //----------------------------
//                            if (i == 0) {//第一二行
//                                finalList.add(new DoubleImageItem(hList.get(i), null));
//                                if(v_size > 0) {
//                                    finalList.add(new DoubleImageItem(null, vList.get(j++)));
//                                } else {
//                                    i++;
//                                    finalList.add(new DoubleImageItem(null, hList.get(i)));
//                                }
//                            } else {
//                            if (j + 2 < v_size) {//本次充足
//                                finalList.add(new DoubleImageItem(hList.get(i), null));
//                                finalList.add(new DoubleImageItem(vList.get(j++), vList.get(j++)));
//                            } else
                            if (j + 1 < v_size) {
                                finalList.add(new DoubleImageItem(hList.get(i), null));
                                finalList.add(new DoubleImageItem(vList.get(j++), vList.get(j++)));
                            } else if (j < v_size) {
                                finalList.add(new DoubleImageItem(hList.get(i), null));
                                finalList.add(new DoubleImageItem(vList.get(j++), null));
                            } else {
                                finalList.add(new DoubleImageItem(hList.get(i), null));
                            }
//                            }
                        }
                    } else {     //竖图比较多

                        if (h_size <= 0) {//没有横图， 全是竖图
                            for (int i = 0; i < v_size; i++) {
                                if (i == 0) {//第一二行
                                    finalList.add(new DoubleImageItem(hList.get(i), null));
                                    i++;
                                    finalList.add(new DoubleImageItem(null, vList.get(i)));
                                } else {
                                    finalList.add(new DoubleImageItem(vList.get(i), null));
                                }
                            }

                        } else {//多个横图
                            for (int i = 0; i < v_size; i++) {
//                                if (i == 0) {//第一二行
//                                    finalList.add(new DoubleImageItem(hList.get(j), null));
//                                    j++;
//                                    finalList.add(new DoubleImageItem(null, vList.get(i)));
//                                } else {
                                if (j < h_size) {//判断横图
                                    finalList.add(new DoubleImageItem(hList.get(j++), null));
                                    if (i + 1 < v_size) {
                                        finalList.add(new DoubleImageItem(vList.get(i), vList.get(++i)));
                                    } else {
                                        finalList.add(new DoubleImageItem(vList.get(i), null));
                                    }
                                } else {
                                    if (i + 1 < v_size) {
                                        finalList.add(new DoubleImageItem(vList.get(i), vList.get(++i)));
                                    } else {
                                        finalList.add(new DoubleImageItem(vList.get(i), null));
                                    }
                                }
//                                }
                            }
                        }
                    }
                    j = 0;


                    final int f_size = finalList.size();
                    for (int i = 0; i < f_size; i++) {
                        DoubleImageItem temp = finalList.get(i);
                        if(temp.getData1() != null && temp.getData2() != null){
                            orderList.add(temp.getData1());
                            orderList.add(temp.getData2());
                        }else if(temp.getData1() != null){
                            orderList.add(temp.getData1());
                        }else if(temp.getData2() != null){
                            orderList.add(temp.getData2());
                        }
                    }

                    adapter.setDataList(finalList);
                    adapter.notifyDataSetChanged();

                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            serverError();
        }
    };

    class MyAdapter extends BaseAdapter implements View.OnClickListener  {

        private List<DoubleImageItem> dataList;

        public MyAdapter(ArrayList<DoubleImageItem> list) {
            this.dataList = list;
        }

        public List<DoubleImageItem> getDataList() {
            return dataList;
        }

        public void setDataList(List<DoubleImageItem> dataList) {
            this.dataList = dataList;
        }

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public DoubleImageItem getItem(int i) {
            return dataList.get(i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            DoubleImageItem imageItem = dataList.get(position);
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_plandetail_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            //两个
            if(imageItem.getData1() != null && imageItem.getData2() != null){
                holder.img1.setVisibility(View.VISIBLE);
                holder.img2.setVisibility(View.VISIBLE);
                Picasso.with(context).load(imageItem.getData1().getContentUrl() + DimenUtil.getVertical50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.img1);
                holder.img1.setTag(imageItem.getData1().getContentUrl());
                Picasso.with(context).load(imageItem.getData2().getContentUrl() + DimenUtil.getVertical50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.img2);
                holder.img2.setTag(imageItem.getData2().getContentUrl());
                holder.img2.setOnClickListener(this);
                holder.img1.setOnClickListener(this);
            }else {//只有一个
                holder.img1.setVisibility(View.VISIBLE);
                holder.img2.setVisibility(View.GONE);

                if (DimenUtil.isHorizontal(imageItem.getData1().getContentUrl())) {
                    Picasso.with(context).load(imageItem.getData1().getContentUrl() + DimenUtil.getHorizontal50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.img1);
                } else {//只有一个， 又是竖图。
//                        Picasso.with(context).load(doubleSimpleCustom.getData1().getContentUrl() + "@" + DimenUtil.screenWidth + "w_" + DimenUtil.screenWidth * 3 + "h_40Q").into(holder.contentImg1);
                    Picasso.with(context).load(imageItem.getData1().getContentUrl() + "@" + DimenUtil.screenWidth + "w_" + DimenUtil.screenWidth * 3 / 2 + "h_40Q" + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.img1);
                }
                holder.img1.setTag(imageItem.getData1().getContentUrl());
                holder.img1.setOnClickListener(this);
            }

            //
            return view;
        }

        @Override
        public void onClick(View v) {
            String url = v.getTag().toString();
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent(context, SimpleDetailPictureActivity.class);
                intent.putExtra("dataList", orderList);
                intent.putExtra("isPlan",   true);
                intent.putExtra("url",      url);
                animStart(intent);
            }
        }

        class ViewHolder {
            ImageView img1;
            ImageView img2;
            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public ViewHolder(View itemView) {
                img1 = (ImageView) itemView.findViewById(R.id.img1);
                img2 = (ImageView) itemView.findViewById(R.id.img2);
            }
        }
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @OnClick(R.id.m_title_left_btn)
    public void back(View view) {
        animFinish();
    }

}
