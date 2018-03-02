package com.xt.cfp.core.pojo.ext;

/**
 * Created by luqinglin on 2015/7/14.
 */
public class WithDrawParams {

    private String merchantaccount;//商户编号
    private String requestid;  //商户请求号（提现订单号）
    private String identityid;//用户标识（userId）
    private int identitytype=5;//默认身份证号
    private String card_top;//卡号前6位
    private String card_last;//卡号后4位
    private int amount;//提现金额（单位分）
    private String drawtype;//提现类型
    private String userip;//用户ip
    private String sign; //签名


    public String getMerchantaccount() {
        return merchantaccount;
    }

    public void setMerchantaccount(String merchantaccount) {
        this.merchantaccount = merchantaccount;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getIdentityid() {
        return identityid;
    }

    public void setIdentityid(String identityid) {
        this.identityid = identityid;
    }

    public int getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(int identitytype) {
        this.identitytype = identitytype;
    }

    public String getCard_top() {
        return card_top;
    }

    public void setCard_top(String card_top) {
        this.card_top = card_top;
    }

    public String getCard_last() {
        return card_last;
    }

    public void setCard_last(String card_last) {
        this.card_last = card_last;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDrawtype() {
        return drawtype;
    }

    public void setDrawtype(String drawtype) {
        this.drawtype = drawtype;
    }

    public String getUserip() {
        return userip;
    }

    public void setUserip(String userip) {
        this.userip = userip;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
