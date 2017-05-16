package com.zs.service.sale.onceorder.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterialstock.FindTeachMaterialStockBytmIdAndChannelIdDAO;
import com.zs.dao.sale.onceorder.FindOnceOrderByCodeDAO;
import com.zs.dao.sale.onceorder.StudentBookOnceOrderDAO;
import com.zs.dao.sale.onceorderlog.OnceOrderLogDAO;
import com.zs.dao.sale.onceordertm.FindOnceOrderTMByOrderIdDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.basic.TeachMaterialStock;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.domain.sale.StudentBookOnceOrder;
import com.zs.domain.sale.StudentBookOnceOrderLog;
import com.zs.domain.sale.StudentBookOnceOrderTM;
import com.zs.domain.sync.Student;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.basic.teachmaterial.FindTeachMaterialService;
import com.zs.service.finance.studentexpensebuy.AddStudentExpenseBuyService;
import com.zs.service.sale.onceorder.EditOnceOrderForSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2017/5/16.
 */
@Service("editOnceOrderForSendService")
public class EditOnceOrderForSendServiceImpl extends EntityServiceImpl<StudentBookOnceOrder, StudentBookOnceOrderDAO> implements EditOnceOrderForSendService {

    @Resource
    private FindOnceOrderByCodeDAO findOnceOrderByCodeDAO;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private OnceOrderLogDAO onceOrderLogDAO;
    @Resource
    private FindOnceOrderTMByOrderIdDAO findOnceOrderTMByOrderIdDAO;
    @Resource
    private FindTeachMaterialService findTeachMaterialService;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private AddStudentExpenseBuyService addStudentExpenseBuyService;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private FindTeachMaterialStockBytmIdAndChannelIdDAO findTeachMaterialStockBytmIdAndChannelIdDAO;

    @Override
    @Transactional
    public void edit(String orderCode) {
        try{
            //获取当前学期
            Semester semester = findNowSemesterDAO.getNowSemester();
            //查询订单
            StudentBookOnceOrder studentBookOnceOrder = findOnceOrderByCodeDAO.find(orderCode);
            //查询学生信息
            Student student = findStudentByCodeDAO.getStudentByCode(studentBookOnceOrder.getStudentCode());
            //修改状态
            studentBookOnceOrder.setState(StudentBookOnceOrder.STATE_SEND);
            super.update(studentBookOnceOrder);

            //记录状态变更日志
            StudentBookOnceOrderLog studentBookOnceOrderLog = new StudentBookOnceOrderLog();
            studentBookOnceOrderLog.setOrderId(studentBookOnceOrder.getId());
            studentBookOnceOrderLog.setState(StudentBookOnceOrder.STATE_SEND);
            studentBookOnceOrderLog.setOperator("管理员");
            onceOrderLogDAO.save(studentBookOnceOrderLog);

            //查询订单下的明细
            List<StudentBookOnceOrderTM> studentBookOnceOrderTMList = findOnceOrderTMByOrderIdDAO.find(studentBookOnceOrder.getId());
            BigDecimal money = new BigDecimal(0);
            if (null != studentBookOnceOrderTMList && 0 < studentBookOnceOrderTMList.size()) {
                for (StudentBookOnceOrderTM studentBookOnceOrderTM : studentBookOnceOrderTMList) {
                    //查询教材信息
                    TeachMaterial teachMaterial = findTeachMaterialService.get(studentBookOnceOrderTM.getTeachMaterialId());
                    if (null != teachMaterial && 0 < studentBookOnceOrderTM.getCount()) {
                        money = money.add(new BigDecimal(studentBookOnceOrderTM.getCount()).multiply(new BigDecimal(studentBookOnceOrderTM.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //记录学生消费
                        StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
                        studentExpenseBuy.setStudentCode(studentBookOnceOrder.getStudentCode());
                        studentExpenseBuy.setSemester(semester);
                        studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
                        studentExpenseBuy.setDetail("购买了" + studentBookOnceOrderTM.getCount() + "本，[" + teachMaterial.getName() + "] 教材");
                        studentExpenseBuy.setMoney(new BigDecimal(studentBookOnceOrderTM.getCount()).multiply(new BigDecimal(studentBookOnceOrderTM.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        studentExpenseBuy.setTeachMaterialId(teachMaterial.getId());
                        addStudentExpenseBuyService.addStudentExpenseBuy(studentExpenseBuy, "管理员");
                        //减去教材库存
                        if (null != student) {
                            IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(student.getSpotCode());
                            if (null != issueRange) {
                                TeachMaterialStock teachMaterialStock = findTeachMaterialStockBytmIdAndChannelIdDAO.getTeachMaterialStockBytmIdAndChannelId(teachMaterial.getId(), issueRange.getIssueChannelId());
                                if (null != teachMaterialStock) {
                                    teachMaterialStock.setStock(teachMaterialStock.getStock() - studentBookOnceOrderTM.getCount());
                                    findTeachMaterialStockBytmIdAndChannelIdDAO.update(teachMaterialStock);
                                }
                            }
                        }
                        //记录该订单明细为已发出
                        studentBookOnceOrderTM.setIsSend(StudentBookOnceOrderTM.IS_SEND_YES);
                        findOnceOrderTMByOrderIdDAO.update(studentBookOnceOrderTM);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
