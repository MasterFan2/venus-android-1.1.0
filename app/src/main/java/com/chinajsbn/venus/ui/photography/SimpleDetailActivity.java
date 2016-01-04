package com.chinajsbn.venus.ui.photography;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.DoubleSimpleCustom;
import com.chinajsbn.venus.net.bean.MoreTag;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.SimplePhotographer;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.fragment.photography.MPhotoSimpleFragment;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
import com.tool.widget.MyRecyclerView;

import java.net.URLDecoder;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 样片详情
 */
@ActivityFeature(layout = R.layout.activity_simple_detail)
public class SimpleDetailActivity extends MBaseFragmentActivity implements OnRecyclerItemClickListener {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;
    //---------------------------------------------------------
    private SimplePhotographer photographer;
    private SimplePhotographer stylist;
    private String strStyles = "";
    private String name = "";
    private String date = "";
    private Simple headSimple;

    private String simpleOrCustom;


    private String f = "";
    private String m = "";

    private DbUtils db;

//
//    @ViewInject(R.id.simple_detail_photographer_head_img)
//    private ImageView photographerImg;
//
//    @ViewInject(R.id.simple_detail_stylist_head_img)
//    private ImageView stylistImg;
//
//    @ViewInject(R.id.simple_detail_photographer_name_txt)
//    private TextView photographerNameTxt;
//
//    @ViewInject(R.id.simple_detail_stylist_name_txt)
//    private TextView stylistNameTxt;
//
//
//    @ViewInject(R.id.simple_detail_styles_txt)
//    private TextView stylesTxt;

    private ArrayList<Simple> data;
    private ArrayList<Simple> orderList = new ArrayList<>();

    //------------------------------------------------------------------


    @Override
    public void initialize() {

        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        String contentId = getIntent().getStringExtra("contentId");
        String parentId = getIntent().getStringExtra("parentId");

        simpleOrCustom = getIntent().getStringExtra("simpleOrCustom");
        if (simpleOrCustom.equals("custom")) {
            titleView.setTitleText("客片详情");
            HttpClient.getInstance().getDayCustomDetail(parentId, contentId, cb);
        } else if (simpleOrCustom.equals("simple")) {
            titleView.setTitleText("作品详情");
            HttpClient.getInstance().getSimpleDetail(parentId, contentId, cb);
        }


        photographer = (SimplePhotographer) getIntent().getSerializableExtra("photographer");
        stylist = (SimplePhotographer) getIntent().getSerializableExtra("stylist");
        strStyles = getIntent().getStringExtra("styles");
        name = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");

    }


