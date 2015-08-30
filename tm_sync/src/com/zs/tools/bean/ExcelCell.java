package com.zs.tools.bean;
/**
 * 对应Excel中的一个单元格
 * @author yanghaosen
 *
 */
public class ExcelCell {
	
	/** 所处的行数 */
	private int row;
	
	/** 所处的列数 */
	private byte col;
	
	/** 合并行数 */
	private int mergeRow;
	
	/** 写入值 */
	private String value;
	
	/** 合并列数 */
	private byte mergeCol;
	
	/** 是否出现写入在单元格的左边,否写入在中间 */
	private boolean ifleft;
	
	/** 字体颜色 */
	private String fontColor = "";
	
	/** 单元格大小 */
	private int celSize;

	public ExcelCell(int row, byte col,String value,boolean ifleft,String fontColor,int celSize) {
		this.row = row;
		this.col = col;
		this.value = value;
		this.ifleft = ifleft;	
		this.fontColor = fontColor;
		this.celSize = celSize;
	}

	public ExcelCell(int row, byte col, int mergeRow, byte mergeCol,String value,boolean ifleft,String fontColor,int celSize) {
		this.row = row;
		this.col = col;
		this.mergeRow = mergeRow;
		this.mergeCol = mergeCol;
		this.value = value;
		this.ifleft = ifleft;
		this.fontColor = fontColor;
		this.celSize = celSize;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public byte getCol() {
		return col;
	}

	public void setCol(byte col) {
		this.col = col;
	}

	public int getMergeRow() {
		return mergeRow;
	}

	public void setMergeRow(int mergeRow) {
		this.mergeRow = mergeRow;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public byte getMergeCol() {
		return mergeCol;
	}

	public void setMergeCol(byte mergeCol) {
		this.mergeCol = mergeCol;
	}

	public boolean isIfleft() {
		return ifleft;
	}

	public void setIfleft(boolean ifleft) {
		this.ifleft = ifleft;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public int getCelSize() {
		return celSize;
	}

	public void setCelSize(int celSize) {
		this.celSize = celSize;
	}
}
