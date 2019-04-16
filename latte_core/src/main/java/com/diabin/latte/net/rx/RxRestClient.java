package com.diabin.latte.net.rx;

import android.content.Context;

import com.diabin.latte.download.DownloadHandler;
import com.diabin.latte.net.RestCreator;
import com.diabin.latte.net.RestService;
import com.diabin.latte.net.callback.IError;
import com.diabin.latte.net.callback.IFailure;
import com.diabin.latte.net.callback.IRequest;
import com.diabin.latte.net.callback.ISuccess;
import com.diabin.latte.net.callback.RequestCallbacks;
import com.diabin.latte.ui.loader.LatteLoader;
import com.diabin.latte.ui.loader.LoaderStyle;
import com.diabin.latte.util.LLog;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import com.diabin.latte.net.HttpMethod;

public  class RxRestClient {

    private final String URL;
    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;

    private final File FILE;
    private final Context CONTEXT;
    /**
     *
     */
    private WeakHashMap<String, Object> PARAMS;


    private RxRestClient(String url,
                         Map<String, Object> params,
                         RequestBody body,
                         File file,
                         Context context,
                         LoaderStyle loaderStyle) {
        this.URL = url;
        this.BODY = body;
        this.FILE = file;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;

        PARAMS = new WeakHashMap<>();
        if (params != null && params.size() > 0) {
            PARAMS.putAll(params);
        }
    }

    public static RxRestClientBuilder Builder() {
        return new RxRestClientBuilder();
    }

    private Observable<String> request(HttpMethod method) {
        final RxRestService service = RestCreator.getRxRestService();
        Observable<String> observable = null;

        if (LOADER_STYLE != null) {
            LatteLoader.showLoading(CONTEXT, LOADER_STYLE);
        }

        switch (method) {
            case GET: //查
                observable = service.get(URL, PARAMS);
                break;
            case POST: //改
                observable = service.post(URL, PARAMS);
                break;
            case POST_RAW: // 改
                observable = service.postRaw(URL, BODY);
                break;
            case PUT: //增
                observable = service.put(URL, PARAMS);
                break;
            case PUT_RAW: //增
                observable = service.putRaw(URL, BODY);
                break;
            case DELETE: //删
                observable = service.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                observable = service.upload(URL, body);
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

        return observable;
    }

    public final Observable<String>  get() {
        return request(HttpMethod.GET);
    }

    public final Observable<String>  post() {
        if (BODY == null) {
            return request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.POST_RAW);
        }
    }

    public final Observable<String>  put() {
        if (BODY == null) {
           return request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            return request(HttpMethod.PUT_RAW);
        }
    }

    public final Observable<String>  delete() {
        return request(HttpMethod.DELETE);
    }

    public final Observable<String>  upload() {
        return request(HttpMethod.UPLOAD);
    }

    public final Observable<ResponseBody>  download() {
        Observable<ResponseBody> responseBodyObservable = RestCreator.getRxRestService().download(URL,PARAMS);
        return responseBodyObservable;
//        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
//                SUCCESS, FAILURE, ERROR)
//                .handleDownload();
    }

    /**
     * 建造者模式
     */
    public static final class RxRestClientBuilder {
        private String mUrl = null;
        private RequestBody mBody = null;
        private Context mContext = null;
        private LoaderStyle mLoaderStyle = null;
        private File mFile = null;


        private WeakHashMap<String, Object> mParams;

        RxRestClientBuilder() {
            mParams = new WeakHashMap<>();
        }

        public final RxRestClientBuilder url(String url) {
            this.mUrl = url;
            return this;
        }

        public final RxRestClientBuilder params(WeakHashMap<String, Object> params) {
            mParams.putAll(params);
            return this;
        }

        public final RxRestClientBuilder param(String key, Object value) {
            mParams.put(key, value);
            return this;
        }

        public final RxRestClientBuilder file(File file) {
            this.mFile = file;
            return this;
        }

        public final RxRestClientBuilder file(String file) {
            this.mFile = new File(file);
            return this;
        }

        public final RxRestClientBuilder raw(String raw) {
            this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
            return this;
        }

        public final RxRestClientBuilder loader(Context context, LoaderStyle style) {
            this.mContext = context;
            this.mLoaderStyle = style;
            return this;
        }

        public final RxRestClientBuilder loader(Context context) {
            this.mContext = context;
            this.mLoaderStyle = LoaderStyle.BallClipRotatePulseIndicator;
            return this;
        }

        public final RxRestClient build() {
            return new RxRestClient(mUrl, mParams,
                    mBody, mFile, mContext,
                    mLoaderStyle);
        }
    }
}
