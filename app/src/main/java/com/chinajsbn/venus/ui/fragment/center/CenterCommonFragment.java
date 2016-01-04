package com.chinajsbn.venus.ui.fragment.center;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * create by master fan on 2015/8/17
 */
@FragmentFeature(layout = R.layout.fragment_center_common)
public class CenterCommonFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    public static CenterCommonFragment newInstance(String param1, String param2) {
        CenterCommonFragment fragment = new CenterCommonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initialize() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //

        int position = getArguments().getInt("position");

        recyclerView.setAdapter(new MyAdapter(null, CenterCommonFragment.this));
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private ArrayList<Simple> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(ArrayList<Simple> list, OnRecyclerItemClickListener listener){
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = null;
            if(viewType == 0){
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pcenter_right, parent, false);
            }else if(viewType == 1){
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pcenter_left, parent, false);
            }

            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
//            holder.idTxt.setText(dataList.get(position).getContentId());
//            holder.nameTxt.setText(dataList.get(position).getContentName());
//            if(TextUtils.isEmpty(dataList.get(position).getContentUrl())){
//                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
//            }else{
//                Picasso.with(getActivity()).load(dataList.get(position).getContentUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.contentImg);
//            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position % 2 == 0)
                return 0;
            else
                return 1;
        }

        @Override
        public int getItemCount() {
            return 5;//dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView idTxt;
            TextView nameTxt;
            ImageView contentImg;
            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
//                idTxt = (TextView) itemView.findViewById(R.id.item_simple_id_txt);
//                nameTxt = (TextView) itemView.findViewById(R.id.item_simple_id_txt);
//                contentImg = (ImageView) itemView.findViewById(R.id.item_simple_pic_img);
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
