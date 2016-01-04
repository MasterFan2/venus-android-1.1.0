package com.chinajsbn.venus.ui.uitest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.tool.widget.viewpager.ZoomOutPageTransformer;

import java.util.ArrayList;

public class TestCoordinatorActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private ViewPaperAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_coordinator);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        View view1 = View.inflate(this, R.layout.guide_view, null);
        View view2 = View.inflate(this, R.layout.guide_view, null);
        View view3 = View.inflate(this, R.layout.guide_view, null);

        ((ImageView)view1.findViewById(R.id.tv_pic)).setImageResource(R.drawable.ic_launcher);
        ((ImageView)view2.findViewById(R.id.tv_pic)).setImageResource(R.drawable.ic_launcher);
        ((ImageView)view3.findViewById(R.id.tv_pic)).setImageResource(R.drawable.ic_launcher);

        ((TextView)view1.findViewById(R.id.tv_title)).setText("IT蓝豹首页");
        ((TextView)view1.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#FF5000"));
        ((TextView)view2.findViewById(R.id.tv_title)).setText("android 特效");
        ((TextView)view2.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#49ca65"));
        ((TextView)view3.findViewById(R.id.tv_title)).setText("一键预览");
        ((TextView)view3.findViewById(R.id.tv_title)).setTextColor(Color.parseColor("#16c5c6"));

        ((TextView)view1.findViewById(R.id.tv_desc)).setText("IT蓝豹（www.itlanbao.com）\n");
        ((TextView)view2.findViewById(R.id.tv_desc)).setText("一个专业特效分享网站\n");
        ((TextView)view3.findViewById(R.id.tv_desc)).setText("欢迎学习代码上IT蓝豹\n");

        ArrayList<View> views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);

        adapter = new ViewPaperAdapter(views);

        viewPager.setOffscreenPageLimit(views.size());
        viewPager.setPageMargin(-dip2px(135));
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_coordinator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ViewPaperAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public ViewPaperAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            } else
                return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
