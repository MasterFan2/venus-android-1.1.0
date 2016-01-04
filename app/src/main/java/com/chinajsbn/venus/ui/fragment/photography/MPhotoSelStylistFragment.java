package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.MajordomoDetailActivity;
import com.chinajsbn.venus.ui.photography.WorksDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.picasso.Picasso;
import com.tool.widget.CircleImageView;
import com.tool.widget.MyGridView;
import com.tool.widget.MyListView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_sel_mstylist)
public class MPhotoSelStylistFragment extends BaseFragment implements OnRecyclerItemClickListener {

    @ViewInject(R.id.select_stylist_gridView)
    private MyGridView gridView;
    private MyGridAdapter majordomoAdapter;

    @ViewInject(R.id.select_stylist_listView)
    private MyListView listView;
    private MyListAdapter seniorAdapter;



    private ArrayList<Photographer> majordomoData;
    private ArrayList<Photographer> seniorData;

    @Override
    public void initialize() {

        majordomoAdapter = new MyGridAdapter();
        gridView .setAdapter(majordomoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                intent.putExtra("personId", majordomoData.get(i).getPersonId() + "");
                intent.putExtra("headUrl", majordomoData.get(i).getPhotoUrl());
                intent.putExtra("isStylist", true);
                animStart(intent);
            }
        });

        seniorAdapter = new MyListAdapter();
        listView.setAdapter(seniorAdapter);

        //获取总监数据
        HttpClient.getInstance().getStylist(1, 1, 10, majordomoCallback);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    /**
     * 契约
     * item点击事件
     *
     * @param v
     * @param position
     */
    @Override
    public void onRecyclerItemClick(View v, int position) {
        T.s(getActivity(), "item " + position + "click.");
    }


    private Callback<Base<ArrayList<Photographer>>> majordomoCallback = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(final Base<ArrayList<Photographer>> photographers, Response response) {
            majordomoData = photographers.getData();
            majordomoAdapter.notifyDataSetChanged();

            //获取资深造型师数据
            HttpClient.getInstance().getStylist(2, 1, 20, seniorCallback);
        }// success end

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "Get majordomoData error! ");
        }
    };

    private Callback<Base<ArrayList<Photographer>>> seniorCallback = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(final Base<ArrayList<Photographer>> photographers, Response response) {
            seniorData = photographers.getData();
            seniorAdapter.notifyDataSetChanged();
        }// success end

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "Get stylist error! ");
        }
    };

    /**
     * 总监
     */
    class MyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return majordomoData == null ? 0 : majordomoData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_majordomo_layout, null);
                holder.headImg = (CircleImageView) view.findViewById(R.id.majordomo_head_img);
                holder.nameTxt = (TextView) view.findViewById(R.id.majordomo_name_txt);
                holder.worksTxt = (TextView) view.findViewById(R.id.majordomo_works_txt);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            //set data
            holder.nameTxt.setText(majordomoData.get(i).getPersonName());
            Picasso.with(getActivity()).load(majordomoData.get(i).getPhotoUrl()).into(holder.headImg);
            holder.worksTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                    intent.putExtra("personId", majordomoData.get(i).getPersonId() + "");
                    intent.putExtra("headUrl", majordomoData.get(i).getPhotoUrl());
                    intent.putExtra("isStylist", true);
                    animStart(intent);
                }
            });
            //
            return view;
        }

        class ViewHolder {
            TextView nameTxt;
            CircleImageView headImg;
            TextView worksTxt;
        }
    }

    class MyListAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return seniorData == null ? 0 : seniorData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            final Photographer stylist = seniorData.get(i);
            if(view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_senior_layout, null);
                holder = new ViewHolder();
                holder.headImg  = (ImageView) view.findViewById(R.id.item_senior_head_img);
                holder.worksTxt = (TextView) view.findViewById(R.id.item_senior_works_btn);
                holder.nameTxt  = (TextView) view.findViewById(R.id.item_senior_name_txt);

//                holder.gridView = (MyGridView) view.findViewById(R.id.item_senior_gridView);

                holder.topLayout    = (LinearLayout) view.findViewById(R.id.item_senior_content_top_layout);
                holder.bottomLayout = (LinearLayout) view.findViewById(R.id.item_senior_content_bottom_layout);

                holder.contentImage1 = (ImageView) view.findViewById(R.id.item_senior_content_img1);
                holder.contentImage2 = (ImageView) view.findViewById(R.id.item_senior_content_img2);
                holder.contentImage3 = (ImageView) view.findViewById(R.id.item_senior_content_img3);
                holder.contentImage4 = (ImageView) view.findViewById(R.id.item_senior_content_img4);

                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            //
            Picasso.with(getActivity()).load(stylist.getPhotoUrl()).into(holder.headImg);
            holder.nameTxt.setText(stylist.getPersonName());
