package com.chinajsbn.venus.ui.fragment.plan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Dresser;
import com.chinajsbn.venus.net.bean.Emcee;
import com.chinajsbn.venus.net.bean.F4;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.WorksDetailActivity;
import com.chinajsbn.venus.ui.plan.VideoActivity;
import com.chinajsbn.venus.utils.DensityUtil;
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
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/10/16.
 * 选婚礼人
 */
@FragmentFeature(layout = R.layout.fragment_f4)
public class F4Fragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener {

    ////////////////////////cache///////////////////////////
    private DbUtils db;

    public static final String TAG_EMCEE = "emcee";
    public static final String TAG_DRESSER = "dresser";
    public static final String TAG_PHOTOGRAPHER = "photographer";
    public static final String TAG_CAMERAMAN = "cameraman";
    /////////////////////end cache/////////////////////////

    private final String[] filters = new String[]{"主持人", "化妆师", "摄影师", "摄像师"};

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;
    private int currentTabSelected = 0;//选项卡选择的项

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Snackbar snackbar;
    private MyAdapter adapter;
    private boolean canRefresh = false;

    ///------------------data--------------------
    private List<F4> emceeList = new ArrayList<>();        //主持人
    private List<F4> dresserList = new ArrayList<>();      //化妆师
    private List<F4> photographerList = new ArrayList<>(); //摄影师
    private List<F4> cameramanList = new ArrayList<>();    //摄像师


