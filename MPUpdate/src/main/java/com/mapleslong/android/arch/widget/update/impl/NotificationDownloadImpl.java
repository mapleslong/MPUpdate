package com.mapleslong.android.arch.widget.update.impl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.mapleslong.android.arch.widget.update.interfaces.IDownloadManager;
import com.mapleslong.android.arch.widget.update.utils.PathUtils;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class NotificationDownloadImpl implements IDownloadManager {
    private Context mContext;

    private int notifyId;
    private static NotificationCompat.Builder builder;
    private static NotificationManager manager;
    private static NotificationChannel channel;

    public NotificationDownloadImpl(Context context) {
        this.mContext = context;
    }

    /**
     * 设置下载路径
     *
     * @return
     */
    @Override
    public String getDownalodPath() {
        return PathUtils.getCachePath(mContext);
    }

    /**
     * 开始下载
     *
     * @param title 下载标题
     * @param desc  下载内容描述
     * @param url   下载地址
     * @return
     */
    @Override
    public void download(String title, String desc, String url) {

    }
}
