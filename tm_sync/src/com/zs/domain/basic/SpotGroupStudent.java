package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;

/**
 * 学习中心关联学生
 * Created by Allen on 2015/5/1.
 */
@Entity
@Table(name = "spot_group_student")
public class SpotGroupStudent extends AbstractEntity {
    private Long id;                            //主键
    private Long spotGroupId;                   //学习中心分组id
    private String studentCode;                 //学号
    private String creator;                     //创建人
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime = new Date();       //创建时间

    private SpotGroup spotGroup;                //所属学习中心分组

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpotGroupId() {
        return spotGroupId;
    }

    public void setSpotGroupId(Long spotGroupId) {
        this.spotGroupId = spotGroupId;
    }

    public String getStudentCode() {
        return StringUtils.isEmpty(studentCode) ? "" : studentCode.trim();
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
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

    @ManyToOne
    @JoinColumn(name="spotGroupId", insertable = false, updatable = false)
    @LazyToOne(value = LazyToOneOption.PROXY)
    public SpotGroup getSpotGroup() {
        return spotGroup;
    }

    public void setSpotGroup(SpotGroup spotGroup) {
        this.spotGroup = spotGroup;
    }
}
