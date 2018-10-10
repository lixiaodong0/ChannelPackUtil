package com.iapppay.channel.pack.util;

import com.iapppay.channel.pack.config.PageConfig;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

/**
 * Created by Administrator on 2018/10/8.
 */

public class DialogUtil {
    private DialogUtil() {
    }


    public static void showDialog(String msg) {
        showDialog(PageConfig.Dialog.TITLE, msg);
    }

    public static void showDialog(String title, String msg) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, msg, title, -1);
    }

}
