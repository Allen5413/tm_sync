package com.zs.domain.api;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 网院数据同步日志记录
 * Created by Allen on 2017/5/3.
 */
@Entity
@Table(name = "wang_yuan_api")
public class WangYuanApi extends AbstractEntity {

    private Long id;
    private String reqData;
    private String resultData;
    private Date operateTime = new Date();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
}
