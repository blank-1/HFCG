package com.xt.cfp.core.pojo;

public class CityInfo {
    private Long cityId;

    private Long provinceId;

    private String cityName;

    private Long pCityId;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public Long getpCityId() {
        return pCityId;
    }

    public void setpCityId(Long pCityId) {
        this.pCityId = pCityId;
    }
}