package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.finance.spotexpenseoth.SpotExpenseOthDAO;
import com.zs.dao.finance.studentexpense.FindRecordStudentCodeDao;
import com.zs.dao.finance.studentexpensebuy.StudentExpenseBuyDao;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByNameAndAuthorDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.dao.sync.CreateStudentOrderFor15DAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.domain.finance.StudentExpense;
import com.zs.domain.finance.StudentExpenseBuy;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.Student;
import com.zs.service.scheduler.CreateStudentOrderFor15Service;
import com.zs.tools.DateTools;
import com.zs.tools.OrderCodeTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 2015/10/20.
 */
@Service("createStudentOrderFor15Service")
public class CreateStudentOrderFor15ServiceImpl implements CreateStudentOrderFor15Service {

    @Resource
    private CreateStudentOrderFor15DAO createStudentOrderFor15DAO;
    @Resource
    private FindTeachMaterialByNameAndAuthorDAO findTeachMaterialByNameAndAuthorDAO;
    @Resource
    private FindStudentBookOrderForMaxCodeDAO findStudentBookOrderForMaxCodeDAO;
    @Resource
    private BatchStudentBookOrderDAO batchStudentBookOrderDAO;
    @Resource
    private BatchStudentBookOrderTMDAO batchStudentBookOrderTMDAO;
    @Resource
    private StudentExpenseBuyDao studentExpenseBuyDao;
    @Resource
    private FindRecordStudentCodeDao findRecordStudentCodeDao;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private SpotExpenseOthDAO spotExpenseOthDAO;

    List<StudentBookOrder> studentBookOrderList = new ArrayList<StudentBookOrder>();
    List<StudentBookOrderTM> studentBookOrderTMList = new ArrayList<StudentBookOrderTM>();

