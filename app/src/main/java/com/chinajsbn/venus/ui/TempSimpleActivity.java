package com.chinajsbn.venus.ui;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.chinajsbn.venus.ui.login.LoginActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.common.Button;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.progress.ProgressView;

/**
 * create bu master fan
 * description:各种示例
 */
@ActivityFeature(layout = R.layout.activity_simple)
public class TempSimpleActivity extends BaseActivity implements OnClickListener {

    private MTDialog dialog;

    @ViewInject(R.id.alert_waiting)
    private Button alertWaitBtn;

    @ViewInject(R.id.alert_cancel)
    private Button alertConfirmCancelBtn;

    @Override
    public void onKeydown() {

    }

    @Override
    public void initialize() {

    }

    @OnClick(R.id.alert_waiting)
    public void alertWait(View view){
        View v = LayoutInflater.from(context).inflate(R.layout.simple_dialog_wait, null);
        final ProgressView progressView = (ProgressView) v.findViewById(R.id.progress_pv_circular_inout);

        ViewHolder holder = new ViewHolder(v);
        dialog = new MTDialog.Builder(this)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.CENTER)
                .setOnClickListener(this)
                .setBackgroundResourceId(R.drawable.dialog_background)
                .create();
        dialog.show();
        progressView.start();

        new Handler(){
            @Override
            public void handleMessage(Message msg) {
                progressView.stop();
                dialog.dismiss();
            }
        }.sendEmptyMessageDelayed(0, 3000);
    }

    @OnClick(R.id.alert_cancel)
    public void alertCancel(View view){
        ViewHolder holder = new ViewHolder(R.layout.simple_dialog_comfrim_cancel);
        dialog = new MTDialog.Builder(this)
                .setContentHolder(holder)
                .setHeader(R.layout.header)
                .setFooter(R.layout.footer)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.CENTER)
                .setOnClickListener(this)
                .create();
        dialog.show();
    }

    @OnClick(R.id.listView_test)
    public void testListView(View view){
        animStart(OtherActivity.class);
    }

    @OnClick(R.id.login_btn)
    public void goLogin(View view){
        animStart(LoginActivity.class);
    }

    @OnClick(R.id.scroll_btn)
    public void scrollTest(View view){
        animStart(ScrollActivity.class);
    }

    @OnClick(R.id.viewPager_btn)
    public void viewPager(View view){
        animStart(ViewPagerActivity.class);
    }

    @OnClick(R.id.simple_btn)
    public void simpleList(View view){
        animStart(SimpleListAcitvity.class);
    }

    @OnClick(R.id.simple_calendar_btn)
    public void calendar(View view){
        animStart(CalendarActivity.class);
    }

    @OnClick(R.id.recycle_btn)
    public void recycleTest(View view){
        animStart(RecycleActivity.class);
    }

    @OnClick(R.id.register_btn)
    public void regs(View view){animStart(ListContentActivity.class);}



    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()){
            case R.id.footer_close_button:
                dialog.dismiss();
                break;
            case R.id.footer_confirm_button:
                dialog.dismiss();
                break;
        }
    }
}
