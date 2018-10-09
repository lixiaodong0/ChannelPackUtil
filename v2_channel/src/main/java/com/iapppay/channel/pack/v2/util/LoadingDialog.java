package com.iapppay.channel.pack.v2.util;

import com.iapppay.channel.pack.v2.config.PageConfig;
import com.iapppay.channel.pack.v2.config.StringsConfig;
import com.iapppay.channel.pack.v2.config.WindowConfig;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Created by Administrator on 2018/10/9.
 */

public class LoadingDialog {
    private static LoadingDialog INSTANCE;
    private JDialog jDialog;
    private UpdateRunable updateRunable;
    private Frame frame;

    public static LoadingDialog getInstance() {
        if (INSTANCE == null) {
            synchronized (LoadingDialog.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LoadingDialog();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Frame owner) {
        frame = owner;
    }

    public void show() {
        jDialog = new JDialog();
        // 设置对话框的宽高
        jDialog.setSize(WindowConfig.Dialog.WIDTH, WindowConfig.Dialog.HEIGHT);
        // 设置对话框大小不可改变
        jDialog.setResizable(false);
        // 设置对话框相对显示的位置
        jDialog.setLocationRelativeTo(frame);

        jDialog.setUndecorated(true);
        jDialog.setAlwaysOnTop(true);
        initView(jDialog);
        jDialog.setVisible(true);
    }

    public void hide() {
        if (jDialog != null && jDialog.isShowing()) {
            closeUpdateRunnable();
            jDialog.setVisible(false);
            jDialog.dispose();
        }
    }

    private void initView(JDialog jDialog) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 1));
        JLabel iconLabel = new JLabel();
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        URL resource = getClass().getResource("loading.gif");
        if (resource != null) {
            iconLabel.setIcon(new ImageIcon(resource));
        } else {
            iconLabel.setText(StringsConfig.FAIL.IMG_LOADING_FAIL);
        }
        JLabel title = new JLabel();
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setText(PageConfig.Dialog.PACK_LOADING);
        if (resource == null) {
            updateRunable = new UpdateRunable(title);
            Thread thread = new Thread(updateRunable);
            thread.start();
        }
        jPanel.add(iconLabel);
        jPanel.add(title);
        jDialog.add(jPanel);
        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeUpdateRunnable();
            }
        });
    }

    private void closeUpdateRunnable() {
        if (updateRunable != null) {
            updateRunable.stop();
        }
    }

    private static class UpdateRunable implements Runnable {

        private JLabel jLabel;
        private boolean isStop;

        public UpdateRunable(JLabel jLabel) {
            this.jLabel = jLabel;
        }

        public void stop() {
            isStop = true;
        }

        @Override
        public void run() {
            String str = PageConfig.Dialog.PACK_LOADING.replace(".", "");
            int count = 1;
            while (!isStop) {
                try {
                    String text = "";
                    if (count == 1) {
                        text = str.concat(".");
                    } else if (count == 2) {
                        text = str.concat("..");
                    } else {
                        text = str.concat("...");
                        count = 0;
                    }
                    jLabel.setText(text);
                    count++;
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
