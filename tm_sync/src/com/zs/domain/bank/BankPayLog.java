package com.zs.domain.bank;

import com.feinno.framework.common.domain.AbstractEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 调用银行支付接口记录情况
 * Created by Allen on 2015/10/27.
 */
@Entity
@Table(name = "bank_pay_log")
public class BankPayLog extends AbstractEntity {

    public final static int PAY_USER_TYPE_STUDENT = 0;

    public final static int STATE_WAIT = 0;
    public final static int STATE_SUCCESS = 1;
    public final static int STATE_FAIL = 2;
    public final static int STATE_IN = 3;
    public final static int STATE_CANEL = 4;
    public final static int STATE_RETURN = 5;

    public final static String TYPE_NOTIFY = "notify";  //通知型
    public final static String TYPE_SEARCH = "search";  //查询型

    private Long id;                                //主键
    private String code;                            //支付产生的编号
    private double money;                            //支付金额
    private String payForm;                         //请求明文
    private String payUserCode;                     //付款人编号
    private int payUserType;                        //付款人类型 0：学生
    private int state;                              //状态 0-待发起；1-成功；2-失败；3-支付中；4-已撤销；5-已退款
    private String type;                            //操作类型
    private String operator;                        //操作人
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
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

    public String getPayUserCode() {
        return payUserCode;
    }

    public void setPayUserCode(String payUserCode) {
        this.payUserCode = payUserCode;
    }

    public int getPayUserType() {
        return payUserType;
    }

    public void setPayUserType(int payUserType) {
        this.payUserType = payUserType;
    }

    public String getPayForm() {
        return payForm;
    }

    public void setPayForm(String payForm) {
        this.payForm = payForm;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
