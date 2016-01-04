package com.chinajsbn.venus.ui.diamond;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ActivityFeature(layout = R.layout.activity_diamond)
public class DiamondActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.webView)
    private WebView webView;

    @Override
    public void initialize() {
        //webView;webView
        WebSettings webSettings = webView.getSettings();

        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        //设置可以访问文件
        webSettings.setAllowFileAccess(true);

        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);

        //加载需要显示的网页
        webView.loadUrl("http://www.weihive.cn/weixin/index.php?g=Wap&m=Product&a=index&token=ibhzwg1436836751&is_web=1");
        webView.setWebViewClient(new webViewClient());
    }

    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.m_title_left_btn)
    public void onBack(View view) {
        animFinish();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

}
