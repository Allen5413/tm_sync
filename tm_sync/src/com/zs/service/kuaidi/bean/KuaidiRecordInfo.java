package com.zs.service.kuaidi.bean;

import java.util.Date;

/**
 * 快递单中每一条记录信息
 * @author Administrator
 *
 */
public class KuaidiRecordInfo {
	
	/** 记录时间 */
	private Date time;
	
	/** 记录内容 */
	private String content;
	
	private Date ftime;
	
	private String timeStr;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getFtime() {
		return ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	
}
