package com.chinajsbn.venus.ui.fragment.photography;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.AddrStyleResp;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.PhotoStyle;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.SimpleStyles;
import com.chinajsbn.venus.net.bean.Style;
import com.chinajsbn.venus.net.bean.SubModule;
import com.chinajsbn.venus.ui.base.BaseFragment;
import com.chinajsbn.venus.ui.base.FragmentFeature;
import com.chinajsbn.venus.ui.base.OnRecyclerItemClickListener;
import com.chinajsbn.venus.ui.photography.SimpleDetailActivity;
import com.chinajsbn.venus.utils.DimenUtil;
import com.chinajsbn.venus.utils.S;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.CircleImageView;
import com.tool.widget.MaterialRippleLayout;
import com.tool.widget.floatActionButton.RecyclerViewScrollDetector;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;
import com.tool.widget.transformation.FabTransformation;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 样片
 * Created by MasterFan on 2015/6/16.
 * description:
 */
@FragmentFeature(layout = R.layout.fragment_photo_simple)
public class PhotoSimpleFragment extends BaseFragment implements MasterListView.OnRefreshListener {

    public static final String TAG = "PhotoSimpleFragment";
//    @ViewInject(R.id.recyclerView)
//    private RecyclerView recyclerView;

    @ViewInject(R.id.myListView)
    private MyListView listView;
    private MyAdapter adapter;

    @ViewInject(R.id.fab)
    private TextView fab;

    @ViewInject(R.id.sheet)
    private View sheet;

    @ViewInject(R.id.overlay)
    private View overlay;

    @ViewInject(R.id.listView)
    private ListView lv;

//    @ViewInject(R.id.swipe_refresh)
//    private SwipeRefreshLayout refreshLayout;

    //--------------page-----------------
    private int pageIndex = 1;
    private int pageSize = 10;
    private boolean isNextPage = false;//true:获取下一页数据    false:获取第一页数据
    private int lastVisibleItem;

    //传递过来的数据
    private SubModule subModules;

    public static boolean refreshed = false;
    public static String styleId = "-1";

    //模块ID
    private String parentId = "";

    //客片、样片
    private String simpleOrCustom = "simple";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getFirstPages();
                    HttpClient.getInstance().getStyleList(styleListCallback);
                    break;
                case 1:
                    listView.stopRefresh();
                    //adapter.notifyDataSetChanged();
                    break;
                default:

                    break;
            }
        }
    };


    private void getFirstPages() {
        isNextPage = false;
        pageIndex = 1;

        listView.startRefresh();
        listView.saveRefreshStrTime();

        if (styleId.equals("-1")) {
            HttpClient.getInstance().getSimples(parentId, pageIndex, pageSize, cb);
        } else {
            getPagesByStyle();
        }
    }

    private void getPagesByStyle() {
        pageIndex = 1;
        if (adapter.getDataList() != null) {
            listView.smoothScrollToPosition(1);
        }
        S.o("::styleId=" + styleId);
        HttpClient.getInstance().getSimplesByStyleId(styleId, parentId, pageIndex, pageSize, cb);
    }

    private void getNextPages() {
        isNextPage = true;
        pageIndex++;
        HttpClient.getInstance().getSimples(parentId, pageIndex, pageSize, cb);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshed && !styleId.equals("-1")) {
            HttpClient.getInstance().getSimplesByStyleId(styleId, parentId, 1, 10, cb);
            refreshed = false;
        } else {
            if (adapter.getDataList() == null) {
                handler.sendEmptyMessageDelayed(0, 800);
            }
        }
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        if (fab.getVisibility() == View.VISIBLE) {
            FabTransformation.with(fab).setOverlay(overlay).transformTo(sheet);
        }
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

    @Override
    public void onRefresh(int id) {
        getFirstPages();
    }

    @Override
    public void onLoadMore(int id) {
        getNextPages();
    }

    class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector {
        private RecyclerView.OnScrollListener mOnScrollListener;

        public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            showViews();
        }

        @Override
        public void onScrollUp() {
            hideViews();
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }

            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }

            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    @Override
    public void initialize() {

        S.o("::PhotoSimpleFragment initialize >>");
//        refreshLayout.setOnRefreshListener(this);
        listView.setPullRefreshEnable(false);
        listView.setPullLoadEnable(false);
        listView.setOnRefreshListener(PhotoSimpleFragment.this, 0);

        listView.startRefresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Simple simple = adapter.getItem(position);
                //SimpleDetailResp
                Intent intent = new Intent(getActivity(), SimpleDetailActivity.class);
                intent.putExtra("parentId", parentId);
                intent.putExtra("contentId", simple.getContentId());
                intent.putExtra("simpleOrCustom", simpleOrCustom);
                intent.putExtra("photographer", simple.getPhotographer());
                intent.putExtra("stylist", simple.getStylist());
                intent.putExtra("date", simple.getCreateDate());
                intent.putExtra("name", simple.getContentName());


                ArrayList<SimpleStyles> styles = simple.getShootingStyles();
                String strStyles = "";
                if (styles != null && styles.size() > 0) {
                    for (int i = 0; i < styles.size(); i++) {
                        strStyles += styles.get(i).getShootingStyleName() + ",";
                    }
                    strStyles = strStyles.substring(0, strStyles.lastIndexOf(","));
                }
                intent.putExtra("styles", strStyles);

                styleId = (styles == null || styles.size() == 0) ? "-1" : styles.get(0).getShootingStyleId();

                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
            }
        });

        //固定写法。
