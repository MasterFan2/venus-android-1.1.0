package com.chinajsbn.venus.net;

import android.content.Context;
import android.text.TextUtils;

import com.chinajsbn.venus.net.bean.AddrStyleResp;
import com.chinajsbn.venus.net.bean.Advert;
import com.chinajsbn.venus.net.bean.BanquetDetail;
import com.chinajsbn.venus.net.bean.Base;
import com.chinajsbn.venus.net.bean.BaseResp;
import com.chinajsbn.venus.net.bean.BrandsResp;
import com.chinajsbn.venus.net.bean.Car;
import com.chinajsbn.venus.net.bean.CarDetail;
import com.chinajsbn.venus.net.bean.CarType;
import com.chinajsbn.venus.net.bean.Custom;
import com.chinajsbn.venus.net.bean.Dress;
import com.chinajsbn.venus.net.bean.F4;
import com.chinajsbn.venus.net.bean.Film;
import com.chinajsbn.venus.net.bean.FilterBean;
import com.chinajsbn.venus.net.bean.HomeMenu;
import com.chinajsbn.venus.net.bean.Hotel;
import com.chinajsbn.venus.net.bean.HotelDetail;
import com.chinajsbn.venus.net.bean.LoginResp;
import com.chinajsbn.venus.net.bean.MasterFanSeason;
import com.chinajsbn.venus.net.bean.OtherDetail;
import com.chinajsbn.venus.net.bean.OtherResp;
import com.chinajsbn.venus.net.bean.Photographer;
import com.chinajsbn.venus.net.bean.PlanDetail;
import com.chinajsbn.venus.net.bean.Planner;
import com.chinajsbn.venus.net.bean.Scheme;
import com.chinajsbn.venus.net.bean.SchemeDetail;
import com.chinajsbn.venus.net.bean.SearchHotelParam;
import com.chinajsbn.venus.net.bean.Simple;
import com.chinajsbn.venus.net.bean.Style;
import com.chinajsbn.venus.net.bean.Supplie;
import com.chinajsbn.venus.net.bean.SupplieDetail;
import com.chinajsbn.venus.net.bean.Team;
import com.chinajsbn.venus.net.bean.TeamWorks;
import com.chinajsbn.venus.net.bean.VersionResp;
import com.chinajsbn.venus.net.bean.WeddingDress;
import com.chinajsbn.venus.net.bean.WeddingDressStyle;
import com.chinajsbn.venus.net.bean.WeddingSuit;
import com.chinajsbn.venus.net.bean.WeddingSuitDetail;
import com.chinajsbn.venus.net.bean.WorksDetail;
import com.squareup.okhttp.OkHttpClient;
import com.tool.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import retrofit.http.QueryMap;

/**
 * Created by MasterFan on 2015/7/3.
 * description:
 */
public class HttpClient {

    // 服务器地址信息
//private static final String BASE_URL = "http://bfjs.f3322.net/api";//正式服务器
//private static final String BASE_URL = "http://bfjs.f3322.net/api";//正式服务器
//private static final String BASE_URL = "http://cd.jsbn.com/api";//正式服务器
//private static final String BASE_URL = "http://www.jsbn.love/api";//开发服务器
    private static final String BASE_URL = "http://app.jsbn.com/api";//开发服务器
//private static final String BASE_URL = "http://up.jsbn.love/api";//开发服务器

    private Context mContext;
    private RestAdapter restAdapter = null;
    private NetInterface netInterface = null;

    private static HttpClient a;

    public HttpClient() {
    }

    public static HttpClient getInstance() {

        if (a == null) {
            a = new HttpClient();
        }

        return a;
    }

