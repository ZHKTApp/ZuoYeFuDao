package com.zwyl.homeworkhelp.main;

import android.os.Build;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;


import com.zwyl.homeworkhelp.R;
import com.zwyl.homeworkhelp.base.BaseActivity;
import com.zwyl.homeworkhelp.util.CustomerVideoView;

import butterknife.BindView;

public class PlayActivity extends BaseActivity {
    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        super.initView();
        setHeadView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        String resourceUri = getIntent().getStringExtra("resourceUri");
        if(resourceUri != null) {
            videoView.setMediaController(new MediaController(PlayActivity.this));
            videoView.setVideoPath(resourceUri);
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    /**
     * 设置顶部点击事件
     */
    private void setHeadView() {
        setTitleCenter("视频");
        setShowLeftHead(false);//左边顶部按钮
        setShowRightHead(false);//右边顶部按钮
        setShowFilter(false);//日历筛选
        setShowLogo(true);//logo
        setShowRefresh(false);//刷新
        setLogoClick(v -> {
            finish();
        });
    }

}
