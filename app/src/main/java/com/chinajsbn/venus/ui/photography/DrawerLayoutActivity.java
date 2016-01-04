package com.chinajsbn.venus.ui.photography;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

@ActivityFeature(layout = R.layout.activity_drawer_layout)
public class DrawerLayoutActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.content_frame)
    private FrameLayout frameLayout;

    @ViewInject(R.id.click_tv)
    private TextView    tv;

    @Override
    public void initialize() {
//        Opt1Fragment fragment = new Opt1Fragment();
//        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onKeydown() {
        return true;
    }

}
