package com.mapleslong.android.arch.widget.update;

/**
 * 创建时间: 2018/11/21
 * 描述: 对外的主要工具类对象
 *
 * @author Mapleslong
 * @version 1.0
 */
public class MPUpdateManager {

    /**
     * 下载回调监听
     */
    public interface DownloadCallBack {
        void onStart();

        void onComplete(String path);

        void onLoading(long total, long current);

        void onFail(Exception e);

        void cancle();
    }


    /**
     * 开始通知栏的进度更新
     */
    public static void startNotificationUpdate() {
    }

    /**
     * 开始磨人的对话框更新
     */
    public static void startDefaultDialogUpdate() {
    }

    /**
     * 开始自定义界面的更新
     */
    public static void startCustomeViewDialogUpdate() {
    }
}
