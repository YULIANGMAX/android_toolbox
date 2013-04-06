
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
 * @类名 HttpUtil
 * @作者 YULIANGMAX
 * @日期 2013-4-5
 * @版本 1.0
 */
public class HttpUtil {

    /**
     * POST发送JSON数据到URL
     * 
     * @param url 目标url
     * @param entity json字串
     * @return 服务器返回
     */
    public String doPost(String url, String entity) {
        // 新建post对象，设置头参数
        HttpPost httpRequest = new HttpPost(url);
        // httpRequest.addHeader("Authorization", mToken);
        // httpRequest.addHeader("Content-Type", "application/json");
        // httpRequest.addHeader("charset", HTTP.UTF_8);

        String strResult = null;
        try {
            // 设置Request内容实体
            httpRequest.setEntity(new StringEntity(entity, HTTP.UTF_8));
            // 执行post，取得Response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得服务器返回数据，直接读取方式
                strResult = EntityUtils.toString(httpResponse.getEntity());

                // 取得服务器返回数据，缓冲流单行读取
//                StringBuilder builder = new StringBuilder(); 
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent())); 
//                for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) { 
//                    builder.append(s); 
//                } 
//                strResult = builder.toString();
            } else {
                // 返回错误信息
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
     * POST发送文件到URL
     * 
     * @param url 目标url
     * @param file 文件
     * @return 服务器返回
     */
    public String doPost(String url, File file) {
        // 新建post对象，设置头参数
        HttpPost httpRequest = new HttpPost(url);
        String strResult = null;
        try {
            // 设置Request内容实体
            httpRequest.setEntity(new FileEntity(file, "binary/octet-stream"));
            // 执行post，取得Response
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得服务器返回数据
                strResult = EntityUtils.toString(httpResponse.getEntity());
            } else {
                // 返回错误信息
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
