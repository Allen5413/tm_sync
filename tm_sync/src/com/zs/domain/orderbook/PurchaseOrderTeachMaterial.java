package com.zs.domain.orderbook;

/**
 * Created by Allen on 2015/5/4.
 */

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchase_order_teach_material")
public class PurchaseOrderTeachMaterial extends AbstractEntity {

    public static int STATE_NORMAL = 0;     //正常
    public static int STATE_CANCEL = 1;     //作废

    private Long id;                            //主键
    private Long semesterId;                    //当前学期ID
    private String code;                        //订书单编号
    private String courseCode;                  //课程编号
    private Long teachMaterialId;               //教材ID
    private Integer teachMaterialCount;         //教材数量
    private Integer putStorageCount;            //已入库数量
    private Integer state;                      //状态[0:正常；1作废]
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

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
    }

    public Integer getTeachMaterialCount() {
        return teachMaterialCount;
    }

    public void setTeachMaterialCount(Integer teachMaterialCount) {
        this.teachMaterialCount = teachMaterialCount;
    }

    public Integer getPutStorageCount() {
        return putStorageCount;
    }

    public void setPutStorageCount(Integer putStorageCount) {
        this.putStorageCount = putStorageCount;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
