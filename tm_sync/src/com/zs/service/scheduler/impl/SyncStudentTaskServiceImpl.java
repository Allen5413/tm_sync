package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.finance.spotexpenseoth.BatchSpotExpenseOthDAO;
import com.zs.dao.finance.spotexpenseoth.FindBySpotCodeAndSemesterDAO;
import com.zs.dao.finance.studentexpense.FindByStudentCodeDAO;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookorder.StudentBookOrderDAO;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.dao.sale.studentbookordertm.StudentBookOrderTmDAO;
import com.zs.dao.sync.*;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.SelectedCourseTemp;
import com.zs.domain.sync.Student;
import com.zs.domain.sync.StudentTemp;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.scheduler.SyncStudentTaskService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.OrderCodeTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步网院学生信息
 * Created by Allen on 2015/9/17.
 */
@Service("syncStudentTaskService")
public class SyncStudentTaskServiceImpl implements SyncStudentTaskService {

    @Resource
    private FindStudentForChangeDAO findStudentForChangeDAO;
    @Resource
    private FindByStudentCodeDAO findByStudentCodeDAO;
    @Resource
    private FindBySpotCodeAndSemesterDAO findBySpotCodeAndSemesterDAO;
    @Resource
    private SelectedCourseDAO selectedCourseDAO;
    @Resource
    private SelectedCourseTempDAO selectedCourseTempDAO;
    @Resource
    private StudentBookOrderDAO studentBookOrderDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindStudentBookOrderForMaxCodeDAO findStudentBookOrderForMaxCodeDAO;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private BatchStudentDAO batchStudentDAO;
    @Resource
    private BatchSelectedCourseDAO batchSelectedCourseDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;
    @Resource
    private BatchStudentBookOrderLogDAO batchStudentBookOrderLogDAO;
    @Resource
    private BatchSpotExpenseOthDAO batchSpotExpenseOthDAO;

    //变更信息描述
    private String detail = "";

    private List<Student> addStudentList = new ArrayList<Student>();
    private List<Student> editStudentList = new ArrayList<Student>();
    private List<SelectedCourse> addSelectCourseList = new ArrayList<SelectedCourse>();
    private List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
    private List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
    private List<StudentBookOrderLog> addStudentBookOrderLogList = new ArrayList<StudentBookOrderLog>();
    private List<SpotExpenseOth> addSpotExpenseOthList = new ArrayList<SpotExpenseOth>();
    private List<SpotExpenseOth> editSpotExpenseOthList = new ArrayList<SpotExpenseOth>();

