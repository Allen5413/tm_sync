package com.zs.service.kuaidi.impl;

import com.alibaba.fastjson.JSONObject;
import com.zs.config.KuaidiCompanyEnum;
import com.zs.service.kuaidi.KuaidiwoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Allen on 2015/9/1.
 */
@Service("kuaidiwoService")
public class KuaidiwoServiceImpl implements KuaidiwoService {

    private static final Log logger = LogFactory.getLog(KuaidiwoServiceImpl.class);

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String url;
    private String key;

    @Override
    public JSONObject queryForEMSByJson(String cno) {
        JSONObject jsonObject = this.queryForObject(KuaidiCompanyEnum.EMS.getCpCode(), cno);
        return jsonObject;
    }

    public JSONObject queryForObject(String com, String cno) {
        String jsonStr = this.query(com, cno, "desc");
        return JSONObject.parseObject(jsonStr);
    }

    /**
     * 对接快递窝
     * @param com 快递公司名称
     * @param cno 快递单号
     * @param order 排序： desc：按时间由新到旧排列， asc：按时间由旧到新排列
     * @return
     */
    private String query(String com,String cno,String order){
        String queryContent = "";
        InputStream urlStream = null;
        try
        {
            this.url = getUrl()+"key="+getKey()+"&com="+com+"&cno="+cno+"&sort="+order;
            URL ineUrl= new URL(url);
            URLConnection urlConnection = ineUrl.openConnection();
            urlConnection.setRequestProperty("charset", "utf-8");
            urlConnection.setAllowUserInteraction(false);
            urlStream = ineUrl.openStream();
            String type = urlConnection.guessContentTypeFromStream(urlStream);
            String charSet=null;
            if (type == null){
                type = urlConnection.getContentType();
            }

            if (type == null || type.trim().length() == 0){
                return "";
            }

            if(type.indexOf("charset=") > 0){
                charSet = type.substring(type.indexOf("charset=") + 8);
            }

            byte b[] = new byte[10000];
            int numRead = urlStream.read(b);
            String content = new String(b, 0, numRead);
            while (numRead != -1) {
                numRead = urlStream.read(b);
                if (numRead != -1) {
                    String newContent = new String(b, 0, numRead);
                    content += newContent;
                }
            }
            queryContent = content;
        } catch (Exception e){
            logger.error("对接快递窝出现异常",e);
        }finally{
            if(null != urlStream){
                try {
                    urlStream.close();
                } catch (IOException e) {
                    logger.error("对接快递窝关闭输入流出现异常!", e);
                }
            }
        }
        return queryContent;
    }
}
