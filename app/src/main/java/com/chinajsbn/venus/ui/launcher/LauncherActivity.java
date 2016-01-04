package com.chinajsbn.venus.ui.launcher;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.VersionInfo;
import com.chinajsbn.venus.net.bean.VersionResp;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.ui.upgrade.UpgradeActivity;
import com.chinajsbn.venus.utils.NetworkUtil;
import com.chinajsbn.venus.utils.PackageUtil;
import com.chinajsbn.venus.utils.PreUtil;
import com.chinajsbn.venus.utils.T;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_launcher, full_screen = true)
public class LauncherActivity extends MBaseFragmentActivity implements OnClickListener {

    private MTDialog dialog;

    private VersionInfo info;


    @Override
    public void initialize() {

        if(NetworkUtil.hasConnection(context)) {
            HttpClient.getInstance().checkUpgrade(2, PackageUtil.getVersion(context), 1, cb);
        }else {
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    Intent intent = new Intent(context, HomeActivity.class);
                    animStart(intent);
                    finish();
                }
            }.sendEmptyMessageDelayed(0, 3500);
        }
    }

    private Callback<VersionResp> cb = new Callback<VersionResp>() {
        @Override
        public void success(VersionResp versionResp, Response response) {
            if (versionResp.getCode() == 200) {
                info = versionResp.getData();//1：强制更新        0：正常更新

                //没有版本信息
                if (info == null || TextUtils.isEmpty(info.getAppInfo())) {
                    checkLogin();
                } else {
                    if (info.getIsForcedUpdate() == 1) {//强制更新
                        Intent intent = new Intent(context, UpgradeActivity.class);
                        intent.putExtra("info", info);
                        animStart(intent);
                        finish();
                    } else {
                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_signup_tips_layout, null);
                        TextView txt = (TextView) view.findViewById(R.id.signup_tips_txt);
                        txt.setText("有新的版本发布了: \n\n" + info.getAppInfo().replace("@", "\n"));
                        ViewHolder holder = new ViewHolder(view);
                        dialog = new MTDialog.Builder(context)
                                .setContentHolder(holder)
                                .setHeader(R.layout.dialog_signup_tips_head_layout)
                                .setFooter(R.layout.dialog_launcher_foot_layout)
                                .setCancelable(false)
                                .setGravity(MTDialog.Gravity.CENTER)
                                .setOnClickListener(LauncherActivity.this)
                                .create();
                        dialog.show();
                    }
                }
            } else {
                T.s(context, "系统维护中 ...");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "网络不是很稳定,请稍后再试.");
        }
    };

    @Override
    public boolean onKeydown() {
        return true;
    }

    private void checkLogin() {
        final boolean isLogin = PreUtil.isLogin(context);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Intent intent = null;
//                if (isLogin)
                    intent = new Intent(context, HomeActivity.class);
//                else
//                    intent = new Intent(context, LoginActivity.class);
                //
                animStart(intent);
                finish();
            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.footer_upgrade_cancel_button:
                dialog.dismiss();
                checkLogin();
                break;
            case R.id.footer_upgrade_upgrade_button:
                Intent intent = new Intent(context, UpgradeActivity.class);
                intent.putExtra("info", info);
                animStart(intent);
                finish();
                break;

        }
    }
}
