package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 套教材关联课程
 * Created by Allen on 2015/4/30.
 */
@Entity
@Table(name = "set_teach_material_course")
public class SetTeachMaterialCourse extends AbstractEntity {
    private Long id;                            //主键
    private Long setTeachMaterialId;            //套教材id
    private String courseCode;                  //课程编号
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetTeachMaterialId() {
        return setTeachMaterialId;
    }

    public void setSetTeachMaterialId(Long setTeachMaterialId) {
        this.setTeachMaterialId = setTeachMaterialId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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
}
