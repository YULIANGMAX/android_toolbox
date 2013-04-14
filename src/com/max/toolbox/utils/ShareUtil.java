
package com.max.toolbox.utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtil {

    /**
     * 分享
     * 
     * @param context 上下文
     * @param subject 分享主题
     * @param extratext 分享内容
     * @param title 目标应用选择对话框标题
     */
    public void Share(Context context, String subject, String extratext, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
        intent.setType("text/plain"); // 分享发送的数据类型
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 分享的主题
        intent.putExtra(Intent.EXTRA_TEXT, extratext); // 分享的内容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 新任务
        context.startActivity(Intent.createChooser(intent, title));// 目标应用选择对话框的标题
    }

}
