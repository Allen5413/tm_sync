package com.zs.domain.basic;


import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 出版社
 * Created by lihongZhang on 2015/4/30.
 */
@Entity
@Table(name = "press")
public class Press extends AbstractEntity {
    private Long id;   //主键
    private String code;    //编号
    private String name;    //出版社名称
    private String tellPhone;   //单位电话
    private String cellPhone;  //手机
    private String contact;     //联系人
    private String address;     //单位地址
    private String accountBank;    //开户银行
    private String accountName;    //开户名称
    private String bankCode;       //银行账号
    private String tariffCode;     //税号
    private String remark;          //备注
    private String creator;         //创建人
    private Date createTime = new Date();     //创建时间
    private String operator;        //操作人
    private Date operateTime = new Date();   //操作时间
    private Integer version;        //版本号

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getCode() {
        return StringUtils.isEmpty(code) ? "" : code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "" : name.trim();
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAccountBank() {
        return StringUtils.isEmpty(accountBank) ? "" : accountBank.trim();
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getAccountName() {
        return StringUtils.isEmpty(accountName) ? "" : accountName.trim();
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankCode() {
        return StringUtils.isEmpty(bankCode) ? "" : bankCode.trim();
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getTariffCode() {
        return StringUtils.isEmpty(tariffCode) ? "" : tariffCode.trim();
    }

    public void setTariffCode(String tariffCode) {
        this.tariffCode = tariffCode;
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

    public String getTellPhone() {
        return tellPhone;
    }

    public void setTellPhone(String tellPhone) {
        this.tellPhone = tellPhone;
    }
}
