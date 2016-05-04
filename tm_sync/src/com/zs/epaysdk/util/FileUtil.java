/**
 * 文件工具类
 *
 * @author xiezz
 * @version 1.1.3
 */
package com.zs.epaysdk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static String getBase64FromFile(String fileName) {
        FileInputStream inputFile = null;
        try {
            File file = new File(fileName);
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            return new sun.misc.BASE64Encoder().encode(buffer);
        } catch (Exception e) {
            return "";
        } finally {
            if (inputFile != null)
                try {
                    inputFile.close();
                } catch (IOException e) {
                }
        }
    }

    public static void writeFileFromBase64(String base64, String fileName) throws IOException {
        FileOutputStream out = null;
        try {
            byte[] buffer = new sun.misc.BASE64Decoder().decodeBuffer(base64);
            out = new FileOutputStream(fileName);
            out.write(buffer);
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                }
        }
    }
}
