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

    private Long id;                //主键id
    private String studentCode;     //学生学号
    private Float money;            //金额
    private Long invoiceId;         //发票id
    private String creator;         //创建人——不能修改
    private Date createTime = new Date();        //创建时间——不能修改

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
}
