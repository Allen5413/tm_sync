package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.FindListByWhereDAO;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.onceorder.BatchStudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorder.FindStudentBookOnceOrderForMaxIdDAO;
import com.zs.dao.sale.onceorder.StudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorder.impl.FindOnceOrderByWhereDAOImpl;
import com.zs.dao.sale.onceorderlog.BatchStudentBookOnceOrderLogDAO;
import com.zs.dao.sale.onceorderlog.OnceOrderLogDAO;
import com.zs.dao.sale.onceordertm.BatchStudentBookOnceOrderTMDAO;
import com.zs.dao.sale.onceordertm.DelStudentBookOnceOrderTMByOrderIdDAO;
import com.zs.dao.sync.*;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.sale.*;
import com.zs.domain.sync.OldSelectedCourseTemp2;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.Student;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.scheduler.SyncStudentOnceOrderService;
import com.zs.tools.DateTools;
import com.zs.tools.FileTools;
import com.zs.tools.PropertiesTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成学生一次性订单
 * Created by Allen on 2016/6/16.
 */
@Service("syncStudentOnceOrderService")
public class SyncStudentOnceOrderServiceImpl extends EntityServiceImpl<StudentBookOnceOrder, StudentBookOnceOrderDAO> implements SyncStudentOnceOrderService {

    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private BatchStudentBookOnceOrderDAO batchStudentBookOnceOrderDAO;
    @Resource
    private BatchStudentBookOnceOrderTMDAO batchStudentBookOnceOrderTMDAO;
    @Resource
    private FindStudentBookOnceOrderForMaxIdDAO findStudentBookOnceOrderForMaxIdDAO;
    @Resource
    private OldSelectedCourseTempDAO oldSelectedCourseTempDAO;
    @Resource
    private BatchSelectedCourseDAO batchSelectedCourseDAO;
    @Resource
    private BatchStudentBookOnceOrderLogDAO batchStudentBookOnceOrderLogDAO;
    @Resource
    private FindListByWhereDAO findOnceOrderByWhereDAO;
    @Resource
    private DelStudentBookOnceOrderTMByOrderIdDAO delStudentBookOnceOrderTMByOrderIdDAO;
    @Resource
    private OnceOrderLogDAO onceOrderLogDAO;
    @Resource
    private OldSelectedCourseTemp2DAO oldSelectedCourseTemp2DAO;

