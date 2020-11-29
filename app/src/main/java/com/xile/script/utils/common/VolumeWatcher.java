package com.xile.script.utils.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import com.xile.script.base.ScriptApplication;

/**
 * date: 2017/5/2 13:19
 *
 * @desc 音量控制
 */
public class VolumeWatcher extends BroadcastReceiver {
    private AudioManager mAudioManager;
    private int defaultVolume;
    public  boolean isRegisitered;

    private VolumeWatcher() {
        mAudioManager = (AudioManager) ScriptApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        defaultVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public void registerReceive(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        context.registerReceiver(this, filter);
        isRegisitered = true;
    }

    public void unRegisterReceive(Context context) {
        context.unregisterReceiver(this);
        isRegisitered = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        }
    }
}
