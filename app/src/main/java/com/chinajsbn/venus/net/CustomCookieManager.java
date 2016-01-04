package com.chinajsbn.venus.net;

import android.content.Context;
import android.util.Log;

import com.tool.PreferenceUtils;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by chenjianjun on 15-4-13.
 * <p/>
 * Beyond their own, let the future
 */
public class CustomCookieManager extends CookieManager {

    private Context mContext;

    public CustomCookieManager(Context context) {
        mContext = context;
        super.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {

        super.put(uri, responseHeaders);

        if (responseHeaders != null && responseHeaders.get("Set-Cookie") != null)
            for (String string : responseHeaders.get("Set-Cookie")) {

                Log.e("chenjianjun", "----" + string);

                // 保存
                if (string.contains("JSESSIONID")) {
                    PreferenceUtils.saveValue(mContext,
                            PreferenceUtils.PREFERENCE_SESSION_ID,
                            PreferenceUtils.DataType.STRING,
                            string.split("=")[1]);
                }else if (string.contains("TOKEN")) {
                    PreferenceUtils.saveValue(mContext,
                            PreferenceUtils.PREFERENCE_TOKEN,
                            PreferenceUtils.DataType.STRING,
                            string.split("=")[1]);
                }else if (string.contains("TICKET")) {
                    PreferenceUtils.saveValue(mContext,
                            PreferenceUtils.PREFERENCE_TICKET,
                            PreferenceUtils.DataType.STRING,
                            string.split("=")[1]);
                }
            }
    }

}
