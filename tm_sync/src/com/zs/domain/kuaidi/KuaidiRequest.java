package com.zs.domain.kuaidi;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 快递100请求结果
 * Created by Allen on 2015/9/30.
 */
@Entity
@Table(name = "kuaidi_request")
public class KuaidiRequest extends AbstractEntity {

    private Long id;                            //主键
    private String company;                     //快递公司
    private String number;                      //快递单号
    private String result;                      //返回结果
    /**
     * 返回结果编号 '200: 提交成功  701: 拒绝订阅的快递公司  700: 订阅方的订阅数据存在错误（如不支持的快递公司、单号为空、单号超长等）
        600: 您不是合法的订阅者（即授权Key出错）
        500: 服务器错误（即快递100的服务器出理间隙或临时性异常，有时如果因为不按规范提交请求，比如快递公司参数写错等，也会报此错误）
        501:重复订阅（请格外注意，501表示这张单已经订阅成功且目前还在跟踪过程中（即单号的status=polling），快递100的服务器会因此忽略您最新的此次订阅请求，从而返回501。
        一个运单号只要提交一次订阅即可，若要提交多次订阅，请在收到单号的status=abort或shutdown后隔半小时再提交订阅，详见本文档第7页“重要提醒”部份说明）
     */
    private String returnCode;
    private String message;                     //消息说明
    private Date operateTime = new Date();      //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
