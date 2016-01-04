package com.chinajsbn.venus.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.mt_listview.MasterListView;
import com.tool.widget.mt_listview.MyListView;

@ActivityFeature(layout = R.layout.activity_other)
public class OtherActivity extends BaseActivity implements MasterListView.OnRefreshListener {

    @ViewInject(R.id.myListView)
    private MyListView listView;

    @Override
    public void onKeydown() {

    }

    @Override
    public void initialize() {
        listView.setPullLoadEnable(false);
        listView.setOnRefreshListener(this, 0);
        listView.setAdapter(new MAdapter());

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.startRefresh();
            }
        }.sendEmptyMessageDelayed(0, 500);

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.stopRefresh();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onRefresh(int id) {
        listView.startRefresh();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                listView.stopRefresh();
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onLoadMore(int id) {

    }

    class MAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
                holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText("Text  " + position);
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }
}
