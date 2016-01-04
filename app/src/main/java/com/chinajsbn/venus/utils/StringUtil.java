package com.chinajsbn.venus.utils;

import com.chinajsbn.venus.utils.entity.Dimension;

/**
 * Created by MasterFan on 2015/7/23.
 * description:
 */
public class StringUtil {

    /**
     *
     * @param url
     * @return
     */
    public static String getFileName(final String url){
        return url.substring(url.lastIndexOf('/')+1, url.length());
    }

    /**
     * return dimension by the image url
     * @param url
     * @return Dimension
     */
    public static Dimension getDimenByUrl(String url){
        if(!url.contains("_")) {
            return null;
        }
        String subStr = getFileName(url);
        String dims = subStr.split("_")[1];

        int width = Integer.parseInt(dims.split("x")[0]);
        String strHeight = dims.split("x")[1];
        strHeight = strHeight.substring(0, strHeight.indexOf("."));

        int height = Integer.parseInt(strHeight);
        return new Dimension(height, width);
    }
}
