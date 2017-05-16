package com.zs.service.finance.studentexpense.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.finance.spotexpenseoth.SpotExpenseOthDAO;
import com.zs.dao.finance.studentexpense.FindRecordStudentCodeDao;
import com.zs.dao.finance.studentexpense.StudentExpenseDao;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.domain.sync.Student;
import com.zs.service.finance.studentexpense.AddStuEBuyService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 记录学生消费信息
 * Created by Allen on 2015/7/29.
 */
@Service("addStuEBuyService")
public class AddStuEBuyServiceImpl extends EntityServiceImpl<StudentExpense,StudentExpenseDao> implements AddStuEBuyService {

    @Resource
    private FindRecordStudentCodeDao findRecordStudentCodeDao;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private SpotExpenseOthDAO spotExpenseOthDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addStuEBuy(StudentExpenseBuy studentExpenseBuy, String userName) throws Exception {
        Date operateTime = DateTools.getLongNowTime();
        //获取当前学期
        Semester semester = findNowSemesterDAO.getNowSemester();
        String studentCode = studentExpenseBuy.getStudentCode();
        //验证学生学号是否存在
        Student student = findStudentByCodeDAO.getStudentByCode(studentCode);
        if (null == student){
            throw new BusinessException("学号："+studentCode+", 该学生不存在");
        }
        //查询学生的费用信息
        List<StudentExpense> studentExpenseList = findRecordStudentCodeDao.getRecordByStuCode(studentCode);
        //不存在时，则为该学生添加记录
        if (null == studentExpenseList || studentExpenseList.size() == 0) {
            StudentExpense studentExpense = new StudentExpense();
            studentExpense.setStudentCode(studentCode);
            studentExpense.setBuy(studentExpenseBuy.getMoney());
            studentExpense.setPay(0f);
            studentExpense.setCreator(userName);
            studentExpense.setOperator(userName);
            studentExpense.setSemesterId(semester.getId());
            studentExpense.setState(StudentExpense.STATE_NO);
            findRecordStudentCodeDao.save(studentExpense);
            //更新中心交费
            this.updateSpotExpenseOth(student.getSpotCode(), studentExpense.getSemesterId(), studentExpenseBuy.getMoney(), userName);
        }else{
            //当前消费的钱
            double tempMoney = studentExpenseBuy.getMoney();
            //每学期多的钱
            double moreMoney = 0;
            //是否当前学期
            boolean isNowSemester = false;
            for (StudentExpense studentExpense : studentExpenseList) {
                float pay = null == studentExpense.getPay() ? 0 : studentExpense.getPay();
                float buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
                //是否当前学期
                if (semester.getId() == studentExpense.getSemesterId()) {
                    isNowSemester = true;
                    studentExpense.setPay(new BigDecimal(moreMoney).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    studentExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    if(studentExpense.getPay() >= studentExpense.getBuy()){
                        studentExpense.setState(StudentExpense.STATE_YES);
                        studentExpense.setClearTime(operateTime);
                    }else{
                        studentExpense.setState(StudentExpense.STATE_NO);
                    }
                    studentExpense.setOperator(userName);
                    studentExpense.setOperateTime(operateTime);
                    findRecordStudentCodeDao.update(studentExpense);
                    //更新中心交费
                    this.updateSpotExpenseOth3(student.getSpotCode(), studentExpense.getSemesterId(), moreMoney, tempMoney, pay, buy, userName);
                } else {
                    double temp = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //如果以前学期有多余的缴费，就存到最新的学期里，减去以前的缴费
                    if(temp > 0){
                        moreMoney = new BigDecimal(moreMoney).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        studentExpense.setPay(new BigDecimal(studentExpense.getPay()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        findRecordStudentCodeDao.update(studentExpense);
                        //更新中心交费
                        this.updateSpotExpenseOth2(student.getSpotCode(), studentExpense.getSemesterId(), temp, userName);
                    }
                }
            }
            if (!isNowSemester) {
                //新增费用记录
                StudentExpense studentExpense = new StudentExpense();
                studentExpense.setStudentCode(studentCode);
                studentExpense.setPay(new BigDecimal(moreMoney).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                studentExpense.setBuy(studentExpenseBuy.getMoney());
                if(moreMoney >= studentExpenseBuy.getMoney()){
                    studentExpense.setState(StudentExpense.STATE_YES);
                    studentExpense.setClearTime(operateTime);
                }else{
                    studentExpense.setState(StudentExpense.STATE_NO);
                }
                studentExpense.setCreator(userName);
                studentExpense.setOperator(userName);
                studentExpense.setSemesterId(semester.getId());
                //添加
                findRecordStudentCodeDao.save(studentExpense);
                //更新中心交费
                this.updateSpotExpenseOth4(student.getSpotCode(), studentExpense.getSemesterId(), studentExpense.getPay(), studentExpense.getBuy(), userName);
            }
        }
    }

    protected void updateSpotExpenseOth(String spotCode, long semesterId, float buy, String userName){
        Date operateTime = DateTools.getLongNowTime();
        List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
        if(null == spotExpenseOthList || 0 == spotExpenseOthList.size()){
            SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
            spotExpenseOth.setPay(0);
            spotExpenseOth.setBuy(buy);
            spotExpenseOth.setStuAccTot(0);
            spotExpenseOth.setStuOwnTot(buy);
            spotExpenseOth.setCreator(userName);
            spotExpenseOth.setOperator(userName);
            spotExpenseOth.setSemesterId(semesterId);
            spotExpenseOth.setSpotCode(spotCode);
            spotExpenseOth.setState(1);
            spotExpenseOthDAO.save(spotExpenseOth);
        }else{
            SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
            spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            if(spotExpenseOth.getStuOwnTot() > 0){
                spotExpenseOth.setState(1);
                spotExpenseOth.setClearTime(null);
            }else{
                spotExpenseOth.setState(0);
                if(null == spotExpenseOth.getClearTime()) {
                    spotExpenseOth.setClearTime(operateTime);
                }
            }
            spotExpenseOth.setOperator(userName);
            spotExpenseOth.setOperateTime(operateTime);
            spotExpenseOthDAO.update(spotExpenseOth);
        }
    }

    protected void updateSpotExpenseOth2(String spotCode, long semesterId, double temp, String userName){
        Date operateTime = DateTools.getLongNowTime();
        List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
        SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
        spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        if(spotExpenseOth.getStuAccTot() < temp){
            spotExpenseOth.setStuAccTot(0);
        }else{
            spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        }
        spotExpenseOth.setOperator(userName);
        spotExpenseOth.setOperateTime(operateTime);
        spotExpenseOthDAO.update(spotExpenseOth);
    }

    protected void updateSpotExpenseOth3(String spotCode, long semesterId, double moreMoney, double tempMoney, float pay, float buy, String userName){
        Date operateTime = DateTools.getLongNowTime();
        double oldTemp = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double temp = new BigDecimal(moreMoney).subtract(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
        SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
        spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(moreMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        if(oldTemp > 0){
            //以前有余额
            if(temp > 0){
                //本次消费还是有余额
                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }else{
                //本次消费有欠款
                if(oldTemp + temp >= 0){
                    //说明还有余额
                    spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                }else{
                    //说明没有余额，产生了欠款
                    spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).subtract(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                }
            }
        }
        if(oldTemp == 0){
            //以前无余额，也无欠款
            if(temp > 0){
                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }else{
                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        if(oldTemp < 0){
            //以前有欠款
            if(temp > 0){
                //本次消费还是有余额
                if(oldTemp + temp >= 0){
                    //说明还有余额
                    spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(oldTemp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(temp).add(new BigDecimal(oldTemp))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                }else{
                    //说明，抵消了一部分欠款，还有欠款
                    spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                }
            }else{
                //本次消费也有欠款
                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        if(spotExpenseOth.getStuOwnTot() <= 0){
            spotExpenseOth.setState(0);
            if(null == spotExpenseOth.getClearTime()) {
                spotExpenseOth.setClearTime(operateTime);
            }
        }else{
            spotExpenseOth.setState(1);
            spotExpenseOth.setClearTime(null);
        }
        spotExpenseOth.setOperator(userName);
        spotExpenseOth.setOperateTime(operateTime);
        spotExpenseOthDAO.update(spotExpenseOth);
    }

    protected void updateSpotExpenseOth4(String spotCode, long semesterId, float pay, float buy, String userName){
        Date operateTime = DateTools.getLongNowTime();
        List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semesterId, spotCode);
        if(null == spotExpenseOthList || 0 == spotExpenseOthList.size()){
            SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
            spotExpenseOth.setPay(pay);
            spotExpenseOth.setBuy(buy);
            if(pay >= buy){
                spotExpenseOth.setStuAccTot(new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseOth.setStuOwnTot(0);
                spotExpenseOth.setState(0);
                if(null == spotExpenseOth.getClearTime()) {
                    spotExpenseOth.setClearTime(operateTime);
                }
            }else{
                spotExpenseOth.setStuAccTot(0);
                spotExpenseOth.setStuOwnTot(new BigDecimal(buy).subtract(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseOth.setState(1);
                spotExpenseOth.setClearTime(null);
            }
            spotExpenseOth.setCreator(userName);
            spotExpenseOth.setOperator(userName);
            spotExpenseOth.setSemesterId(semesterId);
            spotExpenseOth.setSpotCode(spotCode);
            spotExpenseOthDAO.save(spotExpenseOth);
        }else{
            SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
            spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            if(pay > buy){
                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(pay)).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
            }else{
                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(buy).subtract(new BigDecimal(pay))).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                if(spotExpenseOth.getStuOwnTot() > 0){
                    spotExpenseOth.setState(1);
                    spotExpenseOth.setClearTime(null);
                }
            }
            spotExpenseOth.setOperator(userName);
            spotExpenseOth.setOperateTime(operateTime);
            spotExpenseOthDAO.update(spotExpenseOth);
        }
    }
}