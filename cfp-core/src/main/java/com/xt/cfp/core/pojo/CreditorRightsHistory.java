package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-5-16 上午10:57.
 */
public class CreditorRightsHistory {
    private long creditorRightsHistoryId;
    private long creditorRightsIdBeforeChange;
    private long creditorRightsIdAfterChange;

    private char changeType;
    private BigDecimal changeBalance;

    private BigDecimal rightsWorthAfterChange;
    private BigDecimal rightsWorthBeforeChange;

    private BigDecimal lendPriceBeforeChange;
    private BigDecimal lendPriceAfterChange;

    private BigDecimal annualRateBeforeChange;
    private BigDecimal annualRateAfterChange;

    private int repaymentCycleBeforeChange;
    private int repaymentCycleAftreChange;

    private char rightsStateBeforeChange;
    private char rightsStateAftreChange;

    private char displayStateBeforeChange;
    private char displayStateAfterChange;

    private char isDelayBeforeChange;
    private char isDelayAfterChnage;

    private Date changeTime;

    /**
     * 变更类型：1为转让
     */
    public static final char CHANGETYPE_TURNOUT='1';
    public static final char CHANGETYPE_TURNOUTTOSYSTEM='2';

    public long getCreditorRightsHistoryId() {
        return creditorRightsHistoryId;
    }

    public void setCreditorRightsHistoryId(long creditorRightsHistoryId) {
        this.creditorRightsHistoryId = creditorRightsHistoryId;
    }

    public long getCreditorRightsIdBeforeChange() {
        return creditorRightsIdBeforeChange;
    }

    public void setCreditorRightsIdBeforeChange(long creditorRightsIdBeforeChange) {
        this.creditorRightsIdBeforeChange = creditorRightsIdBeforeChange;
    }

    public long getCreditorRightsIdAfterChange() {
        return creditorRightsIdAfterChange;
    }

    public void setCreditorRightsIdAfterChange(long creditorRightsIdAfterChange) {
        this.creditorRightsIdAfterChange = creditorRightsIdAfterChange;
    }

    public char getChangeType() {
        return changeType;
    }

    public void setChangeType(char changeType) {
        this.changeType = changeType;
    }

    public BigDecimal getChangeBalance() {
        return changeBalance;
    }

    public void setChangeBalance(BigDecimal changeBalance) {
        this.changeBalance = changeBalance;
    }

    public BigDecimal getRightsWorthAfterChange() {
        return rightsWorthAfterChange;
    }

    public void setRightsWorthAfterChange(BigDecimal rightsWorthAfterChange) {
        this.rightsWorthAfterChange = rightsWorthAfterChange;
    }

    public BigDecimal getRightsWorthBeforeChange() {
        return rightsWorthBeforeChange;
    }

    public void setRightsWorthBeforeChange(BigDecimal rightsWorthBeforeChange) {
        this.rightsWorthBeforeChange = rightsWorthBeforeChange;
    }

    public BigDecimal getLendPriceBeforeChange() {
        return lendPriceBeforeChange;
    }

    public void setLendPriceBeforeChange(BigDecimal lendPriceBeforeChange) {
        this.lendPriceBeforeChange = lendPriceBeforeChange;
    }

    public BigDecimal getLendPriceAfterChange() {
        return lendPriceAfterChange;
    }

    public void setLendPriceAfterChange(BigDecimal lendPriceAfterChange) {
        this.lendPriceAfterChange = lendPriceAfterChange;
    }

    public BigDecimal getAnnualRateBeforeChange() {
        return annualRateBeforeChange;
    }

    public void setAnnualRateBeforeChange(BigDecimal annualRateBeforeChange) {
        this.annualRateBeforeChange = annualRateBeforeChange;
    }

    public BigDecimal getAnnualRateAfterChange() {
        return annualRateAfterChange;
    }

    public void setAnnualRateAfterChange(BigDecimal annualRateAfterChange) {
        this.annualRateAfterChange = annualRateAfterChange;
    }

    public int getRepaymentCycleBeforeChange() {
        return repaymentCycleBeforeChange;
    }

    public void setRepaymentCycleBeforeChange(int repaymentCycleBeforeChange) {
        this.repaymentCycleBeforeChange = repaymentCycleBeforeChange;
    }

    public int getRepaymentCycleAftreChange() {
        return repaymentCycleAftreChange;
    }

    public void setRepaymentCycleAftreChange(int repaymentCycleAftreChange) {
        this.repaymentCycleAftreChange = repaymentCycleAftreChange;
    }

    public char getRightsStateBeforeChange() {
        return rightsStateBeforeChange;
    }

    public void setRightsStateBeforeChange(char rightsStateBeforeChange) {
        this.rightsStateBeforeChange = rightsStateBeforeChange;
    }

    public char getRightsStateAftreChange() {
        return rightsStateAftreChange;
    }

    public void setRightsStateAftreChange(char rightsStateAftreChange) {
        this.rightsStateAftreChange = rightsStateAftreChange;
    }

    public char getDisplayStateBeforeChange() {
        return displayStateBeforeChange;
    }

    public void setDisplayStateBeforeChange(char displayStateBeforeChange) {
        this.displayStateBeforeChange = displayStateBeforeChange;
    }

    public char getDisplayStateAfterChange() {
        return displayStateAfterChange;
    }

    public void setDisplayStateAfterChange(char displayStateAfterChange) {
        this.displayStateAfterChange = displayStateAfterChange;
    }

    public char getIsDelayBeforeChange() {
        return isDelayBeforeChange;
    }

    public void setIsDelayBeforeChange(char isDelayBeforeChange) {
        this.isDelayBeforeChange = isDelayBeforeChange;
    }

    public char getIsDelayAfterChnage() {
        return isDelayAfterChnage;
    }

    public void setIsDelayAfterChnage(char isDelayAfterChnage) {
        this.isDelayAfterChnage = isDelayAfterChnage;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}
