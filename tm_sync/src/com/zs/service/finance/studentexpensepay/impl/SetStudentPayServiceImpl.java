package com.zs.service.finance.studentexpensepay.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.finance.studentexpense.FindByStudentCodeDAO;
import com.zs.dao.finance.studentexpensepay.FindStudentPayBySpotCodeDAO;
import com.zs.domain.finance.StudentExpense;
import com.zs.service.finance.studentexpensepay.SetStudentPayService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2015/12/17.
 */
@Service("setStudentPayService")
public class SetStudentPayServiceImpl extends EntityServiceImpl implements SetStudentPayService {

    @Resource
    private FindStudentPayBySpotCodeDAO findStudentPayBySpotCodeDAO;
    @Resource
    private FindByStudentCodeDAO findByStudentCodeDAO;

    @Override
    @Transactional
    public void setStudentPayForSpotCode(String spotCode) throws Exception {
        List<Object[]> resultList = findStudentPayBySpotCodeDAO.find(spotCode);
        if(null != resultList && 0 < resultList.size()){
            for(Object[] objs : resultList){
                String studentCode = objs[0].toString();
                double totalPay = null == objs[1] ? 0 : Double.parseDouble(objs[1].toString());

                //查询学生的每学期的财务信息
                List<StudentExpense> studentExpenseList = findByStudentCodeDAO.findByStudentCode(studentCode);
                if(null != studentExpenseList && 0 < studentExpenseList.size()){
                    for(int i=0; i < studentExpenseList.size(); i++){
                        StudentExpense studentExpense = studentExpenseList.get(i);
                        double buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
                        if(totalPay >= buy){
                            //如果是最后一个学期，就把剩的钱全部录进去
                            if(i == studentExpenseList.size()-1){
                                studentExpense.setPay(new BigDecimal(totalPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }else {
                                studentExpense.setPay(new BigDecimal(buy).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }
                            if(studentExpense.getState() == StudentExpense.STATE_NO){
                                studentExpense.setState(StudentExpense.STATE_YES);
                            }
                            if(studentExpense.getClearTime() == null){
                                studentExpense.setClearTime(DateTools.getLongNowTime());
                            }
                            totalPay = new BigDecimal(totalPay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        }else{
                            studentExpense.setPay(new BigDecimal(totalPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            studentExpense.setState(StudentExpense.STATE_NO);
                            studentExpense.setClearTime(null);
                            totalPay = 0;
                        }
                        findByStudentCodeDAO.update(studentExpense);
                    }
                }
            }
        }
    }
}
