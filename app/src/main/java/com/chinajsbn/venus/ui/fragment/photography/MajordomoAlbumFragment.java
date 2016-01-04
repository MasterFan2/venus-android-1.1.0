package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Team;
import com.chinajsbn.venus.net.bean.Work;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.TeamWorksDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * majordomo album
 * Created by 13510 on 2015/9/23.
 */
@FragmentFeature(layout = R.layout.fragment_majordomo_album)
public class MajordomoAlbumFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private Team team;

    private String personId = "";

    @Override
    public void initialize() {

        //固定写法。
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        team = (Team) getArguments().getSerializable("team");
        personId = getArguments().getString("personId");
        recyclerView.setAdapter(new MyAdapter(team.getWorkList(), MajordomoAlbumFragment.this));

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void onRecyclerItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), TeamWorksDetailActivity.class);
        intent.putExtra("team", team);
        intent.putExtra("position", position);
        animStart(intent);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private List<Work> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(List<Work> list, OnRecyclerItemClickListener listener){
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView itemView = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_img_suit_detail, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            if(TextUtils.isEmpty(dataList.get(position).getContentUrl())){
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            }else{
                Picasso.with(getActivity()).load(dataList.get(position).getContentUrl() + DimenUtil.getHorizontal() ).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.contentImg);
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView contentImg;
            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(ImageView imgView, OnRecyclerItemClickListener listener) {
                super(imgView);
                contentImg = imgView;
//                contentImg = (ImageView) itemView.findViewById(R.id.item_simple_pic_img);
                onRecyclerItemClickListener = listener;
                contentImg.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                 onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }
}
