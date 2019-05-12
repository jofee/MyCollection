package com.fk.mycollection.webservice;

import com.fk.mycollection.Constant;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpManager {
    public static final String SERVER_URL = Constant.serviceUrl;
    private static final int DEFAULT_TIMEOUT = 30;
    private static HttpManager mInstance;
    private Retrofit mRetrofit;
    private WebAPI mWebAPI;

    /*
     * 单例模式，创建实例
     */
    public static synchronized HttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new HttpManager();
        }
        return mInstance;
    }

    private HttpManager() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(new GsonBuilder().create()))

                .client(genericClient()).build();
    }

    public OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain
                                .request()
                                .newBuilder()
                                .addHeader("Content-Type",
                                        "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Cookie", "add cookies here")
                                .build();
                        return chain.proceed(request);
                    }

                })
//                .retryOnConnectionFailure(false)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    public synchronized WebAPI createService() {
        if (mWebAPI == null) {
            mWebAPI = mRetrofit.create(WebAPI.class);
        }
        return mWebAPI;
    }

}