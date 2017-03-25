package com.zs.service.finance.spotexpenseoth.impl;

import com.feinno.framework.common.service.EntityServiceImpl;
import com.zs.dao.finance.spotexpenseoth.FindBySpotCodeAndSemesterDAO;
import com.zs.dao.finance.spotexpenseoth.SetSpotExpenseOthDAO;
import com.zs.domain.finance.SpotExpenseOth;
import com.zs.service.finance.spotexpenseoth.SetSpotExpenseOthBySpotCodeService;
import com.zs.tools.DateTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 重新计算一个中心的学生财务统计情况
 * Created by Allen on 2015/12/21.
 */
@Service("setSpotExpenseOthBySpotCodeService")
public class SetSpotExpenseOthBySpotCodeServiceImpl
        extends EntityServiceImpl<SpotExpenseOth, SetSpotExpenseOthDAO> implements SetSpotExpenseOthBySpotCodeService {

    @Resource
    private SetSpotExpenseOthDAO setSpotExpenseOthDAO;
    @Resource
    private FindBySpotCodeAndSemesterDAO findBySpotCodeAndSemesterDAO;

    @Override
    @Transactional
    public void reset(String spotCode) throws Exception {
        List<Object[]> list = setSpotExpenseOthDAO.findCountSetData(spotCode);
        if(null != list && 0 < list.size()){
            for(Object[] objs : list){
                float pay = null == objs[1] ? 0 : Float.parseFloat(objs[1].toString());
                float buy = null == objs[2] ? 0 : Float.parseFloat(objs[2].toString());
                float own = null == objs[3] ? 0 : Float.parseFloat(objs[3].toString());
                float acc = null == objs[4] ? 0 : Float.parseFloat(objs[4].toString());
                long semesterId = Long.parseLong(objs[5].toString());

                //查询现有的记录
                SpotExpenseOth spotExpenseOth = findBySpotCodeAndSemesterDAO.findBySpotCodeAndSemester(spotCode, semesterId);
                if(null == spotExpenseOth){
                    spotExpenseOth = new SpotExpenseOth();
                    spotExpenseOth.setSpotCode(spotCode);
                    spotExpenseOth.setPay(pay);
                    spotExpenseOth.setBuy(buy);
                    spotExpenseOth.setStuAccTot(acc);
                    spotExpenseOth.setStuOwnTot(own);
                    spotExpenseOth.setCreator("管理员");
                    spotExpenseOth.setOperator("管理员");
                    spotExpenseOth.setSemesterId(semesterId);
                    if(pay >= buy && own <= 0) {
                        spotExpenseOth.setState(0);
                        spotExpenseOth.setClearTime(DateTools.getLongNowTime());
                    }else{
                        spotExpenseOth.setState(1);
                    }
                    findBySpotCodeAndSemesterDAO.save(spotExpenseOth);
                }else{
                    spotExpenseOth.setPay(pay);
                    spotExpenseOth.setBuy(buy);
                    spotExpenseOth.setStuAccTot(acc);
                    spotExpenseOth.setStuOwnTot(own);
                    if(pay >= buy && own <= 0) {
                        spotExpenseOth.setState(0);
                        if(null == spotExpenseOth.getClearTime()) {
                            spotExpenseOth.setClearTime(DateTools.getLongNowTime());
                        }
                    }else{
                        spotExpenseOth.setState(1);
                        spotExpenseOth.setClearTime(null);
                    }
                    findBySpotCodeAndSemesterDAO.update(spotExpenseOth);
                }
            }
        }
    }
}
