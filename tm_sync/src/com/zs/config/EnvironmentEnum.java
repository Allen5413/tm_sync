package com.zs.config;

/**
 * 环境变量
 * Created by Allen on 2015/5/13.
 */
public enum EnvironmentEnum {
    IS_NOT_STOCK_SEND_BOOK("IS_NOT_STOCK_SEND_BOOK", "是否允许零库存发书");

    EnvironmentEnum(String value,String descn){
        this.value = value;
        this.descn = descn;
    }
    private final String value;
    private final String descn;

    public String getValue() {
        return value;
    }
    public String getDescn(){
        return descn ;
    }
}
