package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 重置密码
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/11
 */
public class ResetPasswordDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String login_id;
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String busi_tp;
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String back_url;
    private String actionUrl;


    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getMchnt_cd() {
        return mchnt_cd;
    }

    public void setMchnt_cd(String mchnt_cd) {
        this.mchnt_cd = mchnt_cd;
    }

    public String getMchnt_txn_ssn() {
        return mchnt_txn_ssn;
    }

    public void setMchnt_txn_ssn(String mchnt_txn_ssn) {
        this.mchnt_txn_ssn = mchnt_txn_ssn;
    }

    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    public String getBusi_tp() {
        return busi_tp;
    }

    public void setBusi_tp(String busi_tp) {
        this.busi_tp = busi_tp;
    }

    public String getBack_url() {
        return back_url;
    }

    public void setBack_url(String back_url) {
        this.back_url = back_url;
    }

    @Override
    public String toString() {
        return "ResetPasswordDataSource{" +
                "mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", login_id='" + login_id + '\'' +
                ", busi_tp='" + busi_tp + '\'' +
                ", back_url='" + back_url + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                "} " + super.toString();
    }
}
