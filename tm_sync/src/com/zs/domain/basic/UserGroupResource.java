package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户组关联资源
 * Created by Allen on 2015/4/28.
 */
@Entity
@Table(name = "user_group_resource")
public class UserGroupResource extends AbstractEntity {
    private Long id;                            //主键
    private Long userGroupId;                   //用户组id
    private Long resourceId;                    //资源ID
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

    public Long getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
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
