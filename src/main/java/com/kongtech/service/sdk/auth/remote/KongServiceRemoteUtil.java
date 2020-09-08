package com.kongtech.service.sdk.auth.remote;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.kongtech.service.sdk.auth.model.BaseResponse;
import com.kongtech.service.sdk.auth.remote.interceptor.TokenAuthenticator;
import com.kongtech.service.sdk.auth.remote.interceptor.TokenInterceptor;
import com.kongtech.service.sdk.auth.remote.model.ServiceUser;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KongServiceRemoteUtil {

    private static final String BASE_URL = "https://kongservice.com/";

    private static final String PREF_ADMIN = "admin";

    private static KongServiceRemoteUtil instance;

    private KongServiceRemoteInterface remoteInterface;

    public static KongServiceRemoteUtil getInstance() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    public static KongServiceRemoteUtil init() {
        KongServiceRemoteUtil kongPassRemoteUtil = new KongServiceRemoteUtil();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new TokenInterceptor())
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .authenticator(new TokenAuthenticator())
//                .addInterceptor(interceptor)
                .build();

        kongPassRemoteUtil.remoteInterface = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(KongServiceRemoteInterface.class);

        return kongPassRemoteUtil;
    }

    public Flowable<BaseResponse<ServiceUser>> getUserInfo() {
        return remoteInterface.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
