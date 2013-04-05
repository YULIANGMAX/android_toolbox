
package com.max.toolbox.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class ErrorLogHandler implements UncaughtExceptionHandler {

    public static final String TAG = "ErrorLogHandler";
    public static final String LOGPATH = Environment.getExternalStorageState().toString()+"/crash/";

    // ϵͳĬ�ϵ�UncaughtException������
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // ErrorLogHandlerʵ��
    private static ErrorLogHandler INSTANCE = new ErrorLogHandler();
    // �����Context����
    private Context mContext;
    // �����洢�豸��Ϣ���쳣��Ϣ
    private Map<String, String> infos = new HashMap<String, String>();
    // ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
    

    /** ��ֻ֤��һ��ErrorLogHandlerʵ�� */
    private ErrorLogHandler() {
    }

    /** ��ȡErrorLogHandlerʵ�� ,����ģʽ */
    public static ErrorLogHandler getInstance() {
        return INSTANCE;
    }

    /**
     * ��ʼ��
     * 
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // ��ȡϵͳĬ�ϵ�UncaughtException������
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // ���ø�ErrorLogHandlerΪ�����Ĭ�ϴ�����
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * ��UncaughtException����ʱ��ת��ú���������
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // ����û�û�д�������ϵͳĬ�ϵ��쳣������������
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "�߳��ж�", e);
            }
            // �˳�����
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
     * 
     * @param ex
     * @return true:��������˸��쳣��Ϣ;���򷵻�false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        saveErrorInfo2File(ex);
        return true;
    }

    /**
     * �ռ��豸������Ϣ
     * 
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "��ȡ����Ϣ�����쳣", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "��ȡ������Ϣ�����쳣", e);
            }
        }
    }

    /**
     * ���������Ϣ���ļ���
     * 
     * @param ex
     * @return �����ļ�����,���ڽ��ļ����͵�������
     */
    private String saveErrorInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "error-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(LOGPATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(LOGPATH + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "д���ļ������쳣", e);
        }
        return null;
    }

}
