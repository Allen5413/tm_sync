package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习中心欠费额度
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "spot_arrears_amount")
public class SpotArrearsAmount extends AbstractEntity{
    private Long id;                //主键id
    private String spotCode;        //学习中心编号
    private Float money;            //欠费额度
    private String creator;         //创建人
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime  = new Date();        //创建时间
    private String operator;        //操作人
    @Temporal(TemporalType.TIMESTAMP)
    private Date operateTime = new Date();       //操作时间
    private Integer version;        //版本号

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
