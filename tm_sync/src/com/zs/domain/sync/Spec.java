package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 专业表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_spec")
public class Spec extends AbstractEntity {
    private Long id;            //主键
    private String code;        //编号
    private String name;        //名称
    private String deptCode;    //院系编号
    private Date operateTime = new Date();  //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
