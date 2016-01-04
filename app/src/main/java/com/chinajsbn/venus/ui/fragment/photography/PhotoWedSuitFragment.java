package com.chinajsbn.venus.ui.fragment.photography;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.net.bean.WeddingSuit;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.MBaseFragment;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.flip.FlippableStackView;
import com.tool.widget.flip.StackPageTransformer;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * 套系报价
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_price)
public class PhotoWedSuitFragment extends BaseFragment implements OnClickListener {

    public static final String TAG = "PhotoWedSuitFragment";

    @ViewInject(R.id.suit_flipStackView)
    private FlippableStackView stackView;

    @ViewInject(R.id.suit_pages_txt)
    private TextView pagesTxt;

    @ViewInject(R.id.suit_pages_total_txt)
    private TextView totalTxt;

    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

//    @ViewInject(R.id.suit_page_container)
//    private LinearLayout container;

    //传递过来的数据
    private String parentId;

    private int size = 0;

    //---------------cache----------------
    private DbUtils db;

    //---------------cache end----------------

    @Override
    public void onPause() {
        super.onPause();
//        stackView = null;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_photo_price, container, false);
//       stackView = (FlippableStackView) view.findViewById(R.id.suit_flipStackView);
//
//        pagesTxt= (TextView) view.findViewById(R.id.suit_pages_txt);
//
//        totalTxt= (TextView) view.findViewById(R.id.suit_pages_total_txt);
//
//        initialize();
//        return view;
//    }


    @Override
    public void initialize() {

        db = DbUtils.create(getContext());
        db.configAllowTransaction(true);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(PhotoWedSuitFragment.this)
                .create();

//        tempStackView = stackView;
        stackView.initStack(4, StackPageTransformer.Orientation.VERTICAL);
        stackView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagesTxt.setText((size - position) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //固定写法。
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        //获取数据
        parentId = getArguments().getString("contentId", null);
//
    }

    @Override
    public void show() {
        S.o("::获取套系数据");
        dialog.show();
        if(NetworkUtil.hasConnection(getActivity())){
            HttpClient.getInstance().getWeddingSuitList(parentId, 1, 50, cb);
        }else {
            try {
                List<WeddingSuit> suitList = db.findAll(WeddingSuit.class);
                if(suitList != null && suitList.size() > 0){

                    size = suitList == null ? 0 : suitList.size();
                    if(size <= 0){
                        totalTxt.setText("/" + size);
                        pagesTxt.setText("0");
                    }else {

                        SuitAdapter adapter = new SuitAdapter(getActivity().getSupportFragmentManager(), suitList);
                        stackView.setAdapter(adapter);
                        totalTxt.setText("/" + size);
                        pagesTxt.setText("1");
                    }
                }else{
                    handler.sendEmptyMessageDelayed(10, 1000);
                    handler.sendEmptyMessageDelayed(11, 4000);
                }
            } catch (DbException e) {
                e.printStackTrace();
            }

            if(dialog != null && dialog.isShowing()) dialog.dismiss();
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
                    if(mtdialog != null && mtdialog.isShowing()) mtdialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void hide() {

    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                dialog.dismiss();
                break;
        }
    }


    private class SuitAdapter extends FragmentPagerAdapter {
        private List<WeddingSuit> dataList;

        public SuitAdapter(FragmentManager fm, List<WeddingSuit> list) {
            super(fm);
            List<WeddingSuit> temp = new ArrayList<>(list.size());

            final int size = list.size();
            for (int i = size - 1; i >= 0; i--) {
                temp.add(list.get(i));
            }
            this.dataList = temp;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = new SuitFlipFragment();

            Bundle args = new Bundle();
            args.putSerializable("suit", dataList.get(position));
            args.putString("suitId", parentId);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }
    }

    private Callback<Base<ArrayList<WeddingSuit>>> cb = new Callback<Base<ArrayList<WeddingSuit>>>() {
        @Override
        public void success(Base<ArrayList<WeddingSuit>> weddingSuits, Response response) {

            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            if(weddingSuits.getCode() == 200){

                size = weddingSuits.getData() == null ? 0 : weddingSuits.getData().size();

                if(size <= 0){
                    totalTxt.setText("/" + size);
                    pagesTxt.setText("0");
                }else {
                    try {
                        db.saveOrUpdateAll(weddingSuits.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    SuitAdapter adapter = new SuitAdapter(getActivity().getSupportFragmentManager(), weddingSuits.getData());
                    stackView.setAdapter(adapter);
                    totalTxt.setText("/" + size);
                    pagesTxt.setText("1");
                }

            }else {

            }
//            recyclerView.setAdapter(new MyAdapter(weddingSuits.getData(), PhotoWedSuitFragment.this));
        }

        @Override
        public void failure(RetrofitError error) {
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "Get data error! ");
        }
    };
}
