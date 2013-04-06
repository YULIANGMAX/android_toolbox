
package com.max.toolbox.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @���� TimeUtil
 * @���� YULIANGMAX
 * @���� 2013-4-6
 * @�汾 1.0
 */
public class TimeUtil {

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "hh:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd hh:mm";
    public final static String FORMAT_MONTH_DAY_TIME = "MM��dd�� hh:mm";
    private static SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_TIME, Locale.CHINA);
    private static final int YEAR = 365 * 24 * 60 * 60;// ��
    private static final int MONTH = 30 * 24 * 60 * 60;// ��
    private static final int DAY = 24 * 60 * 60;// ��
    private static final int HOUR = 60 * 60;// Сʱ
    private static final int MINUTE = 60;// ����
    
    /**
     * �Զ����ʽ��ȡʱ���ַ���
     * @param format ��ʽ
     * @return
     */
    public static String getDate(String format) {
        SimpleDateFormat formatBuilder = new SimpleDateFormat(format, Locale.CHINA);
        return formatBuilder.format(new Date());
    }

    /**
     * ��ȡhh:mm:ss��ʽʱ���ַ���
     * @return
     */
    public static String getDate() {
        return getDate("hh:mm:ss");
    }

    /**
     * ����ʱ�����ȡ������ʱ�䣬��3����ǰ��1��ǰ
     * 
     * @param timestamp ʱ��� ��λΪ����
     * @return ʱ���ַ���
     */
    public static String getDescriptionTimeFromTimestamp(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// ������ʱ���������
        String timeStr = null;
        if (timeGap > YEAR) {
            timeStr = timeGap / YEAR + "��ǰ";
        } else if (timeGap > MONTH) {
            timeStr = timeGap / MONTH + "����ǰ";
        } else if (timeGap > DAY) {// 1������
            timeStr = timeGap / DAY + "��ǰ";
        } else if (timeGap > HOUR) {// 1Сʱ-24Сʱ
            timeStr = timeGap / HOUR + "Сʱǰ";
        } else if (timeGap > MINUTE) {// 1����-59����
            timeStr = timeGap / MINUTE + "����ǰ";
        } else {// 1����-59����
            timeStr = "�ո�";
        }
        return timeStr;
    }

    /**
     * ����ʱ�����ȡָ����ʽ��ʱ�䣬��2011-11-30 08:40
     * 
     * @param timestamp ʱ��� ��λΪ����
     * @param format ָ����ʽ ���Ϊnull��մ���ʹ��Ĭ�ϸ�ʽ"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int year = Integer.valueOf(sdf.format(new Date(timestamp)).substring(0, 4));
            System.out.println("currentYear: " + currentYear);
            System.out.println("year: " + year);
            if (currentYear == year) {// ���Ϊ��������ʾ���
                sdf.applyPattern(FORMAT_MONTH_DAY_TIME);
            } else {
                sdf.applyPattern(FORMAT_DATE_TIME);
            }
        } else {
            sdf.applyPattern(format);
        }
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * ����ʱ�����ȡʱ���ַ�����������ָ����ʱ��ָ���partionSeconds���Զ��жϷ���������ʱ�仹��ָ����ʽ��ʱ��
     * 
     * @param timestamp ʱ��� ��λ�Ǻ���
     * @param partionSeconds ʱ��ָ��ߣ�������ʱ����ָ����ʱ������������������ָ���ʱ�򷵻�ָ����ʽʱ�䣬���򷵻�������ʱ��
     * @param format
     * @return
     */
    public static String getMixTimeFromTimestamp(long timestamp, long partionSeconds, String format) {
        long currentTime = System.currentTimeMillis();
        long timeGap = (currentTime - timestamp) / 1000;// ������ʱ���������
        if (timeGap <= partionSeconds) {
            return getDescriptionTimeFromTimestamp(timestamp);
        } else {
            return getFormatTimeFromTimestamp(timestamp, format);
        }
    }

    /**
     * ��ȡ��ǰ���ڵ�ָ����ʽ���ַ���
     * 
     * @param format ָ��������ʱ���ʽ����Ϊnull��""��ʹ��ָ���ĸ�ʽ"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    /**
     * �������ַ�����ָ����ʽת��ΪDate
     * 
     * @param time �����ַ���
     * @param format ָ�������ڸ�ʽ����Ϊnull��""��ʹ��ָ���ĸ�ʽ"yyyy-MM-dd HH:MM"
     * @return
     */
    public static Date getTimeFromString(String timeStr, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        try {
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * ��Date��ָ����ʽת��Ϊ����ʱ���ַ���
     * 
     * @param date ����
     * @param format ָ��������ʱ���ʽ����Ϊnull��""��ʹ��ָ���ĸ�ʽ"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getStringFromTime(Date time, String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_DATE_TIME);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(time);
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        String timeStr = "2010-11-30 10:12:23";
        try {
            Date date = sdf.parse(timeStr);
            System.out.println(TimeUtil.getDescriptionTimeFromTimestamp(date.getTime()));
            System.out.println(TimeUtil.getDescriptionTimeFromTimestamp(new Date().getTime()));
            System.out.println(TimeUtil.getFormatTimeFromTimestamp(date.getTime(), "yyyy��MM��dd��"));
            System.out.println(TimeUtil.getFormatTimeFromTimestamp(date.getTime(), null));
            System.out.println(TimeUtil.getFormatTimeFromTimestamp(new Date().getTime(), null));
            System.out.println(TimeUtil.getMixTimeFromTimestamp(date.getTime(), 3 * 24 * 60 * 60, "yyyy��MM��dd�� hh:mm"));
            System.out.println(TimeUtil.getMixTimeFromTimestamp(date.getTime(), 24 * 60 * 60, null));
            System.out.println(TimeUtil.getMixTimeFromTimestamp(new Date().getTime(), 3 * 24 * 60 * 60, "yyyy��MM��dd�� hh:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
