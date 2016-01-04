package com.chinajsbn.venus.net;

import android.content.Context;
import android.text.TextUtils;

import com.chinajsbn.venus.net.bean.AddrStyle;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.BaseResp;
import com.chinajsbn.venus.net.bean.HomeMenu;
import com.chinajsbn.venus.net.bean.LoginReq;
import com.chinajsbn.venus.net.bean.SignupReq;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.SimpleDetailResp;
import com.chinajsbn.venus.net.bean.SimpleResp;
import com.chinajsbn.venus.net.bean.SmsReq;
import com.chinajsbn.venus.net.bean.WeddingSuit;
import com.squareup.okhttp.OkHttpClient;
import com.tool.PreferenceUtils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by chenjianjun on 15-4-13.
 * <p/>
 * Beyond their own, let the future
 */
public class HttpClientApi {

    // 服务器地址信息
    private static final String BASE_URL = "http://192.168.1.130/api";

    private Context mContext;
    private RestAdapter restAdapter = null;
    private NetInterface netInterface = null;

    private static HttpClientApi a;

    public HttpClientApi() {
    }

    public static HttpClientApi getInstance() {

        if (a == null) {
            a = new HttpClientApi();
        }

        return a;
    }

    /**
     * @param var1 上下文
     * @return false:初期化失败 true:初期化成功
     */
    public boolean initialize(Context var1) {

        mContext = var1;
        // 创建适配器
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(new OkHttpClient().setCookieHandler(new CustomCookieManager(mContext))))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {

                        String temp = PreferenceUtils.getValue(mContext, PreferenceUtils.PREFERENCE_SESSION_ID,
                                PreferenceUtils.DataType.STRING);
                        if (temp != null && !temp.equals("")) {
                            TextUtils.isEmpty("");
                            String a = "";
                            // 设置JSESSIONID
                            request.addHeader("Set-Cookie", "JSESSIONID=" + temp);
                        }

                        temp = PreferenceUtils.getValue(mContext,
                                PreferenceUtils.PREFERENCE_TOKEN,
                                PreferenceUtils.DataType.STRING);
                        if (temp != null && !temp.equals("")) {
                            // 设置TOKEN
                            request.addHeader("Set-Cookie",
                                    "TOKEN=" + temp);
                        }

                        temp = PreferenceUtils.getValue(mContext,
                                PreferenceUtils.PREFERENCE_TICKET,
                                PreferenceUtils.DataType.STRING);
                        if (temp != null && !temp.equals("")) {
                            // 设置TICKET
                            request.addHeader("Set-Cookie",
                                    "TICKET=" + temp);
                        }

                    }
                })
                .build();

        if (restAdapter == null) {
            return false;
        }

        netInterface = restAdapter.create(NetInterface.class);

        return netInterface != null;
    }

    //接口
    interface NetInterface {

        /** --------------------------------------------------------------------
         *
         * 最新版接口
         *
         ----------------------------------------------------------------------*/
        //a.首页菜单
        @GET("/global/mainModule")
        void getHomeMenu(Callback<Base<ArrayList<HomeMenu>>> cb);

        //b.每日客片
        @GET("/api/pringles/{moduleId}")
        void getEveryDayCustom(@Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<Simple>> cb);

        //c.样片

        //d.







        /**
         * ****************************************************************************
         *
         * 登录注册
         * ***************************************************************************
         */
        //1.登录
        @FormUrlEncoded
        @POST("/user/login")
        void login(@Field("USERNAME") String userName, @Field("PASSWORD") String passWord, @Field("PLATFORM") Integer type, Callback<BaseResp> cb);

        //2.获取验证码      TYPE 类型 0-注册 1-修改密码
        @FormUrlEncoded
        @POST("/user/getSmsCode")
        void getSmsCode(@Field("USERNAME") String uname, @Field("TYPE") int type, Callback<BaseResp> cb);

        //3.注册   PLATFORM 0-web 1-app 2-3D
        @FormUrlEncoded
        @POST("/user/appRegister")
        void signup(@Field("USERNAME") String uname, @Field("CODE") String code, @Field("PLATFORM") int platform, Callback<BaseResp> cb);

        /**
         * ****************************************************************************
         *
         * 婚纱摄影
         * ***************************************************************************
         */

        //1.样片列表
        @GET("/hssy/getContentByPage")
        void getSimpleList(@Query("contentId") String contentId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<SimpleResp> cb);

        //2.样片详情
        @GET("/hssy/getContentDetail")
        void getSimpleDetail(@Query("contentId") String contentId, Callback<SimpleDetailResp> cb);

        //3.每日客片列表
        @GET("/hssy/getEveryDayPringles")
        void getDayCustom(@Query("contentId") String contentId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<SimpleResp> cb);

        //4.客片详情
        @GET("/hssy/getEveryDayPringlesDetail")
        void getCustomDetail(@Query("contentId") String contentId, Callback<BaseResp> cb);

        //5.客片风格和地点列表
        @GET("/hssy/getPringlesTheme")
        void getCustomStyleAndLocale(Callback<Base<AddrStyle>> cb);

        //6.筛选客片
        @GET("/hssy/getPagePringles")
        void getCustomByCondition(@Field("contentId") String contentId, @Field("styleId") String styleId, @Field("addressId") String addressId, @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize, Callback<BaseResp> cb);

        //7.Web顶部,广告数据
        @GET("/hssy/getMainTopByPage")
        <T> void getMainTop(@Field("contentId") String contentId, @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize, Callback<T> cb);

        //8.获取造型师风格列表
        @GET("/hssy/getStyle")
        <T> void getStyleList(Callback<T> cb);

        //9.获取摄影师作品列表
        @GET("/hssy/getPhotographer")
        <T> void getPhotographerWorkList(@Query("styleId") int styleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<T> cb);

        //10.获取造型师作品列表
        @GET("/hssy/getStyleLists")
        <T> void getStylist(@Query("styleId") int styleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<T> cb);

        //11.获取婚纱套系
        @GET("/hssy/getWeDrSuList")
        void getWeddingSuitList(@Query("contentId") String contentId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<WeddingSuit>>> cb);

        //12.婚纱套系详情
        @GET("/hssy/getWeDrSuDetail/{weddingDressSuitId}")
        <T> void getWeddingSuitDetail(@Path("weddingDressSuitId") int weddingDressSuitId, Callback<T> cb);

        //13.获取摄影师详情
        @GET("/hssy/getPhotographerDetail/{personId}")
        <T> void getPhotographerDetail(@Path("personId") int personId, Callback<T> cb);

        //14.获取摄影师详情作品列表
        @GET("/hssy/getPhotographerProduction/{personId}/{pageIndex}/{pageSize}")
        <T> void getPhotographerDetailWorks(@Path("personId") int personId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<T> cb);

        //15.获取造型师详情
        @GET("/hssy/getStylistDetail/{personId}")
        <T> void getStylistDetail(@Path("personId") int personId, Callback<T> cb);

        //16.获取造型师详情作品列表
        @GET("/hssy/getStylistProduction/{personId}/{pageIndex}/{pageSize}")
        <T> void getStylistDetailWorks(@Path("personId") int personId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<T> cb);


        /**
         * **************************************************************************
         *
         * 婚礼策划
         * *************************************************************************
         */
        //1.推荐案例
        @GET("/api/hlch/getRecommondWeddingCase/{contentId}/{pageIndex}/{pageSize}")
        <Type> void getRecommendWeddingCase(@Path("contentId") int contentId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<Type> cb);

        //2.筛选案例
        @GET("/api/hlch/getRecommondWeddingCase/{contentId}/{pageIndex}/{pageSize}/{weddingCaseStyleId}/{filterTypeId}/{filterHotelId}/{minPrice}/{maxPrice}")
        <T> void getRecommendWeddingCaseByCondition(@Path("contentId") String contentId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize,
                                                    @Path("weddingCaseStyleId")int weddingCaseStyleId, @Path("filterTypeId")int typeId, @Path("filterHotelId")int hotelId,
                                                    @Path("minPrice")double minPrice, @Path("maxPrice")double maxPrice,  Callback<T> cb);

        //3.策划师和作品列表
        @GET("/api/hlch/getPlanner/{pageIndex}/{pageSize}")
        <T> void getPlannerAndWroksList(@Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<T> cb);

        //4.选择四大金刚
        @GET("/api/hlch/getPersonWeddingTeam/{pageIndex}/{pageSize}/{personType}/{personPrice}")
        <T> void getPersonWeddingTeam(@Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, @Path("personType")int type, @Path("personPrice")double price, Callback<T> cb);

        //5.获取策划师详情
        @GET("/api/hlch/getPlannerDetail/{personId}")
        <T> void getPlannerDetail(@Path("personId") int personId, Callback<T> cb);

        //6.获取策划师详情作品列表
        @GET("/api/hlch/getPlannerDetailProduct/{personId}")
        <T> void getPlannerDetailProduct(@Path("personId") int personId, Callback<T> cb);

        //7.获取主持人详细
        @GET("/api/hlch/getHosterDetail/{personId}")
        <T> void getHosterDetail(@Path("personId") int personId, Callback<T> cb);

        //8.获取主持人详情作品列表
        @GET("/api/hlch/getHosterDetailProduct/{personId}")
        <T> void getHosterDetailProduct(@Path("personId") int personId, Callback<T> cb);

        //9.婚礼策划--获取摄影师详情作品列表
        @GET("/api/hlch/getPhotographerDetailProduct/{personId}")
        <T> void getPhotographerDetailProduct(@Path("personId") int personId, Callback<T> cb);

        //10.婚礼策划--获取造型师详情
        @GET("/api/hlch/getDresserDetail/{personId}")
        <T> void getDresserDetail(@Path("personId") int personId, Callback<T> cb);

        //11.婚礼策划--获取造型师详情作品列表
        @GET("/api/hlch/getDresserDetailProduct/{personId}")
        <T> void getDresserDetailProduct(@Path("personId") int personId, Callback<T> cb);

        //12.婚礼策划--获取摄像师详情
        @GET("/api/hlch/getCameramanDetail/{personId}")
        <T> void getCameramanDetail(@Path("personId") int personId, Callback<T> cb);

        //13.婚礼策划--获取摄像师详情作品列表
        @GET("/api/hlch/getCameramanDetailProduct/{personId}")
        <T> void getCameramanDetailProduct(@Path("personId") int personId, Callback<T> cb);

        //14.婚礼策划--获取案例风格列表
        @GET("/api/hlch/getWeddingStyle")
        <T> void getWeddingStyle(Callback<T> cb);

        //15.获取酒店列表
        @GET("/api/hlch/getAllHotel")
        <T> void getAllHotel(Callback<T> cb);

        //16.获取婚礼策划首页广告
        @GET("/api/hlch/getPlanerMainAdvert")
        <T> void getPlanerMainAdvert(@Path("contentId") String contentId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<T> cb);

        //16.了解策划师广告
        @GET("/api/hlch/getPlanerAdvert")
        <T> void getPlanerAdvert(@Path("contentId") String contentId, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize, Callback<T> cb);

        /**
         * **************************************************************************
         *
         * 婚宴预订
         * *************************************************************************
         */

        //1.广告
        @GET("/hyyd/getHotelAdvert")
        void getHotelAdvert(@Field("contentId") String contentId, Callback<BaseResp> cb);

        //2.推荐酒店列表
        @FormUrlEncoded
        @GET("/hyyd/getRecommendHotelList")
        void getRecommendHotelList(@Field("contentId") String
                                           contentId, Callback<BaseResp> cb);

        //3.酒店列表
        @FormUrlEncoded
        @GET("/hyyd/getHotelList")
        void getHotelList(@Field("contentId") String contentId,
                          @Field("pageIndex") int pageIndex, @Field("pageSize") int pageSize,
                          @Field("cityId") int cityId, Callback<BaseResp> cb);

        //4.酒店详细
        @FormUrlEncoded
        @GET("hyyd/getHotelDetail")
        void getHotelDetail(@Field("hotelId") int hotelId, Callback<BaseResp> cb);

        //5.婚宴厅详情
        @FormUrlEncoded
        @GET("/hyyd/getbanquetHallDetail")
        void getbanquetHallDetail(@Field("banquetHallId") int banquetHallId, Callback<
                BaseResp> cb);

        //6.通过酒店推荐的酒店
        @FormUrlEncoded
        @GET("/hyyd/getRecommondHotelByCondition")
        void getRecommondHotelByCondition(@Field("areaId") int areaId,
                                          @Field("price") double price, @Field("pageIndex") int pageIndex,
                                          @Field("pageSize") int pageSize, Callback<BaseResp> cb);

        //7.浏览过的酒店
        @FormUrlEncoded
        @POST("/hyyd/getHistoryHotel")
        void getHistoryHotel(@Field("hotelCount") int hotelCount, Callback<BaseResp> cb);


    }

    ///////////////////////////////////////////////////////////////////
    //              登录,注册,找回密码
    //
    //////////////////////////////////////////////////////////////////

    /**
     * 每日客片
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getEveryDayCustom(String id, int pageIndex, int pageSize, Callback<Base<Simple>> cb){
        netInterface.getEveryDayCustom(id, pageIndex, pageSize, cb);
    }

    /**
     * 登录接口
     *
     * @param loginReq 登录请求字段
     * @param callback 登录结果回调对象
     */
    public void Login(LoginReq loginReq, Callback<BaseResp> callback) {
        netInterface.login(loginReq.getUSERNAME(), loginReq.getPASSWORD(), loginReq.getPLATFORM(), callback);
    }

    /**
     * 获取短信验证码
     *
     * @param smsReq
     * @param cb
     */
    public void getSmsCode(SmsReq smsReq, Callback<BaseResp> cb) {
        netInterface.getSmsCode(smsReq.getUSERNAME(), smsReq.getTYPE(), cb);
    }

    /**
     * 注册
     *
     * @param signupReq
     * @param cb
     */
    public void signup(SignupReq signupReq, Callback<BaseResp> cb) {
        netInterface.signup(signupReq.getUSERNAME(), signupReq.getCODE(), signupReq.getPLATFORM(), cb);
    }

    public void getHomeMenu(Callback<Base<ArrayList<HomeMenu>>> cb) {
        netInterface.getHomeMenu(cb);
    }


    ///////////////////////////////////////////////////////////////////
    //======================婚礼策划========================
    //
    //////////////////////////////////////////////////////////////////

    /**
     * 1.获取案例推荐列表
     *
     * @param contentId content id
     * @param cb        callback
     */
    public <T> void getRecommendCaseList(int contentId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getRecommendWeddingCase(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 2.筛选案例
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @param styleId
     * @param hotelId
     * @param weddingCaseStyleId
     * @param minPrice
     * @param maxPrice
     */
    public <T> void getRecommendWeddingCaseByCondition(String contentId, int pageIndex, int pageSize, int styleId, int hotelId, int weddingCaseStyleId , double minPrice, double maxPrice, Callback<T> cb) {
        netInterface.getRecommendWeddingCaseByCondition(contentId, pageIndex, pageSize, weddingCaseStyleId, styleId, hotelId, minPrice, maxPrice, cb);
    }

    /**
     * 3.策划师和作品列表
     *
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getPlannerAndWroksList(int pageIndex,int pageSize, Callback<T> cb){
        netInterface.getPlannerAndWroksList(pageIndex, pageSize, cb);
    }

    /**
     * 4.选择四大金刚
     *
     * @param pageIndex
     * @param pageSize
     * @param type
     * @param price
     */
    public <T> void getPersonWeddingTeam(int pageIndex,int pageSize,int type,double price, Callback<T> cb){
        netInterface.getPersonWeddingTeam(pageIndex, pageSize, type, price, cb);
    }

    /**
     * 5.获取策划师详情
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getPlannerDetail(int personId, Callback<T> cb){
        netInterface.getPlannerDetail(personId, cb);
    }

    /**
     * 6.获取策划师详情作品列表
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getPlannerDetailProduct(int personId, Callback<T> cb){
        netInterface.getPlannerDetailProduct(personId, cb);
    }

    /**
     * 7.获取主持人详细
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getHosterDetail(int personId, Callback<T> cb){
        netInterface.getHosterDetail(personId, cb);
    }

    /**
     *  8.获取主持人详情作品列表
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getHosterDetailProduct(int personId, Callback<T> cb){
        netInterface.getHosterDetailProduct(personId, cb);
    }

    /**
     * 9.婚礼策划--获取摄影师详情作品列表
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getPhotographerDetailProduct(int personId, Callback<T> cb){
        netInterface.getPhotographerDetailProduct(personId, cb);
    }

    /**
     * 10.婚礼策划--获取造型师详情
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getDresserDetail(int personId, Callback<T> cb){
        netInterface.getDresserDetail(personId, cb);
    }

    /**
     * 11.婚礼策划--获取造型师详情作品列表
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getDresserDetailProduct(int personId, Callback<T> cb){
        netInterface.getDresserDetailProduct(personId, cb);
    }

    /**
     * 12.婚礼策划--获取摄像师详情
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getCameramanDetail(int personId, Callback<T> cb){
        netInterface.getCameramanDetail(personId, cb);
    }

    /**
     * 13.婚礼策划--获取摄像师详情作品列表
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getCameramanDetailProduct(int personId, Callback<T> cb){
        netInterface.getCameramanDetailProduct(personId, cb);
    }

    /**
     * 14.婚礼策划--获取案例风格列表
     *
     * @param cb
     * @param <T>
     */
    public <T> void getWeddingStyle(Callback<T> cb){
        netInterface.getWeddingStyle(cb);
    }

    /**
     * 15.获取酒店列表
     *
     * @param cb
     * @param <T>
     */
    public <T> void getAllHotel(Callback<T> cb){
        netInterface.getAllHotel(cb);
    }

    /**
     * 16.获取婚礼策划首页广告
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     */
    public <T> void getPlanerMainAdvert( String contentId, int pageIndex, int pageSize, Callback<T> cb){
        netInterface.getPlanerMainAdvert(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 17.了解策划师广告
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getPlanerAdvert(String contentId, int pageIndex, int pageSize, Callback<T> cb){
        netInterface.getPlanerAdvert(contentId, pageIndex, pageSize, cb);
    }


    ///////////////////////////////////////////////////////////////////
    //----------------------婚纱摄影--------------------------
    //
    //////////////////////////////////////////////////////////////////

    /**
     * 1.样片列表
     *
     * @param contentId content id
     * @param cb        callback
     */
    public void getSimpleList(String contentId, int pageIndex, int pageSize, Callback<SimpleResp> cb) {
        netInterface.getSimpleList(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 2.样片详情
     *
     * @param contentId content ID
     * @param cb        callback
     */
    public void getSimpleDetail(String contentId, Callback<SimpleDetailResp> cb) {
        netInterface.getSimpleDetail(contentId, cb);
    }

    /**
     * 3.每日客片列表
     *
     * @param contentId content id
     * @param pageIndex page index
     * @param pageSize  page size
     * @param cb        callback
     */
    public void getDayCustom(String contentId, int pageIndex, int pageSize, Callback<SimpleResp> cb) {
        netInterface.getDayCustom(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 4.客片详情
     *
     * @param contentId content id
     * @param cb        callback
     */
    public void getCustomDetail(String contentId, Callback<BaseResp> cb) {
        netInterface.getCustomDetail(contentId, cb);
    }

    /**
     * 5.客片风格和地点列表
     *
     * @param cb callback
     */
    public void getCustomStyleAndLocale(Callback<Base<AddrStyle>> cb) {
        netInterface.getCustomStyleAndLocale(cb);
    }

    /**
     * 6.筛选客片
     *
     * @param contentId content id
     * @param styleId   style id
     * @param addressId address id
     * @param pageIndex page index
     * @param pageSize  page siae
     * @param cb        callback
     */
    public void getCustomByCondition(String contentId, String styleId, String addressId, int pageIndex, int pageSize, Callback<BaseResp> cb) {
        netInterface.getCustomByCondition(contentId, styleId, addressId, pageIndex, pageSize, cb);
    }

    /**
     * 7.Web顶部,广告数据
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public <T> void getMainTop(String contentId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getMainTop(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 8.获取造型师风格列表
     *
     * @param cb
     * @param <T>
     */
    public <T> void getStyleList(Callback<T> cb) {
        netInterface.getStyleList(cb);
    }

    /**
     * 9.获取摄影师作品列表
     *
     * @param styleId
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getPhotogrepherList(int styleId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getPhotographerWorkList(styleId, pageIndex, pageSize, cb);
    }

    /**
     * 10.获取造型师作品列表
     *
     * @param styleId
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getStylist(int styleId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getStylist(styleId, pageIndex, pageSize, cb);
    }

    /**
     * 11.获取婚纱套系列表
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getWeddingList(String contentId, int pageIndex, int pageSize, Callback<Base<ArrayList<WeddingSuit>>> cb) {
        netInterface.getWeddingSuitList(contentId, pageIndex, pageSize, cb);
    }

    /**
     * 12.获取婚纱套系详情
     *
     * @param weddingDressSuitId
     * @param cb
     * @param <T>
     */
    public <T> void getWeddingSuitDetail(int weddingDressSuitId, Callback<T> cb) {
        netInterface.getWeddingSuitDetail(weddingDressSuitId, cb);
    }

    /**
     * 13.获取摄影师详情
     *
     * @param persionId
     * @param cb
     * @param <T>
     */
    public <T> void getPhotographerDetail(int persionId, Callback<T> cb) {
        netInterface.getPhotographerDetail(persionId, cb);
    }

    /**
     * 14.获取摄影师详情作品列表
     *
     * @param personId
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getPhotographerDetailWorks(int personId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getPhotographerDetailWorks(personId, pageIndex, pageSize, cb);
    }

    /**
     * 15.获取造型师详情
     *
     * @param personId
     * @param cb
     * @param <T>
     */
    public <T> void getStylistDetail(int personId, Callback<T> cb) {
        netInterface.getStylistDetail(personId, cb);
    }

    /**
     * 16.获取造型师详情作品列表
     *
     * @param personId
     * @param pageIndex
     * @param pageSize
     * @param cb
     * @param <T>
     */
    public <T> void getStylistDetailWorks(int personId, int pageIndex, int pageSize, Callback<T> cb) {
        netInterface.getStylistDetailWorks(personId, pageIndex, pageSize, cb);
    }


    ///////////////////////////////////////////////////////////////////
    //              婚宴预订
    //
    //////////////////////////////////////////////////////////////////

    /**
     * 1.广告
     *
     * @param contentId content id
     * @param cb        callback
     */
    public void getHotelAdvert(String contentId, Callback<BaseResp> cb) {
        netInterface.getHotelAdvert(contentId, cb);
    }

    /**
     * 2.推荐酒店列表
     *
     * @param contentId content id
     * @param cb        callback
     */
    public void getRecommendHotelList(String contentId, Callback<BaseResp> cb) {
        netInterface.getRecommendHotelList(contentId, cb);
    }

    /**
     * 3.酒店列表
     *
     * @param contentId
     * @param pageIndex
     * @param pageSize
     * @param cityId
     * @param cb
     */
    public void getHotelList(String contentId, int pageIndex, int pageSize, int cityId, Callback<BaseResp> cb) {
        netInterface.getHotelList(contentId, pageIndex, pageSize, cityId, cb);
    }

    /**
     * 4.酒店详细
     *
     * @param hotelId
     * @param cb
     */
    public void getHotelDetail(int hotelId, Callback<BaseResp> cb) {
        netInterface.getHotelDetail(hotelId, cb);
    }

    /**
     * 5.婚宴厅详情
     *
     * @param banquetHallId
     * @param cb
     */
    public void getbanquetHallDetail(int banquetHallId, Callback<BaseResp> cb) {
        netInterface.getbanquetHallDetail(banquetHallId, cb);
    }

    /**
     * 6.通过酒店推荐的酒店
     *
     * @param areaId
     * @param price
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getRecommondHotelByCondition(int areaId, double price, int pageIndex, int pageSize, Callback<BaseResp> cb) {
        netInterface.getRecommondHotelByCondition(areaId, price, pageIndex, pageSize, cb);
    }

    /**
     * 7.浏览过的酒店
     *
     * @param hotelCount
     * @param cb
     */
    public void getHistoryHotel(int hotelCount, Callback<BaseResp> cb) {
        netInterface.getHistoryHotel(hotelCount, cb);
    }
}
