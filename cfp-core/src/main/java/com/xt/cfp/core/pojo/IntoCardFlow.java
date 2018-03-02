package com.xt.cfp.core.pojo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renyulin on 14-4-17 下午5:11.
 */
public class IntoCardFlow {
    private long intoCardFlowId;
    private long customerId;
    private Date intoTime;
    private String serialNumber;
    private String bankName;
    private String countryName;
    private String provinceName;
    private String cityName;
    private String subCityName;
    private String subBankName;
    private String cardCode;
    private String customerName;
    private char intoResult;
    private char intoChannel;
    private BigDecimal intoBalance;

    /**
     * 打款状态：0为进行中，1为打款完成，2为失败
     */
    public static final char INTORESULT_PROCESSING = '0';
    public static final char INTORESULT_COMPLETED = '1';
    public static final char INTORESULT_FAILURE = '2';

    public String getSubCityName() {
        return subCityName;
    }

    public void setSubCityName(String subCityName) {
        this.subCityName = subCityName;
    }

    public BigDecimal getIntoBalance() {
        return intoBalance;
    }

    public void setIntoBalance(BigDecimal intoBalance) {
        this.intoBalance = intoBalance;
    }

    public char getIntoChannel() {
        return intoChannel;
    }

    public void setIntoChannel(char intoChannel) {
        this.intoChannel = intoChannel;
    }

    public long getIntoCardFlowId() {
        return intoCardFlowId;
    }

    public void setIntoCardFlowId(long intoCardFlowId) {
        this.intoCardFlowId = intoCardFlowId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Date getIntoTime() {
        return intoTime;
    }

    public void setIntoTime(Date intoTime) {
        this.intoTime = intoTime;
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

    public char getIntoResult() {
        return intoResult;
    }

    public void setIntoResult(char intoResult) {
        this.intoResult = intoResult;
    }
}
