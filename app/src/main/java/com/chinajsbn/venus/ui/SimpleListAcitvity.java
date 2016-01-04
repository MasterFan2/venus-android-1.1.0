package com.chinajsbn.venus.ui;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.net.HttpClientApi;
import com.chinajsbn.venus.net.bean.BaseResp;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_simple_list_acitvity)
public class SimpleListAcitvity extends BaseActivity {

    @Override
    public void onKeydown() {

    }

    @Override
    public void initialize() {
        HttpClientApi.getInstance().initialize(context);

//        HttpClientApi.getInstance().getSimpleList("1", 1, 10, callback);
    }

    private Callback<BaseResp> callback = new Callback<BaseResp>() {
        @Override
        public void success(BaseResp s, Response response) {
            if(s != null){
                System.out.println(s.getMessage());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            System.out.println("ERROR");
        }
    };
}
