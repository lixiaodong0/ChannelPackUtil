package com.iapppay.channel.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
     * 此方法必须大于 java 7 才可以调用
     * 往Apk文件添加一个渠道标识符
     *
     * @param apkFile apk文件
     * @param channel 渠道标识符
     * @throws IOException
     */
    public static void put(File apkFile, String channel) throws IOException {
        //构建文件系统
        try (FileSystem fs = FileSystems.newFileSystem(Paths.get(apkFile.getAbsolutePath()), null)) {
            //构建路径
            Path filePath = fs.getPath(Config.COMPLETE_CHANNEL_FILE_INITIALLY + channel);
            //输出文件
            try (Writer writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                writer.write("");
            }
        }
        System.out.println("java 7 mode put channel");
    }

    /**
     * 往Apk文件添加一个渠道标识符
     *
     * @param apkFile apk文件
     * @param channel 渠道标识符
     * @throws IOException
     */
    public static void put(File apkFile, File toApkFile, String channel) throws IOException {
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

        System.out.println("default mode put channel");
    }
}
