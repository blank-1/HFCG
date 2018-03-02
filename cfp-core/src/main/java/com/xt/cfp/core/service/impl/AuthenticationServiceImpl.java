package com.xt.cfp.core.service.impl;


import java.io.StringReader;
import java.net.URL;
import java.util.Date;


import org.apache.log4j.Logger;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.util.Base64;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.PayErrorCode;
import com.xt.cfp.core.Exception.code.ext.UserErrorCode;
import com.xt.cfp.core.Exception.code.ext.ValidationErrorCode;
import com.xt.cfp.core.service.AuthenticationService;
import com.xt.cfp.core.util.CompressStringUtil;
import com.xt.cfp.core.util.Property;

@Property
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static Logger logger = Logger.getLogger(AuthenticationServiceImpl.class);


    private static String soapUrl;
    private static String userName;
    private static String passWord;
    private static String flag;//0开  别的都是关

    @Override
    public boolean verifyService(String id, String trueName) {
        if (id == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("id", "null");
        if (trueName == null)
            throw new SystemException(ValidationErrorCode.ERROR_NULL).set("trueName", "null");
        try {
            if (!"0".equals(flag)) {
                return true;
            }
            String queryInfo = "<?xml version=\"1.0\" encoding=\"GBK\"?>" +
                    "<conditions><condition queryType=\"25121\"><item><name>name</name><value>" + trueName + "</value></item>" +
                    "<item><name>documentNo</name><value>" + id + "</value>" +
                    "</item><item><name>subreportIDs</name><value>10602</value></item><item>" +
                    "<name>refID</name><value>" + new Date().getTime() + "</value></item></condition></conditions>";

            logger.info(id + "身份认证请求报文：" + queryInfo);
            Client client = new Client(new URL(soapUrl));
            Object[] results = client.invoke("queryReport", new Object[]{userName, passWord, queryInfo, "xml"});
            String ba64 = "";
            if (results[0] instanceof String) {
                //返回字符串，解析处理字符串内容
                ba64 = results[0].toString();
                //System.out.println("resut:"+results[0].toString());
            } else if (results[0] instanceof org.w3c.dom.Document) {
                //返回字符串Document，解析处理Document内容
                org.w3c.dom.Document doc = (org.w3c.dom.Document) results[0];
                Element element = doc.getDocumentElement();
                NodeList children = element.getChildNodes();
                Node node = children.item(0);
                ba64 = node.getNodeValue();
                //System.out.println("result content:"+ba64);
            }

            StringReader xmlReader = new StringReader(ba64);

            InputSource xmlSource = new InputSource(xmlReader);

            SAXBuilder builder = new SAXBuilder();

            org.jdom.Document doc = builder.build(xmlSource);
            logger.info(doc.toString());

            org.jdom.Element elt = doc.getRootElement();
            //System.out.println(elt.getName());
            org.jdom.Element status = elt.getChild("status");
            //System.out.println(status.getText());
            if ("1".equals(status.getText())) {
                org.jdom.Element returnValue = elt.getChild("returnValue");
                //System.out.println(returnValue.getText());
                Base64 base64 = new Base64();
                byte[] re = base64.decode(returnValue.getText());
                String xml = new CompressStringUtil().decompress(re);
                //System.out.println("结果："+xml);
                xmlReader = new StringReader(xml);

                xmlSource = new InputSource(xmlReader);

                builder = new SAXBuilder();

                doc = builder.build(xmlSource);

                elt = doc.getRootElement();
                org.jdom.Element policeCheckInfo = elt.getChild("cisReport").getChild("policeCheckInfo");
                String treatResult = policeCheckInfo.getAttributeValue("treatResult");
                if ("1".equals(treatResult)) {
                    String idCardresult = policeCheckInfo.getChild("item").getChild("result").getText();
                    if ("1".equals(idCardresult)) {
                        //true
                        System.out.println("查到");
                        logger.info("已查到");
                        return true;
                    } else {
                        System.out.println("身份证名字不一致或者没有这个数据");
                        logger.info("身份证名字不一致或者没有这个数据");
                        return false;
                    }
                } else {
                    //todo 返回失败
                    System.out.println("未查到");
                    logger.info("未查到");
                    return false;
                }
            } else {
                org.jdom.Element errorCode = elt.getChild("errorCode");
                System.out.println("errorCode==错误代码==" + errorCode);
                System.out.println("没有调通");
                logger.info("没有调通");
                //todo返回失败
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            //todo 返回错误
            logger.info("异常" + e.getMessage());
            throw new SystemException(UserErrorCode.USER_AUTHENTICATION_TIMEOUT).set("id", id).set("trueName", trueName);                //return false;
        }
    }

    public static String getSoapUrl() {
        return soapUrl;
    }

    @Property(name = "AUTHEN_SOAP_URL")
    public static void setSoapUrl(String soapUrl) {
        AuthenticationServiceImpl.soapUrl = soapUrl;
    }

    public static String getUserName() {
        return userName;
    }

    @Property(name = "AUTHEN_USER_NAME")
    public static void setUserName(String userName) {
        AuthenticationServiceImpl.userName = userName;
    }

    public static String getPassWord() {
        return passWord;
    }

    @Property(name = "AUTHEN_PASS_WORD")
    public static void setPassWord(String passWord) {
        AuthenticationServiceImpl.passWord = passWord;
    }

    public static String getFlag() {
        return flag;
    }

    @Property(name = "AUTHEN_FLAG")
    public static void setFlag(String flag) {
        AuthenticationServiceImpl.flag = flag;
    }

}
