
package com.max.toolbox.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ImeUtil {

    /**
     * �л����뷨����״̬
     * 
     * @param context
     */
    public static void imeToggle(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * ��ʾ���뷨
     * 
     * @param context
     * @param view
     */
    public static void ShowIme(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * �������뷨
     * 
     * @param context
     * @param view
     */
    public static void HideIme(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // ǿ������
    }

    /**
     * ��ȡ��ǰActivity�����������뷨
     * 
     * @param activity
     * @param view
     */
    public static void sysIme(Activity activity, View view) {
        InputMethodManager imm = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * ��ȡ���뷨��ʾ״̬
     * 
     * @param context
     * @return
     */
    public static boolean isImeActive(Context context) {
        InputMethodManager imm = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        return imm.isActive();
    }

}
