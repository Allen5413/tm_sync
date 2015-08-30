package com.zs.tools.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 对应Excel一行
 * @author yanghaosen
 *
 */
public class ExcelRow {
	
	/** 行数 */
	private int row;
	/** 此行包含的单元格 */
	private List<ExcelCell> excelCellList = new ArrayList<ExcelCell>();
	
	public ExcelRow(int row){
		this.row = row;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public List<ExcelCell> getExcelCellList() {
		return excelCellList;
	}

	public void setExcelCellList(List<ExcelCell> excelCellList) {
		this.excelCellList = excelCellList;
	}
	
	public void addExcelCell(ExcelCell cell){
		this.excelCellList.add(cell);
	}
}
