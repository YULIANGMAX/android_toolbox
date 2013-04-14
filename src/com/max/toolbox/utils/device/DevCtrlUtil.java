
package com.max.toolbox.utils.device;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;

public class DevCtrlUtil {

    /**
     * 呼叫指定号码
     * 
     * @param context
     * @param number
     */
    public static void callNum(Context context, String number) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    /**
     * 调用振动
     * 
     * @param activity Activity实例
     * @param milliseconds 振动时长
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 调用振动
     * 
     * @param activity Activity实例
     * @param pattern 自定义震动模式 。依次含义[静止时长，震动时长，静止时长，震动时长……]
     * @param isRepeat 是否循环
     */
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 屏幕截图
     * 
     * @param activity
     * @return
     */
    public static Bitmap shot(Activity activity) {
        View views = activity.getWindow().getDecorView();
        views.buildDrawingCache();

        // 获取状态栏高度
        Rect frames = new Rect();
        views.getWindowVisibleDisplayFrame(frames);
        int statusBarHeights = frames.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        int widths = display.getWidth();
        int heights = display.getHeight();
        // 第一种方式
        views.layout(0, statusBarHeights, widths, heights - statusBarHeights);
        views.setDrawingCacheEnabled(true);// 允许当前窗口保存缓存信息 ，两种方式都需要加上
        Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache());
        // 第二种方式
        // 1、source 位图 2、X x坐标的第一个像素 3、Y y坐标的第一个像素 4、宽度的像素在每一行 5、高度的行数
        // Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache(), 0,
        // statusBarHeights,widths, heights - statusBarHeights);
        return bmp;
    }

}
