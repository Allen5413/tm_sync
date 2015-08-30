package com.zs.domain.sale;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生订书单状态变更日志
 * Created by Allen on 2015/5/10.
 */
@Entity
@Table(name = "student_book_order_log")
public class StudentBookOrderLog extends AbstractEntity {

    private Long id;                            //主键
    private String orderCode;                   //订单号
    private Integer state;                      //状态[0：未确认；1：已确认；2：分拣中；3：已打包；4：已发出；5：已签收]
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
