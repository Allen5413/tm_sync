package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * 学习中心教材库存
 * Created by LihongZhang on 2015/5/8.
 */
@Entity
@Table(name = "spot_teach_material_stock")
public class SpotTeachMaterialStock extends AbstractEntity {
    private Long id;                 //主键
    private String spotCode;        //学习中心编号
    private Long teachMaterialId;    //教材id
    private int stock;              //库存

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public Long getTeachMaterialId() {
        return teachMaterialId;
    }

    public void setTeachMaterialId(Long teachMaterialId) {
        this.teachMaterialId = teachMaterialId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
