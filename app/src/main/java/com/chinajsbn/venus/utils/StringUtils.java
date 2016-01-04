package com.chinajsbn.venus.utils;

import android.text.TextUtils;

/**
 * Created by 13510 on 2015/10/8.
 */
public class StringUtils {
    public static String getFileNameByUrl(String url) {
        if(TextUtils.isEmpty(url)) return null;
        return url.substring(url.lastIndexOf("/") + 1, url.length());
    }
}
