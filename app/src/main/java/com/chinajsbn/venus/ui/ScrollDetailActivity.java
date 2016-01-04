package com.chinajsbn.venus.ui;

import android.os.Bundle;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.dragger.DraggerPosition;
import com.tool.widget.dragger.DraggerView;

@ActivityFeature(layout = R.layout.activity_scroll_detail, statusBarColor = R.color.base_color)
public class ScrollDetailActivity extends MBaseFragmentActivity {

    private static final String CAN_ANIMATE = "can_animate";

    @ViewInject(R.id.dragger_view)
    private DraggerView draggerView;

    @Override
    public void initialize() {
        draggerView.setDraggerPosition(DraggerPosition.TOP);
    }

    @Override
    public boolean onKeydown() {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        draggerView.closeActivity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CAN_ANIMATE, draggerView.getCanAnimate());
    }
}
