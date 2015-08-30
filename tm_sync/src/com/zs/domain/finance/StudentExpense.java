package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生费用
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "student_expense")
public class StudentExpense extends AbstractEntity {

    public static final int STATE_YES = 0;   //已结算状态
    public static final int STATE_NO = 1;    //未结算状态

    private Long id;                //主键id
    private String studentCode;     //学生学号
    private Float pay;              //已支付金额
    private Float buy;              //消费金额
    private Integer state;          //状态为，是否结算 【0：已结算，1：未结算】
    private String creator;         //创建人
    private Date createTime = new Date();        //创建时间
    private String operator;        //操作人
    private Date operateTime = new Date();       //操作时间
    private Integer version;        //版本号
    private Long semesterId;
    private Date clearTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Float getPay() {
        return pay;
    }

    public void setPay(Float pay) {
        this.pay = pay;
    }

    public Float getBuy() {
        return buy;
    }

    public void setBuy(Float buy) {
        this.buy = buy;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
    

    public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}
	
	@Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	public Date getClearTime() {
		return clearTime;
	}

	public void setClearTime(Date clearTime) {
		this.clearTime = clearTime;
	}
    
    
}
