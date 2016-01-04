package com.chinajsbn.venus.ui.plan;

import android.media.MediaPlayer;
import android.text.TextUtils;
import android.widget.MediaController;

import com.chinajsbn.venus.R;
import com.chinajsbn.venus.ui.base.ActivityFeature;
import com.chinajsbn.venus.ui.base.MBaseFragmentActivity;
import com.chinajsbn.venus.utils.T;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tool.widget.FullScreenVideoView;

@ActivityFeature(layout = R.layout.activity_video)
public class VideoActivity extends MBaseFragmentActivity {

    @ViewInject(R.id.videoView)
    private FullScreenVideoView videoView;

    private MediaController controller ;

    @Override
    public void initialize() {

        String url = getIntent().getStringExtra("url");
        if(TextUtils.isEmpty(url)) {
            T.s(context, "视频地址错误，无法播放!");
            return;
        }

        controller = new MediaController(context);
        videoView.setVideoPath(url);
        videoView.setMediaController(controller);
        videoView.start();
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                
            }
        });
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

}
