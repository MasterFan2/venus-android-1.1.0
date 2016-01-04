package com.chinajsbn.venus.ui.error;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.crash.CustomActivityOnCrash;

@ActivityFeature(layout = R.layout.activity_crash)
public class CrashActivity extends MBaseFragmentActivity {


    @Override
    public void initialize() {

    }

    @Override
    public boolean onKeydown() {
        return true;
    }

    @OnClick(R.id.crash_send_report_btn)
    public void sendReport(View view) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("错误bug信息")
                .setMessage(CustomActivityOnCrash.getAllErrorDetailsFromIntent(context, getIntent()))
                .setPositiveButton("确定", null)
                .show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.sample_text_size));
    }

    @OnClick(R.id.crash_restart_btn)
    public void restart(View view) {
        final Class<? extends Activity> restartActivityClass = CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent());
        if (restartActivityClass != null) {
            Intent intent = new Intent(context, restartActivityClass);
            finish();
            startActivity(intent);
        }
    }
}
