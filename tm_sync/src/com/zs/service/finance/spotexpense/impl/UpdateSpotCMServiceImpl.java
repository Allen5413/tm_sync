package com.zs.service.finance.spotexpense.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.finance.spotexpense.FindSpotRecordBySpotCodeDao;
import com.zs.dao.finance.spotexpense.SpotExpenseDao;
import com.zs.domain.finance.SpotExpense;
import com.zs.service.finance.spotexpense.UpdateSpotCMService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 进行修改学习中心的待确认金额的接口实现类
 * Created by LihongZhang on 2015/5/22.
 */
@Service("updateSpotCMService")
public class UpdateSpotCMServiceImpl extends EntityServiceImpl<SpotExpense,SpotExpenseDao> implements UpdateSpotCMService {

    @Resource
    private FindSpotRecordBySpotCodeDao findSpotRecordBySpotCodeDao;


    @Override
    @Transactional(value = "transactionManager",rollbackFor = Exception.class)
    public void addConfirmedMoney(Float money, String spotCode, String userName) throws Exception {
//        if ( 0 > money){
//            throw new BusinessException("操作的金额不能为负");
//        }
//        SpotExpense spotExpense = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode);
//        if (null == spotExpense){
//            throw new BusinessException("学习中心不存在");
//        }
//        //修改待确认金额
//        spotExpense.setConfirmedMoney(spotExpense.getConfirmedMoney()+money);
//        //写入操作人
//        spotExpense.setOperator(userName);
//        //创建人和创建时间不能变
//        spotExpense.setCreateTime(spotExpense.getCreateTime());
//        spotExpense.setCreator(spotExpense.getCreator());
//        //设置版本号
//        spotExpense.setVersion(spotExpense.getVersion());
//        //执行修改
//        findSpotRecordBySpotCodeDao.update(spotExpense);
    }


    @Override
    @Transactional(value = "transactionManager",rollbackFor = Exception.class)
    public void subConfirmedMoney(Float money, String spotCode, String userName) throws Exception {
//        if ( 0 > money){
//            throw new BusinessException("操作的金额不能为负");
//        }
//        SpotExpense spotExpense = findSpotRecordBySpotCodeDao.getSpotEBySpotCode(spotCode);
//        if (null == spotExpense){
//            throw new BusinessException("学习中心不存在");
//        }
//        //修改待确认金额
//        spotExpense.setConfirmedMoney(spotExpense.getConfirmedMoney()-money);
//        //写入操作人
//        spotExpense.setOperator(userName);
//        //创建人和创建时间不能变
//        spotExpense.setCreateTime(spotExpense.getCreateTime());
//        spotExpense.setCreator(spotExpense.getCreator());
//        //设置版本号
//        spotExpense.setVersion(spotExpense.getVersion());
//        //执行修改
//        findSpotRecordBySpotCodeDao.update(spotExpense);
    }
}
