package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 套教材
 * Created by Allen on 2015/4/30.
 */
@Entity
@Table(name = "set_teach_material")
public class SetTeachMaterial extends AbstractEntity {
    private Long id;                            //主键
    private String name;                        //套教材名称
    private String buyCourseCode;               //购买课程
    private String remark;                      //备注
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private Integer version;                    //版本号，用于乐观锁

    private Set<SetTeachMaterialTM> setTeachMaterialTMs;  //包含的套教材与教材关联


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

    public String getBuyCourseCode() {
        return StringUtils.isEmpty(buyCourseCode) ? "" : buyCourseCode.trim();
    }

    public void setBuyCourseCode(String buyCourseCode) {
        this.buyCourseCode = buyCourseCode;
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
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @OneToMany(mappedBy = "setTeachMaterial", targetEntity = SetTeachMaterialTM.class)
    @LazyCollection(value = LazyCollectionOption.FALSE)
    public Set<SetTeachMaterialTM> getSetTeachMaterialTMs() {
        return setTeachMaterialTMs;
    }

    public void setSetTeachMaterialTMs(Set<SetTeachMaterialTM> setTeachMaterialTMs) {
        this.setTeachMaterialTMs = setTeachMaterialTMs;
    }


}
