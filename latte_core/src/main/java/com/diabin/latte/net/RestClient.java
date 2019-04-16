package com.diabin.latte.net;

import android.content.Context;
import android.util.Log;


import com.diabin.latte.download.DownloadHandler;
import com.diabin.latte.net.callback.IError;
import com.diabin.latte.net.callback.IFailure;
import com.diabin.latte.net.callback.IRequest;
import com.diabin.latte.net.callback.ISuccess;
import com.diabin.latte.net.callback.RequestCallbacks;
import com.diabin.latte.ui.loader.LatteLoader;
import com.diabin.latte.ui.loader.LoaderStyle;
import com.diabin.latte.util.LLog;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public final class RestClient {

    private final String URL;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;

    private final LoaderStyle LOADER_STYLE;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;

    private final File FILE;
    private final Context CONTEXT;
    /**
     *
     */
    private WeakHashMap<String, Object> PARAMS;


    private RestClient(String url,
                       Map<String, Object> params,
                       String downloadDir,
                       String extension,
                       String name,
                       IRequest request,
                       ISuccess success,
                       IFailure failure,
                       IError error,
                       RequestBody body,
                       File file,
                       Context context,
                       LoaderStyle loaderStyle) {
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;

        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.FILE = file;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;

        PARAMS = new WeakHashMap<>();
        if (params != null && params.size() > 0) {
            PARAMS.putAll(params);
        }
    }

    public static RestClientBuilder Builder() {
        return new RestClientBuilder();
    }

    private void request(HttpMethod method) {
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET: //查
                call = service.get(URL, PARAMS);
                break;
            case POST: //改
                call = service.post(URL, PARAMS);
                break;
            case POST_RAW: // 改
                call = service.postRaw(URL, BODY);
                break;
            case PUT: //增
                call = service.put(URL, PARAMS);
                break;
            case PUT_RAW: //增
                call = service.putRaw(URL, BODY);
                break;
            case DELETE: //删
                call = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = service.upload(URL, body);
                break;
            default:
                break;
        }

        LLog.d("start....");
        Set<Map.Entry<String, Object>> mSet = PARAMS.entrySet();
        Iterator<Map.Entry<String, Object>> it = mSet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            LLog.d(entry.getKey() + " " + entry.getValue());
        }


        if (call != null) {
            call.enqueue(getRequestCallback());
        }
    }


    private Callback<String> getRequestCallback() {
        return new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
        request(HttpMethod.UPLOAD);
    }

    public final void download() {
        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
                SUCCESS, FAILURE, ERROR)
                .handleDownload();
    }

    /**
     * 建造者模式
     */
    public static final class RestClientBuilder {
        private String mUrl = null;
        private IRequest mIRequest = null;
        private ISuccess mISuccess = null;
        private IFailure mIFailure = null;
        private IError mIError = null;
        private RequestBody mBody = null;
        private Context mContext = null;
        private LoaderStyle mLoaderStyle = null;
        private File mFile = null;
        private String mDownloadDir = null;
        private String mExtension = null;
        private String mName = null;


        private WeakHashMap<String, Object> mParams;

        RestClientBuilder() {
            mParams = new WeakHashMap<>();
        }

        public final RestClientBuilder url(String url) {
            this.mUrl = url;
            return this;
        }

        public final RestClientBuilder params(WeakHashMap<String, Object> params) {
            mParams.putAll(params);
            return this;
        }

        public final RestClientBuilder param(String key, Object value) {
            mParams.put(key, value);
            return this;
        }

        public final RestClientBuilder file(File file) {
            this.mFile = file;
            return this;
        }

        public final RestClientBuilder file(String file) {
            this.mFile = new File(file);
            return this;
        }

        public final RestClientBuilder name(String name) {
            this.mName = name;
            return this;
        }

        public final RestClientBuilder dir(String dir) {
            this.mDownloadDir = dir;
            return this;
        }

        public final RestClientBuilder extension(String extension) {
            this.mExtension = extension;
            return this;
        }

        public final RestClientBuilder raw(String raw) {
            this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
            return this;
        }

        public final RestClientBuilder onRequest(IRequest iRequest) {
            this.mIRequest = iRequest;
            return this;
        }

        public final RestClientBuilder success(ISuccess iSuccess) {
            this.mISuccess = iSuccess;
            return this;
        }

        public final RestClientBuilder failure(IFailure iFailure) {
            this.mIFailure = iFailure;
            return this;
        }

        public final RestClientBuilder error(IError iError) {
            this.mIError = iError;
            return this;
        }

        public final RestClientBuilder loader(Context context, LoaderStyle style) {
            this.mContext = context;
            this.mLoaderStyle = style;
            return this;
        }

        public final RestClientBuilder loader(Context context) {
            this.mContext = context;
            this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
            return this;
        }

        public final RestClient build() {
            return new RestClient(mUrl, mParams,
                    mDownloadDir, mExtension, mName,
                    mIRequest, mISuccess, mIFailure,
                    mIError, mBody, mFile, mContext,
                    mLoaderStyle);
        }
    }
}
