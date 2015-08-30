package com.zs.config;
/**
 * 快递公司
 * @author yanghaosen
 *
 */
public enum KuaidiCompanyEnum {
	
	/** EMS */
	EMS("ems","EMS"),
	/** 顺丰 */
	SHUNFENG("shunfeng","顺丰速运"),
	/** 申通 */
	SHENTONG("shentong","申通快递"),
	/** 圆通 */
	YUANTONG("yuantong","圆通速运"),
	/** 韵达 */
	YUNDA("yunda","韵达快运"),
	/** 中通 */
	ZHONGTONG("zhongtong","中通速递"),
	/** 宅急送 */
	ZHAIJISONG("zhaijisong","宅急送"),
	/** 天天 */
	TIANTIAN("tiantian","天天快递"),
	/** 汇通 */
	HUITONGKUAIDI("huitongkuaidi","汇通快运");
	
	private String cpCode;
	
	private String cpName;
	
	private KuaidiCompanyEnum(String cpCode, String cpName){
		this.cpCode = cpCode;
		this.cpName = cpName;
	}

	public String getCpCode() {
		return cpCode;
	}

	public String getCpName() {
		return cpName;
	}
	
	public static KuaidiCompanyEnum getKuaidiCompanyEnumByCode(String nu){
		KuaidiCompanyEnum[] kuaidiCompanyEnumArr = KuaidiCompanyEnum.values();
		for(KuaidiCompanyEnum kuaidiCompanyEnum : kuaidiCompanyEnumArr){
			if(kuaidiCompanyEnum.getCpCode().equals(nu)){
				return kuaidiCompanyEnum;
			}
		}
		return null;
	}
	
}
