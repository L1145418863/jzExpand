package com.lzg.musicplayer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 音乐播放器View
 */
public class MyMusicPlayerView extends RelativeLayout {
    //控件
    public ImageView my_music_background;
    public ImageView my_music_image;
    public ImageView my_music_start;
    public SeekBar my_music_seek;
    public LinearLayout my_music_loaderror;
    public ImageView my_music_more;
    public TextView my_music_time;
    public TextView my_music_endtime;
    public TextView my_music_showpopup;
    //变量
    private int seekTo = 0;
    private int anInt;
    private int duration;
    private int anIntTemp;
    private int status = 0;
    private MediaPlayer mediaPlayer;
    private Context context;
    private String path;
    private ObjectAnimator myAnimator;
    private PopupWindow popupWindow;
    private int thisViewHeight;
    private int soundSize = 10;
    private int speedText = 10001;
    private MusicPlayerComplete musicPlayerComplete;
    //常量
    private final int SEEK_MAX = 1000;
    private final int MEDIA_STATUS_ISNOTSTART = 0;//未开始播放
    private final int MEDIA_STATUS_ISSTART = 1;//已经开始播放
    private final int MEDIA_STATUS_ISEND = 2;//播放结束
    private final int MEDIA_STATUS_ISERROR = 3;//播放出错
    private final int MEDIA_URL_NULL = 4;//url为空
    private final float MEDIA_SPEED_1_0 = 1.0f;//一倍速
    private final float MEDIA_SPEED_1_25 = 1.25f;//一点二倍速
    private final float MEDIA_SPEED_1_5 = 1.5f;//一点五倍速
    private final float MEDIA_SPEED_2_0 = 2.0f;//二倍速
    private final int MEDIA_CHECKEDSPEED_1_0 = 10001;//一倍速 选中标识
    private final int MEDIA_CHECKEDSPEED_1_25 = 10002;//一点二倍速 选中标识
    private final int MEDIA_CHECKEDSPEED_1_5 = 10003;//一点五倍速 选中标识
    private final int MEDIA_CHECKEDSPEED_2_0 = 10004;//二倍速 选中标识

