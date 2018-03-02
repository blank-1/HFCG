package com.xt.cfp.core.Exception.code.ext;

import com.xt.cfp.core.Exception.code.ErrorCode;

public enum SecurityErrorCode implements ErrorCode {

	ENCRYPT_FAIL_AES("0", "AES加密失败"),
    SIGN_FAIL_RSA("1", "RSA签名失败"),
	URLENCODE_FAIL("2", "URL编码失败"),
	DECRYPT_FAIL_RSA("3", "RSA解密失败"),
	VERIFY_FAIL_RSA("4", "RSA签名验证失败"),
	ACCESS_URL_INVAILD_NULL("5","请求的URI为NULL"),
	ACCESS_URL_INVAILD_ATTACH("6","请求的参数中Attach参数无效"),
	ACCESS_URL_INVAILD_EXTRA_PARAMS("7","请求的URI的附加路径无效"),
    CANNOT_LOAD_CERT("8", "加载证书失败");

    private String desc;
    private String code;

    private SecurityErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
