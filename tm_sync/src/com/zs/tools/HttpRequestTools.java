package com.zs.tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 发起Http请求
 * Created by Allen on 2015/6/25.
 */
public class HttpRequestTools {

    //Http请求url
    private static String url;

    static{
        try {
            PropertiesTools propertiesTools = new PropertiesTools("resource/commons.properties");
            url = propertiesTools.getProperty("eduwest.url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录验证
     * @return
     * @throws Exception
     */
    public static boolean vaildEduLogin(String pin, String pwd)throws Exception{
        StringBuilder urlStr = new StringBuilder(url);
        urlStr.append("?pin="+pin);
        urlStr.append("&pwd="+pwd);
        String result = sendGet(urlStr.toString());
        if("Success".equals(result)){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args){
        try {
            String pwd = "spot01";
            vaildEduLogin("spot01", CryptTools.Encrypt(pwd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url
     * @return
     */
    private static String sendGet(String url)throws Exception{
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
}
