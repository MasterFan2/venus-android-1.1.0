package com.chinajsbn.venus.ui.photography;

import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClient;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.WeddingDress;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 婚纱详情
 */
@ActivityFeature(layout = R.layout.activity_wedding_dress)
public class WeddingDressDetailActivity extends MBaseFragmentActivity {

    @Override
    public void initialize() {
        String idAndDetail[] = getIntent().getStringExtra("idAndDetail").split("@");
        HttpClient.getInstance().getWeddingDressDetail(idAndDetail[0], idAndDetail[1], cb);
    }

    private Callback<Base<ArrayList<WeddingDress>>> cb = new Callback<Base<ArrayList<WeddingDress>>>() {
        @Override
        public void success(Base<ArrayList<WeddingDress>> weddingDress, Response response) {
            T.s(context, "Get data success ! datasiz => " + weddingDress.getData().size());

//            tempDataList = simpleResp.getData();
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
