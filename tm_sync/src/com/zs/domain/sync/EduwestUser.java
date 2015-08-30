package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 网院用户表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_user")
public class EduwestUser extends AbstractEntity {

    public static final int SEX_MAN = 0;    //男
    public static final int SEX_FEMALE = 1; //女

    public static final int TYPE_ADMIN = 0;
    public static final int TYPE_PROVINCE = 1;
    public static final int TYPE_SPOT = 2;
    public static final int TYPE_STUDENT = 3;
    public static final int TYPE_DEPT = 4;

    public final static Integer STATE_ENABLE = 0;
    public final static Integer STATE_DISABLE = 1;

    private Long id;              //主键
    private String pin;           //登录名
    private String name;          //姓名
    private Integer sex;          //性别[0:男; 1:女]
    private String nickName;      //昵称
    private String tel;           //电话
    private String mobile;        //手机
    private Integer type;         //类型[教材：0:管理员；1省中心用户；2学习中心用户；3学生；4学院用户  --  网院：37--外来学生 41--学生 42--教师 43--网院管理员 44--学习中心管理员 45--院系管理员 46--省中心管理员]
    private Integer state;        //状态[教材：0:启用；1:停用  --  网院：21:启用；22:停用]
    private String provCode;      //省中心编号
    private String spotCode;      //学习中心编号
    private String deptCode;      //学院编号
    private Date operateTime = new Date();  //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
