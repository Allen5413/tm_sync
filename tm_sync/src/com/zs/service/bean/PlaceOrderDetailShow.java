package com.zs.service.bean;

import java.util.Date;

/**
 * 预订单明细数据页面展示
 * @author yanghaosen
 *
 */
public class PlaceOrderDetailShow {
	
	//明细数据ID
	private Long placeOrderDetailId;
	private Long orderId;
	private String courseCode;
	private String courseName;
	//教材ID
	private Long materialId;
	//教材名称
	private String materialName;
	//教材单价
	private Float materialPrice;
	//数量
	private int count;
	private String creator;
	private Date createTime;
	private Long packageId;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getPlaceOrderDetailId() {
		return placeOrderDetailId;
	}
	public void setPlaceOrderDetailId(Long placeOrderDetailId) {
		this.placeOrderDetailId = placeOrderDetailId;
	}
	public Long getMaterialId() {
		return materialId;
	}
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public Float getMaterialPrice() {
		return materialPrice;
	}
	public void setMaterialPrice(Float materialPrice) {
		this.materialPrice = materialPrice;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}


	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getPackageId() {
		return packageId;
	}

	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
}
