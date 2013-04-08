
package com.max.toolbox.utils;

import android.app.Activity;
import android.graphics.Rect;

public class WindowSizeUtil {

    /**
     * 获取状态栏高度
     * 
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect frames = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frames);
        int statusBarHeights = frames.top;
        return statusBarHeights;
    }

    /**
     * 获得屏幕高宽
     * 
     * @param activity
     * @return
     */
    public static int[] getWindowHeight(Activity activity) {
        int heights = activity.getWindowManager().getDefaultDisplay().getHeight();
        int widths = activity.getWindowManager().getDefaultDisplay().getWidth();
        int[] window = {
                heights, widths
        };
        return window;
    }

}
