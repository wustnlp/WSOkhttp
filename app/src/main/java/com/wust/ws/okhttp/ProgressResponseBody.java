package com.wust.ws.okhttp;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private ProgressListener mProListener;
    private BufferedSource mBufferSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener mProListener) {
        this.responseBody = responseBody;
        this.mProListener = mProListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferSource == null){
            mBufferSource = Okio.buffer(getSource(responseBody.source()));
        }
        return mBufferSource;
    }

    private Source getSource(Source source){
        return new ForwardingSource(source) {
            long totalSize = 0l;
            long sum = 0l;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                if (totalSize == 0){
                    totalSize = contentLength();
                }
                long len =  super.read(sink, byteCount);

                sum += (len==-1?0:len);
                int progress = (int) (sum * 1.0f / totalSize *100);
                if (len == -1){
                    mProListener.onDone(totalSize);
                }else {
                    mProListener.onProgress(progress);
                }
                return len;
            }
        };
    }
}
