package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;

/**
 * 层次表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_level")
public class Level extends AbstractEntity {
    private Long id;            //主键
    private String code;        //编号
    private String name;        //名称

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
