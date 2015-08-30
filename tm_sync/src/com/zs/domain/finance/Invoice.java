package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 发票
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "invoice")
public class Invoice extends AbstractEntity {

    private Long id;                //主键id
    private String creator;          //创建人
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
