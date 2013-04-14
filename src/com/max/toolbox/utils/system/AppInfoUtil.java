
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
     * ͨ�� PackageInfo ��ȡ������Ϣ������ 
     * ������ȡ������packageInfo.packageName
     * icon��ȡ��ȡ������packageManager.getApplicationIcon(applicationInfo)
     * Ӧ�����ƻ�ȡ������packageManager.getApplicationLabel(applicationInfo)
     * ʹ��Ȩ�޻�ȡ������packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS).requestedPermissions
     *
     * ͨ�� ResolveInfo ��ȡ������Ϣ������
     * ������ȡ������resolve.activityInfo.packageName
     * icon��ȡ��ȡ������resolve.loadIcon(packageManager)
     * Ӧ�����ƻ�ȡ������resolve.loadLabel(packageManager).toString()
     */
    
    /**
     * ��ѯ�ֻ�������Ӧ�ð���ϵͳӦ��
     * 
     * @param context
     */
    public static List<PackageInfo> getAllApps(Context context) {
        PackageManager pManager = context.getPackageManager();
        // ��ȡ�ֻ�������Ӧ��
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        return paklist;
    }

    /**
     * ��ѯ�ֻ��ڷ�ϵͳӦ��
     * 
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllAppsNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        // ��ȡ�ֻ�������Ӧ��
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            // �ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ�ó���
            if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
    }

    /**
     * ��ѯ�ֻ�������֧�ַ����Ӧ��
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
