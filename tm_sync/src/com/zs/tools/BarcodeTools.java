package com.zs.tools;


import org.jbarcode.JBarcode;
import org.jbarcode.encode.Code39Encoder;
import org.jbarcode.encode.EAN13Encoder;
import org.jbarcode.paint.BaseLineTextPainter;
import org.jbarcode.paint.EAN13TextPainter;
import org.jbarcode.paint.WideRatioCodedPainter;
import org.jbarcode.paint.WidthCodedPainter;
import org.jbarcode.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

/**
 * Created by Allen on 2015/7/19.
 */
public class BarcodeTools {
    public static String createBarCode(String str, String path, String getRealPath)throws Exception{
        JBarcode localJBarcode = new JBarcode(EAN13Encoder.getInstance(), WidthCodedPainter.getInstance(), EAN13TextPainter.getInstance());
        localJBarcode.setEncoder(Code39Encoder.getInstance());
        localJBarcode.setPainter(WideRatioCodedPainter.getInstance());
        localJBarcode.setTextPainter(BaseLineTextPainter.getInstance());
        localJBarcode.setShowCheckDigit(false);
        localJBarcode.setBarHeight(25);
        localJBarcode.setWideRatio(3);
        BufferedImage localBufferedImage = localJBarcode.createBarcode(str);

        String fileName = str+".png";
        saveToPNG(localBufferedImage, fileName, getRealPath+path);
        return path+fileName;
    }

    static void saveToJPEG(BufferedImage paramBufferedImage, String paramString, String path){
        saveToFile(paramBufferedImage, paramString, "jpeg", path);
    }

    static void saveToPNG(BufferedImage paramBufferedImage, String paramString, String path){
       saveToFile(paramBufferedImage, paramString, "png", path);
    }

    static void saveToGIF(BufferedImage paramBufferedImage, String paramString, String path){
       saveToFile(paramBufferedImage, paramString, "gif", path);
    }

    static void saveToFile(BufferedImage paramBufferedImage, String paramString1, String paramString2, String path){
        try{
            FileOutputStream localFileOutputStream = new FileOutputStream(path + paramString1);
            ImageUtil.encodeAndWrite(paramBufferedImage, paramString2, localFileOutputStream, 96, 96);
            localFileOutputStream.close();
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }
}
