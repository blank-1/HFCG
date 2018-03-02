package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.SalesAdminUserInfo;

/**
 * Created by lenovo on 2015/10/16.
 */
public class SalesAdminUserInfoVo  extends SalesAdminUserInfo {
    private String adminCode;
    private String displayName;
    private String LoginName;

    public String getAdminCode() {
        return adminCode;
    }

    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }
}