    ///------------------page--------------------
    private int pageSize = 200;
    private int pageIndex = 1;

    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;


    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(F4Fragment.this)
                .create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tempPosition = tab.getPosition();
                if (currentTabSelected == tempPosition) {
                    return;
                }
                currentTabSelected = tempPosition;
                if (currentTabSelected == 0) {
                    if (emceeList.size() <= 0) {
                        if (NetworkUtil.hasConnection(getActivity())) {
                            dialog.show();
                            HttpClient.getInstance().getEmceeList(pageIndex, pageSize, callback);
                        } else {
                            try {
                                List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_EMCEE));
                                if (emcList == null || emcList.size() <= 0) {
                                    handler.sendEmptyMessageDelayed(10, 100);
                                    handler.sendEmptyMessageDelayed(11, 4000);
                                    return;
                                }

                                for (F4 f4 : emcList) {
                                    List<Emcee> list = db.findAll(Selector.from(Emcee.class).where("tag", "=", f4.getPersonName()));
                                    f4.setHoster(list);
                                    S.o("::::::::::" + f4.getPersonName());
                                }
                                emceeList = emcList;
                                adapter.notifyDataSetChanged();

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else if (currentTabSelected == 1) {
                    if (dresserList.size() <= 0) {
                        if (NetworkUtil.hasConnection(getActivity())) {
                            dialog.show();
                            HttpClient.getInstance().getDresserList(pageIndex, pageSize, callback);
                        } else {
                            try {
                                List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_DRESSER));
                                if (emcList == null || emcList.size() <= 0) {
                                    handler.sendEmptyMessageDelayed(10, 100);
                                    handler.sendEmptyMessageDelayed(11, 4000);
                                    return;
                                }

                                for (F4 f4 : emcList) {
                                    List<Dresser> list = db.findAll(Selector.from(Dresser.class).where("parentName", "=", f4.getPersonName()));
                                    f4.setDresser(list);
                                }
                                dresserList = emcList;
                                adapter.notifyDataSetChanged();

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else if (currentTabSelected == 2) {
                    if (photographerList.size() <= 0) {
                        if (NetworkUtil.hasConnection(getActivity())) {
                            dialog.show();
                            HttpClient.getInstance().getPhotographerList(pageIndex, pageSize, callback);
                        } else {
                            try {
                                List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_PHOTOGRAPHER));
                                if (emcList == null || emcList.size() <= 0) {
                                    handler.sendEmptyMessageDelayed(10, 100);
                                    handler.sendEmptyMessageDelayed(11, 4000);
                                    return;
                                }

                                for (F4 f4 : emcList) {
                                    List<Dresser> list = db.findAll(Selector.from(Dresser.class).where("parentName", "=", f4.getPersonName()));
                                    f4.setPhotographer(list);
                                }
                                photographerList = emcList;
                                adapter.notifyDataSetChanged();

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else if (currentTabSelected == 3) {
                    if (cameramanList.size() <= 0) {
                        if (NetworkUtil.hasConnection(getActivity())) {//有网络连接
                            dialog.show();
                            HttpClient.getInstance().getCameramanList(pageIndex, pageSize, callback);
                        } else {     //无网络连接
                            try {
                                List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_CAMERAMAN));
                                if (emcList == null || emcList.size() <= 0) {
                                    handler.sendEmptyMessageDelayed(10, 100);
                                    handler.sendEmptyMessageDelayed(11, 4000);
                                    return;
                                }

                                for (F4 f4 : emcList) {
                                    List<Emcee> list = db.findAll(Selector.from(Emcee.class).where("tag", "=", f4.getPersonName()));
                                    f4.setCameraman(list);
                                }
                                cameramanList = emcList;
                                adapter.notifyDataSetChanged();

                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {//
                        adapter.notifyDataSetChanged();
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

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.gray_400)).build());

        adapter = new MyAdapter(F4Fragment.this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //加载更多
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && canRefresh) {
                    if (snackbar == null) snackbar = Snackbar.make(recyclerView, "加载数据 ...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                    getNextData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void getNextData() {
        pageIndex++;
        if (currentTabSelected == 0) {
            if (emceeList.size() <= 0) {
                if (NetworkUtil.hasConnection(getActivity())) {
                    dialog.show();
                    HttpClient.getInstance().getEmceeList(pageIndex, pageSize, callback);
                } else {
//                    try {
//                        List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_EMCEE));
//                        if(emcList == null || emcList.size() <= 0) {
//                            handler.sendEmptyMessageDelayed(10, 100);
//                            handler.sendEmptyMessageDelayed(11, 4000);
//                            return;
//                        }
//
//                        for (F4 f4 : emcList){
//                            List<Emcee> list = db.findAll(Selector.from(Emcee.class).where("tag", "=", f4.getPersonName()));
//                            f4.setHoster(list);
//                            S.o("::::::::::" + f4.getPersonName());
//                        }
//                        emceeList = emcList;
//                        adapter.notifyDataSetChanged();
//
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }

            } else {
                adapter.notifyDataSetChanged();
            }
        } else if (currentTabSelected == 1) {
            if (dresserList.size() <= 0) {
                if (NetworkUtil.hasConnection(getActivity())) {
                    dialog.show();
                    HttpClient.getInstance().getDresserList(pageIndex, pageSize, callback);
                } else {
//                    try {
//                        List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_DRESSER));
//                        if(emcList == null || emcList.size() <= 0) {
//                            handler.sendEmptyMessageDelayed(10, 100);
//                            handler.sendEmptyMessageDelayed(11, 4000);
//                            return;
//                        }
//
//                        for (F4 f4 : emcList){
//                            List<Dresser> list = db.findAll(Selector.from(Dresser.class).where("parentName", "=", f4.getPersonName()));
//                            f4.setDresser(list);
//                        }
//                        dresserList = emcList;
//                        adapter.notifyDataSetChanged();
//
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }
            } else {
                adapter.notifyDataSetChanged();
            }
        } else if (currentTabSelected == 2) {
            if (photographerList.size() <= 0) {
                if (NetworkUtil.hasConnection(getActivity())) {
                    dialog.show();
                    HttpClient.getInstance().getPhotographerList(pageIndex, pageSize, callback);
                } else {
//                    try {
//                        List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_PHOTOGRAPHER));
//                        if(emcList == null || emcList.size() <= 0) {
//                            handler.sendEmptyMessageDelayed(10, 100);
//                            handler.sendEmptyMessageDelayed(11, 4000);
//                            return;
//                        }
//
//                        for (F4 f4 : emcList){
//                            List<Dresser> list = db.findAll(Selector.from(Dresser.class).where("parentName", "=", f4.getPersonName()));
//                            f4.setPhotographer(list);
//                        }
//                        photographerList = emcList;
//                        adapter.notifyDataSetChanged();
//
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }

            } else {
                adapter.notifyDataSetChanged();
            }
        } else if (currentTabSelected == 3) {
            if (cameramanList.size() <= 0) {
                if (NetworkUtil.hasConnection(getActivity())) {//有网络连接
                    dialog.show();
                    HttpClient.getInstance().getCameramanList(pageIndex, pageSize, callback);
                } else {     //无网络连接
//                    try {
//                        List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_CAMERAMAN));
//                        if(emcList == null || emcList.size() <= 0) {
//                            handler.sendEmptyMessageDelayed(10, 100);
//                            handler.sendEmptyMessageDelayed(11, 4000);
//                            return;
//                        }
//
//                        for (F4 f4 : emcList){
//                            List<Emcee> list = db.findAll(Selector.from(Emcee.class).where("tag", "=", f4.getPersonName()));
//                            f4.setCameraman(list);
//                        }
//                        cameramanList = emcList;
//                        adapter.notifyDataSetChanged();
//
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
                }
            } else {//
                adapter.notifyDataSetChanged();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10://show
                    mtdialog.show();
                    break;
                case 11://close
                    if (mtdialog != null && mtdialog.isShowing()) mtdialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private Callback<Base<ArrayList<F4>>> callback = new Callback<Base<ArrayList<F4>>>() {

        @Override
        public void success(Base<ArrayList<F4>> base, Response response) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (base.getCode() == 200) {
                try {
                    if (currentTabSelected == 0) {
                        emceeList = base.getData();
                        //1
                        for (F4 f4 : emceeList) {
                            db.delete(Emcee.class, WhereBuilder.b("tag", "=", f4.getPersonName()));
                        }

                        //2
                        db.delete(F4.class, WhereBuilder.b("tag", "=", TAG_EMCEE));

                        //3
                        for (F4 f4 : emceeList) {
                            f4.setTag(TAG_EMCEE);
                            for (Emcee e : f4.getHoster()) {
                                e.setTag(f4.getPersonName());
                            }
                            db.saveAll(f4.getHoster());
                        }

                        //4
                        db.saveAll(emceeList);
                    } else if (currentTabSelected == 1) {
                        dresserList = base.getData();
                        //1 删除画妆师下面的作品
                        for (F4 f4 : dresserList) {
                            db.delete(Dresser.class, WhereBuilder.b("parentName", "=", f4.getPersonName()));
                        }
                        //2 删除画妆师
                        db.delete(F4.class, WhereBuilder.b("tag", "=", TAG_DRESSER));//

                        //3 缓存画妆师的作品
                        for (F4 f4 : dresserList) {
                            f4.setTag(TAG_DRESSER);
                            for (Dresser d : f4.getDresser()) {
                                d.setParentName(f4.getPersonName());
                            }
                            db.saveAll(f4.getDresser());
                        }
                        //4 缓存画妆师
                        db.saveAll(dresserList);

                    } else if (currentTabSelected == 2) {
                        photographerList = base.getData();
                        //1
                        for (F4 f4 : photographerList) {
                            db.delete(Dresser.class, WhereBuilder.b("parentName", "=", f4.getPersonName()));
                        }
                        //2
                        db.delete(F4.class, WhereBuilder.b("tag", "=", TAG_PHOTOGRAPHER));
                        //3
                        for (F4 f4 : photographerList) {
                            f4.setTag(TAG_PHOTOGRAPHER);
                            for (Dresser d : f4.getPhotographer()) {
                                d.setParentName(f4.getPersonName());
                            }
                            db.saveAll(f4.getPhotographer());
                        }
                        //4
                        db.saveAll(photographerList);
                    } else if (currentTabSelected == 3) {
                        cameramanList = base.getData();
                        //1
                        for (F4 f4 : cameramanList) {
                            db.delete(Emcee.class, WhereBuilder.b("tag", "=", f4.getPersonName()));
                        }
                        //2
                        db.delete(F4.class, WhereBuilder.b("tag", "=", TAG_CAMERAMAN));

                        //3
                        for (F4 f4 : cameramanList) {
                            f4.setTag(TAG_CAMERAMAN);
                            for (Emcee e : f4.getCameraman()) {
                                e.setTag(f4.getPersonName());
                            }
                            db.saveAll(f4.getCameraman());
                        }

                        //4
                        db.saveAll(cameramanList);
                    } else {
                        emceeList = base.getData();
                        if (!MTDBUtil.todayChecked(getActivity(), TAG_EMCEE)) {//当天没有缓存过
                            db.delete(F4.class, WhereBuilder.b("tag", "=", TAG_EMCEE));
                            for (F4 f4 : emceeList) {
                                f4.setTag(TAG_EMCEE);
                                for (Emcee e : f4.getHoster()) {
                                    e.setTag(f4.getPersonName());
                                }
                                db.saveOrUpdateAll(f4.getHoster());
                            }
                            db.saveOrUpdateAll(emceeList);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "获取数据失败！");
        }
    };

    @Override
    public void show() {
        if (emceeList == null || emceeList.size() <= 0) {

            if (NetworkUtil.hasConnection(getActivity())) {
                dialog.show();
                HttpClient.getInstance().getEmceeList(pageIndex, pageSize, callback);
            } else {//无网络
                try {
                    List<F4> emcList = db.findAll(Selector.from(F4.class).where("tag", "=", TAG_EMCEE));
                    if (emcList == null || emcList.size() <= 0) {
                        handler.sendEmptyMessageDelayed(10, 1000);
                        handler.sendEmptyMessageDelayed(11, 4000);
                        return;
                    }
                    for (F4 f4 : emcList) {
                        List<Emcee> list = db.findAll(Selector.from(Emcee.class).where("tag", "=", f4.getPersonName()));
                        f4.setHoster(list);
                    }
                    emceeList = emcList;
                    adapter.notifyDataSetChanged();
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void onRecyclerItemClick(View v, int position) {
        S.o("::" + position);
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_f4, parent, false);
            return new EmceeViewHolder(view, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            EmceeViewHolder emceeHolder = (EmceeViewHolder) holder;
            final F4 f4;
            int size = 0;
            if (currentTabSelected == 0) {
                f4 = emceeList.get(position);
                size = f4.getHoster().size();
            } else if (currentTabSelected == 1) {
                f4 = dresserList.get(position);
                size = f4.getDresser().size();
            } else if (currentTabSelected == 2) {
                f4 = photographerList.get(position);
                size = f4.getPhotographer().size();
            } else if (currentTabSelected == 3) {
                f4 = cameramanList.get(position);
                size = f4.getCameraman().size();
            } else {
                f4 = emceeList.get(position);
                size = f4.getHoster().size();
            }


            emceeHolder.nameTxt.setText(f4.getPersonName());//名字
            emceeHolder.descTxt.setText(f4.getDescription());//简介


            Picasso.with(getActivity()).load(f4.getPhotoUrl()).placeholder(R.mipmap.load_error).into(emceeHolder.headImg);

            if (size == 0) {
                emceeHolder.topLayout.setVisibility(View.GONE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);
            } else if (size == 1) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);
                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.INVISIBLE);
                if (currentTabSelected == 0) {
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DensityUtil.getF4ImageSuffix(getActivity()) + "80Q").placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                } else if (currentTabSelected == 1) {
                    Picasso.with(getActivity()).load(f4.getDresser().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    emceeHolder.playImg1.setVisibility(View.GONE);
                } else if (currentTabSelected == 2) {
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    emceeHolder.playImg1.setVisibility(View.GONE);
                } else if (currentTabSelected == 3) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                } else {
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                }
            } else if (size == 2) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.GONE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);
                if (currentTabSelected == 0) {
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                } else if (currentTabSelected == 1) {

                    Picasso.with(getActivity()).load(f4.getDresser().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getDresser().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);
                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                } else if (currentTabSelected == 2) {
                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);

                } else if (currentTabSelected == 3) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                } else {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                }


            } else if (size == 3) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.VISIBLE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);
                emceeHolder.contentImage3.setVisibility(View.VISIBLE);
                emceeHolder.contentImage4.setVisibility(View.INVISIBLE);

                if (currentTabSelected == 0) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getHoster().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);
                } else if (currentTabSelected == 1) {

                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                    emceeHolder.playImg3.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(f4.getDresser().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getDresser().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getDresser().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage3);

                } else if (currentTabSelected == 2) {
                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                    emceeHolder.playImg3.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage3);

                } else if (currentTabSelected == 3) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(f4.getCameraman().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);

                } else {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getHoster().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);
                }

            } else if (size == 4) {
                emceeHolder.topLayout.setVisibility(View.VISIBLE);
                emceeHolder.bottomLayout.setVisibility(View.VISIBLE);

                emceeHolder.contentImage1.setVisibility(View.VISIBLE);
                emceeHolder.contentImage2.setVisibility(View.VISIBLE);
                emceeHolder.contentImage3.setVisibility(View.VISIBLE);
                emceeHolder.contentImage4.setVisibility(View.VISIBLE);

                if (currentTabSelected == 0) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);
                    emceeHolder.playImg4.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getHoster().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);
                    Picasso.with(getActivity()).load(f4.getHoster().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage4);
                } else if (currentTabSelected == 1) {
                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                    emceeHolder.playImg3.setVisibility(View.GONE);
                    emceeHolder.playImg4.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(f4.getDresser().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getDresser().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getDresser().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage3);
                    Picasso.with(getActivity()).load(f4.getDresser().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage4);
                } else if (currentTabSelected == 2) {
                    emceeHolder.playImg1.setVisibility(View.GONE);
                    emceeHolder.playImg2.setVisibility(View.GONE);
                    emceeHolder.playImg3.setVisibility(View.GONE);
                    emceeHolder.playImg4.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage3);
                    Picasso.with(getActivity()).load(f4.getPhotographer().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).into(emceeHolder.contentImage4);
                } else if (currentTabSelected == 3) {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);
                    emceeHolder.playImg4.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);
                    Picasso.with(getActivity()).load(f4.getCameraman().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage4);
                } else {
                    emceeHolder.playImg1.setVisibility(View.VISIBLE);
                    emceeHolder.playImg2.setVisibility(View.VISIBLE);
                    emceeHolder.playImg3.setVisibility(View.VISIBLE);
                    emceeHolder.playImg4.setVisibility(View.VISIBLE);
                    Picasso.with(getActivity()).load(f4.getHoster().get(0).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage1);
                    Picasso.with(getActivity()).load(f4.getHoster().get(1).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage2);
                    Picasso.with(getActivity()).load(f4.getHoster().get(2).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage3);
                    Picasso.with(getActivity()).load(f4.getHoster().get(3).getImageUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.mipmap.load_error).into(emceeHolder.contentImage4);
                }
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
            if(NetworkUtil.hasConnection(getActivity())){
                String p_i[] = view.getTag().toString().split("@");
                int position = Integer.parseInt(p_i[0]);
                int i = Integer.parseInt(p_i[1]) - 1;

                Intent intent = null;
                F4 f4 = null;

                if (currentTabSelected == 0) {
                    f4 = emceeList.get(position);
                    intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra("url", f4.getHoster().get(i).getVideoUrl() + "");
                } else if (currentTabSelected == 1) {
                    f4 = dresserList.get(position);
                    intent = new Intent(getActivity(), WorksDetailActivity.class);
                    intent.putExtra("list", f4.getDresser().get(i).getDetailImages());
                    intent.putExtra("isPlanner", true);

                } else if (currentTabSelected == 2) {
                    f4 = photographerList.get(position);
                    intent = new Intent(getActivity(), WorksDetailActivity.class);
                    intent.putExtra("list", f4.getPhotographer().get(i).getDetailImages());
                    intent.putExtra("isPlanner", true);
                } else if (currentTabSelected == 3) {
                    f4 = cameramanList.get(position);
                    intent = new Intent(getActivity(), VideoActivity.class);
                    intent.putExtra("url", f4.getCameraman().get(i).getVideoUrl() + "");
                }
                animStart(intent);
            }else {
                handler.sendEmptyMessageDelayed(10, 100);
                handler.sendEmptyMessageDelayed(11, 4000);
            }
        }

        @Override
        public int getItemCount() {
            if (currentTabSelected == 0) {
                return emceeList == null ? 0 : emceeList.size();
            } else if (currentTabSelected == 1) {
                return dresserList == null ? 0 : dresserList.size();
            } else if (currentTabSelected == 2) {
                return photographerList == null ? 0 : photographerList.size();
            } else if (currentTabSelected == 3) {
                return cameramanList == null ? 0 : cameramanList.size();
            }
            return 0;
        }

        /**
         *
         */
        public final class EmceeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView headImg;
            TextView nameTxt;
            TextView descTxt;
            LinearLayout root;
            LinearLayout topLayout;
            LinearLayout bottomLayout;

            ImageView contentImage1;
            ImageView contentImage2;
            ImageView contentImage3;
            ImageView contentImage4;

            ImageView playImg1;
            ImageView playImg2;
            ImageView playImg3;
            ImageView playImg4;

            int imageWidth;
            int imageHeight;

            public EmceeViewHolder(View view, OnRecyclerItemClickListener listener) {
                super(view);
                headImg = (ImageView) view.findViewById(R.id.item_f4_head_img);
                nameTxt = (TextView) view.findViewById(R.id.item_f4_name_txt);
                descTxt = (TextView) view.findViewById(R.id.item_f4_description_txt);

                root = (LinearLayout) view.findViewById(R.id.item_f4_root);
                ViewTreeObserver vto = headImg.getViewTreeObserver();
                vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        imageWidth = headImg.getMeasuredWidth();
                        imageHeight = headImg.getMeasuredHeight();
                        headImg.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });

                topLayout = (LinearLayout) view.findViewById(R.id.item_f4_content_top_layout);
                bottomLayout = (LinearLayout) view.findViewById(R.id.item_f4_content_bottom_layout);

                contentImage1 = (ImageView) view.findViewById(R.id.item_f4_content_img1);
                contentImage2 = (ImageView) view.findViewById(R.id.item_f4_content_img2);
                contentImage3 = (ImageView) view.findViewById(R.id.item_f4_content_img3);
                contentImage4 = (ImageView) view.findViewById(R.id.item_f4_content_img4);

                playImg1 = (ImageView) view.findViewById(R.id.item_f4_play_img1);
                playImg2 = (ImageView) view.findViewById(R.id.item_f4_play_img2);
                playImg3 = (ImageView) view.findViewById(R.id.item_f4_play_img3);
                playImg4 = (ImageView) view.findViewById(R.id.item_f4_play_img4);

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
