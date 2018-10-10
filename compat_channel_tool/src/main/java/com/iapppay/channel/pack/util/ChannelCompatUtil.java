package com.iapppay.channel.pack.util;

import com.iapppay.channel.v2.ChannelInfo;
import com.iapppay.channel.v2.ChannelReader;
import com.iapppay.channel.v2.ChannelWriter;
import com.iapppay.channel.v2.SignatureNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 兼容V1,V2读写渠道工具
 */

public class ChannelCompatUtil {
    private ChannelCompatUtil() {
    }

    /**
     * 向apk文件添加渠道标识符
     *
     * @param apkFile apk文件
     * @param channel 渠道标识符
     * @return true 成功 false 失败
     */
    public static boolean put(File apkFile, File toFile, String channel) throws IOException {
        copyFile(apkFile, toFile);
        //优先向V2方式输出渠道标识符
        boolean isSuccess = false;
        try {
            System.out.println("尝试V2方式添加渠道标识符");
            ChannelWriter.put(toFile, channel);
            System.out.println("V2方式『成功』");
            isSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (SignatureNotFoundException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        //V2方式输出失败，尝试V1方式输出渠道标识符
        if (!isSuccess) {
            //先删除V2复制的Apk文件
            toFile.delete();
            System.out.println("V2方式『失败』");
            try {
                System.out.println("尝试V1方式添加渠道标识符");
                com.iapppay.channel.v1.ChannelWriter.put(apkFile, toFile, channel);
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
            try {
                channel = com.iapppay.channel.v1.ChannelReader.get(apkFile);
            } catch (IOException e) {
                e.printStackTrace();
                channel = "";
            }
        }
        return channel;
    }

    /**
     * 拷贝zip文件
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @throws IOException
     */
    private static void copyFile(File fromFile, File toFile) throws IOException {
        FileInputStream is = new FileInputStream(fromFile);
        FileOutputStream os = new FileOutputStream(toFile);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        os.flush();
        os.close();
        is.close();
    }
}
