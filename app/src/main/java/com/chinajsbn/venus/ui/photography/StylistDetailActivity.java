package com.chinajsbn.venus.ui.photography;

import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_stylist_detail)
public class StylistDetailActivity extends MBaseFragmentActivity {

    @Override
    public void initialize() {
        int id  = getIntent().getIntExtra("id" , -1);
        HttpClient.getInstance().getStylistDetail("" + id, cb);
    }

    private Callback<Base<ArrayList<Photographer>>> cb = new Callback<Base<ArrayList<Photographer>>>() {
        @Override
        public void success(Base<ArrayList<Photographer>> weddingDress, Response response) {
            T.s(context, "Get data success ! datasiz => " + weddingDress.getData().size());
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "Get data error! ");
        }
    };

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view){
        animFinish();
    }
}
