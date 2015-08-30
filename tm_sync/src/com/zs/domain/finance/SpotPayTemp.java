package com.zs.domain.finance;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.feinno.framework.common.domain.AbstractEntity;

/**
 * 
 * @author yanghaosen
 *
 */
@Entity
@Table(name = "spot_pay_temp")
public class SpotPayTemp extends AbstractEntity{
	
	private Long id;
	
	private Long ownId;
	
	private Long polId;
	
	private String stuCode;
	
	private Float stuMoney;
	
	private String creator;
	
	private Date createTime = new Date();
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public Float getStuMoney() {
		return stuMoney;
	}

	public void setStuMoney(Float stuMoney) {
		this.stuMoney = stuMoney;
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

	public Long getOwnId() {
		return ownId;
	}

	public void setOwnId(Long ownId) {
		this.ownId = ownId;
	}

	public Long getPolId() {
		return polId;
	}

	public void setPolId(Long polId) {
		this.polId = polId;
	}

}
