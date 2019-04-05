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
import com.diabin.latte.net.callback.IError;
import com.diabin.latte.net.callback.IFailure;
import com.diabin.latte.net.callback.ISuccess;

public class ExampleDelegate extends LatteDelegate {
    @Override
    public Object setLayout() {
        return R.layout.delegate_example;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        test();
    }

    //单次请求测试
    private void test() {
        RestClient.Builder().url("http://news.baidu.com")
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
//                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
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
    }


    /**
     * 第二种retrofit的使用
     */
    private void testRetrofit2(){

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
