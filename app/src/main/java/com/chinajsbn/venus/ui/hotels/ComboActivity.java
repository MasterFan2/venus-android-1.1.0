package com.chinajsbn.venus.ui.hotels;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.bean.Combo;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.MasterTitleView;

@ActivityFeature(layout = R.layout.activity_combo)
public class ComboActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.content_container)
    private LinearLayout container;

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    private Combo combo ;

    @Override
    public void initialize() {
        combo = (Combo) getIntent().getSerializableExtra("combo");

        titleView.setTitleText(combo.getMealPackName());
        final int size = combo.getMealPackDishList().length;
//        TextView  temp= new TextView(context);
//        temp.setTextColor(getResources().getColor(android.R.color.white));
//        temp.setText(combo.getMealPackName());
//        temp.setTextSize(20);
//        container.addView(temp);

        for (int i = 0; i < size ; i++){
            TextView tv = new TextView(context);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            tv.setTextColor(getResources().getColor(android.R.color.white));
            tv.setPadding(10, 10, 10, 10);
            tv.setText(combo.getMealPackDishList()[i]);
            container.addView(tv);
        }
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @OnClick(R.id.m_title_left_btn)
    public void back(View view){
        animFinish();
    }
}
