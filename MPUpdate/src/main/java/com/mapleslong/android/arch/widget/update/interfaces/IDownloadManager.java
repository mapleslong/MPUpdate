package com.mapleslong.android.arch.widget.update.interfaces;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public interface IDownloadManager {
    /**
     * 设置下载路径
     *
     * @return
     */
    String getDownalodPath();

    /**
     * 开始下载
     *
     * @param title 下载标题
     * @param desc  下载内容描述
     * @param url   下载地址
     * @return
     */
    void download(String title, String desc, String url);

}