    /**
     * @param var1 上下文
     * @return false:初期化失败 true:初期化成功
     */
    public boolean initialize(Context var1) {

        mContext = var1;


        OkHttpClient client = new OkHttpClient().setCookieHandler(new CustomCookieManager(mContext));
        client.setConnectTimeout(30, TimeUnit.SECONDS);//连接超时
        client.setReadTimeout(30, TimeUnit.SECONDS);   //读取超时

        // 创建适配器
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(client))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {

                        String temp = PreferenceUtils.getValue(mContext, PreferenceUtils.PREFERENCE_SESSION_ID,
                                PreferenceUtils.DataType.STRING);
                        if (temp != null && !temp.equals("")) {
                            TextUtils.isEmpty("");
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

        //a.首页菜单
        @GET("/global/mainModule")
        void getHomeMenu(@Query("applicablePlatform") String applicablePlatform, Callback<Base<ArrayList<HomeMenu>>> cb);


        @GET("/adv/{id}")
        void getHomeAdvert(@Path("id") String id, Callback<Base<ArrayList<Advert>>> cb);

        //------------------------
        //b1.客片列表
        @GET("/pringles/{moduleId}")
        void getCustomList(@Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<List<Custom>>> cb);

        //b11.客片筛选
        @GET("/pringles/{moduleId}")
        void getCustomListByStyleId(@Query("styleId") String styleId, @Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Custom>>> cb);

        //b2.每日客片详情
        @GET("/pringles/{moduleId}/{detailId}")
        void getDayCustomDetail(@Path("moduleId") String id, @Path("detailId") String detailId, Callback<Base<ArrayList<Simple>>> cb);

        //分季列表
        @GET("/condition/season")
        void seasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb);

        //分季查询列表
        @GET("/pringles/{moduleId}")
        void customListBySeasonId(@Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("seasonId") String seasonId, Callback<Base<List<Custom>>> cb);


        //c1.样片
        @GET("/samples/{moduleId}")
        void getSimples(@Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Simple>>> cb);

        //c1.样片筛选---风格
        @GET("/samples/{moduleId}")
        void getSimplesByStyleId(@Query("styleId") String styleId, @Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Simple>>> cb);

        //c1.样片筛选---场景
        @GET("/samples/{moduleId}")
        void getSimplesByAddressId(@Query("addressId") String addressId, @Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Simple>>> cb);


        //c2.样片详情
        @GET("/samples/{moduleId}/{detailId}")
        void getSimplesDetail(@Path("moduleId") String id, @Path("detailId") String detailId, Callback<Base<ArrayList<Simple>>> cb);


        //d1.获取婚纱套系列表
        @GET("/suite/{suitId}")
        void getWeddingSuitList(@Path("suitId") String suitId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<WeddingSuit>>> cb);

        //d2.获取婚纱套系详情
        @GET("/suite/{suitId}/{detailId}")
        void getWeddingSuitDetail(@Path("suitId") String suitId, @Path("detailId") String detailId, Callback<Base<ArrayList<WeddingSuitDetail>>> cb);


        //e1.摄影师列表
        @GET("/photographers")
        void getPhotographer(@Query("levelId") int levelId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Photographer>>> cb);

        //e2.摄影师详情
        @GET("/photographers/{id}")
        void getPhotographerDetail(@Path("id") String id, Callback<Base<ArrayList<Photographer>>> cb);

        //e3.摄影师作品列表
        @GET("/photographers/{id}/works/{workId}")
        void getPhotographerWorks(@Path("id") String id, @Path("workId") String workId, Callback<Base<WorksDetail>> cb);

        //f1.造型师列表
        @GET("/stylist")
        void getStylist(@Query("levelId") int levelId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Photographer>>> cb);

        //f2.造型师详情
        @GET("/stylist/{id}")
        void getStylistDetail(@Path("id") String id, Callback<Base<ArrayList<Photographer>>> cb);

        //g1.婚纱类型
        @GET("/weddingdress/styles")
        void getWeddingDressStyle(Callback<Base<ArrayList<WeddingDressStyle>>> cb);

        //g2.婚纱列表
        @GET("/weddingdress/{id}")
        void getWeddingDressList(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<WeddingDress>>> cb);

        //g3.婚纱详情
        @GET("/weddingdress/{id}/{detailId}")
        void getWeddingDressDetail(@Path("id") String id, @Path("detailId") String detailId, Callback<Base<ArrayList<WeddingDress>>> cb);

        //团队
        @GET("/team")
        void teamList(@Query("teamLevel") int teamLevel, Callback<Base<ArrayList<Team>>> cb);

        //团队作品详情
        @GET("/team/{teamId}/works/{workId}")
        void teamWorkDetail(@Path("teamId") int teamId, @Path("workId") String workId, Callback<Base<TeamWorks>> cb);


        //1111111111111111111111111111111111111111111111111111111111111111
        //登录、注册
        //1111111111111111111111111111111111111111111111111111111111111111
        //a1.发送验证码 type: 0注册     1修改密码
        @FormUrlEncoded
        @POST("/users/getSmsCode")
        void getSmsCode(@Field("USERNAME") String uname, @Field("TYPE") int type, Callback<BaseResp> cb);

        //a2.登录 type:0-web      1-app       2-3D
        @FormUrlEncoded
        @POST("/users/login")
        void login(@Field("USERNAME") String uname, @Field("PASSWORD") String pwd, @Field("PLATFORM") int type, Callback<LoginResp> cb);

        //a3.注册
        @FormUrlEncoded
        @POST("/appUsers/new")
        void signup(@Field("USERNAME") String uname, @Field("CODE") String pwd, @Field("PLATFORM") int type, @Field("CITYID") int cityId, Callback<BaseResp> cb);

        //a4.找回密码
        @FormUrlEncoded
        @POST("/users/retrieve")
        void forget(@Field("USERNAME") String phone, @Field("CODE") String code, Callback<BaseResp> cb);

        //---------------------------------------------------------------
        //版本更新
        //---------------------------------------------------------------
        @GET("/control/getAppInfo")
        void checkUpgrade(@Query("appType") int type, @Query("appVersion") String version, @Query("applicableUsers") int appUser, Callback<VersionResp> cb);


        //888888888888888888888888888888888888888888888888888888888888888
        //酒店
        //888888888888888888888888888888888888888888888888888888888888888

        //j 获取区域列表
        @GET("/global/hotel/provinces/4/districts")
        void getHotelAreas(Callback<Base<List<FilterBean>>> cb);

        //j1.获取列表
        @GET("/hotel/{id}")
        void getHotels(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Hotel>>> cb);

        //j2.获取酒店详情
        @GET("/hotel/{modelId}/{detailId}")
        void getHotelDetail(@Path("modelId") String modelId, @Path("detailId") String detailId, Callback<Base<ArrayList<HotelDetail>>> cb);

        //j3.筛选--桌数  升序
        @GET("/hotel/{id}")
        void filtrateHotelsByTableOrPrice(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("short") String sort, @Query("order") String order, Callback<Base<ArrayList<Hotel>>> cb);

        //j4.筛选--价格
        @GET("/hotel/{id}")
        void filtrateHotelsByPrice(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("maxPrice") int maxPrice, Callback<Base<ArrayList<Hotel>>> cb);

        //j5.筛选--礼包
        @GET("/hotel/{id}")
        void filtrateHotelsByGift(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isGift") int isGift, Callback<Base<ArrayList<Hotel>>> cb);

        //j6.筛选--优惠
        @GET("/hotel/{id}")
        void filtrateHotelsByDiscount(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") int isDiscount, Callback<Base<ArrayList<Hotel>>> cb);

        //正式酒店搜索SearchHotelParam
        @GET("/hotel/{id}")
        void searchHotels(@Path("id") String id, @QueryMap Map<String, Object> params,  Callback<Base<ArrayList<Hotel>>> cb);

        ///////////////////////////////////////////////////////////////////////
        //j7.筛选--优惠/礼包
        @GET("/hotel/{id}")
        void gFiltrateHotelsByDiscountGift(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") String isDiscount, @Query("isGift") String isGift,
                                           @Query("short") String short_, @Query("order") String order, Callback<Base<ArrayList<Hotel>>> cb);

        //j8.筛选--桌数量
        @GET("/hotel/{id}")
        void gFiltrateHotelsByTableCount(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") String isDiscount, @Query("isGift") String isGift,
                                         @Query("minTable") int minTable, @Query("maxTable") int maxTable, @Query("short") String short_, @Query("order") String order, Callback<Base<ArrayList<Hotel>>> cb);

        //j9.筛选--价格多少
        @GET("/hotel/{id}")
        void gFiltrateHotelsByPrice(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") String isDiscount, @Query("isGift") String isGift,
                                    @Query("minPrice") int minPrice, @Query("maxPrice") int maxPrice, @Query("short") String short_, @Query("order") String order, Callback<Base<ArrayList<Hotel>>> cb);

        //j10.筛选--所有条件
        @GET("/hotel/{id}")
        void gFiltrateHotelsByAll(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") String isDiscount, @Query("isGift") String isGift,
                                  @Query("minTable") int minTable, @Query("maxTable") int maxTable, @Query("minPrice") int minPrice, @Query("maxPrice") int maxPrice, Callback<Base<ArrayList<Hotel>>> cb);


        //j10.筛选--所有条件 order
        @GET("/hotel/{id}")
        void gFiltrateHotelsByAllAndOrderDesc(@Path("id") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("isDisaccount") String isDiscount, @Query("isGift") String isGift,
                                              @Query("minTable") int minTable, @Query("maxTable") int maxTable, @Query("minPrice") int minPrice, @Query("maxPrice") int maxPrice, @Query("short") String short_, @Query("order") String order, Callback<Base<ArrayList<Hotel>>> cb);

        ///////////////////////////////////////////////////////////////////////
        //j7.获取宴会厅详情
        @GET("/hotel/{modelId}/{hotelId}/{banquetId}")
        void getBanquetDetail(@Path("modelId") String modelId, @Path("hotelId") String hotelId, @Path("banquetId") String banquetId, Callback<Base<ArrayList<BanquetDetail>>> cb);


        //999999999999999999999999999999999999999999999999999999999
        // 婚庆案例
        //999999999999999999999999999999999999999999999999999999999
        //c1.案例
        @GET("/scheme/{moduleId}")
        void getPlanSimples(@Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Scheme>>> cb);

        //c1.案例
        @GET("/scheme/{moduleId}")
        void getPlanSimplesByStyleId(@Query("styleId") String styleId, @Path("moduleId") String id, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Scheme>>> cb);

        //c2.案例详情
        @GET("/scheme/{parentId}/{detailId}")
        void getPlanDetail(@Path("parentId") String parentId, @Path("detailId") String detailId, Callback<Base<ArrayList<SchemeDetail>>> cb);

        @GET("/scheme/{parentId}/{detailId}")
        void getPlanNewDetail(@Path("parentId") String parentId, @Path("detailId") String detailId, Callback<Base<ArrayList<PlanDetail>>> cb);

        //c3.分季列表
        @GET("/scheme/season")
        void planSeasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb);

        //c4.最佳跟拍
        @GET("/scheme/{moduleId}")
        void bestFilmList(@Path("moduleId") String moduleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Scheme>>> cb);

        //c5.通过分季查询跟拍案例
        @GET("/scheme/{moduleId}")
        void planListBySeasonId(@Path("moduleId") String moduleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("seasonId") String seasonId, Callback<Base<ArrayList<Scheme>>> cb);


        //###################################################################
        //风格
        //###################################################################
        //.样片/客片 风格列表
        @GET("/condition/styleAddress")
        void getStyleList(Callback<AddrStyleResp> cb);

        //.案例风格列表
        @GET("/scheme/styles")
        void getSchemeList(Callback<Base<ArrayList<Style>>> cb);


        //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        //四大金刚
        //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        //.F4-emcee 主持人
        @GET("/f4/host")
        void getEmceeList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb);

        //.F4-emcee 化妆师
        @GET("/f4/dresser")
        void getDresserList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb);

