package com.zs.domain.placeorder;

import java.util.Date;

import javax.persistence.*;

import com.feinno.framework.common.domain.AbstractEntity;
/**
 * 预计单明细数据
 * @author yanghaosen
 *
 */
@Entity
@Table(name = "place_order_teach_material")
public class PlaceOrderTeachMaterial extends AbstractEntity{

	public static final int IS_SEND_NOT = 0;
	public static final int IS_SEND_YES = 1;

	//ID
	private Long id;
	//预订单ID
	private Long orderId;
	//教材ID
	private Long teachMaterialId;
	//课程编码
	private String courseCode;
	//数量
	private Long count;
	//教材单价
	private Float tmPrice;
	//是否发出
	private Integer isSend;
	//创建者
	private String creator;
	//创建时间
	private Date createTime;
	//修改者
	private String operator;
	//修改时间
	private Date operateTime;
	//数据版本
	private Integer version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getTeachMaterialId() {
		return teachMaterialId;
	}
	public void setTeachMaterialId(Long teachMaterialId) {
		this.teachMaterialId = teachMaterialId;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Float getTmPrice() {
		return tmPrice;
	}
	public void setTmPrice(Float tmPrice) {
		this.tmPrice = tmPrice;
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
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	@Version
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}


	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}
}
