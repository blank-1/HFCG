package com.external.deposites.model.datasource;

import com.external.deposites.api.ApiParameter;
import com.external.deposites.api.HfValidations;
import com.external.deposites.api.Validated;

/**
 * <pre>
 * 充值
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/24
 */
public class Ebank2RechargeDataSource extends AbstractDataSource {
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_cd; // 15 商户代码
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String mchnt_txn_ssn; // 30 流水号
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String login_id; // 60 用户登录名
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private Long amt; // 12 以分为单位 (无小数位)
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String page_notify_url; // 商户返回地址
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String back_notify_url; // 商户后台通知 地址
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String order_pay_type; // 3 支付类型
    @ApiParameter
    @Validated(type = {HfValidations.BasicValidation.NotNull})
    private String iss_ins_cd;     // 10 银行代码 目前测试支持: 个人(B2C)网银:兴业银 行 0803090000
    @ApiParameter
    private String signature; // 签名信息 按参数名字母排序后的值 用“|”线连接起来的明文， 然后用 rsa 签名的值
    private String actionUrl;

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public Long getAmt() {
        return amt;
    }

    public void setAmt(Long amt) {
        this.amt = amt;
    }

    public String getPage_notify_url() {
        return page_notify_url;
    }

    public void setPage_notify_url(String page_notify_url) {
        this.page_notify_url = page_notify_url;
    }

    public String getBack_notify_url() {
        return back_notify_url;
    }

    public void setBack_notify_url(String back_notify_url) {
        this.back_notify_url = back_notify_url;
    }

    public String getIss_ins_cd() {
        return iss_ins_cd;
    }

    public void setIss_ins_cd(String iss_ins_cd) {
        this.iss_ins_cd = iss_ins_cd;
    }

    public String getOrder_pay_type() {
        return order_pay_type;
    }

    public void setOrder_pay_type(String order_pay_type) {
        OrderPayType.valueOf(order_pay_type);
        this.order_pay_type = order_pay_type;
    }

    @Override
    public String toString() {
        return "Ebank2RechargeDataSource{" +
                "mchnt_cd='" + mchnt_cd + '\'' +
                ", mchnt_txn_ssn='" + mchnt_txn_ssn + '\'' +
                ", login_id='" + login_id + '\'' +
                ", amt=" + amt +
                ", page_notify_url='" + page_notify_url + '\'' +
                ", back_notify_url='" + back_notify_url + '\'' +
                ", order_pay_type='" + order_pay_type + '\'' +
                ", iss_ins_cd='" + iss_ins_cd + '\'' +
                ", signature='" + signature + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                "} " + super.toString();
    }

    enum OrderPayType {
        B2C("个人支付"), B2B("企业支付");

        private final String desc;

        OrderPayType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
