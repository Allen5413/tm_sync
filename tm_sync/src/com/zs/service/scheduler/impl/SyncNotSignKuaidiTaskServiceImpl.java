package com.zs.service.scheduler.impl;

import com.alibaba.fastjson.JSONObject;
import com.zs.dao.kuaidi.FindNotSignNuDAO;
import com.zs.dao.kuaidi.push.KuaidiPushDAO;
import com.zs.dao.kuaidi.request.KuaidiRequestDAO;
import com.zs.domain.kuaidi.KuaidiPush;
import com.zs.domain.kuaidi.KuaidiRequest;
import com.zs.service.kuaidi.push.AddPushService;
import com.zs.service.kuaidi.request.AddReqService;
import com.zs.service.scheduler.SyncNotSignKuaidiTaskService;
import com.zs.tools.ApiTools;
import com.zs.tools.HttpRequestTools;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 同步还没有签收的快递信息
 * Created by Allen on 2015/10/9.
 */
@Service("syncNotSignKuaidiTaskService")
public class SyncNotSignKuaidiTaskServiceImpl implements SyncNotSignKuaidiTaskService {

    @Resource
    private FindNotSignNuDAO findNotSignNuDAO;
    @Resource
    private KuaidiRequestDAO kuaidiRequestDAO;
    @Resource
    private AddReqService addReqService;
    @Resource
    private KuaidiPushDAO kuaidiPushDAO;
    @Resource
    private AddPushService addPushService;

    @Override
    public void syncKuaidi() {
        int i = 1;
        try {
            //查询当前学期还没有签收的快递单号
            List<String> logisticCodeList = findNotSignNuDAO.findNotSignNu();
            if (null != logisticCodeList && 0 < logisticCodeList.size()) {
                for (String logisticCode : logisticCodeList) {
                    String[] nus = logisticCode.split(",");
                    if (null != nus) {
                        for (String nu : nus) {
                            //调用接口，查询快递信息
                            JSONObject json = ApiTools.getKuaiDi(nu);
                            //记录请求数据
                            addReqService.add("tiantian", nu, null == json.get("msg") ? "null" : json.get("msg").toString(), null == json.get("status") ? "null" : json.get("status").toString());
                            //更新快递信息



                        }
                    }
                }
            }

            //检查描述信息有“已签收”的，但是状态不是3的推送信息
            List<KuaidiPush> kuaidiPushList = kuaidiPushDAO.findByDataAndState();
            if(null != kuaidiPushList && 0 < kuaidiPushList.size()){
                for(KuaidiPush kuaidiPush : kuaidiPushList){
                    String nu = kuaidiPush.getNu();
                    if(!StringUtils.isEmpty(nu)){
                        addPushService.editOrderState(nu);
                    }
                    kuaidiPush.setSign(KuaidiPush.SIGN_YES);
                    kuaidiPushDAO.update(kuaidiPush);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
