package com.diabin.festec.example;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.diabin.latte.delegates.LatteDelegate;
import com.diabin.latte.net.RestClient;
import com.diabin.latte.net.RestCreator;
import com.diabin.latte.net.callback.IError;
import com.diabin.latte.net.callback.IFailure;
import com.diabin.latte.net.callback.ISuccess;
import com.diabin.latte.net.rx.RxRestClient;

import java.io.File;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ExampleDelegate extends LatteDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
//        test();
//        testRx1();
        testRx2();
    }

    private void testRx2() {
        final String url = "http://news.baidu.com";
        RxRestClient.Builder()
                .url(url)
                .build()
                .get().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void testRx() {
        final String url = "http://news.baidu.com";
        final WeakHashMap<String, Object> params = new WeakHashMap<>();
        final Observable<String> observable = RestCreator.getRxRestService().get(url, params);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) //如果是download，写文件必须在子线程，不能这样
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    //单次请求测试
    private void test() {
        RestClient.Builder().url("http://127.0.0.1/index")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();


//        RestClient.Builder().url("http://192.168.0.103/app-debug.apk")
////                .loader(getContext())
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//
//                    }
//                })
//                .failure(new IFailure() {
//                    @Override
//                    public void onFailure() {
//
//                    }
//                })
//                .error(new IError() {
//                    @Override
//                    public void onError(int code, String msg) {
//
//                    }
//                })
//                .dir(getContext().getCacheDir().getAbsolutePath())
//                .extension("apk")
//                .build()
//                .download();


    }


    /**
     * 第二种retrofit的使用
     */
    private void testRetrofit2() {

//        public interface PersonalProtocol {
//            /**
//             * 用户信息
//             * @param page
//             * @return
//             */
//            @FormUrlEncoded
//            @POST("user/personal_list_info")
//            Call<Response<PersonalInfo>> getPersonalListInfo(@Field("cur_page") int page);
//        }

//        Retrofit retrofit = new Retrofit.Builder().baseUrl("www.xxxx.com/").build();
//        PersonalProtocol personalProtocol = retrofit.create(PersonalProtocol.class);
//        Call<Response<PersonalInfo>> call = personalProtocol.getPersonalListInfo(12);
//        call.enqueue(new Callback<Response<PersonalInfo>>() {
//            @Override
//            public void onResponse(Call<Response<PersonalInfo>> call, Response<Response<PersonalInfo>> response) {
//                //数据请求成功
//            }
//
//            @Override
//            public void onFailure(Call<Response<PersonalInfo>> call, Throwable t) {
//                //数据请求失败
//            }
//        });
    }

}
