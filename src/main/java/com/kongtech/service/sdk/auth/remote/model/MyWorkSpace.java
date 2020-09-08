package com.kongtech.service.sdk.auth.remote.model;

import java.util.ArrayList;
import java.util.List;

public class MyWorkSpace {

    private long userId;

    private String emailAddress;

    private String username;

    private String subdomainName;

    private String workspaceName;

    private List<KongService> kongServiceType = new ArrayList<>();

    public long getUserId() {
        return userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getSubdomainName() {
        return subdomainName;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public List<KongService> getKongServiceType() {
        return kongServiceType;
    }
}
