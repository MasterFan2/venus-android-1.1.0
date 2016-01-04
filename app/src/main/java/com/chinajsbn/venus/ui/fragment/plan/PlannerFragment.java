package com.chinajsbn.venus.ui.fragment.plan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Dresser;
import com.chinajsbn.venus.net.bean.Planner;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.WorksDetailActivity;
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
import com.tool.widget.recyclerviewdiviver.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/10/16.
 */
@FragmentFeature(layout = R.layout.fragment_planner)
public class PlannerFragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener {

    public static final String TAG = "PlannerFragment";

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private List<Planner> dataList;
    private MyAdapter adapter;

    ///------------------page--------------------
    private int pageSize = 30;
    private int pageIndex = 1;
    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

    ////////////////////////cache///////////////////////////
    private DbUtils db;


    /////////////////////end cache/////////////////////////


    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(PlannerFragment.this)
                .create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.gray_400)).build());

        adapter = new MyAdapter(PlannerFragment.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void show() {
        if (dataList == null || dataList.size() <= 0) {
            if(NetworkUtil.hasConnection(getActivity())){
                dialog.show();
                HttpClient.getInstance().getPlannerList(pageIndex, pageSize, callback);
            }else {
                try {
                    List<Planner> plannerList = db.findAll(Planner.class);
                    if(plannerList == null || plannerList.size() <= 0) {
                        handler.sendEmptyMessageDelayed(10, 1000);
                        handler.sendEmptyMessageDelayed(11, 4000);
                        return;
                    }
                    for (Planner p : plannerList){
                        List<Dresser> dresserList = db.findAll(Selector.from(Dresser.class).where("parentName", "=", p.getPlannerName()));
                        p.setList(dresserList);
                    }
                    dataList = plannerList;
                    adapter.notifyDataSetChanged();

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
    public void hide() {

    }

    private Callback<Base<ArrayList<Planner>>> callback = new Callback<Base<ArrayList<Planner>>>() {

        @Override
        public void success(Base<ArrayList<Planner>> base, Response response) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (base.getCode() == 200) {
                dataList = base.getData();
                adapter.notifyDataSetChanged();

                int size = dataList == null ? 0 : dataList.size();
                if (size > 0) { //cache
                    if(!MTDBUtil.todayChecked(getActivity(), TAG)) {//当天没有缓存过
                        try {
                            db.delete(Dresser.class, WhereBuilder.b("tag", "=", TAG));//删除之前缓存的数据
                            db.deleteAll(Planner.class);
                            for (Planner p : dataList) {
                                List<Dresser> dressers = p.getList();
                                for (Dresser d : dressers) {

                                    //
                                    d.setParentName(p.getPlannerName());

                                    d.setTag(TAG);

                                    //保存图片集
                                    if(d.getDetailImages() != null && d.getDetailImages().length > 0){
                                        String temp = null;
                                        for (int i = 0; i < d.getDetailImages().length; i++) {
                                            temp += d.getDetailImages()[i]+",";
                                        }
                                        d.setCacheImgs(temp);
                                    }
                                }
                                db.saveOrUpdateAll(dressers);
                            }

                            db.saveOrUpdateAll(dataList);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }else{
                        S.o(">>>TAG 策划师已经缓存过了");
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "获取数据错误,请重试!");
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

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener listener) {
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_f4, null);
            return new EmceeViewHolder(view, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            EmceeViewHolder emceeHolder = (EmceeViewHolder) holder;
            final Planner planner = dataList.get(position);
            int size = planner.getList() == null ? 0 : planner.getList().size();

            emceeHolder.nameTxt.setText(planner.getPlannerName());//名字
            emceeHolder.descTxt.setText(planner.getDescription());//简介

            Picasso.with(getActivity()).load(planner.getPhotoUrl()).into(emceeHolder.headImg);

            if (size == 0) {
                emceeHolder.topLayout.setVisibility(View.GONE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);
            } else if (size == 1) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);
                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.INVISIBLE);

                Picasso.with(getActivity()).load(planner.getList().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage1);
            } else if (size == 2) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(planner.getList().get(0).getImageUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage1);
                Picasso.with(getActivity()).load(planner.getList().get(1).getImageUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage2);
            } else if (size == 3) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.VISIBLE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);
                emceeHolder.contentImage3.setVisibility(View.VISIBLE);
                emceeHolder.contentImage4.setVisibility(View.INVISIBLE);

                Picasso.with(getActivity()).load(planner.getList().get(0).getImageUrl()).into(emceeHolder.contentImage1);
                Picasso.with(getActivity()).load(planner.getList().get(1).getImageUrl()).into(emceeHolder.contentImage2);
                Picasso.with(getActivity()).load(planner.getList().get(2).getImageUrl()).into(emceeHolder.contentImage3);

            } else if (size == 4) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.VISIBLE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);
                emceeHolder.contentImage3.setVisibility(View.VISIBLE);
                emceeHolder.contentImage4.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(planner.getList().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage1);
                Picasso.with(getActivity()).load(planner.getList().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage2);
                Picasso.with(getActivity()).load(planner.getList().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage3);
                Picasso.with(getActivity()).load(planner.getList().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(emceeHolder.contentImage4);
            }

            emceeHolder.contentImage1.setTag(position + "@" + 1);
            emceeHolder.contentImage2.setTag(position + "@" + 2);
            emceeHolder.contentImage3.setTag(position + "@" + 3);
            emceeHolder.contentImage4.setTag(position + "@" + 4);

            emceeHolder.contentImage1.setOnClickListener(this);
            emceeHolder.contentImage2.setOnClickListener(this);
            emceeHolder.contentImage3.setOnClickListener(this);
            emceeHolder.contentImage4.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String p_i[] = view.getTag().toString().split("@");
            int position = Integer.parseInt(p_i[0]);
            int i = Integer.parseInt(p_i[1]) - 1;

            final Planner f4 = dataList.get(position);
            Intent intent = new Intent(getActivity(), WorksDetailActivity.class);
            intent.putExtra("list", f4.getList().get(i).getDetailImages());
            intent.putExtra("isPlanner", true);
            animStart(intent);
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        /**
         *
         */
        public final class EmceeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView headImg;
            TextView nameTxt;
            TextView descTxt;
            LinearLayout topLayout;
            LinearLayout bottomLayout;
            ImageView contentImage1;
            ImageView contentImage2;
            ImageView contentImage3;
            ImageView contentImage4;

            public EmceeViewHolder(View view, OnRecyclerItemClickListener listener) {
                super(view);
                headImg = (ImageView) view.findViewById(R.id.item_f4_head_img);
                nameTxt = (TextView) view.findViewById(R.id.item_f4_name_txt);
                descTxt = (TextView) view.findViewById(R.id.item_f4_description_txt);

                topLayout = (LinearLayout) view.findViewById(R.id.item_f4_content_top_layout);
                bottomLayout = (LinearLayout) view.findViewById(R.id.item_f4_content_bottom_layout);

                contentImage1 = (ImageView) view.findViewById(R.id.item_f4_content_img1);
                contentImage2 = (ImageView) view.findViewById(R.id.item_f4_content_img2);
                contentImage3 = (ImageView) view.findViewById(R.id.item_f4_content_img3);
                contentImage4 = (ImageView) view.findViewById(R.id.item_f4_content_img4);
                onRecyclerItemClickListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }
}
