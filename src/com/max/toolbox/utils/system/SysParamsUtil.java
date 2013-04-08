
package com.max.toolbox.utils.system;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * @���� SysParamsUtil
 * @���� YULIANGMAX
 * @���� 2013-4-6
 * @�汾 1.0
 */
public class SysParamsUtil {

    private static SysParamsUtil params;
    public int screenWidth;// ��Ļ��ȣ���λΪpx
    public int screenHeight;// ��Ļ�߶ȣ���λΪpx
    public int densityDpi;// ��Ļ�ܶȣ���λΪdpi
    public float scale;// ����ϵ����ֵΪ densityDpi/160
    public float fontScale;// ��������ϵ����ͬscale

    public final static int SCREEN_ORIENTATION_VERTICAL = 1; // ��Ļ״̬������
    public final static int SCREEN_ORIENTATION_HORIZONTAL = 2; // ��Ļ״̬������
    public int screenOrientation = SCREEN_ORIENTATION_VERTICAL;// ��ǰ��Ļ״̬��Ĭ��Ϊ����

    /**
     * ˽�й��췽��
     * 
     * @param activity
     */
    private SysParamsUtil(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        densityDpi = dm.densityDpi;
        scale = dm.density;
        fontScale = dm.scaledDensity;
        screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL : SCREEN_ORIENTATION_HORIZONTAL;
    }

    /**
     * ��ȡʵ��
     * 
     * @param activity
     * @return
     */
    public static SysParamsUtil getInstance(Activity activity) {
        if (params == null) {
            params = new SysParamsUtil(activity);
        }
        return params;
    }

    /**
     * ��ȡһ����ʵ��
     * 
     * @param activity
     * @return
     */
    public static SysParamsUtil getNewInstance(Activity activity) {
        if (params != null) {
            params = null;
        }
        return getInstance(activity);
    }

    /**
     * ������Ϣ
     */
    @Override
    public String toString() {
        return "SystemParams:" +
        		"[" +
        		    "screenWidth: " + screenWidth + 
        		    " screenHeight: " + screenHeight + 
        		    " scale: " + scale + 
        		    " fontScale: " + fontScale + 
        		    " densityDpi: " + densityDpi + 
        		    " screenOrientation: " + (screenOrientation == SCREEN_ORIENTATION_VERTICAL ? "vertical" : "horizontal") + 
        		"]";
    }
    
}
