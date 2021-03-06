package com.zs.service.scheduler.impl;

import com.zs.dao.bank.paylog.FindPayForStateWaitDAO;
import com.zs.dao.bank.searchlog.BankSearchLogDAO;
import com.zs.domain.bank.BankPayLog;
import com.zs.domain.bank.BankSearchLog;
import com.zs.domain.finance.StudentExpensePay;
import com.zs.epaysdk.service.gp.GpQuery;
import com.zs.service.finance.studentexpensepay.AddStudentExpensePayService;
import com.zs.service.scheduler.CheckBankPayForStateWaitTaskService;
import com.zs.tools.DateTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查还在等待中的付款，查看他们的状态是否变更
 * Created by Allen on 2016/5/3.
 */
@Service("checkBankPayForStateWaitTaskService")
public class CheckBankPayForStateWaitTaskServiceImpl implements CheckBankPayForStateWaitTaskService {

    @Resource
    private FindPayForStateWaitDAO findPayForStateWaitDAO;
    @Resource
    private BankSearchLogDAO bankSearchLogDAO;
    @Resource
    private AddStudentExpensePayService addStudentExpensePayService;

    @Override
    @Transactional
    public void check() {
        try {
            GpQuery gpQuery = new GpQuery();
            List<BankPayLog> bankPayLogList = findPayForStateWaitDAO.find();
            if (null != bankPayLogList) {
                for (BankPayLog bankPayLog : bankPayLogList) {
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

                        //返回结果
                        if (!StringUtils.isEmpty(params.get("returnData"))) {
                            JSONObject resultJSON = JSONObject.fromObject(params.get("returnData"));
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
