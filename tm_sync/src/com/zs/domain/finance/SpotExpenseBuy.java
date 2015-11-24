package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习中心费用消费记录
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "spot_expense_buy")
public class SpotExpenseBuy extends AbstractEntity{

    public static final int TYPE_BUY_TM = 0;            //购买教材
    public static final int TYPE_TM_UPDATE_PRICE = 1;   //教材改价

    private Long id;                //主键id
    private String spotCode;     //学习中心编号
    private Float money;            //消费金额
    private int type;               //消费类型
    private Long semesterId;             //学期id
    private String detail;          //消费明细
    private String creator;         //创建人
    private Date createTime = new Date();        //创建时间

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }
}
