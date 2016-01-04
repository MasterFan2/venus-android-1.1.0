package com.chinajsbn.venus.utils;

import android.content.Context;

/**
 * Created by 13510 on 2015/12/19.
 */
public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * * java 代码中设置的宽/高单位为PX
     * xml  中设置的宽/高为 dip /dp
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取婚礼人item的宽
     * rootView padding 16dp
     * headImg  width   70dp
     * rightLayout marginLeft 8dp
     * item image margin left 5dp
     *
     * @param context
     * @return
     */
    public static int getF4ItemWidth(Context context) {
        int doubleImageWidth = DimenUtil.screenWidth - (dip2px(context, 16) * 2) - dip2px(context, 70) - dip2px(context, 8) - dip2px(context, 5);
        return doubleImageWidth / 2;
    }

    /**
     * 获取婚礼人item的宽
     */
    public static int getF4ItemHeight(Context context) {
        return 3 * getF4ItemWidth(context) / 2;
    }

    public static String getF4ImageSuffix(Context context){
        return "@" + getF4ItemWidth(context) + "w_" + getF4ItemHeight(context) + "h_";
    }
}
