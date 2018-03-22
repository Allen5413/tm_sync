package com.zs.tools;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2017/12/28.
 */
public class ApiTools {


    private static String url;
    private static String appId;
    private static String appKey;
    private static String token="";
    private static long expireTime=0;
    private static int againGetTokenNum = 0;

    static{
        try {
            PropertiesTools propertiesTools = new PropertiesTools("resource/commons.properties");
            url = propertiesTools.getProperty("attopapi.url");
            appId = propertiesTools.getProperty("attopapi.appId");
            appKey = propertiesTools.getProperty("attopapi.appKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        String num = "9630060046007";
        JSONObject json = getKuaiDi(num);
        System.out.println(json.toString());
    }

    /**
     * 获取快递信息
     * @param num
     * @return
     */
    public static JSONObject getKuaiDi(String num){
        String result="";
        JSONObject json = null;
        try{
            initToken();
            String u=url+"/kuaidi/findKuaiDi.json?appId="+appId+"&num="+num+"&token="+token;
            result = sendGet(u);
            json = JSONObject.parseObject(result);
            if(null != json.get("state") && json.get("state").toString().equals("204") && againGetTokenNum < 3){
                againGetTokenNum++;
                getKuaiDi(num);
            }else{
                againGetTokenNum = 0;
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
        return json;
    }

    public static void initToken(){
        try{
            if(expireTime==0||"".equals(token)||System.currentTimeMillis()>expireTime){
                String mac=getMd5(appId+appKey);
                String msg=sendGet(url + "/getToken.json?appId=" + appId +"&mac="+mac);
                if(!"".equalsIgnoreCase(msg)){
                    HashMap data= JSONObject.parseObject(msg, HashMap.class);
                    if(Integer.parseInt(data.get("state").toString())==0){
                        long expires=(Long)data.get("expires");
                        token=(String)data.get("token");
                        expireTime=System.currentTimeMillis()+expires*1000;
                    }
                }
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }

    public static String getMd5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println("MD5:  "+e.getMessage());
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String sendGet(String url)throws Exception{
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("charset", "utf-8");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result.trim();
    }


    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("charset", "utf-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
