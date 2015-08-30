package com.zs.tools.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 对应Excel一个sheet
 * @author yanghaosen
 *
 */
public class ExcelSheet {
	
	/** 此sheet所包含的行 */
	private List<ExcelRow> excelRowList = new ArrayList<ExcelRow>();

	public List<ExcelRow> getExcelRowList() {
		return excelRowList;
	}

	public void setExcelRowList(List<ExcelRow> excelRowList) {
		this.excelRowList = excelRowList;
	}
	
	public void addExcelRow(ExcelRow row){
		this.excelRowList.add(row);
	}
}
