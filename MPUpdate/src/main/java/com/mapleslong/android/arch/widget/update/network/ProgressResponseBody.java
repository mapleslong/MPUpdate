package com.mapleslong.android.arch.widget.update.network;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 创建时间: 2018/11/21
 * 描述:
 *
 * @author Mapleslong
 * @version 1.0
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final AbsFileProgressCallback progressListener;
    private BufferedSource bufferedSource;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public ProgressResponseBody(ResponseBody responseBody, AbsFileProgressCallback progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * Returns the number of bytes in that will returned by {@link #bytes}, or {@link #byteStream}, or
     * -1 if unknown.
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                final long finalBytesRead = bytesRead;
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressListener.onProgress(totalBytesRead, responseBody.contentLength(), finalBytesRead == -1);
                    }
                });
                return bytesRead;
            }
        };
    }

}