    /**
     * 进度条
     */
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            my_music_seek.setProgress(seekTo);//修改进度到---
            seekTo++;
            //进度调 进度
            if (seekTo >= SEEK_MAX) {
                handler.removeCallbacksAndMessages(null);
            } else {
                handler.sendEmptyMessageDelayed(1, anInt);
            }
            my_music_time.setText(getTime(seekTo * anInt));
            return false;
        }
    });

    public MyMusicPlayerView(Context context) {
        this(context, null);
    }

    public MyMusicPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyMusicPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        findView(context);
    }

    /**
     * 寻找控件
     *
     * @param context 上下文
     */
    private void findView(Context context) {
        View view = View.inflate(context, R.layout.my_music_player, this);
        initView(view);
        initMediaPlayer();
        initListener(view);
        initAnimation();
    }

    /**
     * 初始化View
     *
     * @param view 布局
     */
    private void initView(View view) {
        my_music_background = (ImageView) view.findViewById(R.id.my_music_background);
        my_music_image = (ImageView) view.findViewById(R.id.my_music_image);
        my_music_start = (ImageView) view.findViewById(R.id.my_music_start);
        my_music_seek = (SeekBar) view.findViewById(R.id.my_music_seek);
        my_music_loaderror = (LinearLayout) view.findViewById(R.id.my_music_loaderror);
        my_music_more = (ImageView) view.findViewById(R.id.my_music_more);
        my_music_time = (TextView) view.findViewById(R.id.my_music_time);
        my_music_endtime = (TextView) view.findViewById(R.id.my_music_endtime);
        my_music_showpopup = (TextView) view.findViewById(R.id.my_music_showpopup);

        my_music_seek.setMax(SEEK_MAX);
    }

    /**
     * 初始化监听
     *
     * @param view 布局
     */
    private void initListener(View view) {
        //播放进度
        my_music_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //手动改变进度
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = my_music_seek.getProgress();
                mediaPlayer.seekTo(progress * anInt);
                seekTo = progress;
            }
        });
        //播放 暂停 重播
        my_music_start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectStatus();
            }
        });

        //播放出错
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                status = MEDIA_STATUS_ISERROR;
                my_music_start.setImageResource(R.drawable.music_restart_normal);
                handler.removeCallbacksAndMessages(null);
                stopAnimation();
                return false;
            }
        });
        //获取当前控件高度
        my_music_background.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            my_music_background.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            my_music_background.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                        thisViewHeight = my_music_background.getHeight();
                    }
                });
        //播放完成
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekTo = SEEK_MAX;
                status = MEDIA_STATUS_ISEND;
                my_music_start.setImageResource(R.drawable.music_restart_normal);
                stopAnimation();
                //播放完成监听 外部方法
                if (musicPlayerComplete != null) {
                    musicPlayerComplete.musicComplete();
                }
            }
        });
        //点击更多功能
        my_music_more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoupWind();
            }
        });
    }

    /**
     * 初始化 MediaPlayer
     */
    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        if (TextUtils.isEmpty(path)) {
            status = MEDIA_URL_NULL;
        } else {
            status = MEDIA_STATUS_ISNOTSTART;
        }
    }

    /**
     * 播放结束 接口回调
     *
     * @param musicPlayerComplete 接口
     */
    public void setMusicPlayerComplete(MusicPlayerComplete musicPlayerComplete) {
        this.musicPlayerComplete = musicPlayerComplete;
    }

    /**
     * 展示更多功能弹框
     */
    private void showPoupWind() {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(thisViewHeight);

        View popupVeiw = LayoutInflater.from(context).inflate(R.layout.my_music_popup, null);
        popupWindow.setContentView(popupVeiw);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(my_music_showpopup);

        final RadioGroup my_music_bsgroup = (RadioGroup) popupVeiw.findViewById(R.id.my_music_bsgroup);
        SeekBar my_music_sound = (SeekBar) popupVeiw.findViewById(R.id.my_music_sound);

        //上一次popupwindow打开时的音量和倍速
        my_music_bsgroup.check(getCheckedId());
        my_music_sound.setProgress(soundSize);
        changedCheckedColor(my_music_bsgroup, getCheckedId());
        //radiobutton 选中
        my_music_bsgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.my_music_bsbtn1) {
                    speedText = MEDIA_CHECKEDSPEED_1_0;
                    changedSpeed(MEDIA_SPEED_1_0);
                } else if (checkedId == R.id.my_music_bsbtn1_25) {
                    speedText = MEDIA_CHECKEDSPEED_1_25;
                    changedSpeed(MEDIA_SPEED_1_25);
                } else if (checkedId == R.id.my_music_bsbtn1_5) {
                    speedText = MEDIA_CHECKEDSPEED_1_5;
                    changedSpeed(MEDIA_SPEED_1_5);

                } else if (checkedId == R.id.my_music_bsbtn2) {
                    speedText = MEDIA_CHECKEDSPEED_2_0;
                    changedSpeed(MEDIA_SPEED_2_0);
                }
                changedCheckedColor(my_music_bsgroup, checkedId);
            }
        });
        //播放音量
        my_music_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                soundSize = seekBar.getProgress();
                float soundSize = progress * 0.1f;
                mediaPlayer.setVolume(soundSize, soundSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //点击背景关闭弹框
        popupVeiw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 改变选中btn颜色
     *
     * @param group     单选按钮的组
     * @param checkedId 组内选中的id
     */
    private void changedCheckedColor(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            if (i != 0) {
                int id = group.getChildAt(i).getId();
                RadioButton childAt = (RadioButton) group.getChildAt(i);
                if (id == checkedId) {
                    childAt.setTextColor(Color.rgb(216, 176, 118));
                } else {
                    childAt.setTextColor(Color.rgb(255, 255, 255));
                }
            }
        }
    }

    /**
     * 倍速选中
     *
     * @return view的id
     */
    private int getCheckedId() {
        int checkedId = R.id.my_music_bsbtn1;
        switch (speedText) {
            case MEDIA_CHECKEDSPEED_1_0:
                return checkedId;
            case MEDIA_CHECKEDSPEED_1_25:
                checkedId = R.id.my_music_bsbtn1_25;
                return checkedId;
            case MEDIA_CHECKEDSPEED_1_5:
                checkedId = R.id.my_music_bsbtn1_5;
                return checkedId;
            case MEDIA_CHECKEDSPEED_2_0:
                checkedId = R.id.my_music_bsbtn2;
                return checkedId;
        }
        return checkedId;
    }

    /**
     * 设置 MP3地址 和背景图地址
     *
     * @param path     Mp3的路径
     * @param imageUrl 封面图路径
     */
    public void setUp(String path, String imageUrl) {
        this.path = path;//播放地址
        try {
            if (TextUtils.isEmpty(path)) {
                //播放地址无效
                status = MEDIA_URL_NULL;
            } else {
                //有效播放地址
                Uri parse = Uri.parse(path);
                mediaPlayer.setDataSource(context, parse);
                mediaPlayer.prepare();
                status = MEDIA_STATUS_ISNOTSTART;
                //视频总长度
                duration = mediaPlayer.getDuration();
                //seekbar最大长度
                anInt = duration / SEEK_MAX;
                //seekbar每一次更新的时间
                anIntTemp = anInt;
                String time = getTime(duration);
                my_music_endtime.setText(time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //高斯模糊背景
        Glide.with(context)
                .load(TextUtils.isEmpty(imageUrl) ? R.drawable.music_play_icon : imageUrl)
                .dontAnimate()
                .bitmapTransform(new BlurTransformation(context, 40, 3))
                .into(my_music_background);
        //音乐封面
        Glide.with(context)
                .load(TextUtils.isEmpty(imageUrl) ? R.drawable.music_play_icon : imageUrl)
                .dontAnimate()
                .transform(new GlideCircleUtil(context))
                .into(my_music_image);
    }

    /**
     * 将int值转换成时长
     *
     * @return 时长
     */
    private String getTime(int durations) {
        int time = durations / 1000;
        int temp;
        StringBuffer sb = new StringBuffer();
        temp = time / 3600;
        if (temp != 0) {//时长不超过一小时则不添加
            sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        }
        temp = time % 3600 / 60;
        sb.append((temp < 10) ? "0" + temp + ":" : "" + temp + ":");
        temp = time % 3600 % 60;
        sb.append((temp < 10) ? "0" + temp : "" + temp);
        return sb.toString();
    }

    /**
     * 播放状态 选中状态
     */
    private void SelectStatus() {
        startMyAnimation();
        switch (status) {
            case MEDIA_URL_NULL:
                Toast.makeText(context, "无效播放地址", Toast.LENGTH_SHORT).show();
                my_music_start.setImageResource(R.drawable.music_play_normal);
                stopAnimation();
                break;
            case MEDIA_STATUS_ISNOTSTART:
                MediaStart();
                break;
            case MEDIA_STATUS_ISSTART:
                MediaPause();
                break;
            case MEDIA_STATUS_ISEND:
                MediaStart();
                break;
            case MEDIA_STATUS_ISERROR:
                MediaStart();
                my_music_start.setVisibility(INVISIBLE);
                my_music_loaderror.setVisibility(VISIBLE);
                break;
        }
    }

    /**
     * 暂停播放
     */
    private void MediaPause() {
        mediaPlayer.pause();
        status = MEDIA_STATUS_ISNOTSTART;
        my_music_start.setVisibility(VISIBLE);
        my_music_loaderror.setVisibility(INVISIBLE);
        my_music_start.setImageResource(R.drawable.music_play_normal);
        handler.removeCallbacksAndMessages(null);
        stopAnimation();
    }

    /**
     * 视频 开始播放 继续播放 重新播放
     */
    private void MediaStart() {
        if (seekTo >= 1000) {
            seekTo = 0;
        }
        status = MEDIA_STATUS_ISSTART;
        my_music_start.setImageResource(R.drawable.music_pause_normal);
        my_music_start.setVisibility(VISIBLE);
        my_music_loaderror.setVisibility(INVISIBLE);
        mediaPlayer.start();
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(1, 0);
        startMyAnimation();
    }

    /**
     * 播放速度
     *
     * @param speed
     */
    private void changedSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!TextUtils.isEmpty(path)) {
                mediaPlayer.setPlaybackParams(new PlaybackParams().setSpeed(speed));
                anInt = (int) (anIntTemp / speed);
                MediaStart();
            } else {
                speedText = MEDIA_CHECKEDSPEED_1_0;
                Toast.makeText(context, "无效播放地址", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 初始化 动画
     */
    private void initAnimation() {
        myAnimator = ObjectAnimator.ofFloat(my_music_image, "rotation", 0f, 360f);
        myAnimator.setDuration(25000);
        myAnimator.setRepeatCount(-1);
        myAnimator.setInterpolator(new LinearInterpolator());
        myAnimator.start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myAnimator.pause();
        } else {
            myAnimator.cancel();
        }
    }

    /**
     * 开始动画
     */
    private void startMyAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myAnimator.resume();
        } else {
            myAnimator.start();
        }
    }

    /**
     * 停止动画
     */
    private void stopAnimation() {
        if (myAnimator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                myAnimator.pause();
            } else {
                myAnimator.cancel();
            }
        }
    }
}
