package com.iapppay.channel.reader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.iapppay.channel.v2.ChannelInfo;
import com.iapppay.channel.v2.ChannelReader;

import java.io.File;

/**
 * Created by Administrator on 2018/10/10.
 */

public class AppChannelReader {

    private static final String TAG = AppChannelReader.class.getSimpleName();

    private AppChannelReader() {
    }


    /**
     * 读取渠道标识符
     *
     * @param context 上下文
     * @return 返回空，读取渠道失败
     */
    public static String get(Context context) {
        String channel = null;
        String apkPath = getApkPath(context);
        if (!TextUtils.isEmpty(apkPath)) {
            channel = getChannel(apkPath);
        }
        return channel;
    }

    private static String getApkPath(Context context) {
        if (context == null) {
            return null;
        }
        String apkPath = null;
        try {
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            apkPath = applicationInfo.sourceDir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apkPath;
    }

    public static String getChannel(String apkPath) {
        String channel = null;
        File file = new File(apkPath);
        //优先V2方式读取

        ChannelInfo channelInfo = ChannelReader.get(file);
        if (channelInfo != null) {
            channel = channelInfo.getChannel();
        } else {
            //V2方式读取失败，尝试V1读取
            channel = com.iapppay.channel.v1.ChannelReader.get(file);
        }
        return channel;
    }
}
