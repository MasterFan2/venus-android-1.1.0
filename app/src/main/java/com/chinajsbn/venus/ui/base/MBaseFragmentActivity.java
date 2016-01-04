package com.chinajsbn.venus.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.utils.MTViewUtils;
import com.chinajsbn.venus.utils.T;

/**
 * Created by MasterFan on 2015/5/5.
 * description:
 */
public abstract class MBaseFragmentActivity extends FragmentActivity {
    public Context context;

    @Override
    protected void onCreate(Bundle arg0) {

        MTViewUtils.injectFeature(this);

        // TODO Auto-generated method stub
        super.onCreate(arg0);

        //
        context = this;

        //
        MTViewUtils.inject(this);

        //
        initialize();

    }

    public void serverError() {
        T.s(context, "服务器错误");
    }

    public void setStatusBarColor(int statusBarColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(statusBarColor);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    /**
     * Initialization operations, such as data acquisition, etc.
     */
    public abstract void initialize();

    /**
     * anim launch Activity
     * @param clazz class
     */
    public void animStart(Class clazz){
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     * anim launch Activity
     * @param intent intent
     */
    public void animStart(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }


    /**
     * anim launch Activity
     */
    public void animFinish(){
        finish();
        overridePendingTransition(0, R.anim.roll_down);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            return onKeydown();
        }
        return super.onKeyDown(keyCode, event);
    }


    public abstract boolean onKeydown();
}
