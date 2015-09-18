package com.zs.service.scheduler.impl;

import com.zs.dao.basic.finance.spotexpenseoth.FindBySpotCodeAndSemesterDAO;
import com.zs.dao.basic.finance.studentexpense.FindByStudentCodeDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.dao.sync.StudentTempDAO;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.sync.Student;
import com.zs.domain.sync.StudentTemp;
import com.zs.service.scheduler.SyncStudentTaskService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

/**
 * 同步网院学生信息
 * Created by Allen on 2015/9/17.
 */
@Service("syncStudentTaskService")
public class SyncStudentTaskServiceImpl implements SyncStudentTaskService {

    @Resource
    private StudentTempDAO studentTempDAO;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private FindByStudentCodeDAO findByStudentCodeDAO;
    @Resource
    private FindBySpotCodeAndSemesterDAO findBySpotCodeAndSemesterDAO;

    @Override
    @Transactional
    public void syncStudent() {
        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学生信息同步\r\n");
        String studentCode = "";
        int tempNum = 0;
        try {
            //查询临时表里面的数据
            List<StudentTemp> studentTempList = studentTempDAO.getAll();
            if (null != studentTempList) {
                for (StudentTemp studentTemp : studentTempList) {
                    studentCode = studentTemp.getCode();
                    //查询我们已有数据中，是否存在该学号
                    Student student = findStudentByCodeDAO.getStudentByCode(studentCode);
                    if (null != student && null != student.getId()) {
                        //说明存在该学生信息，逐个数据比较，如果有差异，就更改
                        boolean isUpdate = false;
                        //姓名
                        if (!student.getName().equals(studentTemp.getName())) {
                            student.setName(studentTemp.getName());
                            isUpdate = true;
                        }
                        //性别
                        if (StudentTemp.SEX_MAN == studentTemp.getSex() && Student.SEX_MAN != student.getSex()) {
                            student.setSex(Student.SEX_MAN);
                            isUpdate = true;
                        }
                        if (StudentTemp.SEX_FEMALE == studentTemp.getSex() && Student.SEX_FEMALE != student.getSex()) {
                            student.setSex(Student.SEX_FEMALE);
                            isUpdate = true;
                        }
                        //证件类型
                        if (StudentTemp.IDCARD_TYPE_IDCARD == studentTemp.getIdcardType() && Student.IDCARD_TYPE_IDCARD != student.getIdcardType()) {
                            student.setIdcardType(Student.IDCARD_TYPE_IDCARD);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_MILITARY_OFFICER == studentTemp.getIdcardType() && Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()) {
                            student.setIdcardType(Student.IDCARD_TYPE_MILITARY_OFFICER);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_PASSPORT == studentTemp.getIdcardType() && Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()) {
                            student.setIdcardType(Student.IDCARD_TYPE_PASSPORT);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_HK_MACAO_TAIWAN == studentTemp.getIdcardType() && Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()) {
                            student.setIdcardType(Student.IDCARD_TYPE_HK_MACAO_TAIWAN);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_OTHER == studentTemp.getIdcardType() && Student.IDCARD_TYPE_OTHER != student.getIdcardType()) {
                            student.setIdcardType(Student.IDCARD_TYPE_OTHER);
                            isUpdate = true;
                        }
                        //证件号码
                        if (!student.getIdcardNo().equals(studentTemp.getIdcardNo())) {
                            student.setIdcardNo(studentTemp.getIdcardNo());
                            isUpdate = true;
                        }
                        //邮编
                        if (!student.getIdcardNo().equals(studentTemp.getIdcardNo())) {
                            student.setIdcardNo(studentTemp.getIdcardNo());
                            isUpdate = true;
                        }
                        //地址
                        if (!student.getAddress().equals(studentTemp.getAddress())) {
                            student.setAddress(studentTemp.getAddress());
                            isUpdate = true;
                        }
                        //手机
                        if (!student.getMobile().equals(studentTemp.getMobile())) {
                            student.setMobile(studentTemp.getMobile());
                            isUpdate = true;
                        }
                        //家庭电话
                        if (!student.getHomeTel().equals(studentTemp.getHomeTel())) {
                            student.setHomeTel(studentTemp.getHomeTel());
                            isUpdate = true;
                        }
                        //公司电话
                        if (!student.getCompanyTel().equals(studentTemp.getCompanyTel())) {
                            student.setCompanyTel(studentTemp.getCompanyTel());
                            isUpdate = true;
                        }
                        //邮箱
                        if (!student.getEmail().equals(studentTemp.getEmail())) {
                            student.setEmail(studentTemp.getEmail());
                            isUpdate = true;
                        }
                        //学习中心
                        if (!student.getSpotCode().equals(studentTemp.getSpotCode())) {
                            student.setSpotCode(studentTemp.getSpotCode());
                            isUpdate = true;
                        }
                        //专业
                        if (!student.getSpecCode().equals(studentTemp.getSpecCode())) {
                            student.setSpecCode(studentTemp.getSpecCode());
                            isUpdate = true;
                        }
                        //层次
                        if (!student.getLevelCode().equals(studentTemp.getLevelCode())) {
                            student.setLevelCode(studentTemp.getLevelCode());
                            isUpdate = true;
                        }
                        //类型
                        if (1 == studentTemp.getType() && 3 != student.getType()) {
                            student.setType(3);
                            isUpdate = true;
                        }
                        if (2 == studentTemp.getType() && 4 != student.getType()) {
                            student.setType(4);
                            isUpdate = true;
                        }
                        if (26 == studentTemp.getType() && 0 != student.getType()) {
                            student.setType(0);
                            isUpdate = true;
                        }
                        if (27 == studentTemp.getType() && 1 != student.getType()) {
                            student.setType(1);
                            isUpdate = true;
                        }
                        if (67 == studentTemp.getType() && 2 != student.getType()) {
                            student.setType(2);
                            isUpdate = true;
                        }
                        //状态
                        if (53 == studentTemp.getState() && 1 != student.getState()) {
                            student.setState(1);
                            isUpdate = true;
                        }
                        if (77 == studentTemp.getState() && 2 != student.getState()) {
                            student.setState(2);
                            isUpdate = true;
                        }
                        if (78 == studentTemp.getState() && 3 != student.getState()) {
                            student.setState(3);
                            isUpdate = true;
                        }
                        if (54 == studentTemp.getState() && 0 != student.getState()) {
                            student.setState(0);
                            isUpdate = true;
                        }
                        if (55 == studentTemp.getState() && 4 != student.getState()) {
                            student.setState(4);
                            isUpdate = true;
                        }
                        //入学年
                        if (!student.getStudyEnterYear().equals(studentTemp.getStudyEnterYear())) {
                            student.setStudyEnterYear(studentTemp.getStudyEnterYear());
                            isUpdate = true;
                        }
                        //入学季
                        if (!student.getStudyQuarter().equals(studentTemp.getStudyQuarter())) {
                            student.setStudyQuarter(studentTemp.getStudyQuarter());
                            isUpdate = true;
                        }

                        if (isUpdate) {
                            findStudentByCodeDAO.update(student);
                        }
                    } else {
                        //说明不存在该学生信息，新增该学生信息
                        student.setCode(studentTemp.getCode());
                        student.setName(studentTemp.getName());
                        if (studentTemp.getSex() == StudentTemp.SEX_MAN) {
                            student.setSex(Student.SEX_MAN);
                        }
                        if (studentTemp.getSex() == StudentTemp.SEX_FEMALE) {
                            student.setSex(Student.SEX_FEMALE);
                        }
                        if (studentTemp.getIdcardType() == StudentTemp.IDCARD_TYPE_IDCARD) {
                            student.setIdcardType(Student.IDCARD_TYPE_IDCARD);
                        }
                        if (studentTemp.getIdcardType() == StudentTemp.IDCARD_TYPE_MILITARY_OFFICER) {
                            student.setIdcardType(Student.IDCARD_TYPE_MILITARY_OFFICER);
                        }
                        if (studentTemp.getIdcardType() == StudentTemp.IDCARD_TYPE_HK_MACAO_TAIWAN) {
                            student.setIdcardType(Student.IDCARD_TYPE_HK_MACAO_TAIWAN);
                        }
                        if (studentTemp.getIdcardType() == StudentTemp.IDCARD_TYPE_PASSPORT) {
                            student.setIdcardType(Student.IDCARD_TYPE_PASSPORT);
                        }
                        if (studentTemp.getIdcardType() == StudentTemp.IDCARD_TYPE_OTHER) {
                            student.setIdcardType(Student.IDCARD_TYPE_OTHER);
                        }
                        student.setIdcardNo(studentTemp.getIdcardNo());
                        student.setPostalCode(studentTemp.getPostalCode());
                        student.setAddress(studentTemp.getAddress());
                        student.setMobile(studentTemp.getMobile());
                        student.setHomeTel(studentTemp.getHomeTel());
                        student.setEmail(studentTemp.getEmail());
                        student.setSpotCode(studentTemp.getSpotCode());
                        student.setSpecCode(studentTemp.getSpecCode());
                        student.setLevelCode(studentTemp.getLevelCode());
                        if (1 == studentTemp.getType()) {
                            student.setType(3);
                        }
                        if (2 == studentTemp.getType()) {
                            student.setType(4);
                        }
                        if (26 == studentTemp.getType()) {
                            student.setType(0);
                        }
                        if (27 == studentTemp.getType()) {
                            student.setType(1);
                        }
                        if (67 == studentTemp.getType()) {
                            student.setType(2);
                        }
                        if (53 == studentTemp.getState()) {
                            student.setState(1);
                        }
                        if (77 == studentTemp.getState()) {
                            student.setState(2);
                        }
                        if (78 == studentTemp.getState()) {
                            student.setState(3);
                        }
                        if (54 == studentTemp.getState()) {
                            student.setState(0);
                        }
                        if (55 == studentTemp.getState()) {
                            student.setState(4);
                        }
                        student.setStudyEnterYear(studentTemp.getStudyEnterYear());
                        if (1 == studentTemp.getStudyQuarter()) {
                            student.setStudyQuarter(0);
                        }
                        if (2 == studentTemp.getStudyQuarter()) {
                            student.setStudyQuarter(1);
                        }
                        student.setOperateTime(DateTools.getLongNowTime());
                        findStudentByCodeDAO.save(student);
                    }
                }
                tempNum++;
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学号："+studentCode+" \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
        }
        finally {
            msg.append("执行了"+tempNum+"条数据\r\n");
            msg.append(DateTools.getLongNowTime()+": 学生信息同步结束");

            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            String filePath = propertiesTools.getProperty("sync.student.log.file.path");
            String nowDate = DateTools.transferLongToDate("yyyy-MM-dd", System.currentTimeMillis());
            FileTools.createFile(rootPath + filePath, nowDate + ".txt");
            FileTools.writeTxtFile(msg.toString(), rootPath + filePath + nowDate + ".txt");
        }
    }


    protected void updateStudentSpotExpense(String oldSpotCode, String newSpotCode, String studentCode)throws Exception{
        //查询学生财务信息
        List<StudentExpense> studentExpenseList = findByStudentCodeDAO.findByStudentCode(studentCode);
        if(null != studentExpenseList && 0 < studentExpenseList.size()){
            for (StudentExpense studentExpense : studentExpenseList){
                //查询旧中心的费用信息
                SpotExpenseOth spotExpenseOth = findBySpotCodeAndSemesterDAO.findBySpotCodeAndSemester(oldSpotCode, studentExpense.getSemesterId());
                if(null != spotExpenseOth){
                    float spotPay = spotExpenseOth.getPay();
                    float spotBuy = spotExpenseOth.getBuy();
                    float stuOwnTot = spotExpenseOth.getStuOwnTot();
                    float stuAccTot = spotExpenseOth.getStuAccTot();
                    float studentPay = null == studentExpense.getPay() ? 0 : studentExpense.getPay();
                    float studentBuy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();

                    spotExpenseOth.setPay(new BigDecimal(spotPay).subtract(new BigDecimal(studentPay)).floatValue());
                    spotExpenseOth.setBuy(new BigDecimal(spotBuy).subtract(new BigDecimal(studentBuy)).floatValue());
                    //学生没有欠款
                    if(studentPay >= studentBuy){
                        spotExpenseOth.setStuAccTot(new BigDecimal(stuAccTot).subtract(new BigDecimal(studentPay).add(new BigDecimal(studentBuy))).floatValue());
                    }else{
                        //学生有欠款
                        spotExpenseOth.setStuOwnTot(new BigDecimal(stuOwnTot).subtract(new BigDecimal(studentBuy).add(new BigDecimal(studentPay))).floatValue());
                    }
                    //算学习中心是否结清
                    findBySpotCodeAndSemesterDAO.update(spotExpenseOth);
                }
            }
        }
    }
}