    @Override
    @Transactional
    public void sync() throws Exception {
        //变更信息描述
        String detail = null;
        List<StudentBookOnceOrderTM> addOrderTMList = new ArrayList<StudentBookOnceOrderTM>();
        List<SelectedCourse> addSelectCourseList = new ArrayList<SelectedCourse>();
        List<StudentBookOnceOrder> addOrderList = new ArrayList<StudentBookOnceOrder>();
        List<StudentBookOnceOrderLog> addOrderLogList = new ArrayList<StudentBookOnceOrderLog>();

        StringBuilder msg = new StringBuilder(DateTools.getLongNowTime()+": 开始执行学生选剩余课信息同步\r\n");
        String studentCode = "", spotCode = "", courseCode = "";

        Map<String, List<TeachMaterial>> courseTMMap = new HashMap<String, List<TeachMaterial>>();

        long num = 1;
        try {
            //查询学生剩余选课
            List<Object[]> list = oldSelectedCourseTempDAO.find();
            List<Object[]> copyList = new ArrayList<Object[]>();
            System.out.println("list:   "+list.size());
            if(null != list && 0 < list.size()) {
                //去掉课程没有关联教材的数据
                int j=0;
                for (Object[] objs : list){
                    System.out.println("j:   "+j);
                    courseCode = objs[2].toString();
                    List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                    if(null == teachMaterialList){
                        teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                        courseTMMap.put(courseCode, null == teachMaterialList ? new ArrayList<TeachMaterial>(0) : teachMaterialList);
                    }
                    if(null != teachMaterialList && 0 < teachMaterialList.size()){
                        copyList.add(objs);
                    }
                    j++;
                }

                Semester semester = findNowSemesterDAO.getNowSemester();
                String beforeStudentCode = "";

                int i=0;
                long orderId = 0l;
                //生成新选课的订单数据
                for (Object[] objs : list){
                    System.out.println("i: "+i+"   "+orderId);
                    i++;
                    studentCode = objs[0].toString();
                    spotCode = objs[1].toString();
                    courseCode = objs[2].toString();
                    int isSendStudent = Integer.parseInt(objs[3].toString());

                    if (!beforeStudentCode.equals(studentCode)) {
                        //根据学生的学习中心查询关联的发行渠道
                        IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(spotCode);
                        Long issueChannelId = 0l;
                        if (null == issueRange || !issueRange.getSpotCode().equals(spotCode)) {
                            throw new BusinessException("没有找到该学号: " + studentCode + " 所属学习中心关联的渠道信息");
                        }
                        issueChannelId = issueRange.getIssueChannelId();

                        long maxId = 0;
                        StudentBookOnceOrder maxIdOrder = findStudentBookOnceOrderForMaxIdDAO.find();
                        if(null != maxIdOrder){
                            maxId = maxIdOrder.getId();
                        }

                        //添加订单信息
                        orderId = maxId+num;
                        StudentBookOnceOrder studentBookOnceOrder = new StudentBookOnceOrder();
                        studentBookOnceOrder.setId(orderId);
                        studentBookOnceOrder.setIssueChannelId(issueChannelId);
                        studentBookOnceOrder.setStudentCode(studentCode);
                        studentBookOnceOrder.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                        studentBookOnceOrder.setStudentSign(StudentBookOnceOrder.STUDENTSIGN_NOT);
                        if(Student.IS_SEND_STUDENT_NOT == isSendStudent){
                            studentBookOnceOrder.setIsSendStudent(StudentBookOnceOrder.IS_SEND_STUDENT_NOT);
                        }
                        if(Student.IS_SEND_STUDENT_YES == isSendStudent){
                            studentBookOnceOrder.setIsSendStudent(StudentBookOnceOrder.IS_SEND_STUDENT_YES);
                        }
                        studentBookOnceOrder.setCreator("管理员");
                        studentBookOnceOrder.setOperator("管理员");
                        addOrderList.add(studentBookOnceOrder);


                        //添加订单日志信息
                        StudentBookOnceOrderLog studentBookOnceOrderLog = new StudentBookOnceOrderLog();
                        studentBookOnceOrderLog.setOrderId(studentBookOnceOrder.getId());
                        studentBookOnceOrderLog.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                        studentBookOnceOrderLog.setOperator("管理员");
                        addOrderLogList.add(studentBookOnceOrderLog);

                        //通过课程查询课程关联的教材
                        List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                        if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                            teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                            courseTMMap.put(courseCode, teachMaterialList);
                        }
                        if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                            for (TeachMaterial teachMaterial : teachMaterialList) {
                                StudentBookOnceOrderTM studentBookOnceOrderTM = new StudentBookOnceOrderTM();
                                studentBookOnceOrderTM.setOrderId(studentBookOnceOrder.getId());
                                studentBookOnceOrderTM.setCourseCode(courseCode);
                                studentBookOnceOrderTM.setTeachMaterialId(teachMaterial.getId());
                                studentBookOnceOrderTM.setPrice(teachMaterial.getPrice());
                                studentBookOnceOrderTM.setCount(1);
                                studentBookOnceOrderTM.setIsSend(StudentBookOnceOrderTM.IS_SEND_NOT);
                                studentBookOnceOrderTM.setOperator("管理员");
                                addOrderTMList.add(studentBookOnceOrderTM);
                            }
                        }

                        num++;
                    } else {
                        //通过课程查询课程关联的教材
                        List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                        if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                            teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                            courseTMMap.put(courseCode, teachMaterialList);
                        }
                        if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                            for (TeachMaterial teachMaterial : teachMaterialList) {
                                StudentBookOnceOrderTM studentBookOnceOrderTM = new StudentBookOnceOrderTM();
                                studentBookOnceOrderTM.setOrderId(orderId);
                                studentBookOnceOrderTM.setCourseCode(courseCode);
                                studentBookOnceOrderTM.setTeachMaterialId(teachMaterial.getId());
                                studentBookOnceOrderTM.setPrice(teachMaterial.getPrice());
                                studentBookOnceOrderTM.setCount(1);
                                studentBookOnceOrderTM.setIsSend(StudentBookOnceOrderTM.IS_SEND_NOT);
                                studentBookOnceOrderTM.setOperator("管理员");
                                addOrderTMList.add(studentBookOnceOrderTM);
                            }
                        }
                    }
                    beforeStudentCode = studentCode;
                }

                //添加新的选课信息进选课表
                int k=0;
                for (Object[] objs : list){
                    System.out.println("k:   "+k);

                    studentCode = objs[0].toString();
                    courseCode = objs[2].toString();

                    //把新选的课程添加进表
                    SelectedCourse selectedCourse = new SelectedCourse();
                    selectedCourse.setSemesterId(semester.getId());
                    selectedCourse.setStudentCode(studentCode);
                    selectedCourse.setCourseCode(courseCode);
                    selectedCourse.setOperateTime(DateTools.getLongNowTime());
                    detail += "学号："+studentCode+", 新增选课["+courseCode+"]。\r\n";
                    addSelectCourseList.add(selectedCourse);
                    k++;
                }

                //批量提交数据
                if(null != addSelectCourseList && 0 < addSelectCourseList.size()){
                    batchSelectedCourseDAO.batchAdd(addSelectCourseList, 1000);
                }
                if(null != addOrderList && 0 < addOrderList.size()){
                    batchStudentBookOnceOrderDAO.batchAdd(addOrderList, 1000);
                }
                if(null != addOrderLogList && 0 < addOrderLogList.size()){
                    batchStudentBookOnceOrderLogDAO.batchAdd(addOrderLogList, 1000);
                }
                if(null != addOrderTMList && 0 < addOrderTMList.size()){
                    batchStudentBookOnceOrderTMDAO.batchAdd(addOrderTMList, 1000);
                }

            }
        }catch (Exception e){
            throw e;
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
     * 临时调整选课有变化的学生
     * @param map
     * @throws Exception
     */
    @Override
    @Transactional
    public void syncTempAdjust(Map<String, String> map) throws Exception {
        Map<String, List<TeachMaterial>> courseTMMap = new HashMap<String, List<TeachMaterial>>();
        List<Object[]> list = findOnceOrderByWhereDAO.findListByWhere(map, null);
        if(null != list && 0 < list.size()){
            for(Object[] objs : list){
                long orderId = Long.parseLong(objs[0].toString());
                String studentCode = objs[1].toString();
                int state = Integer.parseInt(objs[2].toString());
                //删除掉现有订单明细
                delStudentBookOnceOrderTMByOrderIdDAO.del(orderId);
                if(state == StudentBookOnceOrder.STATE_CONFIRMED){
                    //如果订单已经确认了，修改成未确认
                    StudentBookOnceOrder studentBookOnceOrder = super.get(orderId);
                    studentBookOnceOrder.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                    super.update(studentBookOnceOrder);
                    //记录状态变更
                    StudentBookOnceOrderLog studentBookOnceOrderLog = new StudentBookOnceOrderLog();
                    studentBookOnceOrderLog.setOrderId(orderId);
                    studentBookOnceOrderLog.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                    studentBookOnceOrderLog.setOperator("管理员");
                    onceOrderLogDAO.save(studentBookOnceOrderLog);
                }
                //查询该学生新的选课，然后重新生成订单明细
                List<OldSelectedCourseTemp2> oldSelectedCourseTemp2List = oldSelectedCourseTemp2DAO.find(studentCode);
                if(null != oldSelectedCourseTemp2List && 0 < oldSelectedCourseTemp2List.size()){
                    for(OldSelectedCourseTemp2 oldSelectedCourseTemp2 : oldSelectedCourseTemp2List){
                        String courseCode = oldSelectedCourseTemp2.getCourseCode();
                        //通过课程查询课程关联的教材
                        List<TeachMaterial> teachMaterialList = courseTMMap.get(courseCode);
                        if (null == teachMaterialList || 1 > teachMaterialList.size()) {
                            teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                            courseTMMap.put(courseCode, teachMaterialList);
                        }
                        if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                            for (TeachMaterial teachMaterial : teachMaterialList) {
                                StudentBookOnceOrderTM studentBookOnceOrderTM = new StudentBookOnceOrderTM();
                                studentBookOnceOrderTM.setOrderId(orderId);
                                studentBookOnceOrderTM.setCourseCode(courseCode);
                                studentBookOnceOrderTM.setTeachMaterialId(teachMaterial.getId());
                                studentBookOnceOrderTM.setPrice(teachMaterial.getPrice());
                                studentBookOnceOrderTM.setCount(1);
                                studentBookOnceOrderTM.setIsSend(StudentBookOnceOrderTM.IS_SEND_NOT);
                                studentBookOnceOrderTM.setOperator("管理员");
                                delStudentBookOnceOrderTMByOrderIdDAO.save(studentBookOnceOrderTM);
                            }
                        }
                    }
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
}
