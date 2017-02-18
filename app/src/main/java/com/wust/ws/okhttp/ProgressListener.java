package com.wust.ws.okhttp;

public interface ProgressListener {

    public void onProgress(int progress);

    public void onDone(long totalSize);
}
