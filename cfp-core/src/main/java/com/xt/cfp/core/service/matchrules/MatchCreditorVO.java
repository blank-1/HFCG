package com.xt.cfp.core.service.matchrules;

import java.math.BigDecimal;

/**
 * Created by ren yulin on 15-7-11.
 */
public class MatchCreditorVO {
    //是否是债券
    private boolean isCreditorRights;
    //是否是渠道
    private boolean isFromChannel;
    //匹配的id（包扩债权申请id，债权id，和标的id）
    private long theId;
    //匹配的金额
    private BigDecimal balance;
    

    public boolean isFromChannel() {
        return isFromChannel;
    }

    public void setFromChannel(boolean isFromChannel) {
        this.isFromChannel = isFromChannel;
    }

    public boolean isCreditorRights() {
        return isCreditorRights;
    }

    public void setCreditorRights(boolean isCreditorRights) {
        this.isCreditorRights = isCreditorRights;
    }

    public long getTheId() {
        return theId;
    }

    public void setTheId(long theId) {
        this.theId = theId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
