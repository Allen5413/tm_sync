package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学期表
 * Created by Allen on 2015/5/7.
 */
@Entity
@Table(name = "semester")
public class Semester extends AbstractEntity {

    public static final int QUARTER_SPRING = 0; //春季
    public static final int QUARTER_AUTUMN = 1; //秋季

    public static final int ISNOWSEMESTER_YES = 0; //是
    public static final int ISNOWSEMESTER_NOT = 1; //否

    private Long id;                            //主键
    private Integer year;                       //年份
    private Integer quarter;                    //季度[0:春；1:秋]
    private Integer isNowSemester;              //是否当前学期
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private String semester;                    //学期说明

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getIsNowSemester() {
        return isNowSemester;
    }

    public void setIsNowSemester(Integer isNowSemester) {
        this.isNowSemester = isNowSemester;
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

    @Transient
    public String getSemester() {
        return getYear()+"年"+(getQuarter() == Semester.QUARTER_SPRING ? "上半年" : "下半年");
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
