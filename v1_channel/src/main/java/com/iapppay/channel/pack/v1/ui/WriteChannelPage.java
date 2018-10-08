package com.iapppay.channel.pack.v1.ui;

import com.iapppay.channel.pack.v1.ChannelUtil;
import com.iapppay.channel.pack.v1.data.DataSource;
import com.iapppay.channel.pack.v1.interfaces.ResultCallback;
import com.iapppay.channel.pack.v1.util.DialogUtil;
import com.iapppay.channel.pack.v1.util.LetterNumberDocument;
import com.iapppay.channel.pack.v1.util.TextUtil;
import com.iapppay.channel.pack.v1.util.WaitDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 输出渠道号页面
 */

public class WriteChannelPage extends JPanel {
    private static WriteChannelPage INSTANCE;
    //apk路径输入框
    private JTextField etApkPath;
    //apk文件选择器按钮
    private JButton btnApkSelect;
    //单渠道按钮
    private JRadioButton btnSingle;
    //多渠道按钮
    private JRadioButton btnMore;
    //渠道信息容器
    private JPanel channelInfoContainer;
    //单，多 渠道布局容器
    private Component singleChannelLayout, moreChannelLayout;
    //单渠道标识符输入框
    private JTextField etMark;
    //多渠道信息配置路径输入框
    private JTextField etConfigPath;
    //多渠道配置文件按钮
    private JButton btnChannelConfig;
    //打标 按钮
    private JButton btnWrite;

    //apk文件选择器 选中的文件
    private File apkSelectedFile;
    private WaitDialog waitDialog;


    private WriteChannelPage() {
        //        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridBagLayout());
        initView();
        initEvent();
    }

    private void initEvent() {
        btnSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleChannelLayout.setVisible(true);
                moreChannelLayout.setVisible(false);
                DataSource.getInstance().setMoreChannel(false);
            }
        });
        btnMore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                singleChannelLayout.setVisible(false);
                moreChannelLayout.setVisible(true);
                DataSource.getInstance().setMoreChannel(true);
            }
        });

        btnApkSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
                DataSource.getInstance().setFromFile(apkSelectedFile);
            }
        });


        btnChannelConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        btnWrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (apkSelectedFile == null) {
                    DialogUtil.showDialog("请先选择APK路径");
                    return;
                }


                List<String> channelNames = new ArrayList<String>();
                if (DataSource.getInstance().isMoreChannel()) {

                } else {
                    String channelMark = etMark.getText();
                    if (!TextUtil.isEmpty(channelMark)) {
                        channelNames.add(channelMark);
                        DataSource.getInstance().setChannelNames(channelNames);
                        outMark();
                    } else {
                        DialogUtil.showDialog("请输入或配置渠道标识符");
                    }
                }
            }
        });
    }


    private void outMark() {
        waitDialog = new WaitDialog(this, "打标中.....");
        waitDialog.setVisible(true);
        ChannelUtil.getInstance().packTask(new PackResultCallback());
    }

    private class PackResultCallback implements ResultCallback<List<File>> {

        @Override
        public void onSuccess(List<File> data) {
            StringBuilder builder = new StringBuilder();
            if (data.size() == 1) {
                builder.append("打包成功");
                builder.append("\n");
                builder.append("渠道标识：").append(DataSource.getInstance().getChannelNames().get(0));
                builder.append("\n");
                builder.append("Apk路径： ").append(data.get(0).getAbsoluteFile());
            } else {

            }
            DialogUtil.showDialog(builder.toString());

            if (waitDialog != null) {
                waitDialog.setVisible(false);
            }
        }

        @Override
        public void onError(String errorMsg) {
            DialogUtil.showDialog(errorMsg);

            if (waitDialog != null) {
                waitDialog.setVisible(false);
            }
        }
    }


    private void initView() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        JPanel apkFileContainer = new JPanel();
        apkFileContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        etApkPath = new JTextField(20);
        btnApkSelect = new JButton("选择Apk路径");
        apkFileContainer.add(etApkPath);
        apkFileContainer.add(btnApkSelect);
        add(apkFileContainer, gridBagConstraints);

        JPanel modeContainer = new JPanel();
        modeContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnSingle = new JRadioButton("单渠道", true);
        btnMore = new JRadioButton("多渠道");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(btnSingle);
        buttonGroup.add(btnMore);
        modeContainer.add(btnSingle);
        modeContainer.add(btnMore);
        add(modeContainer, gridBagConstraints);

        channelInfoContainer = new JPanel();
        channelInfoContainer.setLayout(new BoxLayout(channelInfoContainer, BoxLayout.Y_AXIS));
        singleChannelLayout = getSingleChannelLayout();
        moreChannelLayout = getMoreChannelLayout();
        singleChannelLayout.setVisible(true);
        moreChannelLayout.setVisible(false);
        channelInfoContainer.add(singleChannelLayout);
        channelInfoContainer.add(moreChannelLayout);
        add(channelInfoContainer, gridBagConstraints);

        btnWrite = new JButton("打标");
        add(btnWrite, gridBagConstraints);
    }

    public static WriteChannelPage getInstance() {
        if (INSTANCE == null) {
            synchronized (WriteChannelPage.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WriteChannelPage();
                }
            }
        }

        return INSTANCE;
    }

    private Component getSingleChannelLayout() {
        JPanel singleLayout = new JPanel();
        singleLayout.setLayout(new BoxLayout(singleLayout, BoxLayout.Y_AXIS));

        JPanel hintLayout = new JPanel();
        hintLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel();
        jLabel.setText("标识符只能输入字母+数字");
        jLabel.setForeground(Color.RED);
        jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        hintLayout.add(jLabel);
        singleLayout.add(hintLayout);

        JPanel inputLayout = new JPanel();
        inputLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel flag = new JLabel("渠道标识符:");
        etMark = new JTextField(20);
        etMark.setDocument(new LetterNumberDocument());
        inputLayout.add(flag);
        inputLayout.add(etMark);
        singleLayout.add(inputLayout);
        return singleLayout;
    }

    private Component getMoreChannelLayout() {
        JPanel moreLayout = new JPanel();
        moreLayout.setLayout(new BoxLayout(moreLayout, BoxLayout.Y_AXIS));

        JPanel hintLayout = new JPanel();
        hintLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel();
        jLabel.setText("如果提示拒绝访问，可以手动填写文件路径");
        jLabel.setForeground(Color.RED);
        jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        hintLayout.add(jLabel);
        moreLayout.add(hintLayout);

        JPanel inputLayout = new JPanel();
        inputLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        etConfigPath = new JTextField(20);
        btnChannelConfig = new JButton("渠道配置文件");
        inputLayout.add(etConfigPath);
        inputLayout.add(btnChannelConfig);
        moreLayout.add(inputLayout);
        return moreLayout;
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
        apkSelectedFile = jFileChooser.getSelectedFile();
        if (apkSelectedFile != null) {
            etApkPath.setText(apkSelectedFile.getAbsolutePath());
        }
    }
}
