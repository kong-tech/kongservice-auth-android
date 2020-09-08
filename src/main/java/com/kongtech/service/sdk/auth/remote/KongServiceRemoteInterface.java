package com.kongtech.service.sdk.auth.remote;

import com.kongtech.service.sdk.auth.model.BaseResponse;
import com.kongtech.service.sdk.auth.remote.model.ServiceUser;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface KongServiceRemoteInterface {
    @GET("api/v1/accounts/me")
    Flowable<BaseResponse<ServiceUser>> getUserInfo();
}
