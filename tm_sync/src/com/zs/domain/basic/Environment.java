package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Allen on 2015/5/13.
 */
@Entity
@Table(name = "environment")
public class Environment extends AbstractEntity {

    public static final int IS_NOT_STOCK_SEND_BOOK_NOT = 0;
    public static final int IS_NOT_STOCK_SEND_BOOK_YES = 1;

    private Long id;                            //主键
    private String name;                        //名称
    private String code;                        //编号
    private String value;                       //值
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private Integer version;                    //版本号，用于乐观锁

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
