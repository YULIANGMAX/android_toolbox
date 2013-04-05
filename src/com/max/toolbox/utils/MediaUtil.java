
package com.max.toolbox.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * @���� MediaUtil
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class MediaUtil {

    private MediaRecorder mRecorder;
    private File file;

    /**
     * ¼�����ܳ�ʼ���������հ�¼���ļ�
     * 
     * @param path ¼���ļ�·��
     * @return MediaRecorder����
     */
    public MediaRecorder recVoice(String path) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
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
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// ������˷�
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// ����ļ���ʽ
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);// ��Ƶ�ļ�����
        mRecorder.setAudioEncodingBitRate(8000);// ������
        mRecorder.setAudioSamplingRate(16000);// ������
        mRecorder.setOutputFile(path);// ����ļ�·��
        return mRecorder;
    }

    /**
     * ��ʼ¼��
     */
    public void startRec() {
        if (mRecorder != null) {
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ֹͣ¼����¼����д���ļ��ɹ������ļ�·�������ɹ�����null��ɾ���ļ�
     * @return ¼���ļ�·��
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

}
