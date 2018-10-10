package com.iapppay.channel.pack;

import com.iapppay.channel.pack.config.StringsConfig;
import com.iapppay.channel.pack.data.DataSource;
import com.iapppay.channel.pack.interfaces.ResultCallback;
import com.iapppay.channel.pack.util.ChannelCompatUtil;
import com.iapppay.channel.pack.util.TextUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 渠道工具
 */

public class ChannelUtil {

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


    /**
     * 渠道打包任务
     */
    public void packTask(ResultCallback<List<File>> callback) {
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
    public void readChannel(File apkFile, ResultCallback<String> callback) {
        String s = ChannelCompatUtil.get(apkFile);
        if (!TextUtil.isEmpty(s)) {
            callback.onSuccess(s);
        } else {
            callback.onSuccess(StringsConfig.FAIL.NOT_FOUND_CHANNEL_MARK);
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
    private void singleChannel(final ResultCallback<List<File>> callback) {
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
        final List<String> failChannelList = new ArrayList<>();
        final List<File> successChannelList = new ArrayList<>();
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
        try {
            boolean isSuccess = ChannelCompatUtil.put(fromFile, toFile, channelName);
            if (isSuccess) {
                callback.onSuccess(toFile);
            } else {
                callback.onError(StringsConfig.FAIL.OUT_CHANNEL_MARK_FAIL);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(StringsConfig.FAIL.COPY_APK_FAIL);
        }
    }

}
