
package com.max.toolbox.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * @类名 MediaUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-5
 * @版本 1.0
 */
public class MediaUtil {

    private MediaRecorder mRecorder;
    private File file;

    /**
     * 录音功能初始化，创建空白录音文件
     * 
     * @param path 录音文件路径
     * @return MediaRecorder对象
     */
    public MediaRecorder recVoice(String path) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            file = new File(path);
            if (!file.exists()) {
                String dir = path.substring(0, path.lastIndexOf("/"));
                new File(dir).mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            return null;
        }
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 输出文件格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// 音频文件编码
        mRecorder.setAudioEncodingBitRate(8000);// 比特率
        mRecorder.setAudioSamplingRate(16000);// 采样率
        mRecorder.setOutputFile(path);// 输出文件路径
        return mRecorder;
    }

    /**
     * 开始录音
     * 
     * @throws IOException
     * @throws
     */
    public void startRec() throws IOException {
        if (mRecorder != null) {
            mRecorder.prepare();
            mRecorder.start();
        }
    }

    /**
     * 停止录音。录音并写入文件成功返回文件路径，不成功返回null并删除文件
     * 
     * @return 录音文件路径
     */
    public String stopRec() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
        if (file == null || !file.exists()) {
            return null;
        } else if (file.length() <= 0) {
            file.delete();
            return null;
        } else {
            return file.getAbsolutePath();
        }
    }

    /**
     * 播放声音
     * @param path 音频文件路径
     * @throws IOException
     */
    public void playVoice(String path) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepare();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mediaPlayer.start();
    }

}
