package com.chinajsbn.venus.ui.base;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.chinajsbn.venus.R;

/**
 * Created by 13510 on 2015/10/28.
 */
public abstract class MBaseFragment extends Fragment {
    private Snackbar snackbar;
    public void snackShow(View v, String msg){
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void snackShow(View v, String msg, String action, View.OnClickListener listener){
        snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_INDEFINITE).setAction(action, listener);
        snackbar.show();
    }

    public void snackDismiss(){
        if(snackbar != null && snackbar.isShown()){
            snackbar.dismiss();
            snackbar = null;
        }
    }

    /**
     *
     * @param intent
     */
    public void animStart(Intent intent){
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     *
     * @param clazz
     */
    public void animStart(Class clazz){
        Intent intent = new Intent(getActivity(), clazz);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.roll_up, R.anim.roll);
    }

    /**
     * init
     */
    public abstract void initialize();

    public abstract void show();
    public abstract void hide();
}
