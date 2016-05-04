package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生费用入账记录
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "student_expense_pay")
public class StudentExpensePay extends AbstractEntity {

    public final static int PAYUSERTYPE_SPOT = 0;
    public final static int PAYUSERTYPE_STUDENT = 1;

    public final static int PAY_TYPE_ONLINE_BANK= 0;
    public final static int PAY_TYPE_TRANSFER= 1;
    public final static int PAY_TYPE_CASH = 2;
    public final static int PAY_TYPE_OTHER= 3;

    private Long id;                //主键id
    private String studentCode;     //学生学号
    private Float money;            //金额
    private Long invoiceId;         //发票id
    private int payUserType;        //缴费人类型(0: 学习中心，1：学生)
    private String creator;         //创建人——不能修改
    private Date createTime = new Date();        //创建时间——不能修改
    private Date arrivalTime;       //银行到账时间
    private int payType;            //缴费类型[0:网银，1:银行转账，2:现金，3:其它]
    private String remark;          //备注


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
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

    public int getPayUserType() {
        return payUserType;
    }

    public void setPayUserType(int payUserType) {
        this.payUserType = payUserType;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
