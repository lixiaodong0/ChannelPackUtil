package com.iapppay.channel.pack.v1.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    private ChannelPropertiesUtil() {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
