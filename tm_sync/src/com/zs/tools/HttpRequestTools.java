package com.zs.tools;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 发起Http请求
 * Created by Allen on 2015/6/25.
 */
public class HttpRequestTools {

    private static Logger logger = LoggerFactory.getLogger(new HttpRequestTools().getClass());

    //快递100Http请求url
    private static String kuaidiReqUrl;
    //快递100Http回调url
    private static String kuaidiCallBackUrl;
    //快递100 key
    private static String key;

    //网院请求url
    private static String wangYuanUrl;

    static{
        try {
            PropertiesTools propertiesTools = new PropertiesTools("resource/commons.properties");
            kuaidiReqUrl = propertiesTools.getProperty("kuaidi100.url");
            kuaidiCallBackUrl = propertiesTools.getProperty("kuaidi100.callback.url");
            key = propertiesTools.getProperty("kuaidi100.key");
            wangYuanUrl = propertiesTools.getProperty("wangyuan.url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求快递100  该方法已过时。求情快递已经调用kuaidi-PostOrder类
     * @return
     * @throws Exception
     */
    public static JSONObject reqKuaidi100(String com, String nu)throws Exception{
        StringBuilder urlStr = new StringBuilder(kuaidiReqUrl);
        JSONObject jsonObject = new JSONObject();
        JSONObject parametersJSON = new JSONObject();
        parametersJSON.put("callbackurl", kuaidiCallBackUrl);
        jsonObject.put("company", com);
        jsonObject.put("number", nu);
        jsonObject.put("key", key);
        jsonObject.put("parameters", parametersJSON);
        System.out.println("paramJSON:     "+jsonObject.toString());
        String result = sendHttpPost(urlStr.toString(), jsonObject.toString());
        System.out.println("result:     "+result);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        return callbackJSON;
    }

    /**
     * 通过网院接口获取课程信息
     * @return
     * @throws Exception
     */
    public static JSONObject getCourse()throws Exception{
        String url = wangYuanUrl+"getCourse?code=&signature"+DesTools.encrypt("getCourse", "eduwest");
        String result = sendGet(url);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        callbackJSON.put("reqUrl", url);
        return callbackJSON;
    }

    /**
     * 通过网院接口获取学习中心信息
     * @return
     * @throws Exception
     */
    public static JSONObject getSpot()throws Exception{
        String url = wangYuanUrl+"getStudyCenter?code=&signature="+DesTools.encrypt("getStudyCenter", "eduwest");
        String result = sendGet(url);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        callbackJSON.put("reqUrl", url);
        return callbackJSON;
    }

    /**
     * 通过网院接口获取学生信息
     * @param year
     * @param term
     * @return
     * @throws Exception
     */
    public static JSONObject getStudent(int year, int term)throws Exception{
        String url = wangYuanUrl+"getStudent?year="+year+"&term="+term+"&studyCenterCode=&pageNo=0&pageSize=0&signature="+DesTools.encrypt("getStudent", "eduwest");
        String result = sendGet(url);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        callbackJSON.put("reqUrl", url);
        return callbackJSON;
    }

    /**
     * 通过网院接口获取学生信息
     * @param year
     * @param term
     * @return
     * @throws Exception
     */
    public static JSONObject getStudent(int year, int term, String spotCode)throws Exception{
        String url = wangYuanUrl+"getStudent?year="+year+"&term="+term+"&studyCenterCode="+spotCode+"&pageNo=0&pageSize=0&signature="+DesTools.encrypt("getStudent", "eduwest");
        String result = sendGet(url);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        callbackJSON.put("reqUrl", url);
        return callbackJSON;
    }

    /**
     * 通过网院接口获取学生选课信息
     * @param year
     * @param term
     * @return
     * @throws Exception
     */
    public static JSONObject getSelectCourse(int stuYear, int stuTerm, int courseYear, int courseTerm)throws Exception{
        String url = wangYuanUrl+"getSelectCourse?stuYear="+stuYear+"&stuYerm="+stuTerm+"&courseYear="+courseYear+"&courseTerm="+courseTerm+"&courseCode=&pageNo=0&pageSize=0&signature="+DesTools.encrypt("getSelectCourse", "eduwest");
        String result = sendGet(url);
        JSONObject callbackJSON = new JSONObject();
        callbackJSON = callbackJSON.fromObject(result);
        callbackJSON.put("reqUrl", url);
        return callbackJSON;
    }



    public static void main(String[] args){
        try {
            getStudent(2001, 1);
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

    /**
     * 模拟form表单提交
     * @param url
     * @param param
     * @return
     */
    public static String sendHttpPost(String url, String param) {
        String responseMessage = "";
        StringBuffer resposne = new StringBuffer();
        HttpURLConnection httpConnection = null;
        DataOutputStream out = null;
        BufferedReader reader = null;
        try {
            URL urlPost = new URL(url);
            httpConnection = (HttpURLConnection) urlPost.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            // 参数长度太大，不能用get方式
            httpConnection.setRequestMethod("POST");
            // 不使用缓存
            httpConnection.setUseCaches(false);
            // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
            httpConnection.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数
            httpConnection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            httpConnection.connect();
            out = new DataOutputStream(httpConnection.getOutputStream());
            //写入参数,DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
            out.writeBytes(param);
            // flush and close
            out.flush();
            reader = new BufferedReader(new InputStreamReader(
                    httpConnection.getInputStream(), "utf-8"));
            while ((responseMessage = reader.readLine()) != null) {
                resposne.append(responseMessage);
            }

            if (!"failure".equals(resposne.toString())) {
                logger.info("success send to JMX");
            } else {
                logger.debug("failure send to JMX");
            }
            // 将该url的配置信息缓存起来
            return resposne.toString();
        } catch (IOException e) {
            logger.error("连接失败,url={}" , url);
            return "failed";
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != reader) {
                    reader.close();
                }
                if (null != httpConnection) {
                    httpConnection.disconnect();
                }
            } catch (Exception e2) {
                logger.error("http connection close error:{}", e2);
            }
        }
    }
}
