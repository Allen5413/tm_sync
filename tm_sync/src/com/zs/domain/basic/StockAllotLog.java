package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 教材库存调拨记录
 * Created by Allen on 2015/5/27.
 */
@Entity
@Table(name = "stock_allot_log")
public class StockAllotLog extends AbstractEntity {

    private Long id;                            //主键
    private Long oldIssueChannelId;             //原来的发行渠道id
    private Long newIssueChannelId;             //调拨后的发行渠道id
    private Long teachMaterialId;               //教材id
    private Long stock;                         //库存
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOldIssueChannelId() {
        return oldIssueChannelId;
    }

    public void setOldIssueChannelId(Long oldIssueChannelId) {
        this.oldIssueChannelId = oldIssueChannelId;
    }

    public Long getNewIssueChannelId() {
        return newIssueChannelId;
    }

    public void setNewIssueChannelId(Long newIssueChannelId) {
        this.newIssueChannelId = newIssueChannelId;
    }

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
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
}
