package com.iapppay.channel.pack.v2.util;

import com.iapppay.channel.pack.v2.interfaces.ResultCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/9/30.
 */

public class ZipUtil {
    private static ZipUtil INSTANCE = null;

    private ZipUtil() {
    }

    public static final ZipUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ZipUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ZipUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @param fromFile    源文件
     * @param toFile      目标文件
     * @param channelName 渠道名称
     * @return
     */
    public void markZip(File fromFile, File toFile, String channelName, ResultCallback<File> resultCallback) {
        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fromFile));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(toFile));
            ZipEntry zipEntry = null;
            while ((zipEntry = zis.getNextEntry()) != null) {
                /**
                 *  这里需要重新计算压缩值，否则有时候会报错:
                 *   java.util.zip.ZipException invalid entry compressed size
                 */
                ZipEntry newZipEntry = new ZipEntry(zipEntry.getName());
                zos.putNextEntry(newZipEntry);

                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = zis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                /**
                 zos.finish();
                 加了这一行，用解压软件打开apk失败，暂时不知道啥原因，
                 * */
            }
            zis.close();
            zos.putNextEntry(new ZipEntry(channelName));
            zos.closeEntry();
            zos.close();
            resultCallback.onSuccess(toFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            resultCallback.onError("文件不存在!");
        } catch (IOException e) {
            e.printStackTrace();
            resultCallback.onError("IO异常!");
        }
    }

}
