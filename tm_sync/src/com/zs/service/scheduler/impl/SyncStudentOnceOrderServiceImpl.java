package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.onceorder.BatchStudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorder.FindStudentBookOnceOrderForMaxCodeDAO;
import com.zs.dao.sale.onceorder.FindStudentBookOnceOrderForMaxIdDAO;
import com.zs.dao.sale.onceorder.StudentBookOnceOrderDAO;
import com.zs.dao.sale.onceordertm.BatchStudentBookOnceOrderTMDAO;
import com.zs.dao.sale.onceordertm.DelStudentBookOnceOrderTMByOrderIdDAO;
import com.zs.dao.sale.studentbookorder.FindSyncOnceOrderStudentDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
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
    private FindSyncOnceOrderStudentDAO findSyncOnceOrderStudentDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private FindTeachScheduleByYearAndQuarterAndSpecAndLevelDAO findTeachScheduleByYearAndQuarterAndSpecAndLevelDAO;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private SelectedCourseDAO selectedCourseDAO;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private DelStudentBookOnceOrderTMByOrderIdDAO delStudentBookOnceOrderTMByOrderIdDAO;
    @Resource
    private BatchStudentBookOnceOrderDAO batchStudentBookOnceOrderDAO;
    @Resource
    private BatchStudentBookOnceOrderTMDAO batchStudentBookOnceOrderTMDAO;
    @Resource
    private FindStudentBookOnceOrderForMaxIdDAO findStudentBookOnceOrderForMaxIdDAO;
    @Resource
    private FindStudentBookOnceOrderForMaxCodeDAO findStudentBookOnceOrderForMaxCodeDAO;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;

    //课程对应的教材信息
    Map<String, List<TeachMaterial>> courseTMMap = null;

    List<StudentBookOnceOrder> addOrderList = null;
    List<StudentBookOnceOrderTM> addOrderTMList = null;

    StringBuilder msg = null;

    //变更信息描述
    String detail = null;

    //查询当前学期
    Semester semester = null;

    @Override
    @Transactional
    public void sync(int isOnlyAdd) throws Exception {
        try {
            courseTMMap = new HashMap<String, List<TeachMaterial>>();
            addOrderList = new ArrayList<StudentBookOnceOrder>();
            addOrderTMList = new ArrayList<StudentBookOnceOrderTM>();

            msg = new StringBuilder(DateTools.getLongNowTime() + ": 开始执行学生一次性订单同步操作\r\n");
            detail = "";

            //查询当前学期
            semester = findNowSemesterDAO.getNowSemester();
            //查询不存在的一次性订单学生
            List<Object[]> notExistsList = findSyncOnceOrderStudentDAO.findNotExists(semester.getId(), semester.getYear(), semester.getQuarter());
            //新增不存在的学生一次性订单
            this.addOnceOrder(notExistsList);

            if(isOnlyAdd == 1) {
                //查询已经存在的一次性订单学生
                List<Object[]> existsList = findSyncOnceOrderStudentDAO.findExists(semester.getId(), semester.getYear(), semester.getQuarter());
                //修改存在的学生一次性订单
                this.editOnceOrder(existsList);
            }

            if(null != addOrderList && 0 < addOrderList.size()){
                batchStudentBookOnceOrderDAO.batchAdd(addOrderList, 1000);
            }
            if(null != addOrderTMList && 0 < addOrderTMList.size()){
                batchStudentBookOnceOrderTMDAO.batchAdd(addOrderTMList, 1000);
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

    protected void addOnceOrder(List<Object[]> notExistsList)throws Exception {
        try {
            System.out.println("新增订单数量总共=============================================" + notExistsList.size());
            int num = 1;
            for (Object[] obj : notExistsList) {
                String studentCode = obj[0].toString();
                String spotCode = obj[1].toString();
                int year = Integer.parseInt(obj[2].toString());
                int quarter = Integer.parseInt(obj[3].toString());
                String specCode = obj[4].toString();
                String levelCode = obj[5].toString();

                //查询该学生到毕业时的所需课程
                List<Object[]> courseList = findTeachScheduleByYearAndQuarterAndSpecAndLevelDAO.find(year, 0 == quarter ? 1 : 2, specCode, levelCode, studentCode);
                if (null != courseList && 0 < courseList.size()) {
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

                    //得到当前学期最大的订单号
                    StudentBookOnceOrder maxCodeOrder = findStudentBookOnceOrderForMaxCodeDAO.find(semester.getId());
                    if(null != maxCodeOrder){
                        String maxOrderCode = maxCodeOrder.getOrderCode();
                        num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
                    }
                    //生成学生订单号
                    String orderCode = OrderCodeTools.createStudentOnceOrderCode(semester.getYear(), semester.getQuarter(), num + 1);

                    Student student = findStudentByCodeDAO.getStudentByCode(studentCode);
                    //添加订单信息
                    StudentBookOnceOrder order = new StudentBookOnceOrder();
                    order.setId(maxId+num);
                    order.setOrderCode(orderCode);
                    order.setIssueChannelId(issueChannelId);
                    order.setStudentCode(studentCode);
                    order.setState(StudentBookOnceOrder.STATE_UNCONFIRMED);
                    order.setStudentSign(StudentBookOnceOrder.STUDENTSIGN_NOT);
                    if(Student.IS_SEND_STUDENT_NOT == student.getIsSendStudent()){
                        order.setIsSendStudent(StudentBookOnceOrder.IS_SEND_STUDENT_NOT);
                    }
                    if(Student.IS_SEND_STUDENT_YES == student.getIsSendStudent()){
                        order.setIsSendStudent(StudentBookOnceOrder.IS_SEND_STUDENT_YES);
                    }
                    order.setCreator("管理员");
                    order.setOperator("管理员");
                    addOrderList.add(order);
                    //findSyncOnceOrderStudentDAO.save(order);
                    //添加订单明细
                    this.addOnceOrderTM(order.getId(), studentCode, year, quarter, specCode, levelCode, courseList);
                    System.out.println("新增订单数量============================================="+num);
                    num++;
                }
            }
        }catch (Exception e){
            throw e;
        }
    }

    protected void editOnceOrder(List<Object[]> existsList)throws Exception {
        try{
            System.out.println("修改订单数量总共=============================================" + existsList.size());
            int num = 0;
            for (Object[] obj : existsList) {
                long orderId = Long.parseLong(obj[0].toString());
                String studentCode = obj[1].toString();
                int year = Integer.parseInt(obj[2].toString());
                int quarter = Integer.parseInt(obj[3].toString());
                String specCode = obj[4].toString();
                String levelCode = obj[5].toString();

                //删除订单明细，重新添加订单明细
                delStudentBookOnceOrderTMByOrderIdDAO.del(orderId);
                this.addOnceOrderTM(orderId, studentCode, year, quarter, specCode, levelCode, null);

                num++;
                System.out.println("修改订单数量============================================="+num);
            }
        }catch (Exception e){
            throw e;
        }
    }

    protected void addOnceOrderTM(long orderId, String studentCode, int year, int quarter, String specCode, String levelCode, List<Object[]> courseList)throws Exception{
        String courseCode="";
        try {
            if(null == courseList) {
                //查询该学生到毕业时的所需课程
                courseList = findTeachScheduleByYearAndQuarterAndSpecAndLevelDAO.find(year, 0 == quarter ? 1 : 2, specCode, levelCode, studentCode);
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
                        orderTM.setOrderId(orderId);
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
                        //delStudentBookOnceOrderTMByOrderIdDAO.save(orderTM);
                    }
                }
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            msg.append("学号：" + studentCode + ", 课程编号：" + courseCode + " \r\n");
            msg.append("异常信息：" + sw.toString() + "\r\n");
            throw e;
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
