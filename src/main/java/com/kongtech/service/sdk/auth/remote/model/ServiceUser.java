package com.kongtech.service.sdk.auth.remote.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    private String phoneNumber;

    private String userName;

    private List<MyWorkSpace> workspaces = new ArrayList<>();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public List<MyWorkSpace> getWorkspaces() {
        return workspaces;
    }
}
