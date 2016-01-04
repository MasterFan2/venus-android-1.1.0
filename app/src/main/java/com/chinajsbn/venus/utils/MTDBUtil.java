package com.chinajsbn.venus.utils;

import android.content.Context;

import com.chinajsbn.venus.net.bean.Banquet;
import com.chinajsbn.venus.net.bean.Cache;
import com.chinajsbn.venus.net.bean.Hotel;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.Calendar;
import java.util.List;

/**
 * Created by 13510 on 2015/10/26.
 */
public class MTDBUtil {

    public static final String PHOTO_MAJORDOMO = "photo_majordomo";
    public static final String PHOTO_SENIOR    = "photo_senior";
    public static final String STYLIST_MAJORDOMO = "stylist_majordomo";
    public static final String STYLIST_SENIOR   = "stylist_senior";

    public static void saveHotel(Context context, List<Hotel> hotelList){
        DbUtils db = DbUtils.create(context);
        try {
            db.saveOrUpdateAll(hotelList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static List<Banquet> getBanquetsByHotelId(Context context, String hotelId){
        DbUtils db = DbUtils.create(context);
        try {
            return db.findAll(Selector.from(Banquet.class).where("hotelId", "=", hotelId));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 以一天缓存一次策略，判断当天是否缓存过
     * @param context
     * @param tag 页面名称
     * @return true:已经缓存过， 不需要再缓存       false:未缓存过，需要缓存操作
     */
    public static boolean todayChecked(Context context, String tag) {
        DbUtils db = DbUtils.create(context);
        try {
            Cache cache = db.findFirst(Selector.from(Cache.class).where("pageName", "=", tag));
            if(cache == null) return false;
            String todayStr = getToday();
            if(todayStr.equals(cache.getDate())){
                return true;
            }else{
                return false;
            }
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当天的日期
     * @return e.g : 2015-9-18
     */
    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        return  calendar.get(Calendar.YEAR)+ "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
    }
}
