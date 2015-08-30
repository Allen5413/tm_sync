package com.zs.domain.finance;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yanghaosen
 *
 */
@Entity
@Table(name = "spot_expense_oth")
public class SpotExpenseOth {
	
	private Integer id;
	
	private String spotCode;
	
	private float pay;
	
	private float buy;
	
	private float stuOwnTot;	//学习中心学生账户欠款
	
	private float stuAccTot;   //学习中心学生账户余额
	
	private Integer state;		//【0：已结算，1：未结算】
	
	private String creator;
	
	private Date createTime = new Date();
	
	private Date clearTime;
	
	private String operator;
	
	private Date operateTime = new Date();
	
	private Long semesterId;
	
	private Integer version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpotCode() {
		return spotCode;
	}

	public void setSpotCode(String spotCode) {
		this.spotCode = spotCode;
	}

	public float getPay() {
		return pay;
	}

	public void setPay(float pay) {
		this.pay = pay;
	}

	public float getBuy() {
		return buy;
	}

	public void setBuy(float buy) {
		this.buy = buy;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public Date getClearTime() {
		return clearTime;
	}

	public void setClearTime(Date clearTime) {
		this.clearTime = clearTime;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}

	public float getStuOwnTot() {
		return stuOwnTot;
	}

	public void setStuOwnTot(float stuOwnTot) {
		this.stuOwnTot = stuOwnTot;
	}

	public float getStuAccTot() {
		return stuAccTot;
	}

	public void setStuAccTot(float stuAccTot) {
		this.stuAccTot = stuAccTot;
	}
	
}
