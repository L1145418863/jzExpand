package com.example.administrator.newjz;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzg.musicplayer.MyMusicPlayerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class MainActivity extends AppCompatActivity {

    private JZVideoPlayerStandard jzvideoplayerstandard;
    private MyMusicPlayerView musicView;
    private String[] mediaName = {"普通", "原画"};//可以做选集
    private List<Object[]> list = new ArrayList<Object[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("打印", "打印");
    }

    private void initView() {
        jzvideoplayerstandard = (JZVideoPlayerStandard) findViewById(R.id.wodeshipin);
        musicView = (MyMusicPlayerView) findViewById(R.id.my_music_view);

//--------------------------------------------------------------------------------
        Object[] objects = new Object[3];
        LinkedHashMap map = new LinkedHashMap();
        for (int i = 0; i < 2; i++) {
            map.put(mediaName[i], "https://onlyboss.oss-cn-beijing.aliyuncs.com/video/17floor_trailer.mp4");
        }
        objects[0] = map;
        objects[1] = false;
        objects[2] = new HashMap<>();
        ((HashMap) objects[2]).put("key", "value");
//--------------------------------------------------------------------------------
        Object[] objects1 = new Object[3];
        LinkedHashMap map1 = new LinkedHashMap();
        for (int i = 0; i < 2; i++) {
            map1.put(mediaName[i], "http://ssb-video.oss-cn-qingdao.aliyuncs.com/Video_1003_20161027140007.mp4");
        }
        objects1[0] = map1;
        objects1[1] = false;
        objects1[2] = new HashMap<>();
        ((HashMap) objects1[2]).put("key", "value");
//--------------------------------------------------------------------------------
        list.add(objects);
        list.add(objects1);
        /*list.add(objects);
        list.add(objects1);
        list.add(objects);
        list.add(objects1);*/

        jzvideoplayerstandard.addSelection(list);
        jzvideoplayerstandard.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this)
                .load("http://image.onlyboss.com/39/9ccef74e0006e114e1d894ae60ec7a.jpg")
                .into(jzvideoplayerstandard.thumbImageView);
        /**
         * 清晰度切换
         */
        jzvideoplayerstandard.setUp(list.get(0), 0
                , JZVideoPlayerStandard.SCROLL_AXIS_HORIZONTAL, "饺子视频播放器功能添加");
        /**
         * 单视频播放
         */
        /*jzvideoplayerstandard.setUp("https://onlyboss.oss-cn-beijing.aliyuncs.com/video/17floor_trailer.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);*/

        jzvideoplayerstandard.setonVideoEndLinstener(new JZVideoPlayerStandard.onVideoEndLinstener() {
            @Override
            public void videoEndListener(int index) {
                Toast.makeText(MainActivity.this, "第" + (index + 1) + "集播放结束", Toast.LENGTH_SHORT).show();
                jzvideoplayerstandard.setUp(list.get(index), 0
                        , JZVideoPlayerStandard.SCROLL_AXIS_HORIZONTAL, "饺子视频播放器功能添加");
            }
        });
        jzvideoplayerstandard.startVideo();
        //设置全屏播放
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;  //横向

        //---------------------------------音频-------------------
        musicView.setUp("http://file.kuyinyun.com/group1/M00/90/B7/rBBGdFPXJNeAM-nhABeMElAM6bY151.mp3","http://image.onlyboss.com/39/9ccef74e0006e114e1d894ae60ec7a.jpg");

    }

    @Override//点击返回键不关闭当前activity
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

}
