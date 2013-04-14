
package com.max.toolbox.utils.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @类名 NetworkUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-5
 * @版本 1.0
 */
public class NetworkUtil {

    /**
     * 网络是否可用
     * 
     * @param context 上下文
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

}