//            holder.gridView.setAdapter(new InternalAdapter(stylist.getList()));
            final  int contentSize = stylist.getList() == null ? 0 : stylist.getList().size();
            if(contentSize ==  0){
                holder.topLayout.setVisibility(View.GONE);
                holder.bottomLayout.setVisibility(View.GONE);
            }else if(contentSize ==  1){
                holder.topLayout.setVisibility(View.VISIBLE);
                holder.bottomLayout.setVisibility(View.GONE);
                holder.contentImage1.setVisibility(View.VISIBLE);
                holder.contentImage2.setVisibility(View.GONE);

                Picasso.with(getActivity()).load(stylist.getList().get(0).getContentUrl() + DimenUtil.getVertical()).into(holder.contentImage1);
            }else if(contentSize ==  2){
                holder.topLayout.setVisibility(View.VISIBLE);
                holder.bottomLayout.setVisibility(View.GONE);
                holder.contentImage1.setVisibility(View.VISIBLE);
                holder.contentImage2.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(stylist.getList().get(0).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage1);
                Picasso.with(getActivity()).load(stylist.getList().get(1).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage2);
            }else if(contentSize ==  3){
                holder.topLayout.setVisibility(View.VISIBLE);
                holder.bottomLayout.setVisibility(View.VISIBLE);
                holder.contentImage4.setVisibility(View.GONE);

                holder.contentImage1.setVisibility(View.VISIBLE);
                holder.contentImage2.setVisibility(View.VISIBLE);
                holder.contentImage3.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(stylist.getList().get(0).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage1);
                Picasso.with(getActivity()).load(stylist.getList().get(1).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage2);
                Picasso.with(getActivity()).load(stylist.getList().get(2).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage3);
            }else if(contentSize ==  4){
                holder.topLayout.setVisibility(View.VISIBLE);
                holder.bottomLayout.setVisibility(View.VISIBLE);

                holder.contentImage1.setVisibility(View.VISIBLE);
                holder.contentImage2.setVisibility(View.VISIBLE);
                holder.contentImage3.setVisibility(View.VISIBLE);
                holder.contentImage4.setVisibility(View.VISIBLE);

                Picasso.with(getActivity()).load(stylist.getList().get(0).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage1);
                Picasso.with(getActivity()).load(stylist.getList().get(1).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage2);
                Picasso.with(getActivity()).load(stylist.getList().get(2).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage3);
                Picasso.with(getActivity()).load(stylist.getList().get(3).getContentUrl()+ DimenUtil.getVertical()).into(holder.contentImage4);


            }else {
                holder.topLayout.setVisibility(View.VISIBLE);
                holder.bottomLayout.setVisibility(View.VISIBLE);

                holder.contentImage1.setVisibility(View.VISIBLE);
                holder.contentImage2.setVisibility(View.VISIBLE);
                holder.contentImage3.setVisibility(View.VISIBLE);
                holder.contentImage4.setVisibility(View.VISIBLE);


                Picasso.with(getActivity()).load(stylist.getList().get(0).getContentUrl()).into(holder.contentImage1);
                Picasso.with(getActivity()).load(stylist.getList().get(1).getContentUrl()).into(holder.contentImage2);
                Picasso.with(getActivity()).load(stylist.getList().get(2).getContentUrl()).into(holder.contentImage3);
                Picasso.with(getActivity()).load(stylist.getList().get(3).getContentUrl()).into(holder.contentImage4);
            }
            //

            holder.contentImage1.setTag(i + "@" + 1);
            holder.contentImage2.setTag(i + "@" + 2);
            holder.contentImage3.setTag(i + "@" + 3);
            holder.contentImage4.setTag(i + "@" + 4);

            holder.contentImage1.setOnClickListener(this);
            holder.contentImage2.setOnClickListener(this);
            holder.contentImage3.setOnClickListener(this);
            holder.contentImage4.setOnClickListener(this);
            return view;
        }

        @Override
        public void onClick(View view) {
            String p_i[] = view.getTag().toString().split("@");
            int position = Integer.parseInt(p_i[0]);
            int i = Integer.parseInt(p_i[1]) -1;

            final Photographer stylist = seniorData.get(position);
            Intent intent = new Intent(getActivity(), WorksDetailActivity.class);
            intent.putExtra("personId", stylist.getPersonId()+"");
            intent.putExtra("worksId", stylist.getList().get(i).getContentId());
            animStart(intent);
        }

        class ViewHolder {
            ImageView headImg;
            TextView  nameTxt;
            TextView worksTxt;
            LinearLayout topLayout;
            LinearLayout bottomLayout;
            ImageView contentImage1;
            ImageView contentImage2;
            ImageView contentImage3;
            ImageView contentImage4;

            MyGridView gridView;
        }
    }
}
