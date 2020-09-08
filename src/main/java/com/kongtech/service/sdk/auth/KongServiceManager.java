package com.kongtech.service.sdk.auth;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.kongtech.service.sdk.auth.model.KongServiceError;
import com.kongtech.service.sdk.auth.model.TokenInfo;
import com.kongtech.service.sdk.auth.remote.KongServiceRemoteUtil;
import com.kongtech.service.sdk.auth.util.PrefUtil;

public class KongServiceManager {

    private static KongServiceManager instance;

    private TokenInfo tokenInfo;

    private Context context;

    public static KongServiceManager initialize(Context context) {
        instance = new KongServiceManager();
        instance.context = context;

        KongServiceRemoteUtil.init();

        return instance;
    }

    public static KongServiceManager getInstance() {
        if (instance == null || instance.context == null) {
            throw new NullPointerException("KongServiceManager must be initialize first.");
        }
        return instance;
    }

    private KongServiceManager() {

    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        String json = new Gson().toJson(tokenInfo);
        this.tokenInfo = tokenInfo;
        PrefUtil.saveStringPreference(context, PrefUtil.TOKEN_INFO, json);
    }

    public TokenInfo getTokenInfo() {
        if (tokenInfo == null) {
            String json = PrefUtil.loadStringPreference(context, PrefUtil.TOKEN_INFO);
            tokenInfo = new Gson().fromJson(json, TokenInfo.class);
        }
        return tokenInfo;
    }

    public String getAccessToken() {
        return tokenInfo != null ? tokenInfo.getAccessToken() : null;
    }

    public void setAccessToken(String token) {
        TokenInfo tokenInfo = new TokenInfo("Bearer " + token, "");
        setTokenInfo(tokenInfo);
    }

    @SuppressLint("CheckResult")
    public void me(ServiceUserCallback callback) {
        KongServiceRemoteUtil.getInstance().getUserInfo()
                .subscribe(response -> {
                    if (callback != null)
                        callback.serviceUserResult(response.getData(), null);
                }, error -> {
                    KongServiceError e = KongServiceError.networkError(error);


                });
    }
}
