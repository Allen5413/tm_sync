package com.zs.domain.bank;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Allen on 2016/5/3.
 */
@Entity
@Table(name = "bank_search_log")
public class BankSearchLog  extends AbstractEntity {

    private Long id;                                //主键
    private String timestamp;                       //提交参数 时间戳
    private String appid;                           //提交参数 兴业银行分配给商户的AppID
    private String service;                         //提交参数 服务名称
    private String ver;                             //提交参数 接口版本号
    private String signType;                        //提交参数 SHA1或RSA
    private String orderNo;                         //提交参数 订单编号，同一商户号下，所有订单的编号必须唯一
    private String orderDate;                       //提交参数 订单提交时间。格式：yyyyMMdd
    private String mac;                             //提交参数 消息校验码
    private String rAppid;                          //返回结果 兴业银行分配给商户的AppID
    private String rOrderNo;                        //返回结果 由商户分配的订单号
    private double rOrderAmount;                    //返回结果 订单金额，单位为元，即：十位整数，两位小数
    private String rOrderTime;                      //返回结果 商户端订单生成时间yyyyMMddHHmmss
    private String rPayTime;                        //返回结果 代收时间 yyyyMMddHHmmss
    private int rTransStatus;                       //返回结果 0-待发起；1-成功；2-失败；3-支付中；4-已撤销；5-已退款
    private String rSno;                            //返回结果 收付直通车支付网关流水号
    private String rMac;                            //返回结果 应答签名/消息校验码
    private String errcode;                         //失败结果 错误代码，20310-订单不存在
    private String errmsg;                          //失败结果 发送错误时返回的错误信息
    private String eMac;                            //失败结果 应答签名/消息校验码
    private Date createTime = new Date();           //创建时间
    private Date operateTime = new Date();          //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getrAppid() {
        return rAppid;
    }

    public void setrAppid(String rAppid) {
        this.rAppid = rAppid;
    }

    public String getrOrderNo() {
        return rOrderNo;
    }

    public void setrOrderNo(String rOrderNo) {
        this.rOrderNo = rOrderNo;
    }

    public double getrOrderAmount() {
        return rOrderAmount;
    }

    public void setrOrderAmount(double rOrderAmount) {
        this.rOrderAmount = rOrderAmount;
    }

    public String getrOrderTime() {
        return rOrderTime;
    }

    public void setrOrderTime(String rOrderTime) {
        this.rOrderTime = rOrderTime;
    }

    public String getrPayTime() {
        return rPayTime;
    }

    public void setrPayTime(String rPayTime) {
        this.rPayTime = rPayTime;
    }

    public int getrTransStatus() {
        return rTransStatus;
    }

    public void setrTransStatus(int rTransStatus) {
        this.rTransStatus = rTransStatus;
    }

    public String getrSno() {
        return rSno;
    }

    public void setrSno(String rSno) {
        this.rSno = rSno;
    }

    public String getrMac() {
        return rMac;
    }

    public void setrMac(String rMac) {
        this.rMac = rMac;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String geteMac() {
        return eMac;
    }

    public void seteMac(String eMac) {
        this.eMac = eMac;
    }
}
