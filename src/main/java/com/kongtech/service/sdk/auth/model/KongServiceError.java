package com.kongtech.service.sdk.auth.model;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

public enum  KongServiceError {
    NETWORK_ERROR,
    SERVER_ERROR,
    BAD_REQUEST,
    UNAUTHORIZED,
    NO_PERMISSION,
    ;

    public static KongServiceError networkError(Throwable error) {
        KongServiceError result = NETWORK_ERROR;
        if (error instanceof HttpException) {
            HttpException httpException = (HttpException) error;
            switch (httpException.code()){
                case 400:
                    result = KongServiceError.BAD_REQUEST;
                    break;
                case 401:
                    result = KongServiceError.UNAUTHORIZED;
                    break;
                case 403:
                    result = KongServiceError.NO_PERMISSION;
                    break;
                case 500:
                    result = KongServiceError.SERVER_ERROR;
                    break;
            }
        }
        return result;
    }
}
