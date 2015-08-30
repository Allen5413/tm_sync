package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 用户组
 * Created by Allen on 2015/4/27.
 */
@Entity
@Table(name = "user_group")
public class UserGroup extends AbstractEntity {

    private Long id;                            //主键
    private String name;                        //名称
    private String remark;                      //说明
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private int version;                        //版本号，用于乐观锁

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "" : name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return StringUtils.isEmpty(remark) ? "" : remark.trim();
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
