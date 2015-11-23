package com.zs.service.scheduler.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByCourseCodeDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByNameAndAuthorDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialFromSetTMByCourseCodeDAO;
import com.zs.dao.basic.teachmaterialstock.FindTeachMaterialStockBytmIdAndChannelIdDAO;
import com.zs.dao.finance.spotexpense.FindSpotRecordBySpotCodeDao;
import com.zs.dao.finance.spotexpensebuy.SpotExpenseBuyDao;
import com.zs.dao.finance.spotexpenseoth.SpotExpenseOthDAO;
import com.zs.dao.finance.studentexpense.FindRecordStudentCodeDao;
import com.zs.dao.finance.studentexpensebuy.StudentExpenseBuyDao;
import com.zs.dao.placeorder.placeorder.EditPlaceOrderStateByNuDAO;
import com.zs.dao.placeorder.placeorder.PlaceOrderDAO;
import com.zs.dao.placeorder.placeorderteachmatiral.PlaceOrderTeachMatiralDAO;
import com.zs.dao.sale.studentbookorder.BatchStudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.FindStudentBookOrderForMaxCodeDAO;
import com.zs.dao.sale.studentbookorder.StudentBookOrderDAO;
import com.zs.dao.sale.studentbookorder.TempDAO;
import com.zs.dao.sale.studentbookorderlog.BatchStudentBookOrderLogDAO;
import com.zs.dao.sale.studentbookordertm.BatchStudentBookOrderTMDAO;
import com.zs.dao.sale.studentbookordertm.StudentBookOrderTmDAO;
import com.zs.dao.sync.FindStudentByCodeDAO;
import com.zs.dao.temp.SpotOrder15DAO;
import com.zs.domain.basic.IssueRange;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.basic.TeachMaterialStock;
import com.zs.domain.finance.*;
import com.zs.domain.placeorder.PlaceOrderTeachMaterial;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.domain.sale.StudentBookOrder;
import com.zs.domain.sale.StudentBookOrderLog;
import com.zs.domain.sale.StudentBookOrderTM;
import com.zs.domain.sync.SelectedCourse;
import com.zs.domain.sync.Spot;
import com.zs.domain.sync.Student;
import com.zs.service.basic.teachmaterial.FindTeachMaterialService;
import com.zs.service.bean.PlaceOrderDetailShow;
import com.zs.service.finance.spotexpensebuy.AddSpotExpenseBuyService;
import com.zs.service.scheduler.TempService;
import com.zs.tools.DateTools;
import com.zs.tools.OrderCodeTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private SpotOrder15DAO spotOrder15DAO;
    @Resource
    private FindTeachMaterialByNameAndAuthorDAO findTeachMaterialByNameAndAuthorDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private StudentExpenseBuyDao studentExpenseBuyDao;
    @Resource
    private FindRecordStudentCodeDao findRecordStudentCodeDao;
    @Resource
    private SpotExpenseOthDAO spotExpenseOthDAO;
    @Resource
    private PlaceOrderDAO placeOrderDAO;
    @Resource
    private EditPlaceOrderStateByNuDAO editPlaceOrderStateByNuDAO;
    @Resource
    private PlaceOrderTeachMatiralDAO placeOrderTeachMatiralDAO;
    @Resource
    private SpotExpenseBuyDao spotExpenseBuyDao;
    @Resource
    private FindSpotRecordBySpotCodeDao findSpotRecordBySpotCodeDao;

    @Resource
    private StudentBookOrderDAO studentBookOrderDao;
    @Resource
    private FindStudentByCodeDAO findStudentByCodeDAO;
    @Resource
    private StudentBookOrderTmDAO studentBookOrderTmDAO;

    @Resource
    private AddSpotExpenseBuyService addSpotExpenseBuyService;
    @Resource
    private FindTeachMaterialService findTeachMaterialService;
    @Resource
    private FindTeachMaterialStockBytmIdAndChannelIdDAO findTeachMaterialStockBytmIdAndChannelIdDAO;

    //private Map<String, List<String>> map = new HashMap<String, List<String>>();
