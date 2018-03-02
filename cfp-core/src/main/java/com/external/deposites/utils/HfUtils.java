package com.external.deposites.utils;

import com.external.deposites.exception.UnimplementException;
import com.external.deposites.model.CodeMapper;
import com.external.deposites.model.response.AbstractResponse;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.utils.flowApi.Http;
import com.xt.cfp.core.Exception.UnCatchedException;
import com.xt.cfp.core.service.redis.RedisCacheManger;
import com.xt.cfp.core.util.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <pre>
 * 恒丰存管 工具包
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/20
 */
public final class HfUtils {
    private static Logger logger = LoggerFactory.getLogger(HfUtils.class);
    private static SimpleDateFormat dateSerialStr = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private HfUtils() {
    }

    public static String getUniqueSerialNum() {
        // 具本再定
//        RedisCacheManger redisCacheManger = ApplicationContextUtil.getBean(RedisCacheManger.class);
        String format = dateSerialStr.format(new Date());

//        long dayOfUniqueSerial = redisCacheManger.setIncreaseValidTime("unique_serial:" + format, 25 * 3600);
        DecimalFormat df = new DecimalFormat("0000");

//        String code = (format + "NO" + df.format(dayOfUniqueSerial)).replaceAll("[-]", "");
        String code = (UUID.randomUUID().toString().replaceAll("[-]", "")).substring(0, 29);
        logger.debug("getUniqueSerialNum() ==>{}", code);
        return code;
    }


    public static HfAddresses address() {
        return new HfAddresses();
    }

    public static String bean2xml(boolean ok) {
        //todo
        throw new UnimplementException("没有实现的方法");
    }

    public static <T extends IResponse> T xml2Bean(String xmlStr, Class<? extends IResponse> clazz, String... arrayFields) {
        try {
            return (T) JsonUtil.toBean(XMLUtil.xml2json(xmlStr, arrayFields), clazz);
        } catch (Exception e) {
            try {
                IResponse iResponse = clazz.newInstance();
                iResponse.setResp_code(IResponse.InternalCode.ERROR.name());
                iResponse.setResp_desc("xml cast to java bean error: " + e.getMessage());
                return (T) iResponse;
            } catch (InstantiationException | IllegalAccessException e1) {
                logger.error("响应体bean需要有默认构造方法：" + e.getMessage(), e);
                throw new UnCatchedException(e1.getMessage(), e);
            }
        }

    }


    public static Http http() {
        return new Http();
    }

    public static CodeMapper codeMapper() {
        return new CodeMapper();
    }

    /**
     * 返回一个格式良好的http[s] url.
     *
     * @param distUrl 要处理的url
     * @param domain  要为disUrl 加的domain,可以为空，为空时取系统的
     * @see #niceUrl(String)
     */
    public static String niceUrl(String distUrl, String domain) {
        if (StringUtils.isNull(distUrl)) {
            return distUrl;
        }
        if (isNiceUrl(distUrl)) {
            return distUrl;
        }

        if (StringUtils.isNull(domain)) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            domain = request.getProtocol() + ":" + request.getServerPort();
        }
        if (!distUrl.startsWith("/")) {
            distUrl += "/" + distUrl;
        }
        return domain + distUrl;
    }

    /**
     * 返回一个格式良好的http[s] url.
     *
     * @see #niceUrl(String, String)
     */
    public static String niceUrl(String distUrl) {
        return niceUrl(distUrl, null);
    }

    /**
     * 是否是一个良好的url格式
     */
    private static boolean isNiceUrl(String distUrl) {
        String protocol1 = "http://";
        String protocol2 = "https://";
        return !StringUtils.isNull(distUrl)
                && distUrl.length() > protocol2.length() + 1
                && (protocol1.equalsIgnoreCase(distUrl.substring(0, protocol1.length()))
                || protocol2.equalsIgnoreCase(distUrl.substring(0, protocol2.length())));
    }

    public static String sendHttp(String url, Object parameters) throws Exception {
        String outStr = "";
        try {
            String charSet = "UTF-8";
            outStr = HttpClientHelper.doHttp(url, charSet, parameters, 10000);
            logger.info("request : {}", url);
            logger.info("response : {}", outStr);
            if (outStr == null) {
                throw new Exception("请求接口失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("请求接口失败!");
        }
        return outStr;
    }

    /**
     * 接口 用户身份类型
     */
    public enum CertifTpEnum {
        ID_CARD(0, "身份证"),
        OTHER(7, "其它");

        private final int code;
        private final String desc;

        CertifTpEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private static Map<Integer, CertifTpEnum> cached = new HashMap<>();

        static {
            for (CertifTpEnum certifTpEnum : CertifTpEnum.values()) {
                cached.put(certifTpEnum.code, certifTpEnum);
            }
        }

        public static CertifTpEnum typeOf(int code) {
            return cached.get(code);
        }

        public int code() {
            return code;
        }

        public String desc() {
            return desc;
        }
    }

    /**
     * 异步回调 响应
     * @param isOK 业务处理是否成功
     * @return xml
     */
    public static String response(boolean isOK) {
        String signStr = String.format("<plain>" +
                        "<resp_code>%s</resp_code>" +
                        "<mchnt_cd>%s</mchnt_cd>" +
                        "<mchnt_txn_ssn>%s</mchnt_txn_ssn>" +
                        "</plain>",
                isOK ? "0000" : "9999",
                PropertiesUtils.property("hf-config", "cg.hf.mchnt_cd"),
                HfUtils.getUniqueSerialNum());
        String exprStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ap>" + signStr + "<signature>%s</signature></ap>";
        String sign = SecurityUtils.sign(signStr);
        return String.format(exprStr, sign);
    }
}
