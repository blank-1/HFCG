package com.xt.cfp.core.util;

import com.external.wxtl.HttpUtil;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4PageMark;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConverterHtml2PDF {

    private String reqUrl;

    private final static String CONTRACT_PNG_PATH = PropertiesUtils.getInstance().get("CONTRACT_PNG_PATH");

    public ConverterHtml2PDF(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public ConverterHtml2PDF() {
        super();
    }

    public static void main(String[] args) throws Exception {
        /*ConverterHtml2PDF converter = new ConverterHtml2PDF();
		File pdfFile = new File("F:/temp/demo_ch_pd4ml.pdf");
		String html=HttpUtil.sendPostRequestByParam("http://localhost:6061/cfp-web/finance/myCreditRightAll?userId=8609", "");
		StringReader strReader = new StringReader(html);
		converter.generatePDF(pdfFile, strReader);*/
		/*InputStream inStream = new FileInputStream(new File("E:/brancches/0908/cfp/cfp-web/src/main/webapp/resources/css/style.css"));
		String a=new String(HttpUtil.readInputStream(inStream));
		System.out.println(a);*/
        /*ConverterHtml2PDF converter = new ConverterHtml2PDF();
        File pdfFile = new File("F:/temp/demo_ch_pd4ml.pdf");
        FileOutputStream fos = new FileOutputStream(pdfFile);
        StringReader strReader = new StringReader("");
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setHtmlWidth(1200);
        pd4ml.setPageSize(PD4Constants.A4);
        pd4ml.useTTF("java:/fonts", true);
        pd4ml.setDefaultTTFs("SongTi_GB2312", "SongTi_GB2312", "SongTi_GB2312");
        pd4ml.enableDebugInfo();

        PD4PageMark header = new PD4PageMark();
        header.setAreaHeight(-1); // autocompute
        header.setHtmlTemplate(null); // autocompute
        pd4ml.setPageHeader(header);


        PD4PageMark footer = new PD4PageMark();
        footer.setWatermark(agg, new Rectangle(10, 10, 180, 180), 30);
        footer.setPageNumberAlignment(PD4PageMark.CENTER_ALIGN);
        pd4ml.setPageFooter(footer);

        pd4ml.render(strReader, fos);
        strReader.close();
        fos.close();*/
    }

    /**
     * @param request
     * @param file    pdf文件地址
     * @param css     加载的样式 对应的样式是/css/ 下的样式  如有多级需自行添加对应的目录
     * @throws Exception
     * @author hu
     */
    public void generatePDF(HttpServletRequest request, File file, String css) throws Exception {
        String baseUrl = "http://10.46.163.177:8080";//request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+request.getContextPath();
        String htmlUrl = baseUrl + reqUrl;

        String html = HttpUtil.sendPostRequestByParam(htmlUrl, "");
        StringReader strReader = new StringReader(html);

        FileOutputStream fos = new FileOutputStream(file);
        PD4ML pd4ml = new PD4ML();
        if (!StringUtils.isNull(css)) {
            pd4ml.addStyle(new URL(baseUrl + "/css/" + css), true);
        }
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setHtmlWidth(1200);
        pd4ml.setPageSize(PD4Constants.A4);
        pd4ml.useTTF("java:fonts", true);
        pd4ml.setDefaultTTFs("SongTi_GB2312", "SongTi_GB2312", "SongTi_GB2312");
        pd4ml.enableDebugInfo();
        pd4ml.render(strReader, fos);
        strReader.close();
        fos.close();
    }

    public void generatePDF(File file, String html) throws Exception {
        StringReader strReader = new StringReader(html);

        FileOutputStream fos = new FileOutputStream(file);
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setHtmlWidth(1200);
        pd4ml.setPageSize(PD4Constants.A4);
        pd4ml.useTTF("java:/fonts", true);
        pd4ml.setDefaultTTFs("SongTi_GB2312", "SongTi_GB2312", "SongTi_GB2312");
        pd4ml.enableDebugInfo();
        pd4ml.render(strReader, fos);
        strReader.close();
        fos.close();
    }

    public void generateContract(File file, String html) throws Exception {
        StringReader strReader = new StringReader(html);

        FileOutputStream fos = new FileOutputStream(file);
        PD4ML pd4ml = new PD4ML();
        pd4ml.setPageInsets(new Insets(20, 10, 10, 10));
        pd4ml.setHtmlWidth(1200);
        pd4ml.setPageSize(PD4Constants.A4);
        pd4ml.useTTF("java:/fonts", true);
        pd4ml.setDefaultTTFs("SongTi_GB2312", "SongTi_GB2312", "SongTi_GB2312");
        pd4ml.enableDebugInfo();

        PD4PageMark footer = new PD4PageMark();
        footer.setWatermark(CONTRACT_PNG_PATH, new Rectangle(400, 10, 180, 180), 60);
        footer.setPagesToSkip(1);
        footer.setInitialPageNumber(1);
        footer.setPageNumberAlignment(PD4PageMark.CENTER_ALIGN);
        pd4ml.setPageFooter(footer);

        pd4ml.render(strReader, fos);
        strReader.close();
        fos.close();
    }

    public String parseURL(String addr, String json) throws Exception {
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");// 提交模式
            conn.setDoOutput(true);// 是否输入参数
            byte[] bypes = json.getBytes("utf-8");
            conn.getOutputStream().write(bypes);// 输入参数
            InputStream inStream = conn.getInputStream();
            return new String(HttpUtil.readInputStream(inStream), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}

