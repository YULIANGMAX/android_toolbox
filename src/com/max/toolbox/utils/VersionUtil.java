
package com.max.toolbox.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * @类名 VersionUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-6
 * @版本 1.0
 */
public class VersionUtil {

    /**
     * 程序包名
     */
    private static String package_name;

    public VersionUtil(String package_name) {
        VersionUtil.package_name = package_name;
    }

    /**
     * 获取当前程序版本号
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
     * 获取当前程序版本名称
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
     * 根据版本名判断得到的版本号是否为新版本
     * 
     * @param versionName 得到的版本号
     * @param currentVersionName 当前版本号
     * @return
     */
    public static boolean isNewVersion(String versionName, String currentVersionName) {
        return versionName.compareToIgnoreCase(currentVersionName) > 0;
    }

    /**
     * 根据版本号判断得到的版本号是否为新版本
     * 
     * @param versionCode得到的版本号
     * @param currentVersionCode 当前版本号
     * @return
     */
    public static boolean isNewVersion(int versionCode, int currentVersionCode) {
        return versionCode > currentVersionCode;
    }

}
