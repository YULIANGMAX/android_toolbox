
package com.max.toolbox.services;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * @���� HeartbeatService
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class HeartbeatService extends Service implements Runnable {

    private static final String TAG = "Heartbeat";
    private static final String PACKAGE = "com.max.toolbox";

    private int count;
    private boolean appIsRuning = true;
    private static String url;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "service onStartCommand");
        if (intent.getExtras().getString("url") != null) {
            url = intent.getExtras().getString("url");
        }
        new Thread(this).start();
        count = 0;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (count > 1) {
                    Log.i(TAG, "����");
                    count = 1;
                    if (appIsRuning) {
                        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        List<RunningTaskInfo> list = am.getRunningTasks(3);
                        for (RunningTaskInfo info : list) {
                            if (info.topActivity.getPackageName().equals(PACKAGE)) {
                                // ֪ͨӦ�ã���ʾ��ʾ�����Ӳ�����������
                                Intent intent = new Intent(PACKAGE);
                                intent.putExtra("isonline", false);
                                sendBroadcast(intent);
                                break;
                            }
                        }
                        appIsRuning = false;
                    }
                } else if (!TextUtils.isEmpty(url)) {
                    sendHeartbeatPackage(url);
                    count++;
                }
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * �����������������
     * 
     * @param msg
     */
    private void sendHeartbeatPackage(String url) {
        HttpGet httpGet = new HttpGet(url);
        DefaultHttpClient httpClient = new DefaultHttpClient();

        HttpResponse httpResponse = null;
        try {
            // ��������
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (httpResponse == null) {
            return;
        }
        final int responseCode = httpResponse.getStatusLine().getStatusCode();
        if (responseCode == HttpStatus.SC_OK) {
            // ������
            count = 0;
            appIsRuning = true;
        } else {
            Log.i(TAG, "responseCode " + responseCode);
        }
    }

}
