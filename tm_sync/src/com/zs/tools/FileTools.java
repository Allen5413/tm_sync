package com.zs.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;

/**
 * Created by Allen on 2015/9/1.
 */
public class FileTools {

    /**
     * 创建文件
     * @param name
     */
    public static void createFile(String path, String name){
        try{
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            File fileName = new File(path+name);
            if(!fileName.exists()){
                fileName.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 写入内容
     * @param content
     * @param filePath
     * @throws Exception
     */
    public static void writeTxtFile(String content, String filePath){
        RandomAccessFile mm = null;
        FileOutputStream o = null;
        try {
            File fileName = new File(filePath);
            o = new FileOutputStream(fileName);
            o.write(content.getBytes("GBK"));
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(mm!=null){
                try {
                    mm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读TXT文件内容
     * @param fileName
     * @return
     */
    public static String readTxtFile(File fileName)throws Exception{
        String result=null;
        FileReader fileReader=null;
        BufferedReader bufferedReader=null;
        try{
            fileReader=new FileReader(fileName);
            bufferedReader=new BufferedReader(fileReader);
            try{
                String read = null;
                while((read=bufferedReader.readLine())!=null){
                    result=result+read+"\r\n";
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(fileReader!=null){
                fileReader.close();
            }
        }
        return result;
    }

    /**
     * 得到一个路径下的文件和文件夹
     * @param path
     * @return
     */
    public static JSONArray getPathFile(String path){
        JSONArray jsonArray = new JSONArray();
        File file = new File(path);
        if(file.isDirectory()){
            String[] childFiles = file.list();
            if(null != childFiles && 0 < childFiles.length) {
                for (String childFile : childFiles) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("path", path);
                    jsonObject.put("fileName", childFile);
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }
}
