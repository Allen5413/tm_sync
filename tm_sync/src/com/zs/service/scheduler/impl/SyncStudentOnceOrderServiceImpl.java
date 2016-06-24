package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.onceorder.BatchStudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorder.FindStudentBookOnceOrderByStudentCodeForUnConfirmDAO;
import com.zs.dao.sale.onceorder.FindStudentBookOnceOrderForMaxCodeDAO;
import com.zs.dao.sale.onceorder.StudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorderlog.BatchStudentBookOnceOrderLogDAO;
import com.zs.dao.sale.onceordertm.BatchStudentBookOnceOrderTMDAO;
import com.zs.dao.sale.onceordertm.DelStudentBookOnceOrderTMByCodeDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForUnconfirmedByStudentCodeDAO;
import com.zs.dao.sale.studentbookorderlog.FindStudentBookOrderLogByCodeDAO;
import com.zs.dao.sale.studentbookorderlog.StudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.dao.sale.studentbookordertm.StudentBookOrderTmDAO;
import com.zs.dao.sync.FindStudentForOnceOrderDAO;
import com.zs.dao.sync.FindTeachScheduleByYearAndQuarterAndSpecAndLevelDAO;
import com.zs.dao.sync.SelectedCourseDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.sale.*;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.Student;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.scheduler.SyncStudentOnceOrderService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.OrderCodeTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * 生成学生一次性订单
 * Created by Allen on 2016/6/16.
 */
@Service("syncStudentOnceOrderService")
public class SyncStudentOnceOrderServiceImpl extends EntityServiceImpl<StudentBookOnceOrder, StudentBookOnceOrderDAO> implements SyncStudentOnceOrderService{

    @Resource
    private FindStudentForOnceOrderDAO findStudentForOnceOrderDAO;
    @Resource
    private FindTeachScheduleByYearAndQuarterAndSpecAndLevelDAO findTeachScheduleByYearAndQuarterAndSpecAndLevelDAO;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindStudentBookOnceOrderForMaxCodeDAO findStudentBookOnceOrderForMaxCodeDAO;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private BatchStudentBookOnceOrderDAO batchStudentBookOnceOrderDAO;
    @Resource
    private BatchStudentBookOnceOrderLogDAO batchStudentBookOnceOrderLogDAO;
    @Resource
    private BatchStudentBookOnceOrderTMDAO batchStudentBookOnceOrderTMDAO;
    @Resource
    private FindStudentBookOrderForUnconfirmedByStudentCodeDAO findStudentBookOrderForUnconfirmedByStudentCodeDAO;
    @Resource
    private FindStudentBookOrderLogByCodeDAO findStudentBookOrderLogByCodeDAO;
    @Resource
    private StudentBookOrderTmDAO studentBookOrderTmDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private StudentBookOrderLogDAO studentBookOrderLogDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;
    @Resource
    private FindStudentBookOnceOrderByStudentCodeForUnConfirmDAO findStudentBookOnceOrderByStudentCodeForUnConfirmDAO;
    @Resource
    private DelStudentBookOnceOrderTMByCodeDAO delStudentBookOnceOrderTMByCodeDAO;
    @Resource
    private SelectedCourseDAO selectedCourseDAO;

