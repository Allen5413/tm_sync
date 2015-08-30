package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习中心入账明细
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "spot_expense_pay")
public class SpotExpensePay extends AbstractEntity{

    public static int STATE_NOT = 0;    //未确认
    public static int STATE_YES = 1;    //确认

    private Long id;                //主键id
    private String spotCode;        //学习中心编号
    private Float money;            //支付金额
    private Long invoiceId;         //发票id
    private Integer state;          //状态[0:未确认；1:已确认]
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


    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
