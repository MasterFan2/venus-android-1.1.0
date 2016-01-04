package com.chinajsbn.venus.ui.fragment.photography;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.chinajsbn.venus.net.bean.Custom;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.MultiViewPager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_customer)
public class PhotoCustomerFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.viewPager)
    private MultiViewPager viewPager;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private SubModule daily;
    private SubModule list;

    @Override
    public void initialize() {

        SubModule subModule = (SubModule) getArguments().getSerializable("subModule");
        if (subModule.isType()) {
            daily = subModule.getSubModule().get(0);
            list = subModule.getSubModule().get(1);
        }

        //固定写法。
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        //get list
        HttpClient.getInstance().getCustomList(daily.getContentId(), 1, 10, listCallback);

        //get daily
//        HttpClient.getInstance().getCustomList(daily.getContentId(), 1, 10, dailyCallback);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    Callback<Base<ArrayList<Simple>>> dailyCallback = new Callback<Base<ArrayList<Simple>>>() {
        @Override
        public void success(final Base<ArrayList<Simple>> simpleCustom, Response response) {

            System.out.println("Success");

            final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
                @Override
                public int getCount() {
                    return simpleCustom == null ? 0 : simpleCustom.getData().size();
                }

                @Override
                public Fragment getItem(int position) {
                    PhotographerPagerFragment pagerFragment = new PhotographerPagerFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("data", simpleCustom.getData().get(position));
                    pagerFragment.setArguments(args);
                    return pagerFragment;
                }
            };
            viewPager.setAdapter(adapter);
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("ERR");
        }
    };

    Callback<Base<List<Custom>>> listCallback = new Callback<Base<List<Custom>>>() {
        @Override
        public void success(Base<List<Custom>> simpleCustom, Response response) {
            System.out.println("Success");
            recyclerView.setAdapter(new MyAdapter(simpleCustom.getData(), PhotoCustomerFragment.this));
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("ERR");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

//    /**
//     * personal center
//     *
//     * @param view
//     */
//    @OnClick(R.id.fab)
//    public void click(View view) {
//        animStart(PersonalActivity.class);
//    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private List<Custom> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(List<Custom> list, OnRecyclerItemClickListener listener) {
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_recycler_layout, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, final int position) {
            holder.nameTxt.setText(dataList.get(position).getContentName());
            holder.unameTxt.setText(dataList.get(position).getActorNameFemale() + " & " + dataList.get(position).getActorNameMale());

            if (TextUtils.isEmpty(dataList.get(position).getContentUrl())) {
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            } else {
                Picasso.with(getActivity()).load(dataList.get(position).getContentUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.contentImg);
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView nameTxt;
            TextView unameTxt;
            ImageView contentImg;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                nameTxt = (TextView) itemView.findViewById(R.id.item_custom_name_txt);
                unameTxt = (TextView) itemView.findViewById(R.id.item_custom_uname_txt);
                contentImg = (ImageView) itemView.findViewById(R.id.item_custom_img);

                onRecyclerItemClickListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener != null)
                    onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }


}
