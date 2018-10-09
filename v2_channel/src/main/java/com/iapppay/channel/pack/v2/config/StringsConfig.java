package com.iapppay.channel.pack.v2.config;

/**
 * 失败，成功提示语配置
 */
public interface StringsConfig {


    interface FAIL {
        String APK_PATH_FAIL = "请先选择APK路径";

        String SINGLE_CHANNEL_MARK_FAIL = "请输入或配置渠道标识符";

        String OPEN_FILE_FAIL = "打开.properties文件失败";

        String NOT_FOUND_CHANNEL_MARK = "读取失败，该apk尚未打渠道号";

        String CREATE_CHANNEL_PROPERTIES_FAIL = "创建默认渠道配置文件失败";

        String READ_CHANNEL_PROPERTIES_FAIL = "读取配置文件失败，请检查配置文件内容格式是否正确";

        String CHANNEL_PROPERTIES_FORMAT_FAIL = "配置文件格式错误，渠道标识符只能包含字母+数字组合";

        String ZIP_FILE_NOT_NULL = "源文件不存在或是空的";

        String CHANNEL_MARK_NOT_NULL = "渠道标识符不能为空";

        String MORE_CHANNEL_PACL_FAIL = "多渠道打包失败，请联系技术人员";

        String IMG_LOADING_FAIL="图片加载失败";
    }


    interface SUCCESS {

        String PACK_SUCCESS = "打包成功";

        String CHANNEL_MARK = "渠道标识：";

        String APK_PATH = "Apk路径：";
    }

}
