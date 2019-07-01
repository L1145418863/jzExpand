package com.lzg.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadsetReceiver extends BroadcastReceiver {
    private OnHeadsetDetectLintener onHeadsetDetectLintener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            if (intent.hasExtra("state")) {
                int state = intent.getIntExtra("state", 0);
                if (state == 1) {
                    //插入耳机
                    onHeadsetDetectLintener.isHeadseDetectConnected(true);
                } else if (state == 0) {
                    //拔出耳机
                    onHeadsetDetectLintener.isHeadseDetectConnected(false);
                }
            }
        }
    }

    public interface OnHeadsetDetectLintener {
        void isHeadseDetectConnected(boolean connected);
    }

    public void setOnHeadsetDetectLintener(OnHeadsetDetectLintener onHeadsetDetectLintener) {
        this.onHeadsetDetectLintener = onHeadsetDetectLintener;
    }
}
