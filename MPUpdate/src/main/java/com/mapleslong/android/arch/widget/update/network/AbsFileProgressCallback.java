package com.mapleslong.android.arch.widget.update.network;

/**
 * 创建时间: 2018/11/21
 * 描述: 文件下载回调
 *
 * @author Mapleslong
 * @version 1.0
 */
public abstract class AbsFileProgressCallback {
    /**
     * 下载开始
     */
    public abstract void onStart();

    /**
     */
    public abstract void onProgress(long bytesRead, long contentLength, boolean done);

    /**
     * 下载成功
     */
    public abstract void onSuccess(String result);

    /**
     * 下载失败
     */
    public abstract void onFailed(String errorMsg);

    /**
     * 下载取消
     */
    public abstract void onCancle();
}
