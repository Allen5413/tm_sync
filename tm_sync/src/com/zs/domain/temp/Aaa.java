package com.zs.domain.temp;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * Created by Allen on 2017/5/17.
 */
@Entity
@Table(name = "aaa")
public class Aaa extends AbstractEntity {
    private Long id;                    //主键
    private String name;                //姓名
    private Integer idcardType;         //证件类型[网院：180--身份证 181--军官证 182--护照 183--港、澳、台居民证件 184--其他。   教材：0--其他  1--身份证 2--军官证 3--护照 4--港、澳、台居民证件]
    private String idcardNo;            //证件号码
    private String mobile;              //移动电话
    private String homeTel;             //住宅电话

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(Integer idcardType) {
        this.idcardType = idcardType;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }
}
