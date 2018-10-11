package com.iapppay.channel.pack.ui;

import com.iapppay.channel.pack.ChannelUtil;
import com.iapppay.channel.pack.config.MarkPropertiesConfig;
import com.iapppay.channel.pack.config.PageConfig;
import com.iapppay.channel.pack.config.StringsConfig;
import com.iapppay.channel.pack.data.DataSource;
import com.iapppay.channel.pack.interfaces.ResultCallback;
import com.iapppay.channel.pack.util.ChannelPropertiesUtil;
import com.iapppay.channel.pack.util.DialogUtil;
import com.iapppay.channel.pack.util.LetterNumberDocument;
import com.iapppay.channel.pack.util.LoadingDialog;
import com.iapppay.channel.pack.util.TextUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
import javax.swing.SwingWorker;
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
    //渠道配置文件选择器 选中的文件
    private File propertiesSelectedFile;

    //统计打包时间
    private long startPackTime;

    private WriteChannelPage() {
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

                showPropertiesFileChooser();
            }
        });

        btnWrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (apkSelectedFile == null) {
                    DialogUtil.showDialog(StringsConfig.FAIL.APK_PATH_FAIL);
                    return;
                }
                if (DataSource.getInstance().isMoreChannel()) {
                    LoadingDialog.getInstance().show();
                    ChannelPropertiesUtil.getInstance().getChannelMark(propertiesSelectedFile == null ? "" : propertiesSelectedFile.getAbsolutePath(), new ChannelPropertiesCallback());
                } else {
                    LoadingDialog.getInstance().show();
                    List<String> channelNames = new ArrayList<String>();
                    String channelMark = etMark.getText();
                    if (!TextUtil.isEmpty(channelMark)) {
                        channelNames.add(channelMark);
                        DataSource.getInstance().setChannelNames(channelNames);
                        outMark();
                    } else {
                        DialogUtil.showDialog(StringsConfig.FAIL.SINGLE_CHANNEL_MARK_FAIL);
                    }
                }
            }
        });
    }


    private class ChannelPropertiesCallback implements ResultCallback<List<String>> {

        @Override
        public void onSuccess(List<String> data) {
            DataSource.getInstance().setChannelNames(data);
            outMark();
        }

        @Override
        public void onError(String errorMsg) {
            DialogUtil.showDialog(errorMsg);
            btnWrite.setEnabled(true);
        }
    }

    private void outMark() {
        startPackTime = System.currentTimeMillis();
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                btnWrite.setEnabled(false);
                ChannelUtil.getInstance().packTask(new PackResultCallback());
                return null;
            }

            @Override
            protected void done() {
                super.done();
                btnWrite.setEnabled(true);
            }
        }.execute();
    }

    private class PackResultCallback implements ResultCallback<List<File>> {

        @Override
        public void onSuccess(List<File> data) {
            long endPackTime = System.currentTimeMillis();
            LoadingDialog.getInstance().hide();
            StringBuilder builder = new StringBuilder();
            builder.append(StringsConfig.SUCCESS.PACK_SUCCESS);
            builder.append("\n");
            if (data.size() == 1) {
                builder.append(StringsConfig.SUCCESS.CHANNEL_MARK).append(DataSource.getInstance().getChannelNames().get(0));
                builder.append("\n");
            } else {
                builder.append(StringsConfig.SUCCESS.CHANNEL_MARK);
                builder.append("\n");
                List<String> channelNames = DataSource.getInstance().getChannelNames();
                for (int count = channelNames.size(), i = 0; i < count; i++) {
                    builder.append("『")
                            .append(channelNames.get(i))
                            .append("』");
                    if (i % 10 == 0 && i != 0) {
                        builder.append("\n");
                    } else {
                        builder.append(" ");
                    }
                }
                builder.append("\n");
            }
            builder.append(StringsConfig.SUCCESS.APK_PATH).append(data.get(0).getParent());
            builder.append("\n");
            builder.append(StringsConfig.SUCCESS.PACK_TIME).append(formatTime(startPackTime, endPackTime));
            DialogUtil.showDialog(builder.toString());
            btnWrite.setEnabled(true);
        }

        @Override
        public void onError(String errorMsg) {
            LoadingDialog.getInstance().hide();
            DialogUtil.showDialog(errorMsg);
            btnWrite.setEnabled(true);
        }
    }

    /**
     * 格式化时间
     *
     * @param statTime 开始时间
     * @param endTime  结束时间
     */
    private String formatTime(long statTime, long endTime) {
        String timeStr = "";
        if (endTime > statTime) {
            long totalTime = endTime - statTime;
            //毫秒值转换成秒
            long second = totalTime / 1000;
            //天
            long days = second / 86400;
            second = second % 86400;
            //时
            long hours = second / 3600;
            second = second % 3600;
            //分
            long minutes = second / 60;
            //秒
            second = second % 60;

            StringBuilder builder = new StringBuilder();
            if (days != 0) {
                builder.append(days).append("天");
            }
            if (hours != 0) {
                builder.append(hours).append("时");
            }
            if (minutes != 0) {
                builder.append(minutes).append("分");
            }
            if (second != 0) {
                builder.append(second).append("秒");
            }
            timeStr = builder.toString();
        } else {
            timeStr = "格式化时间错误";
        }
        return timeStr;
    }


    private void initView() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.weightx = 1;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        JPanel apkFileContainer = new JPanel();
        apkFileContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        etApkPath = new JTextField(20);
        etApkPath.setEnabled(false);
        btnApkSelect = new JButton(PageConfig.WritePage.SELECT_APK_PATH);
        apkFileContainer.add(etApkPath);
        apkFileContainer.add(btnApkSelect);
        add(apkFileContainer, gridBagConstraints);

        JPanel modeContainer = new JPanel();
        modeContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnSingle = new JRadioButton(PageConfig.WritePage.SINGLE_CHANNEL, true);
        btnMore = new JRadioButton(PageConfig.WritePage.MORE_CHANNEL);
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

        btnWrite = new JButton(PageConfig.WritePage.OUT_MARK);
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
        jLabel.setText(PageConfig.WritePage.SINGLE_HINT_INFO);
        jLabel.setForeground(Color.RED);
        jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        hintLayout.add(jLabel);
        singleLayout.add(hintLayout);

        JPanel inputLayout = new JPanel();
        inputLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel flag = new JLabel(PageConfig.WritePage.CHNNEL_MARK_TITLE);
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
        jLabel.setText(PageConfig.WritePage.MORE_HINT_INFO);
        jLabel.setForeground(Color.RED);
        jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        hintLayout.add(jLabel);
        moreLayout.add(hintLayout);

        JPanel inputLayout = new JPanel();
        inputLayout.setLayout(new FlowLayout(FlowLayout.LEFT));
        etConfigPath = new JTextField(20);
        btnChannelConfig = new JButton(PageConfig.WritePage.CHANNEL_PROPERTIES_TITLE);
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
        jFileChooser.setFileFilter(new FileNameExtensionFilter(PageConfig.FileChooser.APK_FILTER_DESCRIPTION, PageConfig.FileChooser.APK_FILTER_RGE));
        jFileChooser.showDialog(new JLabel(), PageConfig.FileChooser.SELECT);
        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null) {
            apkSelectedFile = selectedFile;
            etApkPath.setText(apkSelectedFile.getAbsolutePath());
        }
    }

    /**
     * 显示渠道配置文件选择器
     */
    private void showPropertiesFileChooser() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //设置文件过滤器
        jFileChooser.setFileFilter(new FileNameExtensionFilter(MarkPropertiesConfig.MARK_FILTER_DESCRIPTION, MarkPropertiesConfig.MARK_FILTER_RGE));
        jFileChooser.showDialog(new JLabel(), PageConfig.FileChooser.SELECT);

        File selectedFile = jFileChooser.getSelectedFile();
        if (selectedFile != null) {
            propertiesSelectedFile = selectedFile;
            etConfigPath.setText(propertiesSelectedFile.getAbsolutePath());

            openFile(propertiesSelectedFile);
        }
    }

    /**
     * 打开文件
     */
    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            DialogUtil.showDialog(StringsConfig.FAIL.OPEN_FILE_FAIL);
        }
    }
}
