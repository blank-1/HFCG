package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.ExcelErrorCode;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luqinglin on 2015/6/30.
 */
public class ExcelUtil {
	
	private final static String CREATED_TEMP_FILE_PATH="/opt/backup/"/*"F:/temp/"*/;
	
    /**
     * 根据上传的excel文件，解析数据并返回，而上传文件并不保存
     * @param fi_award
     * @return
     */
    public static List<Map<String, Object>> getUploadExcelData(MultipartFile fi_award) {
        List<Map<String, Object>> result = null;
        //解析excel
        try {
        	String type="1";
        	if(fi_award.getOriginalFilename().indexOf(".xlsx")>=0){
        		type="2";
        	}
            File tempFile = jodd.io.FileUtil.createTempFile();
            jodd.io.FileUtil.appendBytes(tempFile, fi_award.getBytes());
            List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,type);
            result = lists!=null&&lists.size()>0?lists.get(0):null;//sheet-0
            tempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

   public static void main(String[] args) {
	   List<Map<String, Object>> result = null;
       File tempFile = new File("E:\\test2.xlsx");
	   List<List<Map<String, Object>>> lists = ExcelUtil.analysisExcel(tempFile,"2");
	   result = lists.get(0);//sheet-0
	   Object o=result.get(0).get("前端");
	   System.out.println(o);
   }

    /**
     * 生成Excel文件
     * @param dataMap
     * @return
     */
    public static HSSFWorkbook createExcel(List<LinkedHashMap<String, Object>> dataMap,String sheetName) {
        if(dataMap==null||dataMap.size()==0) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            hssfWorkbook.createSheet(sheetName);
            return hssfWorkbook;
        }

        int length = 0;
        int index = 0;
        Map<String, Object> titleMap = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<dataMap.size();i++){
            int _length = dataMap.get(i).keySet().size();
            if (length<_length){
                length = _length;
                index = i;
            }
            list.add(dataMap.get(i));
        }
        titleMap = dataMap.get(index);
        String [] title = new String []{};
        title = titleMap.keySet().toArray(title);
        return createExcel(title,list,sheetName);
    }

    /**
     * 生成Excel文件
     * @param title
     * @param dataMap
     * @return
     */
    public static HSSFWorkbook createExcel(String[] title, List<Map<String, Object>> dataMap,String sheetName) {

        //建立一个excel工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立一个sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        //建立第一行标题
        int rowNum = 0;
        int colNum = title.length;
        HSSFRow titleRow = sheet.createRow(rowNum);
        for(int i=0;i<colNum;i++){
            HSSFCell titileCell = titleRow.createCell(i);
            //设置单元格宽度
            sheet.setColumnWidth(i, 20 * 256);
            //给单元格赋值
            titileCell.setCellValue(title[i]);
        }

        for(Map data : dataMap){
            rowNum += 1;
            HSSFRow dataRow = sheet.createRow(rowNum);
            //填充单元格
            for (int i = 0; i < title.length; i++) {
                HSSFCell cell = dataRow.createCell(i);
                if(data.get(title[i])==null)
                    cell.setCellValue("");
                else
                    cell.setCellValue(data.get(title[i]).toString());
            }
        }
        return wb;
    }


    /**
     * excel 文件解析，
     * @param file
     * @return
     */
	public static List<List<Map<String,Object>>> analysisExcel(File file,String type){
        List<List<Map<String,Object>>> resultList = new ArrayList<List<Map<String, Object>>>();
        //HSSFWorkbook wb = null;
        Workbook wb = null;
        try {
        	if(type.equals("2")){
        		wb = new XSSFWorkbook(new FileInputStream(file));
        	}else{
        		wb = new HSSFWorkbook(new FileInputStream(file));
        	}
            
            int numberOfSheets = wb.getNumberOfSheets();
            for (int i=0;i<numberOfSheets;i++){
                List<Map<String,Object>> sheetResult = new ArrayList<Map<String, Object>>();
                //HSSFSheet sheet = wb.getSheetAt(i);
                Sheet sheet=wb.getSheetAt(i);
                int rowCount = sheet.getLastRowNum();
                if(rowCount<1){
                    continue;
                }
                List<String> title = new ArrayList<String>();
                int colCount = 0;
                for (int rowIndex = 0;rowIndex <=rowCount;rowIndex++){
                    //获取列标题
                    if(rowIndex==0){
                        //HSSFRow titleRow = sheet.getRow(rowIndex);
                    	Row titleRow = sheet.getRow(rowIndex);
                        colCount = titleRow.getPhysicalNumberOfCells();
                        for(int j=0;j<colCount;j++){
                            title.add(getCellString(titleRow.getCell(j)).toString());
                        }
                    }else{
                    	Row row = sheet.getRow(rowIndex);
                        if(row != null){
                            Map<String,Object> map = new HashMap<String, Object>();
                            for(int k=0;k<colCount;k++){
                                map.put(title.get(k),getCellString(row.getCell(k)));
                            }
                            sheetResult.add(map);
                        }
                    }
                }
                resultList.add(sheetResult);
            }
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException(ExcelErrorCode.ANALYSIS_FAILE).set("msg", e.getMessage());
        }


    }


    /**
     * 获取单元格数据
     * @param cell
     * @return
     */
    protected static Object getCellString(Cell cell){
        Object result = null;
        if (cell != null) {
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            int cellType = cell.getCellType();
            switch(cellType){
                case HSSFCell.CELL_TYPE_STRING :
                    result = cell.getRichStringCellValue().getString();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    result=cell.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    result = cell.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    result=null;
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    result=cell.getBooleanCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BLANK:
                    result=null;
                    break;
            }
        }
        return result;
    }
    
    /**
     * 生成批量Excel文件并切导出zip文件
     * @param dataMap
     * @return
     */
    public static HSSFWorkbook createExcelBath(List<LinkedHashMap<String, Object>> dataMap,
    		HttpServletRequest request, OutputStream out,String f,HttpServletResponse response) {
        if(dataMap==null||dataMap.size()==0) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            hssfWorkbook.createSheet(f);
            return hssfWorkbook;
        }

        int length = 0;
        int index = 0;
        Map<String, Object> titleMap = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<dataMap.size();i++){
            int _length = dataMap.get(i).keySet().size();
            if (length<_length){
                length = _length;
                index = i;
            }
            list.add(dataMap.get(i));
        }
        titleMap = dataMap.get(index);
        String [] title = new String []{};
        title = titleMap.keySet().toArray(title);
        return createExcelBatch(title,list,request, out,f,response);
    }
    
    /**
     * 批量生成Excel文件
     * @param title
     * @param dataMap
     * @return
     */
    @SuppressWarnings({ "resource", "unused" })
	public static HSSFWorkbook createExcelBatch(String[] title, List<Map<String, Object>> dataMap,
    		HttpServletRequest request, OutputStream out,String f,HttpServletResponse response) {
    	File zipPath = new File( CREATED_TEMP_FILE_PATH + f + ".zip");// 压缩文件6
    	int excelNum = Integer.parseInt(PropertiesUtils.getInstance().get("EXCEL_SUM"));
    	List<String> fileNames = new ArrayList<String>();// 用于存放生成的文件名称s
		for (int j = 0, n = dataMap.size() / excelNum + 1; j < n; j++) {
			Workbook book = new HSSFWorkbook();
			Sheet sheet = book.createSheet(f);
			String file = CREATED_TEMP_FILE_PATH  + f + "-" + j+ ".xls";
			fileNames.add(file);
			FileOutputStream o = null;
			try {
				o = new FileOutputStream(file);
				   int colNum = title.length;
				   int rowNum = 0;
				Row row = sheet.createRow(rowNum); //创建单元行
		        for(int i=0;i<colNum;i++){
		            Cell createCell = row.createCell(i);
		            //设置单元格宽度
		            sheet.setColumnWidth(i, 20 * 256);
		            //给单元格赋值
		            createCell.setCellValue(title[i]);
		        }
				int m = 1;
				for (int i = 1, min = (dataMap.size() - j * excelNum + 1) > (excelNum + 1) ? (excelNum + 1)
						: (dataMap.size() - j * excelNum + 1); i < min; i++) {
					m++;
					Map<String, Object> map = dataMap.get(excelNum * (j) + i - 1);
					 rowNum += 1;
			        Row createRow = sheet.createRow(rowNum);
			        for (int k = 0; k < title.length; k++) {
			        	Cell createCell = createRow.createCell(k);
			        	   if(map.get(title[k])==null){
			        		   createCell.setCellValue("");
			        	   }else{
			        		   createCell.setCellValue(map.get(title[k]).toString());
			        	   }
			        }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				book.write(o);
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (o != null) {
					try {
						o.flush();
						o.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		File srcfile[] = new File[fileNames.size()];
		for (int i = 0, n = fileNames.size(); i < n; i++) {
			srcfile[i] = new File(fileNames.get(i));
		}
		byte[] bytes = new byte[1024];
		
		try {
		    ZipFiles(srcfile, zipPath);
		    String realPath = CREATED_TEMP_FILE_PATH;
//			FileUtil.zipFiles(srcfile, zipPath.getPath(), f);
			FileUtil.download(bytes, realPath, f, response);
			FileUtil.deleteFile(zipPath.getPath());
			for (int i = 0; i < srcfile.length; i++) {
				FileUtil.deleteFile(srcfile[i].getPath());
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	
    }

	/**
	 * 
	 * 
	 * 
	 * @param srcfile
	 *            文件名数组
	 * 
	 * @param zipfile
	 *            压缩后文件
	 */

	public static void ZipFiles(java.io.File[] srcfile, java.io.File zipfile) {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipfile));
			for (int i = 0; i < srcfile.length; i++) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				out.putNextEntry(new ZipEntry(srcfile[i].getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	 

}
