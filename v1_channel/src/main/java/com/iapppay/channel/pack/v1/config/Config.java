package com.iapppay.channel.pack.v1.config;

/**
 * Created by Administrator on 2018/9/30.
 */

public interface Config {
    //版本号
    String VERSION = "1.0";
    //渠道文件父级路径名称
    String META_INF = "META-INF/";
    //Apk后缀名
    String APK_SUF = ".apk";
    //公司信息
    String COMPANY_INFO = "爱贝信息技术有限公司";

    //窗口弹窗标题
    String DIALOG_TITLE = "提示";

    //宽度
    int WIDTH = 400;
    //高度
    int HEIGHT = 300;


    /**
     * waitDialog的宽度
     */
    int WAIT_DIALOG_WIDTH = 280;
    /**
     * waitDialog的高度
     */
    int WAIT_DIALOG_HEIGHT = 50;


    //多渠道配置
    interface MarkProperties {
        /**
         * 选择标签配置文件过滤规则
         */
        String MARK_FILTER_RGE = ".properties";
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
}
