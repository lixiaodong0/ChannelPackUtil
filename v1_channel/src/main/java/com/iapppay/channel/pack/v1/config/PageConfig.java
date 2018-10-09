package com.iapppay.channel.pack.v1.config;

/**
 * 页面相关配置
 * 主要配置界面需要显示的文字
 * 不包括成功错误提示语，成功错误提示语请到 -> StringsConfig
 */

public interface PageConfig {

    interface WritePage {
        String TITLE = "写渠道";

        String SELECT_APK_PATH = "选择Apk路径";

        String SINGLE_CHANNEL = "单渠道";
        String SINGLE_HINT_INFO = "标识符只能输入字母+数字";
        String CHNNEL_MARK_TITLE = "渠道标识符:";

        String MORE_CHANNEL = "多渠道";
        String MORE_HINT_INFO = "如果提示拒绝访问，可以手动填写文件路径";
        String CHANNEL_PROPERTIES_TITLE = "渠道配置文件";

        String OUT_MARK = "打标";
    }

    interface ReadPage {
        String TITLE = "读渠道";

        String SELECT_APK_PATH = "选择Apk路径";

        String READ = "读取";
    }

    interface AboutPage {
        String TITLE = "关于";

        String COMPANY_INFO = "爱贝信息技术有限公司";
    }


    /**
     * dialog配置
     */
    interface Dialog {
        String TITLE = "提示";
    }


    interface FileChooser {
        /**
         * Apk文件后缀名过滤规则
         */
        String APK_FILTER_RGE = "apk";
        /**
         * Apk文件过滤描述
         */
        String APK_FILTER_DESCRIPTION = "*.apk文件";
        /**
         * 文件选择器 按钮的文本
         */
        String SELECT = "选择";
    }

}
