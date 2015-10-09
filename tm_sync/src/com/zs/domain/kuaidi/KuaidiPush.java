package com.zs.domain.kuaidi;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 快递100推送的的信息记录
 * Created by Allen on 2015/9/30.
 */
@Entity
@Table(name = "kuaidi_push")
public class KuaidiPush extends AbstractEntity {

    private Long id;                            //主键
    /**
     * 监控状态
     * polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。
     * 其中当快递单为已签收时status=shutdown，当message为“3天查询无记录”或“60天无变化时”status= abort ，
     * 对于stuatus=abort的状度，需要增加额外的处理逻辑，详见本节最后的说明。
     */
    private String status;
    private String message;                     //消息说明
    /**
     * 快递状态
     * 0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态，
     * 其中4-7需要另外开通才有效，详见章3.3
     */
    private Integer state;
    private String com;                     //快递公司
    private String nu;                      //快递单号
    private String data;                    //返回快递结果
    private Date operateTime = new Date();      //操作时间

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
