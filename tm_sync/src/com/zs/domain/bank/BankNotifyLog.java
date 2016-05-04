package com.zs.domain.bank;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 调用银行通知接口记录情况
 * Created by Allen on 2016/5/3.
 */
@Entity
@Table(name = "bank_notify_log")
public class BankNotifyLog extends AbstractEntity {

    public final static String EVENT_SUCCESS = "NOTIFY_ACQUIRE_SUCCESS";        //支付成功
    public final static String EVENTT_FAIL = "NOTIFY_ACQUIRE_FAIL";             //支付失败
    public final static String EVENTT_RETURN_SUCCESS = "NOTIFY_REFUND_SUCCESS"; //退款成功
    public final static String EVENTT_AUTH_SUCCESS = "NOTIFY_AUTH_SUCCESS";     //认证成功
    public final static String EVENTT_NOT = "NOTIFY_NOT";                       //银行没有通知内容
    public final static String NOTIFY_SIGNATURE_FAIL = "NOTIFY_SIGNATURE_FAIL";           //验签失败

    private Long id;                                //主键
    private String event;                           //事件类型
    private String timestamp;                       //时间戳
    private String appid;                           //兴业银行分配给商户的AppID
    private String orderNo;                         //已支付的订单号
    private double orderAmount;                     //订单金额，单位为元
    private String orderTime;                       //商户端订单生成时间yyyyMMddHHmmss
    private String payTime;                         //付款时间yyyyMMddHHmmss
    private String sno;                             //支付网关流水号
    private String mac;                             //消息校验码
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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
