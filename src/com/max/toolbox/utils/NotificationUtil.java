
package com.max.toolbox.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @类名 NotificationUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-5
 * @版本 1.0
 */
public class NotificationUtil {

    /**
     * 添加状态栏通知
     * 
     * @param mContext 上下文
     * @param icon 图标资源id，同时用作通知对象的唯一ID
     * @param texts 文字数组：[0]状态栏通知标题，[1]通知栏展开后标题，[2]通知栏展开后内容
     * @param action 点击通知栏图标发出Intent的action触发相应的Activity
     * @return
     */
    public NotificationManager addNotificaction(Context mContext, int icon, String[] texts, String action) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建一个Notification
        Notification notification = new Notification();
        // 设置显示在手机最上边的状态栏的图标
        notification.icon = icon;
        // 当notification被放到状态栏上的时候，提示内容
        notification.tickerText = texts[0];

        /***
         * notification.contentIntent:一个PendingIntent对象，当用户点击了状态栏上的图标时，
         * 该Intent会被触发 notification.contentView:我们可以不在状态栏放图标而是放一个view
         * notification.deleteIntent 当当前notification被移除时执行的intent
         * notification.vibrate 当手机震动时，震动周期设置
         */
        // 添加声音提示
        notification.defaults = Notification.DEFAULT_SOUND;
        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式
        notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;

        // 两种方式添加音乐
        // notification.sound =
        // Uri.parse("file:///sdcard/notification/ringer.mp3");
        // notification.sound =
        // Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
        Intent intent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        // 点击状态栏的图标出现的提示信息设置
        notification.setLatestEventInfo(mContext, texts[1], texts[2], pendingIntent);
        manager.notify(icon, notification);
        return manager;
    }

}
