package com.chinajsbn.venus.ui;

import android.view.View;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.BaseActivity;
import com.chinajsbn.venus.ui.home.HomeActivity;
import com.tool.widget.FlakeView;

@ActivityFeature(full_screen = true, layout = R.layout.activity_launcher)
public class LauncherActivity extends BaseActivity {

//    private float x, y, z;
//    private TextView textView;
//    private SensorManager sensorMgr;
//    private Sensor sensor;

    //
    private FlakeView flakeView;

//    @OnClick(R.id.launcher_img)
    public void goMenu(View view){
        animStart(HomeActivity.class);
        finish();
    }


    @Override
    public void onKeydown() {

    }

    @Override
    public void initialize() {
        flakeView = new FlakeView(this);
        flakeView.addFlakes(20);
//        container.addView(flakeView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
//        sensorMgr .registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME /* SENSOR_DELAY_GAME */);
    }

    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
//        sensorMgr.unregisterListener(sensorEventListener);
    }

//    SensorEventListener sensorEventListener = new SensorEventListener() {
//        @Override
//        public void onSensorChanged(SensorEvent e) {
//            x = e.values[SensorManager.DATA_X];
//            y = e.values[SensorManager.DATA_Y];
//            z = e.values[SensorManager.DATA_Z];
//            // angle = Math.atan(x / y);
//            // if (((angle - last_angle) > 0.5)
//            // || ((angle - last_angle) < -0.5)) {
//            // Toast.makeText(SensorTest.this, "Love", 1000).show();
//            Random mRandom = new Random();
//            int Red = mRandom.nextInt(30);
//            String str = "" + Red;
//            if (x > 7 && y > 6 && z < 8) {
//                Toast.makeText(LauncherActivity.this, str, Toast.LENGTH_LONG).show();
//            }
//            textView.setText("x=" + (int) x + "," + "y=" + (int) y + "," + "z=" + (int) z);
//            // } else {
//            // Toast.makeText(SensorTest.this, "i gao ni", 1000).show();
//            // }
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//        }
//    };
}
