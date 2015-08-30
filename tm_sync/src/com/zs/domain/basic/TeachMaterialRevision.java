package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * 教材版次表
 * Created by Allen on 2015/6/3.
 */
@Entity
@Table(name = "teach_material_revision")
public class TeachMaterialRevision extends AbstractEntity {

    public final static Integer ISNOW_NO = 0;
    public final static Integer ISNOW_YES = 1;

    private Long id;                            //主键
    private Long teachMaterialId;               //教材id
    private String revision;                    //版次
    private Float price;                        //价格
    private Integer isNow;                      //是否当前版次[0:否；1:是]
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

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
    }

    public String getRevision() {
        return StringUtils.isEmpty(revision) ? "" : revision.trim();
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getIsNow() {
        return isNow;
    }

    public void setIsNow(Integer isNow) {
        this.isNow = isNow;
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
