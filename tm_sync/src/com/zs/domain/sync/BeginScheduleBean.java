package com.zs.domain.sync;

public class BeginScheduleBean {
	
	private Integer enterYear;          //入学年
    private Integer quarter;            //入学季
    private Integer academicYear;       //开课年
    private Integer term;               //开课季
    private String specCode;            //专业编号
    private String levelCode;           //层次编号
    private String specName;
    private String levelName;
    
	public Integer getEnterYear() {
		return enterYear;
	}
	public void setEnterYear(Integer enterYear) {
		this.enterYear = enterYear;
	}
	public Integer getQuarter() {
		return quarter;
	}
	public void setQuarter(Integer quarter) {
		this.quarter = quarter;
	}
	public Integer getAcademicYear() {
		return academicYear;
	}
	public void setAcademicYear(Integer academicYear) {
		this.academicYear = academicYear;
	}
	public Integer getTerm() {
		return term;
	}
	public void setTerm(Integer term) {
		this.term = term;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
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
    
    
}
