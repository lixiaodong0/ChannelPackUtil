package com.iapppay.channel.pack.util;

import com.iapppay.channel.pack.data.DataSource;
import com.iapppay.channel.v2.ChannelInfo;
import com.iapppay.channel.v2.ChannelReader;
import com.iapppay.channel.v2.ChannelWriter;
import com.iapppay.channel.v2.SignatureNotFoundException;

import java.io.File;
import java.io.IOException;

/**
 * 兼容V1,V2读写渠道工具
 */

public class ChannelCompatUtil {
    private ChannelCompatUtil() {
    }


    /**
     * 检查apk是否属于V2签名
     *
     * @param apkFile APK文件
     */
    public static boolean isV2Signature(File apkFile) {
        boolean isV2 = false;
        try {
            ChannelWriter.put(apkFile, "");
            isV2 = true;
        } catch (IOException e) {
            e.printStackTrace();
            isV2 = false;
        } catch (SignatureNotFoundException e) {
            e.printStackTrace();
            isV2 = false;
        }
        return isV2;
    }

    /**
     * 向apk文件添加渠道标识符
     *
     * @param apkFile apk文件
     * @param channel 渠道标识符
     * @return true 成功 false 失败
     */
    public static boolean put(File apkFile, File toFile, String channel) throws IOException {
        boolean isSuccess = false;
        if (DataSource.getInstance().isV2Signature()) {
            FileUtil.copyFile(apkFile, toFile);
            try {
                //V2方式添加渠道标识符
                System.out.println("尝试V2方式添加渠道标识符");
                ChannelWriter.put(toFile, channel);
                isSuccess = true;
                System.out.println("V2方式『成功』");
            } catch (SignatureNotFoundException e) {
                e.printStackTrace();
                isSuccess = false;
                System.out.println("V2方式『失败』");
            }
        } else {
            //V1方式添加渠道标识符
            try {
                System.out.println("尝试V1方式添加渠道标识符");
                if (FileUtil.isJava7Version()) {
                    FileUtil.copyFile(apkFile, toFile);
                    com.iapppay.channel.v1.ChannelWriter.put(toFile, channel);
                } else {
                    com.iapppay.channel.v1.ChannelWriter.put(apkFile, toFile, channel);
                }
                isSuccess = true;
                System.out.println("V1方式『成功』");
            } catch (IOException e) {
                e.printStackTrace();
                isSuccess = false;
                System.out.println("V1方式『失败』");
            }
        }
        return isSuccess;
    }


    /**
     * 读取apk文件中的渠道标识符
     *
     * @param apkFile apk文件
     * @return 返回空，读取失败
     */
    public static String get(File apkFile) {
        //优先向V2方式读取
        String channel = "";
        ChannelInfo channelInfo = ChannelReader.get(apkFile);
        if (channelInfo != null) {
            channel = channelInfo.getChannel();
        } else {
            //V2方式失败，尝试V1方式读取
            channel = com.iapppay.channel.v1.ChannelReader.get(apkFile);
        }
        return channel;
    }
}
