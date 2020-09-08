package com.kongtech.service.sdk.auth.remote.interceptor;

import com.kongtech.service.sdk.auth.KongServiceManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();

        try {
            builder.addHeader("Authorization", KongServiceManager.getInstance().getTokenInfo().getAccessToken());
        } catch (Exception ignored) {

        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
