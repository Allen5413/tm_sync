package com.zs.domain.finance;

import com.feinno.framework.common.domain.AbstractEntity;
import com.zs.domain.basic.Semester;

import javax.persistence.*;

import java.util.Date;

/**
 * 学生费用消费记录
 * Created by LihongZhang on 2015/5/14.
 */
@Entity
@Table(name = "student_expense_buy")
public class StudentExpenseBuy extends AbstractEntity {

    public static final int TYPE_BUY_TM = 0;    //购买教材

    private Long id;                //主键id
    private String studentCode;     //学生学号
    private Float money;            //金额
    private int type;               //消费类型[0:购买教材]
    private String detail;          //消费说明
    private String creator;         //创建人
    private Date createTime = new Date();        //创建时间
    private Semester semester;


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

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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
    
    @OneToOne
    @JoinColumn(name = "semester_id")
	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	
    
    
}
