package com.chinajsbn.venus.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.chinajsbn.venus.R;

public class PlayActivity extends ActionBarActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mediaController = new MediaController(this);
        videoView = (VideoView) findViewById(R.id.videoView);//rtmp://192.168.1.136:1935/flvplayback/back/mp4:11.mp4

        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);

        videoView.setVideoPath("http://219.153.52.24/v.cctv.com/flash/mp4video1/huangjinqiangdang/2010/01/02/huangjinqiangdang_h264818000nero_aac32000_20100102_1262437187617-1.mp4?wshc_tag=0&wsts_tag=555b007d&wsid_tag=7d560363&wsiphost=ipdbm");
        videoView.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
