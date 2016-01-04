package com.chinajsbn.venus.ui.fragment.plan;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Scheme;
import com.chinajsbn.venus.net.bean.Style;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.plan.MPlanDetailActivity;
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
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MaterialRippleLayout;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;
import com.tool.widget.transformation.FabTransformation;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 案例欣赏
 * <p>
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_plan)
public class PlanSimpleFragment extends BaseFragment implements OnRecyclerItemClickListener, MasterListView.OnRefreshListener, OnClickListener {

    ///used by cache
    public static final String TAG = "PlanSimpleFragment_tag_sjal";
    public static final String TAG_STYLE = "PlanSimpleFragment_style";

//    @ViewInject(R.id.recyclerView)
//    private RecyclerView recyclerView;
//    private MyAdapter adapter;
//    private LinearLayoutManager layoutManager;

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

//    @ViewInject(R.id.swipe_refresh)
//    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.fab)
    private TextView fab;

    @ViewInject(R.id.sheet)
    private View sheet;

    @ViewInject(R.id.overlay)
    private View overlay;

    @ViewInject(R.id.listView)
    private ListView styleListView;

    //Dialog
    private AlertDialog dialog;
    private MTDialog mtdialog;

    public static boolean refreshed = false;
    public static String styleId = "-1";
    public static String styleName = "";//cache used

    private ArrayList<Scheme> tempDataList;

    //传递过来的数据
    private SubModule subModules;

    //模块ID
    private final String parentId = "27240";

    //--------------page-----------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private int lastVisibleItem;
    private int totalCount = -1;


    //------------------Cache------------------
    private DbUtils db;

    //------------------Cache end---------------

    @Override
    public void onRefresh(int id) {
        getFirstPages();
    }

