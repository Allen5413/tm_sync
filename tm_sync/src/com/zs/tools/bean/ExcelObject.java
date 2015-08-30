package com.zs.tools.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Excel对象,对应一个Excel文件
 * @author yanghaosen
 *
 */
public class ExcelObject {
	
	/** 此excel所包含的sheet */
	private List<ExcelSheet> excelSheetList = new ArrayList<ExcelSheet>();

	public List<ExcelSheet> getExcelSheetList() {
		return excelSheetList;
	}

	public void setExcelSheetList(List<ExcelSheet> excelSheetList) {
		this.excelSheetList = excelSheetList;
	}
	
	public void addExcelSheet(ExcelSheet sheet){
		this.excelSheetList.add(sheet);
	}
	
}
