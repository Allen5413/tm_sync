package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 教材表
 * Created by Allen on 2015/4/29.
 */
@Entity
@Table(name = "teach_material")
public class TeachMaterial extends AbstractEntity {

    public final static Integer ISSET_NO = 0;
    public final static Integer ISSET_YES = 1;

    public final static Integer IS_SPOT_SEND_NO = 0;
    public final static Integer IS_SPOT_SEND_YES = 1;

    public final static Integer STATE_ENABLE = 0;
    public final static Integer STATE_DISABLE = 1;


    private Long id;                            //主键
    private String isbn;                        //isbn
    private String name;                        //教材名称
    private Long pressId;                       //出版社id
    private Float price;                        //价格
    private Long teachMaterialTypeId;           //教材类别id
    private String revision;                    //版次
    private Long stock;                         //库存
    private String author;                      //编著者
    private Integer pressYear;                  //出版年
    private String weight;                      //重量
    private Integer isSet;                      //是否套教材[0:否; 1:是]
    private Integer isSpotSend;                 //是否允许学习中心发书[0：否；1：是]
    private Integer state;                      //状态[0：启用; 1：停用]
    private String remark;                      //备注
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

    public String getIsbn() {
        return StringUtils.isEmpty(isbn) ? "" : isbn.trim();
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return StringUtils.isEmpty(name) ? "" : name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPressId() {
        return pressId;
    }

    public void setPressId(Long pressId) {
        this.pressId = pressId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Long getTeachMaterialTypeId() {
        return teachMaterialTypeId;
    }

    public void setTeachMaterialTypeId(Long teachMaterialTypeId) {
        this.teachMaterialTypeId = teachMaterialTypeId;
    }

    public String getRevision() {
        return StringUtils.isEmpty(revision) ? "" : revision.trim();
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getAuthor() {
        return StringUtils.isEmpty(author) ? "" : author.trim();
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPressYear() {
        return pressYear;
    }

    public void setPressYear(Integer pressYear) {
        this.pressYear = pressYear;
    }

    public Integer getIsSet() {
        return isSet;
    }

    public void setIsSet(Integer isSet) {
        this.isSet = isSet;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public String getWeight() {
        return StringUtils.isEmpty(weight) ? "" : weight.trim();
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getIsSpotSend() {
        return isSpotSend;
    }

    public void setIsSpotSend(Integer isSpotSend) {
        this.isSpotSend = isSpotSend;
    }
}
