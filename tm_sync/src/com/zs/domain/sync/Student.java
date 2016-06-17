package com.zs.domain.sync;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生表
 * Created by Allen on 2015/5/6.
 */
@Entity
@Table(name = "sync_student")
public class Student extends AbstractEntity {

    public static final int SPRING = 0; //春季
    public static final int AUTUMN = 1; //秋季

    public static final int SEX_MAN = 0;    //男
    public static final int SEX_FEMALE = 1; //女

    public static final int IDCARD_TYPE_OTHER = 0;
    public static final int IDCARD_TYPE_IDCARD = 1;
    public static final int IDCARD_TYPE_MILITARY_OFFICER= 2;
    public static final int IDCARD_TYPE_PASSPORT = 3;
    public static final int IDCARD_TYPE_HK_MACAO_TAIWAN = 4;

    public static final int IS_ONCE_ORDER_NOT = 0;      //不是
    public static final int IS_ONCE_ORDER_YES = 1;      //是，但是还没有确认订单
    public static final int IS_ONCE_ORDER_BUY = 2;      //是，已经确认过订单，不再进行订单同步


    private Long id;                    //主键
    private String code;                //学号
    private String name;                //姓名
    private Integer sex;                //性别[0：男；1：女]
    private Integer idcardType;         //证件类型[网院：180--身份证 181--军官证 182--护照 183--港、澳、台居民证件 184--其他。   教材：0--其他  1--身份证 2--军官证 3--护照 4--港、澳、台居民证件]
    private String idcardNo;            //证件号码
    private String postalCode;          //邮编
    private String address;             //通讯地址
    private String mobile;              //移动电话
    private String homeTel;             //住宅电话
    private String companyTel;          //单位电话
    private String email;               //email
    private String spotCode;            //学习中心编号
    private String specCode;            //报考专业
    private String levelCode;           //报考层次
    private Integer type;               //学生类型[网院(StudentType): H--函授 Y--业余 W--网教 Z--自考 B--本科生 J--研究生 P--培训生 K--课程学习生。  教材：0--网教 1--自考 2--程学习生 3--函授 4--业余 5--本科生 6--研究生 7--培训生]
    private Integer state;              //学生状态[网院(StudyState)：53--停生 77--休学 78--退学 54--在籍学生  55--已毕业学.  教材：0--在籍学生 1--停生 2--休学 3--退学 4--已毕业学.]
    private Integer studyEnterYear;     //学籍入学年
    private Integer studyQuarter;       //学籍入学季
    private String changeSpotDetail;    //学生转学习中心情况说明
    private Date operateTime = new Date();  //操作时间
    private String openId;                  //记录微信的openid号
    private Date wxBoundTime;               //微信绑定时间
    private int isOnceOrder;                //是否设置成了一次性订购教材

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

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpotCode() {
        return spotCode;
    }

    public void setSpotCode(String spotCode) {
        this.spotCode = spotCode;
    }

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getStudyEnterYear() {
        return studyEnterYear;
    }

    public void setStudyEnterYear(Integer studyEnterYear) {
        this.studyEnterYear = studyEnterYear;
    }

    public Integer getStudyQuarter() {
        return studyQuarter;
    }

    public void setStudyQuarter(Integer studyQuarter) {
        this.studyQuarter = studyQuarter;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getChangeSpotDetail() {
        return changeSpotDetail;
    }

    public void setChangeSpotDetail(String changeSpotDetail) {
        this.changeSpotDetail = changeSpotDetail;
    }

    public int getIsOnceOrder() {
        return isOnceOrder;
    }

    public void setIsOnceOrder(int isOnceOrder) {
        this.isOnceOrder = isOnceOrder;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getWxBoundTime() {
        return wxBoundTime;
    }

    public void setWxBoundTime(Date wxBoundTime) {
        this.wxBoundTime = wxBoundTime;
    }
}
