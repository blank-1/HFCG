package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.Address;

/**
 * Created by lenovo on 2015/7/9.
 */
public class AddressVO extends Address {
    private String provinceStr;//省
    private String cityStr;//市
    private String districtStr;//区

    public String getProvinceStr() {
        return provinceStr;
    }

    public void setProvinceStr(String provinceStr) {
        this.provinceStr = provinceStr;
    }

    public String getCityStr() {
        return cityStr;
    }

    public void setCityStr(String cityStr) {
        this.cityStr = cityStr;
    }

    public String getDistrictStr() {
        return districtStr;
    }

    public void setDistrictStr(String districtStr) {
        this.districtStr = districtStr;
    }

}
