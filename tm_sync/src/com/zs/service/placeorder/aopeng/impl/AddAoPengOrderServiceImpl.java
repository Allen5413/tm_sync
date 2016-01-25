package com.zs.service.placeorder.aopeng.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.basic.teachmaterial.FindTeachMaterialByNameAndAuthorDAO;
import com.zs.dao.finance.spotexpense.FindSpotRecordBySpotCodeDao;
import com.zs.dao.finance.spotexpensebuy.SpotExpenseBuyDao;
import com.zs.dao.placeorder.aopeng.FindAoPengTeachMaterialDAO;
import com.zs.dao.placeorder.placeorder.EditPlaceOrderStateByNuDAO;
import com.zs.dao.placeorder.placeorder.PlaceOrderDAO;
import com.zs.dao.placeorder.placeorderteachmatiral.PlaceOrderTeachMatiralDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.basic.TeachMaterial;
import com.zs.domain.finance.SpotExpense;
import com.zs.domain.finance.SpotExpenseBuy;
import com.zs.domain.placeorder.PlaceOrderTeachMaterial;
import com.zs.domain.placeorder.TeachMaterialPlaceOrder;
import com.zs.service.placeorder.aopeng.AddAoPengOrderService;
import com.zs.tools.DateTools;
import com.zs.tools.OrderCodeTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Allen on 2016/1/21.
 */
