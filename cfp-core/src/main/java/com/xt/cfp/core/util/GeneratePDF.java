package com.xt.cfp.core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Renyulin on 15-2-3 下午12:08.
 */
public class GeneratePDF {

    private final static String AGREEMENT_CHUO_PNG_PATH = PropertiesUtils.getInstance().get("AGREEMENT_CHUO_PNG_PATH");

    public static class FontsProvider extends XMLWorkerFontProvider{
        public FontsProvider(){
            super(null,null);
        }
        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {

            String fntname = fontname;
            if(fntname==null){
                fntname="华文仿宋";
            }
            return super.getFont(fntname, encoding, size, style);


        }
    }

    static class myFontProvider extends XMLWorkerFontProvider {
        public myFontProvider(){
            super(null,null);
        }
        @Override
        public Font getFont(final String fontname, final String encoding,
                            final boolean embedded, final float size, final int style,
                            final BaseColor color) {
            BaseFont bf = null;
            try {
                bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                        BaseFont.NOT_EMBEDDED);
            } catch (DocumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Font font = new Font(bf, size, style, color);
            font.setColor(color);
            return font;
        }
    }


    public static void html2PDF(String htmlUrl, String pdfPath, String fileName) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath + "/" + fileName + ".pdf"));
        document.open();
        URL url = new URL(htmlUrl);
        InputStream openStream = url.openStream();

//        File file = new File(pdfPath + "/" + fileName + ".txt");
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        byte[] bs = new byte[openStream.available()];
//        openStream.read(bs);
//        fileOutputStream.write(bs);
//
//        fileOutputStream.flush();
//        fileOutputStream.close();
//        openStream = url.openStream();



        FontsProvider fontsProvider = new FontsProvider();
        fontsProvider.addFontSubstitute("lowagie", "garamond");
        fontsProvider.setUseUnicode(true);

        CssAppliers cssAppliers = new CssAppliersImpl(fontsProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance()
                .getDefaultCssResolver(true);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
                new HtmlPipeline(htmlContext, new PdfWriterPipeline(document,
                        writer)));



        XMLWorker xmlWorker = new XMLWorker(pipeline,true);
        XMLParser p = new XMLParser(xmlWorker);
        p.parse(new InputStreamReader(url.openStream(), "UTF-8"));
        document.close();


