
package com.max.toolbox.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @���� FileUtil
 * @���� YULIANGMAX
 * @���� 2013-4-5
 * @�汾 1.0
 */
public class FileUtil {

    /**
     * ���������浽�ļ�
     * 
     * @param source ��������Դ
     * @param targetPath Ŀ���ļ�·��
     * @return �ļ�·��
     */
    public String stream2file(InputStream source, String targetPath) {
        File target = new File(targetPath);
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            if (!target.exists()) {
                target.createNewFile();
            }
            inBuff = new BufferedInputStream(source);
            outBuff = new BufferedOutputStream(new FileOutputStream(target));
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (target.length() > 0) {
            return target.getAbsolutePath();
        } else {
            target.delete();
            return null;
        }
    }

}