package com.chinajsbn.venus.ui.login;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.LoginResp;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.chinajsbn.venus.utils.MatcherUtil;
import com.chinajsbn.venus.utils.PreUtil;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tool.widget.common.Button;
import com.tool.widget.common.EditText;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.dialog.OnClickListener;
import com.tool.widget.dialog.ViewHolder;
import com.tool.widget.progress.ProgressView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_login, statusBarColor = R.color.gray_900)
public class LoginActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.login_account_edit)
    private EditText accountEdit;

    @ViewInject(R.id.login_password_edit)
    private EditText passwordEdit;

    @ViewInject(R.id.login_login_btn)
    private Button loginBtn;


    private String account;
    private String password;

//    @OnClick(R.id.login_samples_btn)
//    public void goSample(View view) {
//        //PersonalCenterActivity
//         animStart(TempSimpleActivity.class);
////        animStart(PersonalCenterActivity.class);
//    }

    @Override
    public void onKeydown() {
        animFinish();
    }

    @OnClick(R.id.login_reg_btn)
    public void goReg(View view) {
        animStart(RegisterActivity.class);
    }

    @OnClick(R.id.login_forget_btn)
    public void goForget(View view) {
        animStart(ForgetActivity.class);
    }

//    @OnClick(R.id.login_upgrade_btn)
//    public void upgrade(View view) {
//        animStart(UpgradeActivity.class);
//    }

    @OnClick(R.id.login_login_btn)
    public void login(View view) {
        view.setEnabled(false);
        account = accountEdit.getText().toString();
        password = passwordEdit.getText().toString();

        if (TextUtils.isEmpty(account)) {
            accountEdit.setError("请输入用户名");
            view.setEnabled(true);
            return;
        } else {
            accountEdit.clearError();
        }
        if (!MatcherUtil.isPhone(account)) {
            accountEdit.setError("手机号格式错误，请检查!");
            view.setEnabled(true);
            return;
        } else {
            accountEdit.clearError();
        }
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("请输入密码");
            view.setEnabled(true);
            return;
        }

        if (password.length() < 6) {
            passwordEdit.setError("密码长度在六位以上.");
            view.setEnabled(true);
            return;
        }

        passwordEdit.clearError();

        View v = LayoutInflater.from(context).inflate(R.layout.simple_dialog_wait, null);
        final ProgressView progressView = (ProgressView) v.findViewById(R.id.progress_pv_circular_inout);

        ViewHolder holder = new ViewHolder(v);
        MTDialog dialog = new MTDialog.Builder(this)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.CENTER)
                .setOnClickListener(this)
                .setBackgroundResourceId(R.drawable.dialog_background)
                .create();

        dialogShow(dialog, progressView);

        HttpClient.getInstance().login(account, password, 1, cb);

//        new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                T.s(context, "登录成功");
//                dialogDismiss();
//                animStart(HomeActivity.class);
//                finish();
//            }
//        }.sendEmptyMessageDelayed(0, 1000);


    }

    private Callback<LoginResp> cb = new Callback<LoginResp>() {
        @Override
        public void success(LoginResp loginResp, Response response) {
            loginBtn.setEnabled(true);
            dialogDismiss();
            if(loginResp.getCode() == 1001011){
                T.s(context, "用户名密码错误,请检查");
            }else if(loginResp.getCode() == 200){
                animStart(HomeActivity.class);
                PreUtil.saveLoginInfo(context, account, password);
                finish();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("失败");
            dialogDismiss();
        }
    };


    @Override
    public void initialize() {
        accountEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String account = accountEdit.getText().toString();
                    if (TextUtils.isEmpty(account)) {
                        accountEdit.setError("请输入用户名.");
                    } else {
                        accountEdit.clearError();
                    }
                }
                return false;
            }
        });

        passwordEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    String password = passwordEdit.getText().toString();
                    if (TextUtils.isEmpty(password)) {
                        passwordEdit.setError("请输入密码.");
                        return false;
                    }
                    if (password.length() < 6) {
                        passwordEdit.setError("密码长度在6位以上..");
                        return false;
                    }
                    passwordEdit.clearError();
                }
                return false;
            }
        });
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    @Override
    public void onClick(MTDialog dialog, View view) {

    }
}
