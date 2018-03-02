package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.model.AbstractSignature;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.utils.AnalysisXMLResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.SecurityUtils;
import com.external.deposites.utils.flowApi.Http;
import com.xt.cfp.core.util.StringUtils;

/**
 * <pre>
 * 通用响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public abstract class AbstractResponse extends AbstractSignature implements IResponse {

    @ApiParameter
    private String mchnt_cd;
    @ApiParameter
    private String mchnt_txn_ssn;
    @ApiParameter
    private String resp_code;
    /**
     * 这个字段不要加注解，有些验签是不需要这个字段的！
     * 还有，这是个同用类！
     * 可要这个字段参与验签就在实现类中加上这个字段，这样影响最小
     */
    private String resp_desc;

    public String getMchnt_cd() {
        return mchnt_cd;
    }

    @Override
    public void setMchnt_cd(String mchnt_cd) {
        this.mchnt_cd = mchnt_cd;
    }

    public String getMchnt_txn_ssn() {
        return mchnt_txn_ssn;
    }

    @Override
    public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
        this.mchnt_txn_ssn = mchnt_txn_ssn;
    }

    public String getResp_code() {
        return resp_code;
    }

    @Override
    public void setResp_code(String resp_code) {
        this.resp_code = resp_code;
    }

    public String getResp_desc() {
        return StringUtils.isNull(resp_desc) ? HfUtils.codeMapper().code(this.resp_code) : resp_desc;
    }

    @Override
    public void setResp_desc(String resp_desc) {
        this.resp_desc = resp_desc;
    }

    public boolean isSuccess() {
        return "0000".equals(this.getResp_code());
    }

    @Override
    public String toString() {
        return "AbstractResponse{" +
                "mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", resp_code='" + resp_code + '\'' +
                ", resp_desc='" + resp_desc + '\'' +
                "} " + super.toString();
    }
}
