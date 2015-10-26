package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookorder.TempDAO;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.Student;
import com.zs.service.scheduler.TempService;
import com.zs.tools.OrderCodeTools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Allen on 2015/10/26.
 */
@Service("tempService")
public class TempServiceImpl implements TempService {

    @Resource
    private TempDAO tempDAO;
    @Resource
    private FindStudentBookOrderForMaxCodeDAO findStudentBookOrderForMaxCodeDAO;
    @Resource
    private FindTeachMaterialByCourseCodeDAO findTeachMaterialByCourseCodeDAO;
    @Resource
    private FindTeachMaterialFromSetTMByCourseCodeDAO findTeachMaterialFromSetTMByCourseCodeDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private BatchStudentBookOrderLogDAO batchStudentBookOrderLogDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;

    private Map<String, List<String>> map = new HashMap<String, List<String>>();
    private List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
    private List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
    private List<StudentBookOrderLog> addStudentBookOrderLogList = new ArrayList<StudentBookOrderLog>();

    @Override
    @Transactional
    public void doSync() {
        try{
            List<Object[]> list = tempDAO.find();
            String beforeStudentCode = "";
            List<Object> existsList = null;
            List<String> courseCodeList = null;
            for(Object[] objs : list){
                String studentCode = objs[0].toString();
                String courseCode = objs[1].toString();

                if(beforeStudentCode.equals(studentCode)){
                    courseCodeList = map.get(studentCode);
                    if(!aa(existsList, courseCode)) {
                        courseCodeList.add(courseCode);
                    }
                    map.put(studentCode, courseCodeList);
                }else{
                    courseCodeList = new ArrayList<String>();
                    existsList = tempDAO.findExistsCourse(studentCode);
                    if(!aa(existsList, courseCode)) {
                        courseCodeList.add(courseCode);
                    }
                    map.put(studentCode, courseCodeList);
                }
                beforeStudentCode = studentCode;
            }

            int i=0;
            for (String studentCode : map.keySet()) {
                System.out.println("i:  " + i);
                i++;
                List<String> courseCodeList2 = map.get(studentCode);

                //得到当前学期最大的订单号
                int num = 0;
                StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(2l);
                if(null != maxCodeStudentBookOrder){
                    String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                    num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
                }
                //生成学生订单号
                String orderCode = OrderCodeTools.createStudentOrderCodeAuto(2015, 1, num + addStudentBookOrderList.size() + 1);
                //添加订单信息
                StudentBookOrder studentBookOrder = new StudentBookOrder();
                studentBookOrder.setSemesterId(2l);
                studentBookOrder.setIssueChannelId(1l);
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
                if(null != courseCodeList2 && 0 < courseCodeList2.size()) {
                    for(String courseCode :courseCodeList2) {
                        List<TeachMaterial> teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
                        if (null != teachMaterialList && 0 < teachMaterialList.size()) {
                            for (TeachMaterial teachMaterial : teachMaterialList) {
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
                    }
                }
            }

            //批量提交数据
            if(null != addStudentBookOrderList && 0 < addStudentBookOrderList.size()){
                batchStudentBookOrderDAO.batchAdd(addStudentBookOrderList, 1000);
            }
            if(null != addStudentBookOrderLogList && 0 < addStudentBookOrderLogList.size()){
                batchStudentBookOrderLogDAO.batchAdd(addStudentBookOrderLogList, 1000);
            }
            if(null != addStudentBookOrderTMList && 0 < addStudentBookOrderTMList.size()){
                batchStudentBookOrderTMDAO.batchAdd(addStudentBookOrderTMList, 1000);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean aa(List<Object> existsList, String courseCode){
        if(null != existsList && 0 < existsList.size()){
           boolean isExists = false;
            for(Object obj : existsList){
                if(obj.toString().equals(courseCode)){
                    isExists = true;
                }
            }
            return isExists;
        }else{
            return false;
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
