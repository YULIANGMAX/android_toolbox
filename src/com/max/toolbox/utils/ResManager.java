
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
 * @���� ResManager
 * @���� YULIANGMAX
 * @���� 2013-4-6
 * @�汾 1.0
 */
public class ResManager {
    
    /**
     * ͼƬ��ʽ
     */
    private static final String IMAGE_FILE_FORMAT = ".png";
    /**
     * �ı��ļ���ʽ
     */
    private static final String TEXT_FILE_FORMAT = ".properties";

    /**
     * ͼƬ���·��
     */
    public final static String IMAGES_DIR = "images/";
    /**
     * �ı��ļ����·��
     */
    public final static String TEXTS_DIR = "texts/";
    /**
     * �ļ�·��
     */
    private static String filePath = "";

    /**
     * ����ͼƬ��Դ��·��Ϊassets/images/**.png��
     * 
     * @param fileName ͼƬ��Դ·��
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
     * �����ı���Դ��·���ǣ�assets/texts/**.properties��
     * 
     * @param fileName
     */
    public static ArrayList<String> loadTextRes(String fileName, Context context) {
        filePath = TEXTS_DIR;
        return loadProperties(filePath + fileName + TEXT_FILE_FORMAT, context);
    }

    /**
     * ��ȡ�����ļ���ȡ������Ϣ
     * 
     * @param filename �����ļ�·��
     * @return ����������Ϣ��hashmap��ֵ��
     */
    private static ArrayList<String> loadProperties(String filename, Context context) {
        ArrayList<String> properties = new ArrayList<String>();
        InputStream is = null;
        FileInputStream fis = null;
        InputStreamReader rin = null;

        // �������ļ��ŵ�res/raw/Ŀ¼�£�����ͨ�����µķ�����ȡ
        // is = context.getResources().openRawResource(R.raw.system);

        // ���Ƕ�ȡ�����ļ��ĵڶ��ַ���
        // �������ļ��ŵ�assetsĿ¼�£�����ͨ�����µķ�����ȡ
        // is = context.getAssets().open("system.properties");

        // ������ȡ��ֵ�Ե���ʱ�ַ���
        StringBuffer tempStr = new StringBuffer();

        // ������Ŷ�ȡ��ÿ���ַ�
        int ch = 0;

        // ���������ȡ�������ļ�һ�е���Ϣ
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
                // �ж϶��������������Ƿ���Ч,#��ͷ�Ĵ���ע��,�����ע������ô��������,�����������
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
