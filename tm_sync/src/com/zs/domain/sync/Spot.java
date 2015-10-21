package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习中心表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_spot")
public class Spot extends AbstractEntity {

    public static final int SEX_MAN = 0;    //男
    public static final int SEX_FEMALE = 1; //女

    public static final int IS_STUDENT_LOGIN_YES = 0;
    public static final int IS_STUDENT_LOGIN_NOT = 1;

    private Long id;            //主键
    private String code;        //编号
    private String name;        //名称
    private String adminName;   //管理员姓名
    private Integer sex;        //性别[0:男；1:女]
    private String email;       //管理员电子邮箱
    private String phone;       //管理员手机
    private String tel;         //管理员固定电话
    private String address;     //管理员地址
    private String postalCode;  //管理员邮编
    private Integer isStudentLogin;     //是否允许学生登录
    private Date operateTime = new Date();  //操作时间


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getIsStudentLogin() {
        return isStudentLogin;
    }

    public void setIsStudentLogin(Integer isStudentLogin) {
        this.isStudentLogin = isStudentLogin;
    }
}
