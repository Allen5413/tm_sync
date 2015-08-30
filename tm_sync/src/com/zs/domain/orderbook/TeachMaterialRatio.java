package com.zs.domain.orderbook;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 教材系数
 * Created by Allen on 2015/5/4.
 */
@Entity
@Table(name = "teach_material_ratio")
public class TeachMaterialRatio extends AbstractEntity {
    private Long id;                            //主键
    private Long semesterId;                    //当前学期ID
    private String newRatio;                    //新生教材系数
    private String oldRatio;                    //旧生教材系数
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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public String getNewRatio() {
        return StringUtils.isEmpty(newRatio) ? "" : newRatio.trim();
    }

    public void setNewRatio(String newRatio) {
        this.newRatio = newRatio;
    }

    public String getOldRatio() {
        return StringUtils.isEmpty(oldRatio) ? "" : oldRatio.trim();
    }

    public void setOldRatio(String oldRatio) {
        this.oldRatio = oldRatio;
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
}
