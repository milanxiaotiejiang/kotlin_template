package com.seabreeze.robot.data.utils.audio;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;

import androidx.annotation.NonNull;

import com.elvishew.xlog.XLog;
import com.seabreeze.robot.base.ext.foundation.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: milan
 * Time: 2020/7/28 1:29
 * Des:
 */
public class AudioTracker {
    // 采样率 44100Hz，所有设备都支持 ,这里暂时用 16000
    private final static int SAMPLE_RATE = 16000;
    // 单声道，所有设备都支持
    private final static int CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
    // 位深 16 位，所有设备都支持
    private final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private int mBufferSizeInBytes = 0;
    // 播放对象
    private AudioTrack mAudioTrack;
    // 状态
    private volatile Status mStatus = Status.STATUS_NO_READY;
    // 单任务线程池
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    private static class AudioTrackerHolder {
        private static final AudioTracker INSTANCE = new AudioTracker();
    }

    public static AudioTracker getInstance() {
        return AudioTrackerHolder.INSTANCE;
    }

    private AudioTracker() {
    }

    public void createAudioTrack() throws IllegalStateException {
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT);
        if (mBufferSizeInBytes <= 0) {
            throw new IllegalStateException("AudioTrack is not available " + mBufferSizeInBytes);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AUDIO_FORMAT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(CHANNEL)
                            .build())
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(mBufferSizeInBytes)
                    .build();
        } else {
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT,
                    mBufferSizeInBytes, AudioTrack.MODE_STREAM);
        }
        mStatus = Status.STATUS_READY;
    }

    /**
     * 开始播放
     */
    public void start(File file, OnAudioTrackerListener listener) throws IllegalStateException {
        if (mStatus == Status.STATUS_NO_READY || mAudioTrack == null) {
            throw new IllegalStateException("播放器尚未初始化");
        }
        if (mStatus == Status.STATUS_START) {
            throw new IllegalStateException("正在播放...");
        }
        mExecutorService.execute(() -> {
            try {
                playAudioData(file, listener);
            } catch (IOException e) {
                XLog.e("播放出错");
                listener.onError(e);
            }
        });
        mStatus = Status.STATUS_START;
    }

    private void playAudioData(File file, OnAudioTrackerListener listener) throws IOException {
        FileInputStream stream = null;
        try {
            Platform.Companion.get().defaultCallbackExecutor().execute(() -> listener.onStart(file));

            stream = new FileInputStream(file);
            XLog.e("播放开始");
            byte[] bytes = new byte[mBufferSizeInBytes];
            int readCount;
            while (stream.available() > 0) {
                readCount = stream.read(bytes);
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (readCount != 0 && readCount != -1) {
                    mAudioTrack.play();
                    mAudioTrack.write(bytes, 0, readCount);
                }
            }
            mStatus = Status.STATUS_STOP;
            Platform.Companion.get().defaultCallbackExecutor().execute(() -> listener.onEnd());
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public void stop() throws IllegalStateException {
        if (mStatus != Status.STATUS_NO_READY && mStatus != Status.STATUS_READY) {
            mStatus = Status.STATUS_STOP;
            mAudioTrack.stop();
//            release();
        }
    }

    public void release() {
        mStatus = Status.STATUS_NO_READY;
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    public int getBufferSizeInBytes() {
        return mBufferSizeInBytes;
    }

    /**
     * 播放对象的状态
     */
    public enum Status {
        //未开始
        STATUS_NO_READY,
        //预备
        STATUS_READY,
        //播放
        STATUS_START,
        //停止
        STATUS_STOP
    }

    public interface OnAudioTrackerListener {

        default void onStart(@NonNull File file) {
        }

        void onEnd();

        default void onError(@NonNull IOException e) {
        }
    }
}
