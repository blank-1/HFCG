package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.HttpErrorCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * User: yulei
 * Date: 14-4-14
 * Time: 下午5:53
 */
public class ResponseUtil {

    public static void sendString(HttpServletResponse response, String string) {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("utf-8");

            PrintWriter writer = response.getWriter();
            writer.print(string);
            writer.flush();
        } catch (IOException e) {
            throw SystemException.wrap(e, HttpErrorCode.CAN_NOT_WRITE_CONTENT_TO_RESP);
        }

    }


    public static void sendExcel(HttpServletResponse response, List<LinkedHashMap<String, Object>> list, String fileName) {
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流

            response.setHeader("Content-disposition", "attachment; filename="+new String(fileName.getBytes("GBK"),"ISO-8859-1")+".xls");// 设定输出文件头     待打款提现单
            response.setContentType("application/msexcel");// 定义输出类型
            HSSFWorkbook wb = ExcelUtil.createExcel(list, fileName);
            wb.write(os);
            os.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void sendExcel(HttpServletResponse response,HSSFWorkbook wb, String fileName) {
        try {
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流

            response.setHeader("Content-disposition", "attachment; filename="+new String(fileName.getBytes("GBK"),"ISO-8859-1")+".xls");// 设定输出文件头     待打款提现单
            response.setContentType("application/msexcel");// 定义输出类型
            wb.write(os);
            os.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
