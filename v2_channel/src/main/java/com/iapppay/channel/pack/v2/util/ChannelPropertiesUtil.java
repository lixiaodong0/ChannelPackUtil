package com.iapppay.channel.pack.v2.util;

import com.iapppay.channel.pack.v2.config.MarkPropertiesConfig;
import com.iapppay.channel.pack.v2.config.StringsConfig;
import com.iapppay.channel.pack.v2.interfaces.ResultCallback;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 渠道配置文件工具类
 * 用于读取多渠道的渠道名称
 */

public class ChannelPropertiesUtil {

    private static ChannelPropertiesUtil INSTANCE;

    private ChannelPropertiesUtil() {
    }


    public static final ChannelPropertiesUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ChannelPropertiesUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChannelPropertiesUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 读取渠道标识符
     *
     * @param filePath 渠道文件路径
     * @param callback 回调
     */
    public void getChannelMark(String filePath, ResultCallback<List<String>> callback) {
        if (callback == null) {
            Log.v("callback not null");
            return;
        }

        File file = null;
        if (TextUtil.isEmpty(filePath)) {
            //没有选择渠道配置文件，使用默认路径生成一个
            try {
                file = getDefaultChannelConfigFile();
            } catch (IOException e) {
                e.printStackTrace();
                callback.onError(StringsConfig.FAIL.CREATE_CHANNEL_PROPERTIES_FAIL);
            }
        } else {
            //选择了渠道配置文件
            file = new File(filePath);
        }

        List<String> channelMarks = readChannel(file);
        if (channelMarks != null && channelMarks.size() > 0) {
            if (checkValidity(channelMarks)) {
                callback.onSuccess(channelMarks);
            } else {
                callback.onError(StringsConfig.FAIL.CHANNEL_PROPERTIES_FORMAT_FAIL);
            }
        } else {
            /**
             * 配置文件读取失败
             * 1. 文件不存在
             * 2. 文件格式没有按照key=value形式
             */
            callback.onError(StringsConfig.FAIL.READ_CHANNEL_PROPERTIES_FAIL);
        }
    }

    /**
     * 数据只能包含数字+字母组合
     */
    private boolean checkValidity(List<String> channelMarks) {
        boolean validity = true;
        for (String channelMark : channelMarks) {
            if (!channelMark.matches(LetterNumberDocument.REG)) {
                validity = false;
            }
        }
        return validity;
    }

    /**
     * 获取默认的渠道信息配置文件
     */
    private File getDefaultChannelConfigFile() throws IOException {
        File file = new File(MarkPropertiesConfig.MARK_VALUE_DIR, MarkPropertiesConfig.MARK_VALUE_FILE);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (!file.exists()) {
            //创建配置文件
            file.createNewFile();

            //输写内容
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(MarkPropertiesConfig.MARK_VALUE_FILE_CONTETNT);
            writer.flush();
            writer.close();
        }
        return file;
    }


    public static final List<String> readChannel(File file) {
        List<String> data = new ArrayList<>();
        if (file != null && file.exists()) {
            try {
                Properties properties = new Properties();
                BufferedReader bufr = new BufferedReader(new FileReader(file));
                properties.load(bufr);

                Set<String> keys = properties.stringPropertyNames();
                for (String key : keys) {
                    String value = properties.getProperty(key);
                    //保存渠道
                    data.add(value);
                }
                bufr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