//        layoutManager = new LinearLayoutManager(getActivity());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setHasFixedSize(true);
//
//        adapter = new MyAdapter(this);
//        recyclerView.setAdapter(adapter);
//
//
//        RecyclerViewScrollDetectorImpl scrollDetector = new RecyclerViewScrollDetectorImpl();
//        int mScrollThreshold = getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold);
//        scrollDetector.setScrollThreshold(mScrollThreshold);
//        recyclerView.addOnScrollListener(scrollDetector);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        //获取数据
        subModules = (SubModule) getArguments().getSerializable("subModule");

        parentId = subModules.getSubModule().get(0).getContentId();
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void onPause() {
        super.onPause();
        isNextPage = false;
    }

    class StyleAdapter extends BaseAdapter {

        List<PhotoStyle> data;

        public StyleAdapter(List<PhotoStyle> list) {
            data = list;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
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
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_single_textview_layout, viewGroup, false);
                holder.txt = (TextView) view.findViewById(R.id.item_single_text);
//                holder.layout = (MaterialRippleLayout) view.findViewById(R.id.item_single_text_ripple);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.txt.setText(data.get(i).getStyleName());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fab.getVisibility() != View.VISIBLE) {
                        FabTransformation.with(fab).setOverlay(overlay).transformFrom(sheet);
                    }
                    if (i == 0) {
                        styleId = "-1";
                        getFirstPages();
                    } else {
                        isNextPage = false;
                        styleId = data.get(i).getStyleId();
                        getPagesByStyle();
                    }
                }
            });
            return view;
        }

        class ViewHolder {
            TextView txt;
            MaterialRippleLayout layout;
        }
    }

    private Callback<AddrStyleResp> styleListCallback = new Callback<AddrStyleResp>() {
        @Override
        public void success(AddrStyleResp addrStyleResp, Response response) {
            List<PhotoStyle> dataList = new ArrayList<>();
            dataList.add(new PhotoStyle("-1", "全部风格"));
            dataList.addAll(addrStyleResp.getData().getStyle());
            lv.setAdapter(new StyleAdapter(dataList));
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "Get data error! ");
        }
    };

