package com.mapleslong.android.arch.widget.update.network;

import java.util.Map;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class DownloadModel {
    /**
     * 请求地址
     */
    private String httpUrl;
    /**
     * 请求头
     */
    private Map<String, String> headersMap;
    /**
     * 请求Tag
     */
    private Object tag;
    /**
     * 下载文件保存的路径
     */
    private String downloadPath;

    /**
     * 文件下载进度
     */
    private AbsFileProgressCallback fileProgressCallback;

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public Map<String, String> getHeadersMap() {
        return headersMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public AbsFileProgressCallback getFileProgressCallback() {
        return fileProgressCallback;
    }

    public void setFileProgressCallback(AbsFileProgressCallback fileProgressCallback) {
        this.fileProgressCallback = fileProgressCallback;
    }
}
