package com.zs.service.bank.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.bank.paylog.FindPayForStateWaitDAO;
import com.zs.dao.bank.paylog.FindPayLogByCodeDAO;
import com.zs.dao.bank.searchlog.BankSearchLogDAO;
import com.zs.domain.bank.BankPayLog;
import com.zs.domain.bank.BankSearchLog;
import com.zs.domain.finance.StudentExpensePay;
import com.zs.epaysdk.service.gp.GpQuery;
import com.zs.service.bank.FindBankPayReqService;
import com.zs.service.finance.studentexpensepay.AddStudentExpensePayService;
import com.zs.tools.DateTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2016/5/4.
 */
@Service("findBankPayReqService")
public class FindBankPayReqServiceImpl extends EntityServiceImpl<BankSearchLog, BankSearchLogDAO> implements FindBankPayReqService {

    @Resource
    private FindPayLogByCodeDAO findPayLogByCodeDAO;
    @Resource
    private FindPayForStateWaitDAO findPayForStateWaitDAO;
    @Resource
    private AddStudentExpensePayService addStudentExpensePayService;
    @Resource
    private BankSearchLogDAO bankSearchLogDAO;

    @Override
    public String find(String orderNo) throws Exception {
        String result = "";
        BankPayLog bankPayLog = findPayLogByCodeDAO.find(orderNo);
        if(null == bankPayLog){
            throw new BusinessException("没有找到该订单的支付记录！");
        }
        if(bankPayLog.getState() == BankPayLog.STATE_SUCCESS){
            throw new BusinessException("该订单已经支付成功！");
        }
        if(bankPayLog.getState() == BankPayLog.STATE_FAIL){
            throw new BusinessException("该订单已经支付失败！");
        }
        if(bankPayLog.getState() == BankPayLog.STATE_CANEL){
            throw new BusinessException("该订单已经取消！");
        }
        if(bankPayLog.getState() == BankPayLog.STATE_RETURN){
            throw new BusinessException("该订单已经退款！");
        }
        if(null != bankPayLog){
            GpQuery gpQuery = new GpQuery();

            BankSearchLog bankSearchLog = new BankSearchLog();

            //查询支付结果
            Map<String, String> params = new HashMap<String, String>();
            params.put("order_no", bankPayLog.getCode());
            params.put("order_date", DateTools.getFormattedString(bankPayLog.getOperateTime(), "yyyyMMdd"));
            params = gpQuery.exec2(params);
            if (null != params) {
                bankSearchLog.setTimestamp(params.get("timestamp"));
                bankSearchLog.setAppid(params.get("appid"));
                bankSearchLog.setService(params.get("service"));
                bankSearchLog.setVer(params.get("ver"));
                bankSearchLog.setSignType(params.get("sign_type"));
                bankSearchLog.setOrderNo(params.get("order_no"));
                bankSearchLog.setOrderDate(params.get("order_date"));
                bankSearchLog.setMac(params.get("mac"));

                result = params.get("returnData");

                //返回结果
                if (!StringUtils.isEmpty(result)) {
                    JSONObject resultJSON = JSONObject.fromObject(result);
                    if (null != resultJSON) {
                        if (null == resultJSON.get("errcode")) {

                            int state = Integer.parseInt(resultJSON.get("trans_status").toString());
                            //查询成功
                            bankSearchLog.setrAppid(resultJSON.get("appid").toString());
                            bankSearchLog.setrOrderNo(resultJSON.get("order_no").toString());
                            bankSearchLog.setrOrderAmount(Double.parseDouble(resultJSON.get("order_amount").toString()));
                            bankSearchLog.setrOrderTime(resultJSON.get("order_time").toString());
                            bankSearchLog.setrPayTime(resultJSON.get("pay_time").toString());
                            bankSearchLog.setrTransStatus(state);
                            bankSearchLog.setrSno(resultJSON.get("sno").toString());
                            bankSearchLog.setrMac(resultJSON.get("mac").toString());

                            //修改支付记录状态
                            bankPayLog.setState(state);
                            bankPayLog.setType(BankPayLog.TYPE_SEARCH);
                            findPayForStateWaitDAO.update(bankPayLog);

                            //如果状态是成功，那么记录学生的缴费信息
                            if (state == BankPayLog.STATE_SUCCESS) {
                                this.addPay(bankPayLog);
                            }
                        } else {
                            //查询失败
                            bankSearchLog.setErrcode(resultJSON.get("errcode").toString());
                            bankSearchLog.setErrmsg(resultJSON.get("errmsg").toString());
                            bankSearchLog.seteMac(resultJSON.get("mac").toString());
                        }
                    }
                }
                bankSearchLogDAO.save(bankSearchLog);
            }
        }
        return result;
    }

    protected void addPay(BankPayLog bankPayLog)throws Exception{
        //学生缴费信息
        if(BankPayLog.PAY_USER_TYPE_STUDENT == bankPayLog.getPayUserType()){
            double money = bankPayLog.getMoney();
            StudentExpensePay studentExpensePay = new StudentExpensePay();
            studentExpensePay.setStudentCode(bankPayLog.getPayUserCode());
            studentExpensePay.setMoney(Float.parseFloat(money+""));
            studentExpensePay.setPayUserType(StudentExpensePay.PAYUSERTYPE_STUDENT);
            studentExpensePay.setPayType(StudentExpensePay.PAY_TYPE_ONLINE_BANK);
            studentExpensePay.setArrivalTime(DateTools.getLongNowTime());
            addStudentExpensePayService.addStudentExpensePay(studentExpensePay, bankPayLog.getOperator(), bankPayLog.getPayUserCode());
        }
    }
}
