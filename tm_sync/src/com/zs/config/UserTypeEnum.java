package com.zs.config;

/**
 * 用户类型
 * Created by Allen on 2015/5/10.
 */
public enum UserTypeEnum {

    ADMIN("0", "管理员"),
    ISSUE("1", "发行商"),
    SUPPLY("2", "供应商"),
    EDUWESTADMIN("3", "网院管理员"),
    PROVINCE("4", "省中心"),
    SPOT("5", "学习中心"),
    STUDENT("6", "学生");

    UserTypeEnum(String value,String descn){
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
