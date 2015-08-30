package com.zs.domain.finance;

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
@Table(name = "center_pay_log")
public class CenterPayLog {
	
	private Long id;
	
	private Long polId;
	
	private Long payTempId;
	
	private String spotCode;
	
	private String stuCode;
	
	private String doPayDescription;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPolId() {
		return polId;
	}

	public void setPolId(Long polId) {
		this.polId = polId;
	}

	public Long getPayTempId() {
		return payTempId;
	}

	public void setPayTempId(Long payTempId) {
		this.payTempId = payTempId;
	}

	public String getSpotCode() {
		return spotCode;
	}

	public void setSpotCode(String spotCode) {
		this.spotCode = spotCode;
	}

	public String getStuCode() {
		return stuCode;
	}

	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}

	public String getDoPayDescription() {
		return doPayDescription;
	}

	public void setDoPayDescription(String doPayDescription) {
		this.doPayDescription = doPayDescription;
	}
	
	
}
