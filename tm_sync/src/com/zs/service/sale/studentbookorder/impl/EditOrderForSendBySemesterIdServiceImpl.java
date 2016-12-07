package com.zs.service.sale.studentbookorder.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterialstock.FindTeachMaterialStockBytmIdAndChannelIdDAO;
import com.zs.dao.finance.studentexpensebuy.StudentExpenseBuyDao;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForStatePackageBySemesterIdDAO;
import com.zs.dao.sale.studentbookorder.StudentBookOrderDAO;
import com.zs.dao.sale.studentbookorderlog.StudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.StudentBookOrderTmDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.basic.TeachMaterialStock;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.Student;
import com.zs.service.basic.issuerange.FindIssueRangeBySpotCodeService;
import com.zs.service.basic.teachmaterial.FindTeachMaterialService;
import com.zs.service.finance.studentexpense.AddStuEBuyService;
import com.zs.service.sale.studentbookorder.EditOrderForSendBySemesterIdService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2016/12/7.
 */
@Service("editOrderForSendBySemesterIdService")
public class EditOrderForSendBySemesterIdServiceImpl extends EntityServiceImpl<StudentBookOrder, StudentBookOrderDAO>
    implements EditOrderForSendBySemesterIdService{

    @Resource
    private FindStudentBookOrderForStatePackageBySemesterIdDAO findStudentBookOrderForStatePackageBySemesterIdDAO;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private StudentBookOrderLogDAO studentBookOrderLogDAO;
    @Resource
    private StudentBookOrderTmDAO studentBookOrderTmDAO;
    @Resource
    private FindTeachMaterialService findTeachMaterialService;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private StudentExpenseBuyDao studentExpenseBuyDao;
    @Resource
    private AddStuEBuyService addStuEBuyService;
    @Resource
    private FindIssueRangeBySpotCodeService findIssueRangeBySpotCodeService;
    @Resource
    private FindTeachMaterialStockBytmIdAndChannelIdDAO findTeachMaterialStockBytmIdAndChannelIdDAO;


    @Override
    @Transactional
    public void edit(long semesterId) throws Exception {
        List<StudentBookOrder> studentBookOrderList = findStudentBookOrderForStatePackageBySemesterIdDAO.find(semesterId);
        if(null != studentBookOrderList && 0 < studentBookOrderList.size()){
            for(StudentBookOrder studentBookOrder : studentBookOrderList){
                //查询学生信息
                Student student = findStudentByCodeDAO.getStudentByCode(studentBookOrder.getStudentCode());

                //修改状态
                studentBookOrder.setState(StudentBookOrder.STATE_SEND);
                studentBookOrder.setOperator("管理员");
                studentBookOrder.setOperateTime(DateTools.getLongNowTime());
                super.update(studentBookOrder);

                //记录状态变更日志
                StudentBookOrderLog studentBookOrderLog = new StudentBookOrderLog();
                studentBookOrderLog.setOrderCode(studentBookOrder.getOrderCode());
                studentBookOrderLog.setState(StudentBookOrder.STATE_SEND);
                studentBookOrderLog.setOperator("管理员");
                studentBookOrderLogDAO.save(studentBookOrderLog);



                //查询订单下的明细
                List<StudentBookOrderTM> studentBookOrderTMList = studentBookOrderTmDAO.findStudentBookOrderTMByOrderCode(studentBookOrder.getOrderCode());
                BigDecimal money = new BigDecimal(0);
                if (null != studentBookOrderTMList && 0 < studentBookOrderTMList.size()) {
                    for (StudentBookOrderTM studentBookOrderTM : studentBookOrderTMList) {
                        //查询教材信息
                        TeachMaterial teachMaterial = findTeachMaterialService.get(studentBookOrderTM.getTeachMaterialId());
                        if (null != teachMaterial && 0 < studentBookOrderTM.getCount()) {
                            money = money.add(new BigDecimal(studentBookOrderTM.getCount()).multiply(new BigDecimal(studentBookOrderTM.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //记录学生消费
                            StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
                            studentExpenseBuy.setStudentCode(studentBookOrder.getStudentCode());
                            studentExpenseBuy.setSemester(findNowSemesterDAO.get(semesterId));
                            studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
                            studentExpenseBuy.setDetail("购买了" + studentBookOrderTM.getCount() + "本，[" + teachMaterial.getName() + "] 教材");
                            studentExpenseBuy.setMoney(new BigDecimal(studentBookOrderTM.getCount()).multiply(new BigDecimal(studentBookOrderTM.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            studentExpenseBuy.setTeachMaterialId(teachMaterial.getId());


                            //添加创建人
                            studentExpenseBuy.setCreator("管理员");
                            studentExpenseBuyDao.save(studentExpenseBuy);
                            //记录学生费用信息
                            addStuEBuyService.addStuEBuy(studentExpenseBuy, "管理员");


                            //减去教材库存
                            if (null != student) {
                                IssueRange issueRange = findIssueRangeBySpotCodeService.getIssueRangeBySpotCode(student.getSpotCode());
                                if (null != issueRange) {
                                    TeachMaterialStock teachMaterialStock = findTeachMaterialStockBytmIdAndChannelIdDAO.getTeachMaterialStockBytmIdAndChannelId(teachMaterial.getId(), issueRange.getIssueChannelId());
                                    if (null != teachMaterialStock) {
                                        teachMaterialStock.setStock(teachMaterialStock.getStock() - studentBookOrderTM.getCount());
                                        findTeachMaterialStockBytmIdAndChannelIdDAO.update(teachMaterialStock);
                                    }
                                }
                            }
                            //记录该订单明细为已发出
                            studentBookOrderTM.setIsSend(StudentBookOrderTM.IS_SEND_YES);
                            studentBookOrderTmDAO.update(studentBookOrderTM);
                        }
                    }
                }
            }
        }
    }
}