    /**
     * 获取数据回调
     */
    private Callback<Base<ArrayList<Simple>>> cb = new Callback<Base<ArrayList<Simple>>>() {
        @Override
        public void success(final Base<ArrayList<Simple>> simpleDetailResp, Response response) {
            data = simpleDetailResp.getData();

            if (data != null && data.size() > 0) {
                f = data.get(0).getActorNameFemale();
                m = data.get(0).getActorNameMale();
            }else {
                T.s(context, "无数据");
                return;
            }

            //1。获取到 所有横图和竖图。
            ArrayList<Simple> hList = new ArrayList<>();
            ArrayList<Simple> vList = new ArrayList<>();
            int size = data.size();
            for (int i = 0; i < size; i++) {
                Simple simple = data.get(i);
                String url = simple.getContentUrl();
                url = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf("."));
                int width = Integer.parseInt(url.split("x")[0]);
                int height = Integer.parseInt(url.split("x")[1]);

                if (width >= height) hList.add(simple);
                else if (width < height) vList.add(simple);
            }

            //2。合并
            ArrayList<DoubleSimpleCustom> finalList = new ArrayList<>();
            int h_size = hList.size();
            int v_size = vList.size();

            if(h_size <= 0 && v_size <= 0){
                T.s(context, "无数据");
                return;
            }

            int j = 0;

            if(h_size > v_size || h_size == v_size){//模图比较多
                for (int i = 0; i < h_size; i++) {
                    //----------------------------
                    if (i == 0) {//第一二行
                        ///////////////////////第一行//////////////////////
                        finalList.add(new DoubleSimpleCustom(hList.get(i), null));

                        ///////////////////////第二行//////////////////////
                        if(v_size > 0) {
                            finalList.add(new DoubleSimpleCustom(null, vList.get(j++)));
                        } else {
                            i++;
                            finalList.add(new DoubleSimpleCustom(null, hList.get(i)));
                        }
                    } else {
                        if (j + 2 < v_size) {//本次充足
                            finalList.add(new DoubleSimpleCustom(hList.get(i), null));
                            finalList.add(new DoubleSimpleCustom(vList.get(j++), vList.get(j++)));
                        } else if (j + 1 < v_size) {
                            finalList.add(new DoubleSimpleCustom(hList.get(i), null));
                            finalList.add(new DoubleSimpleCustom(vList.get(j++), vList.get(j++)));
                        } else if (j < v_size) {
                            finalList.add(new DoubleSimpleCustom(hList.get(i), null));
                            finalList.add(new DoubleSimpleCustom(vList.get(j++), null));
                        } else {
                            finalList.add(new DoubleSimpleCustom(hList.get(i), null));
                        }
                    }
                }
            } else {     //竖图比较多

                if(h_size <= 0){//没有横图， 全是竖图
                    for (int i = 0; i < v_size; i++) {
                        if (i == 0) {//第一二行
                            finalList.add(new DoubleSimpleCustom(hList.get(i), null));
                            i++;
                            finalList.add(new DoubleSimpleCustom(null, vList.get(i)));
                        } else {
                            finalList.add(new DoubleSimpleCustom(vList.get(i), null));
                        }
                    }

                }else {//多个横图
                    for (int i = 0; i < v_size; i++) {
                        if (i == 0) {//第一二行
                            finalList.add(new DoubleSimpleCustom(hList.get(j), null));
                            j++;
                            finalList.add(new DoubleSimpleCustom(null, vList.get(i)));
                        } else {

                            if(j < h_size) {//判断横图
                                finalList.add(new DoubleSimpleCustom(hList.get(j++), null));
                                if(i + 1 < v_size){
                                    finalList.add(new DoubleSimpleCustom(vList.get(i), vList.get(++i)));
                                }else {
                                    finalList.add(new DoubleSimpleCustom(vList.get(i), null));
                                }
                            }else {
                                if(i+ 1 < v_size){
                                    finalList.add(new DoubleSimpleCustom(vList.get(i), vList.get(++i)));
                                }else {
                                    finalList.add(new DoubleSimpleCustom(vList.get(i), null));
                                }

                            }
                        }
                    }
                }

            }
            j = 0;
            recyclerView.setAdapter(new MyAdapter(finalList, SimpleDetailActivity.this));

            final int f_size = finalList.size();
            for (int i = 0; i < f_size; i++) {
                DoubleSimpleCustom temp = finalList.get(i);
                if(temp.getData1() != null && temp.getData2() != null){
                    orderList.add(temp.getData1());
                    orderList.add(temp.getData2());
                }else if(temp.getData1() != null){
                    orderList.add(temp.getData1());
                }else if(temp.getData2() != null){
                    orderList.add(temp.getData2());
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "获取数据错误,请重试 !");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
//        T.s(context, "position:" + position);
    }

    class MyAdapter extends MyRecyclerView.Adapter<MyAdapter.MViewHolder> implements View.OnClickListener {

        private ArrayList<DoubleSimpleCustom> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(ArrayList<DoubleSimpleCustom> list, OnRecyclerItemClickListener listener) {
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_custom_layout, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            final DoubleSimpleCustom doubleSimpleCustom = dataList.get(position);
            if (position == 1) {
                holder.twoLayout.setVisibility(View.GONE);
                holder.styleLayout.setVisibility(View.VISIBLE);
                holder.contentImg2.setVisibility(View.VISIBLE);
                holder.contentImg1.setVisibility(View.GONE);
                holder.styleTxt.setText("风格：\n" + strStyles);
                holder.nameTxt.setText(name);
                if (simpleOrCustom.equals("custom")) {
                    holder.dateTxt.setText(date.split(" ")[0]);
                    holder.moreTxt.setVisibility(View.GONE);
                } else {
                    holder.dateTxt.setVisibility(View.GONE);
                    //查看更多
                    holder.moreTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (simpleOrCustom.equals("simple")) {
                                try {
                                    db.saveOrUpdate(new MoreTag("simpleMore", "load_more"));
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                MPhotoSimpleFragment.refreshed = "true";
                            }
                            animFinish();
                        }
                    });
                }

                //摄影师造型师头像
                if (photographer != null && stylist != null) {

                    holder.photographerStylistContainer.setVisibility(View.VISIBLE);
                    holder.stylistContainer.setVisibility(View.VISIBLE);
                    if (photographer != null) {
                        holder.photographerContainer.setVisibility(View.VISIBLE);
                        holder.photographerTxt.setText(photographer.getPersonName());
                        Picasso.with(context).load(photographer.getPhotoUrl()).into(holder.photographerImg);
                    } else {
                        holder.photographerContainer.setVisibility(View.GONE);
                    }

                    if (stylist != null) {
                        holder.stylistContainer.setVisibility(View.VISIBLE);
                        holder.styleTxt.setText(stylist.getPersonName());
                        Picasso.with(context).load(stylist.getPhotoUrl()).into(holder.stylistImg);
                    } else {
                        holder.stylistContainer.setVisibility(View.GONE);
                    }
                } else {
                    holder.photographerStylistContainer.setVisibility(View.GONE);
                    holder.stylistContainer.setVisibility(View.GONE);
                }

                //内容
                Picasso.with(context).load(doubleSimpleCustom.getData2().getContentUrl() + DimenUtil.getVertical50Q()+ DimenUtil.getSuffixUTF8()).into(holder.contentImg2);
                holder.contentImg2.setTag(doubleSimpleCustom.getData2().getContentUrl());
                holder.contentImg2.setOnClickListener(this);

            } else {

                if (position == 0 && simpleOrCustom.equals("custom")) {//显示新郎新娘
                    holder.twoLayout.setVisibility(View.VISIBLE);
                    holder.fTxt.setText(f);
                    holder.mTxt.setText(m);
                } else {
                    holder.twoLayout.setVisibility(View.GONE);
                }
                holder.styleLayout.setVisibility(View.GONE);

                if (doubleSimpleCustom.getData1() != null && doubleSimpleCustom.getData2() != null) {//显示两个
                    holder.contentImg1.setVisibility(View.VISIBLE);
                    holder.contentImg2.setVisibility(View.VISIBLE);
                    Picasso.with(context).load(doubleSimpleCustom.getData1().getContentUrl() + DimenUtil.getVertical50Q()+ DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.contentImg1);
                    holder.contentImg1.setTag(doubleSimpleCustom.getData1().getContentUrl());
                    Picasso.with(context).load(doubleSimpleCustom.getData2().getContentUrl() + DimenUtil.getVertical50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.contentImg2);
                    holder.contentImg2.setTag(doubleSimpleCustom.getData2().getContentUrl());
                    holder.contentImg2.setOnClickListener(this);
                    holder.contentImg1.setOnClickListener(this);
                } else {//显示一个
                    holder.contentImg2.setVisibility(View.GONE);
                    holder.contentImg1.setVisibility(View.VISIBLE);

                    if (DimenUtil.isHorizontal(doubleSimpleCustom.getData1().getContentUrl())) {
                        Picasso.with(context).load(doubleSimpleCustom.getData1().getContentUrl() + DimenUtil.getHorizontal50Q() + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.contentImg1);
                    } else {//只有一个， 又是竖图。
//                        Picasso.with(context).load(doubleSimpleCustom.getData1().getContentUrl() + "@" + DimenUtil.screenWidth + "w_" + DimenUtil.screenWidth * 3 + "h_40Q").into(holder.contentImg1);
                        Picasso.with(context).load(doubleSimpleCustom.getData1().getContentUrl() + "@" + DimenUtil.screenWidth + "w_" + DimenUtil.screenWidth * 3 / 2 + "h_40Q" + DimenUtil.getSuffixUTF8()).placeholder(R.drawable.loading).into(holder.contentImg1);
                    }

                    holder.contentImg1.setTag(doubleSimpleCustom.getData1().getContentUrl());
                    holder.contentImg1.setOnClickListener(this);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public void onClick(View view) {
            String url = view.getTag().toString();
            if (!TextUtils.isEmpty(url)) {
                Intent intent = new Intent(context, SimpleDetailPictureActivity.class);
                intent.putExtra("dataList", orderList);
                intent.putExtra("url", url);
                animStart(intent);
            }
        }

        public final class MViewHolder extends MyRecyclerView.ViewHolder implements View.OnClickListener {

            ImageView contentImg1;
            ImageView contentImg2;
            LinearLayout styleLayout;

            //摄影师造型师
            LinearLayout photographerStylistContainer;
            LinearLayout photographerContainer;
            LinearLayout stylistContainer;
            ImageView photographerImg;
            TextView photographerTxt;
            ImageView stylistImg;
            TextView stylistTxt;

            //新郎新娘名字
            LinearLayout twoLayout;
            TextView fTxt;
            TextView mTxt;

            //样式 风格
            TextView nameTxt;
            TextView dateTxt;
            TextView styleTxt;
            TextView moreTxt;


            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);

                contentImg1 = (ImageView) itemView.findViewById(R.id.img1);
                contentImg2 = (ImageView) itemView.findViewById(R.id.img2);

                styleLayout = (LinearLayout) itemView.findViewById(R.id.styleLayout);

                twoLayout = (LinearLayout) itemView.findViewById(R.id.two_person_layout);
                fTxt = (TextView) itemView.findViewById(R.id.two_person_f_txt);
                mTxt = (TextView) itemView.findViewById(R.id.two_person_m_txt);

                nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
                dateTxt = (TextView) itemView.findViewById(R.id.dateTxt);
                styleTxt = (TextView) itemView.findViewById(R.id.styleTxt);
                moreTxt = (TextView) itemView.findViewById(R.id.moreTxt);

                //
                photographerStylistContainer = (LinearLayout) itemView.findViewById(R.id.photographer_stylist_container);
                photographerContainer = (LinearLayout) itemView.findViewById(R.id.photographer_container);
                stylistContainer = (LinearLayout) itemView.findViewById(R.id.stylist_container);
                photographerImg = (ImageView) itemView.findViewById(R.id.photographer_head_img);
                photographerTxt = (TextView) itemView.findViewById(R.id.photographer_head_txt);
                stylistImg = (ImageView) itemView.findViewById(R.id.stylist_head_img);
                stylistTxt = (TextView) itemView.findViewById(R.id.stylist_head_txt);

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
    public boolean onKeydown() {
//        if (fab.getVisibility() != View.VISIBLE) {
//            FabTransformation.with(fab).setOverlay(overlay).transformFrom(sheet);
//            return false;
//        }
        animFinish();
        return true;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        System.gc();
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.activity_empty);
    }
}
