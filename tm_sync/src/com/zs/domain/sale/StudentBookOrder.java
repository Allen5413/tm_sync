package com.zs.domain.sale;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 学生订书单
 * Created by Allen on 2015/5/10.
 */
@Entity
@Table(name = "student_book_order")
public class StudentBookOrder extends AbstractEntity {

    public static final int ISSPOTORDER_YES = 0;    //是
    public static final int ISSPOTORDER_NOT = 1;    //否

    public static final int ISSTOCK_YES = 0;    //是
    public static final int ISSTOCK_NOT = 1;    //否

    public static final int STUDENTSIGN_YES = 0;    //是
    public static final int STUDENTSIGN_NOT = 1;    //否

    public static final int STATE_UNCONFIRMED = 0;  //未确认
    public static final int STATE_CONFIRMED = 1;    //已确认
    public static final int STATE_SORTING = 2;      //分拣中
    public static final int STATE_PACK = 3;         //已打包
    public static final int STATE_SEND = 4;         //已发出
    public static final int STATE_SIGN = 5;         //已签收

    public static final int IS_SEND_STUDENT_NOT = 0;      //不是
    public static final int IS_SEND_STUDENT_YES = 1;      //是


    private Long id;                            //主键
    private Long semesterId;                    //学期id
    private Long issueChannelId;                //渠道id
    private String orderCode;                   //订单号
    private String studentCode;                 //学号
    private Integer state;                      //状态[0：未确认；1：已确认；2：分拣中；3：已打包；4：已发出； 5：已签收]
    private Integer isStock;                    //是否有库存[0:是；1：否]
    private Integer isSpotOrder;                //是否学习中心订单[0：是；1：否]
    private String logisticCode;                //物流单号
    private Long packageId;                     //订单打包id
    private Integer printSort;                  //打印顺序号
    private Integer studentSign;                //学生签收[0:是；1:否]
    private int isSendStudent;                  //是否邮寄给学生[0:否；1:是]
    private String creator;                     //创建人
    private Date createTime = new Date();       //创建时间
    private String operator;                    //操作人
    private Date operateTime = new Date();      //操作时间
    private Integer version;                    //版本号，用于乐观锁
    private String sendAddress;             //邮寄给学生的地址
    private String sendPhone;               //邮寄给学生的手机
    private String sendZipCode;             //邮寄给学生的邮编

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getIssueChannelId() {
        return issueChannelId;
    }

    public void setIssueChannelId(Long issueChannelId) {
        this.issueChannelId = issueChannelId;
    }

    public Integer getIsSpotOrder() {
        return isSpotOrder;
    }

    public void setIsSpotOrder(Integer isSpotOrder) {
        this.isSpotOrder = isSpotOrder;
    }

    public Integer getIsStock() {
        return isStock;
    }

    public void setIsStock(Integer isStock) {
        this.isStock = isStock;
    }

    public Integer getPrintSort() {
        return printSort;
    }

    public void setPrintSort(Integer printSort) {
        this.printSort = printSort;
    }

    public Integer getStudentSign() {
        return studentSign;
    }

    public void setStudentSign(Integer studentSign) {
        this.studentSign = studentSign;
    }

    public int getIsSendStudent() {
        return isSendStudent;
    }

    public void setIsSendStudent(int isSendStudent) {
        this.isSendStudent = isSendStudent;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getSendZipCode() {
        return sendZipCode;
    }

    public void setSendZipCode(String sendZipCode) {
        this.sendZipCode = sendZipCode;
    }
}
