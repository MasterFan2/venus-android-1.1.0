package com.chinajsbn.venus.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.utils.MTViewUtils;
import com.tool.widget.dialog.MTDialog;
import com.tool.widget.progress.ProgressView;

/**
 * @author Master
 * @TODO
 * @date 2014-10-4 PM 6:38:49
 */
public abstract class BaseActivity extends Activity {

    //
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Check is set to full screen, no title and so on.
        MTViewUtils.injectFeature(this);

        //
        super.onCreate(savedInstanceState);

        //
        context = this;

        //
        MTViewUtils.inject(this);

        //
        initialize();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onKeydown();
        }
        return super.onKeyDown(keyCode, event);
    }

    private MTDialog dialog;
    private ProgressView progressView;
    public void dialogShow(MTDialog dialog, ProgressView progressView){

        this.progressView = progressView;
        this.dialog = dialog;

        dialog.show();
        progressView.start();
    }

    public void dialogDismiss(){
        if(dialog == null)
            return;
        dialog .dismiss();
        progressView.stop();
    }

    public abstract void onKeydown();

    /**
     * Initialization operations, such as data acquisition, etc.
     */
    public abstract void initialize();

    /**
     * anim launch Activity
     *
     * @param clazz class
     */
    public void animStart(Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
        overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    public void animStart(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    public void animStartWithDragger(Class clazz){
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     * anim finish Activity
     */
    public void animFinish() {
        finish();
        overridePendingTransition(0, R.anim.roll_down);
    }

}
