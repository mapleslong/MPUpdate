package com.mapleslong.android.arch.widget.update.interfaces;

import com.mapleslong.android.arch.widget.update.MPUpdateManager;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public interface IDownloadManager {

    /**
     * 开始下载
     *
     * @param title            下载标题
     * @param desc             下载内容描述
     * @param url              下载地址
     * @param downloadCallBack 回调方法
     */
    void download(String title, String desc, String url, MPUpdateManager.DownloadCallBack downloadCallBack);

    /**
     * 取消下载
     */
    void cancel();
}
