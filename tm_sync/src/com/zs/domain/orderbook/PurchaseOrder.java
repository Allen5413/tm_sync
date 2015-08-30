package com.zs.domain.orderbook;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Allen on 2015/5/4.
 */
@Entity
@Table(name = "purchase_order")
public class PurchaseOrder extends AbstractEntity {

    public static final int ISAUTO_YSE = 0;
    public static final int ISAUTO_NO = 1;

    private Long id;                            //主键
    private Long semesterId;                    //当前学期ID
    private String code;                        //订书单编号
    private Long issueChannelId;                //发行渠道ID
    private Long teachMaterialTypeId;           //教材类别ID
    private Integer isAuto;                     //是否自动生成[0:是; 1:否]
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getIssueChannelId() {
        return issueChannelId;
    }

    public void setIssueChannelId(Long issueChannelId) {
        this.issueChannelId = issueChannelId;
    }

    public Long getTeachMaterialTypeId() {
        return teachMaterialTypeId;
    }

    public void setTeachMaterialTypeId(Long teachMaterialTypeId) {
        this.teachMaterialTypeId = teachMaterialTypeId;
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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }
}
