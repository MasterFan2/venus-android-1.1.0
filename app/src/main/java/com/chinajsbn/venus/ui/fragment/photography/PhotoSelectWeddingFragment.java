package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.WeddingDress;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.WeddingDressDetailActivity;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 选婚纱
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_sel_wedding)
public class PhotoSelectWeddingFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private ArrayList<WeddingDress> data;

    //传递过来的数据
    private String id;

    @Override
    public void initialize() {
        //固定写法。
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //获取数据
//        id = getArguments().getString("id");
//
//        HttpClient.getInstance().getWeddingDressList(id, 1, 10, cb);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    /**
     * item点击事件
     *
     * @param v
     * @param position
     */
    @Override
    public void onRecyclerItemClick(View v, int position) {
        //SimpleDetailResp
        Intent intent = new Intent(getActivity(), WeddingDressDetailActivity.class);
        intent.putExtra("idAndDetail", id+"@" + data.get(position).getContentId());
        getActivity().startActivity(intent);
    }

    private Callback<Base<ArrayList<WeddingDress>>> cb = new Callback<Base<ArrayList<WeddingDress>>>() {
        @Override
        public void success(Base<ArrayList<WeddingDress>> weddingDress, Response response) {
            T.s(getActivity(), "Get data success ! datasiz => " + weddingDress.getData().size());
            data = weddingDress.getData();
            recyclerView.setAdapter(new MyAdapter(weddingDress.getData(), PhotoSelectWeddingFragment.this));
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "Get data error! ");
        }
    };

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private ArrayList<WeddingDress> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(ArrayList<WeddingDress> list, OnRecyclerItemClickListener listener){
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weddingdress_layout, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, final int position) {

            holder.nameTxt.setText(dataList.get(position).getContentName());

            if(TextUtils.isEmpty(dataList.get(position).getContentUrl())){
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            }else{
                Picasso.with(getActivity()).load(dataList.get(position).getContentUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.contentImg);
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView nameTxt;
            ImageView contentImg;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                nameTxt    = (TextView) itemView.findViewById(R.id.item_weddingdress_txt);
                contentImg = (ImageView) itemView.findViewById(R.id.item_weddingdress_img);

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
