package com.zs.domain.sale;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生订书单关联教材
 * Created by Allen on 2015/5/10.
 */
@Entity
@Table(name = "student_book_order_tm")
public class StudentBookOrderTM extends AbstractEntity {

    private Long id;                            //主键
    private String orderCode;                   //订单号
    private String courseCode;                  //课程编号
    private Long teachMaterialId;               //教材id
    private Float price;                        //教材单价
    private Integer count;                      //订购数量
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
