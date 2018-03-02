package com.external.deposites.utils.flowApi;

import com.external.deposites.api.IFlowApi;
import com.external.deposites.model.datasource.AbstractDataSource;
import com.external.deposites.model.response.IResponse;
import com.external.deposites.utils.AnalysisXMLResponse;
import com.external.deposites.utils.HfUtils;
import com.external.deposites.utils.SecurityUtils;
import com.xt.cfp.core.Exception.UnCatchedException;
import com.xt.cfp.core.event.service.EventTriggerInfoService;
import com.xt.cfp.core.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/11/22
 */
public class Http implements IFlowApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String url;
    private AbstractDataSource request;
    private IResponse.ResponseType responseType;
    private String[] arrayFields;

    public Http url(String httpUrl) {
        this.url = httpUrl;
        return this;
    }

    public Http arrayFields(String... arrayFields) {
        this.arrayFields = arrayFields;
        return this;
    }

    public Http request(AbstractDataSource request) {
        this.request = request;
        return this;
    }

    public Http response(IResponse.ResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * 不作签名的字段
     */
    public Http ignoreSignFields(String... ignoreSignFields) {
        this.request.addIgnoreSignFields(ignoreSignFields);
        return this;
    }

    /**
     * 先这么写
     */
    @Override
    public <T> T merge(Class responseClass) {
        IResponse iResponse = null;
        try {
            String inputStr = this.request.regSignVal();
            String sign = SecurityUtils.sign(inputStr);
            this.request.setSignature(sign);
            logger.debug("请求signature: {}", sign);

            String resp = HfUtils.sendHttp(this.url, this.request);

            AnalysisXMLResponse analysisXMLResponse = new AnalysisXMLResponse(resp).invoke();
            boolean verifyPass = analysisXMLResponse.isVerifyPass();

            if (!verifyPass) {
                iResponse = (IResponse) responseClass.newInstance();
                iResponse.setResp_code(IResponse.InternalCode.ERROR.name());
                iResponse.setResp_desc("信息安全证验不通过！");
            }
            if (responseType == IResponse.ResponseType.XML) {
                iResponse = HfUtils.xml2Bean(analysisXMLResponse.getRetPlainStr(), responseClass, this.arrayFields);
                iResponse.setSignature(analysisXMLResponse.getRetSignatureStr());
            }
        } catch (Exception e) {
            try {
                iResponse = (IResponse) responseClass.newInstance();
                iResponse.setResp_code(IResponse.InternalCode.ERROR.name());
                iResponse.setResp_desc(e.getMessage());
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new UnCatchedException(responseClass.getName() + "没有指定默认构造方法！", e1);
            }
        }
        return (T) iResponse;
    }

    /**
     * form 数据
     */
    public <T extends AbstractDataSource> T form() {
        String inputStr = this.request.regSignVal();
        String sign = SecurityUtils.sign(inputStr);
        ReflectionUtil.invokeSetterMethod(this.request, "signature", sign);
        ReflectionUtil.invokeSetterMethod(this.request, "actionUrl", this.url);
        return (T) this.request;
    }

}
