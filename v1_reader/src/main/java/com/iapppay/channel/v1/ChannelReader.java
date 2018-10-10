package com.iapppay.channel.v1;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by Administrator on 2018/10/10.
 */

public class ChannelReader {
    private ChannelReader() {
    }


    /**
     * 读取APK中的渠道信息
     *
     * @param apkFile apk文件
     * @return 返回null, 没有找到渠道信息
     * @throws IOException
     */
    public static String get(File apkFile) {
        String channel = null;
        try {
            ZipFile zipFile = new ZipFile(apkFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String channelFileName = "";
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                if (zipEntry.getName().startsWith(Config.COMPLETE_CHANNEL_FILE_INITIALLY)) {
                    channelFileName = zipEntry.getName();
                    break;
                }
            }
            if (channelFileName.contains(Config.COMPLETE_CHANNEL_FILE_INITIALLY)) {
                channel = channelFileName.substring(Config.COMPLETE_CHANNEL_FILE_INITIALLY.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("channel:" + channel);
        return channel;
    }
}
