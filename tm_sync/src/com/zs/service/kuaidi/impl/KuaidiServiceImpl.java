package com.zs.service.kuaidi.impl;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zs.config.KuaidiCompanyEnum;
import com.zs.service.kuaidi.KuaidiService;
import com.zs.service.kuaidi.bean.KuaidiOrder;
import com.zs.service.kuaidi.bean.KuaidiRecordInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("kuaidiService")
public class KuaidiServiceImpl implements KuaidiService {
	
	private static final Log logger = LogFactory.getLog(KuaidiServiceImpl.class);

	private String id;
	
	private String url;
	
	private String htmlApiUrl;
	
	private String key;
	
	private String jsonUrl;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getHtmlApiUrl() {
		return htmlApiUrl;
	}

	public void setHtmlApiUrl(String htmlApiUrl) {
		this.htmlApiUrl = htmlApiUrl;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	public String getJsonUrl() {
		return jsonUrl;
	}

	public void setJsonUrl(String jsonUrl) {
		this.jsonUrl = jsonUrl;
	}

	/**
	 * 对接快递100
	 * @param com 快递公司名称
	 * @param nu 快递单号
	 * @param show 返回类型 0：返回json字符串， 1：返回xml对象， 2：返回html对象， 3：返回text文本。 如果不填，默认返回json字符串。
	 * @param muti 返回信息数量： 1:返回多行完整的信息， 0:只返回一行信息。 不填默认返回多行。 
	 * @param order 排序： desc：按时间由新到旧排列， asc：按时间由旧到新排列
	 * @return
	 */
	private String query(String id,String key,String url,String com,String nu,String show,String muti,String order){
		String queryContent = "";
		InputStream urlStream = null;
		try
		{
			//构造url
			if(null != id){
				url += "?id=" + id;
			}
			if(null != key && null == id){
				url += "?key=" + key;
			}
			if(null != com){
				url += "&com=" + com;
			}
			if(null != nu){
				url += "&nu=" + nu;
			}
			if(null != show){
				url += "&show=" + show;
			}
			if(null != muti){
				url += "muti=" + muti;
			}
			if(null != order){
				url += "order=" + order;
			}
			
			
			URL ineUrl= new URL(url);
			URLConnection con=ineUrl.openConnection();
			con.setRequestProperty("charset", "utf-8");
			con.setAllowUserInteraction(false);
		    urlStream = ineUrl.openStream();
		    String type = con.guessContentTypeFromStream(urlStream);
		    String charSet=null;
		    if (type == null){
		    	type = con.getContentType();
		    }

		    if (type == null || type.trim().length() == 0){
		    	return "";
		    }

		    if(type.indexOf("charset=") > 0){
		    	charSet = type.substring(type.indexOf("charset=") + 8);
		    }

		    byte b[] = new byte[10000];
		    int numRead = urlStream.read(b);
		    String content = new String(b, 0, numRead, charSet);
		    while (numRead != -1) {
		    	numRead = urlStream.read(b);
		    	if (numRead != -1) {
		    		String newContent = new String(b, 0, numRead, charSet);
		    		content += newContent;
		    	}
		   }
		   queryContent = content;
		} catch (Exception e){
			logger.error("对接快递100出现异常",e);
		}finally{
			if(null != urlStream){
				 try {
					urlStream.close();
				} catch (IOException e) {
					logger.error("对接快递100关闭输入流出现异常!", e);
				}
			}
		}
		return queryContent;
	}
	
	private String query(String type,String postid){
		String jsonUrlTemp = jsonUrl;
		if(null != type){
			jsonUrlTemp += "?type=" + type;
		}
		if(null != postid){
			jsonUrlTemp += "&postid=" + postid;
		}
		return query(null,null,jsonUrlTemp,null,null,null,null,null);
	}

	@Override
	public String queryForJson(String cpCode, String nu) {
		//调用其直接查询json数据的接口获取数据
		return query(cpCode,nu);
	}
	
//	public KuaidiServiceImpl(){
//		this.id = "70fda4bff7a1a891";
//		this.htmlApiUrl = "http://www.kuaidi100.com/applyurl";
//		this.jsonUrl = "http://www.kuaidi100.com/query";
//		this.url = "http://api.kuaidi100.com/api";
//		this.key = "70fda4bff7a1a891";
//	}
//
//	public static void main(String[] args) {
//		System.out.println(new KuaidiServiceImpl().queryForEMSObject("5163189444103")+"   ...");
//	}

	@Override
	public String queryForXml(String cpCode, String nu, String muti, String sort) {
		return query(id,null,url,cpCode,nu,"1",muti,sort);
	}

	@Override
	public String queryForHtml(String cpCode, String nu) {
		return query(null,key,htmlApiUrl,cpCode,nu,null,null,null);
	}

	@Override
	public String queryForEMSByHtml(String nu) {
		return queryForHtml(KuaidiCompanyEnum.EMS.getCpCode(),nu);
	}

	@Override
	public String queryForShunFengByHtml(String nu) {
		return queryForHtml(KuaidiCompanyEnum.SHUNFENG.getCpCode(),nu);
	}
	
	@Override
	public String queryForEMSByJson(String nu) {
		
		return queryForJson(KuaidiCompanyEnum.EMS.getCpCode(), nu);
	}

	@Override
	public String queryForShunFengByJson(String nu) {
		return queryForJson(KuaidiCompanyEnum.SHUNFENG.getCpCode(), nu);
	}

	@Override
	public KuaidiOrder queryForObject(String cpCode, String nu) {
		
		String json = queryForJson(cpCode, nu);
		return convertJsonToKuaidiOrder(json);
	}

	@Override
	public KuaidiOrder queryForEMSObject(String nu) {
		return queryForObject(KuaidiCompanyEnum.EMS.getCpCode(),nu);
	}

	@Override
	public KuaidiOrder queryForShunFengObject(String nu) {
		return queryForObject(KuaidiCompanyEnum.SHUNFENG.getCpCode(), nu);
	}
	
	private KuaidiOrder convertJsonToKuaidiOrder(String json){
		if(!StringUtils.isEmpty(json)){
			try{
				JSONObject outJsonObj = JSONObject.parseObject(json);
				JSONArray innerJsonAry = outJsonObj.getJSONArray("data");
				
				KuaidiOrder kuaidiOrder = new KuaidiOrder();
				kuaidiOrder.setCom(outJsonObj.getString("com"));
				KuaidiCompanyEnum freightCompany = KuaidiCompanyEnum.getKuaidiCompanyEnumByCode(outJsonObj.getString("com"));
				kuaidiOrder.setComName(null != freightCompany ? freightCompany.getCpName() : "");
				kuaidiOrder.setCondition(outJsonObj.getString("condition"));
				kuaidiOrder.setIsCheck(outJsonObj.getString("ischeck"));
				kuaidiOrder.setMessage(outJsonObj.getString("message"));
				kuaidiOrder.setNu(outJsonObj.getString("nu"));
				kuaidiOrder.setState(outJsonObj.getString("state"));
				kuaidiOrder.setStatus(outJsonObj.getString("status"));
				
				List<KuaidiRecordInfo> recordInfoList = new ArrayList<KuaidiRecordInfo>();
				kuaidiOrder.setRecordInfoList(recordInfoList);
				if(null != innerJsonAry){
					for(int i = 0;i < innerJsonAry.size();i ++){
						JSONObject innerJsonObj = innerJsonAry.getJSONObject(i);
						KuaidiRecordInfo freightRecordInfo =  new KuaidiRecordInfo();
						freightRecordInfo.setContent(innerJsonObj.getString("context"));
						freightRecordInfo.setFtime(innerJsonObj.getDate("ftime"));
						freightRecordInfo.setTime(innerJsonObj.getDate("time"));
						freightRecordInfo.setTimeStr(innerJsonObj.getString("time"));
						
						recordInfoList.add(freightRecordInfo);
					}
				}
				
				return kuaidiOrder;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
}
