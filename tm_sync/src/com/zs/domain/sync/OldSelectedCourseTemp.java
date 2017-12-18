package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * 同步选课表,旧生的
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_old_selected_course_temp")
public class OldSelectedCourseTemp extends AbstractEntity {
    private Long id;                //主键
    private String studentCode;     //学号
    private String courseCode;      //课程编号
    private int score;

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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
