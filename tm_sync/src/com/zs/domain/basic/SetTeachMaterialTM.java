package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 套教材关联教材
 * Created by Allen on 2015/4/30.
 */
@Entity
@Table(name = "set_teach_material_tm")
public class SetTeachMaterialTM extends AbstractEntity {
    private Long id;                            //主键
    private Long setTeachMaterialId;            //套教材id
    private Long teachMaterialId;               //教材id
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间

    private SetTeachMaterial setTeachMaterial;  //所属的套教材
    private TeachMaterial teachMaterial;        //所属的教材

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetTeachMaterialId() {
        return setTeachMaterialId;
    }

    public void setSetTeachMaterialId(Long setTeachMaterialId) {
        this.setTeachMaterialId = setTeachMaterialId;
    }

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
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
    @JoinColumn(name="setTeachMaterialId", insertable = false, updatable = false)
    @LazyToOne(value = LazyToOneOption.PROXY)
    public SetTeachMaterial getSetTeachMaterial() {
        return setTeachMaterial;
    }

    public void setSetTeachMaterial(SetTeachMaterial setTeachMaterial) {
        this.setTeachMaterial = setTeachMaterial;
    }

    @ManyToOne
    @JoinColumn(name="teachMaterialId", insertable = false, updatable = false)
    @LazyToOne(value = LazyToOneOption.PROXY)
    public TeachMaterial getTeachMaterial() {
        return teachMaterial;
    }

    public void setTeachMaterial(TeachMaterial teachMaterial) {
        this.teachMaterial = teachMaterial;
    }
}
