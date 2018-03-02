package com.external.deposites.model.response;

import java.io.Serializable;

/**
 * <pre>
 * 响应体
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public interface IResponse extends Serializable{
    /**
     * 响应code
     */
    void setResp_code(String respCode);

    /**
     * 响应说明
     */
    void setResp_desc(String respDesc);

    /**
     * 签名数据
     */
    void setSignature(String signature);

    /**
     * 商户代码 code
     */
    void setMchnt_cd(String mchnt_cd);

    /**
     * 商户流水号
     */
    void setMchnt_txn_ssn(String mchnt_txn_ssn);

    /**
     * 内部接口码
     */
    enum InternalCode {
        ERROR
    }

    /**
     * 响应类型
     */
    enum ResponseType {
        XML
    }
}
