package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * 开课计划表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_begin_schedule")
public class BeginSchedule extends AbstractEntity {

    public static final int SPRING = 0; //春季
    public static final int AUTUMN = 1; //秋季

    private Long id;                    //主键
    private Integer enterYear;          //入学年
    private Integer quarter;            //入学季
    private Integer academicYear;       //开课年
    private Integer term;               //开课季
    private String specCode;            //专业编号
    private String levelCode;           //层次编号
    private String courseCode;          //课程编号

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnterYear() {
        return enterYear;
    }

    public void setEnterYear(Integer enterYear) {
        this.enterYear = enterYear;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
