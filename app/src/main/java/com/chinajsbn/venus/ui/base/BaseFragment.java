package com.chinajsbn.venus.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.utils.MTViewUtils;

/**
 * Created by MasterFan on 2015/5/5.
 * description:
 */
public abstract class BaseFragment extends Fragment {

    private Snackbar snackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentFeature config = this.getClass().getAnnotation(FragmentFeature.class);
        if(config == null) throw new IllegalArgumentException("not set resources");
        if(config.layout() <= 0) throw new RuntimeException("resources error!");

        //
        View view = inflater.inflate(config.layout(), container, false);

        MTViewUtils.inject(this, view);

        //
        initialize();

        //
        return view;
    }

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
