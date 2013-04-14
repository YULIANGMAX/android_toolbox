
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
     * ����ָ������
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
     * ������
     * 
     * @param activity Activityʵ��
     * @param milliseconds ��ʱ��
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * ������
     * 
     * @param activity Activityʵ��
     * @param pattern �Զ�����ģʽ �����κ���[��ֹʱ������ʱ������ֹʱ������ʱ������]
     * @param isRepeat �Ƿ�ѭ��
     */
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * ��Ļ��ͼ
     * 
     * @param activity
     * @return
     */
    public static Bitmap shot(Activity activity) {
        View views = activity.getWindow().getDecorView();
        views.buildDrawingCache();

        // ��ȡ״̬���߶�
        Rect frames = new Rect();
        views.getWindowVisibleDisplayFrame(frames);
        int statusBarHeights = frames.top;
        Display display = activity.getWindowManager().getDefaultDisplay();
        int widths = display.getWidth();
        int heights = display.getHeight();
        // ��һ�ַ�ʽ
        views.layout(0, statusBarHeights, widths, heights - statusBarHeights);
        views.setDrawingCacheEnabled(true);// ����ǰ���ڱ��滺����Ϣ �����ַ�ʽ����Ҫ����
        Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache());
        // �ڶ��ַ�ʽ
        // 1��source λͼ 2��X x����ĵ�һ������ 3��Y y����ĵ�һ������ 4����ȵ�������ÿһ�� 5���߶ȵ�����
        // Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache(), 0,
        // statusBarHeights,widths, heights - statusBarHeights);
        return bmp;
    }

}
