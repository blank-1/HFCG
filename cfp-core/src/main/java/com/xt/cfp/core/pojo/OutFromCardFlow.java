package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-4-17 下午5:34.
 */
public class OutFromCardFlow {
    private long outFromCardFlowId;
    private Date outTime;
    private long customerId;
    private String serialNumber;
    private String bankName;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String subCityName;
    private String subBankName;
    private String cardCode;
    private String customerName;
    private char outResult;
    private char outChannel;
    private BigDecimal outBalance;


    /**
     * 划扣状态：0为进行中，1为划扣完成，2为失败
     */
    public static final char OUTRESULT_PROCESSING = '0';
    public static final char OUTRESULT_COMPLETED = '1';
    public static final char OUTRESULT_FAILURE = '2';

    public String getSubCityName() {
        return subCityName;
    }

    public void setSubCityName(String subCityName) {
        this.subCityName = subCityName;
    }

    public BigDecimal getOutBalance() {
        return outBalance;
    }

    public void setOutBalance(BigDecimal outBalance) {
        this.outBalance = outBalance;
    }

    public char getOutChannel() {
        return outChannel;
    }

    public void setOutChannel(char outChannel) {
        this.outChannel = outChannel;
    }

    public long getOutFromCardFlowId() {
        return outFromCardFlowId;
    }

    public void setOutFromCardFlowId(long outFromCardFlowId) {
        this.outFromCardFlowId = outFromCardFlowId;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSubBankName() {
        return subBankName;
    }

    public void setSubBankName(String subBankName) {
        this.subBankName = subBankName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public char getOutResult() {
        return outResult;
    }

    public void setOutResult(char outResult) {
        this.outResult = outResult;
    }
}
