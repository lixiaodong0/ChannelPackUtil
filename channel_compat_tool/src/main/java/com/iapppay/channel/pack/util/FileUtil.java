package com.iapppay.channel.pack.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Administrator on 2018/10/12.
 */
public class FileUtil {

    /**
     * 判断当前java版本号是否 >= 1.7
     *
     * @return
     */
    public static boolean isJava7Version() {
        String version = System.getProperty("java.version");
        Log.v("java version " + version);
        if (TextUtil.isEmpty(version)) {
            return false;

        }

        //version = 1.8.0_171
        String[] split = version.split("\\.");
        if (split != null && split.length > 2) {
            //读取版本
            version = split[0] + "." + split[1];
            Log.v("format java version " + version);
        }

        try {
            Double aDouble = Double.valueOf(version);
            if (aDouble >= 1.7) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void copyFile(File fromFile, File toFile) throws IOException {
        if (isJava7Version()) {
            Log.v("java 7 mode copy file ");
            java7CopyMode(fromFile, toFile);
        } else {
            Log.v("default mode copy file ");
            defaultCopyMode(fromFile, toFile);
        }
    }


    /**
     * 默认的拷贝文件方式
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @throws IOException
     */
    private static void defaultCopyMode(File fromFile, File toFile) throws IOException {
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

    /**
     * java 7 拷贝文件方式
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @throws IOException
     */
    private static void java7CopyMode(File fromFile, File toFile) throws IOException {
        Path fromFilePath = Paths.get(fromFile.getAbsolutePath());
        Path toFilePath = Paths.get(toFile.getAbsolutePath());
        Files.copy(fromFilePath, toFilePath);
    }

}
