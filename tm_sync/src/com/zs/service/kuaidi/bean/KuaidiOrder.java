package com.zs.service.kuaidi.bean;

import java.util.List;

/**
 * 快递单(针对快递100接口返回的json数据解析)
 * @author yanghaosen
 *
 */
public class KuaidiOrder {
	
	/** 快递单查询的描述信息 */
	private String message;
	
	/** 快递单号 */
	private String nu;
	
	private String isCheck;
	
	/** 快递公司编码 */
	private String com;
	
	/** 快递公司名称 */
	private String comName;
	
	/** 状态 */
	private String status;
	
	private String condition;
	
	/** 快递单当前状态 
	 * 0：在途，即货物处于运输过程中
	 * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
     * 2：疑难，货物寄送过程出了问题
     * 3：签收，收件人已签收
     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
     * 5：派件，即快递正在进行同城派件
     * 6：退回，货物正处于退回发件人的途中
     */
	private String state;
	
	/** 快递单包含的快递记录信息 */
	private List<KuaidiRecordInfo> recordInfoList;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<KuaidiRecordInfo> getRecordInfoList() {
		return recordInfoList;
	}

	public void setRecordInfoList(List<KuaidiRecordInfo> recordInfoList) {
		this.recordInfoList = recordInfoList;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}
	
}