    /**
     * 同步学生信息
     */
    @Override
    @Transactional
    public void syncStudent() {
        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学生信息同步\r\n");
        String studentCode = "";
        int tempNum = 0;
        try {
            //查询有变化的学生数据
            List<Object[]> resultList = findStudentForChangeDAO.find();
            List<Object[]> newResultList = findStudentForChangeDAO.findNewStudent();
            if (null != resultList) {
                int i=0;
                for (Object[] obj : resultList) {
                    System.out.println("i:  "+i);
                    i++;
                    //得到学生原有信息和学生新的信息
                    Student student = this.getStudent(obj);
                    StudentTemp studentTemp = this.getStudentTemp(obj, 0);
                    studentCode = student.getCode();
                    if (null != student && null != student.getId()) {
                        //说明存在该学生信息，逐个数据比较，如果有差异，就更改
                        boolean isUpdate = false;
                        boolean isChangeSpot = false;
                        String oldSpotCode = "";
                        detail += "学号["+studentCode+"]: ";
                        //入学年,如果有学籍入学年，优先使用学籍入学年
                        if(null != studentTemp.getStudyEnterYear()) {
                            if (!student.getStudyEnterYear().equals(studentTemp.getStudyEnterYear())) {
                                detail += "入学年由 " + student.getStudyEnterYear() + " 改为 " + studentTemp.getStudyEnterYear() + "、";
                                student.setStudyEnterYear(studentTemp.getStudyEnterYear());
                                isUpdate = true;
                            }
                        }else{
                            if (!student.getStudyEnterYear().equals(studentTemp.getEnterYear())) {
                                detail += "入学年由 " + student.getStudyEnterYear() + " 改为 " + studentTemp.getEnterYear() + "、";
                                student.setStudyEnterYear(studentTemp.getEnterYear());
                                isUpdate = true;
                            }
                        }
                        //入学季, 如果有学籍入学季，优先使用学籍入学季
                        if(null != studentTemp.getStudyQuarter()) {
                            if (1 == studentTemp.getStudyQuarter() && 0 != student.getStudyQuarter()) {
                                student.setStudyQuarter(0);
                                isUpdate = true;
                                detail += "入学季由 秋季 改为 春季 、";
                            }
                            if (2 == studentTemp.getStudyQuarter() && 1 != student.getStudyQuarter()) {
                                student.setStudyQuarter(1);
                                isUpdate = true;
                                detail += "入学季由 春季 改为 秋季 、";
                            }
                        }else{
                            if (1 == studentTemp.getQuarter() && 0 != student.getStudyQuarter()) {
                                student.setStudyQuarter(0);
                                isUpdate = true;
                                detail += "入学季由 秋季 改为 春季 、";
                            }
                            if (2 == studentTemp.getQuarter() && 1 != student.getStudyQuarter()) {
                                student.setStudyQuarter(1);
                                isUpdate = true;
                                detail += "入学季由 春季 改为 秋季 、";
                            }
                        }
                        //姓名
                        if (!student.getName().equals(studentTemp.getName())) {
                            detail += "姓名由 "+student.getName()+" 改为 "+studentTemp.getName()+"、";
                            student.setName(studentTemp.getName());
                            isUpdate = true;
                        }
                        //性别
                        if (StudentTemp.SEX_MAN == studentTemp.getSex() && Student.SEX_MAN != student.getSex()) {
                            student.setSex(Student.SEX_MAN);
                            isUpdate = true;
                            detail += "性别由 女 改为 男、";
                        }
                        if (StudentTemp.SEX_FEMALE == studentTemp.getSex() && Student.SEX_FEMALE != student.getSex()) {
                            student.setSex(Student.SEX_FEMALE);
                            isUpdate = true;
                            detail += "性别由 男 改为 女、";
                        }
                        //证件类型
                        if (StudentTemp.IDCARD_TYPE_IDCARD == studentTemp.getIdcardType() && Student.IDCARD_TYPE_IDCARD != student.getIdcardType()) {
                            detail += "证件类型由 ";
                            if(Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()){
                                detail += "港、澳、台居民证件 ";
                            }
                            if(Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()){
                                detail += "军官证 ";
                            }
                            if(Student.IDCARD_TYPE_OTHER != student.getIdcardType()){
                                detail += "其它 ";
                            }
                            if(Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()){
                                detail += "护照 ";
                            }
                            detail += "改为 身份证、 ";
                            student.setIdcardType(Student.IDCARD_TYPE_IDCARD);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_MILITARY_OFFICER == studentTemp.getIdcardType() && Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()) {
                            detail += "证件类型由 ";
                            if(Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()){
                                detail += "港、澳、台居民证件 ";
                            }
                            if(Student.IDCARD_TYPE_IDCARD != student.getIdcardType()){
                                detail += "身份证 ";
                            }
                            if(Student.IDCARD_TYPE_OTHER != student.getIdcardType()){
                                detail += "其它 ";
                            }
                            if(Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()){
                                detail += "护照 ";
                            }
                            detail += "改为 军官证、 ";
                            student.setIdcardType(Student.IDCARD_TYPE_MILITARY_OFFICER);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_PASSPORT == studentTemp.getIdcardType() && Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()) {
                            detail += "证件类型由 ";
                            if(Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()){
                                detail += "港、澳、台居民证件 ";
                            }
                            if(Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()){
                                detail += "军官证 ";
                            }
                            if(Student.IDCARD_TYPE_OTHER != student.getIdcardType()){
                                detail += "其它 ";
                            }
                            if(Student.IDCARD_TYPE_IDCARD != student.getIdcardType()){
                                detail += "身份证 ";
                            }
                            detail += "改为 护照、 ";
                            student.setIdcardType(Student.IDCARD_TYPE_PASSPORT);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_HK_MACAO_TAIWAN == studentTemp.getIdcardType() && Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()) {
                            detail += "证件类型由 ";
                            if(Student.IDCARD_TYPE_IDCARD != student.getIdcardType()){
                                detail += "身份证 ";
                            }
                            if(Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()){
                                detail += "军官证 ";
                            }
                            if(Student.IDCARD_TYPE_OTHER != student.getIdcardType()){
                                detail += "其它 ";
                            }
                            if(Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()){
                                detail += "护照 ";
                            }
                            detail += "改为 港、澳、台居民证件、 ";
                            student.setIdcardType(Student.IDCARD_TYPE_HK_MACAO_TAIWAN);
                            isUpdate = true;
                        }
                        if (StudentTemp.IDCARD_TYPE_OTHER == studentTemp.getIdcardType() && Student.IDCARD_TYPE_OTHER != student.getIdcardType()) {
                            detail += "证件类型由 ";
                            if(Student.IDCARD_TYPE_IDCARD != student.getIdcardType()){
                                detail += "身份证 ";
                            }
                            if(Student.IDCARD_TYPE_MILITARY_OFFICER != student.getIdcardType()){
                                detail += "军官证 ";
                            }
                            if(Student.IDCARD_TYPE_HK_MACAO_TAIWAN != student.getIdcardType()){
                                detail += "港、澳、台居民证件 ";
                            }
                            if(Student.IDCARD_TYPE_PASSPORT != student.getIdcardType()){
                                detail += "护照 ";
                            }
                            detail += "改为 其它、 ";
                            student.setIdcardType(Student.IDCARD_TYPE_OTHER);
                            isUpdate = true;
                        }
                        //证件号码
                        if (!student.getIdcardNo().equals(studentTemp.getIdcardNo())) {
                            detail += "证件号码由 "+student.getIdcardNo()+" 改为 "+studentTemp.getIdcardNo()+"、";
                            student.setIdcardNo(studentTemp.getIdcardNo());
                            isUpdate = true;
                        }
                        //邮编
                        if(null == student.getPostalCode() && null != studentTemp.getPostalCode()){
                            detail += "邮编由 Null 改为 "+studentTemp.getPostalCode()+"、";
                            student.setPostalCode(studentTemp.getPostalCode());
                            isUpdate = true;
                        }
                        if(null != student.getPostalCode() && null == studentTemp.getPostalCode()){
                            detail += "邮编由 "+student.getPostalCode()+" 改为 Null、";
                            student.setPostalCode(studentTemp.getPostalCode());
                            isUpdate = true;
                        }
                        if(null != student.getPostalCode() && null != studentTemp.getPostalCode()) {
                            if (!student.getPostalCode().equals(studentTemp.getPostalCode())) {
                                detail += "邮编由 " + student.getPostalCode() + " 改为 " + studentTemp.getPostalCode() + "、";
                                student.setPostalCode(studentTemp.getPostalCode());
                                isUpdate = true;
                            }
                        }
                        //地址
                        if(null == student.getAddress() && null != studentTemp.getAddress()){
                            detail += "地址由 Null 改为 "+studentTemp.getAddress()+"、";
                            student.setAddress(studentTemp.getAddress());
                            isUpdate = true;
                        }
                        if(null != student.getAddress() && null == studentTemp.getAddress()){
                            detail += "地址由 "+student.getAddress()+" 改为 Null、";
                            student.setAddress(studentTemp.getAddress());
                            isUpdate = true;
                        }
                        if(null != student.getAddress() && null != studentTemp.getAddress()) {
                            if (!student.getAddress().equals(studentTemp.getAddress())) {
                                detail += "地址由 " + student.getAddress() + " 改为 " + studentTemp.getAddress() + "、";
                                student.setAddress(studentTemp.getAddress());
                                isUpdate = true;
                            }
                        }
                        //手机
                        if(null == student.getMobile() && null != studentTemp.getMobile()){
                            detail += "手机由 Null 改为 "+studentTemp.getMobile()+"、";
                            student.setMobile(studentTemp.getMobile());
                            isUpdate = true;
                        }
                        if(null != student.getMobile() && null == studentTemp.getMobile()){
                            detail += "手机由 "+student.getMobile()+" 改为 Null、";
                            student.setMobile(studentTemp.getMobile());
                            isUpdate = true;
                        }
                        if(null != student.getMobile() && null != studentTemp.getMobile()) {
                            if (!student.getMobile().equals(studentTemp.getMobile())) {
                                detail += "手机由 " + student.getMobile() + " 改为 " + studentTemp.getMobile() + "、";
                                student.setMobile(studentTemp.getMobile());
                                isUpdate = true;
                            }
                        }
                        //家庭电话
                        if(null == student.getHomeTel() && null != studentTemp.getHomeTel()){
                            detail += "家庭电话由 Null 改为 "+studentTemp.getHomeTel()+"、";
                            student.setHomeTel(studentTemp.getHomeTel());
                            isUpdate = true;
                        }
                        if(null != student.getHomeTel() && null == studentTemp.getHomeTel()){
                            detail += "家庭电话由 "+student.getHomeTel()+" 改为 Null、";
                            student.setHomeTel(studentTemp.getHomeTel());
                            isUpdate = true;
                        }
                        if(null != student.getHomeTel() && null != studentTemp.getHomeTel()) {
                            if (!student.getHomeTel().equals(studentTemp.getHomeTel())) {
                                detail += "家庭电话由 " + student.getHomeTel() + " 改为 " + studentTemp.getHomeTel() + "、";
                                student.setHomeTel(studentTemp.getHomeTel());
                                isUpdate = true;
                            }
                        }
                        //公司电话
                        if(null == student.getCompanyTel() && null != studentTemp.getCompanyTel()){
                            detail += "公司电话由 Null 改为 "+studentTemp.getCompanyTel()+"、";
                            student.setCompanyTel(studentTemp.getCompanyTel());
                            isUpdate = true;
                        }
                        if(null != student.getCompanyTel() && null == studentTemp.getCompanyTel()){
                            detail += "公司电话由 "+student.getCompanyTel()+" 改为 Null、";
                            student.setCompanyTel(studentTemp.getCompanyTel());
                            isUpdate = true;
                        }
                        if(null != student.getCompanyTel() && null != studentTemp.getCompanyTel()) {
                            if (!student.getCompanyTel().equals(studentTemp.getCompanyTel())) {
                                detail += "公司电话由 " + student.getCompanyTel() + " 改为 " + studentTemp.getCompanyTel() + "、";
                                student.setCompanyTel(studentTemp.getCompanyTel());
                                isUpdate = true;
                            }
                        }
                        //邮箱
                        if(null == student.getEmail() && null != studentTemp.getEmail()){
                            detail += "邮箱由 Null 改为 "+studentTemp.getEmail()+"、";
                            student.setEmail(studentTemp.getEmail());
                            isUpdate = true;
                        }
                        if(null != student.getEmail() && null == studentTemp.getEmail()){
                            detail += "邮箱由 "+student.getEmail()+" 改为 Null、";
                            student.setEmail(studentTemp.getEmail());
                            isUpdate = true;
                        }
                        if(null != student.getEmail() && null != studentTemp.getEmail()) {
                            if (!student.getEmail().equals(studentTemp.getEmail())) {
                                detail += "邮箱由 " + student.getEmail() + " 改为 " + studentTemp.getEmail() + "、";
                                student.setEmail(studentTemp.getEmail());
                                isUpdate = true;
                            }
                        }
                        //学习中心
                        if (!student.getSpotCode().equals(studentTemp.getSpotCode())) {
                            oldSpotCode = student.getSpotCode();
                            detail += "学习中心由 "+student.getSpotCode()+" 改为 "+studentTemp.getSpotCode()+"、";
                            student.setSpotCode(studentTemp.getSpotCode());
                            isUpdate = true;
                            isChangeSpot = true;
                        }
                        //专业
                        if (!student.getSpecCode().equals(studentTemp.getSpecCode())) {
                            detail += "专业由 "+student.getSpecCode()+" 改为 "+studentTemp.getSpecCode()+"、";
                            student.setSpecCode(studentTemp.getSpecCode());
                            isUpdate = true;
                        }
                        //层次
                        if (!student.getLevelCode().equals(studentTemp.getLevelCode())) {
                            detail += "层次由 "+student.getLevelCode()+" 改为 "+studentTemp.getLevelCode()+"、";
                            student.setLevelCode(studentTemp.getLevelCode());
                            isUpdate = true;
                        }
                        //类型
                        if (1 == studentTemp.getType() && 3 != student.getType()) {
                            detail += "类型由 ";
                            if(0 == student.getType()){
                                detail += "普通";
                            }
                            if(1 == student.getType()){
                                detail += "免试";
                            }
                            if(2 == student.getType()){
                                detail += "课程";
                            }
                            if(4 == student.getType()){
                                detail += "业余";
                            }
                            detail += " 改为 函授、 ";
                            student.setType(3);
                            isUpdate = true;
                        }
                        if (2 == studentTemp.getType() && 4 != student.getType()) {
                            detail += "类型由 ";
                            if(0 == student.getType()){
                                detail += "普通";
                            }
                            if(1 == student.getType()){
                                detail += "免试";
                            }
                            if(2 == student.getType()){
                                detail += "课程";
                            }
                            if(3 == student.getType()){
                                detail += "函授";
                            }
                            detail += " 改为 业余、 ";
                            student.setType(4);
                            isUpdate = true;
                        }
                        if (26 == studentTemp.getType() && 0 != student.getType()) {
                            detail += "类型由 ";
                            if(4 == student.getType()){
                                detail += "业余";
                            }
                            if(1 == student.getType()){
                                detail += "免试";
                            }
                            if(2 == student.getType()){
                                detail += "课程";
                            }
                            if(3 == student.getType()){
                                detail += "函授";
                            }
                            detail += " 改为 普通、 ";
                            student.setType(0);
                            isUpdate = true;
                        }
                        if (27 == studentTemp.getType() && 1 != student.getType()) {
                            detail += "类型由 ";
                            if(4 == student.getType()){
                                detail += "业余";
                            }
                            if(0 == student.getType()){
                                detail += "普通";
                            }
                            if(2 == student.getType()){
                                detail += "课程";
                            }
                            if(3 == student.getType()){
                                detail += "函授";
                            }
                            detail += " 改为 免试、 ";
                            student.setType(1);
                            isUpdate = true;
                        }
                        if (67 == studentTemp.getType() && 2 != student.getType()) {
                            detail += "类型由 ";
                            if(4 == student.getType()){
                                detail += "业余";
                            }
                            if(0 == student.getType()){
                                detail += "普通";
                            }
                            if(1 == student.getType()){
                                detail += "免试";
                            }
                            if(3 == student.getType()){
                                detail += "函授";
                            }
                            detail += " 改为 课程、 ";
                            student.setType(2);
                            isUpdate = true;
                        }
                        //状态
                        if (53 == studentTemp.getState() && 1 != student.getState()) {
                            detail += "状态由 ";
                            if(0 == student.getState()){
                                detail += "在籍";
                            }
                            if(2 == student.getState()){
                                detail += "休学";
                            }
                            if(3 == student.getState()){
                                detail += "退学";
                            }
                            if(4 == student.getState()){
                                detail += "毕业";
                            }
                            detail += " 改为 停用、 ";
                            student.setState(1);
                            isUpdate = true;
                        }
                        if (77 == studentTemp.getState() && 2 != student.getState()) {
                            detail += "状态由 ";
                            if(0 == student.getState()){
                                detail += "在籍";
                            }
                            if(1 == student.getState()){
                                detail += "停用";
                            }
                            if(3 == student.getState()){
                                detail += "退学";
                            }
                            if(4 == student.getState()){
                                detail += "毕业";
                            }
                            detail += " 改为 休学、 ";
                            student.setState(2);
                            isUpdate = true;
                        }
                        if (78 == studentTemp.getState() && 3 != student.getState()) {
                            detail += "状态由 ";
                            if(0 == student.getState()){
                                detail += "在籍";
                            }
                            if(1 == student.getState()){
                                detail += "停用";
                            }
                            if(2 == student.getState()){
                                detail += "休学";
                            }
                            if(4 == student.getState()){
                                detail += "毕业";
                            }
                            detail += " 改为 退学、 ";
                            student.setState(3);
                            isUpdate = true;
                        }
                        if (54 == studentTemp.getState() && 0 != student.getState()) {
                            detail += "状态由 ";
                            if(3 == student.getState()){
                                detail += "退学";
                            }
                            if(1 == student.getState()){
                                detail += "停用";
                            }
                            if(2 == student.getState()){
                                detail += "休学";
                            }
                            if(4 == student.getState()){
                                detail += "毕业";
                            }
                            detail += " 改为 在籍、 ";
                            student.setState(0);
                            isUpdate = true;
                        }
                        if (55 == studentTemp.getState() && 4 != student.getState()) {
                            detail += "状态由 ";
                            if(3 == student.getState()){
                                detail += "退学";
                            }
                            if(1 == student.getState()){
                                detail += "停用";
                            }
                            if(2 == student.getState()){
                                detail += "休学";
                            }
                            if(0 == student.getState()){
                                detail += "在籍";
                            }
                            detail += " 改为 毕业、";
                            student.setState(4);
                            isUpdate = true;
                        }

                        if (isUpdate) {
                            detail += "信息发生变更。\r\n";
                            if(isChangeSpot){
                                this.updateStudentSpotExpense(oldSpotCode, student.getSpotCode(), student.getCode());
                            }
                            Timestamp operateTime = DateTools.getLongNowTime();
                            String changeSpotDetail = operateTime.toString()+", 由"+oldSpotCode+"中心转到"+student.getSpotCode()+"中心；";
                            student.setChangeSpotDetail((null == student.getChangeSpotDetail() ? "" : student.getChangeSpotDetail())+changeSpotDetail);
                            editStudentList.add(student);
                        }else{
                            detail = "";
                        }
                    }
                    tempNum++;
                }
            }

            if(null != newResultList){
                int j=0;
                for (Object[] obj : newResultList) {
                    System.out.println("j:  "+j);
                    j++;
                    Student student = new Student();
                    StudentTemp studentTemp = this.getStudentTemp(obj, 1);
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
                    student.setStudyEnterYear(null == studentTemp.getStudyEnterYear() ? studentTemp.getEnterYear() : studentTemp.getStudyEnterYear());
                    if(null != studentTemp.getStudyQuarter()) {
                        if (1 == studentTemp.getStudyQuarter()) {
                            student.setStudyQuarter(0);
                        }
                        if (2 == studentTemp.getStudyQuarter()) {
                            student.setStudyQuarter(1);
                        }
                    }else{
                        if (1 == studentTemp.getQuarter()) {
                            student.setStudyQuarter(0);
                        }
                        if (2 == studentTemp.getQuarter()) {
                            student.setStudyQuarter(1);
                        }
                    }
                    student.setOperateTime(DateTools.getLongNowTime());
                    detail += "学号："+studentTemp.getCode()+", 为新增学生。\r\n";
                    addStudentList.add(student);

                    //如果学生状态为在籍，检查学生的选课
                    if(student.getState() == 0) {
                        this.syncSelectedCourse(student.getCode(), student.getSpotCode());
                    }
                    tempNum++;
                }
            }
            //执行要操作的数据
            this.doDataOperate();
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学号："+studentCode+" \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
        }
        finally {
            msg.append("\r\n"+detail+"\r\n");
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

    /**
     * 修改学生的学习中心
     * @param oldSpotCode
     * @param newSpotCode
     * @param studentCode
     * @throws Exception
     */
    protected void updateStudentSpotExpense(String oldSpotCode, String newSpotCode, String studentCode)throws Exception{
        //查询学生财务信息
        List<StudentExpense> studentExpenseList = findByStudentCodeDAO.findByStudentCode(studentCode);
        if(null != studentExpenseList && 0 < studentExpenseList.size()){
            for (StudentExpense studentExpense : studentExpenseList){
                //查询旧中心的费用信息
                SpotExpenseOth spotExpenseOth = findBySpotCodeAndSemesterDAO.findBySpotCodeAndSemester(oldSpotCode, studentExpense.getSemesterId());
                float studentPay = null == studentExpense.getPay() ? 0 : studentExpense.getPay();
                float studentBuy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
                if(null != spotExpenseOth){
                    float spotPay = spotExpenseOth.getPay();
                    float spotBuy = spotExpenseOth.getBuy();
                    float stuOwnTot = spotExpenseOth.getStuOwnTot();
                    float stuAccTot = spotExpenseOth.getStuAccTot();

                    spotExpenseOth.setPay(new BigDecimal(spotPay).subtract(new BigDecimal(studentPay)).floatValue());
                    spotExpenseOth.setBuy(new BigDecimal(spotBuy).subtract(new BigDecimal(studentBuy)).floatValue());
                    //学生没有欠款
                    if(studentPay >= studentBuy){
                        spotExpenseOth.setStuAccTot(new BigDecimal(stuAccTot).subtract(new BigDecimal(studentPay).add(new BigDecimal(studentBuy))).floatValue());
                    }else{
                        //学生有欠款
                        spotExpenseOth.setStuOwnTot(new BigDecimal(stuOwnTot).subtract(new BigDecimal(studentBuy).subtract(new BigDecimal(studentPay))).floatValue());
                    }
                    //算学习中心是否结清
                    if(0 < spotExpenseOth.getStuOwnTot()){
                        spotExpenseOth.setState(1);
                        spotExpenseOth.setClearTime(null);
                    }else{
                        spotExpenseOth.setState(0);
                        spotExpenseOth.setClearTime(DateTools.getLongNowTime());
                    }
                    spotExpenseOth.setOperator("管理员");
                    spotExpenseOth.setOperateTime(DateTools.getLongNowTime());
                    editSpotExpenseOthList.add(spotExpenseOth);
                }
                //查询新中心的费用信息
                SpotExpenseOth newSpotExpenseOth = findBySpotCodeAndSemesterDAO.findBySpotCodeAndSemester(newSpotCode, studentExpense.getSemesterId());
                if(null != newSpotExpenseOth){
                    float spotPay = newSpotExpenseOth.getPay();
                    float spotBuy = newSpotExpenseOth.getBuy();
                    float stuOwnTot = newSpotExpenseOth.getStuOwnTot();
                    float stuAccTot = newSpotExpenseOth.getStuAccTot();

                    newSpotExpenseOth.setPay(new BigDecimal(spotPay).add(new BigDecimal(studentPay)).floatValue());
                    newSpotExpenseOth.setBuy(new BigDecimal(spotBuy).add(new BigDecimal(studentBuy)).floatValue());
                    //学生没有欠款
                    if(studentPay >= studentBuy){
                        newSpotExpenseOth.setStuAccTot(new BigDecimal(stuAccTot).add(new BigDecimal(studentPay).add(new BigDecimal(studentBuy))).floatValue());
                    }else{
                        //学生有欠款
                        newSpotExpenseOth.setStuOwnTot(new BigDecimal(stuOwnTot).add(new BigDecimal(studentBuy).subtract(new BigDecimal(studentPay))).floatValue());
                    }
                    //算学习中心是否结清
                    if(0 < newSpotExpenseOth.getStuOwnTot()){
                        newSpotExpenseOth.setState(1);
                        newSpotExpenseOth.setClearTime(null);
                    }else{
                        newSpotExpenseOth.setState(0);
                        newSpotExpenseOth.setClearTime(DateTools.getLongNowTime());
                    }
                    newSpotExpenseOth.setOperator("管理员");
                    newSpotExpenseOth.setOperateTime(DateTools.getLongNowTime());
                    editSpotExpenseOthList.add(newSpotExpenseOth);
                }else{
                    newSpotExpenseOth = new SpotExpenseOth();
                    newSpotExpenseOth.setSemesterId(studentExpense.getSemesterId());
                    newSpotExpenseOth.setPay(studentPay);
                    newSpotExpenseOth.setBuy(studentBuy);
                    if(studentBuy - studentPay > 0){
                        newSpotExpenseOth.setStuOwnTot(new BigDecimal(studentBuy).subtract(new BigDecimal(studentPay)).floatValue());
                        newSpotExpenseOth.setStuAccTot(0);
                        newSpotExpenseOth.setState(1);
                        newSpotExpenseOth.setClearTime(null);
                    }else{
                        newSpotExpenseOth.setStuOwnTot(0);
                        newSpotExpenseOth.setStuAccTot(new BigDecimal(studentPay).subtract(new BigDecimal(studentBuy)).floatValue());
                        newSpotExpenseOth.setState(0);
                        newSpotExpenseOth.setClearTime(DateTools.getLongNowTime());
                    }
                    newSpotExpenseOth.setSpotCode(newSpotCode);
                    newSpotExpenseOth.setCreator("管理员");
                    newSpotExpenseOth.setOperator("管理员");
                    newSpotExpenseOth.setCreateTime(DateTools.getLongNowTime());
                    newSpotExpenseOth.setOperateTime(DateTools.getLongNowTime());
                    addSpotExpenseOthList.add(newSpotExpenseOth);
                }
            }
        }
    }

    /**
     * 同步选课，如果有新的课程，就要生成订单
     * @param studentCode
     * @param spotCode
     * @throws Exception
     */
    protected void syncSelectedCourse(String studentCode, String spotCode)throws Exception{
        //查询学生当前选课
        List<SelectedCourse> selectedCourseList = selectedCourseDAO.findByStudentCode(studentCode);
        //查询学生最新选课
        List<SelectedCourseTemp> selectedCourseTempList = selectedCourseTempDAO.findByStudentCode(studentCode);
        //查找最新的选课里面有没有新增的选课，如果有新增的选课，生成学生订单。
        List<SelectedCourseTemp> newSelectedCourseTempList = new ArrayList<SelectedCourseTemp>();
        if(null != selectedCourseTempList){
            for(SelectedCourseTemp selectedCourseTemp : selectedCourseTempList){
                if(null != selectedCourseList){
                    boolean isNewCourse = true;
                    for(SelectedCourse selectedCourse : selectedCourseList){
                        if(selectedCourseTemp.getCourseCode().equals(selectedCourse.getCourseCode())){
                            isNewCourse = false;
                            break;
                        }
                    }
                    if(isNewCourse){
                        newSelectedCourseTempList.add(selectedCourseTemp);
                    }
                }else{
                    newSelectedCourseTempList.addAll(selectedCourseTempList);
                }
            }
        }
        if(null != newSelectedCourseTempList && 0 < newSelectedCourseTempList.size()){
            Semester semester = findNowSemesterDAO.getNowSemester();
            //查询学生当前学期有没有未确认的订单， 如果有，就把新课程的教材加进去，如果没得就新生成一个订单
            List<StudentBookOrder> studentBookOrderList = studentBookOrderDAO.findByStudentCodeAndSemesterIdForUnconfirmed(studentCode, semester.getId());
            if(null != studentBookOrderList && 0 < studentBookOrderList.size()){
                StudentBookOrder studentBookOrder = studentBookOrderList.get(0);

                //添加订单教材明细
                for(SelectedCourseTemp selectedCourseTemp : newSelectedCourseTempList) {
                    String courseCode = selectedCourseTemp.getCourseCode();
                    //通过课程查询课程关联的教材
                    List<TeachMaterial> teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                    if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                        for (TeachMaterial teachMaterial : teachMaterialList) {
                            StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                            studentBookOrderTM.setOrderCode(studentBookOrder.getOrderCode());
                            studentBookOrderTM.setCourseCode(courseCode);
                            studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
                            studentBookOrderTM.setPrice(teachMaterial.getPrice());
                            studentBookOrderTM.setCount(1);
                            studentBookOrderTM.setOperator("管理员");
                            addStudentBookOrderTMList.add(studentBookOrderTM);
                        }
                    }
                    //把新选的课程添加进表
                    SelectedCourse selectedCourse = new SelectedCourse();
                    selectedCourse.setSemesterId(semester.getId());
                    selectedCourse.setStudentCode(studentCode);
                    selectedCourse.setCourseCode(selectedCourseTemp.getCourseCode());
                    selectedCourse.setOperateTime(DateTools.getLongNowTime());
                    detail += "学号："+studentCode+", 新增选课["+selectedCourseTemp.getCourseCode()+"]。\r\n";
                    addSelectCourseList.add(selectedCourse);
                }
            }else{
                //根据学生的学习中心查询关联的发行渠道
                IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(spotCode);
                Long issueChannelId = 0l;
                if(null == issueRange || !issueRange.getSpotCode().equals(spotCode)){
                    throw new BusinessException("没有找到该学号: "+studentCode+" 所属学习中心关联的渠道信息");
                }
                issueChannelId = issueRange.getIssueChannelId();
                //得到当前学期最大的订单号
                int num = 0;
                StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(semester.getId());
                if(null != maxCodeStudentBookOrder){
                    String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                    num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
                }
                //生成学生订单号
                String orderCode = OrderCodeTools.createStudentOrderCodeAuto(semester.getYear(), semester.getQuarter(), num + addStudentBookOrderList.size() + 1);
                //添加订单信息
                StudentBookOrder studentBookOrder = new StudentBookOrder();
                studentBookOrder.setSemesterId(semester.getId());
                studentBookOrder.setIssueChannelId(issueChannelId);
                studentBookOrder.setOrderCode(orderCode);
                studentBookOrder.setStudentCode(studentCode);
                studentBookOrder.setState(StudentBookOrder.STATE_UNCONFIRMED);
                studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
                studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
                studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_NOT);
                studentBookOrder.setCreator("管理员");
                studentBookOrder.setOperator("管理员");
                addStudentBookOrderList.add(studentBookOrder);

                //添加订单日志信息
                StudentBookOrderLog studentBookOrderLog = new StudentBookOrderLog();
                studentBookOrderLog.setOrderCode(orderCode);
                studentBookOrderLog.setState(StudentBookOrder.STATE_UNCONFIRMED);
                studentBookOrderLog.setOperator("管理员");
                addStudentBookOrderLogList.add(studentBookOrderLog);

                //添加订单教材明细
                for(SelectedCourseTemp selectedCourseTemp : newSelectedCourseTempList){
                    String courseCode = selectedCourseTemp.getCourseCode();
                    //通过课程查询课程关联的教材
                    List<TeachMaterial> teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                    if(null != teachMaterialList && 0 < teachMaterialList.size()) {
                        for(TeachMaterial teachMaterial : teachMaterialList) {
                            StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                            studentBookOrderTM.setOrderCode(orderCode);
                            studentBookOrderTM.setCourseCode(courseCode);
                            studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
                            studentBookOrderTM.setPrice(teachMaterial.getPrice());
                            studentBookOrderTM.setCount(1);
                            studentBookOrderTM.setOperator("管理员");
                            addStudentBookOrderTMList.add(studentBookOrderTM);
                        }
                    }
                    //把新选的课程添加进表
                    SelectedCourse selectedCourse = new SelectedCourse();
                    selectedCourse.setSemesterId(semester.getId());
                    selectedCourse.setStudentCode(studentCode);
                    selectedCourse.setCourseCode(selectedCourseTemp.getCourseCode());
                    selectedCourse.setOperateTime(DateTools.getLongNowTime());
                    detail += "学号："+studentCode+", 新增选课["+selectedCourseTemp.getCourseCode()+"]。\r\n";
                    addSelectCourseList.add(selectedCourse);
                }
            }
        }
    }

