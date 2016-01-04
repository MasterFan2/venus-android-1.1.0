package com.chinajsbn.venus.ui.microfilm.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
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
import com.chinajsbn.venus.net.bean.Brands;
import com.chinajsbn.venus.net.bean.CoverImage;
import com.chinajsbn.venus.net.bean.Film;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.plan.VideoActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.MTDBUtil;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.xrecyclerview.ProgressStyle;
import com.tool.widget.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/11/26.
 */
@FragmentFeature(layout = R.layout.fragment_microfilm)
public class MicrofilmFragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener {

    ///used by cache
    public static final String TAG = "MicrofilmFragment_tag_microfilm";//
    private final String tag = "microfilm";
    private DbUtils db;

    ///page
    private int pageSize = 10;
    private int pageIndex = 1;
    private String videoType = "5";//微电影.
    private boolean isNextPage = false;

    ///////////////////Dialog///////////////////
    private MTDialog networkDialog;//无网络提示

    private List<Film> dataList;
    private MyAdapter adapter;

    @ViewInject(R.id.recyclerView)
    XRecyclerView recyclerView;

    private boolean isRefresh = false;// pull down refresh

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
                .setOnClickListener(MicrofilmFragment.this)
                .create();


//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(MicrofilmFragment.this);
        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setArrowImageView(R.mipmap.ic_arrow_right);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                HttpClient.getInstance().filmList("date", videoType, pageIndex, pageSize, cb);
                isRefresh = true;
                isNextPage = false;
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                isNextPage = true;
                HttpClient.getInstance().filmList("date", videoType, pageIndex, pageSize, cb);
            }
        });

        if (NetworkUtil.hasConnection(getActivity())) {
            HttpClient.getInstance().filmList("date", videoType, pageIndex, pageSize, cb);
        } else {
            try {
                dataList = db.findAll(Selector.from(Film.class).where("tag", "=", tag));
                if (dataList != null && dataList.size() > 0) {
                    adapter.notifyDataSetChanged();
                } else {
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

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.FilmHolder> {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener l) {
            this.onRecyclerItemClickListener = l;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FilmHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_film, parent, false), onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            Film film = dataList.get(position);
            if (film.getCoverImage() != null)
                Picasso.with(getActivity()).load(film.getCoverImage().getImageUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.coverImg);
            else if(!TextUtils.isEmpty(film.getImgUrl())){
                Picasso.with(getActivity()).load(film.getImgUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).placeholder(R.drawable.loading).into(holder.coverImg);
            }
            holder.nameTxt.setText(film.getName());
            holder.hintTxt.setText("浏览:" + film.getHits());
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }


        class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView coverImg;
            TextView nameTxt;
            TextView hintTxt;

            public FilmHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                coverImg = (ImageView) itemView.findViewById(R.id.item_film_content_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_film_name_txt);
                hintTxt = (TextView) itemView.findViewById(R.id.item_film_hint_txt);
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

    Callback<Base<ArrayList<Film>>> cb = new Callback<Base<ArrayList<Film>>>() {
        @Override
        public void success(Base<ArrayList<Film>> arrayListBase, Response response) {
            if (isRefresh) {
                recyclerView.refreshComplete();
                isRefresh = false;
            }
            if (arrayListBase.getCode() == 200) {
                if (!isNextPage) {//第一页
                    dataList = arrayListBase.getData();
                    if (!MTDBUtil.todayChecked(getActivity(), TAG)) {
                        S.o(":::微电影 TAG=" + TAG + "缓存");
                        try {
                            for (Film film : dataList) {
                                film.setTag(tag);
                                film.setImgUrl(film.getCoverImage().getImageUrl());
                            }
                            db.delete(Film.class, WhereBuilder.b("tag", "=", tag));
                            db.saveAll(dataList);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    } else {
                        S.o(":::微电影 TAG=" + TAG + "已经缓存过了");
                    }
                } else {//下一页
                    dataList.addAll(arrayListBase.getData());
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (isRefresh) {
                recyclerView.refreshComplete();
                isRefresh = false;
            }
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        intent.putExtra("url", dataList.get(position - 1).getUrl());
        animStart(intent);
    }
}
