
package com.max.toolbox.utils.system;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class AppInfoUtil {
    
    /*
     * 通过 PackageInfo 获取具体信息方法： 
     * 包名获取方法：packageInfo.packageName
     * icon获取获取方法：packageManager.getApplicationIcon(applicationInfo)
     * 应用名称获取方法：packageManager.getApplicationLabel(applicationInfo)
     * 使用权限获取方法：packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS).requestedPermissions
     *
     * 通过 ResolveInfo 获取具体信息方法：
     * 包名获取方法：resolve.activityInfo.packageName
     * icon获取获取方法：resolve.loadIcon(packageManager)
     * 应用名称获取方法：resolve.loadLabel(packageManager).toString()
     */
    
    /**
     * 查询手机内所有应用包括系统应用
     * 
     * @param context
     */
    public static List<PackageInfo> getAllApps(Context context) {
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        return paklist;
    }

    /**
     * 查询手机内非系统应用
     * 
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllAppsNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            // 判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * 查询手机内所有支持分享的应用
     * 
     * @param context
     * @return
     */
    public static List<ResolveInfo> getShareApps(Context context) {
        List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);

        return mApps;
    }

}