    /**
     * 通过课程查询课程关联的教材
     * @param courseCode
     * @return
     * @throws Exception
     */
    protected List<TeachMaterial> getTeachMaterialByCourseCode(String courseCode)throws Exception{
        List<TeachMaterial> teachMaterialList = new ArrayList<TeachMaterial>();
        List<TeachMaterial> teachMaterialList2 = findTeachMaterialByCourseCodeDAO.getTeachMaterialByCourseCode(courseCode);
        List<TeachMaterial> teachMaterialList3 = findTeachMaterialFromSetTMByCourseCodeDAO.getTeachMaterialFromSetTMByCourseCode(courseCode);
        if(null != teachMaterialList2 && 0 < teachMaterialList2.size()){
            teachMaterialList.addAll(teachMaterialList2);
        }
        if(null != teachMaterialList3 && 0 < teachMaterialList3.size()){
            teachMaterialList.addAll(teachMaterialList3);
        }
        return teachMaterialList;
    }

    protected Student getStudent(Object[] obj){
        Student student = new Student();
        student.setId(null != obj[0] ? Long.parseLong(obj[0].toString()) : null);
        student.setCode(null != obj[1] ? obj[1].toString() : null);
        student.setName(null != obj[2] ? obj[2].toString() : null);
        student.setSex(null != obj[3] ? Integer.parseInt(obj[3].toString()) : null);
        student.setIdcardType(null != obj[4] ? Integer.parseInt(obj[4].toString()) : null);
        student.setIdcardNo(null != obj[5] ? obj[5].toString() : null);
        student.setPostalCode(null != obj[6] ? obj[6].toString() : null);
        student.setAddress(null != obj[7] ? obj[7].toString() : null);
        student.setMobile(null != obj[8] ? obj[8].toString() : null);
        student.setHomeTel(null != obj[9] ? obj[9].toString() : null);
        student.setCompanyTel(null != obj[10] ? obj[10].toString() : null);
        student.setEmail(null != obj[11] ? obj[11].toString() : null);
        student.setSpotCode(null != obj[12] ? obj[12].toString() : null);
        student.setSpecCode(null != obj[13] ? obj[13].toString() : null);
        student.setLevelCode(null != obj[14] ? obj[14].toString() : null);
        student.setType(null != obj[15] ? Integer.parseInt(obj[15].toString()) : null);
        student.setState(null != obj[16] ? Integer.parseInt(obj[16].toString()) : null);
        student.setStudyEnterYear(null != obj[17] ? Integer.parseInt(obj[17].toString()) : null);
        student.setStudyQuarter(null != obj[18] ? Integer.parseInt(obj[18].toString()) : null);
        student.setOperateTime(null != obj[19] ? (Date)obj[19] : null);
        student.setChangeSpotDetail(null != obj[20] ? obj[20].toString() : null);
        return student;
    }

