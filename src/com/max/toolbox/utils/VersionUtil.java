
package com.max.toolbox.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @���� VersionUtil
 * @���� YULIANGMAX
 * @���� 2013-4-6
 * @�汾 1.0
 */
public class VersionUtil {

    /**
     * �������
     */
    private static String package_name;

    public VersionUtil(String package_name) {
        VersionUtil.package_name = package_name;
    }

    /**
     * ��ȡ��ǰ����汾��
     * 
     * @param context
     * @return
     */
    public static int getCurrentVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(package_name, 0).versionCode;
        } catch (NameNotFoundException e) {
            return -1;
        }
    }

    /**
     * ��ȡ��ǰ����汾����
     * 
     * @param context
     * @return
     */
    public static String getCurrentVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(package_name, 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    /**
     * ���ݰ汾���жϵõ��İ汾���Ƿ�Ϊ�°汾
     * 
     * @param versionName �õ��İ汾��
     * @param currentVersionName ��ǰ�汾��
     * @return
     */
    public static boolean isNewVersion(String versionName, String currentVersionName) {
        return versionName.compareToIgnoreCase(currentVersionName) > 0;
    }

    /**
     * ���ݰ汾���жϵõ��İ汾���Ƿ�Ϊ�°汾
     * 
     * @param versionCode�õ��İ汾��
     * @param currentVersionCode ��ǰ�汾��
     * @return
     */
    public static boolean isNewVersion(int versionCode, int currentVersionCode) {
        return versionCode > currentVersionCode;
    }

}
