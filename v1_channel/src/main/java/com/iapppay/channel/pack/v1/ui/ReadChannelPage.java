package com.iapppay.channel.pack.v1.ui;

import com.iapppay.channel.pack.v1.ChannelUtil;
import com.iapppay.channel.pack.v1.interfaces.ResultCallback;
import com.iapppay.channel.pack.v1.util.DialogUtil;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private File selectedFile;

    private ReadChannelPage() {
        setLayout(new GridBagLayout());
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
                if (selectedFile != null) {
                    //操作逻辑
                    ChannelUtil.getInstance().readChannel(selectedFile, new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String data) {
                            DialogUtil.showDialog(data);
                        }

                        @Override
                        public void onError(String errorMsg) {
                            DialogUtil.showDialog(errorMsg);
                        }
                    });
                } else {
                    DialogUtil.showDialog("请先选择APK路径");
                }
            }
        });
    }

    private void initView() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1;

        etPath = new JTextField(30);
        etPath.setEditable(false);
        gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(etPath, gridBagConstraints);

        btnSelectPath = new JButton("选择APK路径");
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        add(btnSelectPath, gridBagConstraints);

        btnRead = new JButton("读取");
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        add(btnRead, gridBagConstraints);
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
        jFileChooser.setFileFilter(new FileNameExtensionFilter("apk文件", "apk"));
        jFileChooser.showDialog(new JLabel(), "选择");
        selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null) {
            etPath.setText(selectedFile.getAbsolutePath());
        }
    }
}
