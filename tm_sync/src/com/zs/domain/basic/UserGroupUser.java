package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 用户管理用户组
 * Created by Allen on 2015/5/15.
 */
@Entity
@Table(name = "user_group_user")
public class UserGroupUser extends AbstractEntity {

    public static final int TYPE_TM = 0;
    public static final int TYPE_SYNC = 1;

    private Long id;                            //主键
    private Long userGroupId;                   //用户组id
    private String userName;                    //用户登录名
    private int type;                           //用户类型[0：教材系统用户；1：网院同步用户]
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

    public String getUserName() {
        return StringUtils.isEmpty(userName) ? "" : userName.trim();
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
