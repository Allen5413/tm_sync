package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;

/**
 * 自有用户
 * Created by Allen on 2015/4/25.
 */
@Entity
@Table(name = "user")
public class User extends AbstractEntity {

    public static final int TYPE_ADMIN = 0;   //管理员
    public static final int TYPE_ISSUE = 1;   //发行商
    public static final int TYPE_SUPPLY = 2;  //供应商

    public static final int STATE_ENABLE = 0;     //启用
    public static final int STATE_DISABLE = 1;    //停用

    private Long id;                            //主键
    private String loginName;                   //登录名
    private String name;                        //姓名
    private String password;                    //密码
    private String companyCode;                 //单位编号
    private String companyName;                 //单位名称
    private Integer	issueRangeId;               //发行范围
    private	String telPhone;                    //单位电话（座机）
    private String cellPhone;                   //手机
    private String contact;                     //联系人
    private String address;                     //单位地址
    private Integer	type;                       //用户类型[0：管理员; 1：发行商; 2：供应商]
    private Integer state;                      //用户状态[0：启用; 1：停用]
    private String remark;                      //备注
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private Integer version;                    //版本号，用于乐观锁

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {return StringUtils.isEmpty(loginName) ? "" : loginName.trim();}
    public void setLoginName(String loginName) {this.loginName = loginName;}

    public String getName() {return StringUtils.isEmpty(name) ? "" : name;}
    public void setName(String name) {this.name = name;}

    public String getPassword() {return StringUtils.isEmpty(password) ? "" : password.trim();}
    public void setPassword(String password) {this.password = password;}

    public String getCompanyCode() {
        return StringUtils.isEmpty(companyCode) ? "" : companyCode.trim();
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return StringUtils.isEmpty(companyName) ? "" : companyName.trim();
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getIssueRangeId() {
        return issueRangeId;
    }

    public void setIssueRangeId(Integer issueRangeId) {
        this.issueRangeId = issueRangeId;
    }

    public String getTelPhone() {
        return StringUtils.isEmpty(telPhone) ? "" : telPhone.trim();
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getCellPhone() {
        return StringUtils.isEmpty(cellPhone) ? "" : cellPhone.trim();
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getContact() {
        return StringUtils.isEmpty(contact) ? "" : contact.trim();
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return StringUtils.isEmpty(address) ? "" : address.trim();
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRemark() {
        return StringUtils.isEmpty(remark) ? "" : remark.trim();
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
