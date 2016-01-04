package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Context;
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
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.net.bean.PhotographerWorks;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.recyclerpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_sel_stylist)
public class PhotoSelStylistFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.recyclerViewPager)
    private RecyclerViewPager viewPager;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    private View[] views;

    private View parentView;

    private ArrayList<Photographer> data;
    private int currentHeadSelect = 0;

    //
    private int width;
    private int height;

    @Override
    public void initialize() {

        //固定写法。
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(layout);
        viewPager.setHasFixedSize(true);
        viewPager.setLongClickable(true);

        viewPager.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rView, int scrollState) {
                //updateState(scrollState);
                int position = viewPager.getCurrentPosition();

                if (scrollState == RecyclerViewPager.SCROLL_STATE_IDLE && currentHeadSelect != position) {
                    currentHeadSelect = position;
                    recyclerView.setAdapter(new MyAdapter(data.get(position).getList(), PhotoSelStylistFragment.this));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerView.getFirstVisiblePosition());
                int childCount = viewPager.getChildCount();
                int width = viewPager.getChildAt(0).getWidth();
                int padding = (viewPager.getWidth() - width) / 2;
//                mCountText.setText("Count: " + childCount);


                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }
            }
        });

        viewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (viewPager.getChildCount() < 3) {
                    if (viewPager.getChildAt(1) != null) {
                        if (viewPager.getCurrentPosition() == 0) {
                            View v1 = viewPager.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        } else {
                            View v1 = viewPager.getChildAt(0);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (viewPager.getChildAt(0) != null) {
                        View v0 = viewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (viewPager.getChildAt(2) != null) {
                        View v2 = viewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }
            }
        });
//        recyclerView.setAdapter(new MyAdapter(null, PhotoSelPhotographerFragment.this));
        //获取数据
        HttpClient.getInstance().getStylist(1, 1, 10, cb);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }
//
//    @OnClick(R.id.fab)
//    public void click(View view){
//        animStart(PersonalActivity.class);
//    }

    /**
     * 契约
     * item点击事件
     *
     * @param v
     * @param position
     */
    @Override
    public void onRecyclerItemClick(View v, int position) {
        //SimpleDetailResp
//        Intent intent = new Intent(getActivity(), StylistDetailActivity.class);
//        intent.putExtra("id", data.get(position).getPersonId());
//        getActivity().startActivity(intent);
        T.s(getActivity(), "item " + position + "click.");
    }



    private Callback<Base<ArrayList<Photographer>>> cb = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(final Base<ArrayList<Photographer>> photographers, Response response) {

            data = photographers.getData();

            data = photographers.getData();
            viewPager.setAdapter(new LayoutAdapter(getActivity(), viewPager, data));

        }// success end

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "Get data error! ");
        }
    };

    public class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.SimpleViewHolder> {

        private final Context mContext;
        private final RecyclerView mRecyclerView;
        private int mCurrentItemId = 0;
        private ArrayList<Photographer> currentData;

        public class SimpleViewHolder extends RecyclerView.ViewHolder {

            public TextView nameTxt;
            public TextView experienceTxt;
            public TextView levelTxt;
            public TextView ownedCompanyTxt;
            public ImageView headImg;

            public SimpleViewHolder(View view) {
                super(view);
                nameTxt         = (TextView) view.findViewById(R.id.pager_name_txt);
                experienceTxt   = (TextView) view.findViewById(R.id.pager_level_txt);
                levelTxt        = (TextView) view.findViewById(R.id.pager_experience_txt);
                ownedCompanyTxt = (TextView) view.findViewById(R.id.pager_own_txt);
                headImg         = (ImageView) view.findViewById(R.id.pager_img);
            }
        }

        public LayoutAdapter(Context context, RecyclerView recyclerView, ArrayList<Photographer> d) {
            currentData     = d;
            mContext        = context;
            mRecyclerView   = recyclerView;
        }

        public void addItem(int position) {
            final int id = mCurrentItemId++;
            notifyItemInserted(position);
        }

        public void removeItem(int position) {
            notifyItemRemoved(position);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_photographer_page, parent, false);

            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            final Photographer photographer = currentData .get(position);

            holder.nameTxt.setText(photographer.getPersonName());
            holder.experienceTxt.setText(photographer.getExperience()+ "年");
            holder.levelTxt.setText(photographer.getLevel());
            holder.ownedCompanyTxt.setText(photographer.getOwnedCompany());

            if(photographer.getPhotoUrl() != null && photographer.getPhotoUrl().length() > 10){
                Picasso.with(getActivity()).load(photographer.getPhotoUrl()).into(holder.headImg);
            }else{
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.headImg);
            }
        }

        @Override
        public int getItemCount() {
            return currentData.size();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private List<PhotographerWorks> dataList;
        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(List<PhotographerWorks> list, OnRecyclerItemClickListener listener) {
            this.dataList = list;
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_works_layout, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, final int position) {
            holder.nameTxt.setText(dataList.get(position).getContentName());

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
            ImageView contentImg;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);
                nameTxt = (TextView) itemView.findViewById(R.id.item_work_name_txt);
                contentImg = (ImageView) itemView.findViewById(R.id.item_works_img);

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
