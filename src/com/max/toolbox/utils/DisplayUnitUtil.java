
package com.max.toolbox.utils;

/**
 * @���� DisplayUnitUtil
 * @���� YULIANGMAX
 * @���� 2013-4-6
 * @�汾 1.0
 */
public class DisplayUnitUtil {

    /**
     * ��pxֵת��Ϊdip��dpֵ����֤�ߴ��С����
     * 
     * @param pxValue
     * @param scale��DisplayMetrics��������density��
     * @return
     */
    public static int px2dip(float pxValue, float scale) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * ��dip��dpֵת��Ϊpxֵ����֤�ߴ��С����
     * 
     * @param dipValue
     * @param scale��DisplayMetrics��������density��
     * @return
     */
    public static int dip2px(float dipValue, float scale) {
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * ��pxֵת��Ϊspֵ����֤���ִ�С����
     * 
     * @param pxValue
     * @param fontScale��DisplayMetrics��������scaledDensity��
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * ��spֵת��Ϊpxֵ����֤���ִ�С����
     * 
     * @param spValue
     * @param fontScale��DisplayMetrics��������scaledDensity��
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
        return (int) (spValue * fontScale + 0.5f);
    }
}
