package com.chinajsbn.venus.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.SegmentControl;
import com.tool.widget.listanim.prepared.SwingBottomInAnimationAdapter;
import com.tool.widget.refresh.PullRefreshLayout;

@ActivityFeature(layout = R.layout.activity_list_content)
public class ListContentActivity extends BaseActivity {

    private String mUrl = "http://img5.duitang.com/uploads/blog/201407/17/20140717113117_mUssJ.thumb.jpeg";
    private long mStartLoadingTime = -1;
    private boolean mImageHasLoaded = false;

    @ViewInject(R.id.list_pullRefreshLayout)
    private PullRefreshLayout pullRefreshLayout;

    @ViewInject(R.id.segment_control)
    private SegmentControl segmentControl;

    @ViewInject(R.id.listView)
    private ListView listView;

    private LayoutInflater inflater;

    private int currentSegmentIndex = 0;// 0, 1, 2

    @Override
    public void onKeydown() {
        animFinish();
    }

    @Override
    public void initialize() {
        inflater = LayoutInflater.from(context);
        segmentControl.setOnSegmentControlClickListener(new SegmentControl.OnSegmentControlClickListener() {
            @Override
            public void onSegmentControlClick(int index) {
                switch (index) {
                    case 0:
                        if (currentSegmentIndex != index) {
                            showLargePic();
                            currentSegmentIndex = 0;
                        }
                        break;
                    case 1:
                        if (currentSegmentIndex != index) {
                            showPicAndText();
                            currentSegmentIndex = 1;
                        }
                        break;
                    case 2:
                        if (currentSegmentIndex != index) {
                            showSmallText();
                            currentSegmentIndex = 2;
                        }
                        break;
                }
            }
        });

        pullRefreshLayout.setRefreshing(true);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        pullRefreshLayout.setRefreshing(false);
                    }
                }.sendEmptyMessageDelayed(0, 3000);
            }
        });

        listView.setDivider(null);

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                pullRefreshLayout.setRefreshing(false);
                showLargePic();
            }
        }.sendEmptyMessageDelayed(0, 3000);
    }

    /**
     * 显示大图列表
     */
    private void showLargePic() {
        LargePicAdapter largePicAdapter = new LargePicAdapter();
        SwingBottomInAnimationAdapter swingLeftInAnimationAdapter = new SwingBottomInAnimationAdapter(largePicAdapter);
        swingLeftInAnimationAdapter.setAbsListView(listView);
        listView.setAdapter(swingLeftInAnimationAdapter);
    }

    /**
     * 图文列表
     */
    private void showPicAndText() {
        PicAndTxtAdapter largePicAdapter = new PicAndTxtAdapter();
        SwingBottomInAnimationAdapter swingLeftInAnimationAdapter = new SwingBottomInAnimationAdapter(largePicAdapter);
        swingLeftInAnimationAdapter.setAbsListView(listView);
        listView.setAdapter(swingLeftInAnimationAdapter);
    }

    /**
     * 文字列表
     */
    private void showSmallText() {
        SmallPicAdapter largePicAdapter = new SmallPicAdapter();
        SwingBottomInAnimationAdapter swingLeftInAnimationAdapter = new SwingBottomInAnimationAdapter(largePicAdapter);
        swingLeftInAnimationAdapter.setAbsListView(listView);
        listView.setAdapter(swingLeftInAnimationAdapter);
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view){
        animFinish();
    }

    class LargePicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listview_large_pic, parent, false);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder {

        }
    }

    class PicAndTxtAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listview_picture_and_txt, parent, false);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder {

        }
    }

    class SmallPicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.listview_text_smallpic, parent, false);

                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        class ViewHolder {

        }
    }
}
