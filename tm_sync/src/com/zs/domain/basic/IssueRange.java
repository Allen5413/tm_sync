package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;

/**
 * 发行范围
 * Created by Allen on 2015/5/4.
 */
@Entity
@Table(name = "issue_range")
public class IssueRange extends AbstractEntity {

    public static final int ISISSUE_YES = 0;    //发行
    public static final int ISISSUE_NOT = 1;    //不发行

    private Long id;                            //主键
    private String spotCode;                    //学习中心编号
    private Long issueChannelId;                //发行渠道ID
    private Integer isIssue;                    //是否发行[0：是；1：否]
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private Integer version;                    //版本号，用于乐观锁

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotCode() {
        return StringUtils.isEmpty(spotCode) ? "" : spotCode.trim();
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public Long getIssueChannelId() {
        return issueChannelId;
    }

    public void setIssueChannelId(Long issueChannelId) {
        this.issueChannelId = issueChannelId;
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
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getIsIssue() {
        return isIssue;
    }

    public void setIsIssue(Integer isIssue) {
        this.isIssue = isIssue;
    }
}
