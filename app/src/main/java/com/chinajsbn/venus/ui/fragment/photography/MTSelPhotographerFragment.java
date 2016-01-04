package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.net.bean.DoublePhotographer;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.net.bean.PhotographerWorks;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.MBaseFragment;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.MajordomoDetailActivity;
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

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_mt_sel_photographer)
public class MTSelPhotographerFragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener {

    public static final String TAG_PHOTO_MAJORDOME = "photo_majordomo";
    public static final String TAG_PHOTO_SENIOR = "photo_senior";

    private DbUtils db;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

    private ArrayList<DoublePhotographer> finaData = new ArrayList<>();

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_mt_sel_photographer, container, false);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        initialize();
//        return view;
//    }

    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(MTSelPhotographerFragment.this)
                .create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(R.color.gray_400)).build());

        adapter = new MyAdapter(MTSelPhotographerFragment.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void show() {
        S.o("《《获取摄影师总监数据");
        //获取总监数据
        if (NetworkUtil.hasConnection(getActivity())) {
            dialog.show();
            HttpClient.getInstance().getPhotographer(1, 1, 20, majordomoCallback);
        } else {//获取本地数据
            try {
                List<Photographer> listMajordomo = db.findAll(Selector.from(Photographer.class).where("tag", "=", MTDBUtil.PHOTO_MAJORDOMO));
                List<Photographer> listSenior = db.findAll(Selector.from(Photographer.class).where("tag", "=", MTDBUtil.PHOTO_SENIOR));

                if (listMajordomo != null && listMajordomo.size() > 0) {
                    finaData.add(new DoublePhotographer("majordomo_tag", null, null));
                    final int size = listMajordomo.size();

                    if (size == 1) {
                        finaData.add(new DoublePhotographer("majordomo", listMajordomo.get(0), null));
                    } else if (size == 2) {
                        finaData.add(new DoublePhotographer("majordomo", listMajordomo.get(0), listMajordomo.get(1)));
                    } else {
                        for (int i = 0; i < size; ) {
                            if (i + 1 < size) {
                                Photographer photographer1 = listMajordomo.get(i++);
                                Photographer photographer2 = listMajordomo.get(i++);

                                finaData.add(new DoublePhotographer("majordomo", photographer1, photographer2));

                            } else if (i < size) {
                                Photographer photographer1 = listMajordomo.get(i++);
                                finaData.add(new DoublePhotographer("majordomo", photographer1, null));
                            }
                        }
                    }
                }

                //添加资深tag
                final int size = listSenior == null ? 0 : listSenior.size();
                if (size > 0) {
                    finaData.add(new DoublePhotographer("senior_tag", null, null));
                    for (int i = 0; i < size; i++) {
                        Photographer photographer1 = listSenior.get(i);
                        List<PhotographerWorks> localWorkses = db.findAll(Selector.from(PhotographerWorks.class).where("pName", "=", photographer1.getPersonName()));
                        photographer1.setList(localWorkses);
                        finaData.add(new DoublePhotographer("senior", photographer1, null));
                    }
                }

                //刷新显示
                adapter.notifyDataSetChanged();

                //
                if (listMajordomo == null || listSenior == null) {
                    handler.sendEmptyMessageDelayed(10, 1000);
                    handler.sendEmptyMessageDelayed(11, 4000);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            if (dialog != null && dialog.isShowing()) dialog.dismiss();
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
                    if (mtdialog != null && mtdialog.isShowing()) mtdialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void hide() {

    }

    /**
     * 总监
     */
    private Callback<Base<ArrayList<Photographer>>> majordomoCallback = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(final Base<ArrayList<Photographer>> photographers, Response response) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            //添加总监tag
            finaData.add(new DoublePhotographer("majordomo_tag", null, null));

            List<Photographer> tempMajordomo = new ArrayList<>();//保存数据库的列表

            if (photographers.getCode() == 200) {
                //添加总监数据
                if (photographers.getData() != null && photographers.getData().size() > 0) {
                    final int size = photographers.getData().size();

                    if (size == 1) {
                        Photographer photographer1 = photographers.getData().get(0);
                        photographer1.setTag(MTDBUtil.PHOTO_MAJORDOMO);
                        finaData.add(new DoublePhotographer("majordomo", photographer1, null));
                        tempMajordomo.add(photographer1);
                    } else if (size == 2) {

                        Photographer photographer1 = photographers.getData().get(1);
                        photographer1.setTag(MTDBUtil.PHOTO_MAJORDOMO);
                        Photographer photographer2 = photographers.getData().get(1);
                        photographer2.setTag(MTDBUtil.PHOTO_MAJORDOMO);
                        finaData.add(new DoublePhotographer("majordomo", photographer1, photographer2));

                        tempMajordomo.add(photographer1);
                        tempMajordomo.add(photographer2);
                    } else {
                        for (int i = 0; i < size; ) {
                            if (i + 1 < size) {
                                Photographer photographer1 = photographers.getData().get(i++);
                                photographer1.setTag(MTDBUtil.PHOTO_MAJORDOMO);
                                Photographer photographer2 = photographers.getData().get(i++);
                                photographer2.setTag(MTDBUtil.PHOTO_MAJORDOMO);

                                finaData.add(new DoublePhotographer("majordomo", photographer1, photographer2));
                                tempMajordomo.add(photographer1);
                                tempMajordomo.add(photographer2);

                            } else if (i < size) {
                                Photographer photographer1 = photographers.getData().get(i++);
                                photographer1.setTag(MTDBUtil.PHOTO_MAJORDOMO);
                                finaData.add(new DoublePhotographer("majordomo", photographer1, null));
                                tempMajordomo.add(photographer1);
                            }
                        }
                    }
                }

                //
                adapter.notifyDataSetChanged();
                dialog.show();
                try {
                    if (!MTDBUtil.todayChecked(getActivity(), MTSelPhotographerFragment.TAG_PHOTO_MAJORDOME)) {
                        db.delete(Photographer.class, WhereBuilder.b("tag", "=", MTDBUtil.PHOTO_MAJORDOMO));
                        Cache cache = new Cache(MTSelPhotographerFragment.TAG_PHOTO_MAJORDOME, MTDBUtil.getToday());
                        db.saveOrUpdate(cache);
                        db.saveOrUpdateAll(tempMajordomo);
                    } else {
                        S.o(":::今天已经缓存过[[" + MTSelPhotographerFragment.TAG_PHOTO_MAJORDOME);
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }
                HttpClient.getInstance().getPhotographer(2, 1, 20, seniorCallback);
            }
        }// success end

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "Get majordomo error! ");
        }
    };

    /**
     * 资深
     */
    private Callback<Base<ArrayList<Photographer>>> seniorCallback = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(final Base<ArrayList<Photographer>> photographers, Response response) {

            if (dialog != null && dialog.isShowing()) dialog.dismiss();

            //添加资深tag
            finaData.add(new DoublePhotographer("senior_tag", null, null));
            if (photographers.getCode() == 200) {
                List<Photographer> tempSenior = new ArrayList<>();//保存数据库的列表
                final int size = photographers.getData() == null ? 0 : photographers.getData().size();
                if (size > 0) {
                    try {
                        for (int i = 0; i < size; i++) {
                            Photographer photographer1 = photographers.getData().get(i);
                            photographer1.setTag(MTDBUtil.PHOTO_SENIOR);
                            finaData.add(new DoublePhotographer("senior", photographer1, null));

                            List<PhotographerWorks> workses = photographer1.getList();

                            for (PhotographerWorks works : workses) {
                                works.setpName(photographer1.getPersonName());
                            }

                            db.saveOrUpdateAll(workses);
//                            List<PhotographerWorks> localWorkses = db.findAll(PhotographerWorks.class);
//                            for (PhotographerWorks w: localWorkses){
//                                S.o("::缓存后::::" + w.getpName() + ":::" + w.getContentName()) ;
//                            }
                            tempSenior.add(photographer1);
                        }

                        if (!MTDBUtil.todayChecked(getActivity(), MTSelPhotographerFragment.TAG_PHOTO_SENIOR)) {
                            db.delete(Photographer.class, WhereBuilder.b("tag", "=", MTDBUtil.PHOTO_SENIOR));
                            Cache cache = new Cache(MTSelPhotographerFragment.TAG_PHOTO_SENIOR, MTDBUtil.getToday());
                            db.saveOrUpdate(cache);
                            db.saveOrUpdateAll(tempSenior);
                        } else {
                            S.o(":::今天已经缓存过[[" + MTSelPhotographerFragment.TAG_PHOTO_MAJORDOME);
                        }

                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }// success end

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "Get senior error! ");
        }
    };

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

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

        private static final int TYPE_TAG_MAJORDOMO = 0;
        private static final int TYPE_TAG_SENIOR = 1;
        private static final int TYPE_MAJORDOMO = 2;
        private static final int TYPE_SENIOR = 3;

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener listener) {
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public int getItemViewType(int position) {
            if (finaData.get(position).getTag().equals("majordomo_tag")) {
                return TYPE_TAG_MAJORDOMO;
            } else if (finaData.get(position).getTag().equals("senior_tag")) {
                return TYPE_TAG_SENIOR;
            } else if (finaData.get(position).getTag().equals("majordomo")) {
                return TYPE_MAJORDOMO;
            } else if (finaData.get(position).getTag().equals("senior")) {
                return TYPE_SENIOR;
            }
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_TAG_MAJORDOMO) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singel_textview, parent, false);
                return new MajordomoTagViewHolder(itemView);
            } else if (viewType == TYPE_TAG_SENIOR) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singel_textview, parent, false);
                return new SeniorTagViewHolder(view);
            } else if (viewType == TYPE_MAJORDOMO) {
                View view = View.inflate(parent.getContext(), R.layout.item_double_majordomo, null);
                return new MajordomoViewHolder(view);
            } else if (viewType == TYPE_SENIOR) {
                View view = View.inflate(parent.getContext(), R.layout.item_senior_layout, null);
                return new SeniorViewHolder(view, null);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof MajordomoTagViewHolder) {
                MajordomoTagViewHolder tagHolder = (MajordomoTagViewHolder) holder;
                tagHolder.tagTxt.setText("总监级摄影师");
            } else if (holder instanceof SeniorTagViewHolder) {
                SeniorTagViewHolder tagHolder = (SeniorTagViewHolder) holder;
                tagHolder.tagTxt.setText("资深摄影师");
            } else if (holder instanceof MajordomoViewHolder) {
                MajordomoViewHolder majorHolder = (MajordomoViewHolder) holder;
                DoublePhotographer doublePhoto = finaData.get(position);

                majorHolder.rightLayout.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DimenUtil.screenWidth / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
                majorHolder.leftLayout.setLayoutParams(params);
                majorHolder.rightLayout.setLayoutParams(params);

                if (doublePhoto.getPhotographer1() != null && doublePhoto.getPhotographer2() != null) { //double
                    majorHolder.nameTxt1.setText(doublePhoto.getPhotographer1().getPersonName());
                    majorHolder.worksTxt1.setTag(doublePhoto.getPhotographer1());
                    majorHolder.worksTxt1.setOnClickListener(new View.OnClickListener() {//跳转
                        @Override
                        public void onClick(View view) {
                            Photographer photographer = (Photographer) view.getTag();
                            Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                            intent.putExtra("personId", photographer.getPersonId() + "");
                            intent.putExtra("headUrl", photographer.getPhotoUrl());
                            animStart(intent);
                        }
                    });
                    Picasso.with(getActivity()).load(doublePhoto.getPhotographer1().getPhotoUrl()).into(majorHolder.headImg1);

                    majorHolder.nameTxt2.setText(doublePhoto.getPhotographer2().getPersonName());
                    majorHolder.worksTxt2.setTag(doublePhoto.getPhotographer2());
                    majorHolder.worksTxt2.setOnClickListener(new View.OnClickListener() {//跳转
                        @Override
                        public void onClick(View view) {
                            Photographer photographer = (Photographer) view.getTag();
                            Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                            intent.putExtra("personId", photographer.getPersonId() + "");
                            intent.putExtra("headUrl", photographer.getPhotoUrl());
                            animStart(intent);
                        }
                    });
                    Picasso.with(getActivity()).load(doublePhoto.getPhotographer2().getPhotoUrl()).into(majorHolder.headImg2);
                } else {//only one
                    majorHolder.rightLayout.setVisibility(View.INVISIBLE);
                    majorHolder.worksTxt1.setTag(doublePhoto.getPhotographer1());
                    majorHolder.nameTxt1.setText(doublePhoto.getPhotographer1().getPersonName());
                    majorHolder.worksTxt1.setOnClickListener(new View.OnClickListener() {//跳转
                        @Override
                        public void onClick(View view) {
                            Photographer photographer = (Photographer) view.getTag();
                            Intent intent = new Intent(getActivity(), MajordomoDetailActivity.class);
                            intent.putExtra("personId", photographer.getPersonId() + "");
                            intent.putExtra("headUrl", photographer.getPhotoUrl());
                            animStart(intent);
                        }
                    });
                    Picasso.with(getActivity()).load(doublePhoto.getPhotographer1().getPhotoUrl()).into(majorHolder.headImg1);
                }
            } else if (holder instanceof SeniorViewHolder) {
                SeniorViewHolder seniorHolder = (SeniorViewHolder) holder;
                final Photographer photographer = finaData.get(position).getPhotographer1();
                final int size = photographer.getList() == null ? 0 : photographer.getList().size();
                seniorHolder.nameTxt.setText(photographer.getPersonName());
                Picasso.with(getActivity()).load(photographer.getPhotoUrl()).into(seniorHolder.headImg);
                if (size == 0) {
                    seniorHolder.topLayout.setVisibility(View.GONE);
                    seniorHolder.bottomLayout.setVisibility(View.GONE);
                } else if (size == 1) {
                    seniorHolder.topLayout.setVisibility(View.VISIBLE);
                    seniorHolder.bottomLayout.setVisibility(View.GONE);
                    seniorHolder.contentImage1.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage2.setVisibility(View.INVISIBLE);
                    Picasso.with(getActivity()).load(photographer.getList().get(0).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage1);

                } else if (size == 2) {
                    seniorHolder.topLayout.setVisibility(View.VISIBLE);
                    seniorHolder.bottomLayout.setVisibility(View.INVISIBLE);

                    seniorHolder.contentImage1.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage2.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(photographer.getList().get(0).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage1);
                    Picasso.with(getActivity()).load(photographer.getList().get(1).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage2);
                } else if (size == 3) {
                    seniorHolder.topLayout.setVisibility(View.VISIBLE);
                    seniorHolder.bottomLayout.setVisibility(View.VISIBLE);

                    seniorHolder.contentImage1.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage2.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage3.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage4.setVisibility(View.INVISIBLE);

                    Picasso.with(getActivity()).load(photographer.getList().get(0).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage1);
                    Picasso.with(getActivity()).load(photographer.getList().get(1).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage2);
                    Picasso.with(getActivity()).load(photographer.getList().get(2).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage3);

                } else if (size == 4) {
                    seniorHolder.topLayout.setVisibility(View.VISIBLE);
                    seniorHolder.bottomLayout.setVisibility(View.VISIBLE);

                    seniorHolder.contentImage1.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage2.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage3.setVisibility(View.VISIBLE);
                    seniorHolder.contentImage4.setVisibility(View.VISIBLE);

                    Picasso.with(getActivity()).load(photographer.getList().get(0).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage1);
                    Picasso.with(getActivity()).load(photographer.getList().get(1).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage2);
                    Picasso.with(getActivity()).load(photographer.getList().get(2).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage3);
                    Picasso.with(getActivity()).load(photographer.getList().get(3).getContentUrl() + DimenUtil.getVerticalListViewStringDimension(DimenUtil.screenWidth)).into(seniorHolder.contentImage4);
                }

                seniorHolder.contentImage1.setTag(position + "@" + 1);
                seniorHolder.contentImage2.setTag(position + "@" + 2);
                seniorHolder.contentImage3.setTag(position + "@" + 3);
                seniorHolder.contentImage4.setTag(position + "@" + 4);

                seniorHolder.contentImage1.setOnClickListener(this);
                seniorHolder.contentImage2.setOnClickListener(this);
                seniorHolder.contentImage3.setOnClickListener(this);
                seniorHolder.contentImage4.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            String p_i[] = view.getTag().toString().split("@");
            int position = Integer.parseInt(p_i[0]);
            int i = Integer.parseInt(p_i[1]) - 1;

            final Photographer stylist = finaData.get(position).getPhotographer1();
            Intent intent = new Intent(getActivity(), WorksDetailActivity.class);
            intent.putExtra("personId", stylist.getPersonId() + "");
            intent.putExtra("worksId", stylist.getList().get(i).getContentId());
            animStart(intent);
        }

        @Override
        public int getItemCount() {
            return finaData == null ? 0 : finaData.size();
        }

        /**
         * majordomo tag vew holder
         */
        public class MajordomoTagViewHolder extends RecyclerView.ViewHolder {

            TextView tagTxt;

            public MajordomoTagViewHolder(View itemView) {
                super(itemView);
                tagTxt = (TextView) itemView.findViewById(R.id.single_text);
            }
        }

        /**
         * senior tag view holder
         */
        public class SeniorTagViewHolder extends RecyclerView.ViewHolder {

            TextView tagTxt;

            public SeniorTagViewHolder(View itemView) {
                super(itemView);
                tagTxt = (TextView) itemView.findViewById(R.id.single_text);
            }
        }

        /**
         * majordomo vew holder
         */
        public class MajordomoViewHolder extends RecyclerView.ViewHolder {

            ImageView headImg1;
            ImageView headImg2;
            TextView nameTxt1;
            TextView nameTxt2;
            TextView worksTxt1;
            TextView worksTxt2;
            LinearLayout rightLayout;
            LinearLayout leftLayout;

            public MajordomoViewHolder(View itemView) {
                super(itemView);

                headImg1 = (ImageView) itemView.findViewById(R.id.majordomo_head_img1);
                headImg2 = (ImageView) itemView.findViewById(R.id.majordomo_head_img2);

                nameTxt1 = (TextView) itemView.findViewById(R.id.majordomo_name_txt1);
                nameTxt2 = (TextView) itemView.findViewById(R.id.majordomo_name_txt2);

                worksTxt1 = (TextView) itemView.findViewById(R.id.majordomo_works_txt1);
                worksTxt2 = (TextView) itemView.findViewById(R.id.majordomo_works_txt2);

                leftLayout = (LinearLayout) itemView.findViewById(R.id.left_layout);
                rightLayout = (LinearLayout) itemView.findViewById(R.id.right_layout);
            }
        }

        /**
         *
         */
        public final class SeniorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ImageView headImg;
            TextView nameTxt;
            LinearLayout topLayout;
            LinearLayout bottomLayout;
            ImageView contentImage1;
            ImageView contentImage2;
            ImageView contentImage3;
            ImageView contentImage4;

            public SeniorViewHolder(View view, OnRecyclerItemClickListener listener) {
                super(view);
                headImg = (ImageView) view.findViewById(R.id.item_senior_head_img);
                nameTxt = (TextView) view.findViewById(R.id.item_senior_name_txt);

                topLayout = (LinearLayout) view.findViewById(R.id.item_senior_content_top_layout);
                bottomLayout = (LinearLayout) view.findViewById(R.id.item_senior_content_bottom_layout);

                contentImage1 = (ImageView) view.findViewById(R.id.item_senior_content_img1);
                contentImage2 = (ImageView) view.findViewById(R.id.item_senior_content_img2);
                contentImage3 = (ImageView) view.findViewById(R.id.item_senior_content_img3);
                contentImage4 = (ImageView) view.findViewById(R.id.item_senior_content_img4);
                onRecyclerItemClickListener = listener;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                //onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }

}
