package com.iapppay.channel.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/10/10.
 */

public class ChannelWriter {
    private ChannelWriter() {
    }

    /**
     * 往Apk文件添加一个渠道标识符
     * <p>
     * 按照如下代码想指定zip添加一个新的文件，会导致原zip文件的东西全部丢失，google出来说，
     * java的ZipOutputStream不能向原zip追加新文件。
     *
     * @param apkFile apk文件
     * @param channel 渠道标识符
     * @throws IOException
    @Deprecated
    private static void put(File apkFile, String channel) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(apkFile)));
        String channelFileName = Config.COMPLETE_CHANNEL_FILE_INITIALLY + channel;
        ZipEntry channelZipEntry = new ZipEntry(channelFileName);
        zos.putNextEntry(channelZipEntry);
        zos.closeEntry();
        zos.close();
    }*/


    /**
     * 往Apk文件添加一个渠道标识符
     *
     * @param apkFile   apk文件
     * @param channel   渠道标识符
     * @throws IOException
     */
    public static void put(File apkFile,File toApkFile, String channel) throws IOException {
        ZipInputStream zis = new ZipInputStream(new FileInputStream(apkFile));
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(toApkFile));
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
        String channelFileName = Config.COMPLETE_CHANNEL_FILE_INITIALLY + channel;
        ZipEntry channelZipEntry = new ZipEntry(channelFileName);
        zos.putNextEntry(channelZipEntry);
        zos.closeEntry();
        zos.close();
    }
}
