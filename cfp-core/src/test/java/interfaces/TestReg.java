package interfaces;


import com.external.deposites.api.ApiParameter;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.PropertiesUtils;
import com.external.deposites.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class TestReg {
    private static Logger logger = LoggerFactory.getLogger(TestReg.class);

    @ApiParameter
    private String mchnt_cd = ""; // 商户代码
    @ApiParameter
    private String mchnt_txn_ssn = "";// 流水号
    @ApiParameter
    private String cust_nm = ""; // 注册企业名称
    @ApiParameter
    private String certif_id = ""; // 法人身份证号
    @ApiParameter
    private String mobile_no = ""; // 手机号
    @ApiParameter
    private String email = ""; // 邮箱地址
    @ApiParameter
    private String rem = ""; // 备注信息（企业号）
    @ApiParameter
    private String city_id = ""; // 开户区县代码
    @ApiParameter
    private String parent_bank_id = "";// 开户银行总行号
    @ApiParameter
    private String capAcntNo = ""; // 账号
    @ApiParameter
    private String capAcntNm = ""; // 账号户名
    @ApiParameter
    private String lpassword = ""; // 登录密码
    @ApiParameter
    private String password = ""; // 支付密码
    private String signature = ""; // 签名数据
    @ApiParameter
    private String bank_nm = "";
    @ApiParameter
    private String certif_tp;

    public String getBank_nm() {
        return bank_nm;
    }

    public void setBank_nm(String bank_nm) {
        this.bank_nm = bank_nm;
    }

    // 其他get\set方法省略
    // .......
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

    public String getCust_nm() {
        return cust_nm;
    }

    public void setCust_nm(String cust_nm) {
        this.cust_nm = cust_nm;
    }

    public String getCertif_id() {
        return certif_id;
    }

    public void setCertif_id(String certif_id) {
        this.certif_id = certif_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRem() {
        return rem;
    }

    public void setRem(String rem) {
        this.rem = rem;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getParent_bank_id() {
        return parent_bank_id;
    }

    public void setParent_bank_id(String parent_bank_id) {
        this.parent_bank_id = parent_bank_id;
    }

    public String getCapAcntNo() {
        return capAcntNo;
    }

    public void setCapAcntNo(String capAcntNo) {
        this.capAcntNo = capAcntNo;
    }

    public String getCapAcntNm() {
        return capAcntNm;
    }

    public void setCapAcntNm(String capAcntNm) {
        this.capAcntNm = capAcntNm;
    }

    public String getLpassword() {
        return lpassword;
    }

    public void setLpassword(String lpassword) {
        this.lpassword = lpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    /**
     * 注册时请求的明文
     *
     * @return
     */
    public String regSignVal() {
        String src = bank_nm + "|" + capAcntNm + "|" + capAcntNo + "|" + certif_id + "|" + city_id + "|" + cust_nm + "|"
                + email + "|" + lpassword + "|" + mchnt_cd + "|" + mchnt_txn_ssn + "|" + mobile_no + "|"
                + parent_bank_id + "|" + password + "|" + rem;
        // String[] fields = new String[] {};

        logger.debug("bank_nm:" + bank_nm + "|capAcntNm:" + capAcntNm + "|capAcntNo:" + capAcntNo + "|certif_id:"
                + certif_id + "|city_id:" + city_id + "|cust_nm:" + cust_nm + "|email:" + email + "|lpassword:"
                + lpassword + "|mchnt_cd:" + mchnt_cd + "|mchnt_txn_ssn:" + mchnt_txn_ssn + "|mobile_no:" + mobile_no
                + "|parent_bank_id:" + parent_bank_id + "|password:" + password + "|rem:" + rem);
        System.out.println(src);
        return src;
    }

    public String regSignVal2() throws IllegalAccessException {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        List<String> paramSet = new ArrayList<>(declaredFields.length);

        Map<String, String> nameValue = new HashMap<>(declaredFields.length);
        for (Field field : declaredFields) {
            ApiParameter apiParameter = field.getAnnotation(ApiParameter.class);
            if (apiParameter == null) {
                continue;
            }
            if (field.get(this) == null) {
                continue;
            }
            paramSet.add(field.getName());
            nameValue.put(field.getName(), field.get(this).toString());
        }
        paramSet.add("ver");
        nameValue.put("ver", PropertiesUtils.property("hf-config", "cg.hf.ver"));
        Collections.sort(paramSet);

        StringBuilder sb = new StringBuilder();
        for (String field : paramSet) {
            sb.append(nameValue.get(field)).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb.toString());
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
        // 注册请求
        TestReg regData = new TestReg();
        // 设值
        // 密码需md5加密，如：MD5Util.encode("密码明文,如888888", "UTF-8");
        // regData.setCapAcntNm("xxx");
        // regData.setCapAcntNo("xxx");
        // ....
//        regData.setBank_nm("中国农业银行");
//        regData.setCapAcntNm("");
        regData.setCapAcntNo("62284803111111111111");
        regData.setCertif_tp("1");
        regData.setCertif_id("330382111111010110");
        regData.setCity_id("3333");
        regData.setCust_nm("小明");
//        regData.setEmail("");
        regData.setLpassword("97b149a269795ef98a7e31b66d1f105e");
        regData.setMchnt_cd("0002900F0041077");
        regData.setMchnt_txn_ssn("96f14200a794dbcc91cad69b50ef0f");
        regData.setMobile_no("11111111111");
        regData.setParent_bank_id("0103");
//        regData.setPassword("97b149a269795ef98a7e31b66d1f105e");
//        regData.setRem("");
        // 请求明文

        String inputStr = regData.regSignVal2();
        logger.debug("请求明文:" + inputStr);
        // 密文(私钥加密)
//        String signatureStr = SecurityUtils.sign(inputStr);
        // String signatureStr =
        // SecurityUtils.sign("0002900F0338384|20110519|201503021425261790937|13980566277|18521084585");
        // regData.setSignature(signatureStr);
//        logger.debug("请求密文:" + signatureStr);
        String backStr = HfUtils.sendHttp("http://cg.zjcgxt.com/jzh/reg.action", regData);
        logger.debug("接口返回的串：" + backStr);
        String retPlainStr = backStr.substring(backStr.indexOf("<plain>"), backStr.indexOf("</plain>") + 8);
        String retSignatureStr = backStr.substring(backStr.indexOf("<signature>") + 11,
                backStr.indexOf("</signature>"));
        boolean b = SecurityUtils.verifySign(retPlainStr, retSignatureStr);// 验签结果

        logger.debug("==== 是否成功：" + b);
    }

    public void setCertif_tp(String certif_tp) {
        this.certif_tp = certif_tp;
    }

    public String getCertif_tp() {
        return certif_tp;
    }
}