@Service("addAoPengOrderService")
public class AddAoPengOrderServiceImpl extends EntityServiceImpl<TeachMaterialPlaceOrder, EditPlaceOrderStateByNuDAO>
        implements AddAoPengOrderService{

    @Resource
    private FindAoPengTeachMaterialDAO findAoPengTeachMaterialDAO;
    @Resource
    private FindTeachMaterialByNameAndAuthorDAO findTeachMaterialByNameAndAuthorDAO;
    @Resource
    private PlaceOrderDAO placeOrderDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;
    @Resource
    private PlaceOrderTeachMatiralDAO placeOrderTeachMatiralDAO;
    @Resource
    private EditPlaceOrderStateByNuDAO editPlaceOrderStateByNuDAO;
    @Resource
    private FindSpotRecordBySpotCodeDao findSpotRecordBySpotCodeDao;
    @Resource
    private SpotExpenseBuyDao spotExpenseBuyDao;

    @Override
    @Transactional
    public void add(long semesterId) throws Exception {
        Semester semester = findNowSemesterDAO.get(semesterId);

        //查询奥鹏订购教材数据
        List<Object[]> aopengList = findAoPengTeachMaterialDAO.find();
        if(null != aopengList && 0 < aopengList.size()){
            String beforeSpotCode = "";
            long beforeOrderId = 0;
            for(int i=0; i < aopengList.size(); i++){
                Object[] objs = aopengList.get(i);
                String spotCode = objs[0].toString();
                String courseCode = objs[1].toString();
                String name = objs[2].toString();
                String author = objs[3].toString();
                double price = Double.parseDouble(objs[4].toString());
                long count = Long.parseLong(objs[5].toString());

                //判断是否跟上一条记录的学习中心一样，一样就添加明细，不一样要先添加订单，再添加明细
                TeachMaterialPlaceOrder teachMaterialPlaceOrder = new TeachMaterialPlaceOrder();
                if(!beforeSpotCode.equals(spotCode)) {
                    //创建订单号
                    String maxOrderCode = placeOrderDAO.queryMaxOrderNumber(spotCode, semesterId);
                    if (null == maxOrderCode) {
                        maxOrderCode = "0";
                    } else {
                        maxOrderCode = maxOrderCode.substring(maxOrderCode.length() - 6, maxOrderCode.length());
                    }
                    String orderCode = OrderCodeTools.createSpotOrderCode(semester.getYear(), semester.getQuarter(), spotCode, Integer.parseInt(maxOrderCode) + 1);

                    //存储预订单数据
                    teachMaterialPlaceOrder.setCreateTime(DateTools.getLongNowTime());
                    teachMaterialPlaceOrder.setCreator("管理员");
                    teachMaterialPlaceOrder.setOperator("管理员");
                    teachMaterialPlaceOrder.setOperateTime(DateTools.getLongNowTime());
                    teachMaterialPlaceOrder.setOrderCode(orderCode);
                    teachMaterialPlaceOrder.setOrderStatus(TeachMaterialPlaceOrder.STATE_SIGN);
                    teachMaterialPlaceOrder.setSemesterId(semesterId);
                    teachMaterialPlaceOrder.setSpotCode(spotCode);
                    teachMaterialPlaceOrder.setIsStock(TeachMaterialPlaceOrder.ISSTOCK_YES);
                    teachMaterialPlaceOrder = editPlaceOrderStateByNuDAO.save(teachMaterialPlaceOrder);
                    beforeOrderId = teachMaterialPlaceOrder.getId();
                }

                beforeSpotCode = spotCode;

                //根据名称作者，查询教材
                long tmId = 0;
                List<TeachMaterial> teachMaterialList = findTeachMaterialByNameAndAuthorDAO.find(name, author);
                if(null != teachMaterialList && 0 < teachMaterialList.size()){
                    tmId = teachMaterialList.get(0).getId();
                }

                //记录订单明细
                PlaceOrderTeachMaterial placeOrderTeachMaterial = new PlaceOrderTeachMaterial();
                placeOrderTeachMaterial.setCount(count);
                placeOrderTeachMaterial.setCourseCode(courseCode);
                placeOrderTeachMaterial.setCreateTime(DateTools.getLongNowTime());
                placeOrderTeachMaterial.setCreator("管理员");
                placeOrderTeachMaterial.setOrderId(beforeOrderId);
                placeOrderTeachMaterial.setOperator("管理员");
                placeOrderTeachMaterial.setOperateTime(DateTools.getLongNowTime());
                placeOrderTeachMaterial.setTeachMaterialId(tmId);
                placeOrderTeachMaterial.setTmPrice(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                placeOrderTeachMaterial.setIsSend(PlaceOrderTeachMaterial.IS_SEND_YES);
                placeOrderTeachMatiralDAO.save(placeOrderTeachMaterial);

                //记录中心消费信息
                SpotExpenseBuy spotExpenseBuy = new SpotExpenseBuy();
                spotExpenseBuy.setSpotCode(spotCode);
                spotExpenseBuy.setSemesterId(semesterId);
                spotExpenseBuy.setType(SpotExpenseBuy.TYPE_BUY_TM);
                spotExpenseBuy.setDetail("购买了" + count + "本，[" + name + "] 教材");
                spotExpenseBuy.setMoney(new BigDecimal(count).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpenseBuy.setTeachMaterialId(tmId);
                spotExpenseBuy.setCreator("管理员");
                //新增入账记录
                spotExpenseBuyDao.save(spotExpenseBuy);

                //记录学习中心每学期的费用信息
                //查询是否存在该中心的费用信息，不存在就新增，存在就修改
                List<SpotExpense> spotExpenseList = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode);
                if(null == spotExpenseList || 1 > spotExpenseList.size()){
                    SpotExpense spotExpense = new SpotExpense();
                    spotExpense.setSpotCode(spotCode);
                    spotExpense.setBuy(spotExpenseBuy.getMoney());
                    spotExpense.setPay(0f);
                    spotExpense.setCreator("管理员");
                    spotExpense.setOperator("管理员");
                    spotExpense.setSemesterId(spotExpenseBuy.getSemesterId());
                    spotExpense.setState(SpotExpense.STATE_NO);
                    if(spotCode.equals("9989")){
                        spotExpense.setDiscount(100);
                    }else{
                        spotExpense.setDiscount(95);
                    }
                    findSpotRecordBySpotCodeDao.save(spotExpense);
                }else {
                    SpotExpense spotExpense = spotExpenseList.get(0);
                    spotExpense.setBuy(new BigDecimal(spotExpense.getBuy()).add(new BigDecimal(spotExpenseBuy.getMoney())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    spotExpense.setOperator("管理员");
                    spotExpense.setOperateTime(DateTools.getLongNowTime());
                    findSpotRecordBySpotCodeDao.update(spotExpense);
                }
            }
        }
    }
}
