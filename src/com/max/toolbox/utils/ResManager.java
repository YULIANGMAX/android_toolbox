
package com.max.toolbox.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @类名 ResManager
 * @作者 YULIANGMAX
 * @日期 2013-4-6
 * @版本 1.0
 */
public class ResManager {
    
    /**
     * 图片格式
     */
    private static final String IMAGE_FILE_FORMAT = ".png";
    /**
     * 文本文件格式
     */
    private static final String TEXT_FILE_FORMAT = ".properties";

    /**
     * 图片存放路径
     */
    public final static String IMAGES_DIR = "images/";
    /**
     * 文本文件存放路径
     */
    public final static String TEXTS_DIR = "texts/";
    /**
     * 文件路径
     */
    private static String filePath = "";

    /**
     * 加载图片资源（路径为assets/images/**.png）
     * 
     * @param fileName 图片资源路径
     */
    public static Bitmap loadImageRes(Activity activity, int screenWidth, String fileName) {
        Bitmap bitmap = null;
        InputStream is = null;
        FileInputStream fis = null;
        filePath = IMAGES_DIR;
        try {
            is = activity.getAssets().open(filePath + fileName + IMAGE_FILE_FORMAT);
            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                is = null;
                fis = null;
            }
        }
        return bitmap;
    }

    /**
     * 加载文本资源（路径是：assets/texts/**.properties）
     * 
     * @param fileName
     */
    public static ArrayList<String> loadTextRes(String fileName, Context context) {
        filePath = TEXTS_DIR;
        return loadProperties(filePath + fileName + TEXT_FILE_FORMAT, context);
    }

    /**
     * 读取配置文件读取配置信息
     * 
     * @param filename 配置文件路径
     * @return 包含配置信息的hashmap键值对
     */
    private static ArrayList<String> loadProperties(String filename, Context context) {
        ArrayList<String> properties = new ArrayList<String>();
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader rin = null;

        // 将配置文件放到res/raw/目录下，可以通过以下的方法获取
        // is = context.getResources().openRawResource(R.raw.system);

        // 这是读取配置文件的第二种方法
        // 将配置文件放到assets目录下，可以通过以下的方法获取
        // is = context.getAssets().open("system.properties");

        // 用来提取键值对的临时字符串
        StringBuffer tempStr = new StringBuffer();

        // 用来存放读取的每个字符
        int ch = 0;

        // 用来保存读取的配置文件一行的信息
        String line = null;
        try {
            is = context.getAssets().open(filename);
            rin = new InputStreamReader(is, "UTF-8");

            while (ch != -1) {
                tempStr.delete(0, tempStr.length());
                while ((ch = rin.read()) != -1) {
                    if (ch != '\n') {
                        tempStr.append((char) ch);
                    } else {
                        break;
                    }
                }
                line = tempStr.toString().trim();
                // 判断读出的那行数据是否有效,#开头的代表注释,如果是注释行那么跳过下面,继续上面操作
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                properties.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fis != null) {
                    fis.close();
                }
                if (null != rin) {
                    rin.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                is = null;
                fis = null;
                rin = null;
            }
        }
        return properties;
    }
    
}
