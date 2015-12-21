package com.zs.service.scheduler.impl;

import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.TeachMaterialDAO;
import com.zs.dao.finance.studentexpense.FindByStudentCodeDAO;
import com.zs.dao.finance.studentexpense.FindRecordStudentCodeDao;
import com.zs.dao.finance.studentexpensebuy.StudentExpenseBuyDao;
import com.zs.dao.finance.studentexpensepay.FindStudentPayByStudentCodeDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.domain.sync.Student;
import com.zs.service.finance.spotexpenseoth.SetSpotExpenseOthBySpotCodeService;
import com.zs.service.scheduler.CheckLeaveOutStudentOrderTMService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2015/12/21.
 */
@Service("checkLeaveOutStudentOrderTMService")
public class CheckLeaveOutStudentOrderTMServiceImpl implements CheckLeaveOutStudentOrderTMService {

    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private StudentExpenseBuyDao studentExpenseBuyDao;
    @Resource
    private FindRecordStudentCodeDao findRecordStudentCodeDao;
    @Resource
    private FindStudentPayByStudentCodeDAO findStudentPayByStudentCodeDAO;
    @Resource
    private FindByStudentCodeDAO findByStudentCodeDAO;
    @Resource
    private SetSpotExpenseOthBySpotCodeService setSpotExpenseOthBySpotCodeService;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private TeachMaterialDAO teachMaterialDAO;

    @Override
    @Transactional
    public void check() {
        try{
            //获取当前学期
            Semester semester = findNowSemesterDAO.getNowSemester();
            List<Object> studentList = studentExpenseBuyDao.findLeaveOutStudentOrderTM(semester.getId());

            if(null != studentList && 0 < studentList.size()){
                for(Object obj : studentList){
                    String studentCode = obj.toString();

                    Student student = findStudentByCodeDAO.getStudentByCode(studentCode);

                    if(null != student) {
                        //查询学生还有哪本教材没有记录在消费里面
                        List<Object[]> tmList = studentExpenseBuyDao.findLeaveOutStudentOrderTMByStudentCode(semester.getId(), studentCode);
                        if (null != tmList && 0 < tmList.size()) {
                            for (Object[] objs : tmList) {
                                long tmId = null == objs[0] ? 0 : Long.parseLong(objs[0].toString());
                                float price = null == objs[1] ? 0 : Float.parseFloat(objs[1].toString());
                                int count = null == objs[2] ? 0 : Integer.parseInt(objs[2].toString());

                                String detail = "";
                                TeachMaterial teachMaterial = teachMaterialDAO.get(tmId);
                                if(null != teachMaterial){
                                    detail = "购买了"+count+"本，["+teachMaterial.getName()+"] 教材";
                                }


                                StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
                                studentExpenseBuy.setStudentCode(studentCode);
                                studentExpenseBuy.setMoney(price);
                                studentExpenseBuy.setSemester(semester);
                                studentExpenseBuy.setDetail(detail);
                                studentExpenseBuy.setCreator("管理员");
                                studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
                                //添加消费明细记录
                                studentExpenseBuyDao.save(studentExpenseBuy);

                                //查询该学生的账户信息
                                StudentExpense studentExpense = findRecordStudentCodeDao.getRecordByStuCode(studentCode, semester.getId());
                                if (null == studentExpense) {
                                    studentExpense = new StudentExpense();
                                    studentExpense.setStudentCode(studentCode);
                                    studentExpense.setPay(0f);
                                    studentExpense.setBuy(price);
                                    studentExpense.setState(StudentExpense.STATE_NO);
                                    studentExpense.setCreator("管理员");
                                    studentExpense.setOperator("管理员");
                                    studentExpense.setSemesterId(semester.getId());
                                    findRecordStudentCodeDao.save(studentExpense);
                                } else {
                                    double newBuy = new BigDecimal(price).add(new BigDecimal(studentExpense.getBuy())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    studentExpense.setBuy(Float.parseFloat(newBuy + ""));
                                    findRecordStudentCodeDao.update(studentExpense);
                                }

                                //重新计算每学期的缴费信息
                                Object[] payObj = findStudentPayByStudentCodeDAO.find(studentCode);
                                if (null != payObj) {
                                    double totalPay = null == payObj[1] ? 0 : Double.parseDouble(payObj[1] + "");

                                    //查询学生的每学期的财务信息
                                    List<StudentExpense> studentExpenseList = findByStudentCodeDAO.findByStudentCode(studentCode);
                                    if (null != studentExpenseList && 0 < studentExpenseList.size()) {
                                        for (int i = 0; i < studentExpenseList.size(); i++) {
                                            StudentExpense studentExpense2 = studentExpenseList.get(i);
                                            double buy = null == studentExpense2.getBuy() ? 0 : studentExpense2.getBuy();
                                            if (totalPay >= buy) {
                                                //如果是最后一个学期，就把剩的钱全部录进去
                                                if (i == studentExpenseList.size() - 1) {
                                                    studentExpense2.setPay(new BigDecimal(totalPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                                } else {
                                                    studentExpense2.setPay(new BigDecimal(buy).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                                }
                                                if (null == studentExpense2.getState() || studentExpense2.getState() == StudentExpense.STATE_NO) {
                                                    studentExpense2.setState(StudentExpense.STATE_YES);
                                                }
                                                if (studentExpense2.getClearTime() == null) {
                                                    studentExpense2.setClearTime(DateTools.getLongNowTime());
                                                }
                                                totalPay = new BigDecimal(totalPay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                            } else {
                                                studentExpense2.setPay(new BigDecimal(totalPay).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                                studentExpense2.setState(StudentExpense.STATE_NO);
                                                studentExpense2.setClearTime(null);
                                                totalPay = 0;
                                            }
                                            findByStudentCodeDAO.update(studentExpense2);
                                        }
                                    }
                                }

                                //重新计算该学生的中心财务统计信息
                                setSpotExpenseOthBySpotCodeService.reset(student.getSpotCode());
                            }
                        }
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