    @Override
    @Transactional
    public void create() {
        try {
        //查询当前最大订单号
        int num = 0;
        StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(1l);
        if(null != maxCodeStudentBookOrder){
            String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
            num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
        }
        List<Object[]> list = createStudentOrderFor15DAO.find();
        String beforeStudentCode = "";
        int i=0;
        for(Object[] objs : list){
            System.out.println("i :  "+i);
            i++;
            String studentCode = objs[0].toString();
            String courseCode = objs[1].toString();
            String name = objs[2].toString();
            String author = objs[3].toString();
            float price = Float.valueOf(objs[4].toString());

            Student student = findStudentByCodeDAO.getStudentByCode(studentCode);
            if(null == student){
                throw new BusinessException("没有找到学号： "+studentCode);
            }

            //如果学号不一样了，就生成订单
            if(!beforeStudentCode.equals(studentCode)){
                //生成订单号
                String orderCode = OrderCodeTools.createStudentOrderCodeAuto(2015, 0, studentBookOrderList.size() + num + 1);
                this.addStudentBookOrder(orderCode, studentCode);
            }
            //查询教材
            List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name,author);
            TeachMaterial teachMaterial = null;
            if(null != teachMaterialList && 0 < teachMaterialList.size()){
                for(TeachMaterial teachMaterial2 : teachMaterialList){
                    if(teachMaterial2.getState() == TeachMaterial.STATE_ENABLE){
                        teachMaterial = teachMaterial2;
                        break;
                    }
                }
                if(null == teachMaterial){
                    teachMaterial = teachMaterialList.get(0);
                }
            }
            //得到上一次订单的订单号
            StudentBookOrder studentBookOrder = studentBookOrderList.get(studentBookOrderList.size() - 1);
            this.addStudentBookOrderTM(student, studentBookOrder.getOrderCode(), courseCode,
                    null == teachMaterial ? null : teachMaterial.getId(), null == teachMaterial ? null : teachMaterial.getName(), price);

            beforeStudentCode = studentCode;
        }

            batchStudentBookOrderDAO.batchAdd(studentBookOrderList, 1000);
            batchStudentBookOrderTMDAO.batchAdd(studentBookOrderTMList, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStudentBookOrder(String orderCode, String studentCode){
        StudentBookOrder studentBookOrder = new StudentBookOrder();
        studentBookOrder.setSemesterId(1l);
        studentBookOrder.setIssueChannelId(1l);
        studentBookOrder.setOrderCode(orderCode);
        studentBookOrder.setStudentCode(studentCode);
        studentBookOrder.setState(StudentBookOrder.STATE_SIGN);
        studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
        studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
        studentBookOrder.setPrintSort(null);
        studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_YES);
        studentBookOrder.setCreator("管理员");
        studentBookOrder.setOperator("管理员");
        //执行订单数据保存操作
        studentBookOrderList.add(studentBookOrder);
    }

    private void addStudentBookOrderTM(Student student, String orderCode, String courseCode, Long tmId, String tmName, float price){
        StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
        studentBookOrderTM.setOrderCode(orderCode);
        studentBookOrderTM.setCourseCode(courseCode);
        studentBookOrderTM.setTeachMaterialId(tmId);
        studentBookOrderTM.setPrice(price);
        studentBookOrderTM.setCount(1);
        studentBookOrderTM.setOperator("管理员");
        studentBookOrderTMList.add(studentBookOrderTM);

        //记录学生消费
        StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
        studentExpenseBuy.setStudentCode(student.getCode());
        Semester semester = new Semester();
        semester.setId(1l);
        semester.setYear(2015);
        semester.setQuarter(0);
        studentExpenseBuy.setSemester(semester);
        studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
        studentExpenseBuy.setDetail("购买了1本，[" + tmName + "] 教材");
        studentExpenseBuy.setMoney(price);
        studentExpenseBuy.setCreator("管理员");
        studentExpenseBuyDao.save(studentExpenseBuy);

        //记录学生费用信息
        this.addStuEBuy(studentExpenseBuy.getMoney(), studentExpenseBuy.getStudentCode(), "管理员", student.getSpotCode());

    }

    private void addStuEBuy(Float changeMoney, String studentCode,String userName, String spotCode){
        //查询是否存在该学生的费用信息，不存在就新增，存在就修改
        StudentExpense studentExpense = findRecordStudentCodeDao.getRecordByStuCode(studentCode, 1l);
        float pay = 0;
        float buy = 0;
        if (null == studentExpense){
            studentExpense = new StudentExpense();
            studentExpense.setSemesterId(1l);
            studentExpense.setStudentCode(studentCode);
            studentExpense.setPay(0f);
            studentExpense.setBuy(changeMoney);
            //添加状态
            studentExpense.setState(StudentExpense.STATE_NO);
            //添加创建人和操作人
            studentExpense.setCreator(userName);
            studentExpense.setOperator(userName);
            //执行添加
            findRecordStudentCodeDao.save(studentExpense);
        }else{
            buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
            //修改金额
            studentExpense.setBuy(buy+changeMoney);
            //写入操作人
            studentExpense.setOperator(userName);
            //创建人和创建时间不能变
            studentExpense.setCreator(studentExpense.getCreator());
            studentExpense.setCreateTime(studentExpense.getCreateTime());
            //版本号设置
            studentExpense.setVersion(studentExpense.getVersion());
            //执行修改
            findRecordStudentCodeDao.update(studentExpense);
        }

        //更新中心消费数据
        pay = null == studentExpense.getPay() ? 0 : studentExpense.getPay();
        buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
        float tempFee = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if(0 <= tempFee){
            List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(1l, spotCode);
            if(null != spotExpenseOthList && spotExpenseOthList.size() > 0){
                SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(changeMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(changeMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                this.spotExpenseOthDAO.update(spotExpenseOth);
            }else{
                SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
                spotExpenseOth.setCreateTime(DateTools.getLongNowTime());
                spotExpenseOth.setCreator(userName);
                spotExpenseOth.setPay(0);
                spotExpenseOth.setBuy(changeMoney);
                spotExpenseOth.setSemesterId(1l);
                spotExpenseOth.setSpotCode(spotCode);
                spotExpenseOth.setState(1);
                spotExpenseOth.setStuAccTot(tempFee);
                spotExpenseOth.setStuOwnTot(0);
                this.spotExpenseOthDAO.save(spotExpenseOth);
            }
        }else{
            List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(1l, spotCode);
            if(null != spotExpenseOthList && spotExpenseOthList.size() > 0){
                SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(changeMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).subtract(new BigDecimal(tempFee)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(tempFee)).subtract(new BigDecimal(changeMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                if(spotExpenseOth.getStuOwnTot() > 0){
                    spotExpenseOth.setClearTime(null);
                }
                this.spotExpenseOthDAO.update(spotExpenseOth);
            }else{
                SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
                spotExpenseOth.setCreateTime(null);
                spotExpenseOth.setCreator(userName);
                spotExpenseOth.setPay(0);
                spotExpenseOth.setBuy(changeMoney);
                spotExpenseOth.setSemesterId(1l);
                spotExpenseOth.setSpotCode(spotCode);
                spotExpenseOth.setState(1);
                spotExpenseOth.setStuAccTot(0);
                spotExpenseOth.setStuOwnTot(tempFee);
                this.spotExpenseOthDAO.save(spotExpenseOth);
            }
        }
    }
}
