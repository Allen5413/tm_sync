package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.FindListByWhereDAO;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookorder.StudentBookOrderDAO;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.service.scheduler.CreateStudentBookOrderService;
import com.zs.tools.OrderCodeTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成学生订书单
 * 通过当前学期选课，按学生生成订单。在生成订单时，要检查学习中心是否有指定教材得库存，
 * 如果有，就生成学习中心订单，由学习中心自己打包发给学生。没有就生成我们的订单，由我们打包发给学习中心，在分给学生。
 * Created by Allen on 2015/5/12.
 */
@Service("createStudentBookOrderService")
public class CreateStudentBookOrderServiceImpl
        extends EntityServiceImpl<StudentBookOrder, StudentBookOrderDAO>
        implements CreateStudentBookOrderService {

    @Resource
    private FindListByWhereDAO countStudentOrderTMForSelectCourse;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;
    @Resource
    private BatchStudentBookOrderLogDAO batchStudentBookOrderLogDAO;
    @Resource
    private FindStudentBookOrderForMaxCodeDAO findStudentBookOrderForMaxCodeDAO;

    @Override
    @Transactional
    public void addStudentBookOrder() throws Exception {
        String loginName = "管理员";
        List<StudentBookOrder> studentBookOrderList = new ArrayList<StudentBookOrder>();
        List<StudentBookOrderTM> studentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
        List<StudentBookOrderLog> studentBookOrderLogList = new ArrayList<StudentBookOrderLog>();

        //获取当前学期
        Semester semester = findNowSemesterDAO.getNowSemester();
        if(null == semester || null == semester.getId()){
            throw new BusinessException("没有设置当前学期！");
        }
        //获取通过选课，得到学生需要购买的教材数据
        //object顺序：issue_channel_id, student_code, course_code, teach_material_id, price
        List<Object[]> resultList = this.getCountStudentOrderTMForSelectCourse(semester.getId());
        if(null != resultList && 0 < resultList.size()){
            System.out.println("一共" + resultList.size()+"条数据");
            long beforeIssueChannelId = 0;
            String beforeStudentCode = "";
            int i=0;
            //查询当前最大订单号
            int num = 0;
            StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(semester.getId());
            if(null != maxCodeStudentBookOrder){
                String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
            }
            for(Object[] objs : resultList){
                System.out.println("i:  " + i);
                //如果有一个值为空，就不能生产订单
                if(null == objs[0] || null == objs[1] || null == objs[2] || null == objs[3]){
                    continue;
                }
                long issueChannelId = Long.parseLong(objs[0].toString());
                String studentCode = objs[1].toString();
                String courseCode = objs[2].toString();
                long tmId = Long.parseLong(objs[3].toString());
                float price = null == objs[4] ? 0 : Float.parseFloat(objs[4].toString());
                if(i < 1){
                    //生成订单号
                    String orderCode = OrderCodeTools.createStudentOrderCodeAuto(semester.getYear(), semester.getQuarter(), studentBookOrderList.size() + num + 1);
                    //新增订单
                    this.addStudentBookOrder(semester.getId(), issueChannelId, orderCode, studentCode, loginName, studentBookOrderList, studentBookOrderLogList);
                    //新增订单明细
                    this.addStudentBookOrderTM(orderCode, courseCode, tmId, price, loginName, studentBookOrderTMList);
                }else{
                    //因为数据是根据渠道id和学号 排升序的，所以可以这样比较
                    //判断和上一条记录得渠道id和学号是否一致，不一致，就说明要新增订单，一致就只增加订单明细
                    if(beforeIssueChannelId == issueChannelId && beforeStudentCode.equals(studentCode)){
                        //得到上一次订单的订单号
                        StudentBookOrder studentBookOrder = studentBookOrderList.get(studentBookOrderList.size() - 1);
                        //新增订单明细
                        this.addStudentBookOrderTM(studentBookOrder.getOrderCode(), courseCode, tmId, price, loginName, studentBookOrderTMList);
                    }else{
                        //生成订单号
                        String orderCode = OrderCodeTools.createStudentOrderCodeAuto(semester.getYear(), semester.getQuarter(), studentBookOrderList.size() + num + 1);
                        //新增订单
                        this.addStudentBookOrder(semester.getId(), issueChannelId, orderCode, studentCode, loginName, studentBookOrderList, studentBookOrderLogList);
                        //新增订单明细
                        this.addStudentBookOrderTM(orderCode, courseCode, tmId, price, loginName, studentBookOrderTMList);
                    }
                }
                //记录当前的渠道id和学号
                beforeIssueChannelId = issueChannelId;
                beforeStudentCode = studentCode;
                i++;
            }
        }
        batchStudentBookOrderDAO.batchAdd(studentBookOrderList, 1000);
        batchStudentBookOrderLogDAO.batchAdd(studentBookOrderLogList, 1000);
        batchStudentBookOrderTMDAO.batchAdd(studentBookOrderTMList, 1000);
    }

    /**
     * 查询当前学期学生选课需要购买的教材信息
     * @param semesterId
     * @return
     */
    private List<Object[]> getCountStudentOrderTMForSelectCourse(long semesterId){
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("semesterId", semesterId+"");
        List<Object[]> list = countStudentOrderTMForSelectCourse.findListByWhere(paramsMap, null);
        return list;
    }


    private void addStudentBookOrder(long semesterId, long issueChannelId, String orderCode, String studentCode,
                                     String loginName, List<StudentBookOrder> studentBookOrderList, List<StudentBookOrderLog> studentBookOrderLogList){
        StudentBookOrder studentBookOrder = new StudentBookOrder();
        studentBookOrder.setSemesterId(semesterId);
        studentBookOrder.setIssueChannelId(issueChannelId);
        studentBookOrder.setOrderCode(orderCode);
        studentBookOrder.setStudentCode(studentCode);
        studentBookOrder.setState(StudentBookOrder.STATE_UNCONFIRMED);
        studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
        studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
        studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_NOT);
        studentBookOrder.setCreator(loginName);
        studentBookOrder.setOperator(loginName);
        //执行订单数据保存操作
        studentBookOrderList.add(studentBookOrder);

        //添加学生订书单状态变更日志
        StudentBookOrderLog studentBookOrderLog = new StudentBookOrderLog();
        studentBookOrderLog.setOrderCode(orderCode);
        studentBookOrderLog.setState(StudentBookOrder.STATE_UNCONFIRMED);
        studentBookOrderLog.setOperator(loginName);
        studentBookOrderLogList.add(studentBookOrderLog);
    }


    private void addStudentBookOrderTM(String orderCode, String courseCode, long tmId, float price, String loginName, List<StudentBookOrderTM> studentBookOrderTMList){
        StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
        studentBookOrderTM.setOrderCode(orderCode);
        studentBookOrderTM.setCourseCode(courseCode);
        studentBookOrderTM.setTeachMaterialId(tmId);
        studentBookOrderTM.setPrice(price);
        studentBookOrderTM.setCount(1);
        studentBookOrderTM.setOperator(loginName);
        studentBookOrderTMList.add(studentBookOrderTM);
    }
}
