package com.zs.service.scheduler.impl;

import com.zs.dao.kuaidi.FindNotSignOrderForNuDAO;
import com.zs.dao.kuaidi.push.KuaidiPushDAO;
import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.service.scheduler.CheckNotSignOrderTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Allen on 2015/10/11.
 */
@Service("checkNotSignOrderTaskService")
public class CheckNotSignOrderTaskServiceImpl implements CheckNotSignOrderTaskService {

    @Resource
    private FindNotSignOrderForNuDAO findNotSignOrderForNuDAO;
    @Resource
    private KuaidiPushDAO kuaidiPushDAO;
    @Resource
    private AddPushService addPushService;

    @Override
    public void CheckNotSignOrder() {
        try {
            List<String> nuList = findNotSignOrderForNuDAO.find();
            if (null != nuList && 0 < nuList.size()) {
                for (String nus : nuList) {
                    String[] nuArray = nus.split(",");
                    if(null != nuArray && 0 < nuArray.length){
                        boolean isSign = true;
                        for(String nu : nuArray){
                            //查询该快递号的推送信息
                            KuaidiPush kuaidiPush = kuaidiPushDAO.findByNu(nu);
                            if (null != kuaidiPush) {
                                //如果推送状态为签收，就修改订单状态
                                if (3 != kuaidiPush.getState()) {
                                    isSign = false;
                                }
                            }else{
                                isSign = false;
                            }
                        }
                        if(isSign){
                            addPushService.editOrderState(nus);
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
