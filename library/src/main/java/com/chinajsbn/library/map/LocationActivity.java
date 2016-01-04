//package com.chinajsbn.library.map;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.chinajsbn.library.R;
//
//public class LocationActivity extends Activity {
//
//    private LocationClient mLocationClient;
//    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
//    private String tempcoor = "gcj02";
//    private Button btn;
//    private TextView txt;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location);
//
//        //
//        btn = (Button) findViewById(R.id.btn);
//        txt = (TextView) findViewById(R.id.text);
//
//        // 定位初始化
//        mLocationClient = new LocationClient(this);
//        mLocationClient.registerLocationListener(new MyLocationListener());
//
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);// 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//
//        mLocationClient.setLocOption(option);
//        mLocationClient.start();
//
//
//        btn.setText("开始定位");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InitLocation();
//
//                if (btn.getText().equals("开始定位")) {
//                    mLocationClient.start();
//                    btn.setText("停止定位");
//                } else {
//                    mLocationClient.stop();
//                    btn.setText("开始定位");
//                }
//            }
//        });
//
//    }
//
//    private void InitLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(tempMode);//设置定位模式
//        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
//        int span = 1000;
//        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }
//
//    /**
//     * 实现实位回调监听
//     */
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation) {
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\ndirection : ");
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append(location.getDirection());
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//            }
//            txt.setText("定位信息：" + sb.toString());
//            Log.i("BaiduLocationApiDem", sb.toString());
//
//            //
//            mLocationClient.stop();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        mLocationClient.stop();
//        super.onStop();
//    }
//}
