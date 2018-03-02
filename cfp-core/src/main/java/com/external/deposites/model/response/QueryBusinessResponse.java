package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * 查询充值信息响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/20
 */
public class QueryBusinessResponse extends AbstractResponse {
    @ApiParameter
    private String busi_tp; // 业务类型
    @ApiParameter
    private String total_number; //总记录数

    private Set<QueryBusinessResponseItem> results = new HashSet<>();

    public String getBusi_tp() {
        return busi_tp;
    }

    public void setBusi_tp(String busi_tp) {
        this.busi_tp = busi_tp;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

    public Set<QueryBusinessResponseItem> getResults() {
        return results;
    }

    public void setResults(Set<QueryBusinessResponseItem> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "QueryRechargeResponse{" +
                "busi_tp='" + busi_tp + '\'' +
                ", total_number='" + total_number + '\'' +
                ", results=" + results +
                "} " + super.toString();
    }
}
