package com.iapppay.channel.pack.v1;

import com.iapppay.channel.pack.v1.config.Constant;
import com.iapppay.channel.pack.v1.config.StringsConfig;
import com.iapppay.channel.pack.v1.data.DataSource;
import com.iapppay.channel.pack.v1.interfaces.ChannelCoding;
import com.iapppay.channel.pack.v1.interfaces.ResultCallback;
import com.iapppay.channel.pack.v1.util.TextUtil;
import com.iapppay.channel.pack.v1.util.ZipUtil;
import com.sun.istack.internal.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 渠道工具
 */

public class ChannelUtil implements ChannelCoding {


    private static ChannelUtil INSTANCE = null;

    public static ChannelUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ChannelUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChannelUtil();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String encode(String channelName) {
        return HexChannelCodeing.INSTANCE.encode(channelName);
    }

    @Override
    public String decode(String channelName) {
        return HexChannelCodeing.INSTANCE.decode(channelName);
    }

    /**
     * 渠道打包任务
     */
    public void packTask(@NotNull ResultCallback<List<File>> callback) {
        boolean moreChannel = DataSource.getInstance().isMoreChannel();
        String validity = checkParams();
        if (!TextUtil.isEmpty(validity)) {
            //校验数据失败，中断打包流程
            callback.onError(validity);
            return;
        }

        if (moreChannel) {
            moreChannel(callback);
        } else {
            singleChannel(callback);
        }
    }

    /**
     * 读取apk文件里的渠道号
     *
     * @param apkFile  apk文件
     * @param callback 回调
     */
    public void readChannel(@NotNull File apkFile, @NotNull ResultCallback<String> callback) {
        try {
            ZipFile zipFile = new ZipFile(apkFile);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String channelFileName = "";
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                //判断文件是不是渠道文件
                if (name.startsWith(Constant.META_INF + DataSource.getInstance().getChannelFlag())) {
                    channelFileName = name;
                    break;
                }
            }
            if (!TextUtil.isEmpty(channelFileName)) {
                //截取渠道号
                String channelStr = channelFileName.substring((Constant.META_INF + DataSource.getInstance().getChannelFlag()).length());
                //解码
                String decode = decode(channelStr);
                callback.onSuccess(StringsConfig.SUCCESS.CHANNEL_MARK + decode);
            } else {
                //渠道文件不存在，证明该APK没有打过渠道号
                callback.onError(StringsConfig.FAIL.NOT_FOUND_CHANNEL_MARK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查数据的合法性
     *
     * @return 返回空字符串检查通过, 反之失败
     */
    private String checkParams() {
        String failInfo = "";
        DataSource dataSource = DataSource.getInstance();
        File fromFile = dataSource.getFromFile();
        if (!(fromFile != null && fromFile.exists())) {
            return StringsConfig.FAIL.ZIP_FILE_NOT_NULL;
        }
        List<String> channelName = dataSource.getChannelNames();
        if (DataSource.getInstance().isMoreChannel()) {
            for (String str : channelName) {
                if (TextUtil.isEmpty(str)) {
                    return StringsConfig.FAIL.CHANNEL_MARK_NOT_NULL;
                }
            }
        } else {
            if (TextUtil.isEmpty(channelName.get(0))) {
                return StringsConfig.FAIL.CHANNEL_MARK_NOT_NULL;
            }
        }
        return failInfo;
    }


    /**
     * 单渠道
     *
     * @param callback
     */
    private void singleChannel(ResultCallback<List<File>> callback) {
        pack(DataSource.getInstance().getChannelNames().get(0), new ResultCallback<File>() {
            @Override
            public void onSuccess(File data) {
                //打包成功
                List<File> result = new ArrayList<>();
                result.add(data);
                callback.onSuccess(result);
            }

            @Override
            public void onError(String errorMsg) {
                //打包失败
                callback.onError(errorMsg);
            }
        });
    }

    /**
     * 多渠道
     *
     * @param callback
     */
    private void moreChannel(ResultCallback<List<File>> callback) {
        DataSource dataSource = DataSource.getInstance();
        List<String> channelNames = dataSource.getChannelNames();
        List<String> failChannelList = new ArrayList<>();
        List<File> successChannelList = new ArrayList<>();
        for (String name : channelNames) {
            pack(name, new ResultCallback<File>() {
                @Override
                public void onSuccess(File data) {
                    //打包成功
                    successChannelList.add(data);
                }

                @Override
                public void onError(String errorMsg) {
                    //记录失败渠道号信息
                    failChannelList.add(errorMsg);
                }
            });
        }
        if (failChannelList.isEmpty()) {
            callback.onSuccess(successChannelList);
        } else {
            callback.onError(StringsConfig.FAIL.MORE_CHANNEL_PACL_FAIL);
        }
    }


    private void pack(String channelName, ResultCallback<File> callback) {
        DataSource dataSource = DataSource.getInstance();
        //源apk
        File fromFile = dataSource.getFromFile();
        //构建复制的apk文件名称 = (渠道名称)+(_)+(源Apk名称)+(.apk)
        StringBuilder toFileNameBuilder = new StringBuilder();
        toFileNameBuilder.append(channelName)
                .append("_")
                .append(fromFile.getName());
        //渠道apk
        File toFile = new File(fromFile.getParent(), toFileNameBuilder.toString());
        //渠道文件名称 = (META_INF/)+ (渠道标识符) + encode(渠道名称)
        String channelFileName = Constant.META_INF + dataSource.getChannelFlag() + encode(channelName);
        ZipUtil.getInstance().markZip(fromFile, toFile, channelFileName, callback);
    }
}