    protected StudentTemp getStudentTemp(Object[] obj, int flag){
        StudentTemp student = new StudentTemp();
        if(flag == 0) {
            student.setId(null != obj[21] ? Long.parseLong(obj[21].toString()) : null);
            student.setCode(null != obj[22] ? obj[22].toString() : null);
            student.setName(null != obj[23] ? obj[23].toString() : null);
            student.setSex(null != obj[24] ? Integer.parseInt(obj[24].toString()) : null);
            student.setIdcardType(null != obj[25] ? Integer.parseInt(obj[25].toString()) : null);
            student.setIdcardNo(null != obj[26] ? obj[26].toString() : null);
            student.setPostalCode(null != obj[27] ? obj[27].toString() : null);
            student.setAddress(null != obj[28] ? obj[28].toString() : null);
            student.setMobile(null != obj[29] ? obj[29].toString() : null);
            student.setHomeTel(null != obj[30] ? obj[30].toString() : null);
            student.setCompanyTel(null != obj[31] ? obj[31].toString() : null);
            student.setEmail(null != obj[32] ? obj[32].toString() : null);
            student.setSpotCode(null != obj[33] ? obj[33].toString() : null);
            student.setSpecCode(null != obj[34] ? obj[34].toString() : null);
            student.setLevelCode(null != obj[35] ? obj[35].toString() : null);
            student.setType(null != obj[36] ? Integer.parseInt(obj[36].toString()) : null);
            student.setState(null != obj[37] ? Integer.parseInt(obj[37].toString()) : null);
            student.setEnterYear(null != obj[38] ? Integer.parseInt(obj[38].toString()) : null);
            student.setQuarter(null != obj[39] ? Integer.parseInt(obj[39].toString()) : null);
            student.setStudyEnterYear(null != obj[40] ? Integer.parseInt(obj[40].toString()) : null);
            student.setStudyQuarter(null != obj[41] ? Integer.parseInt(obj[41].toString()) : null);
            student.setOperateTime(null != obj[42] ? (Date) obj[42] : null);
        }else {
            student.setId(null != obj[0] ? Long.parseLong(obj[0].toString()) : null);
            student.setCode(null != obj[1] ? obj[1].toString() : null);
            student.setName(null != obj[2] ? obj[2].toString() : null);
            student.setSex(null != obj[3] ? Integer.parseInt(obj[3].toString()) : null);
            student.setIdcardType(null != obj[4] ? Integer.parseInt(obj[4].toString()) : null);
            student.setIdcardNo(null != obj[5] ? obj[5].toString() : null);
            student.setPostalCode(null != obj[6] ? obj[6].toString() : null);
            student.setAddress(null != obj[7] ? obj[7].toString() : null);
            student.setMobile(null != obj[8] ? obj[8].toString() : null);
            student.setHomeTel(null != obj[9] ? obj[9].toString() : null);
            student.setCompanyTel(null != obj[10] ? obj[10].toString() : null);
            student.setEmail(null != obj[11] ? obj[11].toString() : null);
            student.setSpotCode(null != obj[12] ? obj[12].toString() : null);
            student.setSpecCode(null != obj[13] ? obj[13].toString() : null);
            student.setLevelCode(null != obj[14] ? obj[14].toString() : null);
            student.setType(null != obj[15] ? Integer.parseInt(obj[15].toString()) : null);
            student.setState(null != obj[16] ? Integer.parseInt(obj[16].toString()) : null);
            student.setEnterYear(null != obj[17] ? Integer.parseInt(obj[17].toString()) : null);
            student.setQuarter(null != obj[18] ? Integer.parseInt(obj[18].toString()) : null);
            student.setStudyEnterYear(null != obj[19] ? Integer.parseInt(obj[19].toString()) : null);
            student.setStudyQuarter(null != obj[20] ? Integer.parseInt(obj[20].toString()) : null);
            student.setOperateTime(null != obj[21] ? (Date) obj[21] : null);
        }
        return student;
    }


