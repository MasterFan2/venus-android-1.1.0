package com.chinajsbn.venus.ui.BigPicutreActivity;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;
import com.tool.widget.MasterTitleView;
import com.tool.widget.TouchImageView;

@ActivityFeature(layout = R.layout.activity_big_picture)
public class BigPictureActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.titleView)
    private MasterTitleView titleView;

    @ViewInject(R.id.big_picture_img)
    private TouchImageView img;

    //标识文字是否    显示/隐藏    true:显示     false:隐藏
    private boolean show = true;

    @Override
    public void initialize() {
        String url = getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url).into(img);
        }
    }

    @OnClick(R.id.big_picture_img)
    public void onClick(View view){
        if(show){
            hide();
        }else{
            show();
        }
        show = !show;
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

    private void show() {
        titleView .animate().translationY( 0).setInterpolator(new DecelerateInterpolator(2)) ;
    }

    private void hide() {
        titleView .animate().translationY(-titleView.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
