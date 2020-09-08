package com.kongtech.service.sdk.auth.remote.interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kongtech.service.sdk.auth.KongServiceManager;
import com.kongtech.service.sdk.auth.model.TokenInfo;

import java.io.IOException;

import androidx.annotation.Nullable;
import okhttp3.Authenticator;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    private static final String TAG = "TokenAuthenticator";

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        TokenInfo tokenInfo = getNewToken();
        if (tokenInfo != null) {
            KongServiceManager.getInstance().setTokenInfo(tokenInfo);
            return response.request().newBuilder()
                    .header("Authorization", tokenInfo.getAccessToken())
                    .build();
        }
        return null;
    }

    private TokenInfo getNewToken() {
        Log.d(TAG, "Request refresh Token");
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("refreshToken", KongServiceManager.getInstance().getTokenInfo().getRefreshToken());

        Request request = new Request.Builder()
                .url("https://kongservice.com/api/v1/tokens/refresh")
                .post(formBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
                JsonObject root = (JsonObject) jsonObject.get("data");
                String accessToken = "Bearer " + root.get("accessToken").getAsString();
                String refreshToken = root.get("refreshToken").getAsString();

                return new TokenInfo(accessToken, refreshToken);
            } else {
                Log.e(TAG, "fail: " + response.body().string());
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "fail: " + e.toString());
            e.printStackTrace();
        }
        return null;
    }
}
