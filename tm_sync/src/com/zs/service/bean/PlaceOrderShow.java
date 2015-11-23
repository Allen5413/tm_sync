package com.zs.service.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 预订单数据页面展示
 * @author yanghaosen
 *
 */
public class PlaceOrderShow {
	
	//预订单ID
	private Long placeOrderId;
	//预订单号
	private String placeOrderCode;
	//预订单价格
	private Float placePrice;
	//入学年
	private String enYear;
	//入学季
	private String enQuarter;
	//专业名称
	private String specName;
	//层次名称
	private String levelName;
	//人数
	private int personNum;
	//明细数据
	private List<PlaceOrderDetailShow> orderDetailList = new ArrayList<PlaceOrderDetailShow>();
	
	public Long getPlaceOrderId() {
		return placeOrderId;
	}
	public void setPlaceOrderId(Long placeOrderId) {
		this.placeOrderId = placeOrderId;
	}
	public String getPlaceOrderCode() {
		return placeOrderCode;
	}
	public void setPlaceOrderCode(String placeOrderCode) {
		this.placeOrderCode = placeOrderCode;
	}
	public Float getPlacePrice() {
		return placePrice;
	}
	public void setPlacePrice(Float placePrice) {
		this.placePrice = placePrice;
	}
	public String getEnYear() {
		return enYear;
	}
	public void setEnYear(String enYear) {
		this.enYear = enYear;
	}
	public String getEnQuarter() {
		return enQuarter;
	}
	public void setEnQuarter(String enQuarter) {
		this.enQuarter = enQuarter;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	public List<PlaceOrderDetailShow> getOrderDetailList() {
		return orderDetailList;
	}
	public void setOrderDetailList(List<PlaceOrderDetailShow> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
}
