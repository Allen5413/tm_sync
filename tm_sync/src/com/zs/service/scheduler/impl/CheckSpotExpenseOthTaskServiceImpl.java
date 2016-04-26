package com.zs.service.scheduler.impl;

import com.zs.dao.sync.SpotDAO;
import com.zs.domain.sync.Spot;
import com.zs.service.finance.spotexpenseoth.SetSpotExpenseOthBySpotCodeService;
import com.zs.service.scheduler.CheckSpotExpenseOthTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 每天都重置一下每个学习中心学生费用情况
 * Created by Allen on 2016/4/26 0026.
 */
@Service("checkSpotExpenseOthTaskService")
public class CheckSpotExpenseOthTaskServiceImpl implements CheckSpotExpenseOthTaskService {

    @Resource
    private SpotDAO spotDAO;
    @Resource
    private SetSpotExpenseOthBySpotCodeService setSpotExpenseOthBySpotCodeService;

    @Override
    public void checkSpotExpenseOthTask() {
        try {
            //查询所有的学习中心
            List<Spot> spotList = spotDAO.getAll();
            for(Spot spot : spotList){
                //设置学习中心费用
                setSpotExpenseOthBySpotCodeService.reset(spot.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
