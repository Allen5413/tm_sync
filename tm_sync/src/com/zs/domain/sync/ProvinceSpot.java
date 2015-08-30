package com.zs.domain.sync;

import javax.persistence.*;

/**
 * 省中心关联学习中心
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_province_spot")
public class ProvinceSpot {
    private Long id;                //主键
    private String provinceCode;   //省中心编号
    private String spotCode;       //学习中心编号

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }
}
