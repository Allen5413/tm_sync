package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 确认金额日志
 * Created by LihongZhang on 2015/5/17.
 */
@Entity
@Table(name = "spot_insure_log")
public class SpotInsureLog extends AbstractEntity {

    private Long id;                //编号
    private String spotCode;        //学习中心编号
    private Float insureMoney;      //确认金额
    private String creator;         //创建人
    @Temporal(TemporalType.TIMESTAMP)
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

    public Float getInsureMoney() {
        return insureMoney;
    }

    public void setInsureMoney(Float insureMoney) {
        this.insureMoney = insureMoney;
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
