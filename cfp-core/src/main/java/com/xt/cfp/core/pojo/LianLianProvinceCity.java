package com.xt.cfp.core.pojo;

public class LianLianProvinceCity {
    private Long provinceCityId;//省市ID
    private String provinceName;//省名称
    private String cityName;//市名称
    private String provinceCityCode;//省市编码
    private Long pProvinceCityId;//父ID

    public Long getProvinceCityId() {
        return provinceCityId;
    }

    public void setProvinceCityId(Long provinceCityId) {
        this.provinceCityId = provinceCityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName == null ? null : provinceName.trim();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public String getProvinceCityCode() {
        return provinceCityCode;
    }

    public void setProvinceCityCode(String provinceCityCode) {
        this.provinceCityCode = provinceCityCode == null ? null : provinceCityCode.trim();
    }

    public Long getpProvinceCityId() {
        return pProvinceCityId;
    }

    public void setpProvinceCityId(Long pProvinceCityId) {
        this.pProvinceCityId = pProvinceCityId;
    }
}