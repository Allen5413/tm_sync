package com.zs.service.scheduler.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.StudentExpenseDAO;
import com.zs.dao.sale.StudentExpensePayDAO;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpensePay;
import com.zs.service.scheduler.CreateStudentExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2015/9/12.
 */
@Service("createStudentExpenseService")
public class CreateStudentExpenseServiceImpl extends EntityServiceImpl<StudentExpense, StudentExpenseDAO> implements CreateStudentExpenseService {

    @Resource
    private StudentExpenseDAO studentExpenseDAO;
    @Resource
    private StudentExpensePayDAO studentExpensePayDAO;

    @Override
    @Transactional
    public void add() {
        try {
            List<StudentExpensePay> studentExpensePayList = studentExpensePayDAO.getAll();
            int i = 0;
            for (StudentExpensePay studentExpensePay : studentExpensePayList) {
                System.out.println("i: " + i);
                float pay = studentExpensePay.getMoney();
                StudentExpense studentExpense = studentExpenseDAO.getList(studentExpensePay.getStudentCode());
                StudentExpense studentExpense2 = studentExpenseDAO.getList2(studentExpensePay.getStudentCode());
                if (studentExpense != null && studentExpense.getBuy() != null) {
                    float buy = studentExpense.getBuy();
                    if (pay <= buy) {
                        studentExpense.setPay(pay);
                        super.update(studentExpense);
                    } else {
                        BigDecimal bigDecimal = new BigDecimal(pay).subtract(new BigDecimal(buy));
                        studentExpense.setPay(buy);
                        super.update(studentExpense);
                        if (studentExpense2 != null && studentExpense2.getId() != null) {
                            float pay2 = null == studentExpense2.getPay() ? 0 : studentExpense2.getPay();
                            studentExpense2.setPay(bigDecimal.add(new BigDecimal(pay2)).floatValue());
                            super.update(studentExpense2);
                        } else {
                            StudentExpense studentExpense3 = new StudentExpense();
                            studentExpense3.setStudentCode(studentExpensePay.getStudentCode());
                            studentExpense3.setPay(bigDecimal.floatValue());
                            studentExpense3.setState(1);
                            studentExpense3.setCreator("管理员");
                            studentExpense3.setSemesterId(2l);
                            studentExpense3.setOperator("管理员");
                            super.save(studentExpense3);
                        }
                    }
                } else {
                    if (studentExpense2 != null && studentExpense2.getId() != null) {
                        studentExpense2.setPay(pay);
                        super.update(studentExpense2);
                    } else {
                        StudentExpense studentExpense3 = new StudentExpense();
                        studentExpense3.setStudentCode(studentExpensePay.getStudentCode());
                        studentExpense3.setPay(pay);
                        studentExpense3.setState(1);
                        studentExpense3.setCreator("管理员");
                        studentExpense3.setSemesterId(2l);
                        studentExpense3.setOperator("管理员");
                        super.save(studentExpense3);
                    }
                }
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
