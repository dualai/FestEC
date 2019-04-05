package com.diabin.latte.download;

import android.os.AsyncTask;
import android.os.Environment;

import com.diabin.latte.net.RestCreator;
import com.diabin.latte.net.callback.IError;
import com.diabin.latte.net.callback.IFailure;
import com.diabin.latte.net.callback.IRequest;
import com.diabin.latte.net.callback.ISuccess;
import com.diabin.latte.util.LLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 傅令杰 on 2017/4/2
 */

public final class DownloadHandler {

    private final String URL;
    private final WeakHashMap<String, Object> PARAMS;
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;

    public DownloadHandler(String url,
                           IRequest request,
                           String downDir,
                           String extension,
                           String name,
                           ISuccess success,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = downDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;

        PARAMS = new WeakHashMap<>();
    }

    public final void handleDownload() {
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        RestCreator
                .getRestService()
                .download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                        if (response.isSuccessful()) {

                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    DOWNLOAD_DIR, EXTENSION, responseBody, NAME);

                            LLog.d("onResponse...");

                            //这里一定要注意判断，否则文件下载不全
                            if (task.isCancelled()) { //判断asyntask是否已经结束了，否则这里文件下载不全
                                if (REQUEST != null) {
                                    REQUEST.onRequestEnd();
                                }
                            }

//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        InputStream is = response.body().byteStream();
//                                        File file = new File(Environment.getExternalStorageDirectory(), "abc1.apk");
//                                        FileOutputStream fos = new FileOutputStream(file);
//                                        BufferedInputStream bis = new BufferedInputStream(is);
//                                        byte[] buffer = new byte[1024];
//                                        int len;
//                                        while ((len = bis.read(buffer)) != -1) {
//                                            fos.write(buffer, 0, len);
//                                            fos.flush();
//                                        }
//                                        fos.close();
//                                        bis.close();
//                                        is.close();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();


                        } else {
                            LLog.d("ERROR...");
                            if (ERROR != null) {
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        LLog.d("onFailure...");
                        if (FAILURE != null) {
                            FAILURE.onFailure();
                        }
                    }
                });
    }
}