//    @OnClick(R.id.fab)
//    public void goPersonal(View view){
//        animStart(PersonalActivity.class);
//    }

    private Callback<Base<ArrayList<Simple>>> cb = new Callback<Base<ArrayList<Simple>>>() {
        @Override
        public void success(Base<ArrayList<Simple>> simpleResp, Response response) {
//            refreshLayout.setRefreshing(false);
            if (simpleResp.getCode() == 200) {

                if (isNextPage) {//下一页
                    adapter.getDataList().addAll(simpleResp.getData());
                    adapter.notifyDataSetChanged();
                } else {       //首次
                    S.o("==>获取样片数据");
                    adapter.setDataList(simpleResp.getData());
                    if(!styleId.equals("-1")){
                        adapter.notifyDataSetChanged();
                    }else {
                        handler.sendEmptyMessageDelayed(1, 1500);
                    }
                }

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
            T.s(getActivity(), "Get data error! ");
        }
    };

//    @Override
//    public void onRecyclerItemClick(View v, int position) {
//        Simple simpleCustom = adapter.getItem(position);
//        //SimpleDetailResp
//        Intent intent = new Intent(getActivity(), SimpleDetailActivity.class);
//        intent.putExtra("parentId", parentId);
//        intent.putExtra("contentId", simpleCustom.getContentId());
//        intent.putExtra("simpleOrCustom", simpleOrCustom);
//        intent.putExtra("photographer", simpleCustom.getPhotographer());
//        intent.putExtra("stylist", simpleCustom.getStylist());
//        intent.putExtra("date", simpleCustom.getCreateDate());
//        intent.putExtra("name", simpleCustom.getContentName());
//
//
//        ArrayList<SimpleStyles> styles = simpleCustom.getShootingStyles();
//        String strStyles = "";
//        if (styles != null && styles.size() > 0) {
//            for (int i = 0; i < styles.size(); i++) {
//                strStyles += styles.get(i).getShootingStyleName() + ",";
//            }
//            strStyles = strStyles.substring(0, strStyles.lastIndexOf(","));
//        }
//        intent.putExtra("styles", strStyles);
//
//        styleId = (styles == null || styles.size() == 0) ? "-1" : styles.get(0).getShootingStyleId();
//
//        getActivity().startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
//    }

    class MyAdapter extends BaseAdapter {

        private ArrayList<Simple> dataList;

        public ArrayList<Simple> getDataList() {
            return dataList;
        }

        public void setDataList(ArrayList<Simple> dataList) {
            this.dataList = dataList;
        }

        public MyAdapter(ArrayList<Simple> d) {
            this.dataList = d;
        }

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public Simple getItem(int i) {
            return dataList.get(i - 1);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_simple_layout, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            if (dataList.get(position).getShootingStyles() != null && dataList.get(position).getShootingStyles().size() > 0)
                holder.styleTxt.setText("风格: " + dataList.get(position).getShootingStyles().get(0).getShootingStyleName());

            holder.nameTxt.setText(dataList.get(position).getContentName());

            //加载摄影师
            if (dataList.get(position).getPhotographer() != null && !TextUtils.isEmpty(dataList.get(position).getPhotographer().getPhotoUrl())) {
                holder.headImgLayout.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(dataList.get(position).getPhotographer().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.photographerImg);
                //加造型师
                if (dataList.get(position).getStylist() != null && !TextUtils.isEmpty(dataList.get(position).getStylist().getPhotoUrl())) {
                    Picasso.with(getActivity()).load(dataList.get(position).getStylist().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.stylistImg);
                }
            } else {
                holder.headImgLayout.setVisibility(View.GONE);
            }

            //加载内容
            if (TextUtils.isEmpty(dataList.get(position).getContentUrl())) {
                Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(holder.contentImg);
            } else {
                Picasso.with(getActivity()).load(dataList.get(position).getContentUrl() + DimenUtil.getHorizontalListViewStringDimension(DimenUtil.screenWidth)).error(getResources().getDrawable(R.mipmap.ic_launcher)).resize(DimenUtil.screenWidth, DimenUtil.targetHeight).into(holder.contentImg);
            }

            return view;
        }

        class ViewHolder {
            TextView styleTxt;
            TextView nameTxt;
            ImageView contentImg;
            CircleImageView photographerImg;
            CircleImageView stylistImg;
            LinearLayout headImgLayout;
            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public ViewHolder(View itemView) {
                contentImg = (ImageView) itemView.findViewById(R.id.item_simple_pic_img);
                photographerImg = (CircleImageView) itemView.findViewById(R.id.item_simple_photographer_head_img);
                stylistImg = (CircleImageView) itemView.findViewById(R.id.item_simple_stylist_head_img);
                styleTxt = (TextView) itemView.findViewById(R.id.item_simple_styles_txt);
                nameTxt = (TextView) itemView.findViewById(R.id.item_simple_name_txt);
                headImgLayout = (LinearLayout) itemView.findViewById(R.id.item_simple_headimg_layout);
            }
        }

    }

//    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//        private static final int TYPE_ITEM = 0;
//        private static final int TYPE_FOOTER = 1;
//
//        private ArrayList<Simple> dataList;
//        private OnRecyclerItemClickListener onRecyclerItemClickListener;
//
//        public ArrayList<Simple> getDataList() {
//            return dataList;
//        }
//
//        public void setDataList(ArrayList<Simple> dataList) {
//            this.dataList = dataList;
//        }
//
//        public MyAdapter( OnRecyclerItemClickListener listener) {
//            this.onRecyclerItemClickListener = listener;
//        }
//
//        public MyAdapter(ArrayList<Simple> list, OnRecyclerItemClickListener listener) {
//            this.dataList = list;
//            this.onRecyclerItemClickListener = listener;
//        }
//
//        public Simple getItemByPosition(int position){
//            return dataList.get(position);
//        }
//
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            if (viewType == TYPE_ITEM) {
//                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_layout, parent, false);
//                return new MyViewHolder(itemView, onRecyclerItemClickListener);
//            } else if (viewType == TYPE_FOOTER) {
//                View view = View.inflate(parent.getContext(), R.layout.footerview, null);
//                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                return new MyFooterHolder(view);
//            }
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            if (holder instanceof MyViewHolder) {
//                MyViewHolder myViewHolder = (MyViewHolder) holder;
//                if (dataList.get(position).getShootingStyles() != null && dataList.get(position).getShootingStyles().size() > 0)
//                    myViewHolder.styleTxt.setText("风格: " + dataList.get(position).getShootingStyles().get(0).getShootingStyleName());
//
//                myViewHolder.nameTxt.setText(dataList.get(position).getContentName());
//
//                //加载摄影师
//                if (dataList.get(position).getPhotographer() != null && !TextUtils.isEmpty(dataList.get(position).getPhotographer().getPhotoUrl())) {
//                    myViewHolder.headImgLayout.setVisibility(View.VISIBLE);
//                    Picasso.with(getActivity()).load(dataList.get(position).getPhotographer().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(myViewHolder.photographerImg);
//                    //加造型师
//                    if (dataList.get(position).getStylist() != null && !TextUtils.isEmpty(dataList.get(position).getStylist().getPhotoUrl())) {
//                        Picasso.with(getActivity()).load(dataList.get(position).getStylist().getPhotoUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(myViewHolder.stylistImg);
//                    }
//                } else {
//                    myViewHolder.headImgLayout.setVisibility(View.GONE);
//                }
//
//                //加载内容
//                if (TextUtils.isEmpty(dataList.get(position).getContentUrl())) {
//                    Picasso.with(getActivity()).load(R.mipmap.ic_launcher).into(myViewHolder.contentImg);
//                } else {
//                    Picasso.with(getActivity()).load(dataList.get(position).getContentUrl()+ DimenUtil.listViewHorizontalStringDimension ).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(myViewHolder.contentImg);
//                }
//            }else if (holder instanceof MyFooterHolder) {
//                MyFooterHolder footerHolder = (MyFooterHolder) holder;
//                if (dataList.size() < totalCount) {
//                    footerHolder.progressBar.setVisibility(View.VISIBLE);
//                    footerHolder.txt.setText("正在加载...");
//                } else {
//                    footerHolder.progressBar.setVisibility(View.GONE);
//                    footerHolder.txt.setText("无更多数据");
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return dataList == null ? 0 : dataList.size();
//        }
//
//        public class MyFooterHolder extends RecyclerView.ViewHolder {
//            ContentLoadingProgressBar progressBar;
//            TextView txt;
//
//            public MyFooterHolder(View itemView) {
//                super(itemView);
//                progressBar = (ContentLoadingProgressBar) itemView.findViewById(R.id.footer_progressbar);
//                txt = (TextView) itemView.findViewById(R.id.footer_txt);
//            }
//        }
//
//        public final class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//            TextView styleTxt;
//            TextView nameTxt;
//            ImageView contentImg;
//            CircleImageView photographerImg;
//            CircleImageView stylistImg;
//            LinearLayout headImgLayout;
//            OnRecyclerItemClickListener onRecyclerItemClickListener;
//
//            public MyViewHolder(View itemView, OnRecyclerItemClickListener listener) {
//                super(itemView);
//                contentImg = (ImageView) itemView.findViewById(R.id.item_simple_pic_img);
//                photographerImg = (CircleImageView) itemView.findViewById(R.id.item_simple_photographer_head_img);
//                stylistImg = (CircleImageView) itemView.findViewById(R.id.item_simple_stylist_head_img);
//                styleTxt = (TextView) itemView.findViewById(R.id.item_simple_styles_txt);
//                nameTxt = (TextView) itemView.findViewById(R.id.item_simple_name_txt);
//                headImgLayout = (LinearLayout) itemView.findViewById(R.id.item_simple_headimg_layout);
//                onRecyclerItemClickListener = listener;
//                itemView.setOnClickListener(this);
//            }
//
//            @Override
//            public void onClick(View v) {
//                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
//            }
//        }
//    }
}
