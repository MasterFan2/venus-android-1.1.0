package com.chinajsbn.venus.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

@ActivityFeature(layout = R.layout.activity_recycle)
public class RecycleActivity extends BaseActivity {

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView;

    @Override
    public void onKeydown() {

    }

    @Override
    public void initialize() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 创建数据集
        String[] dataset = new String[100];
        for (int i = 0; i < dataset.length; i++) {
            dataset[i] = "item" + i;
        }

        recyclerView.setAdapter(new MyAdapter(dataset));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onRecyclerViewItemClickListener();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements OnRecyclerViewItemClickListener {

        private String[] datas;

        public MyAdapter(String[] datas) {
            this.datas = datas;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.demo_item_recycler, null);
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // set data info
           // holder.mTextView.setText(datas[position]);
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        @Override
        public void onRecyclerViewItemClickListener() {

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);

            }
        }
    }
}