//    private List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
//    private List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
//    private List<StudentBookOrderLog> addStudentBookOrderLogList = new ArrayList<StudentBookOrderLog>();

    @Override
    @Transactional
    public void doSync() {
        try{
//            List<Object[]> list = tempDAO.find();
//            String beforeStudentCode = "";
//            List<Object> existsList = null;
//            List<String> courseCodeList = null;
//            for(Object[] objs : list){
//                String studentCode = objs[0].toString();
//                String courseCode = objs[1].toString();
//
//                if(beforeStudentCode.equals(studentCode)){
//                    courseCodeList = map.get(studentCode);
//                    if(!aa(existsList, courseCode)) {
//                        courseCodeList.add(courseCode);
//                    }
//                    map.put(studentCode, courseCodeList);
//                }else{
//                    courseCodeList = new ArrayList<String>();
//                    existsList = tempDAO.findExistsCourse(studentCode);
//                    if(!aa(existsList, courseCode)) {
//                        courseCodeList.add(courseCode);
//                    }
//                    map.put(studentCode, courseCodeList);
//                }
//                beforeStudentCode = studentCode;
//            }
//
//            int i=0;
//            for (String studentCode : map.keySet()) {
//                System.out.println("i:  " + i);
//                i++;
//                List<String> courseCodeList2 = map.get(studentCode);
//
//                //得到当前学期最大的订单号
//                int num = 0;
//                StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(2l);
//                if(null != maxCodeStudentBookOrder){
//                    String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
//                    num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length()-6, maxOrderCode.length()));
//                }
//                //生成学生订单号
//                String orderCode = OrderCodeTools.createStudentOrderCodeAuto(2015, 1, num + addStudentBookOrderList.size() + 1);
//                //添加订单信息
//                StudentBookOrder studentBookOrder = new StudentBookOrder();
//                studentBookOrder.setSemesterId(2l);
//                studentBookOrder.setIssueChannelId(1l);
//                studentBookOrder.setOrderCode(orderCode);
//                studentBookOrder.setStudentCode(studentCode);
//                studentBookOrder.setState(StudentBookOrder.STATE_UNCONFIRMED);
//                studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
//                studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
//                studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_NOT);
//                studentBookOrder.setCreator("管理员");
//                studentBookOrder.setOperator("管理员");
//                addStudentBookOrderList.add(studentBookOrder);
//
//                //添加订单日志信息
//                StudentBookOrderLog studentBookOrderLog = new StudentBookOrderLog();
//                studentBookOrderLog.setOrderCode(orderCode);
//                studentBookOrderLog.setState(StudentBookOrder.STATE_UNCONFIRMED);
//                studentBookOrderLog.setOperator("管理员");
//                addStudentBookOrderLogList.add(studentBookOrderLog);
//
//                //通过课程查询课程关联的教材
//                if(null != courseCodeList2 && 0 < courseCodeList2.size()) {
//                    for(String courseCode :courseCodeList2) {
//                        List<TeachMaterial> teachMaterialList = this.getTeachMaterialByCourseCode(courseCode);
//                        if (null != teachMaterialList && 0 < teachMaterialList.size()) {
//                            for (TeachMaterial teachMaterial : teachMaterialList) {
//                                StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
//                                studentBookOrderTM.setOrderCode(orderCode);
//                                studentBookOrderTM.setCourseCode(courseCode);
//                                studentBookOrderTM.setTeachMaterialId(teachMaterial.getId());
//                                studentBookOrderTM.setPrice(teachMaterial.getPrice());
//                                studentBookOrderTM.setCount(1);
//                                studentBookOrderTM.setOperator("管理员");
//                                addStudentBookOrderTMList.add(studentBookOrderTM);
//                            }
//                        }
//                    }
//                }
//            }
//
//            //批量提交数据
//            if(null != addStudentBookOrderList && 0 < addStudentBookOrderList.size()){
//                batchStudentBookOrderDAO.batchAdd(addStudentBookOrderList, 1000);
//            }
//            if(null != addStudentBookOrderLogList && 0 < addStudentBookOrderLogList.size()){
//                batchStudentBookOrderLogDAO.batchAdd(addStudentBookOrderLogList, 1000);
//            }
//            if(null != addStudentBookOrderTMList && 0 < addStudentBookOrderTMList.size()){
//                batchStudentBookOrderTMDAO.batchAdd(addStudentBookOrderTMList, 1000);
//            }

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







    @Override
    @Transactional
    public void doSync2(String spotCode) {

            Map<String, Map<String, Map<Float, Map<Integer, String>>>> map = new HashMap<String, Map<String, Map<Float, Map<Integer, String>>>>();
            List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
            List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
            List<StudentBookOrderLog> addStudentBookOrderLogList = new ArrayList<StudentBookOrderLog>();
            try {
                spotOrder15DAO.delNotExists(spotCode);
                List<Object[]> spotOrderList = spotOrder15DAO.find(spotCode);
                for (Object[] obj : spotOrderList) {
                    String courseCode = obj[1].toString();
                    String name = obj[2].toString();
                    String author = obj[4].toString();
                    float price = Float.parseFloat(obj[5].toString());
                    int count = Integer.parseInt(obj[6].toString());

                    if (null == map.get(name)) {
                        Map<String, Map<Float, Map<Integer, String>>> authorMap = new HashMap<String, Map<Float, Map<Integer, String>>>();
                        Map<Float, Map<Integer, String>> priceMap = new HashMap<Float, Map<Integer, String>>();
                        Map<Integer, String> countMap = new HashMap<Integer, String>();
                        countMap.put(count, courseCode);
                        priceMap.put(price, countMap);
                        authorMap.put(author, priceMap);
                        map.put(name, authorMap);
                    } else {
                        Map<String, Map<Float, Map<Integer, String>>> authorMap = map.get(name);
                        if (null == authorMap.get(author)) {
                            Map<Float, Map<Integer, String>> priceMap = new HashMap<Float, Map<Integer, String>>();
                            Map<Integer, String> countMap = new HashMap<Integer, String>();
                            countMap.put(count, courseCode);
                            priceMap.put(price, countMap);
                            authorMap.put(author, priceMap);
                            map.put(name, authorMap);
                        } else {
                            Map<Float, Map<Integer, String>> priceMap = authorMap.get(author);
                            if (null == priceMap.get(price)) {
                                Map<Integer, String> countMap = new HashMap<Integer, String>();
                                countMap.put(count, courseCode);
                                priceMap.put(price, countMap);
                                authorMap.put(author, priceMap);
                                map.put(name, authorMap);
                            }
                        }
                    }
                }


                List<Object[]> studentOrderList = spotOrder15DAO.findStudent(spotCode);
                if (null != studentOrderList && 0 < studentOrderList.size()) {
                    String beforeStudentCode = "";
                    for (Object[] objs : studentOrderList) {
                        String studentCode = objs[0].toString();
                        String name = objs[4].toString();
                        String author = objs[6].toString();
                        float price = Float.parseFloat(objs[7].toString());
                        String courseCode = bb(name, author, price, map);
                        if (!StringUtils.isEmpty(courseCode)) {
                            if (!beforeStudentCode.equals(studentCode)) {
                                //得到当前学期最大的订单号
                                int num = 0;
                                StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(1l);
                                if (null != maxCodeStudentBookOrder) {
                                    String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                                    num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length()));
                                }
                                //生成学生订单号
                                String orderCode = OrderCodeTools.createStudentOrderCodeAuto(2015, 0, num + addStudentBookOrderList.size() + 1);
                                //添加订单信息
                                StudentBookOrder studentBookOrder = new StudentBookOrder();
                                studentBookOrder.setSemesterId(1l);
                                studentBookOrder.setIssueChannelId(1l);
                                studentBookOrder.setOrderCode(orderCode);
                                studentBookOrder.setStudentCode(studentCode);
                                studentBookOrder.setState(StudentBookOrder.STATE_SIGN);
                                studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
                                studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
                                studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_NOT);
                                studentBookOrder.setCreator("管理员");
                                studentBookOrder.setOperator("管理员");
                                addStudentBookOrderList.add(studentBookOrder);

                                List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);

                                StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                                studentBookOrderTM.setOrderCode(orderCode);
                                studentBookOrderTM.setCourseCode(courseCode);
                                studentBookOrderTM.setTeachMaterialId(teachMaterialList.get(0).getId());
                                studentBookOrderTM.setPrice(price);
                                studentBookOrderTM.setCount(1);
                                studentBookOrderTM.setOperator("管理员");
                                addStudentBookOrderTMList.add(studentBookOrderTM);
                            } else {
                                List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);
                                StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                                studentBookOrderTM.setOrderCode(addStudentBookOrderList.get(addStudentBookOrderList.size() - 1).getOrderCode());
                                studentBookOrderTM.setCourseCode(courseCode);
                                studentBookOrderTM.setTeachMaterialId(teachMaterialList.get(0).getId());
                                studentBookOrderTM.setPrice(price);
                                studentBookOrderTM.setCount(1);
                                studentBookOrderTM.setOperator("管理员");
                                addStudentBookOrderTMList.add(studentBookOrderTM);

                            }

                            //记录学生消费
                            Semester semester = findNowSemesterDAO.get(1l);
                            StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
                            studentExpenseBuy.setStudentCode(studentCode);
                            studentExpenseBuy.setSemester(semester);
                            studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
                            studentExpenseBuy.setDetail("购买了1本，[" + name + "] 教材");
                            studentExpenseBuy.setMoney(price);
                            studentExpenseBuy.setCreator("管理员");
                            studentExpenseBuyDao.save(studentExpenseBuy);


                            //查询是否存在该学生的费用信息，不存在就新增，存在就修改
                            StudentExpense studentExpense = findRecordStudentCodeDao.getRecordByStuCode(studentCode, semester.getId());
                            float pay = 0;
                            float buy = 0;
                            if (null == studentExpense) {
                                studentExpense = new StudentExpense();
                                studentExpense.setSemesterId(semester.getId());
                                studentExpense.setStudentCode(studentCode);
                                studentExpense.setPay(0f);
                                studentExpense.setBuy(price);
                                //添加状态
                                studentExpense.setState(StudentExpense.STATE_NO);
                                //添加创建人和操作人
                                studentExpense.setCreator("管理员");
                                studentExpense.setOperator("管理员");
                                //执行添加
                                findRecordStudentCodeDao.save(studentExpense);
                            } else {
                                buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
                                //修改金额
                                studentExpense.setBuy(buy + price);
                                //写入操作人
                                studentExpense.setOperator("管理员");
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
                            if (0 <= tempFee) {
                                List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semester.getId(), spotCode);
                                if (null != spotExpenseOthList && spotExpenseOthList.size() > 0) {
                                    SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                                    spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    this.spotExpenseOthDAO.update(spotExpenseOth);
                                } else {
                                    SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
                                    spotExpenseOth.setCreateTime(DateTools.getLongNowTime());
                                    spotExpenseOth.setCreator("管理员");
                                    spotExpenseOth.setPay(0);
                                    spotExpenseOth.setBuy(price);
                                    spotExpenseOth.setSemesterId(semester.getId());
                                    spotExpenseOth.setSpotCode(spotCode);
                                    spotExpenseOth.setState(1);
                                    spotExpenseOth.setStuAccTot(tempFee);
                                    spotExpenseOth.setStuOwnTot(0);
                                    this.spotExpenseOthDAO.save(spotExpenseOth);
                                }
                            } else {
                                List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(semester.getId(), spotCode);
                                if (null != spotExpenseOthList && spotExpenseOthList.size() > 0) {
                                    SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                                    spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).add(new BigDecimal(price)).subtract(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    if (spotExpenseOth.getStuOwnTot() > 0) {
                                        spotExpenseOth.setClearTime(null);
                                    }
                                    this.spotExpenseOthDAO.update(spotExpenseOth);
                                } else {
                                    SpotExpenseOth spotExpenseOth = new SpotExpenseOth();
                                    spotExpenseOth.setCreateTime(null);
                                    spotExpenseOth.setCreator("管理员");
                                    spotExpenseOth.setPay(0);
                                    spotExpenseOth.setBuy(price);
                                    spotExpenseOth.setSemesterId(semester.getId());
                                    spotExpenseOth.setSpotCode(spotCode);
                                    spotExpenseOth.setState(1);
                                    spotExpenseOth.setStuAccTot(0);
                                    spotExpenseOth.setStuOwnTot(tempFee);
                                    this.spotExpenseOthDAO.save(spotExpenseOth);
                                }
                            }

                            beforeStudentCode = studentCode;
                        }
                    }
                }

                //如果中心还有教材没有消耗完，就生成中心的预订单，记录中心的财务信息
                List<PlaceOrderTeachMaterial> placeOrderTeachMaterialList = new ArrayList<PlaceOrderTeachMaterial>();
                for (String name : map.keySet()) {
                    Map<String, Map<Float, Map<Integer, String>>> authorMap = map.get(name);
                    if (null != authorMap) {
                        for (String author : authorMap.keySet()) {
                            Map<Float, Map<Integer, String>> priceMap = authorMap.get(author);
                            if (null != priceMap) {
                                for (float price : priceMap.keySet()) {
                                    Map<Integer, String> countMap = priceMap.get(price);
                                    if (null != countMap) {
                                        for (int count : countMap.keySet()) {
                                            if (count > 0) {
                                                List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);
                                                PlaceOrderTeachMaterial placeOrderTeachMaterial = new PlaceOrderTeachMaterial();
                                                placeOrderTeachMaterial.setTmPrice(price);
                                                placeOrderTeachMaterial.setOperator("管理员");
                                                placeOrderTeachMaterial.setCreator("管理员");
                                                placeOrderTeachMaterial.setCount(Long.parseLong(count + ""));
                                                placeOrderTeachMaterial.setCourseCode(countMap.get(count));
                                                placeOrderTeachMaterial.setTeachMaterialId(teachMaterialList.get(0).getId());
                                                placeOrderTeachMaterialList.add(placeOrderTeachMaterial);
                                                double totalPrice = new BigDecimal(count).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                                                //记录中心消费
                                                SpotExpenseBuy spotExpenseBuy = new SpotExpenseBuy();
                                                spotExpenseBuy.setSpotCode(spotCode);
                                                spotExpenseBuy.setSemesterId(1l);
                                                spotExpenseBuy.setType(SpotExpenseBuy.TYPE_BUY_TM);
                                                spotExpenseBuy.setDetail("购买了" + count + "本，[" + name + "] 教材");
                                                spotExpenseBuy.setMoney(Float.parseFloat(totalPrice + ""));
                                                spotExpenseBuy.setCreator("管理员");
                                                //新增入账记录
                                                spotExpenseBuyDao.save(spotExpenseBuy);

                                                //查询是否存在该中心的费用信息，不存在就新增，存在就修改
                                                SpotExpense spotExpense = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode, 1l);
                                                if (null == spotExpense) {
                                                    spotExpense = new SpotExpense();
                                                    spotExpense.setSemesterId(1l);
                                                    spotExpense.setSpotCode(spotCode);
                                                    spotExpense.setPay(0f);
                                                    spotExpense.setBuy(Float.parseFloat(totalPrice + ""));
                                                    //添加创建人和操作人
                                                    spotExpense.setCreator("管理员");
                                                    spotExpense.setOperator("管理员");
                                                    //执行添加
                                                    findSpotRecordBySpotCodeDao.save(spotExpense);
                                                } else {
                                                    float buy = null == spotExpense.getBuy() ? 0 : spotExpense.getBuy();
                                                    //修改金额
                                                    spotExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                                    //写入操作人
                                                    spotExpense.setOperator("管理员");
                                                    //执行修改
                                                    findSpotRecordBySpotCodeDao.update(spotExpense);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (null != placeOrderTeachMaterialList && 0 < placeOrderTeachMaterialList.size()) {
                    TeachMaterialPlaceOrder teachMaterialPlaceOrder = new TeachMaterialPlaceOrder();
                    teachMaterialPlaceOrder.setCreator("管理员");
                    teachMaterialPlaceOrder.setOperator("管理员");
                    teachMaterialPlaceOrder.setSemesterId(1l);
                    teachMaterialPlaceOrder.setSpotCode(spotCode);

                    //创建订单号
                    String maxOrderCode = placeOrderDAO.queryMaxOrderNumber(spotCode, 1l);
                    if (null == maxOrderCode) {
                        maxOrderCode = "0";
                    } else {
                        maxOrderCode = maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length());
                    }
                    String orderCode = OrderCodeTools.createSpotOrderCode(2015, 0, spotCode, Integer.parseInt(maxOrderCode) + 1);
                    teachMaterialPlaceOrder.setOrderCode(orderCode);
                    teachMaterialPlaceOrder.setIsStock(TeachMaterialPlaceOrder.ISSTOCK_YES);
                    teachMaterialPlaceOrder.setOrderStatus(TeachMaterialPlaceOrder.STATE_SIGN);
                    editPlaceOrderStateByNuDAO.save(teachMaterialPlaceOrder);

                    for (PlaceOrderTeachMaterial placeOrderTeachMaterial : placeOrderTeachMaterialList) {
                        placeOrderTeachMaterial.setOrderId(teachMaterialPlaceOrder.getId());
                        placeOrderTeachMatiralDAO.save(placeOrderTeachMaterial);
                    }
                }


                //批量提交数据
                if (null != addStudentBookOrderList && 0 < addStudentBookOrderList.size()) {
                    batchStudentBookOrderDAO.batchAdd(addStudentBookOrderList, 1000);
                }
                if (null != addStudentBookOrderLogList && 0 < addStudentBookOrderLogList.size()) {
                    batchStudentBookOrderLogDAO.batchAdd(addStudentBookOrderLogList, 1000);
                }
                if (null != addStudentBookOrderTMList && 0 < addStudentBookOrderTMList.size()) {
                    batchStudentBookOrderTMDAO.batchAdd(addStudentBookOrderTMList, 1000);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public String bb(String name, String author, float price, Map<String, Map<String, Map<Float, Map<Integer, String>>>> map){
        String courseCode = "";
        Map<String, Map<Float, Map<Integer, String>>> authorMap = map.get(name);
        if(null != authorMap){
            Map<Float, Map<Integer, String>> priceMap = authorMap.get(author);
            if(null != priceMap){
                Map<Integer, String> countMap = priceMap.get(price);
                if(null != countMap){
                    int count = -1;
                    for(Integer key : countMap.keySet()){
                        courseCode = countMap.get(key);
                        count = key;
                    }
                    if(count < 1){
                        courseCode = "";
                    }else{
                        countMap.put(count-1, courseCode);
                        countMap.remove(count);
                        priceMap.put(price, countMap);
                    }
                }
            }
        }
        return courseCode;
    }



    @Override
    @Transactional
    public void doSync3() {
        List<TeachMaterialPlaceOrder> teachMaterialPlaceOrderList = new ArrayList<TeachMaterialPlaceOrder>();
        List<PlaceOrderTeachMaterial> placeOrderTeachMaterialList = new ArrayList<PlaceOrderTeachMaterial>();
        try{
            List<Object[]> list = spotOrder15DAO.findSpotOrderForHANSHOU();
            String beforeSpotCode = "";
            for(Object[] objs : list){
                String spotCode = objs[0].toString();
                String courseCode = objs[1].toString();
                double price = Double.parseDouble(objs[2].toString());
                String name = objs[3].toString();
                String author = objs[4].toString();
                int count = Integer.parseInt(objs[5].toString());

                List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);


                double totalPrice = 0;

                if (!beforeSpotCode.equals(spotCode)) {
                    TeachMaterialPlaceOrder teachMaterialPlaceOrder = new TeachMaterialPlaceOrder();
                    teachMaterialPlaceOrder.setCreator("管理员");
                    teachMaterialPlaceOrder.setOperator("管理员");
                    teachMaterialPlaceOrder.setCreateTime(DateTools.getLongNowTime());
                    teachMaterialPlaceOrder.setOperateTime(DateTools.getLongNowTime());
                    teachMaterialPlaceOrder.setSemesterId(1l);
                    teachMaterialPlaceOrder.setSpotCode(spotCode);

                    //创建订单号
                    String maxOrderCode = placeOrderDAO.queryMaxOrderNumber(spotCode, 1l);
                    if (null == maxOrderCode) {
                        maxOrderCode = "0";
                    } else {
                        maxOrderCode = maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length());
                    }
                    String orderCode = OrderCodeTools.createSpotOrderCode(2015, 0, spotCode, Integer.parseInt(maxOrderCode) + 1);
                    teachMaterialPlaceOrder.setOrderCode(orderCode);
                    teachMaterialPlaceOrder.setIsStock(TeachMaterialPlaceOrder.ISSTOCK_YES);
                    teachMaterialPlaceOrder.setOrderStatus(TeachMaterialPlaceOrder.STATE_SIGN);
                    editPlaceOrderStateByNuDAO.save(teachMaterialPlaceOrder);
                    teachMaterialPlaceOrderList.add(teachMaterialPlaceOrder);

                    PlaceOrderTeachMaterial placeOrderTeachMaterial = new PlaceOrderTeachMaterial();
                    placeOrderTeachMaterial.setOrderId(teachMaterialPlaceOrder.getId());
                    placeOrderTeachMaterial.setTmPrice(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    placeOrderTeachMaterial.setOperator("管理员");
                    placeOrderTeachMaterial.setCreateTime(DateTools.getLongNowTime());
                    placeOrderTeachMaterial.setOperateTime(DateTools.getLongNowTime());
                    placeOrderTeachMaterial.setCreator("管理员");
                    placeOrderTeachMaterial.setCount(Long.parseLong(count + ""));
                    placeOrderTeachMaterial.setCourseCode(courseCode);
                    placeOrderTeachMaterial.setTeachMaterialId(teachMaterialList.get(0).getId());
                    placeOrderTeachMaterialList.add(placeOrderTeachMaterial);
                    totalPrice = new BigDecimal(count).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    placeOrderTeachMatiralDAO.save(placeOrderTeachMaterial);
                } else {
                    PlaceOrderTeachMaterial placeOrderTeachMaterial = new PlaceOrderTeachMaterial();
                    placeOrderTeachMaterial.setOrderId(teachMaterialPlaceOrderList.get(teachMaterialPlaceOrderList.size() - 1).getId());
                    placeOrderTeachMaterial.setTmPrice(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    placeOrderTeachMaterial.setOperator("管理员");
                    placeOrderTeachMaterial.setCreateTime(DateTools.getLongNowTime());
                    placeOrderTeachMaterial.setOperateTime(DateTools.getLongNowTime());
                    placeOrderTeachMaterial.setCreator("管理员");
                    placeOrderTeachMaterial.setCount(Long.parseLong(count + ""));
                    placeOrderTeachMaterial.setCourseCode(courseCode);
                    placeOrderTeachMaterial.setTeachMaterialId(teachMaterialList.get(0).getId());
                    placeOrderTeachMaterialList.add(placeOrderTeachMaterial);
                    totalPrice = new BigDecimal(count).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    placeOrderTeachMatiralDAO.save(placeOrderTeachMaterial);
                }
                //记录中心消费
                SpotExpenseBuy spotExpenseBuy = new SpotExpenseBuy();
                spotExpenseBuy.setSpotCode(spotCode);
                spotExpenseBuy.setSemesterId(1l);
                spotExpenseBuy.setType(SpotExpenseBuy.TYPE_BUY_TM);
                spotExpenseBuy.setDetail("购买了" + count + "本，[" + name + "] 教材");
                spotExpenseBuy.setMoney(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseBuy.setCreator("管理员");
                //新增入账记录
                spotExpenseBuyDao.save(spotExpenseBuy);

                //查询是否存在该中心的费用信息，不存在就新增，存在就修改
                SpotExpense spotExpense = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode, 1l);
                if (null == spotExpense) {
                    spotExpense = new SpotExpense();
                    spotExpense.setSemesterId(1l);
                    spotExpense.setSpotCode(spotCode);
                    spotExpense.setPay(0f);
                    spotExpense.setBuy(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    //添加创建人和操作人
                    spotExpense.setCreator("管理员");
                    spotExpense.setOperator("管理员");
                    //执行添加
                    findSpotRecordBySpotCodeDao.save(spotExpense);
                } else {
                    float buy = null == spotExpense.getBuy() ? 0 : spotExpense.getBuy();
                    //修改金额
                    spotExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    //写入操作人
                    spotExpense.setOperator("管理员");
                    //执行修改
                    findSpotRecordBySpotCodeDao.update(spotExpense);
                }

                beforeSpotCode = spotCode;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public void doSync4(String spotCode) {
        List<StudentBookOrder> addStudentBookOrderList = new ArrayList<StudentBookOrder>();
        List<StudentBookOrderTM> addStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
        //查询中心的账户欠费
        SpotExpense spotExpense = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode, 1l);
        try{
            if(spotExpense != null && spotExpense.getBuy() > 0) {
                List<String> list = spotOrder15DAO.findStudent2(spotCode);
                int i=0;
                for (String studentCode : list) {
                    System.out.println("i:    "+i);
                    i++;
                    StudentExpense oldStudentExpense = null;
                    StudentExpense oldStudentExpense2 = null;
                    //查询学生
                    Student student = findStudentByCodeDAO.getStudentByCode(studentCode);
                    if (null == student) {
                        throw new BusinessException(studentCode + " 没找到学生");
                    }
                    //查询学生的财务信息
                    StudentExpense studentExpense = findRecordStudentCodeDao.getRecordByStuCode(studentCode, 1l);
                    StudentExpense studentExpense2 = findRecordStudentCodeDao.getRecordByStuCode(studentCode, 2l);
                    if(null == studentExpense){
                        studentExpense = new StudentExpense();
                    }
                    if(null == studentExpense2){
                        studentExpense2 = new StudentExpense();
                    }
                    oldStudentExpense = studentExpense;
                    oldStudentExpense2 = studentExpense2;

                    //得到当前学期最大的订单号
                    int num = 0;
                    StudentBookOrder maxCodeStudentBookOrder = findStudentBookOrderForMaxCodeDAO.getStudentBookOrderForMaxCode(1l);
                    if (null != maxCodeStudentBookOrder) {
                        String maxOrderCode = maxCodeStudentBookOrder.getOrderCode();
                        num = Integer.parseInt(maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length()));
                    }
                    //生成学生订单号
                    String orderCode = OrderCodeTools.createStudentOrderCodeAuto(2015, 0, num + addStudentBookOrderList.size() + 1);
                    //添加订单信息
                    StudentBookOrder studentBookOrder = new StudentBookOrder();
                    studentBookOrder.setSemesterId(1l);
                    studentBookOrder.setIssueChannelId(1l);
                    studentBookOrder.setOrderCode(orderCode);
                    studentBookOrder.setStudentCode(studentCode);
                    studentBookOrder.setState(StudentBookOrder.STATE_SIGN);
                    studentBookOrder.setIsStock(StudentBookOrder.ISSTOCK_YES);
                    studentBookOrder.setIsSpotOrder(StudentBookOrder.ISSPOTORDER_NOT);
                    studentBookOrder.setStudentSign(StudentBookOrder.STUDENTSIGN_NOT);
                    studentBookOrder.setCreator("管理员");
                    studentBookOrder.setOperator("管理员");
                    addStudentBookOrderList.add(studentBookOrder);

                    //查学生购书信息
                    List<Object[]> list2 = spotOrder15DAO.findStudent2ByStudentCode(studentCode);
                    double totalPrice = 0;
                    for(Object[] objs2 : list2){
                        String courseCode = objs2[0].toString();
                        String name = objs2[1].toString();
                        String author = objs2[2].toString();
                        double price = Double.parseDouble(objs2[3].toString());

                        List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);

                        StudentBookOrderTM studentBookOrderTM = new StudentBookOrderTM();
                        studentBookOrderTM.setOrderCode(orderCode);
                        studentBookOrderTM.setCourseCode(courseCode);
                        studentBookOrderTM.setTeachMaterialId(teachMaterialList.get(0).getId());
                        studentBookOrderTM.setPrice(Float.parseFloat(price + ""));
                        studentBookOrderTM.setCount(1);
                        studentBookOrderTM.setOperator("管理员");
                        addStudentBookOrderTMList.add(studentBookOrderTM);

                        //记录学生消费
                        Semester semester = findNowSemesterDAO.get(1l);
                        StudentExpenseBuy studentExpenseBuy = new StudentExpenseBuy();
                        studentExpenseBuy.setStudentCode(studentCode);
                        studentExpenseBuy.setSemester(semester);
                        studentExpenseBuy.setType(StudentExpenseBuy.TYPE_BUY_TM);
                        studentExpenseBuy.setDetail("购买了1本，[" + name + "] 教材");
                        studentExpenseBuy.setMoney(Float.parseFloat(price + ""));
                        studentExpenseBuy.setCreator("管理员");
                        studentExpenseBuyDao.save(studentExpenseBuy);

                        totalPrice = new BigDecimal(totalPrice).add(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    }
                    if(null == studentExpense2 || null == studentExpense2.getPay() || 0 >= studentExpense2.getPay()){
                        float buy = null == studentExpense.getBuy() ? 0 : studentExpense.getBuy();
                        studentExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        //写入操作人
                        studentExpense.setOperator("管理员");
                        if (studentExpense.getBuy() > studentExpense.getPay()) {
                            studentExpense.setClearTime(null);
                            studentExpense.setState(1);
                        }
                        //执行修改
                        findRecordStudentCodeDao.update(studentExpense);

                        //修改中心交费信息
                        List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(1l, spotCode);
                        SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                        spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        float oldBuy = null == oldStudentExpense.getBuy() ? 0 : oldStudentExpense.getBuy();
                        float oldPay = null == oldStudentExpense.getPay() ? 0 : oldStudentExpense.getPay();
                        //以前有欠款
                        if(oldBuy >= oldPay){
                            spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        }else{
                            double temp = new BigDecimal(oldPay).subtract(new BigDecimal(oldBuy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            //如果这次消费大于等于之前余额
                            if(totalPrice >= temp){
                                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(totalPrice)).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }else{
                                spotExpenseOth.setStuAccTot(new BigDecimal(spotExpenseOth.getStuAccTot()).subtract(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }
                        }
                        if(spotExpenseOth.getStuOwnTot() > 0){
                            spotExpenseOth.setClearTime(null);
                            spotExpenseOth.setState(1);
                        }
                        spotExpenseOthDAO.update(spotExpenseOth);
                    }else{
                        float pay = null == oldStudentExpense.getPay() ? 0 : oldStudentExpense.getPay();
                        float buy = null == oldStudentExpense.getBuy() ? 0 : oldStudentExpense.getBuy();
                        float pay2 = null == oldStudentExpense2.getPay() ? 0 : oldStudentExpense2.getPay();
                        float buy2 = null == oldStudentExpense2.getBuy() ? 0 : oldStudentExpense2.getBuy();
                        if(totalPrice >= pay2){
                            studentExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            studentExpense.setPay(new BigDecimal(pay).add(new BigDecimal(pay2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            //写入操作人
                            studentExpense.setOperator("管理员");
                            if (studentExpense.getBuy() > studentExpense.getPay()) {
                                studentExpense.setClearTime(null);
                                studentExpense.setState(1);
                            }
                            //执行修改
                            findRecordStudentCodeDao.update(studentExpense);

                            studentExpense2.setPay(0f);
                            studentExpense2.setOperator("管理员");
                            if (studentExpense2.getBuy() > studentExpense2.getPay()) {
                                studentExpense2.setClearTime(null);
                                studentExpense2.setState(1);
                            }
                            //执行修改
                            findRecordStudentCodeDao.update(studentExpense2);

                            //修改中心交费信息
                            List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(1l, spotCode);
                            List<SpotExpenseOth> spotExpenseOthList2 = spotExpenseOthDAO.querySpotExpenseOthBySemeter(2l, spotCode);
                            SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                            SpotExpenseOth spotExpenseOth2 = spotExpenseOthList2.get(0);

                            spotExpenseOth2.setPay(new BigDecimal(spotExpenseOth2.getPay()).subtract(new BigDecimal(pay2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(pay2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            //15秋
                            if(pay2 >= buy2){
                                //说明以前是有余额
                                double temp = new BigDecimal(pay2).subtract(new BigDecimal(buy2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                spotExpenseOth2.setStuAccTot(new BigDecimal(spotExpenseOth2.getStuAccTot()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }else{
                                //说明以前是有欠款
                                spotExpenseOth2.setStuOwnTot(new BigDecimal(spotExpenseOth2.getStuOwnTot()).add(new BigDecimal(pay2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }
                            if(spotExpenseOth2.getStuOwnTot() > 0){
                                spotExpenseOth2.setClearTime(null);
                                spotExpenseOth2.setState(1);
                            }
                            spotExpenseOthDAO.update(spotExpenseOth2);

                            //15春
                            if(pay != buy){
                                throw new BusinessException(studentCode+" 15春账不平");
                            }else{
                                double temp = new BigDecimal(totalPrice).subtract(new BigDecimal(pay2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                spotExpenseOth.setStuOwnTot(new BigDecimal(spotExpenseOth.getStuOwnTot()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }
                            if(spotExpenseOth.getStuOwnTot() > 0){
                                spotExpenseOth.setClearTime(null);
                                spotExpenseOth.setState(1);
                            }
                            spotExpenseOthDAO.update(spotExpenseOth);
                        }else{
                            studentExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            studentExpense.setPay(new BigDecimal(pay).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            //写入操作人
                            studentExpense.setOperator("管理员");
                            if (studentExpense.getBuy() > studentExpense.getPay()) {
                                studentExpense.setClearTime(null);
                                studentExpense.setState(1);
                            }
                            //执行修改
                            findRecordStudentCodeDao.update(studentExpense);

                            studentExpense2.setPay(new BigDecimal(pay2).subtract(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            studentExpense2.setOperator("管理员");
                            if (studentExpense2.getBuy() > studentExpense2.getPay()) {
                                studentExpense2.setClearTime(null);
                                studentExpense2.setState(1);
                            }
                            //执行修改
                            findRecordStudentCodeDao.update(studentExpense2);

                            //修改中心交费信息
                            List<SpotExpenseOth> spotExpenseOthList = spotExpenseOthDAO.querySpotExpenseOthBySemeter(1l, spotCode);
                            List<SpotExpenseOth> spotExpenseOthList2 = spotExpenseOthDAO.querySpotExpenseOthBySemeter(2l, spotCode);
                            SpotExpenseOth spotExpenseOth = spotExpenseOthList.get(0);
                            SpotExpenseOth spotExpenseOth2 = spotExpenseOthList2.get(0);

                            spotExpenseOth2.setPay(new BigDecimal(spotExpenseOth2.getPay()).subtract(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            spotExpenseOth.setPay(new BigDecimal(spotExpenseOth.getPay()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            spotExpenseOth.setBuy(new BigDecimal(spotExpenseOth.getBuy()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            //15秋 以前有余额
                            if(pay2 >= buy2){
                                double temp = new BigDecimal(pay2).subtract(new BigDecimal(totalPrice)).subtract(new BigDecimal(buy2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                //减去一部分15春的消费后 还有余额
                                if(temp >= 0){
                                    spotExpenseOth2.setStuAccTot(new BigDecimal(spotExpenseOth2.getStuAccTot()).subtract(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                }else{
                                    double temp2 = new BigDecimal(pay2).subtract(new BigDecimal(buy2)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                    spotExpenseOth2.setStuAccTot(new BigDecimal(spotExpenseOth2.getStuAccTot()).subtract(new BigDecimal(temp2)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                    spotExpenseOth2.setStuOwnTot(new BigDecimal(spotExpenseOth2.getStuOwnTot()).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                                }
                            }else{
                                //说明以前是有欠款
                                spotExpenseOth2.setStuOwnTot(new BigDecimal(spotExpenseOth2.getStuOwnTot()).add(new BigDecimal(totalPrice)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                            }
                            if(spotExpenseOth2.getStuOwnTot() > 0){
                                spotExpenseOth2.setClearTime(null);
                                spotExpenseOth2.setState(1);
                            }
                            spotExpenseOthDAO.update(spotExpenseOth2);

                            //15春
                            spotExpenseOthDAO.update(spotExpenseOth);
                        }

                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public void doSync5() {
        List<StudentBookOrderTM> editStudentBookOrderTMList = new ArrayList<StudentBookOrderTM>();
        List<StudentBookOrder> delStudentBookOrderList = new ArrayList<StudentBookOrder>();
        try{
            List<String> studentCodeList = tempDAO.findStudentByMoreOrder(2l);
            int j=0;
            for(String studentCode : studentCodeList){
                System.out.println("j:   " + j);
                j++;
                List<StudentBookOrder> studentBookOrderList = studentBookOrderDao.findByStudentCodeAndSemesterIdForUnconfirmed(studentCode, 2l);
                if(null != studentBookOrderList){
                    int i=0;
                    String orderCode = "";
                    for(StudentBookOrder studentBookOrder : studentBookOrderList){
                        if(i==0){
                            orderCode = studentBookOrder.getOrderCode();
                        }else{
                            //查询订单明细
                            List<StudentBookOrderTM> studentBookOrderTMList = studentBookOrderTmDAO.findStudentBookOrderTMByOrderCode(studentBookOrder.getOrderCode());
                            if(null != studentBookOrderTMList && 0 < studentBookOrderTMList.size()){
                                for(StudentBookOrderTM studentBookOrderTM : studentBookOrderTMList){
                                    studentBookOrderTM.setOrderCode(orderCode);
                                    editStudentBookOrderTMList.add(studentBookOrderTM);
                                }
                            }
                            //delStudentBookOrderList.add(studentBookOrder);
                            studentBookOrderDao.remove(studentBookOrder);
                        }
                        i++;
                    }
                }
            }

            batchStudentBookOrderTMDAO.batchUpdate(editStudentBookOrderTMList, 1000);
            //batchStudentBookOrderDAO.batchDelete(delStudentBookOrderList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void doSync6() {
        try{
            List<Object[]> list = spotOrder15DAO.getT();
            for(Object[] result : list){
                String year = result[5].toString();
                String quarter = result[6].toString();
                String specCode = result[3].toString();
                String levelCode = result[4].toString();
                String spotCode = result[1].toString();
                long id = Long.parseLong(result[0].toString());
                long packageId = Long.parseLong(result[11].toString());
                boolean isAuto = true;
                if (StringUtils.isEmpty(year) && StringUtils.isEmpty(quarter) && StringUtils.isEmpty(specCode) && StringUtils.isEmpty(levelCode)) {
                    isAuto = false;
                }
                //查询订单下的明细
                List<PlaceOrderDetailShow> placeOrderDetailShowList = null;
                List<Object[]> resultList = spotOrder15DAO.getT2(id);
                if(null != resultList && 0 < resultList.size()){
                    placeOrderDetailShowList = new ArrayList<PlaceOrderDetailShow>();
                    for(Object[] objs : resultList){
                        PlaceOrderDetailShow placeOrderDetailShow = new PlaceOrderDetailShow();
                        placeOrderDetailShow.setPlaceOrderDetailId(Long.parseLong(objs[0].toString()));
                        placeOrderDetailShow.setOrderId(Long.parseLong(objs[1].toString()));
                        placeOrderDetailShow.setCourseCode(objs[2].toString());
                        placeOrderDetailShow.setCourseName(objs[3].toString());
                        placeOrderDetailShow.setMaterialId(Long.parseLong(objs[4].toString()));
                        placeOrderDetailShow.setMaterialName(objs[5].toString());
                        placeOrderDetailShow.setMaterialPrice(Float.parseFloat(objs[6].toString()));
                        placeOrderDetailShow.setCount(Integer.parseInt(objs[7].toString()));
                        placeOrderDetailShow.setCreator(objs[8].toString());
                        placeOrderDetailShow.setCreateTime((Date) objs[9]);
                        placeOrderDetailShow.setPackageId(packageId);
                        placeOrderDetailShowList.add(placeOrderDetailShow);
                    }
                }


                if(null != placeOrderDetailShowList && 0 < placeOrderDetailShowList.size()) {
                    String beforeCourseCode = "";
                    String beforeCreator = "";
                    Date beforeCreateTime = null;
                    for(PlaceOrderDetailShow placeOrderDetailShow : placeOrderDetailShowList) {
                        //如果是自动生成的预订单，需要把发出去的教材信息记录到预订单明细里面去
                        if(isAuto){
                            //这里判断如果一门课程关联了多个教材，那么后面的教材需要新增进来；这里做了课程编号排序，所以可以利用下一个课程编号是否与上一个一样
                            if(!beforeCourseCode.equals(placeOrderDetailShow.getCourseCode())){
                                //查询明细
                                PlaceOrderTeachMaterial placeOrderTeachMaterial = placeOrderTeachMatiralDAO.get(placeOrderDetailShow.getPlaceOrderDetailId());
                                if(null != placeOrderTeachMaterial){
                                    placeOrderTeachMaterial.setTeachMaterialId(placeOrderDetailShow.getMaterialId());
                                    placeOrderTeachMaterial.setTmPrice(placeOrderDetailShow.getMaterialPrice());
                                    placeOrderTeachMaterial.setOperator("管理员");
                                    placeOrderTeachMaterial.setOperateTime(DateTools.getLongNowTime());
                                    placeOrderTeachMatiralDAO.update(placeOrderTeachMaterial);
                                }
                            }else {
                                PlaceOrderTeachMaterial placeOrderTeachMaterial = new PlaceOrderTeachMaterial();
                                placeOrderTeachMaterial.setOrderId(placeOrderDetailShow.getOrderId());
                                placeOrderTeachMaterial.setCourseCode(placeOrderDetailShow.getCourseCode());
                                placeOrderTeachMaterial.setTeachMaterialId(placeOrderDetailShow.getMaterialId());
                                placeOrderTeachMaterial.setTmPrice(placeOrderDetailShow.getMaterialPrice());
                                placeOrderTeachMaterial.setCount(Long.parseLong(String.valueOf(placeOrderDetailShow.getCount())));
                                placeOrderTeachMaterial.setCreator(beforeCreator);
                                placeOrderTeachMaterial.setCreateTime(beforeCreateTime);
                                placeOrderTeachMaterial.setOperator("管理员");
                                placeOrderTeachMaterial.setOperateTime(DateTools.getLongNowTime());
                                placeOrderTeachMatiralDAO.save(placeOrderTeachMaterial);

                            }
                            beforeCourseCode = placeOrderDetailShow.getCourseCode();
                            beforeCreator = placeOrderDetailShow.getCreator();
                            beforeCreateTime = placeOrderDetailShow.getCreateTime();
                        }

                        //查询教材信息
                        TeachMaterial teachMaterial = findTeachMaterialService.get(placeOrderDetailShow.getMaterialId());
                        if(null != teachMaterial && teachMaterial.getState() == TeachMaterial.STATE_ENABLE && 0 < placeOrderDetailShow.getCount()) {
                            //记录中心消费信息
                            SpotExpenseBuy spotExpenseBuy = new SpotExpenseBuy();
                            spotExpenseBuy.setSpotCode(spotCode);
                            spotExpenseBuy.setSemesterId(2l);
                            spotExpenseBuy.setType(SpotExpenseBuy.TYPE_BUY_TM);
                            spotExpenseBuy.setDetail("购买了" + placeOrderDetailShow.getCount() + "本，[" + teachMaterial.getName() + "] 教材");
                            spotExpenseBuy.setMoney(placeOrderDetailShow.getMaterialPrice());
                            addSpotExpenseBuyService.addSpotExpenseBuy(spotExpenseBuy, "管理员");

                                TeachMaterialStock teachMaterialStock = findTeachMaterialStockBytmIdAndChannelIdDAO.getTeachMaterialStockBytmIdAndChannelId(teachMaterial.getId(), 1l);
                                if(null != teachMaterialStock) {
                                    teachMaterialStock.setStock(teachMaterialStock.getStock() - placeOrderDetailShow.getCount());
                                    findTeachMaterialStockBytmIdAndChannelIdDAO.update(teachMaterialStock);
                                }

                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
