package com.chinajsbn.venus.ui;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.chinajsbn.venus.R;

import java.io.IOException;

public class SurfacePlayActivity extends ActionBarActivity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener,MediaPlayer.OnInfoListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener,MediaPlayer.OnVideoSizeChangedListener,SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private Display currDisplay;
    private SurfaceHolder holder;
    private MediaPlayer player;
    private int vWidth,vHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_play);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.addCallback(this);

        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnInfoListener(this);
        player.setOnPreparedListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnVideoSizeChangedListener(this);

        try {
            player.setDataSource(this, Uri.parse("http://219.153.52.24/v.cctv.com/flash/mp4video1/huangjinqiangdang/2010/01/02/huangjinqiangdang_h264818000nero_aac32000_20100102_1262437187617-1.mp4?wshc_tag=0&wsts_tag=555b007d&wsid_tag=7d560363&wsiphost=ipdbm"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currDisplay = this.getWindowManager().getDefaultDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_surface_play, menu);
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

    //--------------------------start-SurfaceView--------------------------------------
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        //prepare
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("尺寸改变");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v("Surface Destory:::", "surfaceDestroyed called");
    }
    //--------------------------end-SurfaceView--------------------------------------

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v("Video Size Change", "onVideoSizeChanged called");
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        Log.v("Seek Completion", "onSeekComplete called");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.v("Play Over:::", "onComletion called");
        this.finish();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch(what){
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:

                break;

            case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:

                break;

            case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:

                break;

            case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:

                break;

        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        float wRatio = (float)vWidth/(float)currDisplay.getWidth();

        float hRatio = (float)vHeight/(float)currDisplay.getHeight();
        //选择大的一个进行缩放
        float ratio = Math.max(wRatio, hRatio);
        vWidth = (int)Math.ceil((float)vWidth/ratio);
        vHeight = (int)Math.ceil((float)vHeight/ratio);
        //设置surfaceView的布局参数
        surfaceView.setLayoutParams(new RelativeLayout.LayoutParams(vWidth, vHeight));
        //然后开始播放视频
        player.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
