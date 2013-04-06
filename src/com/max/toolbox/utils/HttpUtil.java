
package com.max.toolbox.utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @���� HttpUtil
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class HttpUtil {

    /**
     * POST����JSON���ݵ�URL
     * 
     * @param url Ŀ��url
     * @param entity json�ִ�
     * @return ����������
     */
    public String doPost(String url, String entity) {
        // �½�post��������ͷ����
        HttpPost httpRequest = new HttpPost(url);
        // httpRequest.addHeader("Authorization", mToken);
        // httpRequest.addHeader("Content-Type", "application/json");
        // httpRequest.addHeader("charset", HTTP.UTF_8);

        String strResult = null;
        try {
            // ����Request����ʵ��
            httpRequest.setEntity(new StringEntity(entity, HTTP.UTF_8));
            // ִ��post��ȡ��Response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // ȡ�÷������������ݣ�ֱ�Ӷ�ȡ��ʽ
                strResult = EntityUtils.toString(httpResponse.getEntity());

                // ȡ�÷������������ݣ����������ж�ȡ
//                StringBuilder builder = new StringBuilder(); 
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())); 
//                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) { 
//                    builder.append(s); 
//                } 
//                strResult = builder.toString();
            } else {
                // ���ش�����Ϣ
                Log.e("HTTP_ERROR", httpResponse.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }

    /**
     * POST�����ļ���URL
     * 
     * @param url Ŀ��url
     * @param file �ļ�
     * @return ����������
     */
    public String doPost(String url, File file) {
        // �½�post��������ͷ����
        HttpPost httpRequest = new HttpPost(url);
        String strResult = null;
        try {
            // ����Request����ʵ��
            httpRequest.setEntity(new FileEntity(file, "binary/octet-stream"));
            // ִ��post��ȡ��Response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // ȡ�÷�������������
                strResult = EntityUtils.toString(httpResponse.getEntity());
            } else {
                // ���ش�����Ϣ
                Log.e("HTTP_ERROR", httpResponse.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }

}
