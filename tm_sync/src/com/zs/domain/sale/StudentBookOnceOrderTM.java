package com.zs.domain.sale;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生订书单关联教材
 * Created by Allen on 2015/5/10.
 */
@Entity
@Table(name = "student_book_once_order_tm")
public class StudentBookOnceOrderTM extends AbstractEntity {

    public static final int IS_SEND_NOT = 0;        //未发出
    public static final int IS_SEND_YES = 1;        //已发出

    public static final int IS_MUST_NOT = 0;        //选修
    public static final int IS_MUST_YES = 1;        //必修

    public static final int IS_BUY_NOT = 0;        //未买过该课程
    public static final int IS_BUY_YES = 1;        //已经买过该课程

    public static final int IS_SELECT_NOT = 0;     //未选该课程
    public static final int IS_SELECT_YES = 1;     //已选该课程

    private Long id;                            //主键
    private String orderCode;                   //订单号
    private String courseCode;                  //课程编号
    private Long teachMaterialId;               //教材id
    private Float price;                        //教材单价
    private Integer count;                      //订购数量
    private Integer isSend = 0;                 //是否发出
    private Integer isMust = 0;                 //是否必修
    private Integer isBuy = 0;                  //是否已经买过该书
    private Integer xf;                         //学分
    private Integer isSelect = 0;              //是否已经选过课
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

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend;
    }

    public Integer getIsMust() {
        return isMust;
    }

    public void setIsMust(Integer isMust) {
        this.isMust = isMust;
    }

    public Integer getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(Integer isBuy) {
        this.isBuy = isBuy;
    }

    public Integer getXf() {
        return xf;
    }

    public void setXf(Integer xf) {
        this.xf = xf;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }
}
