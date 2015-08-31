package com.zs.domain.basic;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 快递信息
 * Created by Allen on 2015/8/31.
 */
@Entity
@Table(name = "Kuaidi")
public class Kuaidi extends AbstractEntity {

    /** 快递单当前状态
     * 0：在途，即货物处于运输过程中
     * 1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息
     * 2：疑难，货物寄送过程出了问题
     * 3：签收，收件人已签收
     * 4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收
     * 5：派件，即快递正在进行同城派件
     * 6：退回，货物正处于退回发件人的途中
     */

    private Long id;                            //主键
    private String nu;                          //快递单号
    private String com;                         //快递公司编码
    private String comName;                     //快递公司名称
    private String status;                      //状态
    private String conditions;                  //conditions
    private String state;                       //快递单当前状态
    private String record;                      //订单的快递信息[JSON格式：{record:{{time:***,content:***},{time:***,content:***}}}]
    private String message;                     //快递单查询的描述信息
    private Date syncTime = new Date();         //同步时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }
}