    protected void doDataOperate() throws Exception {
        if(null != addStudentList && 0 < addStudentList.size()){
            batchStudentDAO.batchAdd(addStudentList, 1000);
        }
        if(null != editStudentList && 0 < editStudentList.size()){
            batchStudentDAO.batchEdit(editStudentList, 1000);
        }

        if(null != addSelectCourseList && 0 < addSelectCourseList.size()){
            batchSelectedCourseDAO.batchAdd(addSelectCourseList, 1000);
        }
        if(null != addStudentBookOrderList && 0 < addStudentBookOrderList.size()){
            batchStudentBookOrderDAO.batchAdd(addStudentBookOrderList, 1000);
        }
        if(null != addStudentBookOrderTMList && 0 < addStudentBookOrderTMList.size()){
            batchStudentBookOrderTMDAO.batchAdd(addStudentBookOrderTMList, 1000);
        }
        if(null != addStudentBookOrderLogList && 0 < addStudentBookOrderLogList.size()){
            batchStudentBookOrderLogDAO.batchAdd(addStudentBookOrderLogList, 1000);
        }

        if(null != addSpotExpenseOthList && 0 < addSpotExpenseOthList.size()){
            batchSpotExpenseOthDAO.batchAdd(addSpotExpenseOthList, 1000);
        }
        if(null != editSpotExpenseOthList && 0 < editSpotExpenseOthList.size()){
            batchSpotExpenseOthDAO.batchEdit(editSpotExpenseOthList, 1000);
        }
    }
}