    @Override
    @Transactional
    public void sync() throws Exception {
        Map<String, List<TeachMaterial>> courseTMMap = new HashMap<String, List<TeachMaterial>>();

        List<StudentBookOnceOrder> addOrderList = new ArrayList<StudentBookOnceOrder>();
        List<StudentBookOnceOrderLog> addOrderLogList = new ArrayList<StudentBookOnceOrderLog>();
        List<StudentBookOnceOrderTM> addOrderTMList = new ArrayList<StudentBookOnceOrderTM>();

        List<StudentBookOrder> delOrderList = new ArrayList<StudentBookOrder>();
        List<StudentBookOrderTM> delOrderTMList = new ArrayList<StudentBookOrderTM>();
        List<StudentBookOrderLog> delOrderLogList = new ArrayList<StudentBookOrderLog>();

        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学生一次性订单同步操作\r\n");
        String studentCode = "", courseCode = "";

        //变更信息描述
        String detail = "";

        try {
            //获取当前学期
            Semester semester = findNowSemesterDAO.getNowSemester();

            //查询可以生成一次性订单的学生
            List<Student> studentList = findStudentForOnceOrderDAO.find();
            if (null != studentList && 0 < studentList.size()) {
                for (Student student : studentList) {
                    studentCode = student.getCode();
                    //查询该学生有没有未确认的一次性订单
                    StudentBookOnceOrder unconfirmOrder = findStudentBookOnceOrderByStudentCodeForUnConfirmDAO.find(studentCode);

                    //查询该学生到毕业时的所需课程
                    List<Object[]> courseList = findTeachScheduleByYearAndQuarterAndSpecAndLevelDAO.find(student.getStudyEnterYear(), 0 == student.getStudyQuarter() ? 1 : 2, student.getSpecCode(), student.getLevelCode(), student.getCode());
                    if (null != courseList && 0 < courseList.size()) {
                        //学生订单号
                        String orderCode = "";
                        //如果没有，新建订单
                        if(null == unconfirmOrder){
                            //根据学生的学习中心查询关联的发行渠道
                            IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(student.getSpotCode());
                            Long issueChannelId = 0l;
                            if (null == issueRange || !issueRange.getSpotCode().equals(student.getSpotCode())) {
                                throw new BusinessException("没有找到该学号: " + studentCode + " 所属学习中心关联的渠道信息");
                            }
                            issueChannelId = issueRange.getIssueChannelId();
                            //得到当前学期最大的订单号
                            int num = 0;
                            StudentBookOnceOrder maxCodeStudentBookOnceOrder = findStudentBookOnceOrderForMaxCodeDAO.find(semester.getId());
                            if (null != maxCodeStudentBookOnceOrder) {
                                String maxOrderCode = maxCodeStudentBookOnceOrder.getOrderCode();
                                num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length()));
                            }
                            //生成学生订单号
                            orderCode = OrderCodeTools.createStudentOnceOrderCode(semester.getYear(), semester.getQuarter(), num + addOrderList.size() + 1);

                            //添加订单信息
                            StudentBookOnceOrder order = new StudentBookOnceOrder();
                            order.setSemesterId(semester.getId());
                            order.setIssueChannelId(issueChannelId);
                            order.setOrderCode(orderCode);
                            order.setStudentCode(studentCode);
                            order.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                            order.setStudentSign(StudentBookOnceOrder.STUDENTSIGN_NOT);
                            order.setCreator("管理员");
                            order.setOperator("管理员");
                            addOrderList.add(order);

                            //添加订单日志信息
                            StudentBookOnceOrderLog studentBookOnceOrderLog = new StudentBookOnceOrderLog();
                            studentBookOnceOrderLog.setOrderCode(orderCode);
                            studentBookOnceOrderLog.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                            studentBookOnceOrderLog.setOperator("管理员");
                            addOrderLogList.add(studentBookOnceOrderLog);
                        }else{
                            //如果有，删除订单明细，重新添加订单明细
                            orderCode = unconfirmOrder.getOrderCode();
                            delStudentBookOnceOrderTMByCodeDAO.del(orderCode);
                        }

                        //查询该学生的已有选课
                        List<SelectedCourse> selectedCourseList = selectedCourseDAO.findByStudentCode(studentCode);

                        //添加订单教材明细
                        for (Object[] objs : courseList) {
                            courseCode = objs[0].toString();
                            int courseType = Integer.parseInt(objs[1].toString());
                            boolean isBuyCourse = null == objs[2] ? false : true;
                            int xf = Integer.parseInt(objs[3].toString());

                            List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                            if (null == teachMaterialList) {
                                teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                                courseTMMap.put(courseCode, null == teachMaterialList ? new ArrayList<TeachMaterial>(0) : teachMaterialList);
                            }
                            if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                                for (TeachMaterial teachMaterial : teachMaterialList) {
                                    StudentBookOnceOrderTM orderTM = new StudentBookOnceOrderTM();
                                    orderTM.setOrderCode(orderCode);
                                    orderTM.setCourseCode(courseCode);
                                    orderTM.setTeachMaterialId(teachMaterial.getId());
                                    orderTM.setPrice(teachMaterial.getPrice());
                                    orderTM.setIsSend(StudentBookOnceOrderTM.IS_SEND_NOT);
                                    orderTM.setIsMust(0 == courseType || 2 == courseType ? StudentBookOnceOrderTM.IS_MUST_YES : StudentBookOnceOrderTM.IS_MUST_NOT);
                                    orderTM.setIsBuy(isBuyCourse ? StudentBookOnceOrderTM.IS_BUY_YES : StudentBookOnceOrderTM.IS_BUY_NOT);
                                    orderTM.setCount(isBuyCourse ? 0 : 1);
                                    orderTM.setXf(xf);
                                    orderTM.setIsSelect(this.isSelectedCourse(selectedCourseList, courseCode));
                                    orderTM.setOperator("管理员");
                                    addOrderTMList.add(orderTM);

                                    detail += "学号："+studentCode+", 新增订单课程["+courseCode+"]。\r\n";
                                }
                            }
                        }

                        //删除该学生在学生订单中，还没有确认的订单
                        List<StudentBookOrder> studentBookOrderList = findStudentBookOrderForUnconfirmedByStudentCodeDAO.find(studentCode);
                        if(null != studentBookOrderList && 0 < studentBookOrderList.size()){
                            for(StudentBookOrder studentBookOrder : studentBookOrderList){
                                //查询订单日志
                                List<StudentBookOrderLog> studentBookOrderLogList = findStudentBookOrderLogByCodeDAO.find(studentBookOrder.getOrderCode());
                                //查询订单明细
                                List<StudentBookOrderTM> studentBookOrderTMList = studentBookOrderTmDAO.findStudentBookOrderTMByOrderCode(studentBookOrder.getOrderCode());


                                if(null != studentBookOrderLogList){
                                    for(StudentBookOrderLog studentBookOrderLog : studentBookOrderLogList){
                                        delOrderLogList.add(studentBookOrderLog);
                                        studentBookOrderLogDAO.remove(studentBookOrderLog);
                                    }
                                }
                                if(null != studentBookOrderTMList){
                                    for(StudentBookOrderTM studentBookOrderTM : studentBookOrderTMList){
                                        delOrderTMList.add(studentBookOrderTM);
                                    }
                                }

                                delOrderList.add(studentBookOrder);
                            }
                        }

                    }
                }
            }

            //批量提交数据
            if (null != addOrderList && 0 < addOrderList.size()) {
                batchStudentBookOnceOrderDAO.batchAdd(addOrderList, 1000);
            }
            if (null != addOrderLogList && 0 < addOrderLogList.size()) {
                batchStudentBookOnceOrderLogDAO.batchAdd(addOrderLogList, 1000);
            }
            if (null != addOrderTMList && 0 < addOrderTMList.size()) {
                batchStudentBookOnceOrderTMDAO.batchAdd(addOrderTMList, 1000);
            }

            //批量删除
            if(null != delOrderList && 0 < delOrderList.size()){
                batchStudentBookOrderDAO.batchDelete(delOrderList);
            }
            if(null != delOrderTMList && 0 < delOrderTMList.size()){
                batchStudentBookOrderTMDAO.batchDelete(delOrderTMList);
            }


        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学号：" + studentCode + ", 课程编号：" + courseCode + " \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
        }finally {
            msg.append("\r\n"+detail+"\r\n");
            msg.append(DateTools.getLongNowTime()+": 学生一次性订单信息同步结束");

            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            String filePath = propertiesTools.getProperty("sync.onceorder.log.file.path");
            String nowDate = DateTools.transferLongToDate("yyyy-MM-dd", System.currentTimeMillis());
            FileTools.createFile(rootPath + filePath, nowDate + ".txt");
            FileTools.writeTxtFile(msg.toString(), rootPath + filePath + nowDate + ".txt");
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

    protected int isSelectedCourse(List<SelectedCourse> selectedCourseList, String courseCode){
        if(null != selectedCourseList && 0 < selectedCourseList.size()){
            for(SelectedCourse selectedCourse : selectedCourseList){
                if(courseCode.equals(selectedCourse.getCourseCode())){
                    return StudentBookOnceOrderTM.IS_SELECT_YES;
                }
            }
        }
        return StudentBookOnceOrderTM.IS_SELECT_NOT;
    }
}
