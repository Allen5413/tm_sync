package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.onceordertm.EditStudentBookOnceOrderTMForIsSelectDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookorder.StudentBookOrderDAO;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.dao.sync.BatchSelectedCourseDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.dao.sync.SelectedCourseTempDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.SelectedCourseTemp;
import com.zs.domain.sync.Student;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.scheduler.SyncSelectedCourseTaskService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.OrderCodeTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Allen on 2015/10/26.
 */
@Service("syncSelectedCourseTaskService")
public class SyncSelectedCourseTaskServiceImpl implements SyncSelectedCourseTaskService {

    @Resource
    private SelectedCourseTempDAO selectedCourseTempDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private StudentBookOrderDAO studentBookOrderDAO;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private FindStudentBookOrderForMaxCodeDAO findStudentBookOrderForMaxCodeDAO;
    @Resource
    private BatchSelectedCourseDAO batchSelectedCourseDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private BatchStudentBookOrderLogDAO batchStudentBookOrderLogDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;
    @Resource
    private EditStudentBookOnceOrderTMForIsSelectDAO editStudentBookOnceOrderTMForIsSelectDAO;

    //变更信息描述
    private String detail = "";



    @Override
    @Transactional
    public void syncSelectedCourse() {
        List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
        List<SelectedCourse> addSelectCourseList = new ArrayList<SelectedCourse>();
        List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
        List<StudentBookOrderLog> addStudentBookOrderLogList = new ArrayList<StudentBookOrderLog>();

        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学生选课信息同步\r\n");
        String studentCode = "", courseCode = "";

        Map<String, List<TeachMaterial>> courseTMMap = new HashMap<String, List<TeachMaterial>>();

        int tempNum = 0;
        try {
            //学生新增的选课
            List<SelectedCourseTemp> selectedCourseTempList = selectedCourseTempDAO.findNewSelectedCourse();
            List<SelectedCourseTemp> copySelectedCourseTempList = new ArrayList<SelectedCourseTemp>();
            System.out.println("selectedCourseTempList:   "+selectedCourseTempList.size());
            if (null != selectedCourseTempList && 0 < selectedCourseTempList.size()) {

                //去掉课程没有关联教材的数据
                int j=0;
                for (SelectedCourseTemp selectedCourseTemp : selectedCourseTempList){
                    System.out.println("j:   "+j);
                    courseCode = selectedCourseTemp.getCourseCode();
                    List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                    if(null == teachMaterialList){
                        teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                        courseTMMap.put(courseCode, null == teachMaterialList ? new ArrayList<TeachMaterial>(0) : teachMaterialList);
                    }
                    if(null != teachMaterialList && 0 < teachMaterialList.size()){
                        copySelectedCourseTempList.add(selectedCourseTemp);
                    }
                    j++;
                }


                Semester semester = findNowSemesterDAO.getNowSemester();
                String beforeStudentCode = "";
                Map<String, Integer> isOrderOnceMap = new HashMap<String, Integer>();
                int i=0;
                Student student = null;
                //生成新选课的订单数据
                for (SelectedCourseTemp selectedCourseTemp : copySelectedCourseTempList){
                    System.out.println("i: "+i);
                    i++;
                    studentCode = selectedCourseTemp.getStudentCode();
                    courseCode = selectedCourseTemp.getCourseCode();
                    //是否同步一次性订单，是的话这里就不在创建订单了，只同步选课
                    boolean isOnceOrder = false;
                    if(null == isOrderOnceMap.get(studentCode)){
                        //查询学生信息
                        student = findStudentByCodeDAO.getStudentByCode(studentCode);
                        isOrderOnceMap.put(studentCode, student.getIsOnceOrder());
                        if(student.getIsOnceOrder() != Student.IS_ONCE_ORDER_NOT){
                            isOnceOrder = true;
                        }
                    }else{
                        if(isOrderOnceMap.get(studentCode) != Student.IS_ONCE_ORDER_NOT){
                            isOnceOrder = true;
                        }
                    }

                    if(!isOnceOrder) {
                        if (!beforeStudentCode.equals(studentCode)) {
                            //查询学生当前学期有没有未确认的订单， 如果有，就把新课程的教材加进去，如果没得就新生成一个订单
                            List<StudentBookOrder> studentBookOrderList = studentBookOrderDAO.findByStudentCodeAndSemesterIdForUnconfirmed(studentCode, semester.getId());
                            if (null != studentBookOrderList && 0 < studentBookOrderList.size()) {
                                StudentBookOrder studentBookOrder = studentBookOrderList.get(0);
                                //通过课程查询课程关联的教材
                                List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                                if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                                    teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                                    courseTMMap.put(courseCode, teachMaterialList);
                                }
                                if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                                    for (TeachMaterial teachMaterial : teachMaterialList) {
                                        StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                                        studentBookOrderTM.setOrderCode(studentBookOrder.getOrderCode());
                                        studentBookOrderTM.setCourseCode(courseCode);
                                        studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
                                        studentBookOrderTM.setPrice(teachMaterial.getPrice());
                                        studentBookOrderTM.setIsSend(StudentBookOrderTM.IS_SEND_NOT);
                                        studentBookOrderTM.setCount(1);
                                        studentBookOrderTM.setOperator("管理员");
                                        addStudentBookOrderTMList.add(studentBookOrderTM);
                                    }
                                }
                            } else {
                                if(null == student){
                                    //查询学生信息
                                    student = findStudentByCodeDAO.getStudentByCode(studentCode);
                                }
                                //根据学生的学习中心查询关联的发行渠道
                                IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(student.getSpotCode());
                                Long issueChannelId = 0l;
                                if (null == issueRange || !issueRange.getSpotCode().equals(student.getSpotCode())) {
                                    throw new BusinessException("没有找到该学号: " + studentCode + " 所属学习中心关联的渠道信息");
                                }
                                issueChannelId = issueRange.getIssueChannelId();
                                //得到当前学期最大的订单号
                                int num = 0;
                                StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(semester.getId());
                                if (null != maxCodeStudentBookOrder) {
                                    String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                                    num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length()));
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

                                //通过课程查询课程关联的教材
                                List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                                if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                                    teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                                    courseTMMap.put(courseCode, teachMaterialList);
                                }
                                if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                                    for (TeachMaterial teachMaterial : teachMaterialList) {
                                        StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                                        studentBookOrderTM.setOrderCode(orderCode);
                                        studentBookOrderTM.setCourseCode(courseCode);
                                        studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
                                        studentBookOrderTM.setPrice(teachMaterial.getPrice());
                                        studentBookOrderTM.setCount(1);
                                        studentBookOrderTM.setIsSend(StudentBookOrderTM.IS_SEND_NOT);
                                        studentBookOrderTM.setOperator("管理员");
                                        addStudentBookOrderTMList.add(studentBookOrderTM);
                                    }
                                }
                            }
                        } else {
                            StudentBookOrderTM beforeStudentBookOrderTM = addStudentBookOrderTMList.get(addStudentBookOrderTMList.size() - 1);
                            String orderCode = beforeStudentBookOrderTM.getOrderCode();

                            //通过课程查询课程关联的教材
                            List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                            if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                                teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                                courseTMMap.put(courseCode, teachMaterialList);
                            }
                            if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                                for (TeachMaterial teachMaterial : teachMaterialList) {
                                    StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                                    studentBookOrderTM.setOrderCode(orderCode);
                                    studentBookOrderTM.setCourseCode(courseCode);
                                    studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
                                    studentBookOrderTM.setPrice(teachMaterial.getPrice());
                                    studentBookOrderTM.setCount(1);
                                    studentBookOrderTM.setIsSend(StudentBookOrderTM.IS_SEND_NOT);
                                    studentBookOrderTM.setOperator("管理员");
                                    addStudentBookOrderTMList.add(studentBookOrderTM);
                                }
                            }
                        }
                    }
                    beforeStudentCode = selectedCourseTemp.getStudentCode();
                    tempNum++;
                }

                //添加新的选课信息进选课表
                int k=0;
                for (SelectedCourseTemp selectedCourseTemp : selectedCourseTempList){
                    System.out.println("k:   "+k);

                    boolean isOnceOrder = false;
                    if(null == isOrderOnceMap.get(selectedCourseTemp.getStudentCode())){
                        //查询学生信息
                        student = findStudentByCodeDAO.getStudentByCode(studentCode);
                        isOrderOnceMap.put(studentCode, student.getIsOnceOrder());
                        if(student.getIsOnceOrder() != Student.IS_ONCE_ORDER_NOT){
                            isOnceOrder = true;
                        }
                    }else{
                        if(isOrderOnceMap.get(studentCode) != Student.IS_ONCE_ORDER_NOT){
                            isOnceOrder = true;
                        }
                    }

                    //如果是生成的一次性订单，需要把选课数据修改到一次性订单的明细里面
                    if(isOnceOrder){
                        editStudentBookOnceOrderTMForIsSelectDAO.editor(selectedCourseTemp.getStudentCode(), selectedCourseTemp.getCourseCode());
                    }

                    //把新选的课程添加进表
                    SelectedCourse selectedCourse = new SelectedCourse();
                    selectedCourse.setSemesterId(semester.getId());
                    selectedCourse.setStudentCode(selectedCourseTemp.getStudentCode());
                    selectedCourse.setCourseCode(selectedCourseTemp.getCourseCode());
                    selectedCourse.setOperateTime(DateTools.getLongNowTime());
                    detail += "学号："+selectedCourseTemp.getStudentCode()+", 新增选课["+selectedCourseTemp.getCourseCode()+"]。\r\n";
                    addSelectCourseList.add(selectedCourse);
                    k++;
                }

                //批量提交数据
                if(null != addSelectCourseList && 0 < addSelectCourseList.size()){
                    batchSelectedCourseDAO.batchAdd(addSelectCourseList, 1000);
                }
                if(null != addStudentBookOrderList && 0 < addStudentBookOrderList.size()){
                    batchStudentBookOrderDAO.batchAdd(addStudentBookOrderList, 1000);
                }
                if(null != addStudentBookOrderLogList && 0 < addStudentBookOrderLogList.size()){
                    batchStudentBookOrderLogDAO.batchAdd(addStudentBookOrderLogList, 1000);
                }
                if(null != addStudentBookOrderTMList && 0 < addStudentBookOrderTMList.size()){
                    batchStudentBookOrderTMDAO.batchAdd(addStudentBookOrderTMList, 1000);
                }
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学号：" + studentCode + ", 课程编号：" + courseCode + " \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
        }finally {
            msg.append("\r\n"+detail+"\r\n");
            msg.append("执行了"+tempNum+"条数据\r\n");
            msg.append(DateTools.getLongNowTime()+": 学生选课信息同步结束");

            PropertiesTools propertiesTools =  new PropertiesTools("resource/commons.properties");
            String rootPath = propertiesTools.getProperty("sync.log.file.path");
            String filePath = propertiesTools.getProperty("sync.selectedcourse.log.file.path");
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
}
