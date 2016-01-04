package com.chinajsbn.venus.ui.login;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.BaseResp;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.MatcherUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.common.Button;
import com.tool.widget.common.EditText;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;

import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * create by master fan on 2015/7/8
 * 注册
 */
@ActivityFeature(layout = R.layout.activity_register, statusBarColor = R.color.gray_900)
public class RegisterActivity extends MBaseFragmentActivity implements OnClickListener {

    @ViewInject(R.id.reg_register_btn)
    private Button regBtn;

    @ViewInject(R.id.reg_getCode_btn)
    private Button getCodeBtn;

    @ViewInject(R.id.reg_phone_edit)
    private EditText unameEdit;

    @ViewInject(R.id.reg_code_edit)
    private EditText codeEdit;

    //重发间隔
    private int reSend = 60;
    private boolean isRun = false;
    private Timer timer;
    private TimerTask task;

    //
    private MTDialog dialog;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            reSend--;
            if (reSend <= 0) {
                getCodeBtn.setEnabled(true);
                getCodeBtn.setText("获取验证码");
                timer.cancel();
                task = null;
                timer = null;
                isRun = false;
                reSend = 60;
                return;
            }
            getCodeBtn.setText((reSend < 10 ? "0" + reSend : reSend) + " 秒后重新发");
        }
    };

    @Override
    public void initialize() {
        unameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String phone = unameEdit.getText().toString();
                    if (!TextUtils.isEmpty(phone)) {
                        regBtn.setEnabled(true);
                        regBtn.applyStyle(R.style.RaiseColorButtonRippleStyle);
                    }
                }
            }
        });
    }

    @OnClick(R.id.reg_register_btn)
    public void signup(View view) {
        String uname = unameEdit.getText().toString();
        String code = codeEdit.getText().toString();

        if (TextUtils.isEmpty(uname)) {
            unameEdit.setError("请输入手机号!");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            codeEdit.setError("请输入收到的验证码!");
            return;
        }

        HttpClient.getInstance().signup(uname, code, 1, 100, signupCallback);
    }

    /**
     * 注册回调
     */
    private Callback<BaseResp> signupCallback = new Callback<BaseResp>() {
        @Override
        public void success(BaseResp baseResp, Response response) {
            if (baseResp.getCode() == 200) {
                ViewHolder holder = new ViewHolder(R.layout.dialog_signup_tips_layout);
                dialog = new MTDialog.Builder(context)
                        .setContentHolder(holder)
                        .setHeader(R.layout.dialog_signup_tips_head_layout)
                        .setFooter(R.layout.dialog_signup_tips_foot_layout)
                        .setCancelable(false)
                        .setGravity(MTDialog.Gravity.CENTER)
                        .setOnClickListener(RegisterActivity.this)
                        .create();
                dialog.show();
            } else {
                T.s(context, "注册失败，请稍后重试!");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "注册失败，请稍后重试!");
        }
    };

    /**
     * 开始计时
     */
    private void startTimer() {
        if (timer == null)
            timer = new Timer();
        if (task == null)
            task = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };

        timer.schedule(task, 0, 1000);
    }

    @OnClick(R.id.reg_getCode_btn)
    public void getCode(View view) {

        view.setEnabled(false);

        String phone = unameEdit.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            unameEdit.setError("请输入手机号");
            view.setEnabled(true);
            return;
        }
        if (!MatcherUtil.isPhone(phone)) {
            unameEdit.setError("手机号格式错误，请检查!");
            view.setEnabled(true);
            return;
        } else {
            unameEdit.clearError();
        }

        startTimer();

        isRun = true;

        HttpClient.getInstance().getSmsCode(phone, 0, cb);
    }

    private Callback<BaseResp> cb = new Callback<BaseResp>() {
        @Override
        public void success(BaseResp baseResp, Response response) {
            if (baseResp.getCode() == 200) {
                T.s(context, "短信发送成功,请注意查收!");
                unameEdit.setEnabled(false);
            } else {
                T.s(context, "短信发送失败!请检查网络");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "短信发送失败!请检查网络");
        }
    };

    @Override
    protected void onDestroy() {

        if (isRun) {
            timer.cancel();
            task.cancel();
        }
        task = null;
        timer = null;
        super.onDestroy();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        if (view.getId() == R.id.footer_confirm_button) {
            dialog.dismiss();
        }
    }
}
