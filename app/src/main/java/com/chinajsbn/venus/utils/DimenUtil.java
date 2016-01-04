package com.chinajsbn.venus.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chinajsbn.venus.utils.entity.Dimension;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * Created by master on 15-8-20.
 */
public class DimenUtil {

    //详情后缀
    public static final String SUFFIX = "|watermark=1&object=c2h1aXlpbi5wbmc&t=40&p=5&y=10&x=10";

    public static int screenWidth = 0;
    public static int screenHeight = 0;

    public static int targetHeight = 0;

    //缩放比例
    public static int DEFAULT_SCALE = 2;

    public static int DEFAULT_PHOTOGRAPHER_STYLIST_SCALE = 3;

    public static final String DEFAULT_QUALITY = "60Q";

    public enum emRatio {
        W3H2,
        W2H3
    }

    public static String getSuffixUTF8(){
        try {
            return URLEncoder.encode(SUFFIX, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return SUFFIX;
        }
    }

    /**
     * 获取竖图， stylist & photographer
     *
     * @param baseScale
     * @return
     */
    public static Dimension getPhotoStylistDimensionWidthHeight(int baseScale) {
        Dimension dimension = new Dimension(0, 0);
        dimension.setWidth(baseScale / DEFAULT_PHOTOGRAPHER_STYLIST_SCALE);
        dimension.setHeight(baseScale / DEFAULT_PHOTOGRAPHER_STYLIST_SCALE / 2 * 3);
        return dimension;
    }

    public static String getPhotoStylistStringDimen(int scale){
        Dimension dimension = getPhotoStylistDimensionWidthHeight(scale);
        return "@" + dimension.getWidth()+"w_" + dimension.getHeight()+"h_" + DEFAULT_QUALITY;
    }

    /**
     * @param type
     * @param baseScale
     * @return
     */
    public static Dimension getDimensionWidthHeight(emRatio type, int baseScale) {

        Dimension dimension = new Dimension(0, 0);
        switch (type) {
            case W3H2: {
                dimension.setWidth(baseScale / DEFAULT_SCALE);
                dimension.setHeight(baseScale / DEFAULT_SCALE / 3 * 2);
                targetHeight = dimension.getHeight() * DEFAULT_SCALE;
                break;
            }
            case W2H3: {
                dimension.setWidth(baseScale / DEFAULT_PHOTOGRAPHER_STYLIST_SCALE);
                dimension.setHeight(baseScale / DEFAULT_PHOTOGRAPHER_STYLIST_SCALE / 2 * 3);
                targetHeight = dimension.getHeight() * DEFAULT_PHOTOGRAPHER_STYLIST_SCALE;
                break;
            }
            default: {
                Log.e("Venus", "不支持的缩放比例");
                return null;
            }
        }
        return dimension;
    }

    /**
     * 正方形尺寸
     *
     * @return
     */
    public static String getRectStringDimension() {
        return "@" + screenWidth + "w_" + screenWidth + "h_" + DEFAULT_QUALITY;
    }

    public static String getHorizontalListViewStringDimension80Q() {
        return "@" + getHorizonalListViewDimension().getWidth() + "w_" + getHorizonalListViewDimension().getHeight() + "h_80q";
    }

    public static String getVerticalListViewStringDimension80Q() {
        return "@" + getVerticalListViewDimension().getWidth() + "w_" + getVerticalListViewDimension().getHeight() + "h_80q";
    }

    /**
     * 3：2   width:3     height:2
     *
     * @param scale
     * @return
     */
    public static String getHorizontalListViewStringDimension(int scale) {
        Dimension dimension = getDimensionWidthHeight(emRatio.W3H2, scale);
        return "@" + dimension.getWidth() + "w_" + dimension.getHeight() + "h_" + DEFAULT_QUALITY;
    }

    /**
     * 2：3
     *
     * @return
     */
    public static String getVerticalListViewStringDimension(int scale) {
        Dimension dimension = getDimensionWidthHeight(emRatio.W2H3, scale);
        return "@" + dimension.getWidth() + "w_" + dimension.getHeight() + "h_" + DEFAULT_QUALITY;
    }

    public static Dimension getVerticalListViewDimension() {
        int width = screenWidth / 2;
        int height = 3 * width / 2;
        return new Dimension(width, height);
    }

    /**
     * 获取3比2的图片的高
     *
     * @return
     */
    public static int getPictureHeightByW3H2() {
        return 2 * screenWidth / 3;
    }

    /**
     * 获取2比3的图片的高
     *
     * @return
     */
    public static int getPictureHeightByW2H3() {
        return 3 * screenWidth / 2;
    }

    public static Dimension getPictureDimensionByUrl(String url) {
        if (TextUtils.isEmpty(url)) return null;
        url = url.substring(url.lastIndexOf("_") + 1, url.lastIndexOf("."));
        String[] dimens = url.split("x");
        return new Dimension(Integer.parseInt(dimens[0]), Integer.parseInt(dimens[1]));
    }

    public static boolean isHorizontal(String url) {
        Dimension dimension = getPictureDimensionByUrl(url);
        if (dimension.getWidth() < dimension.getHeight()) {
            return false;
        } else
            return true;
    }

    /**
     * 获取显示的尺寸
     *
     * @param picDimen
     * @return
     */
    public static Dimension getDimen(Dimension picDimen, int targetWidth) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (picDimen == null)
            return null;

        if (picDimen.getWidth() < targetWidth) {
            double delay = ((double) targetWidth) / picDimen.getWidth();
            BigDecimal b = new BigDecimal(delay);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            picDimen.setHeight((int) (targetWidth * f1));
            return new Dimension(picDimen.getHeight(), targetWidth);
        }
        return new Dimension(picDimen.getHeight() * targetWidth / picDimen.getWidth(), targetWidth);
    }

    //------------------------------------------------------------

    public static Dimension getHorizonalListViewDimension(){
        int height = getPictureHeightByW3H2();
        return new Dimension(screenWidth, height);
    }

    public static String getVertical() {
        return "@" + getVerticalListViewDimension().getWidth()  + "w_" + getVerticalListViewDimension().getHeight() + "h_" + DEFAULT_QUALITY;
    }

    public static Dimension getVerticalDimen() {
        return new Dimension(getVerticalListViewDimension().getWidth(), getVerticalListViewDimension().getHeight());
    }

    public static String getHorizontal() {
       return "@"+ getHorizonalListViewDimension().getWidth() + "w_" + getHorizonalListViewDimension().getHeight() + "h_" + DEFAULT_QUALITY;
    }

    public static String getVertical50Q() {
        return "@" + getVerticalListViewDimension().getWidth()  + "w_" + getVerticalListViewDimension().getHeight() + "h_40Q";
    }
    public static String getHorizontal50Q() {
        return "@"+ getHorizonalListViewDimension().getWidth() + "w_" + getHorizonalListViewDimension().getHeight() + "h_40Q";
    }
}