    @Override
    public void onLoadMore(int id) {
        getNextPages();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshed) {//详情的查看更多相似风格跳转处理
            HttpClient.getInstance().getPlanSimplesByStyleId(styleId, parentId, 1, 10, cb);
            refreshed = false;
        }
    }

    @Override
    public void onDestroy() {
        styleId = "-1";
        super.onDestroy();
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        sheet.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.overlay)
    public void onClickOverlay(View view) {
        if (fab.getVisibility() != View.VISIBLE) {
            FabTransformation.with(fab).setOverlay(overlay).transformFrom(sheet);
        }
    }

    private void hideViews() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        fab.animate().translationY(fab.getHeight() + fabBottomMargin).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }

    private void showViews() {
        fab.animate().translationY(0).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }

    private void getFirstPages() {
        if (NetworkUtil.hasConnection(getActivity())) {
            isNextPage = false;
            pageIndex = 1;

            listView.startRefresh();
            listView.saveRefreshStrTime();

            listView.startRotateProgress();
            listView.setPullRefreshEnable(false);
            if(styleId.equals("-1")){
                HttpClient.getInstance().getPlanSimples(parentId, pageIndex, pageSize, cb);
            }else{
                HttpClient.getInstance().getPlanSimplesByStyleId(styleId, parentId, pageIndex, pageSize, cb);
            }
        } else {
            getPagesFromLocal();
        }
    }

    private void getPagesByStyle() {
        pageIndex = 1;
        if (NetworkUtil.hasConnection(getActivity())) {
            HttpClient.getInstance().getPlanSimplesByStyleId(styleId, parentId, pageIndex, pageSize, cb);
        } else {
            getPagesByStyleFromLocal();
        }
    }

    private void getPagesFromLocal() {
        List<Scheme> schemeList = null;
        try {
            schemeList = db.findAll(Selector.from(Scheme.class).where("tag", "=", TAG));
        } catch (DbException e) {
            e.printStackTrace();
        }
        adapter.setDataList(schemeList);
        adapter.notifyDataSetChanged();
    }

    private void getPagesByStyleFromLocal() {
        try {
            List<Scheme> schemeList = null;

            if (styleId.equals("-1"))
                schemeList = db.findAll(Selector.from(Scheme.class).where("tag", "=", TAG));
            else
                schemeList = db.findAll(Selector.from(Scheme.class).where("styleName", "=", styleName));

            S.o(":getPagesByStyleFromLocal::" + schemeList.size() + ":::::styleName=" + styleName);

            adapter.setDataList(schemeList);
            adapter.notifyDataSetChanged();

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void getNextPages() {
        isNextPage = true;
        pageIndex++;
        if(styleId.equals("-1")){
            HttpClient.getInstance().getPlanSimples(parentId, pageIndex, pageSize, cb);
        }else{
            HttpClient.getInstance().getPlanSimplesByStyleId(styleId, parentId, pageIndex, pageSize, cb);
        }
    }

    @Override
    public void initialize() {

        db = DbUtils.create(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_not_network_layout, null);
        ViewHolder holder = new ViewHolder(view);
        mtdialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .setOnClickListener(PlanSimpleFragment.this)
                .create();

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setOnRefreshListener(PlanSimpleFragment.this, 0);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.startRefresh();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Intent intent = new Intent(getActivity(), PlanDetailActivity.class);
                if(NetworkUtil.hasConnection(getActivity())){
                    Intent intent = new Intent(getActivity(), MPlanDetailActivity.class);
                    intent.putExtra("parentId", parentId);
                    intent.putExtra("detailId", adapter.getItem(position).getWeddingCaseId() + "");
                    intent.putExtra("name", adapter.getItem(position).getSchemeName());
                    animStart(intent);
                }else{
                    handler.sendEmptyMessageDelayed(10, 100);
                    handler.sendEmptyMessageDelayed(11, 4000);
                }
            }
        });

        //获取数据
        if (NetworkUtil.hasConnection(getActivity())) {
            dialog.show();
            getFirstPages();
            //Dialog
            HttpClient.getInstance().getSchemeList(styleListCallback);
        } else {
            try {
                List<Scheme> schemeList = db.findAll(Scheme.class);
                List<Style> styleList = db.findAll(Style.class);

                if (schemeList == null || schemeList.size() <= 0) {
                    handler.sendEmptyMessageDelayed(10, 1000);
                    handler.sendEmptyMessageDelayed(11, 4000);
                } else {
                    adapter.setDataList(schemeList);
                    styleListView.setAdapter(new StyleAdapter(styleList));
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        styleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sheet.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                listView.smoothScrollToPosition(0);
                isNextPage = false;
                if (position == 0) {//XXB
                    styleId = "-1";
                    getFirstPages();
                } else {
                    styleId = ((StyleAdapter)styleListView.getAdapter()).getItem(position).getStyleId();
                    styleName = ((StyleAdapter)styleListView.getAdapter()).getItem(position).getStyleName();
                    getPagesByStyle();
                }
            }
        });
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
    public void show() {

    }

    @Override
    public void hide() {

    }

    private Callback<Base<ArrayList<Style>>> styleListCallback = new Callback<Base<ArrayList<Style>>>() {
        @Override
        public void success(Base<ArrayList<Style>> resp, Response response) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (resp.getCode() == 200) {

                styleListView.setAdapter(new StyleAdapter(resp.getData()));

                if (!MTDBUtil.todayChecked(getActivity(), PlanSimpleFragment.TAG_STYLE)) {
                    try {
                        db.saveOrUpdateAll(resp.getData());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                } else {
                    S.o("::::已经缓存过案例的style");
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "获取数据失败,请稍后重试! ");
        }
    };

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.network_confirm_button:
                mtdialog.dismiss();
                break;
        }
    }

    /**
     * 风格样式adapter
     */
    class StyleAdapter extends BaseAdapter {

        List<Style> data;

        public StyleAdapter(List<Style> list) {
            data = new ArrayList<>();
            Style style = new Style("-1", "全部风格");
            data.add(style);
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Style getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_single_textview_layout, viewGroup, false);
                holder.txt = (TextView) view.findViewById(R.id.item_single_text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.txt.setText(data.get(i).getStyleName());
            return view;
        }

        class ViewHolder {
            TextView txt;
        }
    }

    private Callback<Base<ArrayList<Scheme>>> cb = new Callback<Base<ArrayList<Scheme>>>() {
        @Override
        public void success(Base<ArrayList<Scheme>> simpleResp, Response response) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            listView.stopRefresh();
            if (simpleResp.getCode() == 200) {
                try {
                    if (isNextPage) {//下一页
                        adapter.getDataList().addAll(simpleResp.getData());
                        if(styleId.equals("-1")){//全部， 没有风格的时候缓存
                            S.o(":::缓存第下一页数据");
                            for (Scheme scheme : simpleResp.getData()){
                                scheme.setTag(TAG);
                            }
                            db.saveAll(simpleResp.getData());
                        }else{
                            S.o(":::有风格不缓存");
                        }
                        adapter.notifyDataSetChanged();
                    } else {       //first 首次
                        adapter.setDataList(simpleResp.getData());
                        adapter.notifyDataSetChanged();

                        if(styleId.equals("-1")){//全部， 没有风格的时候缓存
                            S.o(":::缓存第一页数据");
                            db.delete(Scheme.class, WhereBuilder.b("tag", "=", TAG));
                            for (Scheme scheme : simpleResp.getData()){
                                scheme.setTag(TAG);
                            }
                            db.saveAll(simpleResp.getData());
                        }else{
                            S.o(":::有风格不缓存");
                        }
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }

                ///can load more
                if (simpleResp.getData() != null && simpleResp.getData().size() > 0) {
                    if (simpleResp.getData().size() < pageSize) {
                        listView.setPullLoadEnable(false);
                    } else {
                        listView.setPullLoadEnable(true);
                    }
                } else {
                    listView.setPullLoadEnable(false);
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            listView.stopRefresh();
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if(getActivity() != null){
                T.s(getActivity(), "获取数据失败,请稍后重试! ");
            }
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {}

    class MyAdapter extends BaseAdapter {

        private List<Scheme> dataList;

        public MyAdapter(ArrayList<Scheme> list) {
            this.dataList = list;
        }

        public List<Scheme> getDataList() {
            return dataList;
        }

        public void setDataList(List<Scheme> dataList) {
            this.dataList = dataList;
        }

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Scheme getItem(int i) {
            return dataList.get(i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_plan_simple_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.nameTxt.setText(dataList.get(position).getSchemeName());
            holder.styleTxt.setText("风格:" + dataList.get(position).getStyleName());
            //加载内容
            if (TextUtils.isEmpty(dataList.get(position).getWeddingCaseImage())) {
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            } else {
                Picasso.with(getActivity()).load(dataList.get(position).getWeddingCaseImage() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).placeholder(R.drawable.loading).error(getResources().getDrawable(R.mipmap.ic_launcher)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.contentImg);
            }
            //
            return view;
        }

        class ViewHolder {
            TextView nameTxt;
            TextView styleTxt;
            ImageView contentImg;
            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public ViewHolder(View itemView) {
                contentImg = (ImageView) itemView.findViewById(R.id.item_plan_simple_pic_img);
                nameTxt = (TextView) itemView.findViewById(R.id.item_plan_simple_name_txt);
                styleTxt = (TextView) itemView.findViewById(R.id.item_plan_simple_style_txt);
            }
        }
    }
}