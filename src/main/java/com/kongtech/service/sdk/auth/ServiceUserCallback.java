package com.kongtech.service.sdk.auth;

import com.kongtech.service.sdk.auth.model.KongServiceError;
import com.kongtech.service.sdk.auth.remote.model.ServiceUser;

public interface ServiceUserCallback {
    void serviceUserResult(ServiceUser user, KongServiceError error);
}
