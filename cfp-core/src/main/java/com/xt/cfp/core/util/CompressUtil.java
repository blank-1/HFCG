package com.xt.cfp.core.util;

import com.xt.cfp.core.Exception.SystemException;
import com.xt.cfp.core.Exception.code.ext.SystemErrorCode;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by yulei on 2015/9/7 0007.
 */
public class CompressUtil {

    private CompressUtil() {}


    public static void zipFiles(String zipFileFullName, File... needZipFiles) {
        try {
            ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileFullName, false));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOutputStream);
            zipFiles(zipOutputStream, bufferedOutputStream, "/", needZipFiles);
            bufferedOutputStream.close();
            zipOutputStream.close();
        } catch (FileNotFoundException e) {
            throw SystemException.wrap(e, SystemErrorCode.FILE_ERROR).set("zipFileFullName", zipFileFullName);
        } catch (IOException e) {
            throw SystemException.wrap(e, SystemErrorCode.IO_ERROR);
        }
    }

    private static void zipFiles(ZipOutputStream zipOutputStream, BufferedOutputStream bufferedOutputStream, String basePath, File... needZipFiles) throws IOException {
        for (File file : needZipFiles) {
            if (file.isDirectory()) {
                zipOutputStream.putNextEntry(new ZipEntry(file.getName() + "/"));
                File[] files = file.listFiles();
                zipFiles(zipOutputStream, bufferedOutputStream, file.getName() + "/");
            } else {

            }
        }
    }
}
