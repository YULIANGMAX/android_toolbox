
package com.max.toolbox.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @���� NotificationUtil
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class NotificationUtil {

    /**
     * ���״̬��֪ͨ
     * 
     * @param mContext ������
     * @param icon ͼ����Դid��ͬʱ����֪ͨ�����ΨһID
     * @param texts �������飺[0]״̬��֪ͨ���⣬[1]֪ͨ��չ������⣬[2]֪ͨ��չ��������
     * @param action ���֪ͨ��ͼ�귢��Intent��action������Ӧ��Activity
     * @return
     */
    public NotificationManager addNotificaction(Context mContext, int icon, String[] texts, String action) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // ����һ��Notification
        Notification notification = new Notification();
        // ������ʾ���ֻ����ϱߵ�״̬����ͼ��
        notification.icon = icon;
        // ��notification���ŵ�״̬���ϵ�ʱ����ʾ����
        notification.tickerText = texts[0];

        /***
         * notification.contentIntent:һ��PendingIntent���󣬵��û������״̬���ϵ�ͼ��ʱ��
         * ��Intent�ᱻ���� notification.contentView:���ǿ��Բ���״̬����ͼ����Ƿ�һ��view
         * notification.deleteIntent ����ǰnotification���Ƴ�ʱִ�е�intent
         * notification.vibrate ���ֻ���ʱ������������
         */
        // ���������ʾ
        notification.defaults = Notification.DEFAULT_SOUND;
        // audioStreamType��ֵ����AudioManager�е�ֵ�������������ģʽ
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        // ���ַ�ʽ�������
        // notification.sound =
        // Uri.parse("file:///sdcard/notification/ringer.mp3");
        // notification.sound =
        // Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        // ���״̬����ͼ����ֵ���ʾ��Ϣ����
        notification.setLatestEventInfo(mContext, texts[1], texts[2], pendingIntent);
        manager.notify(icon, notification);
        return manager;
    }

}
