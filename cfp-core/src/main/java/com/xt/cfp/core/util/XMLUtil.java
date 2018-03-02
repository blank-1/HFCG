package com.xt.cfp.core.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import org.apache.poi.ss.formula.functions.T;
import org.codehaus.jackson.map.DeserializationConfig;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

import static com.fasterxml.jackson.core.JsonTokenId.ID_START_ARRAY;

/**
 * xml工具类
 *
 * @author miklchen
 */
public class XMLUtil {
    private static Logger logger = LoggerFactory.getLogger(XMLUtil.class);

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     * @throws org.jdom2.JDOMException
     * @throws java.io.IOException
     */
    public static Map doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");

        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map m = new HashMap();

        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XMLUtil.getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(XMLUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * 获取xml编码字符集
     *
     * @param strxml
     * @return
     * @throws java.io.IOException
     * @throws org.jdom2.JDOMException
     */
    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String) doc.getProperty("encoding");
    }


    /**
     * xml转json (string to string)
     * @param xml
     */
    public static String xml2json(String xml, boolean isArray) {

        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        try {
            StringWriter w = new StringWriter();
            XmlFactory xmlMapperFactory = xmlMapper.getFactory();
            JsonParser jp = xmlMapperFactory.createParser(xml);
            JsonFactory objectMapperFactory = objectMapper.getFactory();
            JsonGenerator jg = objectMapperFactory.createGenerator(w);
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
            jp.close();
            jg.close();
            String result = w.toString();
            if (isArray) {
                String pre = result.substring(1, result.length() - 1);
                String removedStr = pre.substring(0, pre.indexOf(":") + 1);
                result = "[" + pre.replaceAll(removedStr, "") + "]";
            }
            return result;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * xml转json (string to string)
     * 不支持xml数组
     *
     * @param xml
     */
    public static String xml2json(String xml, String... fields) {
        if (fields == null || fields.length == 0) {
            return xml2json(xml, false);
        }
        String replaceStr = "--replace-%s--";

        Map<String, String> subJson = new HashMap<>();
        String mainXml = xml;
        for (String fieldName : fields) {
            String s = "<" + fieldName + ">";
            String e = "</" + fieldName + ">";

            String arrays = mainXml.substring(mainXml.indexOf(s) + s.length(), mainXml.lastIndexOf(e));
            logger.debug(arrays);
            //当集合为空时不做转换
            if(!StringUtils.isNull(arrays)) {
            	mainXml = mainXml.replaceAll(arrays, String.format(replaceStr, fieldName));
            	String arraysJson = XMLUtil.xml2json(s + arrays + e, true);
            	subJson.put(fieldName, arraysJson);
            }else {
            	mainXml = mainXml.replaceAll(s + arrays + e,arrays);
            }
        }

        String json = XMLUtil.xml2json(mainXml, false);
        for (Map.Entry<String, String> entry : subJson.entrySet()) {
            json = json.replaceAll("\"" + String.format(replaceStr, entry.getKey()) + "\"", entry.getValue());
        }
        logger.debug("xml 2 json ==> {}", json);
        return json;
    }
}
