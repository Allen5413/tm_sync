package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * 专业关联课程
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_spec_course")
public class SpecCourse extends AbstractEntity {
    private Long id;                //主键
    private String specCode;        //专业编号
    private String courseCode;      //课程编号

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
