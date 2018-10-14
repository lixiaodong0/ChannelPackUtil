package com.iapppay.channel.pack.ui;

import com.iapppay.channel.pack.ChannelUtil;
import com.iapppay.channel.pack.config.PageConfig;
import com.iapppay.channel.pack.config.StringsConfig;
import com.iapppay.channel.pack.interfaces.ResultCallback;
import com.iapppay.channel.pack.util.DialogUtil;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 读取渠道号页面
 */

public class ReadChannelPage extends JPanel {

    public static ReadChannelPage INSTANCE = null;
    //选择APK文件 按钮
    private JButton btnSelectPath;
    //APK路径 输入框
    private JTextField etPath;
    //读取 按钮
    private JButton btnRead;
    //选中的File文件
    private File selectedApkFile;

    private ReadChannelPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initView();
        initEvent();
    }

    private void initEvent() {
        btnSelectPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //打开文件选择器
                showFileChooser();
            }
        });
        btnRead.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedApkFile != null) {
                    //操作逻辑
                    ChannelUtil.getInstance().readChannel(selectedApkFile, new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            StringBuilder builder = new StringBuilder();
                            builder.append(StringsConfig.SUCCESS.READ_SUCCESS).append("\n");
                            builder.append(StringsConfig.SUCCESS.CHANNEL_MARK).append(data);
                            DialogUtil.showDialog(builder.toString());
                        }

                        @Override
                        public void onError(String errorMsg) {
                            DialogUtil.showDialog(errorMsg);

                        }
                    });
                } else {
                    DialogUtil.showDialog(StringsConfig.FAIL.APK_PATH_FAIL);
                }
            }
        });
    }

    private void initView() {

        //占位置
        JPanel topLayout = new JPanel();
        topLayout.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(topLayout);

        JPanel centerLayout = new JPanel();
        centerLayout.setLayout(new FlowLayout(FlowLayout.CENTER));
        etPath = new JTextField(20);
        etPath.setEnabled(false);
        btnSelectPath = new JButton(PageConfig.ReadPage.SELECT_APK_PATH);
        centerLayout.add(etPath);
        centerLayout.add(btnSelectPath);
        add(centerLayout);
        JPanel bottomLayout = new JPanel();
        bottomLayout.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnRead = new JButton(PageConfig.ReadPage.READ);
        bottomLayout.add(btnRead);
        add(bottomLayout);
    }

    public static ReadChannelPage getInstance() {
        if (INSTANCE == null) {
            synchronized (ReadChannelPage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ReadChannelPage();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 显示文件选择器
     */
    private void showFileChooser() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //设置文件过滤器
        jFileChooser.setFileFilter(new FileNameExtensionFilter(PageConfig.FileChooser.APK_FILTER_DESCRIPTION, PageConfig.FileChooser.APK_FILTER_RGE));
        jFileChooser.showDialog(new JLabel(), PageConfig.FileChooser.SELECT);
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null) {
            selectedApkFile = selectedFile;
            etPath.setText(selectedFile.getAbsolutePath());
        }
    }
}
