package com.chinajsbn.venus.config;

/**
 * Created by 13510 on 2015/9/23.
 */
public class Conf {

    //套系图片尺寸
    private static final String SUITE_QUALITY = "60q";
    private static final String SUITE_WIDTH   = "1200w";
    private static final String SUITE_HEIGHT  = "800h";

    /**width:1200   height:800  quality:60q*/
    public static final String SUIT_DIMENSION= "@" + SUITE_WIDTH + "_" + SUITE_HEIGHT + "_" + SUITE_QUALITY;

    //样片， 客片图片尺寸
    private static final String SIMPLE_QUALITY = "60q";
    private static final String SIMPLE_WIDTH   = "1200";
    private static final String SIMPLE_HEIGHT  = "800";
    /**width:1200   height:800  quality:60q*/
    public static final String SIMPLE_CUSTOM   = "@" + SIMPLE_HEIGHT + "w_" + SIMPLE_WIDTH + "h_" + SIMPLE_QUALITY;

    private static final String DEFAULT_QUALITY = "60q";
    private static final String DEFAULT_WIDTH   = "1422";
    private static final String DEFAULT_HEIGHT  = "800";

    public static final String THREE_TWO = "@" + DEFAULT_WIDTH + "w_" + DEFAULT_HEIGHT + "h_" + DEFAULT_QUALITY;
    public static final String TWO_THREE = "@" + DEFAULT_HEIGHT + "w_" + DEFAULT_WIDTH + "h_" + DEFAULT_QUALITY;


    public static String simpleCustom = "AAAAAAAAAA";
}
