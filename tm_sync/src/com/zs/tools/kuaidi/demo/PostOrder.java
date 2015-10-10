package com.zs.tools.kuaidi.demo;

import com.zs.tools.PropertiesTools;
import com.zs.tools.kuaidi.pojo.TaskRequest;
import com.zs.tools.kuaidi.pojo.TaskResponse;

import java.util.HashMap;


public class PostOrder {

	//快递100Http请求url
	private static String kuaidiReqUrl;
	//快递100Http回调url
	private static String kuaidiCallBackUrl;
	//快递100 key
	private static String key;

	static{
		try {
			PropertiesTools propertiesTools = new PropertiesTools("resource/commons.properties");
			kuaidiReqUrl = propertiesTools.getProperty("kuaidi100.url");
			kuaidiCallBackUrl = propertiesTools.getProperty("kuaidi100.callback.url");
			key = propertiesTools.getProperty("kuaidi100.key");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 请求快递100
	 * @return
	 * @throws Exception
	 */
	public static TaskResponse reqKuaidi100(String com, String nu)throws Exception{
		TaskResponse resp = null;
		TaskRequest req = new TaskRequest();
		req.setCompany(com);
		req.setNumber(nu);
		req.getParameters().put("callbackurl", kuaidiCallBackUrl);
		req.setKey(key);

		HashMap<String, String> p = new HashMap<String, String>();
		p.put("schema", "json");
		p.put("param", JacksonHelper.toJSON(req));
		try {
			String ret = HttpRequest.postData(kuaidiReqUrl, p, "UTF-8");
			resp = JacksonHelper.fromJSON(ret, TaskResponse.class);
			if(resp.getResult()==true){
				System.out.println("快递100请求订阅成功");
			}else{
				System.out.println("快递100请求订阅失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	public static void main(String[] args){
		TaskRequest req = new TaskRequest();
		req.setCompany("ems");
		req.setNumber("9920051849015");
		req.getParameters().put("callbackurl", "http://xiwang.attop.com:8080/kuaidiPush/push.htm");
		req.setKey("bwgCJUSX1701");
		
		HashMap<String, String> p = new HashMap<String, String>(); 
		p.put("schema", "json");
		p.put("param", JacksonHelper.toJSON(req));
		try {
			String ret = HttpRequest.postData("http://www.kuaidi100.com/poll", p, "UTF-8");
			TaskResponse resp = JacksonHelper.fromJSON(ret, TaskResponse.class);
			if(resp.getResult()==true){
				System.out.println("订阅成功");
			}else{
				System.out.println("订阅失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
