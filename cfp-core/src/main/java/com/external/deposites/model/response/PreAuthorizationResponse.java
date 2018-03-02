package com.external.deposites.model.response;

import com.external.deposites.api.ApiParameter;

/**
 * <pre>
 * 预授权
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class PreAuthorizationResponse extends AbstractResponse {

    @ApiParameter
    private String contract_no; //预授权合同号

    public String getContract_no() {
        return contract_no;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    @Override
    public String toString() {
        return "PreAuthorizationResponse{" +
                "contract_no='" + contract_no + '\'' +
                "} " + super.toString();
    }
}
