package com.zs.service.kuaidi;


import com.zs.service.kuaidi.bean.KuaidiOrder;

/**
 * 快递查询服务
 * @author yanghaosen
 *
 */
public interface KuaidiService {
	
	/**
	 * 查询JSON结果数据
	 * @param cpCode 快递公司代码
	 * @param nu 快递单号
	 * @return JSON结果数据
	 */
	public String queryForJson(String cpCode, String nu);
	
	/**
	 * 查询XML结果数据(目前 EMS,顺丰,申通,圆通,韵达,中通不支持该数据结构返回)
	 * @param cpCode 快递公司代码
	 * @param nu 快递单号
	 * @param muti 是否多行信息(1:是,0:不是)
	 * @param sort 排序方式("desc":倒序,"asc":顺序)
	 * @return XML结果数据
	 */
	public String queryForXml(String cpCode, String nu, String muti, String sort);
	
	/**
	 * 查询HTML_API结果,该结果返回一个可访问的URL，页面通过访问该URL,可展示快递信息
	 * @param cpCode 快递公司代码
	 * @param nu 快递单号
	 * @return 可访问的URL
	 */
	public String queryForHtml(String cpCode, String nu);
	
	/**
	 * 查询EMS快递信息(HTML_API)
	 * @param nu 快递单号
	 * @return 可访问的URL
	 */
	public String queryForEMSByHtml(String nu);
	
	/**
	 * 查询顺丰快递信息(HTML_API)
	 * @param nu 快递单号
	 * @return 可访问的URL
	 */
	public String queryForShunFengByHtml(String nu);
	
	/**
	 * 查询EMS快递信息(JSON)
	 * @param nu 快递单号
	 * @return json格式的数据
	 */
	public String queryForEMSByJson(String nu);
	
	/**
	 * 查询顺丰快递信息(JSON)
	 * @param nu 快递单号
	 * @return json格式的数据
	 */
	public String queryForShunFengByJson(String nu);
	
	/**
	 * 查询快递信息(Object)
	 * @param cpCode 快递公司编码
	 * @param nu 快递单号
	 * @return 快递单信息对象
	 */
	public KuaidiOrder queryForObject(String cpCode, String nu);
	
	/**
	 * 查询EMS快递信息(Object)
	 * @param nu 快递单号
	 * @return 快递单信息对象
	 */
	public KuaidiOrder queryForEMSObject(String nu);
	
	/**
	 * 查询顺丰快递信息(Object)
	 * @param nu 快递单号
	 * @return 快递单信息对象
	 */
	public KuaidiOrder queryForShunFengObject(String nu);
	
}
