package com.iapppay.channel.pack.config;

/**
 * 标识Properties文件相关配置
 */
public interface MarkPropertiesConfig {
    /**
     * 选择标签配置文件过滤规则
     */
    String MARK_FILTER_RGE = "properties";
    /**
     * 打标配置文件名
     */
    String MARK_VALUE_FILE = "markValue.properties";
    /**
     * 打标配置文件夹<br/>
     * 与exe版保持一致
     */
    String MARK_VALUE_DIR = "C:/Users/Administrator/AppData/Local/Temp/iapppay";

    /**
     * 打标文件过滤描述
     */
    String MARK_FILTER_DESCRIPTION = "*.properties文件";
    /**
     * 新建的打标配置文件填入的内容
     */
    String MARK_VALUE_FILE_CONTETNT = "#这里是markValue.properties文件" + "\n"
            + "#这个文件的作用是配置多渠道打标的渠道" + "\n"
            + "\n"
            + "#怎么配置渠道标识？" + "\n"
            + "#渠道标识以key=value的形式存在" + "\n"
            + "\n"
            + "#配置渠道标识的要求" + "\n"
            + "#文件每行只写一个key=value,并且写在每一行的最左侧" + "\n"
            + "#key和value 必须是字母或者数字 value限制长度20位,例如 channel1=oppo" + "\n"
            + "#key或者value不要重复" + "\n"
            + "\n"
            + "channel1=360" + "\n"
            + "channel2=wandoujia";
}
