package com.xt.cfp.core.pojo;

import java.util.Date;

/**
 * Created by ren yulin on 15-7-18.
 */
public class IDCardVerified {
    private long verifiedId;
    private String realName;
    private String cardCode;
    private Date verifiedTime;
    private char verifiedResult;

    public long getVerifiedId() {
        return verifiedId;
    }

    public void setVerifiedId(long verifiedId) {
        this.verifiedId = verifiedId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Date getVerifiedTime() {
        return verifiedTime;
    }

    public void setVerifiedTime(Date verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public char getVerifiedResult() {
        return verifiedResult;
    }

    public void setVerifiedResult(char verifiedResult) {
        this.verifiedResult = verifiedResult;
    }
}
