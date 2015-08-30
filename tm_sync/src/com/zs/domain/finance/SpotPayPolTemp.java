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
@Table(name = "spot_pay_pol_temp")
public class SpotPayPolTemp extends AbstractEntity{

	private Long id;
	
	private String spotCode;
	
	private Long ownId;
	
	private Float spotMoney;
	
	private String imagUrl;
	
	private String isSure;
	
	private String verifyer;
	
	private Date verifyTime;
	
	private String payWay;
	
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

	public String getSpotCode() {
		return spotCode;
	}

	public void setSpotCode(String spotCode) {
		this.spotCode = spotCode;
	}

	public Long getOwnId() {
		return ownId;
	}

	public void setOwnId(Long ownId) {
		this.ownId = ownId;
	}

	public Float getSpotMoney() {
		return spotMoney;
	}

	public void setSpotMoney(Float spotMoney) {
		this.spotMoney = spotMoney;
	}

	public String getImagUrl() {
		return imagUrl;
	}

	public void setImagUrl(String imagUrl) {
		this.imagUrl = imagUrl;
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

	public String getIsSure() {
		return isSure;
	}

	public void setIsSure(String isSure) {
		this.isSure = isSure;
	}

	public String getVerifyer() {
		return verifyer;
	}

	public void setVerifyer(String verifyer) {
		this.verifyer = verifyer;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	
}
