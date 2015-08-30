package com.zs.domain.placeorder;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.feinno.framework.common.domain.AbstractEntity;
/**
 * 
 * @author 预订单日志
 *
 */
@Entity
@Table(name = "place_order_log")
public class PlaceOrderLog extends AbstractEntity{
	
	//ID
	private Long id;
	//订单ID
	private Long orderId;
	//订单状态
	private String state;
	//操作者
	private String operator;
	//操作时间
	private Date operateTime;
	
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	
	
	
}
