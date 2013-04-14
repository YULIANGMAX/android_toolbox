
package com.max.toolbox.utils;

import android.content.Context;
import android.content.Intent;

public class ShareUtil {

    /**
     * ����
     * 
     * @param context ������
     * @param subject ��������
     * @param extratext ��������
     * @param title Ŀ��Ӧ��ѡ��Ի������
     */
    public void Share(Context context, String subject, String extratext, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND); // ���������͵�����
        intent.setType("text/plain"); // �����͵���������
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // ���������
        intent.putExtra(Intent.EXTRA_TEXT, extratext); // ���������
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// ������
        context.startActivity(Intent.createChooser(intent, title));// Ŀ��Ӧ��ѡ��Ի���ı���
    }

}
