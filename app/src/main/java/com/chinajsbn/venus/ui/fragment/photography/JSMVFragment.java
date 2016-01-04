package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Film;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.plan.VideoActivity;
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

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/11/26.
 */
@FragmentFeature(layout = R.layout.fragment_microfilm)
public class JSMVFragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener {

    ///used by cache
    private final String TAG = "JSMVFragment_tag_film";
    private DbUtils db;
    private MTDialog dialog;

    ///
    private int pageIndex = 1;
    private int pageSize  = 30;
    private boolean isNextPage = false;
    private String order = "date"; // date hint
    private String type  = "1";//

    private List<Film> dataList;
    private MyAdapter adapter;

    @ViewInject(R.id.recyclerView)
    XRecyclerView recyclerView;

    private boolean isRefresh = false;// pull down refresh

    @Override
    public void initialize() {

        S.o("::::::::JSMVFragment:::::::");

        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        //init dailog
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(this)
                .create();


//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(JSMVFragment.this);
        recyclerView.setAdapter(adapter);

        recyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        recyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                HttpClient.getInstance().filmList(order, type, pageIndex, pageSize, cb);
                isNextPage = false;
                isRefresh = true;
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                isNextPage = true;
                HttpClient.getInstance().filmList(order, type, pageIndex, pageSize, cb);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10://show
                    dialog.show();
                    break;
                case 11://close
                    if (dialog != null && dialog.isShowing()) dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    Callback<Base<ArrayList<Film>>> cb = new Callback<Base<ArrayList<Film>>>() {
        @Override
        public void success(Base<ArrayList<Film>> arrayListBase, Response response) {
            if(isRefresh){
                recyclerView.refreshComplete();
                isRefresh = false;
            }
            if(arrayListBase.getCode() == 200){
                if(arrayListBase.getData() != null && arrayListBase.getData().size() >= pageSize){
                    recyclerView.setLoadingMoreEnabled(true);
                }else{
                    recyclerView.setLoadingMoreEnabled(false);
                }

                if(isNextPage){
                    dataList.addAll(arrayListBase.getData());
                    for (Film film: arrayListBase.getData()){
                        film.setTag(TAG);
                        film.setImgUrl(film.getCoverImage().getImageUrl());
                    }
                    try {
                        db.saveAll(arrayListBase.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }else{//refresh first page
                    dataList = arrayListBase.getData();
                    try {
                        db.delete(Film.class, WhereBuilder.b("tag", "=", TAG));
                        for (Film film: dataList){
                            film.setTag(TAG);
                            film.setImgUrl(film.getCoverImage().getImageUrl());
                        }
                        db.saveAll(dataList);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if(isRefresh){
                recyclerView.refreshComplete();
                isRefresh = false;
            }
        }
    };

    @Override
    public void onClick(MTDialog dialog, View view) {
        dialog.dismiss();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.FilmHolder>{

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener l){
            this.onRecyclerItemClickListener = l;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FilmHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_film, parent, false), onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            Film film = dataList .get(position);
            if(film.getCoverImage() != null && !TextUtils.isEmpty(film.getCoverImage().getImageUrl()))
                Picasso.with(getActivity()).load(film.getCoverImage().getImageUrl()+"@900h_600h").placeholder(R.drawable.loading).into(holder.coverImg);
            else
                Picasso.with(getActivity()).load(film.getImgUrl()+"@900h_600h").placeholder(R.drawable.loading).into(holder.coverImg);
            holder.nameTxt.setText(film.getName());
            holder.hintTxt.setText("浏览量:" + film.getHits());
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }


        class FilmHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView coverImg;
            TextView nameTxt;
            TextView  hintTxt;

            public FilmHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                coverImg = (ImageView) itemView.findViewById(R.id.item_film_content_img);
                nameTxt  = (TextView) itemView.findViewById(R.id.item_film_name_txt);
                hintTxt  = (TextView) itemView.findViewById(R.id.item_film_hint_txt);
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
        if(NetworkUtil.hasConnection(getActivity())){
            HttpClient.getInstance().filmList(order, type, pageIndex , pageSize, cb);
        }else {
            recyclerView.setPullRefreshEnabled(false);
            try {
                dataList = db.findAll(Selector.from(Film.class).where("tag", "=", TAG));
                adapter.notifyDataSetChanged();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void onRecyclerItemClick(View v, int position) {
        if(NetworkUtil.hasConnection(getActivity())){
            Intent intent = new Intent(getActivity(), VideoActivity.class);
            intent.putExtra("url", dataList.get(position -1).getUrl());
            animStart(intent);
        }else{
            handler.sendEmptyMessageDelayed(10, 10);
            handler.sendEmptyMessageDelayed(11, 3000);
        }
    }
}