        //.F4-emcee 摄影师
        @GET("/f4/photographer")
        void getPhotographerList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb);

        //.F4-emcee 摄像师
        @GET("/f4/camera")
        void getCameramanList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb);


        //////////////////////////////////////////////////
        // 策划师
        //////////////////////////////////////////////////
        //.选策划师
        @GET("/planner")
        void getPlannerList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Planner>>> cb);


        //*************************************************
        // 微电影
        //*************************************************
        //.微电影
        @GET("/videos")
        void filmList(@Query("sort") String sort, @Query("videoType") String videoType, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Film>>> cb);

        //.分季列表数据
        @GET("/videos/season")
        void filmSeasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb);

        @GET("/videos")
        void filmListBySeasonId(@Query("sort") String sort, @Query("videoType") String videoType, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, @Query("seasonId") String seasonId, Callback<Base<ArrayList<Film>>> cb);


        //==================================================
        // 婚纱礼服
        //==================================================
        //---品牌列表----
        @GET("/dress/brands")
        void brandsList(@Query("weddingDressType") int weddingDressType, Callback<BrandsResp> cb);

        //---品牌下婚纱列表---
        @GET("/dress/brand/{brandId}")
        void dressListByBrandId(@Path("brandId") int brandId, Callback<Base<ArrayList<Dress>>> cb);


        //################################################
        // 租车
        //################################################
        //车列表
        @GET("/car/{moduleId}")
        void carList(@Path("moduleId") String moduleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Car>>> cb);

        //车详情
        @GET("/car/{moduleId}/{detailId}")
        void carDetail(@Path("moduleId") String moduleId, @Path("detailId") String detailId, Callback<Base<CarDetail>> cb);

        //品牌
        @GET("/car/brands")
        void carBrandList(Callback<Base<ArrayList<CarType>>> cb);

        //类型
        @GET("/car/models")
        void carTypeList(Callback<Base<ArrayList<CarType>>> cb);

        //车搜索
        @GET("/car/{moduleId}")
        void carSearch(@Path("moduleId") String moduleId, @Query("carModelsId") String carModelsId, @Query("carBrandId") String carBrandId, @Query("priceStart") String priceStart, @Query("priceEnd") String priceEnd, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Car>>> cb);

        //车所有条件搜索
        @GET("/car/{moduleId}")
        void carSearchParamAll(@Path("moduleId") String moduleId, @Query("carNature") int carNature, @Query("carModelsId") String carModelsId, @Query("carBrandId") String carBrandId, @Query("priceStart") String priceStart, @Query("priceEnd") String priceEnd, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Car>>> cb);

        //车队/单车 搜索
        @GET("/car/{moduleId}")
        void carSearch(@Path("moduleId") String moduleId, @Query("carNature") int carNature, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Car>>> cb);


        //￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥
        // 用品
        //￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥
        //用品列表
        @GET("/supplies/{moduleId}")
        void supplieList(@Path("moduleId") String moduleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Supplie>>> cb);

        //用品详情
        @GET("/supplies/{moduleId}/{detailId}")
        void supplieDetail(@Path("moduleId") String moduleId, @Path("detailId") String detailId, Callback<Base<SupplieDetail>> cb);

        //用品类型
        @GET("/supplies/types")
        void supplieTypes(Callback<Base<ArrayList<CarType>>> cb);

        //用品查询
        @GET("/supplies/{moduleId}")
        void supplieSearch(@Path("moduleId") String moduleId, @Query("weddingSuppliesTypeId") int weddingSuppliesTypeId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Supplie>>> cb);


        //+++++++++++++++++++++++++++++++++++++++++++++++++
        // 其它
        //+++++++++++++++++++++++++++++++++++++++++++++++++
        //杂项
        @GET("/wenddingroom")
        void other(@Query("moduleTypeId") int moduleTypeId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<OtherResp> cb);

        //详情
        @GET("/wenddingroom/{id}")
        void otherDetail(@Path("id") int id, Callback<Base<OtherDetail>> cb);
    }

    /**
     * 首页菜单
     *
     * @param cb
     */
    public void getHomeMenu(String applicablePlatform, Callback<Base<ArrayList<HomeMenu>>> cb) {
        netInterface.getHomeMenu(applicablePlatform, cb);
    }

    /**
     * 获取首页广告
     *
     * @param id
     * @param cb
     */
    public void getHomeAdvert(@Path("id") String id, Callback<Base<ArrayList<Advert>>> cb) {
        netInterface.getHomeAdvert(id, cb);
    }

    /**
     * 每日客片
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     */
    public void getCustomList(String id, int pageIndex, int pageSize, Callback<Base<List<Custom>>> cb) {
        netInterface.getCustomList(id, pageIndex, pageSize, cb);
    }

    /**
     * 客片筛选
     *
     * @param styleId
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getCustomListByStyleId(String styleId, String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Custom>>> cb) {
        netInterface.getCustomListByStyleId(styleId, id, pageIndex, pageSize, cb);
    }

    /**
     * 客片详情
     *
     * @param id
     * @param detailId
     * @param cb
     */
    public void getDayCustomDetail(String id, String detailId, Callback<Base<ArrayList<Simple>>> cb) {
        netInterface.getDayCustomDetail(id, detailId, cb);
    }

    /**
     * 分季列表
     *
     * @param cb
     */
    public void seasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb) {
        netInterface.seasonList(cb);
    }

    /**
     * 分季查询列表
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param seasonId
     * @param cb
     */
    public void customListBySeasonId(String id, int pageIndex, int pageSize, String seasonId, Callback<Base<List<Custom>>> cb) {
        netInterface.customListBySeasonId(id, pageIndex, pageSize, seasonId, cb);
    }

    /**
     * b1.样片
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     */
    public void getSimples(String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Simple>>> cb) {
        netInterface.getSimples(id, pageIndex, pageSize, cb);
    }

    /**
     * 1.样片筛选---风格
     *
     * @param styleId
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getSimplesByStyleId(String styleId, String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Simple>>> cb) {
        netInterface.getSimplesByStyleId(styleId, id, pageIndex, pageSize, cb);
    }

    /**
     * c1.样片筛选---场景
     *
     * @param addressId
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getSimplesByAddressId(String addressId, String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Simple>>> cb) {
        netInterface.getSimplesByAddressId(addressId, id, pageIndex, pageSize, cb);
    }

    /**
     * b2.样片详情
     *
     * @param id
     * @param detailId
     * @param cb
     */
    public void getSimpleDetail(String id, String detailId, Callback<Base<ArrayList<Simple>>> cb) {
        netInterface.getSimplesDetail(id, detailId, cb);
    }

    /**
     * 获取婚纱套系列表
     *
     * @param suitId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getWeddingSuitList(String suitId, int pageIndex, int pageSize, Callback<Base<ArrayList<WeddingSuit>>> cb) {
        netInterface.getWeddingSuitList(suitId, pageIndex, pageSize, cb);
    }

    /**
     * d2.获取婚纱套系详情
     *
     * @param suitId
     * @param detailId
     * @param cb
     */
    public void getWeddingSuitDetail(String suitId, String detailId, Callback<Base<ArrayList<WeddingSuitDetail>>> cb) {
        netInterface.getWeddingSuitDetail(suitId, detailId, cb);
    }

    /**
     * 摄影师列表
     *
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getPhotographer(int levelId, int pageIndex, int pageSize, Callback<Base<ArrayList<Photographer>>> cb) {
        netInterface.getPhotographer(levelId, pageIndex, pageSize, cb);
    }

    /**
     * 摄影师详情
     *
     * @param id
     * @param cb
     */
    public void getPhotographerDetail(String id, Callback<Base<ArrayList<Photographer>>> cb) {
        netInterface.getPhotographerDetail(id, cb);
    }

    /**
     * e3.摄影师作品列表
     *
     * @param id
     * @param cb
     */
    public void getPhotographerWorks(String id, String workdsId, Callback<Base<WorksDetail>> cb) {
        netInterface.getPhotographerWorks(id, workdsId, cb);
    }

    /**
     * f1.造型师列表
     *
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getStylist(int levelId, int pageIndex, int pageSize, Callback<Base<ArrayList<Photographer>>> cb) {
        netInterface.getStylist(levelId, pageIndex, pageSize, cb);
    }

    /**
     * f2.造型师详情
     *
     * @param id
     * @param cb
     */
    public void getStylistDetail(String id, Callback<Base<ArrayList<Photographer>>> cb) {
        netInterface.getStylistDetail(id, cb);
    }

    /**
     * g1.婚纱类型
     *
     * @param cb
     */
    public void getWeddingDressStyle(Callback<Base<ArrayList<WeddingDressStyle>>> cb) {
        netInterface.getWeddingDressStyle(cb);
    }

    /**
     * g2.婚纱列表
     *
     * @param id
     * @param cb
     */
    public void getWeddingDressList(String id, int pageIndex, int pageSize, Callback<Base<ArrayList<WeddingDress>>> cb) {
        netInterface.getWeddingDressList(id, pageIndex, pageSize, cb);
    }

    /**
     * g3.婚纱详情
     *
     * @param id
     * @param detailId
     * @param cb
     */
    public void getWeddingDressDetail(String id, String detailId, Callback<Base<ArrayList<WeddingDress>>> cb) {
        netInterface.getWeddingDressDetail(id, detailId, cb);
    }

    /**
     * 团队
     *
     * @param teamLevel
     * @param cb
     */
    public void teamList(int teamLevel, Callback<Base<ArrayList<Team>>> cb) {
        netInterface.teamList(teamLevel, cb);
    }

    /**
     * 团队作品详情
     *
     * @param teamId
     * @param workId
     * @param cb
     */
    public void teamWorkDetail(int teamId, String workId, Callback<Base<TeamWorks>> cb) {
        netInterface.teamWorkDetail(teamId, workId, cb);
    }


    //-----------------------------------------------------
    // 版本更新
    //-----------------------------------------------------

    /**
     * @param type
     * @param version
     * @param appUser 1：客户   2：四大金刚 3:统筹师
     * @param cb
     */
    public void checkUpgrade(int type, String version, int appUser, Callback<VersionResp> cb) {
        netInterface.checkUpgrade(type, version, appUser, cb);
    }


    //-----------------------------------------------------
    // 登录/注册
    //-----------------------------------------------------

    /**
     * 登录
     *
     * @param uname
     * @param pwd
     * @param type
     */
    public void login(String uname, String pwd, int type, Callback<LoginResp> cb) {
        netInterface.login(uname, pwd, type, cb);
    }

    /**
     * 获取验证码
     *
     * @param name
     * @param type
     * @param cb
     */
    public void getSmsCode(String name, int type, Callback<BaseResp> cb) {
        netInterface.getSmsCode(name, type, cb);
    }

    /**
     * sign up
     *
     * @param uname
     * @param code
     * @param type
     * @param cityId
     * @param cb
     */
    public void signup(String uname, String code, int type, int cityId, Callback<BaseResp> cb) {
        netInterface.signup(uname, code, type, cityId, cb);
    }

    /**
     * forget
     *
     * @param phone
     * @param code
     * @param cb
     */
    public void forget(String phone, String code, Callback<BaseResp> cb) {
        netInterface.forget(phone, code, cb);
    }


    //---------------------------------------------------------------
    //酒店
    //---------------------------------------------------------------

    /**
     * j 获取区域列表
     * @param cb
     */
    public void getHotelAreas(Callback<Base<List<FilterBean>>> cb){
        netInterface.getHotelAreas(cb);
    }
    /**
     * 获取酒店列表
     *
     * @param id
     */
    public void getHotels(String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.getHotels(id, pageIndex, pageSize, cb);
    }


    /**
     * j3.筛选--桌数
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void filtrateHotelsByTableOrPrice(String id, int pageIndex, int pageSize, String sort, String order, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.filtrateHotelsByTableOrPrice(id, pageIndex, pageSize, sort, order, cb);
    }

    /**
     * j4.筛选--价格
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param maxPrice
     * @param cb
     */
    public void filtrateHotelsByPrice(String id, int pageIndex, int pageSize, int maxPrice, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.filtrateHotelsByPrice(id, pageIndex, pageSize, maxPrice, cb);
    }

    /**
     * j5.筛选--礼包
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isGift
     * @param cb
     */
    public void filtrateHotelsByGift(String id, int pageIndex, int pageSize, int isGift, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.filtrateHotelsByGift(id, pageIndex, pageSize, isGift, cb);
    }

    /**
     * j6.筛选--优惠
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isDiscount
     * @param cb
     */
    public void filtrateHotelsByDiscount(String id, int pageIndex, int pageSize, int isDiscount, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.filtrateHotelsByDiscount(id, pageIndex, pageSize, isDiscount, cb);
    }

    /**
     * j7.筛选--优惠/礼包
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isDiscount
     * @param isGift
     * @param cb
     */
    public void gFiltrateHotelsByDiscountGift(String id, int pageIndex, int pageSize, String isDiscount, String isGift, String sort, String order, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.gFiltrateHotelsByDiscountGift(id, pageIndex, pageSize, isDiscount, isGift, sort, order, cb);
    }

    /**
     * 正式酒店搜索
     * @param id
     * @param p
     */
    public void searchHotels(String id, SearchHotelParam p,  Callback<Base<ArrayList<Hotel>>> cb) {

        Map<String, Object> params = new HashMap<>();

        if (p.minTable != -1) {
            params.put("minTable", p.minTable);
            params.put("maxTable", p.maxTable);
        }

        if (p.minPrice != -1) {
            params.put("minPrice", p.minPrice);
            params.put("maxPrice", p.maxPrice);
        }

        if (p.isDisaccount == 1) {
            params.put("isDisaccount", "1");
        }
        if (p.isGift == 1) {
            params.put("isGift", "1");
        }

        if (!TextUtils.isEmpty(p.order)) {
            params.put("order", p.order);
        }
        if (!TextUtils.isEmpty(p.sort)) {
            params.put("short", p.sort);
        }

        if(p.cityId > 0){
            params.put("cityId", p.cityId);
        }

        params.put("pageIndex", p.pageIndex);
        params.put("pageSize", p.pageSize);

        netInterface.searchHotels(id, params, cb);
    }

    /**
     * j6.筛选--桌数量
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isDiscount
     * @param isGift
     * @param minTable
     * @param maxTable
     * @param cb
     */
    public void gFiltrateHotelsByTableCount(String id, int pageIndex, int pageSize, String isDiscount, String isGift,
                                            int minTable, int maxTable, String sort, String order, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.gFiltrateHotelsByTableCount(id, pageIndex, pageSize, isDiscount, isGift, minTable, maxTable, sort, order, cb);
    }

    /**
     * j6.筛选--价格多少
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isDiscount
     * @param isGift
     * @param minPrice
     * @param maxPrice
     * @param cb
     */
    @GET("/hotel/{id}")
    public void gFiltrateHotelsByPrice(String id, int pageIndex, int pageSize, String isDiscount, String isGift,
                                       int minPrice, int maxPrice, String sort, String order, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.gFiltrateHotelsByPrice(id, pageIndex, pageSize, isDiscount, isGift, minPrice, maxPrice, sort, order, cb);
    }

    /**
     * j6.筛选--所有条件
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param isDiscount
     * @param isGift
     * @param minTable
     * @param maxTable
     * @param minPrice
     * @param maxPrice
     * @param cb
     */
    @GET("/hotel/{id}")
    public void gFiltrateHotelsByAll(String id, int pageIndex, int pageSize, String isDiscount, String isGift,
                                     int minTable, int maxTable, int minPrice, int maxPrice, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.gFiltrateHotelsByAll(id, pageIndex, pageSize, isDiscount, isGift, minTable, maxTable, minPrice, maxPrice, cb);
    }

    //j10.筛选--所有条件 order
    @GET("/hotel/{id}")
    public void gFiltrateHotelsByAllAndOrderDesc(String id, int pageIndex, int pageSize, String isDiscount, String isGift,
                                                 int minTable, int maxTable, int minPrice, int maxPrice, String short_, String order, Callback<Base<ArrayList<Hotel>>> cb) {
        netInterface.gFiltrateHotelsByAllAndOrderDesc(id, pageIndex, pageSize, isDiscount, isGift, minTable, maxTable, minPrice, maxPrice, short_, order, cb);
    }

    /**
     * 获取酒店详情
     *
     * @param modelId
     * @param detailId
     */
    public void getHotelDetail(String modelId, String detailId, Callback<Base<ArrayList<HotelDetail>>> cb) {
        netInterface.getHotelDetail(modelId, detailId, cb);
    }

    //j7.获取宴会厅详情
    public void getBanquetDetail(String modelId, String hotelId, String banquetId, Callback<Base<ArrayList<BanquetDetail>>> cb) {
        netInterface.getBanquetDetail(modelId, hotelId, banquetId, cb);
    }

    //---------------------------------------------------------------
    //策划
    //---------------------------------------------------------------

    /**
     * 案例列表
     *
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getPlanSimples(String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Scheme>>> cb) {
        netInterface.getPlanSimples(id, pageIndex, pageSize, cb);
    }

    /**
     * 通过风格获取案例
     *
     * @param styleId
     * @param id
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getPlanSimplesByStyleId(String styleId, String id, int pageIndex, int pageSize, Callback<Base<ArrayList<Scheme>>> cb) {
        netInterface.getPlanSimplesByStyleId(styleId, id, pageIndex, pageSize, cb);
    }

    /**
     * 案例详情
     *
     * @param parentId
     * @param detailId
     * @param cb
     */
    public void getPlanDetail(String parentId, String detailId, Callback<Base<ArrayList<SchemeDetail>>> cb) {
        netInterface.getPlanDetail(parentId, detailId, cb);
    }

    /**
     * @param parentId
     * @param detailId
     * @param cb
     */
    public void getPlanNewDetail(@Path("parentId") String parentId, @Path("detailId") String detailId, Callback<Base<ArrayList<PlanDetail>>> cb) {
        netInterface.getPlanNewDetail(parentId, detailId, cb);
    }

    /**
     * c3.分季列表
     *
     * @param cb
     */
    public void planSeasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb) {
        netInterface.planSeasonList(cb);
    }

    /**
     * c4.最佳跟拍
     *
     * @param moduleId
     * @param cb
     */
    public void bestFilmList(String moduleId, int pageIndex, int pageSize, Callback<Base<ArrayList<Scheme>>> cb) {
        netInterface.bestFilmList(moduleId, pageIndex, pageSize, cb);
    }


    /**
     * c5.通过分季查询跟拍案例
     *
     * @param moduleId
     * @param pageIndex
     * @param pageSize
     * @param seasonId
     * @param cb
     */
    public void planListBySeasonId(String moduleId, int pageIndex, int pageSize, String seasonId, Callback<Base<ArrayList<Scheme>>> cb) {
        netInterface.planListBySeasonId(moduleId, pageIndex, pageSize, seasonId, cb);
    }

    /**
     * c1.风格列表
     *
     * @param cb
     */
    public void getStyleList(Callback<AddrStyleResp> cb) {
        netInterface.getStyleList(cb);
    }


    /**
     * 案例风格列表
     *
     * @param cb
     */
    public void getSchemeList(Callback<Base<ArrayList<Style>>> cb) {
        netInterface.getSchemeList(cb);
    }


    /////////////////////////////////////////////////////////////////
    /// 四大金刚
    ////////////////////////////////////////////////////////////////
    //.F4-emcee 主持人
    public void getEmceeList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb) {
        netInterface.getEmceeList(pageIndex, pageSize, cb);
    }

    //.F4-dresser 化妆师
    public void getDresserList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb) {
        netInterface.getDresserList(pageIndex, pageSize, cb);
    }

    //.F4-photographer 摄影师
    public void getPhotographerList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb) {
        netInterface.getPhotographerList(pageIndex, pageSize, cb);
    }

    //.F4-cameraman 摄像师
    public void getCameramanList(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<F4>>> cb) {
        netInterface.getCameramanList(pageIndex, pageSize, cb);
    }


    //////////////////////////////////////////////////
    // 策划师
    //////////////////////////////////////////////////

    /**
     * 选策划师
     *
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void getPlannerList(int pageIndex, int pageSize, Callback<Base<ArrayList<Planner>>> cb) {
        netInterface.getPlannerList(pageIndex, pageSize, cb);
    }


    //*********************************************************
    // 微电影
    //**********************************************************

    /**
     * 选策划师
     *
     * @param sort      时间排序:date 访问量:hits
     * @param videoType 1：婚纱纪实MV 2：婚礼跟拍视频 3：婚礼视频欣赏 4：爱情MV 5：爱情微电影       如有合集那么传多个用","逗号隔开
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void filmList(String sort, String videoType, int pageIndex, int pageSize, Callback<Base<ArrayList<Film>>> cb) {
        netInterface.filmList(sort, videoType, pageIndex, pageSize, cb);
    }

    /**
     * 分季
     *
     * @param cb
     */
    public void filmSeasonList(Callback<Base<ArrayList<MasterFanSeason>>> cb) {
        netInterface.filmSeasonList(cb);
    }

    /**
     * 通过分季筛选视频列表
     *
     * @param sort
     * @param videoType
     * @param pageIndex
     * @param pageSize
     * @param seasonId
     * @param cb
     */
    public void filmListBySeasonId(String sort, String videoType, int pageIndex, int pageSize, String seasonId, Callback<Base<ArrayList<Film>>> cb) {
        netInterface.filmListBySeasonId(sort, videoType, pageIndex, pageSize, seasonId, cb);
    }

    //==================================================
    // 婚纱礼服
    //==================================================

    /**
     * 品牌列表
     *
     * @param weddingDressType
     * @param cb
     */
    public void brandsList(@Query("weddingDressType") int weddingDressType, Callback<BrandsResp> cb) {
        netInterface.brandsList(weddingDressType, cb);
    }

    /**
     * 品牌下婚纱列表
     *
     * @param brandId
     * @param cb
     */
    public void dressListByBrandId(@Path("brandId") int brandId, Callback<Base<ArrayList<Dress>>> cb) {
        netInterface.dressListByBrandId(brandId, cb);
    }

    //################################################
    // 租车
    //################################################

    /**
     * 车列表
     *
     * @param moduleId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void carList(@Path("moduleId") String moduleId, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize, Callback<Base<ArrayList<Car>>> cb) {
        netInterface.carList(moduleId, pageIndex, pageSize, cb);
    }

    /**
     * 车详情
     *
     * @param moduleId
     * @param detailId
     * @param cb
     */
    public void carDetail(@Path("moduleId") String moduleId, @Path("detailId") String detailId, Callback<Base<CarDetail>> cb) {
        netInterface.carDetail(moduleId, detailId, cb);
    }

    /**
     * 品牌
     *
     * @param cb
     */
    public void carBrandList(Callback<Base<ArrayList<CarType>>> cb) {
        netInterface.carBrandList(cb);
    }

    /**
     * 类型
     *
     * @param cb
     */
    public void carTypeList(Callback<Base<ArrayList<CarType>>> cb) {
        netInterface.carTypeList(cb);
    }

    /**
     * 车搜索
     *
     * @param moduleId    模块ID
     * @param carModelsId 婚车类型
     * @param carBrandId  品牌
     * @param priceStart
     * @param priceEnd
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void carSearch(String moduleId, String carModelsId, String carBrandId, String priceStart, String priceEnd, int pageIndex, int pageSize, Callback<Base<ArrayList<Car>>> cb) {
        netInterface.carSearch(moduleId, carModelsId, carBrandId, priceStart, priceEnd, pageIndex, pageSize, cb);
    }

    /**
     * 车所有条件搜索
     *
     * @param moduleId
     * @param carNature
     * @param carModelsId
     * @param carBrandId
     * @param priceStart
     * @param priceEnd
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void carSearchParamAll(String moduleId, int carNature, String carModelsId, String carBrandId, String priceStart, String priceEnd, int pageIndex, int pageSize, Callback<Base<ArrayList<Car>>> cb) {
        netInterface.carSearchParamAll(moduleId, carNature, carModelsId, carBrandId, priceStart, priceEnd, pageIndex, pageSize, cb);
    }

    /**
     * 车队/单车 搜索
     *
     * @param moduleId
     * @param carNature
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void carSearch(String moduleId, int carNature, int pageIndex, int pageSize, Callback<Base<ArrayList<Car>>> cb) {
        netInterface.carSearch(moduleId, carNature, pageIndex, pageSize, cb);
    }


    //￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥
    // 用品
    //￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥

    /**
     * 用品列表
     *
     * @param moduleId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void supplieList(String moduleId, int pageIndex, int pageSize, Callback<Base<ArrayList<Supplie>>> cb) {
        netInterface.supplieList(moduleId, pageIndex, pageSize, cb);
    }

    /**
     * 用品详情
     *
     * @param moduleId
     * @param detailId
     * @param cb
     */
    public void supplieDetail(String moduleId, String detailId, Callback<Base<SupplieDetail>> cb) {
        netInterface.supplieDetail(moduleId, detailId, cb);
    }

    /**
     * 获取用品类型
     *
     * @param cb
     */
    public void supplieTypes(Callback<Base<ArrayList<CarType>>> cb) {
        netInterface.supplieTypes(cb);
    }

    /**
     * 用品查询
     *
     * @param moduleId
     * @param weddingSuppliesTypeId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void supplieSearch(String moduleId, int weddingSuppliesTypeId, int pageIndex, int pageSize, Callback<Base<ArrayList<Supplie>>> cb) {
        netInterface.supplieSearch(moduleId, weddingSuppliesTypeId, pageIndex, pageSize, cb);
    }


    //+++++++++++++++++++++++++++++++++++++++++++++++++
    // 其它
    //+++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * 杂项
     *
     * @param moduleTypeId
     * @param pageIndex
     * @param pageSize
     * @param cb
     */
    public void other(int moduleTypeId, int pageIndex, int pageSize, Callback<OtherResp> cb) {
        netInterface.other(moduleTypeId, pageIndex, pageSize, cb);
    }

    /**
     * 详情
     *
     * @param id
     * @param cb
     */
    public void otherDetail(int id, Callback<Base<OtherDetail>> cb) {
        netInterface.otherDetail(id, cb);
    }
}
