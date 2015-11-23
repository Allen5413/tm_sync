package com.zs.service.finance.spotexpense.impl;

import com.feinno.framework.common.exception.BusinessException;
import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.basic.semester.FindNowSemesterDAO;
import com.zs.dao.finance.spotexpense.FindSpotRecordBySpotCodeDao;
import com.zs.dao.finance.spotexpense.SpotExpenseDao;
import com.zs.dao.sync.spot.FindSpotByCodeDAO;
import com.zs.domain.basic.Semester;
import com.zs.domain.finance.SpotExpense;
import com.zs.domain.finance.SpotExpenseBuy;
import com.zs.domain.sync.Spot;
import com.zs.service.finance.spotexpense.AddSpotEBuyService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Allen on 2015/8/3.
 */
@Service("addSpotEBuyService")
public class AddSpotEBuyServiceImpl extends EntityServiceImpl<SpotExpense,SpotExpenseDao> implements AddSpotEBuyService {

    @Resource
    private FindSpotRecordBySpotCodeDao findSpotRecordBySpotCodeDao;
    @Resource
    private FindSpotByCodeDAO findSpotByCodeDAO;
    @Resource
    private FindNowSemesterDAO findNowSemesterDAO;

    @Override
    @Transactional(value = "transactionManager", rollbackFor = Exception.class)
    public void addSpotEBuy(SpotExpenseBuy spotExpenseBuy,String userName) throws Exception {
        Date operateTime = DateTools.getLongNowTime();
        //获取当前学期
        Semester semester = findNowSemesterDAO.getNowSemester();
        String spotCode = spotExpenseBuy.getSpotCode();
        //验证学习中心是否存在
        Spot spot = findSpotByCodeDAO.getSpotByCode(spotCode);
        if (null == spot){
            throw new BusinessException("学习中心："+spotCode+", 不存在");
        }
        //查询是否存在该中心的费用信息，不存在就新增，存在就修改
        List<SpotExpense> spotExpenseList = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode);
        if(null == spotExpenseList || 1 > spotExpenseList.size()){
            SpotExpense spotExpense = new SpotExpense();
            spotExpense.setSpotCode(spotCode);
            spotExpense.setBuy(spotExpenseBuy.getMoney());
            spotExpense.setPay(0f);
            spotExpense.setCreator(userName);
            spotExpense.setOperator(userName);
            spotExpense.setSemesterId(semester.getId());
            spotExpense.setState(SpotExpense.STATE_NO);
            findSpotRecordBySpotCodeDao.save(spotExpense);
        }else{
            //当前消费的钱
            double tempMoney = spotExpenseBuy.getMoney();
            //每学期多的钱
            double moreMoney = 0;
            //是否当前学期
            boolean isNowSemester = false;
            for (SpotExpense spotExpense : spotExpenseList) {
                float pay = null == spotExpense.getPay() ? 0 : spotExpense.getPay();
                float buy = null == spotExpense.getBuy() ? 0 : spotExpense.getBuy();
                //是否当前学期
                if (semester.getId() == spotExpense.getSemesterId()) {
                    isNowSemester = true;
                    spotExpense.setPay(new BigDecimal(moreMoney).add(new BigDecimal(pay)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    spotExpense.setBuy(new BigDecimal(buy).add(new BigDecimal(tempMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                    if(spotExpense.getPay() >= spotExpense.getBuy()){
                        spotExpense.setState(spotExpense.STATE_YES);
                        spotExpense.setClearTime(operateTime);
                    }else{
                        spotExpense.setState(spotExpense.STATE_NO);
                    }
                    spotExpense.setOperator(userName);
                    spotExpense.setOperateTime(operateTime);
                    findSpotRecordBySpotCodeDao.update(spotExpense);
                } else {
                    double temp = new BigDecimal(pay).subtract(new BigDecimal(buy)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    //如果以前学期有多余的缴费，就存到最新的学期里，减去以前的缴费
                    if(temp > 0){
                        moreMoney = new BigDecimal(moreMoney).add(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        spotExpense.setPay(new BigDecimal(spotExpense.getPay()).subtract(new BigDecimal(temp)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                        findSpotRecordBySpotCodeDao.update(spotExpense);
                    }
                }
            }
            if (!isNowSemester) {
                //新增费用记录
                SpotExpense spotExpense = new SpotExpense();
                spotExpense.setSpotCode(spotCode);
                spotExpense.setPay(new BigDecimal(moreMoney).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
                spotExpense.setBuy(spotExpenseBuy.getMoney());
                if(moreMoney >= spotExpenseBuy.getMoney()){
                    spotExpense.setState(SpotExpense.STATE_YES);
                    spotExpense.setClearTime(operateTime);
                }else{
                    spotExpense.setState(SpotExpense.STATE_NO);
                }
                spotExpense.setCreator(userName);
                spotExpense.setOperator(userName);
                spotExpense.setSemesterId(semester.getId());
                //添加
                findSpotRecordBySpotCodeDao.save(spotExpense);
            }
        }
    }
}
