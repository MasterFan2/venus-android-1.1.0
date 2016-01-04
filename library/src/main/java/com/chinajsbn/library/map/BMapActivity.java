package com.chinajsbn.library.map;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//public class BMapActivity extends Activity implements OnGetRoutePlanResultListener,View.OnClickListener{
//
//    private MapView mapView;    //地图view
//    private BaiduMap baiduMap;  //控制器
//    private Button driveBtn, busBtn, walkBtn;
//    private EditText startEdit, endEdit;
//    private boolean isFirstLoc = true;  //是否首次定位
//
//    //-----------搜索模块，也可去掉地图模块独立使用------------
//    private RoutePlanSearch mSearch = null;//
//    private OverlayManager routeOverlay = null;
//    private RouteLine route = null;
//
//    //------------------------定位相关------------------------
//    private LocationClient mLocClient;
//    public MyLocationListenner myListener = new MyLocationListenner();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bmap);
//
//        //initialize
//        mapView = (MapView) findViewById(R.id.bMapView);
//        baiduMap = mapView.getMap();
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        baiduMap.setMyLocationEnabled(true);
//
//        startEdit = (EditText) findViewById(R.id.map_start_edit);
//        endEdit = (EditText) findViewById(R.id.map_end_edit);
//
//        driveBtn = (Button) findViewById(R.id.drive_btn);
//        driveBtn.setOnClickListener(this);
//
//        busBtn = (Button) findViewById(R.id.bus_btn);
//        busBtn.setOnClickListener(this);
//
//        walkBtn = (Button) findViewById(R.id.walk_btn);
//        walkBtn.setOnClickListener(this);
//
//        //配置位置信息
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//
//        //初始化定位模块
//        mLocClient.start();
//
//        // 初始化搜索模块，注册事件监听
//        mSearch = RoutePlanSearch.newInstance();
//        mSearch.setOnGetRoutePlanResultListener(this);
//    }
//
//    //=================================搜索结果=================================
//    @Override
//    public void onGetWalkingRouteResult(WalkingRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
////            return;
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            SuggestAddrInfo suggestAddrInfo = result.getSuggestAddrInfo();
//            PoiInfo poiInfo ;
//            if(suggestAddrInfo.getSuggestStartNode() != null && suggestAddrInfo.getSuggestStartNode().size() > 0) {
//                poiInfo = suggestAddrInfo.getSuggestStartNode().get(0);
//                startEdit.setText(poiInfo.address);
//            }
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////            nodeIndex = -1;
////            mBtnPre.setVisibility(View.VISIBLE);
////            mBtnNext.setVisibility(View.VISIBLE);
//            route = result.getRouteLines().get(0);
//            WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
//            baiduMap.setOnMarkerClickListener(overlay);
//            routeOverlay = overlay;
//            overlay.setData(result.getRouteLines().get(0));
//            overlay.addToMap();
//            overlay.zoomToSpan();
//        }
//    }
//
//    @Override
//    public void onGetTransitRouteResult(TransitRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            //result.getSuggestAddrInfo()
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            route = result.getRouteLines().get(0);
//            TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
//            baiduMap.setOnMarkerClickListener(overlay);
//            routeOverlay = overlay;
//            overlay.setData(result.getRouteLines().get(0));
//            overlay.addToMap();
//            overlay.zoomToSpan();
//        }
//    }
//
//    @Override
//    public void onGetDrivingRouteResult(DrivingRouteResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            //result.getSuggestAddrInfo()
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            route = result.getRouteLines().get(0);
//            DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
//            routeOverlay = overlay;
//            baiduMap.setOnMarkerClickListener(overlay);
//            overlay.setData(result.getRouteLines().get(0));
//            overlay.addToMap();
//            overlay.zoomToSpan();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        String start = startEdit.getText().toString();
//        String end   = endEdit  .getText().toString();
//        if(TextUtils.isEmpty(start) || TextUtils.isEmpty(end)){
//            Toast.makeText(BMapActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        baiduMap.clear();
//        PlanNode stNode = PlanNode.withCityNameAndPlaceName("重庆", start);
//        PlanNode enNode = PlanNode.withCityNameAndPlaceName("重庆", end);
//
//        if(v.getId() == R.id.drive_btn){
//            mSearch.drivingSearch((new DrivingRoutePlanOption())
//                    .from(stNode)
//                    .to(enNode));
//        }else if(v.getId() == R.id.bus_btn){
//            mSearch.transitSearch((new TransitRoutePlanOption())
//                    .from(stNode)
//                    .city("重庆市")
//                    .to(enNode));
//        }else if(v.getId() == R.id.walk_btn){
//            mSearch.walkingSearch((new WalkingRoutePlanOption())
//                    .from(stNode)
//                    .to(enNode));
//        }
//    }
//    //=================================搜索结果=================================
//
//
//    /**
//     * 定位SDK监听函数
//     */
//    public class MyLocationListenner implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            // map view 销毁后不在处理新接收的位置
//            if (location == null || baiduMap == null)
//                return;
//
//            //
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            baiduMap.setMyLocationData(locData);
//
//            //
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                baiduMap.animateMapStatus(u);
//            }
//
//            mLocClient.stop();//停止定位
//        }
//
//        public void onReceivePoi(BDLocation poiLocation) {
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//}