//        XMLWorkerHelper workerHelper = XMLWorkerHelper.getInstance();
//        workerHelper.parseXHtml(writer, document,
//                openStream, Charset.forName("utf-8"));


        document.close();
        writer.flush();
        writer.close();
        openStream.close();
    }

    public static void main(String[] args) {
        try {
//            html2PDF("file:///D:/testpdf.html","d:/","baidu");
            html2PDF("http://localhost:8080/bpm/jsp/auditingInfo/toPdf?id=303&code=BPM_201503051021251","d:/","baidu");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void convert2(String infile, String outfile)
            throws FileNotFoundException, IOException, DocumentException,
            CssResolverException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(outfile));
        document.open();
        myFontProvider fontProvider = new myFontProvider();
        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        //使用我们的字体提供器，并将其设置为unicode字体样式
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        CSSResolver cssResolver = XMLWorkerHelper.getInstance()
                .getDefaultCssResolver(true);
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver,
                new HtmlPipeline(htmlContext, new PdfWriterPipeline(document,
                        writer)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(worker);
        File input = new File(infile);
        p.parse(new InputStreamReader(new FileInputStream(input), "UTF-8"));
        document.close();
    }

    public static void create(String htmlUrl, String pdfPath, String fileName,Pair... pairs) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        FileOutputStream outputStream = new FileOutputStream(pdfPath + "/" + fileName + ".pdf");
        PdfWriter.getInstance(document, outputStream);
        document.open();
        myFontProvider fontProvider = new myFontProvider();
        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(htmlUrl);
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] param = new NameValuePair[pairs.length];
        for (int i =0;i<pairs.length;i++) {
            Pair pair = pairs[i];
            NameValuePair nameValuePair = new NameValuePair((String)pair.getK(),(String)pair.getV());
            param[i] = nameValuePair;
        }
        post.setRequestBody(param);
        httpClient.executeMethod(post);
        
        final java.util.List<Element> elements = new ArrayList<Element>();
        ElementHandler elementHandler = new ElementHandler() {
            @Override
            public void add(Writable writable) {
                if (writable instanceof WritableElement) {
                    elements.addAll(((WritableElement) writable).elements());
                }
            }
        };

        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext hpc = new HtmlPipelineContext(cssAppliers);
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
        CssResolverPipeline pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(elementHandler, null)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, Charset.forName("UTF-8"));
        p.parse(post.getResponseBodyAsStream(), Charset.forName("UTF-8"));

        Image image = Image.getInstance(AGREEMENT_CHUO_PNG_PATH);
        image.setAbsolutePosition(350, 550);
        Phrase phrase = new Paragraph();
        elements.add(0, image);
        phrase.addAll(elements);
        document.add(phrase);
        outputStream.flush();
        document.close();
    }

    public static void html2PDF1(String htmlUrl) throws Exception {

        BaseFont bfCN = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",
                false);
        // 中文字体定义
        Font chFont = new Font(bfCN, 10, Font.NORMAL, BaseColor.BLUE);
        Font secFont = new Font(bfCN, 10, Font.NORMAL, new BaseColor(0, 204,
                255));

        Document document = new Document();
        PdfWriter pdfwriter = PdfWriter.getInstance(document,
                new FileOutputStream("e:\\test.pdf"));
        pdfwriter.setViewerPreferences(PdfWriter.HideToolbar);
        document.open();

        int chNum = 1;
        Chapter chapter = new Chapter(new Paragraph("", chFont),
                chNum++);

        Section section = chapter.addSection(new Paragraph("",
                secFont));
        // section.setNumberDepth(2);
        // section.setBookmarkTitle("基本信息");
        section.setIndentation(10);
        section.setIndentationLeft(10);
        section.setBookmarkOpen(false);
        section.setNumberStyle(Section.NUMBERSTYLE_DOTTED);
        section.add(Chunk.NEWLINE);
        document.add(chapter);

        // html文件
        URL url = new URL(htmlUrl);
        InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");


        XMLWorkerHelper.getInstance().parseXHtml(pdfwriter, document, isr);


        document.close();
    }

    public static void createForFinance(String htmlUrl, String pdfPath, String fileName,Pair... pairs) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        document.setMargins(30, 30, 30, 30);
        FileOutputStream outputStream = new FileOutputStream(pdfPath + "/" + fileName + ".pdf");
        PdfWriter.getInstance(document, outputStream);
        document.open();
        myFontProvider fontProvider = new myFontProvider();
        fontProvider.addFontSubstitute("lowagie", "garamond");
        fontProvider.setUseUnicode(true);
        HttpClient httpClient = new HttpClient();
        PostMethod post = new PostMethod(htmlUrl);
        post.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair[] param = new NameValuePair[pairs.length];
        for (int i =0;i<pairs.length;i++) {
            Pair pair = pairs[i];
            NameValuePair nameValuePair = new NameValuePair((String)pair.getK(),(String)pair.getV());
            param[i] = nameValuePair;
        }
        post.setRequestBody(param);
        httpClient.executeMethod(post);
        
        final java.util.List<Element> elements = new ArrayList<Element>();
        ElementHandler elementHandler = new ElementHandler() {
            @Override
            public void add(Writable writable) {
                if (writable instanceof WritableElement) {
                    elements.addAll(((WritableElement) writable).elements());
                }
            }
        };

        CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
        HtmlPipelineContext hpc = new HtmlPipelineContext(cssAppliers);
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
        CssResolverPipeline pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(elementHandler, null)));
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, Charset.forName("UTF-8"));
        p.parse(post.getResponseBodyAsStream(), Charset.forName("UTF-8"));

        Image image = Image.getInstance(AGREEMENT_CHUO_PNG_PATH);
        image.setAbsolutePosition(50, 600);
        Phrase phrase = new Paragraph();
        elements.add(0, image);
        phrase.addAll(elements);
        document.add(phrase);
        outputStream.flush();
        document.close();
    }

}
