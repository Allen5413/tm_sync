package com.zs.tools;

import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Allen on 2017/10/12.
 */
public class AttopUtil {

    private static String portUrl;
    private static String secret;
    private static String shopId;

    static{
        try {
            PropertiesTools propertiesTools = new PropertiesTools("resource/commons.properties");
            portUrl = propertiesTools.getProperty("attop.portUrl");
            secret = propertiesTools.getProperty("attop.secret");
            shopId = propertiesTools.getProperty("attop.shopId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        String shopId="1001";
//        String secret="BL4vOSGzQ3iE7ah1umNdvAV8escNQPlzkTP1NcTuhWBg4WObrXFPew5e28lXafjot2SQniZ4WbDIBuFSgNz9bqUoxZ44EUxT7hWbcdZCeIrTowhN3nBCdHeO1DFMFtlg";
//        String portUrl="http://ttt.attop.com/dangport/attop.do";

        String shopId = "1002";
        String secret = "8ioi548wl7nHnp3LBNzFRC6VmbvATOBr4j7vZb5BMlU1anz21SuOujIFziWHJGwZKcc12uyF9hFWccKxKLg39JjFqpVLdhJ2wQHQOZ5D7KzILr6R0gc1Me4FTw04cGMU";
        String portUrl = "http://www.attop.com/dangport/attop.do";

        JSONObject json = getPortMsg(201,"zz=281536&msg=", shopId, secret, portUrl);
        System.out.println(json);
    }


    public static int sendMsg(String mobile, int mb, String msg){
        String mbCode = "";
        if(0 == mb){
            //旧生模板
            mbCode = "SMS_141581069";
        }else{
            //新生模板
            mbCode = "SMS_141596124";
        }
        JSONObject json = AttopUtil.getPortMsg(205, "mobile=" + mobile+"&mb="+mbCode+"&msg="+msg, shopId, secret, portUrl);
        if(null != json){
            return null == json.get("status") ? 0 : Integer.parseInt(json.get("status").toString());
        }
        return 0;
    }


    public static JSONObject getPortMsg(int funcId,String query,String shopId,String secret,String portUrl){
        String result="";
        try{
            SortedMap<String,Object> p=new TreeMap<String,Object>();
            p.put("shopId", shopId);
            p.put("funcId", funcId);
            if(!(query==null||"".equals(query.trim()))){
                String[] a=query.split("\\&");
                for(String b:a){
                    String[] c=b.split("\\=",2);
                    p.put(c[0],toUrlDecode(c[1]));
                }
            }
            String mac=createMac(p,secret);
            String url=portUrl+"?funcId="+funcId+"&shopId="+shopId;
            if(!(query==null||"".equals(query.trim()))){
                url+="&"+query+"&mac="+mac;
            }else{
                url+="&mac="+mac;
            }
            System.out.println(url);
            result=getContent(url);
        }catch(Throwable e){
            e.printStackTrace();
        }
        return JSONObject.parseObject(result);
    }
    public static String getContent(String url) {
        String result ="";
        try {
            URL ml = new URL(url);
            URLConnection mcl = ml.openConnection();
            DataInputStream is=new DataInputStream(mcl.getInputStream());
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            byte[] bf=new byte[128];
            int len=0;
            while ( (len=is.read(bf))!= -1) {
                tmp.write(bf,0,len);
            }
            result = new String(tmp.toByteArray(),"utf-8");
            is.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;

    }
    public static String createMac(SortedMap<String,Object> parameters,String secret){
        String result="";
        try{
            StringBuffer sb = new StringBuffer();
            Set es = parameters.entrySet();
            Iterator it = es.iterator();
            while(it.hasNext()) {
                Entry entry = (Entry)it.next();
                String k = (String)entry.getKey();
                Object v = entry.getValue();
                if(null != v && !"".equals(v)  && !"mac".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
            sb.append("&secret=" + secret);
            result=getMd5(sb.toString());
        }catch(Throwable e){
            e.printStackTrace();
        }
        return result;
    }

    public static String getMd5(String text) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("UTF-8"));
            byte[] s = md.digest();
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00)
                        .substring(6);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return result;
    }
    public static String toUrlEncode(String str){
        String result=str;
        try{
            result = URLEncoder.encode(str, "utf-8");
        }catch(Throwable e){
            e.printStackTrace();
        }
        return result;
    }
    public static String toUrlDecode(String str){
        String result=str;
        try{
            result = URLDecoder.decode(str, "utf-8");
        }catch(Throwable e){
            e.printStackTrace();
        }
        return result;
    }
}
